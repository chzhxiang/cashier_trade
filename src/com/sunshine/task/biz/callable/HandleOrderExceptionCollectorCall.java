/**
 * <html>
 * <body>
 *  <P> Copyright 2017 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年10月11日</p>
 *  <p> Created by 党参</p>
 *  </body>
 * </html>
 */
package com.sunshine.task.biz.callable;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunshine.common.GlobalConstant;
import com.sunshine.common.datas.cache.component.PaymentExceptionCache;
import com.sunshine.framework.cache.redis.RedisLock;
import com.sunshine.framework.cache.redis.RedisService;
import com.sunshine.framework.common.spring.ext.SpringContextHolder;
import com.sunshine.mobileapp.order.entity.ExceptionTradeOrder;
import com.sunshine.mobileapp.order.entity.TradeOrder;
import com.sunshine.mobileapp.order.service.impl.TradeOrderServiceImpl;
import com.sunshine.payment.TradeConstant;
import com.sunshine.payment.TradeUtils;
import com.sunshine.platform.application.entity.MerchantApplication;
import com.sunshine.platform.applicationchannel.entity.ApplicationChannel;
import com.sunshine.platform.applicationchannel.service.ApplicationChannelService;
import com.sunshine.task.biz.taskitem.HandleOrderExceptionTask;
import com.sunshine.thread.ExecuteNotifyUrlRunnable;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.task.biz.callable
 * @ClassName: HandleOrderExceptionCollectorCall
 * @Description: <p>支付订单处理</p>
 * @JDK version used: 
 * @Author: 党参
 * @Create Date: 2017年10月11日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class HandleOrderExceptionCollectorCall implements Callable<Object> {

	public static Logger logger = LoggerFactory.getLogger(HandleOrderExceptionCollectorCall.class);

	private PaymentExceptionCache exceptionCache = SpringContextHolder.getBean(PaymentExceptionCache.class);
	private TradeOrderServiceImpl tradeOrderService = SpringContextHolder.getBean(TradeOrderServiceImpl.class);
	private ApplicationChannelService channelService = SpringContextHolder.getBean(ApplicationChannelService.class);

	/**
	 * 任务参数对象
	 */
	private MerchantApplication application;

	public static final Charset COLLECT_CHARSET = Charset.forName("UTF-8");

	public HandleOrderExceptionCollectorCall(MerchantApplication application) {
		super();
		this.application = application;
	}

	@Override
	public Object call() throws Exception {
		// TODO Auto-generated method stub
		ExceptionTradeOrder order = null;
		TradeOrder tradeOrder = null;
		try {
			order = exceptionCache.getExceptionPayTradeOrderFromCache(application.getAppId());
			if (order == null) {
				HandleOrderExceptionTask.logger.info("商户应用 :{} ,未找到支付异常单.", application.getAppName());
				return null;
			} else {
				tradeOrder = tradeOrderService.getTradeOrderByAppIdAndOutTradeNo(order.getAppId(), order.getOutTradeNo());
				HandleOrderExceptionTask.logger.info("查询到支付异常订单,商户名称:{}, 商户订单号:{}, 平台订单号:{}, 支付状态:{}, 申请时间;{}",
						new Object[] { tradeOrder.getMerchantName(),
								tradeOrder.getOutTradeNo(),
								tradeOrder.getTradeNo(),
								tradeOrder.getTradeStatus(),
								GlobalConstant.formatYYYYMMDDHHMMSS(tradeOrder.getPayApplyTime()) });
			}

			String lockKey = order.getTradeNo();
			boolean isLock = false;
			RedisService redisService = SpringContextHolder.getBean(RedisService.class);
			RedisLock redisLock = new RedisLock(redisService.getRedisPool());

			try {
				do {
					//300000 毫秒数 5分钟
					isLock = redisLock.singleLock(lockKey, GlobalConstant.REDIS_PAY_LOCKED_TIME);
				} while (!isLock);

				//处理异常数据
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("tradeNo", lockKey);
				tradeOrder = tradeOrderService.getTradeOrderByAppIdAndOutTradeNo(params);

				if (tradeOrder.getHandleCount() == null) {
					tradeOrder.setHandleCount(0);
				}

				StringBuffer content = new StringBuffer("支付异常订单处理,");
				if (TradeConstant.ORDER_SUCCESS_FALSE == tradeOrder.getIsSuccess() && tradeOrder.getHandleCount() < 3) {
					tradeOrder.setHandleCount(tradeOrder.getHandleCount() + 1);

					ApplicationChannel channel =
							channelService.findTradeChannelByAppIdChannelCode(tradeOrder.getAppId(), tradeOrder.getChannelCode());
					Map<String, Object> retMap = TradeUtils.paymentQuery(tradeOrder, channel.getParamsJson());
					Boolean isException = (Boolean) retMap.get(GlobalConstant.TRADE_IS_EXCEPTION);
					Boolean isSuccess = (Boolean) retMap.get(GlobalConstant.TRADE_IS_SUCCESS);
					if (isException) {
						tradeOrder.setIsException(TradeConstant.ORDER_EXCEPTION_TRUE);
						content.append("处理发生异常，原因:").append(retMap.get(GlobalConstant.TRADE_FAIL_MSG));
					} else {
						tradeOrder.setIsException(TradeConstant.ORDER_EXCEPTION_FALSE);
						if (isSuccess) {
							String agtTradeNo = retMap.get(GlobalConstant.TRADE_SUCCESS_AGTPAYNO).toString();
							tradeOrder.setAgtTradeNo(agtTradeNo);
							tradeOrder.setPayTime(GlobalConstant.parseYYYYMMDDHHMMSS(retMap.get(GlobalConstant.TRADE_DATE).toString()));
							tradeOrder.setIsPayQuery(TradeConstant.ORDER_PAY_QUERY_TRUE);
							tradeOrder.setIsSuccess(TradeConstant.ORDER_SUCCESS_TRUE);
							int tradeStatus = Integer.parseInt(retMap.get(GlobalConstant.TRADE_ORDER_STATE).toString());
							tradeOrder.setTradeStatus(tradeStatus);
							tradeOrder.setPayQueryJson(retMap.get(GlobalConstant.TRADE_SUCCESS_DATA).toString());
							content.append("处理成功，订单状态:").append(tradeStatus).append(",第三方支付订单号:").append(agtTradeNo);
							if (TradeConstant.TRADE_STATUS_PAYMENT == tradeStatus) {//如果订单未支付中，则通知商户
								new Thread(new ExecuteNotifyUrlRunnable(tradeOrder)).start();
							}
						} else {
							content.append("处理失败，失败原因:").append(retMap.get(GlobalConstant.TRADE_FAIL_MSG));
						}
					}

					//处理失败  添加到队列尾部  下次处理
					exceptionCache.setExceptionPayTradeOrderToCache(tradeOrder.convertExceptionObj());

					tradeOrder.formatHandleLog(content.toString());
					tradeOrderService.updateTradeOrderForDbAndCache(tradeOrder);

				} else {
					content.append("处理失败且处理次数超过三次，从异常队列移除，商户订单号:").append(tradeOrder.getOutTradeNo()).append(", 平台订单号:")
							.append(tradeOrder.getTradeNo());
					exceptionCache.removeExceptionPayTradeOrderFromCache(tradeOrder.getMerchantNo());
					logger.info("商户订单:{},平台订单:{}, 已经由其它逻辑处理,从异常队列移除.", tradeOrder.getOutTradeNo(), tradeOrder.getTradeNo());
				}

			} finally {
				redisLock.singleUnlock(lockKey);
			}

			//更新挂号缓存中挂号记录的信息
			HandleOrderExceptionTask.logger.info("HandleExceptionCollectCall handleRegisterException end .订单号:{} , 更新状态-> payStatus:{}",
					new Object[] { order.getTradeNo(), order.getTradeStatus() });

		} catch (Exception e) {
			e.printStackTrace();
			HandleOrderExceptionTask.logger.error("订单号:{} , exec HandlePayOrderExceptionTask is execption. msg:{}",
					new Object[] { order.getTradeNo(), e.getMessage() });
		}

		return null;
	}

}

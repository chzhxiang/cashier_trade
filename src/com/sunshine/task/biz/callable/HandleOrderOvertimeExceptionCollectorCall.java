/**
 * <html>
 * <body>
 *  <P> Copyright 2017 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年10月19日</p>
 *  <p> Created by 党参</p>
 *  </body>
 * </html>
 */
package com.sunshine.task.biz.callable;

import java.util.Map;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunshine.common.GlobalConstant;
import com.sunshine.common.datas.cache.component.PaymentOvertimeExceptionCache;
import com.sunshine.framework.cache.redis.RedisLock;
import com.sunshine.framework.cache.redis.RedisService;
import com.sunshine.framework.common.spring.ext.SpringContextHolder;
import com.sunshine.mobileapp.order.entity.ExceptionTradeOrder;
import com.sunshine.mobileapp.order.entity.TradeOrder;
import com.sunshine.mobileapp.order.service.TradeOrderService;
import com.sunshine.payment.TradeConstant;
import com.sunshine.payment.TradeUtils;
import com.sunshine.platform.application.entity.MerchantApplication;
import com.sunshine.platform.applicationchannel.entity.ApplicationChannel;
import com.sunshine.platform.applicationchannel.service.ApplicationChannelService;
import com.sunshine.task.biz.taskitem.HandleOrderOvertimeExceptionTask;
import com.sunshine.thread.ExecuteNotifyUrlRunnable;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.task.biz.callable
 * @ClassName: HandleOrderOvertimeExceptionCollectorCall
 * @Description: <p>超时订单处理</p>
 * @JDK version used: 
 * @Author: 党参
 * @Create Date: 2017年10月19日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class HandleOrderOvertimeExceptionCollectorCall implements Callable {
	public static Logger logger = LoggerFactory.getLogger(HandleOrderOvertimeExceptionCollectorCall.class);

	private PaymentOvertimeExceptionCache overtimeExceptionCache = SpringContextHolder.getBean(PaymentOvertimeExceptionCache.class);
	private TradeOrderService tradeOrderService = SpringContextHolder.getBean(TradeOrderService.class);
	private ApplicationChannelService channelService = SpringContextHolder.getBean(ApplicationChannelService.class);

	private MerchantApplication application;

	public HandleOrderOvertimeExceptionCollectorCall(MerchantApplication application) {
		super();
		this.application = application;
	}

	@Override
	public Object call() throws Exception {
		TradeOrder tradeOrder = null;
		try {
			ExceptionTradeOrder exceptionTradeOrder = overtimeExceptionCache.getExceptionPayTradeOrderFromCache(application.getAppId());
			if (exceptionTradeOrder == null) {
				HandleOrderOvertimeExceptionTask.logger.info("未找到支付超时异常订单,商户应用:{}", application.getAppName());
				return null;
			} else {
				tradeOrder = tradeOrderService.getTradeOrderByAppIdAndOutTradeNo(exceptionTradeOrder.getAppId(), exceptionTradeOrder.getOutTradeNo());
				HandleOrderOvertimeExceptionTask.logger.info("查询到支付超时异常订单,商户名称:{}, 商户订单号:{}, 平台订单号:{}, 支付状态:{}, 申请时间;{}",
						new Object[] { tradeOrder.getMerchantName(),
								tradeOrder.getOutTradeNo(),
								tradeOrder.getTradeNo(),
								tradeOrder.getTradeStatus(),
								GlobalConstant.formatYYYYMMDDHHMMSS(tradeOrder.getPayApplyTime()) });
			}

			String lockKey = exceptionTradeOrder.getTradeNo();
			boolean isLock = false;
			RedisService redisService = SpringContextHolder.getBean(RedisService.class);
			RedisLock redisLock = new RedisLock(redisService.getRedisPool());

			try {
				do {
					//300000 毫秒数 5分钟
					isLock = redisLock.singleLock(lockKey, GlobalConstant.REDIS_PAY_LOCKED_TIME);
				} while (!isLock);

				if (exceptionTradeOrder.getHandleCount() == null) {
					tradeOrder.setHandleCount(0);
				}
				StringBuffer content = new StringBuffer("(订单超时轮询)支付超时异常订单处理,");
				//如果依然是支付中、未处理成功状态，则查询订单是否支付成功
				if (TradeConstant.TRADE_STATUS_PAYMENTING == tradeOrder.getTradeStatus()
						&& TradeConstant.ORDER_SUCCESS_FALSE == tradeOrder.getIsSuccess() && tradeOrder.getHandleCount() < 3) {
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
							int tradeStatus = Integer.parseInt(retMap.get(GlobalConstant.TRADE_ORDER_STATE).toString());
							tradeOrder.setTradeStatus(tradeStatus);
							tradeOrder.setIsPayQuery(TradeConstant.ORDER_PAY_QUERY_TRUE);
							tradeOrder.setIsSuccess(TradeConstant.ORDER_SUCCESS_TRUE);
							if (TradeConstant.TRADE_STATUS_PAYMENT == tradeStatus) {//如果订单已支付，则通知商户
								String agtTradeNo = retMap.get(GlobalConstant.TRADE_SUCCESS_AGTPAYNO).toString();
								tradeOrder.setAgtTradeNo(agtTradeNo);
								tradeOrder.setPayTime(GlobalConstant.parseYYYYMMDDHHMMSS(retMap.get(GlobalConstant.TRADE_DATE).toString()));
								content.append("处理成功，订单状态:").append(tradeStatus).append(",第三方支付订单号:").append(agtTradeNo);
								new Thread(new ExecuteNotifyUrlRunnable(tradeOrder)).start();
							} else {
								logger.info("其他状态暂时不做处理");
								content.append("查询到状态为:").append(tradeStatus).append(",暂时不做处理");
							}
							if (retMap.containsKey(GlobalConstant.TRADE_SUCCESS_DATA)) {
								tradeOrder.setPayQueryJson(retMap.get(GlobalConstant.TRADE_SUCCESS_DATA).toString());
							}
							tradeOrder.setIsException(TradeConstant.ORDER_EXCEPTION_FALSE);
						} else {
							if (retMap.containsKey(GlobalConstant.TRADE_ORDER_STATE)
									&& TradeConstant.TRADE_STATUS_NOT_EXIST == Integer.parseInt(retMap.get(GlobalConstant.TRADE_ORDER_STATE)
											.toString())) {//订单不存在
								tradeOrder.setTradeStatus(TradeConstant.TRADE_STATUS_NO_PAYMENT);
								tradeOrder.setIsSuccess(TradeConstant.ORDER_SUCCESS_TRUE);
								//订单不存在，移出队列
								overtimeExceptionCache.removeExceptionPayTradeOrderFromCache(application.getAppId());
								content.append("订单不存在，移出队列，商户订单号:").append(tradeOrder.getOutTradeNo()).append("平台订单号")
										.append(tradeOrder.getTradeNo());
								tradeOrder.setIsException(TradeConstant.ORDER_EXCEPTION_FALSE);
							} else {
								content.append("处理失败，失败原因:").append(retMap.get(GlobalConstant.TRADE_FAIL_MSG));
							}
						}
					}

				} else {
					content.append("处理失败且处理次数超过三次，从异常队列移除，商户订单号:").append(tradeOrder.getOutTradeNo()).append(", 平台订单号:")
							.append(tradeOrder.getTradeNo());
					overtimeExceptionCache.removeExceptionPayTradeOrderFromCache(application.getAppId());
					logger.info("商户订单:{},平台订单:{}, 已经由其它逻辑处理,从异常队列移除.", tradeOrder.getOutTradeNo(), tradeOrder.getTradeNo());
				}
				//处理失败  添加到队列尾部  下次处理
				if (TradeConstant.ORDER_SUCCESS_FALSE == tradeOrder.getIsSuccess() && tradeOrder.getHandleCount() < 3) {
					overtimeExceptionCache.setExceptionPayTradeOrderToCache(tradeOrder.convertExceptionObj());
				}
				tradeOrder.formatHandleLog(content.toString());
				tradeOrderService.updateTradeOrderForDbAndCache(tradeOrder);
			} finally {
				redisLock.singleUnlock(lockKey);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

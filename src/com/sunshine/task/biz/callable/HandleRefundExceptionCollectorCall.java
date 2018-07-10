/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年10月19日</p>
 *  <p> Created by hqy</p>
 *  </body>
 * </html>
 */
package com.sunshine.task.biz.callable;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunshine.common.GlobalConstant;
import com.sunshine.common.datas.cache.component.RefundExceptionCache;
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
import com.sunshine.task.biz.taskitem.HandleRefundExceptionTask;
import com.sunshine.task.biz.vo.TaskResultInfo;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.task.biz.callable
 * @ClassName: HandleRefundExceptionCollectorCall
 * @Description: <p>退费异常处理</p>
 * @JDK version used: 
 * @Author: 百部
 * @Create Date: 2017年10月19日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class HandleRefundExceptionCollectorCall implements Callable<TaskResultInfo> {
	private Logger logger = LoggerFactory.getLogger(HandleRefundExceptionCollectorCall.class);
	private MerchantApplication application;
	public static final Charset COLLECT_CHARSET = Charset.forName("UTF-8");
	private RefundExceptionCache refundExceptionCache = SpringContextHolder.getBean(RefundExceptionCache.class);
	private TradeOrderService orderService = SpringContextHolder.getBean(TradeOrderService.class);
	private ApplicationChannelService channelService = SpringContextHolder.getBean(ApplicationChannelService.class);

	public HandleRefundExceptionCollectorCall(MerchantApplication application) {
		super();
		this.application = application;
	}

	@Override
	public TaskResultInfo call() throws Exception {
		TradeOrder order = null;
		try {
			ExceptionTradeOrder exceptionOrder = refundExceptionCache.getRefundExceptionFromCache(application.getAppId());
			if (exceptionOrder == null) {
				HandleRefundExceptionTask.logger.info("应用 :{} , 没有找到异常退费订单", application.getAppName());
				return null;
			} else {
				order = orderService.getTradeOrderByAppIdAndOutTradeNo(exceptionOrder.getAppId(), exceptionOrder.getOutTradeNo());
				HandleRefundExceptionTask.logger.info("应用 :{},发现异常退费订单,进入后续处理.orderNo:{},isRefund:{},payStatus:{}",
						new Object[] { order.getAppName(), order.getTradeNo(), order.getIsRefund(), order.getTradeStatus() });
			}
			String lockKey = exceptionOrder.getTradeNo();
			boolean isLock = false;
			RedisService redisService = SpringContextHolder.getBean(RedisService.class);
			RedisLock lock = new RedisLock(redisService.getRedisPool());
			try {
				do {
					isLock = lock.singleLock(lockKey, GlobalConstant.REDIS_PAY_LOCKED_TIME);
				} while (!isLock);
				if (exceptionOrder.getHandleCount() == null) {
					order.setHandleCount(0);
				}
				if (order.getIsRefund() == TradeConstant.ORDER_REFUND_FALSE) {
					//处理退费逻辑
					ApplicationChannel channel = channelService.findTradeChannelByAppIdChannelCode(order.getAppId(), order.getChannelCode());
					if (channel != null && !StringUtils.isEmpty(channel.getParamsJson())) {
						if (TradeConstant.ORDER_REFUND_QUERY_TRUE == order.getIsRefundQuery().intValue()) {
							order = handleRefundException(order, channel.getParamsJson());
						} else {
							order = handleRefundQueryException(order, channel.getParamsJson());
						}

					} else {
						logger.info("订单:{},支付参数为空.", order.getTradeNo());
					}
				} else {
					logger.info("订单:{},已经由其它逻辑处理为状态[ORDER_REFUND_NO_START=1],从退费异常队列移除.", order.getTradeNo());
				}
			} finally {
				lock.singleUnlock(lockKey);
			}
			//处理失败  添加到队列尾部  下次处理
			if (TradeConstant.ORDER_SUCCESS_FALSE == order.getIsSuccess() && order.getHandleCount() < 3) {
				refundExceptionCache.setRefundExceptionToCache(order.convertExceptionObj());
			}
			orderService.updateTradeOrderForDbAndCache(order);
			HandleRefundExceptionTask.logger.info("处理退费订单结束 .订单号:{} , 更新退费状态-> isRefund:{}", new Object[] { exceptionOrder.getTradeNo(),
					exceptionOrder.getIsRefund() });
		} catch (Exception e) {
			HandleRefundExceptionTask.logger.info("退费订单处理异常 .订单号:{} , 退费状态-> isRefund:{}", new Object[] { order.getTradeNo(), order.getIsRefund() });
			e.printStackTrace();

		}
		return null;
	}

	/**
	 * 处理查询
	 * @param record
	 * @return
	 */
	private TradeOrder handleRefundQueryException(TradeOrder order, String paramsJson) throws Exception {
		//查询订单退费信息，判断是否已经退费成功
		Map<String, Object> retMap = TradeUtils.refundQuery(order, paramsJson);
		Boolean isException = (Boolean) retMap.get(GlobalConstant.TRADE_IS_EXCEPTION);
		Boolean isSuccess = (Boolean) retMap.get(GlobalConstant.TRADE_IS_SUCCESS);
		StringBuffer content = new StringBuffer("(退费异常轮询)退费异常订单处理(查询),");
		if (isException) {
			content.append("查询异常，原因:").append(retMap.get(GlobalConstant.TRADE_FAIL_MSG));
		} else {
			if (isSuccess) {
				int tradeStatus = Integer.parseInt(retMap.get(GlobalConstant.TRADE_ORDER_STATE).toString());
				if (TradeConstant.TRADE_STATUS_PAYMENT == tradeStatus) {//订单已支付，执行退费
					order.setIsRefundQuery(TradeConstant.ORDER_REFUND_QUERY_TRUE);
					order.setRefundQueryJson(retMap.get(GlobalConstant.TRADE_SUCCESS_DATA).toString());
					order = handleRefundException(order, paramsJson);
				} else if (TradeConstant.TRADE_STATUS_REFUNDING == tradeStatus) {
					order.setTradeStatus(TradeConstant.TRADE_STATUS_REFUNDING);
					order.setIsRefund(TradeConstant.ORDER_REFUND_FALSE);
					content.append("查询该订单退费中，不能退费。");
				} else {
					if (TradeConstant.TRADE_STATUS_REFUND == tradeStatus) { //查询状态位已退款
						order.setIsException(TradeConstant.ORDER_EXCEPTION_FALSE);
						order.setTradeStatus(TradeConstant.TRADE_STATUS_REFUND);
						order.setIsRefund(TradeConstant.ORDER_REFUND_TRUE);
						content.append("查询该订单已经退费");
						//已退费，处理完成移出异常队列
						refundExceptionCache.removeRefundExceptionFromCache(order.getAppId());
					} else if (TradeConstant.TRADE_STATUS_CLOSE == tradeStatus) {//订单已关闭
						order.setIsException(TradeConstant.ORDER_EXCEPTION_FALSE);
						order.setTradeStatus(TradeConstant.TRADE_STATUS_CLOSE);
						order.setIsRefund(TradeConstant.ORDER_REFUND_FALSE);
						//已经关闭，处理完成移出异常队列
						refundExceptionCache.removeRefundExceptionFromCache(order.getAppId());
						content.append("查询该订单已经关闭，不能退费。");
					} else if (TradeConstant.TRADE_STATUS_REFUND == tradeStatus) {//订单未支付
						order.setIsException(TradeConstant.ORDER_EXCEPTION_FALSE);
						order.setTradeStatus(TradeConstant.TRADE_STATUS_REFUND);
						order.setIsRefund(TradeConstant.ORDER_REFUND_FALSE);
						//未支付，处理完成移出异常队列
						refundExceptionCache.removeRefundExceptionFromCache(order.getAppId());
						content.append("查询该订单未支付，不能退费。");
					}
				}
			} else {
				content.append("查询失败，原因:").append(retMap.get(GlobalConstant.TRADE_FAIL_MSG));
			}
		}
		order.formatHandleLog(content.toString());
		return order;
	}

	/**
	 * 退费
	 * @param order
	 * @param paramsJson
	 * @return
	 * @throws Exception
	 */
	private TradeOrder handleRefundException(TradeOrder order, String paramsJson) throws Exception {
		//退费
		Map<String, Object> refundMap = TradeUtils.refund(order, paramsJson);
		Boolean isException = (Boolean) refundMap.get(GlobalConstant.TRADE_IS_EXCEPTION);
		Boolean isSuccess = (Boolean) refundMap.get(GlobalConstant.TRADE_IS_SUCCESS);
		StringBuffer content = new StringBuffer("(退费异常轮询)查询该订单已支付，执行退费,");
		if (isException) {
			content.append("退费异常，原因:").append(refundMap.get(GlobalConstant.TRADE_FAIL_MSG));
		} else {
			if (isSuccess) {//退费成功
				String agtRefundNo = refundMap.get(GlobalConstant.TRADE_SUCCESS_DATA).toString();
				order.setIsException(TradeConstant.ORDER_EXCEPTION_FALSE);
				order.setAgtRefundNo(agtRefundNo);
				order.setTradeStatus(TradeConstant.TRADE_STATUS_REFUND); // 状态改为：已退费
				order.setIsRefund(TradeConstant.ORDER_REFUND_TRUE);
				content.append("退费成功，第三方退费订单号:").append(agtRefundNo);
			} else {//退费失败
				content.append("退费失败，失败原因:").append(refundMap.get(GlobalConstant.TRADE_FAIL_MSG));
			}
		}
		order.formatHandleLog(content.toString());
		return order;
	}
}

/**
 * <html>
 * <body>
 *  <P> Copyright 2017 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年9月26日</p>
 *  <p> Created by 党参</p>
 *  </body>
 * </html>
 */
package com.sunshine.thread;

import java.net.URLDecoder;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.sunshine.common.GlobalConstant;
import com.sunshine.framework.cache.redis.RedisLock;
import com.sunshine.framework.cache.redis.RedisService;
import com.sunshine.framework.common.spring.ext.SpringContextHolder;
import com.sunshine.framework.utils.DateUtils;
import com.sunshine.mobileapp.order.entity.TradeOrder;
import com.sunshine.mobileapp.order.service.TradeOrderService;
import com.sunshine.mobileapp.order.service.impl.TradeOrderServiceImpl;
import com.sunshine.payment.TradeConstant;
import com.sunshine.payment.unionpay.UnionPayUtils;
import com.sunshine.payment.utils.http.HttpClient;
import com.sunshine.restful.RestConstant;
import com.sunshine.restful.dto.response.ResponseNotify;
import com.sunshine.trade.service.RestAuthorizationService;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.thread
 * @ClassName: ExecuteNotifyUrlRunnable
 * @Description: <p>异步回调通知分线程处理</p>
 * @JDK version used: 
 * @Author: 党参
 * @Create Date: 2017年9月26日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class UnionExecuteNotifyUrlRunnable implements Runnable {

	private static Logger logger = LoggerFactory.getLogger(UnionExecuteNotifyUrlRunnable.class);

	private Map<String, String> valideData;

	/**
	 * 
	 */
	public UnionExecuteNotifyUrlRunnable() {
		super();
	}

	/**
	 * @param notifyMap
	 */
	public UnionExecuteNotifyUrlRunnable(Map<String, String> valideData) {
		super();
		this.valideData = valideData;
	}

	@Override
	public void run() {
		try {
			logger.info("银联商户主扫二维码异步回调参数:{}", JSON.toJSONString(valideData));
			String queryId = valideData.get("queryId");
			String respCode = valideData.get("respCode"); // 获取应答码，收到后台通知了respCode的值一般是00，可以不需要根据这个应答码判断。
			String reqReserved = valideData.get("reqReserved");
			String txnTime = valideData.get("txnTime");
			String traceNo = valideData.get("traceNo");
			txnTime = DateUtils.formatDate(txnTime, "yyyyMMddHHmmss", "yyyyMMddHHmm");
			String respMsg = valideData.get("respMsg");
			String tradeNo = valideData.get("orderId");

			@SuppressWarnings("unchecked")
			Map<String, String> attachMap = (Map<String, String>) JSON.parse(URLDecoder.decode(reqReserved, TradeConstant.INPUT_CHARSET));
			logger.info("银联商户主扫二维码异步回调透传参数(attachMap):{}", JSON.toJSONString(attachMap));

			String cashierId = attachMap.get(GlobalConstant.MOTHED_INVOKE_RES_CASHIER_ID);
			String channelCode = attachMap.get(RestConstant.PARAM_CHANNELCODE);
			String cardId = attachMap.get(GlobalConstant.UNIONPAY_CARDID);
			boolean isLock = false;
			RedisService redisService = SpringContextHolder.getBean(RedisService.class);
			RedisLock redisLock = new RedisLock(redisService.getRedisPool());
			try {
				do {
					//300000 毫秒数 5分钟
					isLock = redisLock.singleLock(cashierId, GlobalConstant.REDIS_PAY_LOCKED_TIME);
				} while (!isLock);
				TradeOrderService orderService = SpringContextHolder.getBean(TradeOrderServiceImpl.class);
				TradeOrder order = orderService.findById(cashierId);
				if (order != null) {
					order.setPayJson(JSON.toJSONString(valideData));//存储商户银联二维码回调结果
					StringBuffer logContent = new StringBuffer("商户银联二维码回调通知");
					if (UnionPayUtils.RESPCODE_SUCCESS.equalsIgnoreCase(respCode)) {
						order.setTradeNo(tradeNo);
						order.setAgtTradeNo(queryId);
						order.setPayTime(GlobalConstant.parseyyyymmddhhmm(txnTime));
						order.setTradeStatus(TradeConstant.TRADE_STATUS_PAYMENT);
						order.setIsSuccess(TradeConstant.ORDER_SUCCESS_TRUE);
						order.setTraceNo(traceNo);
						order.setCardId(cardId);
						logContent.append("支付成功,商户银联二维码订单号:").append(queryId).append("支付时间:")
								.append(GlobalConstant.formatYYYYMMDDHHMMSS(order.getPayTime()));
						if (TradeConstant.ORDER_NOTIFY_FALSE == order.getIsNotify()) {
							order.setNotifyCount(order.getNotifyCount() + 1);
							ResponseNotify notify = new ResponseNotify();
							notify.pullNotify(order);
							notify.setSignMode(TradeConstant.SIGN_TYPE_RSA);
							Map<String, String> notifyMap = (Map<String, String>) JSON.parse(JSON.toJSONString(notify));
							RestAuthorizationService authorizationService = SpringContextHolder.getBean(RestAuthorizationService.class);
							notifyMap = authorizationService.responseAuthorization(notifyMap);
							logger.info("平台订单号:{}, 商户订单号:{}, 回调次数:{}, 回调参数:{}", order.getTradeNo(), order.getOutTradeNo(), order.getNotifyCount(),
									JSON.toJSONString(notifyMap));
							long startTime = System.currentTimeMillis();
							Map<String, Object> retMap = HttpClient.post(order.getNotifyUrl(), notifyMap, "");
							logger.info("平台订单号:{}, 商户订单号:{}, 回调结果:{}, 回调耗时:{} millis", order.getTradeNo(), order.getOutTradeNo(),
									JSON.toJSONString(retMap), ( System.currentTimeMillis() - startTime ));
							int status = (int) retMap.get(HttpClient.HTTP_STATUS);
							String result = (String) retMap.get(HttpClient.HTTP_DATA);
							if (TradeConstant.HTTP_IS_SUCCESS == status && RestConstant.RETURN_SUCCESS[0].equalsIgnoreCase(result)) {
								order.setIsNotify(TradeConstant.ORDER_NOTIFY_TRUE);//回调成功
								logContent.append("回调成功，商户返回:" + result + "。");
							} else {
								logContent.append("回调失败，商户返回:" + JSON.toJSONString(retMap) + "。");
							}
						} else {
							logger.info("商户订单:{}, 平台订单:{} 已经成功回调,此次不回调", order.getOutTradeNo(), order.getTradeNo());
						}
					} else {
						logContent.append("支付失败，失败原因").append("return_msg:").append(respMsg);
					}

					//返回支付渠道，与订单记录的支付渠道不相同
					if (!channelCode.equals(order.getChannelCode())) {
						logContent.append("。支付方式不对应,支付渠道编码为:").append(channelCode).append("可能为重复支付，请联系支付组");
					}
					order.formatHandleLog(logContent.toString());
				} else {
					logger.info("未查询到订单:{}.", cashierId);
				}
				orderService.updateTradeOrderForDbAndCache(order);
			} finally {
				redisLock.singleLock(cashierId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("银联回调异常:{}", e);
		}
	}
}

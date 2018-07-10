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

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.sunshine.common.GlobalConstant;
import com.sunshine.framework.cache.redis.RedisLock;
import com.sunshine.framework.cache.redis.RedisService;
import com.sunshine.framework.common.spring.ext.SpringContextHolder;
import com.sunshine.mobileapp.order.entity.TradeOrder;
import com.sunshine.mobileapp.order.service.TradeOrderService;
import com.sunshine.mobileapp.order.service.impl.TradeOrderServiceImpl;
import com.sunshine.payment.TradeConstant;
import com.sunshine.payment.utils.http.HttpClient;
import com.sunshine.restful.RestConstant;
import com.sunshine.restful.dto.response.ResponseNotify;
import com.sunshine.trade.service.RestAuthorizationService;
import com.sunshine.trade.service.impl.PayServiceImpl;

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
public class ExecuteNotifyUrlRunnable implements Runnable {

	private static Logger logger = LoggerFactory.getLogger(ExecuteNotifyUrlRunnable.class);

	@Autowired
	private PayServiceImpl payService;

	private TradeOrder order;

	/**
	 * 
	 */
	public ExecuteNotifyUrlRunnable() {
		super();
	}

	/**
	 * @param notifyMap
	 */
	public ExecuteNotifyUrlRunnable(TradeOrder order) {
		super();
		this.order = order;
	}

	@Override
	public void run() {
		if (TradeConstant.ORDER_NOTIFY_FALSE == order.getIsNotify()) {
			order.setNotifyCount(order.getNotifyCount() + 1);
			ResponseNotify notify = new ResponseNotify();
			//如果订单不为已支付、异步回调未到达、支付查询结果未返回，则查询第三方平台,切如果查询成功，重新查询订单信息，提供回调参数
			/*if (TradeConstant.TRADE_STATUS_PAYMENT != order.getTradeStatus() && !StringUtils.isEmpty(order.getPayJson())
					&& GlobalConstant.YES == order.getIsPayQuery()) {
				Map<String, Object> retMap = payService.polymerizationPayQuery(order);
				Boolean isException = (Boolean) retMap.get(GlobalConstant.TRADE_IS_EXCEPTION);
				Boolean isSuccess = (Boolean) retMap.get(GlobalConstant.TRADE_IS_SUCCESS);
				if (!isException && isSuccess) {
					order = orderService.findById(order.getId());
				}
			}*/
			notify.pullNotify(order);
			notify.setSignMode(TradeConstant.SIGN_TYPE_RSA);
			Map<String, String> notifyMap = (Map<String, String>) JSON.parse(JSON.toJSONString(notify));
			RestAuthorizationService authorizationService = SpringContextHolder.getBean(RestAuthorizationService.class);
			notifyMap = authorizationService.responseAuthorization(notifyMap);
			logger.info("平台订单号:{}, 商户订单号:{}, 回调次数:{}, 回调参数:{}", order.getTradeNo(), order.getOutTradeNo(), order.getNotifyCount(),
					JSON.toJSONString(notifyMap));
			long startTime = System.currentTimeMillis();
			Map<String, Object> retMap = HttpClient.post(order.getNotifyUrl(), notifyMap, "");
			logger.info("平台订单号:{}, 商户订单号:{}, 回调结果:{}, 回调耗时:{} millis", order.getTradeNo(), order.getOutTradeNo(), JSON.toJSONString(retMap),
					( System.currentTimeMillis() - startTime ));
			int status = (int) retMap.get(HttpClient.HTTP_STATUS);
			String result = (String) retMap.get(HttpClient.HTTP_DATA);
			if (TradeConstant.HTTP_IS_SUCCESS == status && RestConstant.RETURN_SUCCESS[0].equalsIgnoreCase(result)) {
				order.setIsNotify(TradeConstant.ORDER_NOTIFY_TRUE);//回调成功
				order.formatHandleLog("回调成功，商户返回:" + result + "。");
			} else {
				order.formatHandleLog("回调失败，商户返回:" + JSON.toJSONString(retMap) + "。");
			}
		} else {
			logger.info("商户订单:{}, 平台订单:{} 已经成功回调,此次不回调", order.getOutTradeNo(), order.getTradeNo());
		}
		String lockKey = order.getId();
		boolean isLock = false;
		RedisService redisService = SpringContextHolder.getBean(RedisService.class);
		RedisLock redisLock = new RedisLock(redisService.getRedisPool());
		try {
			do {
				//300000 毫秒数 5分钟
				isLock = redisLock.singleLock(lockKey, GlobalConstant.REDIS_PAY_LOCKED_TIME);
				logger.info("redisLock加锁ID:{},加锁时间:{}", lockKey, GlobalConstant.formatYYYYMMDDHHMMSS(new Date()));
			} while (!isLock);
			TradeOrderService orderService = SpringContextHolder.getBean(TradeOrderServiceImpl.class);
			logger.info("异步通知更新订单信息:{}", JSON.toJSONString(order));
			orderService.updateTradeOrderForDbAndCache(order);
		} finally {
			redisLock.singleLock(lockKey);
		}
	}
}

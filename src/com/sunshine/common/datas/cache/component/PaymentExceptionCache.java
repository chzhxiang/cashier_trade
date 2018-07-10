/**
 * <html>
 * <body>
 *  <P> Copyright 2017 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年9月22日</p>
 *  <p> Created by 党参</p>
 *  </body>
 * </html>
 */
package com.sunshine.common.datas.cache.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.sunshine.common.datas.cache.CacheConstants;
import com.sunshine.framework.cache.redis.RedisService;
import com.sunshine.framework.common.spring.ext.SpringContextHolder;
import com.sunshine.mobileapp.order.entity.ExceptionTradeOrder;
import com.sunshine.mobileapp.order.entity.TradeOrder;
import com.sunshine.mobileapp.order.service.TradeOrderService;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.cache.mobileapp.trade.order
 * @ClassName: TradeOrderCache
 * @Description: <p>支付异常订单缓存</p>
 * @JDK version used: 
 * @Author: 党参
 * @Create Date: 2017年9月22日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class PaymentExceptionCache {
	private static Logger logger = LoggerFactory.getLogger(PaymentExceptionCache.class);
	private RedisService redisSvc = SpringContextHolder.getBean(RedisService.class);

	public void initCache() {
		logger.info("加载支付异常订单信息缓存start.................");
		TradeOrderService orderService = SpringContextHolder.getBean(TradeOrderService.class);
		List<TradeOrder> orders = orderService.getAllPaymentException();
		logger.info("orders.size:{}", orders.size());

		Map<String, List<String>> groupJsonMap = new HashMap<String, List<String>>();
		List<String> groupJsonOrders = null;
		String cacheKey = null;
		for (TradeOrder order : orders) {
			boolean isNeedPut = false;
			cacheKey = getCacheKey(order.getAppId());
			groupJsonOrders = groupJsonMap.get(cacheKey);
			if (groupJsonOrders == null) {
				groupJsonOrders = new ArrayList<String>();
				isNeedPut = true;
			}
			groupJsonOrders.add(JSON.toJSONString(order.convertExceptionObj()));
			if (isNeedPut) {
				groupJsonMap.put(cacheKey, groupJsonOrders);
			}
		}

		//删除缓存中的异常队列
		if (!groupJsonMap.isEmpty()) {
			redisSvc.del(groupJsonMap.keySet().toArray(new String[groupJsonMap.keySet().size()]));
			redisSvc.pipelineLDatas(groupJsonMap);
		}
		logger.info("加载支付异常订单信息缓存end.................");
	}

	/**
	 * 获取异常订单缓存key
	 * @return
	 */
	public String getCacheKey(String appId) {
		return CacheConstants.CACHE_PAYMENT_EXCEPTION_REFIX.concat(appId);
	}

	/**
	 * 从异常队列获取index 0(链表的头部) 的支付异常订单记录
	 * @param Record
	 * @return
	 */
	public ExceptionTradeOrder getExceptionPayTradeOrderFromCache(String appId) {
		String cacheKey = getCacheKey(appId);
		ExceptionTradeOrder order = null;
		String val = redisSvc.lpop(cacheKey);
		//判断是否未空 或者是否存在
		if (StringUtils.isNotEmpty(val) && !CacheConstants.CACHE_NULL_STRING.equalsIgnoreCase(val)) {
			order = JSON.parseObject(val, ExceptionTradeOrder.class);
		}
		return order;
	}

	/**
	 * 异常支付异常订单记录存入 缓存的异常队列
	 * @param Record
	 * @return
	 */
	public void setExceptionPayTradeOrderToCache(ExceptionTradeOrder payOrder) {
		//存储的key
		//payOrder.setIsException(TradeConstant.ORDER_EXCEPTION_TRUE);
		String cacheKey = getCacheKey(payOrder.getAppId());
		String val = JSON.toJSONString(payOrder);
		redisSvc.rpush(cacheKey, val);
	}

	/**
	 * 将队列的头部元素移出异常挂号队列  
	 * @param hospitalId
	 * @return
	 */
	public ExceptionTradeOrder removeExceptionPayTradeOrderFromCache(String appId) {
		String cacheKey = getCacheKey(appId);
		ExceptionTradeOrder order = null;

		String jsonVal = redisSvc.lpop(cacheKey);
		//判断是否未空 或者是否存在
		if (StringUtils.isNotEmpty(jsonVal) && !CacheConstants.CACHE_NULL_STRING.equalsIgnoreCase(jsonVal)) {
			order = JSON.parseObject(jsonVal, ExceptionTradeOrder.class);
		}
		return order;
	}
}

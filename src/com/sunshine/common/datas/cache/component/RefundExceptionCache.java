/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年10月18日</p>
 *  <p> Created by hqy</p>
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
 * @Package: com.sunshine.common.datas.cache.component
 * @ClassName: RefundExceptionCache
 * @Description: <p>退费异常订单缓存</p>
 * @JDK version used: 
 * @Author: 百部
 * @Create Date: 2017年10月18日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class RefundExceptionCache {
	private static Logger logger = LoggerFactory.getLogger(PaymentExceptionCache.class);
	private RedisService redisSvc = SpringContextHolder.getBean(RedisService.class);

	public void initCache() {
		logger.info("加载退费异常订单信息缓存start.................");
		TradeOrderService orderService = SpringContextHolder.getBean(TradeOrderService.class);
		List<TradeOrder> orders = orderService.getAllRefundException();
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
		logger.info("加载退费异常订单信息缓存end.................");
	}

	/**
	 * 退费异常订单存入 缓存的异常队列
	 * @param exceptionOrder
	 */
	public void setRefundExceptionToCache(ExceptionTradeOrder exceptionOrder) {
		String cacheKey = getCacheKey(exceptionOrder.getAppId());
		String val = JSON.toJSONString(exceptionOrder);
		redisSvc.rpush(cacheKey, val);
	}

	/**
	 * 从异常队列获取index 0(链表的头部) 的退费订单
	 * @param appId
	 * @return
	 */
	public ExceptionTradeOrder getRefundExceptionFromCache(String appId) {
		String cacheKey = getCacheKey(appId);
		ExceptionTradeOrder exceptionTradeOrder = null;
		String val = redisSvc.lpop(cacheKey);
		if (StringUtils.isNotEmpty(val) && !CacheConstants.CACHE_NULL_STRING.equalsIgnoreCase(val)) {
			exceptionTradeOrder = JSON.parseObject(val, ExceptionTradeOrder.class);
		}
		return exceptionTradeOrder;
	}

	/**
	 * 将队列的头部元素移出异常退费队列  
	 * @param merchantNo
	 * @return
	 */
	public ExceptionTradeOrder removeRefundExceptionFromCache(String appId) {
		String cacheKey = getCacheKey(appId);
		ExceptionTradeOrder exceptionTradeOrder = null;
		String val = redisSvc.lpop(cacheKey);
		if (StringUtils.isNotEmpty(val) && !CacheConstants.CACHE_NULL_STRING.equalsIgnoreCase(val)) {
			exceptionTradeOrder = JSON.parseObject(val, ExceptionTradeOrder.class);
		}
		return exceptionTradeOrder;
	}

	/**
	 * 获取异常订单缓存key
	 * @return
	 */
	public String getCacheKey(String appId) {
		return CacheConstants.CACHE_REFUND_EXCEPTION_REFIX.concat(appId);
	}
}

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.sunshine.common.datas.cache.CacheConstants;
import com.sunshine.framework.cache.redis.RedisService;
import com.sunshine.framework.common.spring.ext.SpringContextHolder;
import com.sunshine.mobileapp.order.entity.TradeOrder;
import com.sunshine.mobileapp.order.service.TradeOrderService;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.cache.mobileapp.trade.order
 * @ClassName: TradeOrderCache
 * @Description: <p>交易订单缓存</p>
 * @JDK version used: 
 * @Author: 党参
 * @Create Date: 2017年9月22日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class TradeOrderCache {
	private static Logger logger = LoggerFactory.getLogger(TradeOrderCache.class);
	private RedisService redisSvc = SpringContextHolder.getBean(RedisService.class);

	public static final String CACHE_TRADE_ORDER_FIELD_FREFIX = "tradeOrder";

	public void initCache() {
		logger.info("加载订单信息缓存start.................");
		redisSvc.del(getCacheKey());
		TradeOrderService orderService = SpringContextHolder.getBean(TradeOrderService.class);
		List<TradeOrder> orders = orderService.getTradeOrderForCache(-2);
		Map<String, String> tradeOrderMap = new HashMap<String, String>();
		if (!CollectionUtils.isEmpty(orders)) {
			for (TradeOrder order : orders) {
				if (!StringUtils.isEmpty(order.getOutTradeNo())) {
					String fieldName = getTradeOrderFieldByOutTradeNo(order.getAppId(), order.getOutTradeNo());
					String jsonTradeOrder = JSON.toJSONString(order);
					tradeOrderMap.put(fieldName, jsonTradeOrder);
				}
			}
		}

		if (!tradeOrderMap.isEmpty()) {
			logger.info("订单缓存信息:{}", JSON.toJSONString(tradeOrderMap));
			redisSvc.hmset(getCacheKey(), tradeOrderMap);
		}

		logger.info("加载订单信息缓存end.................");
	}

	/**
	 * 订单缓存写入
	 * @param tradeOrder
	 */
	public void setTradeOrder(TradeOrder order) {
		if (order != null) {
			redisSvc.hset(getCacheKey(), getTradeOrderFieldByOutTradeNo(order.getAppId(), order.getOutTradeNo()), JSON.toJSONString(order));
		}
	}

	/**
	 * 获取订单缓存
	 * @param outTradeNo
	 * @return
	 */
	public TradeOrder getTradeOrderForCache(String appId, String outTradeNo) {
		TradeOrder tradeOrder = null;
		String json = redisSvc.hget(getCacheKey(), getTradeOrderFieldByOutTradeNo(appId, outTradeNo));
		if (!StringUtils.isEmpty(json)) {
			tradeOrder = JSON.parseObject(json, TradeOrder.class);
		}
		return tradeOrder;
	}

	/**
	 * 订单信息缓存fieldName 生成
	 * @param outTradeNo
	 * @return
	 */
	private String getTradeOrderFieldByOutTradeNo(String appId, String outTradeNo) {
		return CACHE_TRADE_ORDER_FIELD_FREFIX.concat(CacheConstants.CACHE_KEY_SPLIT_CHAR).concat(appId).concat(CacheConstants.CACHE_KEY_SPLIT_CHAR)
				.concat(outTradeNo);
	}

	/**
	 * 获取订单缓存key
	 * @return
	 */
	public String getCacheKey() {
		return CacheConstants.CACHE_TRADE_ORDER_KEY_FREFIX;
	}
}

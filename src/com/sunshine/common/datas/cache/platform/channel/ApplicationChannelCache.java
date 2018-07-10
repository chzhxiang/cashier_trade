/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年9月14日</p>
 *  <p> Created by 赤芍</p>
 *  </body>
 * </html>
 */
package com.sunshine.common.datas.cache.platform.channel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.sunshine.common.GlobalConstant;
import com.sunshine.common.datas.cache.CacheConstants;
import com.sunshine.framework.cache.redis.RedisService;
import com.sunshine.framework.common.spring.ext.SpringContextHolder;
import com.sunshine.platform.applicationchannel.entity.ApplicationChannel;
import com.sunshine.platform.applicationchannel.service.ApplicationChannelService;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.cache.platform.channel
 * @ClassName: ApplicationChannelCache
 * @Description: <p></p>
 * @JDK version used: 
 * @Author: 赤芍
 * @Create Date: 2017年9月14日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class ApplicationChannelCache {
	private static Logger logger = LoggerFactory.getLogger(ApplicationChannelCache.class);
	private RedisService redisSvc = SpringContextHolder.getBean(RedisService.class);

	/**
	 * cache key
	 * @return
	 */
	public String getCacheKey() {
		return CacheConstants.CACHE_APPLICATION_CHANNEL_KEY_FREFIX;
	}

	/**
	 * 获得缓存hash的key
	 * @param applicationChannel
	 * @return
	 */
	public String getFileName(ApplicationChannel applicationChannel) {
		String fileName =
				applicationChannel.getMerchantNo() + CacheConstants.CACHE_KEY_SPLIT_CHAR + applicationChannel.getAppId()
						+ CacheConstants.CACHE_KEY_SPLIT_CHAR + applicationChannel.getChannelCode();
		return fileName;
	}

	/**
	 * 初始化应用支付渠道信息
	 */
	public void initCache() {
		logger.info("初始化应用支付渠道信息start...");
		ApplicationChannelService applicationChannelService = SpringContextHolder.getBean(ApplicationChannelService.class);
		List<ApplicationChannel> applicationChannels = applicationChannelService.findAll();
		redisSvc.del(getCacheKey());
		Map<String, String> channelMap = new HashMap<String, String>();
		for (ApplicationChannel applicationChannel : applicationChannels) {
			if (GlobalConstant.YES == applicationChannel.getIsOpen()) { //已开启
				String jsonChannelInfo = JSON.toJSONString(applicationChannel);
				String fileName = getFileName(applicationChannel);
				channelMap.put(fileName, jsonChannelInfo);
			}
		}
		if (!CollectionUtils.isEmpty(channelMap)) {
			updateApplicationChannel(channelMap);
		}
		logger.info("初始化应用支付渠道信息end...");
	}

	/**
	 * 更新应用支付渠道信息
	 * @param applicationChannel
	 */
	public void updateApplicationChannel(ApplicationChannel applicationChannel) {
		if (applicationChannel != null) {
			redisSvc.hset(getCacheKey(), getFileName(applicationChannel), JSON.toJSONString(applicationChannel));
		}
	}

	/**
	 * 批量更新支付渠道缓存
	 */
	public void updateApplicationChannel(Map<String, String> map) {
		if (map != null) {
			redisSvc.hmset(getCacheKey(), map);
		}
	}

	/**
	 * 删除关闭的支付渠道参数缓存
	 */
	public void deleteApplicationChannel(ApplicationChannel applicationChannel) {
		if (applicationChannel != null) {
			redisSvc.hdel(getCacheKey(), getFileName(applicationChannel));
		}
	}

	/**
	 * 判断支付渠道缓存是否存在
	 */
	public boolean isExitsApplicationChannel(ApplicationChannel applicationChannel) {
		boolean isExits = false;
		if (applicationChannel != null) {
			isExits = redisSvc.hexists(getCacheKey(), getFileName(applicationChannel));
		}
		return isExits;
	}

	/**
	 * 根据appId和channelCode查找应用支付渠道信息
	 * @param merchantNo
	 * @return
	 */
	public ApplicationChannel getMerchantByMerchantNo(String merchantNo, String appId, String channelCode) {
		String applicationChannelJson =
				redisSvc.hget(getCacheKey(), merchantNo + CacheConstants.CACHE_KEY_SPLIT_CHAR + appId + CacheConstants.CACHE_KEY_SPLIT_CHAR
						+ channelCode);
		if (applicationChannelJson != null && !CacheConstants.CACHE_KEY_NOT_EXIST.equals(applicationChannelJson)) {
			return JSON.parseObject(applicationChannelJson, ApplicationChannel.class);
		} else {
			return null;
		}
	}

	/**
	 * 根据merchantNo、appId查询缓存中已开启的支付渠道配置信息
	 * @param appId
	 * @return
	 */
	public List<ApplicationChannel> getChannelCacheByAppId(String merchantNo, String appId) {
		List<ApplicationChannel> list = new ArrayList<ApplicationChannel>();
		Map<String, String> allChannel = redisSvc.hgetAll(getCacheKey());
		for (Map.Entry<String, String> entry : allChannel.entrySet()) {
			String key = entry.getKey(); //缓存的key
			String[] split = key.split(CacheConstants.CACHE_KEY_SPLIT_CHAR);
			if (merchantNo.equals(split[0]) && appId.equals(split[1])) {
				String applicationChannelJson = allChannel.get(key);
				ApplicationChannel parseObject = JSON.parseObject(applicationChannelJson, ApplicationChannel.class);
				list.add(parseObject);
			}
		}
		return list;
	}

	/**
	 * 根据标识获取一类的支付渠道配置信息，比如微信：wechat
	 * @param identity
	 * @return
	 */
	public List<ApplicationChannel> getAllChannelByIdentity(String identity) {
		List<ApplicationChannel> list = new ArrayList<ApplicationChannel>();
		Map<String, String> allChannel = redisSvc.hgetAll(getCacheKey());
		for (Map.Entry<String, String> entry : allChannel.entrySet()) {
			String key = entry.getKey(); //缓存的key
			String applicationChannelJson = allChannel.get(key);
			ApplicationChannel parseObject = JSON.parseObject(applicationChannelJson, ApplicationChannel.class);
			String channelCode = parseObject.getChannelCode();
			if (channelCode.indexOf(identity) != -1) {
				list.add(parseObject);
			}
		}
		return list;
	}

	/**
	 * 获取所有
	 */
	public List<ApplicationChannel> getAllChannel() {
		List<ApplicationChannel> list = new ArrayList<ApplicationChannel>();
		Map<String, String> allChannel = redisSvc.hgetAll(getCacheKey());
		for (Map.Entry<String, String> entry : allChannel.entrySet()) {
			String key = entry.getKey(); //缓存的key
			String applicationChannelJson = allChannel.get(key);
			ApplicationChannel parseObject = JSON.parseObject(applicationChannelJson, ApplicationChannel.class);
			list.add(parseObject);
		}
		return list;
	}

}

/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年8月28日</p>
 *  <p> Created by 赤芍</p>
 *  </body>
 * </html>
 */
package com.sunshine.common.datas.cache.platform.merchant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.sunshine.common.datas.cache.CacheConstants;
import com.sunshine.framework.cache.redis.RedisService;
import com.sunshine.framework.common.spring.ext.SpringContextHolder;
import com.sunshine.platform.application.entity.MerchantApplication;
import com.sunshine.platform.application.service.MerchantApplicationService;
import com.sunshine.platform.merchant.entity.Merchant;
import com.sunshine.platform.merchant.service.MerchantService;

/**
 * @Project: cashier_desk 
 * @Package: com.sunshine.common.datas.cache.component
 * @ClassName: MerchantCache
 * @Description: <p>商户基本信息缓存</p>
 * @JDK version used: 
 * @Author: 赤芍
 * @Create Date: 2017年8月28日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class MerchantCache {
	private static Logger logger = LoggerFactory.getLogger(MerchantCache.class);
	private RedisService redisSvc = SpringContextHolder.getBean(RedisService.class);

	/**
	 * 商户cache key
	 * @return
	 */
	public String getCacheKey() {
		return CacheConstants.CACHE_MERCHANT_KEY_FREFIX;
	}

	/**
	 * 初始化商户基本信息
	 */
	public void initCache() {
		logger.info("初始化商户基本信息start...");
		MerchantService merchantService = SpringContextHolder.getBean(MerchantService.class);
		List<Merchant> merchants = merchantService.findAll();
		redisSvc.del(getCacheKey());
		Map<String, String> dataMap = new HashMap<String, String>();
		for (Merchant merchant : merchants) {
			dataMap.put(merchant.getMerchantNo(), JSON.toJSONString(merchant));
		}
		if (!CollectionUtils.isEmpty(dataMap)) {
			redisSvc.hmset(getCacheKey(), dataMap);
		}
		logger.info("初始化商户基本信息end...");
	}

	/**
	 * 添加/更新商户接入信息
	 * @param merchant
	 */
	public void updateMerchant(Merchant merchant) {
		if (merchant != null) {
			redisSvc.hset(getCacheKey(), merchant.getMerchantNo(), JSON.toJSONString(merchant));
		}
	}

	/**
	 * 根据merchantNo查找商户信息
	 * @param merchantNo
	 * @return
	 */
	public Merchant getMerchantByMerchantNo(String merchantNo) {
		String merchantJson = redisSvc.hget(getCacheKey(), merchantNo);
		if (merchantJson != null && !CacheConstants.CACHE_KEY_NOT_EXIST.equals(merchantJson)) {
			return JSON.parseObject(merchantJson, Merchant.class);
		} else {
			return null;
		}
	}

	/**
	 * 获取缓存中的所有商户信息
	 * @return
	 */
	public List<Merchant> getAllMerchant() {
		List<Merchant> resultList = new ArrayList<Merchant>();
		Map<String, String> allMap = redisSvc.hgetAll(getCacheKey());
		for (Map.Entry<String, String> entry : allMap.entrySet()) {
			String value = entry.getValue();
			Merchant merchant = JSON.parseObject(value, Merchant.class);
			resultList.add(merchant);
		}
		return resultList;
	}

	/**
	 * 获取商户应用信息缓存key
	 * @return
	 */
	public String getApplicationKey() {
		return CacheConstants.CACHE_MERCHANTAPPLICATION_KEY_FREFIX;
	}

	/**
	 * 获取hash key
	 */
	public String getApplicationHashKey(MerchantApplication merchantApplication) {
		String hashKey =
				merchantApplication.getMerchantNo().trim().concat(CacheConstants.CACHE_KEY_SPLIT_CHAR).concat(merchantApplication.getAppId().trim());
		return hashKey;
	}

	/**
	 * 初始化加载商户应用信息。
	 */
	public void initApplicationCache() {
		logger.info("初始化商户应用信息start...");
		MerchantApplicationService bean = SpringContextHolder.getBean(MerchantApplicationService.class);
		List<MerchantApplication> allMerchantApplication = bean.findAll();
		redisSvc.del(getApplicationKey());
		Map<String, String> cacheMap = new HashMap<String, String>();
		for (MerchantApplication merchantApplication : allMerchantApplication) {
			String hashKey = getApplicationHashKey(merchantApplication);
			String jsonData = JSON.toJSONString(merchantApplication);
			cacheMap.put(hashKey, jsonData);
		}
		if (!CollectionUtils.isEmpty(cacheMap)) {
			cacheMerchantApplication(cacheMap);
		}
		logger.info("初始化商户应用信息end...");
	}

	/**
	 * 缓存(更新)商户应用信息
	 */
	public Long cacheMerchantApplication(MerchantApplication merchantApplication) {
		if (merchantApplication != null) {
			String hashKey = getApplicationHashKey(merchantApplication);
			Long result = redisSvc.hset(getApplicationKey(), hashKey, JSON.toJSONString(merchantApplication));
			return result;
		} else {
			return -1L;
		}
	}

	/**
	 * 批量更新
	 */
	public void cacheMerchantApplication(Map<String, String> map) {
		if (map != null) {
			redisSvc.hmset(getApplicationKey(), map);
		}
	}

	/**
	 * 根据mechNo和appId获取商户应用缓存信息
	 * @param mechNo
	 * @param appId
	 * @return
	 */
	public MerchantApplication getMerchantApplicationInfoByAppId(String mechNo, String appId) {
		String jsonData = redisSvc.hget(getApplicationKey(), mechNo.trim().concat(CacheConstants.CACHE_KEY_SPLIT_CHAR).concat(appId.trim()));
		MerchantApplication application = JSON.parseObject(jsonData, MerchantApplication.class);
		if (application == null) {
			logger.debug("获取商户应用缓存信息失败,AppId:{}", appId);
		} else {
			logger.debug("获取商户应用缓存信息成功,AppId:{},商户应用信息：{}", appId, jsonData);
		}
		return application;
	}

	/**
	 * 根据mechNo取商户应用缓存信息
	 * @param mechNo
	 * @param appId
	 * @return
	 */
	public List<MerchantApplication> getMerchantApplicationByMechNo(String mechNo) {
		Map<String, String> all = redisSvc.hgetAll(getApplicationKey());
		List<MerchantApplication> list = new ArrayList<MerchantApplication>();
		for (Map.Entry<String, String> entry : all.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			String merchan = key.split(CacheConstants.CACHE_KEY_SPLIT_CHAR)[0];
			if (mechNo.equals(merchan)) {
				MerchantApplication application = JSON.parseObject(value, MerchantApplication.class);
				list.add(application);
			}
		}
		return list;
	}
}

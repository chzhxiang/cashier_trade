/**
 * <html>
 * <body>
 *  <P> Copyright 2017 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年10月29日</p>
 *  <p> Created by 熊胆</p>
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
import com.sunshine.mobileapp.bankCard.entity.BankCard;
import com.sunshine.mobileapp.bankCard.service.BankCardService;

/**
 * @Project: cashier_trade
 * @Package: com.sunshine.common.datas.cache.component
 * @ClassName: BankCardCache
 * @Description: 银行卡信息缓存
 * @JDK version used: 
 * @Author: 熊胆
 * @Create Date: 2017年10月29日上午10:41:14
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class BankCardCache {
	private static Logger logger = LoggerFactory.getLogger(BankCardCache.class);
	private RedisService redisSvc = SpringContextHolder.getBean(RedisService.class);

	public static final String CACHE_BANKCARD_FIELD_FREFIX = "bankCard";

	public void initCache() {
		logger.info("加载银行卡信息缓存start.................");
		redisSvc.del(getCacheKey());
		BankCardService bankCardService = SpringContextHolder.getBean(BankCardService.class);
		List<BankCard> cards = bankCardService.getBankCardForCache(-2);
		Map<String, String> bankCardMap = new HashMap<String, String>();
		if (!CollectionUtils.isEmpty(cards)) {
			for (BankCard card : cards) {
				if (!StringUtils.isEmpty(card.getBankCardNo())) {
					String fieldName = getBankCardFieldByCardNo(card.getAppId(), card.getBankCardNo());
					String jsonBankCard = JSON.toJSONString(card);
					bankCardMap.put(fieldName, jsonBankCard);
				}
			}
		}

		if (!bankCardMap.isEmpty()) {
			logger.info("银行卡信息缓存信息:{}", JSON.toJSONString(bankCardMap));
			redisSvc.hmset(getCacheKey(), bankCardMap);
		}

		logger.info("加载银行卡信息缓存end.................");
	}

	/**
	 * 银行卡信息缓存写入
	 * @param BankCard
	 */
	public void setBankCard(BankCard card) {
		if (card != null) {
			redisSvc.hset(getCacheKey(), getBankCardFieldByCardNo(card.getAppId(), card.getBankCardNo()), JSON.toJSONString(card));
		}
	}

	/**
	 * 获取银行卡信息缓存
	 * @param bankCardNo,appId
	 * @return
	 */
	public BankCard getBankCardForCache(String appId, String bankCardNo) {
		BankCard bankCard = null;
		String json = redisSvc.hget(getCacheKey(), getBankCardFieldByCardNo(appId, bankCardNo));
		if (!StringUtils.isEmpty(json)) {
			bankCard = JSON.parseObject(json, BankCard.class);
		}
		return bankCard;
	}

	/**
	 * 银行卡信息缓存fieldName 生成
	 * @param bankCardNo
	 * @return
	 */
	private String getBankCardFieldByCardNo(String appId, String bankCardNo) {
		return CACHE_BANKCARD_FIELD_FREFIX.concat(CacheConstants.CACHE_KEY_SPLIT_CHAR).concat(appId).concat(CacheConstants.CACHE_KEY_SPLIT_CHAR)
				.concat(bankCardNo);
	}

	/**
	 * 获取银行卡缓存key
	 * @return
	 */
	public String getCacheKey() {
		return CacheConstants.CACHE_BANKCARD_KEY_FREFIX;
	}
}

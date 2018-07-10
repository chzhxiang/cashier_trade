/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年9月13日</p>
 *  <p> Created by hqy</p>
 *  </body>
 * </html>
 */
package com.sunshine.platform.applicationchannel.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.sunshine.common.GlobalConstant;
import com.sunshine.common.datas.cache.platform.channel.ApplicationChannelCache;
import com.sunshine.framework.common.spring.ext.SpringContextHolder;
import com.sunshine.framework.mvc.mysql.dao.BaseDao;
import com.sunshine.framework.mvc.mysql.service.impl.BaseServiceImpl;
import com.sunshine.platform.TradeChannelConstants;
import com.sunshine.platform.applicationchannel.dao.ApplicationChannelDao;
import com.sunshine.platform.applicationchannel.entity.ApplicationChannel;
import com.sunshine.platform.applicationchannel.service.ApplicationChannelService;
import com.sunshine.platform.applicationchannel.vo.DefautChannelViewNameConstants;
import com.sunshine.platform.channel.dao.TradeChannelDao;
import com.sunshine.platform.channel.entity.TradeChannel;
import com.sunshine.platform.channel.vo.WechatVo;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.platform.application_channel.service.impl
 * @ClassName: ApplicationChannelServiceImpl
 * @Description: <p>应用支付渠道配置service实现</p>
 * @JDK version used: 
 * @Author: 百部
 * @Create Date: 2017年9月13日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
@Service
public class ApplicationChannelServiceImpl extends BaseServiceImpl<ApplicationChannel, String> implements ApplicationChannelService {
	private ApplicationChannelCache applicationChannelCache = SpringContextHolder.getBean(ApplicationChannelCache.class);
	private static Logger logger = LoggerFactory.getLogger(ApplicationChannelServiceImpl.class);
	@Autowired
	protected ApplicationChannelDao applicationChannelDao;
	@Autowired
	protected TradeChannelDao tradeChannelDao;

	@Override
	protected BaseDao<ApplicationChannel, String> getDao() {
		return applicationChannelDao;
	}

	/**
	 * 根据appid查找支付渠道配置信息
	 */
	@Override
	public List<ApplicationChannel> findTradeChannelByAppId(String appId) {
		return applicationChannelDao.findTradeChannelByAppId(appId);
	}

	/**
	 * 根据appid 渠道编码查找支付渠道配置信息
	 */
	@Override
	public ApplicationChannel findTradeChannelByAppIdChannelCode(String appId, String channelCode) {
		return applicationChannelDao.findTradeChannelByAppIdChannelCode(appId, channelCode);
	}

	/**
	 * 支付渠道配置信息写入缓存
	 */
	@Override
	public void setTradeChannelCache(ApplicationChannel applicationChannel) {
		String id = applicationChannel.getId();
		ApplicationChannel cacheApplicationChannel = applicationChannelDao.findById(id);
		if (cacheApplicationChannel != null) {
			logger.info("将商户应用的支付渠道信息写入缓存：{}", JSON.toJSON(cacheApplicationChannel));
			applicationChannelCache.updateApplicationChannel(cacheApplicationChannel);
		}
	}

	/**
	 * 支付渠道配置信息写入缓存(批量)
	 */
	@Override
	public void setTradeChannelCache(List<String> list) {
		List<ApplicationChannel> applictionList = applicationChannelDao.findByIds(list);
		Map<String, String> cacheMap = new HashMap<String, String>();
		for (ApplicationChannel application : applictionList) {
			String jsonChannelInfo = JSON.toJSONString(application);
			String fileName = applicationChannelCache.getFileName(application);
			cacheMap.put(fileName, jsonChannelInfo);
		}
		if (!CollectionUtils.isEmpty(cacheMap)) {
			//重新写入缓存
			logger.info("开启支付渠道，更新缓存！");
			applicationChannelCache.updateApplicationChannel(cacheMap);
		}
	}

	/**
	 * 支付渠道配置信息移出缓存
	 */
	@Override
	public void deleteTradeChannelCache(ApplicationChannel applicationChannel) {
		String id = applicationChannel.getId();
		ApplicationChannel cacheApplicationChannel = applicationChannelDao.findById(id);
		if (cacheApplicationChannel != null) {
			logger.info("将商户应用的支付渠道信息移出缓存：{}", JSON.toJSON(cacheApplicationChannel));
			applicationChannelCache.deleteApplicationChannel(cacheApplicationChannel);
		}

	}

	/**
	 * 根据商户号、appid查找已开启的支付渠道列表
	 */
	@Override
	public List<ApplicationChannel> getTradeChannelCacheByMerchantNoAppId(String merchantNo, String appId) {
		logger.info("从缓存中获取商户应用已开启的支付渠道列表，商户号:{},appId:{}", merchantNo, appId);
		List<ApplicationChannel> channelCache = applicationChannelCache.getChannelCacheByAppId(merchantNo, appId);
		if (channelCache == null || channelCache.size() < 1) {
			logger.info("从缓存中获取商户应用已开启的支付渠道列表为Null,尝试从数据库中获取。");
			List<ApplicationChannel> dbOpenList = new ArrayList<ApplicationChannel>();
			List<ApplicationChannel> dbChannelList = applicationChannelDao.findTradeChannelByAppId(appId);
			//批量更新支付渠道配置
			Map<String, String> channelMap = new HashMap<String, String>();
			for (ApplicationChannel dbApplicationChannel : dbChannelList) {
				//判断是否开启
				if (dbApplicationChannel.getMerchantNo().equals(merchantNo) && GlobalConstant.YES == dbApplicationChannel.getIsOpen()) { //已开启
					dbOpenList.add(dbApplicationChannel);
					//封装hash
					String jsonChannelInfo = JSON.toJSONString(dbApplicationChannel);
					String fileName = applicationChannelCache.getFileName(dbApplicationChannel);
					channelMap.put(fileName, jsonChannelInfo);
				}
			}
			logger.debug("获取商户应用已开启的支付渠道列表结果：{}", JSON.toJSON(channelMap));
			if (!CollectionUtils.isEmpty(channelMap)) {
				//重新写入缓存
				applicationChannelCache.updateApplicationChannel(channelMap);
			}
			sortList(dbOpenList); //排序
			return dbOpenList;
		} else {
			logger.debug("从缓存中获取商户应用已开启的支付渠道列表结果：{}", JSON.toJSON(channelCache));
			sortList(channelCache); //排序
			return channelCache;
		}

	}

	/**
	 * 根据商户号、appid、渠道编码查找已开启的该支付渠道信息
	 */
	@Override
	public ApplicationChannel getTradeChannel(String merchantNo, String appId, String channelCode) {
		logger.info("从缓存中获取商户应用的支付渠道信息，商户号:{},appId:{},channelCode:{}", merchantNo, appId, channelCode);
		ApplicationChannel cacheApplicationChannel = applicationChannelCache.getMerchantByMerchantNo(merchantNo, appId, channelCode);
		if (cacheApplicationChannel == null) {
			logger.info("从缓存中获取商户应用的支付渠道信息为Null,尝试从数据库中获取。");
			ApplicationChannel dbApplicationChannel = applicationChannelDao.findTradeChannelByAppIdChannelCode(appId, channelCode);
			if (dbApplicationChannel != null) {
				if (dbApplicationChannel.getMerchantNo().equals(merchantNo) && GlobalConstant.YES == dbApplicationChannel.getIsOpen()) { //已开启
					logger.debug("获取商户应用的支付渠道信息结果：{}", JSON.toJSON(dbApplicationChannel));
					//重新写入缓存
					setTradeChannelCache(dbApplicationChannel);
					return dbApplicationChannel;
				} else {
					logger.info("该应用的该支付渠道未开启！");
					return null;
				}
			} else {
				logger.info("从数据中获取商户应用的支付渠道信息为空，该支付渠道未配置！");
				return null;
			}
		} else {
			logger.debug("从缓存中获取商户应用的支付渠道信息结果：{}", JSON.toJSON(cacheApplicationChannel));
			return cacheApplicationChannel;
		}
	}

	@Override
	public String channelValConvertCode(int appCodeVal) {
		List<ApplicationChannel> allChannel = applicationChannelCache.getAllChannel();
		for (ApplicationChannel applicationChannel : allChannel) {
			if (applicationChannel.getChannelValue() == appCodeVal) {
				return applicationChannel.getChannelCode();
			}
		}
		logger.info("从数据中获取。。。。");
		List<TradeChannel> all = tradeChannelDao.findAll();
		for (TradeChannel channel : all) {
			if (channel.getValue() == appCodeVal) {
				return channel.getCode();
			}
		}
		return null;
	}

	@Override
	public Integer channeCodeConvertVal(String appCode) {
		List<ApplicationChannel> allChannel = applicationChannelCache.getAllChannel();
		for (ApplicationChannel applicationChannel : allChannel) {
			if (applicationChannel.getChannelCode().equals(appCode)) {
				return applicationChannel.getChannelValue();
			}
		}
		logger.info("从数据中获取。。。。");
		List<TradeChannel> all = tradeChannelDao.findAll();
		for (TradeChannel channel : all) {
			if (channel.getCode().equals(appCode)) {
				return channel.getValue();
			}
		}
		return null;
	}

	@Override
	public ApplicationChannel getAllChannelByIdentity(String otherMucId, String otherAppId, String identity) {
		List<ApplicationChannel> allChannelList = applicationChannelCache.getAllChannelByIdentity(identity);
		for (ApplicationChannel applicationChannel : allChannelList) {
			if (TradeChannelConstants.TRADE_WECHAT_IDENTITY.equals(identity)) {
				WechatVo wechatVo = JSON.parseObject(applicationChannel.getParamsJson(), WechatVo.class);
				if (wechatVo.getAppId().equals(otherAppId) && wechatVo.getMchId().equals(otherMucId)) {
					return applicationChannel;
				}
			} else {
				return null;
			}
		}
		return null;
	}

	/**
	 * 判断该支付渠道信息是否存在于缓存
	 */
	@Override
	public boolean isExitsApplicationChannel(ApplicationChannel applicationChannel) {
		String id = applicationChannel.getId();
		ApplicationChannel cacheApplicationChannel = applicationChannelDao.findById(id);
		if (cacheApplicationChannel != null) {
			return applicationChannelCache.isExitsApplicationChannel(cacheApplicationChannel);
		}
		return false;
	}

	/**
	 * 排序
	 * @param list
	 */
	public void sortList(List<ApplicationChannel> list) {
		Collections.sort(list, new Comparator<ApplicationChannel>() {

			public int compare(ApplicationChannel o1, ApplicationChannel o2) {

				//升序排列  
				if (o1.getSeq() > o2.getSeq()) {
					return 1;
				}
				if (o1.getSeq() == o2.getSeq()) {
					return 0;
				}
				return -1;
			}
		});
	}

	@Override
	public void updateChannelViewName(String id, String channelViewName, String urlStatus) {
		applicationChannelDao.updateChannelViewName(id, channelViewName, urlStatus);
	}

	@Override
	public String getChannelIdentityWithCode(String channelCode) {
		String result = "";
		if (channelCode.indexOf(TradeChannelConstants.TRADE_WECHAT_IDENTITY) != -1) {
			result = TradeChannelConstants.TRADE_WECHAT_IDENTITY;
		} else if (channelCode.indexOf(TradeChannelConstants.TRADE_ALIPAY_IDENTITY) != -1) {
			result = TradeChannelConstants.TRADE_ALIPAY_IDENTITY;
		} else if (channelCode.indexOf(TradeChannelConstants.TRADE_UNIONPAY_IDENTITY) != -1) {
			result = TradeChannelConstants.TRADE_UNIONPAY_IDENTITY;
		} else if (channelCode.indexOf(TradeChannelConstants.TRADE_SUNSHIEN_IDENTITY) != -1) {
			result = TradeChannelConstants.TRADE_SUNSHIEN_IDENTITY;
		} else {
			result = "无法识别的支付平台";
		}
		return result;
	}

	@Override
	public Integer getChannelIdentityWithValue(String channelCode) {
		Integer result;
		if (channelCode.indexOf(TradeChannelConstants.TRADE_WECHAT_IDENTITY) != -1) {
			result = TradeChannelConstants.TRADE_WECHAT_IDENTITY_VALUE;
		} else if (channelCode.indexOf(TradeChannelConstants.TRADE_ALIPAY_IDENTITY) != -1) {
			result = TradeChannelConstants.TRADE_ALIPAY_IDENTITY_VALUE;
		} else if (channelCode.indexOf(TradeChannelConstants.TRADE_UNIONPAY_IDENTITY) != -1) {
			result = TradeChannelConstants.TRADE_UNIONPAY_IDENTITY_VALUE;
		} else if (channelCode.indexOf(TradeChannelConstants.TRADE_SUNSHIEN_IDENTITY) != -1) {
			result = TradeChannelConstants.TRADE_SUNSHIEN_IDENTITY_VALUE;
		} else {
			result = 0;
		}
		return result;
	}

	@Override
	public String getChannelViewName(String channelCode) {
		if (channelCode.equals(TradeChannelConstants.TRADE_CHANNEL_WECHAT_JSAPI)
				|| channelCode.equals(TradeChannelConstants.TRADE_CHANNEL_WECHAT_APP)
				|| channelCode.equals(TradeChannelConstants.TRADE_CHANNEL_WECHAT_H5)) {
			return DefautChannelViewNameConstants.WECHER_NAME;
		} else if (channelCode.equals(TradeChannelConstants.TRADE_CHANNEL_ALIPAY_H5)
				|| channelCode.equals(TradeChannelConstants.TRADE_CHANNEL_ALIPAY_APP)) {
			return DefautChannelViewNameConstants.ALIPAY_NAME;
		} else if (channelCode.equals(TradeChannelConstants.TRADE_CHANNEL_WECHAT_INSTCARD)
				|| channelCode.equals(TradeChannelConstants.TRADE_CHANNEL_ALIPAY_INSTCARD)
				|| channelCode.equals(TradeChannelConstants.TRADE_CHANNEL_SUNSHIEN_INSTCARD)) {
			return DefautChannelViewNameConstants.INSTCARD_NAME;
		} else if (channelCode.equals(TradeChannelConstants.TRADE_CHANNEL_UNIONPAY_H5)
				|| channelCode.equals(TradeChannelConstants.TRADE_CHANNEL_UNIONPAY_SDK)) {
			return DefautChannelViewNameConstants.ALIPAY_ONLINE_NAME;
		} else if (channelCode.equals(TradeChannelConstants.TRADE_CHANNEL_WECHAT_SCAN_CODE)) {
			return DefautChannelViewNameConstants.SCAN_NAME;
		} else if (channelCode.equals(TradeChannelConstants.TRADE_CHANNEL_UNIONPAY_APPLE_PAY)) {
			return DefautChannelViewNameConstants.ALIPAY_APPLE_PAY_NAME;
		} else if (channelCode.equals(TradeChannelConstants.TRADE_CHANNEL_UNIONPAY_DS)) {
			return DefautChannelViewNameConstants.ALIPAY_DS_NAME;
		} else if (channelCode.equals(TradeChannelConstants.TRADE_CHANNEL_SUNSHIEN_WALLET)) {
			return DefautChannelViewNameConstants.SUNSHIEN_WALLERT;
		} else {
			return "";
		}
	}

	@Override
	public void updateChannelParams(ApplicationChannel applicationChannel) {
		applicationChannelDao.updateChannelParams(applicationChannel);
	}

	@Override
	public void updateChannelOpen(ApplicationChannel applicationChannel) {
		applicationChannelDao.updateChannelOpen(applicationChannel);
	}
}

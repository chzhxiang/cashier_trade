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
package com.sunshine.platform.applicationchannel.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.sunshine.framework.exception.SystemException;
import com.sunshine.framework.mvc.mysql.dao.impl.BaseDaoImpl;
import com.sunshine.platform.applicationchannel.dao.ApplicationChannelDao;
import com.sunshine.platform.applicationchannel.entity.ApplicationChannel;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.platform.application_channel.dao.impl
 * @ClassName: ApplicationChannelDaoImpl
 * @Description: <p>应用支付渠道配置dao实现</p>
 * @JDK version used: 
 * @Author: 百部
 * @Create Date: 2017年9月13日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
@Repository
public class ApplicationChannelDaoImpl extends BaseDaoImpl<ApplicationChannel, String> implements ApplicationChannelDao {

	private static Logger logger = LoggerFactory.getLogger(ApplicationChannelDaoImpl.class);

	private final static String SQLNAME_FIND_BY_APPID = "findByAppId";
	private final static String SQLNAME_FIND_BY_IDS = "findByIds";
	private final static String SQLNAME_FIND_TRADE_CHANNEL_BY_APPID_CHANNEL_CODE = "findTradeChannelByAppIdChannelCode";
	private final static String SQLNAME_UPDATE_VIEWNAME = "updateChannelViewName";
	private final static String SQLNAME_UPDATE_PARAMS = "updateChannelParams";
	private final static String SQLNAME_UPDATE_OPEN = "updateChannelOpen";

	@Override
	public List<ApplicationChannel> findTradeChannelByAppId(String appId) {
		try {
			return sqlSession.selectList(getSqlName(SQLNAME_FIND_BY_APPID), appId);
		} catch (Exception e) {
			logger.error(String.format("根据merchantNo查询应用出错！语句：%s", getSqlName(SQLNAME_FIND_BY_APPID)), e);
			throw new SystemException(String.format("根据merchantNo查询应用出错！语句：%s", getSqlName(SQLNAME_FIND_BY_APPID)), e);
		}
	}

	@Override
	public ApplicationChannel findTradeChannelByAppIdChannelCode(String appId, String channelCode) {
		try {
			Map<String, Object> queryMap = new HashMap<String, Object>();
			queryMap.put("appId", appId);
			queryMap.put("channelCode", channelCode);
			return sqlSession.selectOne(getSqlName(SQLNAME_FIND_TRADE_CHANNEL_BY_APPID_CHANNEL_CODE), queryMap);
		} catch (Exception e) {
			logger.error(String.format("根据appId、channelCode查询应用支付渠道信息出错！语句：%s", getSqlName(SQLNAME_FIND_TRADE_CHANNEL_BY_APPID_CHANNEL_CODE)), e);
			throw new SystemException(
					String.format("根据appId、channelCode查询应用支付渠道信息出错！语句：%s", getSqlName(SQLNAME_FIND_TRADE_CHANNEL_BY_APPID_CHANNEL_CODE)), e);
		}
	}

	@Override
	public List<ApplicationChannel> findByIds(List<String> ids) {
		try {
			Assert.notNull(ids);
			Map<String, Object> queryMap = new HashMap<String, Object>();
			queryMap.put("ids", ids);
			return sqlSession.selectList(getSqlName(SQLNAME_FIND_BY_IDS), queryMap);
		} catch (Exception e) {
			logger.error(String.format("根据ID集合查询对象出错！语句：%s", getSqlName(SQLNAME_FIND_BY_IDS)), e);
			throw new SystemException(String.format("根据ID集合查询对象出错！语句：%s", getSqlName(SQLNAME_FIND_BY_IDS)), e);
		}
	}

	@Override
	public void updateChannelViewName(String id, String channelViewName, String urlStatus) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", id);
			map.put("channelViewName", channelViewName);
			map.put("urlStatus", urlStatus);
			sqlSession.update(getSqlName(SQLNAME_UPDATE_VIEWNAME), map);
		} catch (Exception e) {
			logger.error(String.format("根据id更新出错！语句：%s", getSqlName(SQLNAME_UPDATE_VIEWNAME)), e);
			throw new SystemException(String.format("根据id更新出错！语句：%s", getSqlName(SQLNAME_UPDATE_VIEWNAME)), e);
		}

	}

	@Override
	public void updateChannelParams(ApplicationChannel applicationChannel) {
		try {
			sqlSession.update(getSqlName(SQLNAME_UPDATE_PARAMS), applicationChannel);
		} catch (Exception e) {
			logger.error(String.format("更新渠道配置信息出错！语句：%s", getSqlName(SQLNAME_UPDATE_PARAMS)), e);
			throw new SystemException(String.format("更新渠道配置信息出错！语句：%s", getSqlName(SQLNAME_UPDATE_PARAMS)), e);
		}
	}

	@Override
	public void updateChannelOpen(ApplicationChannel applicationChannel) {
		try {
			sqlSession.update(getSqlName(SQLNAME_UPDATE_OPEN), applicationChannel);
		} catch (Exception e) {
			logger.error(String.format("更新渠道开启信息出错！语句：%s", getSqlName(SQLNAME_UPDATE_OPEN)), e);
			throw new SystemException(String.format("更新渠道开启信息出错！语句：%s", getSqlName(SQLNAME_UPDATE_OPEN)), e);
		}
	}

}

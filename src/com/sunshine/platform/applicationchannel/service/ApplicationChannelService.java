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
package com.sunshine.platform.applicationchannel.service;

import java.util.List;

import com.sunshine.framework.mvc.service.BaseService;
import com.sunshine.platform.applicationchannel.entity.ApplicationChannel;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.platform.application_channel.service
 * @ClassName: ApplicationChannelService
 * @Description: <p>应用支付渠道配置service</p>
 * @JDK version used: 
 * @Author: 百部
 * @Create Date: 2017年9月13日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public interface ApplicationChannelService extends BaseService<ApplicationChannel, String> {
	/**
	 * 根据appid查询相关信息
	 * @param appId
	 * @return
	 */
	public List<ApplicationChannel> findTradeChannelByAppId(String appId);

	/**
	 * 根据appId和channelCode（缓存中）获取应用支付渠道信息
	 * @param appId
	 * @param channelCode
	 * @return
	 */
	public ApplicationChannel getTradeChannel(String merchantNo, String appId, String channelCode);

	/**
	 * 根据appid跟交易渠道编码查询交易渠道信息
	 * @param appId
	 * @param channelCode
	 * @return
	 */
	public ApplicationChannel findTradeChannelByAppIdChannelCode(String appId, String channelCode);

	/**
	 * 配置的交易渠道信息放进缓存
	 */
	public void setTradeChannelCache(ApplicationChannel applicationChannel);

	/**
	 * 配置的交易渠道信息放进缓存(批量)
	 */
	public void setTradeChannelCache(List<String> list);

	/**
	 * 删除关闭的支付渠道参数缓存
	 */
	public void deleteTradeChannelCache(ApplicationChannel applicationChannel);

	/**
	 * 判断支付渠道缓存是否存在
	 */
	public boolean isExitsApplicationChannel(ApplicationChannel applicationChannel);

	/**
	 * 根据merchantNo、appId查询缓存中已开启的支付渠道配置信息
	 */
	public List<ApplicationChannel> getTradeChannelCacheByMerchantNoAppId(String merchantNo, String appId);

	/**
	 * 更新渠道配置信息（前台显示地址）
	 * @param id
	 * @param channelViewName
	 * @param urlStatus
	 */
	public void updateChannelViewName(String id, String channelViewName, String urlStatus);

	/**
	 * 更新支付渠道配置信息
	 */
	public void updateChannelParams(ApplicationChannel applicationChannel);

	/**
	 * 更新支付渠道开启信息
	 * @param applicationChannel
	 */
	public void updateChannelOpen(ApplicationChannel applicationChannel);

	/**
	 * 根据第三方支付分配的商户号、appid、标识获取一类的支付渠道配置信息，比如微信：wechat
	 * @param otherMucId
	 * @param otherAppId
	 * @param identity
	 * @return
	 */
	public ApplicationChannel getAllChannelByIdentity(String otherMucId, String otherAppId, String identity);

	/**
	 * channel标识值转channel编码
	 * @param appCodeVal
	 * @return
	 */
	String channelValConvertCode(int appCodeVal);

	/**
	 * channel编码转channel标识值
	 * @param appCode
	 * @return
	 */
	Integer channeCodeConvertVal(String appCode);

	/**
	 * 根据渠道编码获取支付平台标识
	 * @param channelCode
	 * @return
	 */
	String getChannelIdentityWithCode(String channelCode);

	/**
	 * 根据渠道编码获取支付平台标识值
	 * @param channelCode
	 * @return
	 */
	Integer getChannelIdentityWithValue(String channelCode);

	/**
	 * 获取支付渠道前台显示默认值
	 * @param channelCode
	 * @return
	 */
	String getChannelViewName(String channelCode);
}

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
package com.sunshine.platform.applicationchannel.dao;

import java.util.List;

import com.sunshine.framework.mvc.mysql.dao.BaseDao;
import com.sunshine.platform.applicationchannel.entity.ApplicationChannel;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.platform.application_channel.dao
 * @ClassName: ApplicationChannelDao
 * @Description: <p>应用支付渠道配置dao</p>
 * @JDK version used: 
 * @Author: 百部
 * @Create Date: 2017年9月13日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public interface ApplicationChannelDao extends BaseDao<ApplicationChannel, String> {

	/**
	 * 根据appId查询应用信息
	 * @param appid
	 * @return
	 */
	List<ApplicationChannel> findTradeChannelByAppId(String appId);

	/**
	 * 根据appId、渠道编码查询支付渠道信息
	 * @param merchantNo
	 * @param appId
	 * @param channelCode
	 * @return
	 */
	ApplicationChannel findTradeChannelByAppIdChannelCode(String appId, String channelCode);

	/**
	 * 修改支付渠道前台显示名称
	 * @param id
	 */
	void updateChannelViewName(String id, String channelViewName, String urlStatus);

	/**
	 * 更新支付渠道配置信息
	 */
	void updateChannelParams(ApplicationChannel applicationChannel);

	/**
	 * 更新支付渠道开启信息
	 * @param applicationChannel
	 */
	void updateChannelOpen(ApplicationChannel applicationChannel);

}

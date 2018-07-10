/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年7月25日</p>
 *  <p> Created by 赤芍</p>
 *  </body>
 * </html>
 */
package com.sunshine.platform.channel.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sunshine.common.GlobalConstant;
import com.sunshine.common.vo.TradeChannelVO;
import com.sunshine.framework.mvc.mysql.dao.BaseDao;
import com.sunshine.framework.mvc.mysql.service.impl.BaseServiceImpl;
import com.sunshine.platform.applicationchannel.dao.ApplicationChannelDao;
import com.sunshine.platform.applicationchannel.entity.ApplicationChannel;
import com.sunshine.platform.channel.dao.TradeChannelDao;
import com.sunshine.platform.channel.entity.TradeChannel;
import com.sunshine.platform.channel.service.TradeChannelService;

/**
 * 
 * @Project: cashier_trade 
 * @Package: com.sunshine.platform.channel.service.impl
 * @ClassName: TradeChannelServiceImpl
 * @Description: <p></p>
 * @JDK version used: 
 * @Author: 赤芍
 * @Create Date: 2017年9月12日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
@Service
public class TradeChannelServiceImpl extends BaseServiceImpl<TradeChannel, String> implements TradeChannelService {

	@Autowired
	protected TradeChannelDao tradeChannelDao;

	@Autowired
	protected ApplicationChannelDao applicationChannelDao;

	@Override
	protected BaseDao<TradeChannel, String> getDao() {
		return tradeChannelDao;
	}

	@Override
	public List<TradeChannelVO> loadAppTradeChannelList(String appId) {
		List<ApplicationChannel> hadChannel = applicationChannelDao.findTradeChannelByAppId(appId);
		List<TradeChannel> allChannel = tradeChannelDao.findAll();
		List<TradeChannelVO> channelList = new ArrayList<TradeChannelVO>();
		for (TradeChannel tradeChannel : allChannel) {
			boolean flag = false;
			TradeChannelVO channelVo = new TradeChannelVO();
			channelVo.setId(tradeChannel.getId());
			try {
				BeanUtils.copyProperties(channelVo, tradeChannel);
			} catch (Exception e) {
			}
			for (ApplicationChannel appChannel : hadChannel) {
				if (appChannel.getChannelCode().equals(tradeChannel.getCode())) { //此支付渠道已配置
					flag = true;
					channelVo.setIsOpen(appChannel.getIsOpen());
					channelVo.setConnect_id(appChannel.getId());
					break;
				}
			}
			if (flag) {
				channelVo.setHasConfig(GlobalConstant.YES);
			} else {
				channelVo.setHasConfig(GlobalConstant.NO);
			}
			channelList.add(channelVo);
		}
		return channelList;
	}

}

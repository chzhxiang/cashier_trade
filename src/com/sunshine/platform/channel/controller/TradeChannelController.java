/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年9月8日</p>
 *  <p> Created by 赤芍</p>
 *  </body>
 * </html>
 */
package com.sunshine.platform.channel.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.sunshine.common.GlobalConstant;
import com.sunshine.common.controller.BasePlatformController;
import com.sunshine.common.vo.TradeChannelVO;
import com.sunshine.framework.mvc.controller.RespBody;
import com.sunshine.framework.mvc.controller.RespBody.StatusEnum;
import com.sunshine.framework.mvc.service.BaseService;
import com.sunshine.platform.TradeChannelConstants;
import com.sunshine.platform.applicationchannel.entity.ApplicationChannel;
import com.sunshine.platform.applicationchannel.service.ApplicationChannelService;
import com.sunshine.platform.channel.entity.TradeChannel;
import com.sunshine.platform.channel.service.TradeChannelService;
import com.sunshine.platform.channel.vo.AlipayVo;
import com.sunshine.platform.channel.vo.UnionPayVo;
import com.sunshine.platform.channel.vo.WechatVo;
import com.sunshine.platform.channel.vo.YctInstcardVo;
import com.sunshine.platform.channel.vo.YctWalletVo;

/**
 * 
 * @Project: cashier_trade 
 * @Package: com.sunshine.platform.channel.controller
 * @ClassName: TradeChannelController
 * @Description: <p>支付渠道</p>
 * @JDK version used: 
 * @Author: 赤芍
 * @Create Date: 2017年9月12日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
@Controller
@RequestMapping("/tradeChannel")
public class TradeChannelController extends BasePlatformController<TradeChannel, String> {

	@Autowired
	protected TradeChannelService tradeChannelService;

	@Autowired
	protected ApplicationChannelService channelService;

	@Override
	protected BaseService<TradeChannel, String> getService() {
		return tradeChannelService;
	}

	protected String getPageBasePath() {
		return "/platform/merchant";
	}

	@RequestMapping("/toConfig")
	public ModelAndView toConfig(String channelCode) {
		ModelAndView modelAndView = new ModelAndView(getPageBasePath().concat("/tradechannelconfig/").concat(channelCode.toLowerCase()));
		modelAndView.addObject(GlobalConstant.MOTHED_INVOKE_RES_CHANNEL_CODE, channelCode);
		return modelAndView;
	}

	@RequestMapping("/toUpdateConfig")
	public ModelAndView toUpdateConfig(String channelCode, String appId) {
		ApplicationChannel applicationChannel = channelService.findTradeChannelByAppIdChannelCode(appId, channelCode);
		ModelAndView modelAndView = null;
		if (applicationChannel == null) {
			modelAndView = new ModelAndView(getPageBasePath() + "/tradechannelconfig/updateError");
			return modelAndView;
		}
		String lowerChannelCode = channelCode.toLowerCase();
		modelAndView = new ModelAndView(getPageBasePath().concat("/tradechannelconfig/").concat(lowerChannelCode));
		modelAndView.addObject(GlobalConstant.MOTHED_INVOKE_RES_APPLICATION_ID, applicationChannel.getId());
		if (lowerChannelCode.indexOf(TradeChannelConstants.TRADE_WECHAT_IDENTITY) != -1) {//微信相关支付
			WechatVo wechatVo = JSON.parseObject(applicationChannel.getParamsJson(), WechatVo.class);
			modelAndView.addObject(GlobalConstant.MOTHED_INVOKE_RES_ENTITY, wechatVo);
		} else if (lowerChannelCode.indexOf(TradeChannelConstants.TRADE_ALIPAY_IDENTITY) != -1) {//支付宝相关支付
			AlipayVo alipayVo = JSON.parseObject(applicationChannel.getParamsJson(), AlipayVo.class);
			modelAndView.addObject(GlobalConstant.MOTHED_INVOKE_RES_ENTITY, alipayVo);
		} else if (lowerChannelCode.indexOf(TradeChannelConstants.TRADE_UNIONPAY_IDENTITY) != -1) {//银联相关支付
			UnionPayVo unionPayVo = JSON.parseObject(applicationChannel.getParamsJson(), UnionPayVo.class);
			modelAndView.addObject(GlobalConstant.MOTHED_INVOKE_RES_ENTITY, unionPayVo);
		} else if (lowerChannelCode.indexOf(TradeChannelConstants.TRADE_SUNSHIEN_IDENTITY) != -1) {//阳光康众相关支付
			if (lowerChannelCode.equals(TradeChannelConstants.TRADE_CHANNEL_SUNSHIEN_INSTCARD)) {
				YctInstcardVo yctInstcardVo = JSON.parseObject(applicationChannel.getParamsJson(), YctInstcardVo.class);
				modelAndView.addObject(GlobalConstant.MOTHED_INVOKE_RES_ENTITY, yctInstcardVo);
			} else if (lowerChannelCode.equals(TradeChannelConstants.TRADE_CHANNEL_SUNSHIEN_WALLET)) {
				YctWalletVo yctWalletVo = JSON.parseObject(applicationChannel.getParamsJson(), YctWalletVo.class);
				modelAndView.addObject(GlobalConstant.MOTHED_INVOKE_RES_ENTITY, yctWalletVo);
			} else {
				return null;
			}
		} else {
			return null;
		}
		return modelAndView;
	}

	@ResponseBody
	@RequestMapping("/loadAppTradeChannelList")
	public RespBody loadAppTradeChannelList(String appId, HttpServletRequest request) {
		List<TradeChannelVO> appTradeChannelList = tradeChannelService.loadAppTradeChannelList(appId);
		return new RespBody(StatusEnum.OK, appTradeChannelList, "");
	}

	@RequestMapping("/toChange")
	public ModelAndView toChange(String channelCode, String appId) {
		ModelAndView modelAndView = new ModelAndView(getPageBasePath() + "/tradechannelconfig/editChannel");
		ApplicationChannel applicationChannel = channelService.findTradeChannelByAppIdChannelCode(appId, channelCode);
		modelAndView.addObject(GlobalConstant.MOTHED_INVOKE_RES_ENTITY, applicationChannel);
		return modelAndView;
	}

}

/**
 * <html>
 * <body>
 *  <P> Copyright 2017 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年10月12日</p>
 *  <p> Created by 党参</p>
 *  </body>
 * </html>
 */
package com.sunshine.trade.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.sunshine.common.GlobalConstant;
import com.sunshine.mobileapp.order.entity.TradeOrder;
import com.sunshine.mobileapp.order.service.TradeOrderService;
import com.sunshine.platform.applicationchannel.entity.ApplicationChannel;
import com.sunshine.platform.applicationchannel.service.ApplicationChannelService;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.trade.controller
 * @ClassName: SynchroController
 * @Description: <p>收银台同步跳转控制</p>
 * @JDK version used: 
 * @Author: 党参
 * @Create Date: 2017年10月12日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
@Controller
@RequestMapping("/synchro")
public class SynchroController {
	private Logger logger = LoggerFactory.getLogger(SynchroController.class);
	@Autowired
	private TradeOrderService orderService;

	@Autowired
	private ApplicationChannelService channelService;

	/**
	 * 支付同步跳转
	 * @param request
	 * @param response
	 */
	@RequestMapping("/callback")
	public ModelAndView callback(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		StringBuffer url = new StringBuffer();
		try {
			String cashierId = request.getParameter(GlobalConstant.MOTHED_INVOKE_RES_CASHIER_ID);
			logger.info("第三方同步跳转到达cashierId:{}", cashierId);
			if (!StringUtils.isEmpty(cashierId)) {
				TradeOrder order = orderService.findById(cashierId);
				if (order != null) {
					logger.info("同步跳转，订单号:{}, 商户订单号:{}, 回调地址:{}", order.getTradeNo(), order.getOutTradeNo(), order.getReturnUrl());
					ApplicationChannel channel = channelService.getTradeChannel(order.getMerchantNo(), order.getAppId(), order.getChannelCode());
					if (channel != null && GlobalConstant.PAY_LINK_PLATFORM == channel.getUrlStatus()) {
						url.append("/trade/merchantRedirect");
						if (!StringUtils.isEmpty(order.getReturnUrl())) {
							modelMap.put(GlobalConstant.MOTHED_INVOKE_RES_RETURN_URL, order.getReturnUrl());
							modelMap.put(GlobalConstant.MOTHED_INVOKE_RES_RETURN_TEXT, "返回商户");
						} else {
							// 无跳转地址则显示返回按钮区分扫码APP
							modelMap.put(GlobalConstant.MOTHED_INVOKE_RES_RETURN_TEXT, "关闭");
							String userAgent = request.getHeader("user-agent");
							logger.info("扫码应用userAgent{},{}", userAgent, order.getId());
							if (userAgent.toLowerCase().contains("micromessenger")) {
								//微信浏览器
								modelMap.put(GlobalConstant.MOTHED_INVOKE_RES_RETURN_URL, "javascript:WeixinJSBridge.call('closeWindow');");
							} else if (userAgent.toLowerCase().contains("alipay")) {
								//支付宝APP浏览器
								modelMap.put(GlobalConstant.MOTHED_INVOKE_RES_RETURN_URL, "javascript:AlipayJSBridge.call('exitApp');");
							}
						}
						modelMap.put("totalFee", String.valueOf(Double.valueOf(order.getTotalFee()) / 100.0));
						modelMap.put("merchantName", order.getMerchantName());
					} else {
						url.append("redirect:").append(order.getReturnUrl());
					}
				} else {
					logger.info("同步跳转，cashierId为:{}的订单未查询到", cashierId);
				}
			}
			logger.info("同步跳转地址:{}", url.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView(url.toString());
	}
}

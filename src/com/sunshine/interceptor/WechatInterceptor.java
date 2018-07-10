/**
 * <html>
 * <body>
 *  <P> Copyright 2017 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年9月19日</p>
 *  <p> Created by 党参</p>
 *  </body>
 * </html>
 */
package com.sunshine.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.sunshine.common.GlobalConstant;
import com.sunshine.framework.common.spring.ext.SpringContextHolder;
import com.sunshine.mobileapp.order.entity.TradeOrder;
import com.sunshine.mobileapp.order.service.impl.TradeOrderServiceImpl;
import com.sunshine.payment.wechat.WechatUtils;
import com.sunshine.platform.TradeChannelConstants;
import com.sunshine.platform.applicationchannel.entity.ApplicationChannel;
import com.sunshine.platform.applicationchannel.service.ApplicationChannelService;
import com.sunshine.platform.channel.vo.WechatVo;
import com.sunshine.restful.utils.RestUtils;
import com.sunshine.trade.service.RestAuthorizationService;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.interceptor
 * @ClassName: WechatInterceptor
 * @Description: <p>微信支付拦截器</p>
 * @JDK version used: 
 * @Author: 党参
 * @Create Date: 2017年9月19日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class WechatInterceptor implements HandlerInterceptor {
	private static Logger logger = LoggerFactory.getLogger(WechatInterceptor.class);
	RestAuthorizationService authorizationService = SpringContextHolder.getBean(RestAuthorizationService.class);

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3) throws Exception {
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3) throws Exception {

	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
		//请求地址
		logger.info("请求地址:{},参数:{},uri:{}", request.getRequestURL(), request.getQueryString(), request.getRequestURI());
		String requestUrl = String.valueOf(request.getRequestURL());
		//收银台请求Id
		String cashierId = request.getParameter(GlobalConstant.MOTHED_INVOKE_RES_CASHIER_ID);
		//支付渠道
		String channelCode = request.getParameter(GlobalConstant.MOTHED_INVOKE_RES_CHANNEL_CODE);
		/*RedisService redisService = SpringContextHolder.getBean(RedisService.class);
		//获取传送过来的
		String temporaryAuth = redisService.get(cashierId);
		logger.info("临时鉴权码:{}", temporaryAuth);
		if (StringUtils.isEmpty(redisService.get(cashierId))) {
			return false;
		} else {*/
		if (TradeChannelConstants.TRADE_CHANNEL_WECHAT_JSAPI.equals(channelCode)) {
			String requestUri = String.valueOf(request.getRequestURI());
			requestUri = requestUri.replace("//", "/");
			requestUrl = RestUtils.WECHAT_TRADE_DOMAIN_NAME.concat(requestUri);
			logger.info("微信普通商户支付。修正请求地址:{}", requestUrl);
			TradeOrderServiceImpl orderService = SpringContextHolder.getBean(TradeOrderServiceImpl.class);
			TradeOrder order = orderService.findById(cashierId);
			if (order != null && StringUtils.isEmpty(order.getOpenId())) {
				ApplicationChannelService channelService = SpringContextHolder.getBean(ApplicationChannelService.class);
				ApplicationChannel channel = channelService.findTradeChannelByAppIdChannelCode(order.getAppId(), channelCode);
				WechatVo wechatVo = JSON.parseObject(channel.getParamsJson(), WechatVo.class);
				String code = request.getParameter("code");
				String openId = "";
				if (!StringUtils.isEmpty(code)) {
					openId = WechatUtils.getOpenId(wechatVo.getAppId(), wechatVo.getSecret(), code);
					logger.info("get openid : {}", openId);
					order.setOpenId(openId);
					orderService.update(order);
					//redisService.del(cashierId);
					return true;
				} else {// 当code为空的时候,需要访问授权地址再获取code,之后会重定向回来再次进入拦截器，此时已经有code值回传
					String backParam =
							GlobalConstant.MOTHED_INVOKE_RES_CASHIER_ID.concat(GlobalConstant.STRING_ASSIGN_CHAR).concat(cashierId)
									.concat(GlobalConstant.STRING_AND_CHAR).concat(GlobalConstant.MOTHED_INVOKE_RES_CHANNEL_CODE)
									.concat(GlobalConstant.STRING_ASSIGN_CHAR).concat(channelCode);
					logger.info("backParam : {}", backParam);
					// 跳转微信授权获取 openId / 跳转支付宝授权获取 openId
					String redirectUrl = WechatUtils.getOAuth2(wechatVo.getAppId(), requestUrl.concat("?").concat(backParam));
					logger.info("getOAuth2 : {}", redirectUrl);
					response.sendRedirect(redirectUrl);
					return false;
				}
			} else {
				return true;
			}
		} else {
			return true;
		}

		//}

	}
}

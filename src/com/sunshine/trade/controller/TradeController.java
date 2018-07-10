/**
 * <html>
 * <body>
 *  <P> Copyright 2017 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年9月18日</p>
 *  <p> Created by 党参</p>
 *  </body>
 * </html>
 */
package com.sunshine.trade.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.sunshine.common.GlobalConstant;
import com.sunshine.mobileapp.order.entity.TradeOrder;
import com.sunshine.mobileapp.order.service.TradeOrderService;
import com.sunshine.payment.TradeConstant;
import com.sunshine.payment.TradeUtils;
import com.sunshine.platform.TradeChannelConstants;
import com.sunshine.platform.applicationchannel.entity.ApplicationChannel;
import com.sunshine.platform.applicationchannel.service.ApplicationChannelService;
import com.sunshine.restful.RestConstant;
import com.sunshine.restful.utils.RestUtils;
import com.sunshine.trade.service.PayService;
import com.sunshine.trade.service.RestAuthorizationService;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.trade.service.controller
 * @ClassName: TradeController
 * @Description: <p></p>
 * @JDK version used: 
 * @Author: 党参
 * @Create Date: 2017年9月18日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
@Controller
@RequestMapping("/trade")
public class TradeController {
	private Logger logger = LoggerFactory.getLogger(TradeController.class);

	@Autowired
	private TradeOrderService orderService;

	@Autowired
	private ApplicationChannelService channelService;

	@Autowired
	private PayService payService;

	@Autowired
	RestAuthorizationService authorizationService;

	@RequestMapping(value = "/test")
	public ModelAndView test(HttpServletRequest request, ModelMap modelMap) {
		return new ModelAndView("trade/test");
	}

	/**
	 * 收银台中心
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/cashierCenter")
	public ModelAndView cashierCenter(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "X-Requested-With,Content-Type,Content-Length,Authorization,Account,Id");
		response.setHeader("Content-Type", "text/html;charset=UTF-8");
		ModelAndView view = new ModelAndView("trade/paymentError");
		Map<String, String> params = RestUtils.getRequestMap(request.getParameterMap());
		Map<String, String> retMap = authorizationService.authorization(params);
		if (retMap.isEmpty()) {
			String cashierId = request.getParameter(GlobalConstant.MOTHED_INVOKE_RES_CASHIER_ID);
			String merchantNo = request.getParameter(RestConstant.PARAMS_MERCHANTNO);
			String appId = request.getParameter(RestConstant.PARAMS_APPID);
			TradeOrder order = orderService.findById(cashierId);
			if (order == null) {
				retMap.put(RestUtils.RETURN_CODE_KEY, RestConstant.RETURN_OUTORDERNO_NO_EXIST[0]);
				retMap.put(RestUtils.RETURN_MSG_KEY, RestConstant.RETURN_OUTORDERNO_NO_EXIST[1]);
			} else {
				//判断商户号和APPID是否错误
				if (!merchantNo.equals(order.getMerchantNo()) || !appId.equals(order.getAppId())) {
					retMap.put(RestUtils.RETURN_CODE_KEY, RestConstant.RETURN_INVALID_APPID_OR_MERCHANTNO[0]);
					retMap.put(RestUtils.RETURN_MSG_KEY, RestConstant.RETURN_INVALID_APPID_OR_MERCHANTNO[1]);
				} else if (TradeConstant.TRADE_STATUS_PAYMENT == order.getTradeStatus()
						|| TradeConstant.TRADE_STATUS_REFUND == order.getTradeStatus()
						|| TradeConstant.TRADE_STATUS_REFUNDING == order.getTradeStatus()
						|| TradeConstant.TRADE_STATUS_CLOSE == order.getTradeStatus()) {
					retMap.put(RestUtils.RETURN_CODE_KEY, RestConstant.RETURN_OUTORDERNO_USED[0]);
					retMap.put(RestUtils.RETURN_MSG_KEY, RestConstant.RETURN_OUTORDERNO_USED[1]);
				} else {
					modelMap.put(GlobalConstant.MOTHED_INVOKE_RES_CASHIER_ID, cashierId);
					modelMap.put(GlobalConstant.MOTHED_INVOKE_RES_IS_SUCCESS, true);
					view = new ModelAndView("trade/cashierCenter");
				}
			}
		}
		modelMap.putAll(retMap);
		return view;
	}

	/**
	 * 获取支付方式
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getChoicePayment")
	public Object getChoicePayment(HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "X-Requested-With,Content-Type,Content-Length,Authorization,Account,Id");
		response.setHeader("Content-Type", "text/html;charset=UTF-8");
		Map<String, Object> retMap = new HashMap<String, Object>();
		String cashierId = request.getParameter(GlobalConstant.MOTHED_INVOKE_RES_CASHIER_ID);
		retMap.put(GlobalConstant.MOTHED_INVOKE_RES_CASHIER_ID, cashierId);
		try {
			TradeOrder order = orderService.findById(cashierId);
			List<ApplicationChannel> channels = channelService.getTradeChannelCacheByMerchantNoAppId(order.getMerchantNo(), order.getAppId());
			logger.info("支付参数:{}", JSON.toJSON(channels));
			retMap.put(GlobalConstant.MOTHED_INVOKE_RES_ENTITIES, channels);
			retMap.put(GlobalConstant.MOTHED_INVOKE_RES_IS_SUCCESS, true);
		} catch (Exception e) {
			e.printStackTrace();
			retMap.put(GlobalConstant.MOTHED_INVOKE_RES_MSG, e.getMessage());
			retMap.put(GlobalConstant.MOTHED_INVOKE_RES_IS_SUCCESS, false);
		}
		return retMap;
	}

	/**
	 * 支付
	 * @param request
	 * @param order
	 * @return
	 */
	@RequestMapping("/payment")
	public ModelAndView payment(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "X-Requested-With,Content-Type,Content-Length,Authorization,Account,Id");
		response.setHeader("Content-Type", "text/html;charset=UTF-8");
		ModelAndView view = null;
		Map<String, Object> retMap = new HashMap<String, Object>();
		try {
			String cashierId = request.getParameter(GlobalConstant.MOTHED_INVOKE_RES_CASHIER_ID);
			String channelCode = request.getParameter(GlobalConstant.MOTHED_INVOKE_RES_CHANNEL_CODE);
			TradeOrder order = orderService.findById(cashierId);
			order.setSpbillCreateIp(getIpAddr(request));
			retMap = payService.polymerizationPay(order, channelCode);
			Boolean isException = (Boolean) retMap.get(GlobalConstant.TRADE_IS_EXCEPTION);
			Boolean isSuccess = (Boolean) retMap.get(GlobalConstant.TRADE_IS_SUCCESS);
			if (!isException && isSuccess) {
				retMap.put(GlobalConstant.MOTHED_INVOKE_RES_CHANNEL_CODE, channelCode);
				String tradePlatform = channelService.getChannelIdentityWithCode(channelCode);
				view = new ModelAndView("trade/" + tradePlatform + "/payment_" + channelCode);
			} else {
				int tradeStatus = Integer.parseInt(retMap.get(GlobalConstant.TRADE_ORDER_STATE).toString());
				if (TradeConstant.TRADE_STATUS_PAYMENT == tradeStatus) {//已支付，跳转至已支付页面
					view = new ModelAndView("redirect:" + TradeConstant.getSynchroCallback(order.getId()));
				} else {//其他状态，提示订单重复
					retMap.put(RestUtils.RETURN_CODE_KEY, RestConstant.RETURN_OUTORDERNO_USED[0]);
					retMap.put(RestUtils.RETURN_MSG_KEY, RestConstant.RETURN_OUTORDERNO_USED[1]);
					view = new ModelAndView("trade/paymentError");
				}
			}
		} catch (Exception e) {
			retMap.put(GlobalConstant.TRADE_IS_EXCEPTION, true);
			retMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
			retMap.put(GlobalConstant.TRADE_FAIL_MSG, e.getMessage());
			e.printStackTrace();
		}
		modelMap.putAll(retMap);
		logger.info("返回至前端的结果:{}，url:{}", JSON.toJSONString(retMap), view.getViewName());
		return view;
	}

	/**
	 * 支付
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/paymentJson")
	public Object paymentJson(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		String cashierId = request.getParameter(GlobalConstant.MOTHED_INVOKE_RES_CASHIER_ID);
		String channelCode = request.getParameter(GlobalConstant.MOTHED_INVOKE_RES_CHANNEL_CODE);
		try {
			TradeOrder order = orderService.findById(cashierId);
			if (StringUtils.isEmpty(order.getSpbillCreateIp())) {
				order.setSpbillCreateIp(getIpAddr(request));
			}
			retMap = payService.polymerizationPay(order, channelCode);
			retMap.put(GlobalConstant.MOTHED_INVOKE_RES_CHANNEL_CODE, channelCode);
		} catch (Exception e) {
			retMap.put(GlobalConstant.TRADE_IS_EXCEPTION, true);
			retMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
			retMap.put(GlobalConstant.TRADE_FAIL_MSG, e.getMessage());
			e.printStackTrace();
		}
		logger.info("返回至前端的结果:{}", JSON.toJSONString(retMap));
		return retMap;
	}

	/**
	 * 跳转支付页面
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/cashierPayment")
	public ModelAndView cashierPayment(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		try {
			String cashierId = request.getParameter(GlobalConstant.MOTHED_INVOKE_RES_CASHIER_ID);
			String channelCode = request.getParameter(GlobalConstant.MOTHED_INVOKE_RES_CHANNEL_CODE);
			modelMap.put(GlobalConstant.MOTHED_INVOKE_RES_CASHIER_ID, cashierId);
			modelMap.put(GlobalConstant.MOTHED_INVOKE_RES_CHANNEL_CODE, channelCode);
			modelMap.put(GlobalConstant.MOTHED_INVOKE_RES_PAY_IDENTITY, TradeChannelConstants.getChannelIdentityWithCode(channelCode));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("/trade/cashierPayment");
	}

	/**
	 * 继续支付
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/continuePayment")
	public Object continuePayment(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		String cashierId = request.getParameter(GlobalConstant.MOTHED_INVOKE_RES_CASHIER_ID);
		String channelCode = request.getParameter(GlobalConstant.MOTHED_INVOKE_RES_CHANNEL_CODE);
		logger.info("继续支付查询参数cashierId:{}, channelCode:{}", cashierId, channelCode);
		try {
			TradeOrder order = orderService.findById(cashierId);
			logger.info("继续支付查询订单:{}", JSON.toJSONString(order));
			if (order != null) {
				if (TradeConstant.TRADE_STATUS_PAYMENTING == order.getTradeStatus()) {//如果订单为支付中，则查询第三方支付平台是否可以支付
					String paramsJson = payService.getTradeParams(order.getId());
					logger.info("继续支付查询支付参数结果:{}", paramsJson);
					if (!StringUtils.isEmpty(paramsJson)) {
						Map<String, Object> queryMap = TradeUtils.paymentQuery(order, paramsJson);
						Boolean isException = (Boolean) queryMap.get(GlobalConstant.TRADE_IS_EXCEPTION);
						Boolean isSuccess = (Boolean) queryMap.get(GlobalConstant.TRADE_IS_SUCCESS);
						if (!isException) {
							if (isSuccess) {
								order.setTradeStatus(Integer.parseInt(queryMap.get(GlobalConstant.TRADE_ORDER_STATE).toString()));
							} else {
								order.setTradeStatus(TradeConstant.TRADE_STATUS_NO_PAYMENT);
							}
							orderService.updateNotWithNull(order);
						}
						retMap.put(GlobalConstant.TRADE_ORDER_STATE, TradeConstant.tradeStatus.get(order.getTradeStatus()));
						retMap.put(GlobalConstant.MOTHED_INVOKE_RES_RETURN_URL, order.getReturnUrl());
						retMap.put(GlobalConstant.TRADE_IS_SUCCESS, true);
					} else {
						retMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
						retMap.put(GlobalConstant.TRADE_FAIL_MSG, "网络异常,请稍后再试!");
					}
				} else {
					retMap.put(GlobalConstant.TRADE_IS_SUCCESS, true);
					retMap.put(GlobalConstant.TRADE_ORDER_STATE, TradeConstant.tradeStatus.get(order.getTradeStatus()));
					retMap.put(GlobalConstant.MOTHED_INVOKE_RES_RETURN_URL, order.getReturnUrl());
				}
			} else {
				retMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
				retMap.put(GlobalConstant.TRADE_FAIL_MSG, "订单未查询到订单,请稍候再试!");
			}
			retMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
		} catch (Exception e) {
			retMap.put(GlobalConstant.TRADE_IS_EXCEPTION, true);
			retMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
			retMap.put(GlobalConstant.TRADE_FAIL_MSG, e.getMessage());
			e.printStackTrace();
		}
		logger.info("返回至前端的结果:{}", JSON.toJSONString(retMap));
		return retMap;
	}

	/**
	 * 支付回调
	 * @param request
	 * @param response
	 */
	@RequestMapping("/callback")
	public void callback(HttpServletRequest request, HttpServletResponse response) {
		logger.info("测试异步回调数据:{}", JSON.toJSONString(RestUtils.getRequestMap(request.getParameterMap())));
		try {
			PrintWriter out = response.getWriter();
			out.println("success");
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 获取IP
	 * @param request
	 * @return
	 */
	public String getIpAddr(HttpServletRequest request) {
		String ipAddress = request.getHeader("X-Forwarded-For");
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
				//根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				ipAddress = inet.getHostAddress();
			}
		}
		//对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ipAddress != null && ipAddress.length() > 15) { //"***.***.***.***".length() = 15
			if (ipAddress.indexOf(",") > 0) {
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
			}
		}
		return ipAddress;
	}

	/**
	 * 支付二维码分派中心。
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/qrCodeCenter")
	public ModelAndView qrCodeCenter(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		logger.info("支付二维码分派中心{}", request.getQueryString());
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "X-Requested-With,Content-Type,Content-Length,Authorization,Account,Id");
		response.setHeader("Content-Type", "text/html;charset=UTF-8");
		ModelAndView view = new ModelAndView("trade/paymentError");
		Map<String, String> retMap = new HashMap<String, String>();
		//if (retMap.isEmpty()) {
		String cashierId = request.getParameter(GlobalConstant.MOTHED_INVOKE_RES_CASHIER_ID);
		String merchantNo = request.getParameter(RestConstant.PARAMS_MERCHANTNO);
		String appId = request.getParameter(RestConstant.PARAMS_APPID);
		TradeOrder order = orderService.findById(cashierId);
		if (order == null) {
			logger.info("商户订单不存在.cashierId={}", cashierId);
			retMap.put(RestUtils.RETURN_CODE_KEY, RestConstant.RETURN_OUTORDERNO_NO_EXIST[0]);
			retMap.put(RestUtils.RETURN_MSG_KEY, RestConstant.RETURN_OUTORDERNO_NO_EXIST[1]);
			modelMap.putAll(retMap);
			return view;
		}
		// 订单已超时支付
		Date curTime = new java.util.Date();
		if (order.getOutTime() != null && curTime.after(order.getOutTime())) {
			logger.info("订单已超时不可支付.cashierId={},outTime={}", cashierId, order.getOutTime());
			retMap.put(RestUtils.RETURN_CODE_KEY, RestConstant.RETURN_TIME_STAMP_ERROR[0]);
			retMap.put(RestUtils.RETURN_MSG_KEY, "订单超时支付");
			modelMap.putAll(retMap);
			return view;
		}
		//判断商户号和APPID是否错误
		if (!merchantNo.equals(order.getMerchantNo()) || !appId.equals(order.getAppId())) {
			logger.info("商户号和APPID出错.cashierId={},merchantNo={}|{},appId={}|{}", cashierId, merchantNo, order.getMerchantNo(), appId,
					order.getAppId());
			retMap.put(RestUtils.RETURN_CODE_KEY, RestConstant.RETURN_INVALID_APPID_OR_MERCHANTNO[0]);
			retMap.put(RestUtils.RETURN_MSG_KEY, RestConstant.RETURN_INVALID_APPID_OR_MERCHANTNO[1]);
			modelMap.putAll(retMap);
			return view;
		}
		// 订单状态判断
		if (TradeConstant.TRADE_STATUS_PAYMENT == order.getTradeStatus() || TradeConstant.TRADE_STATUS_REFUND == order.getTradeStatus()
					|| TradeConstant.TRADE_STATUS_REFUNDING == order.getTradeStatus() || TradeConstant.TRADE_STATUS_CLOSE == order.getTradeStatus()) {
			logger.info("订单状态不可支付.cashierId={},TradeStatus={}", cashierId, order.getTradeStatus());
			retMap.put(RestUtils.RETURN_CODE_KEY, RestConstant.RETURN_OUTORDERNO_USED[0]);
			retMap.put(RestUtils.RETURN_MSG_KEY, RestConstant.RETURN_OUTORDERNO_USED[1]);
			modelMap.putAll(retMap);
			return view;
		}

		// 区分扫码APP
		String userAgent = request.getHeader("user-agent");
		logger.info("扫码应用userAgent{},{}", userAgent, order.getId());
		if (userAgent.toLowerCase().contains("micromessenger")) {
			//微信浏览器
			String url = "redirect:" + TradeConstant.getWechatPaymentUrl(cashierId, "wechat_jsapi");
			logger.info("微信浏览器跳转url={}", url);
			view = new ModelAndView(url);
			return view;
		} else if (userAgent.toLowerCase().contains("alipay")) {
			//支付宝APP浏览器
			view = new ModelAndView("redirect:" + TradeConstant.getPaymentUrl(cashierId, "alipay_h5"));
			return view;
		} else {
			logger.info("不支持扫码应用.cashierId={},userAgent={}", cashierId, userAgent);
			retMap.put(RestUtils.RETURN_CODE_KEY, "");
			retMap.put(RestUtils.RETURN_MSG_KEY, "暂只支持支付宝/微信扫码支付");

		}
		modelMap.putAll(retMap);
		return view;
	}
}

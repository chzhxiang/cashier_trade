/**
 * <html>
 * <body>
 *  <P> Copyright 2017 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年9月21日</p>
 *  <p> Created by 党参</p>
 *  </body>
 * </html>
 */
package com.sunshine.trade.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.sunshine.common.GlobalConstant;
import com.sunshine.mobileapp.order.entity.TradeOrder;
import com.sunshine.mobileapp.order.service.TradeOrderService;
import com.sunshine.payment.TradeConstant;
import com.sunshine.payment.wechat.ResponseHandler;
import com.sunshine.payment.wechat.WechatUtils;
import com.sunshine.platform.TradeChannelConstants;
import com.sunshine.platform.applicationchannel.entity.ApplicationChannel;
import com.sunshine.platform.applicationchannel.service.impl.ApplicationChannelServiceImpl;
import com.sunshine.platform.channel.vo.WechatVo;
import com.sunshine.restful.RestConstant;
import com.sunshine.restful.utils.RestUtils;
import com.sunshine.thread.ExecuteNotifyUrlRunnable;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.trade.controller
 * @ClassName: NotifyWechatController
 * @Description: <p>微信回调处理类</p>
 * @JDK version used: 
 * @Author: 党参
 * @Create Date: 2017年9月21日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
@Controller
@RequestMapping("/notifyWechat")
public class NotifyWechatController {

	private Logger logger = LoggerFactory.getLogger(NotifyWechatController.class);

	@Autowired
	private TradeOrderService orderService;

	@Autowired
	private ApplicationChannelServiceImpl channelService;

	/**
	 * 微信支付回调
	 * @param request
	 * @param response
	 */
	@RequestMapping("/callback")
	public void callback(HttpServletRequest request, HttpServletResponse response) {
		try {
			ResponseHandler resHandler = new ResponseHandler(request, response);
			Map<String, String> postdata = resHandler.getSmap();
			String result = "<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>";
			logger.info("微信支付异步回调数据:{}", JSON.toJSONString(postdata));
			String outTradeNo = postdata.get("out_trade_no"); // 商户订单号
			String agtTradeNo = postdata.get("transaction_id"); // 财付通订单号
			String tradeState = postdata.get("result_code"); // 支付结果
			String tradeTime = postdata.get("time_end");// 支付完成时间,即交易时间
			String attach = postdata.get("attach");
			Map<String, Object> attachMap = JSON.parseObject(attach);
			String cashierId = attachMap.get(GlobalConstant.MOTHED_INVOKE_RES_CASHIER_ID).toString();
			String channelCode = attachMap.get(RestConstant.PARAM_CHANNELCODE).toString();
			TradeOrder order = orderService.findById(cashierId);
			if (order != null) {
				StringBuffer logContent = new StringBuffer("微信回调通知:");
				ApplicationChannel channel = channelService.findTradeChannelByAppIdChannelCode(order.getAppId(), channelCode);
				WechatVo wechatVo = JSON.parseObject(channel.getParamsJson(), WechatVo.class);

				if (WechatUtils.checkSign(postdata, wechatVo.getPaySecret())) {
					order.setPayJson(JSON.toJSONString(postdata));//存储微信回调结果
					if (WechatUtils.TRADE_IS_SUCCESS.equalsIgnoreCase(tradeState)) {
						order.setAgtTradeNo(agtTradeNo);
						order.setPayTime(GlobalConstant.parseyyyymmddhhmmss(tradeTime));
						order.setTradeStatus(TradeConstant.TRADE_STATUS_PAYMENT);
						order.setIsSuccess(TradeConstant.ORDER_SUCCESS_TRUE);
						logContent.append("支付成功,微信订单号:").append(agtTradeNo).append("支付时间:")
								.append(GlobalConstant.formatYYYYMMDDHHMMSS(order.getPayTime()));
					} else {
						logContent.append("支付失败，失败原因").append("return_msg:").append(postdata.get("return_msg"));
					}

				} else {
					logContent.append("验签失败,平台订单号:").append(outTradeNo).append("微信订单号:").append(agtTradeNo).append("支付时间:")
							.append(GlobalConstant.formatYYYYMMDDHHMMSS(order.getPayTime()));
					result = "<xml><return_code><![CDATA[FAIL]]></return_code></xml>";
				}
				//返回支付渠道，与订单记录的支付渠道不相同
				if (!channelCode.equals(order.getChannelCode())) {
					logContent.append("。支付方式不对应,支付渠道编码为:").append(channelCode).append("可能为重复支付，请联系支付组");
				}
				order.formatHandleLog(logContent.toString());
				new Thread(new ExecuteNotifyUrlRunnable(order)).start();
			} else {
				logger.info("未查询到订单:{}.", cashierId);
			}
			resHandler.sendToCFT(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Description: (描述)-微信退费回调
	 * @param request
	 * @param response
	 * @return void
	 *
	 */
	@RequestMapping("/wechatRefundNotify")
	public void wechatRefundNotify(HttpServletRequest request, HttpServletResponse response) {
		ResponseHandler resHandler = new ResponseHandler(request, response);
		Map<String, String> postdata = resHandler.getSmap();
		logger.info("微信退款异步回调数据:{}", JSON.toJSONString(postdata));
		String returnCode = postdata.get("return_code");
		String appId = postdata.get("appid");
		String mchId = postdata.get("mch_id"); //退款的商户号
		String reqInfo = postdata.get("req_info"); // 重要数据的加密字符串
		ApplicationChannel channel = channelService.getAllChannelByIdentity(mchId, appId, TradeChannelConstants.TRADE_WECHAT_IDENTITY);
		WechatVo wechatVo = JSON.parseObject(channel.getParamsJson(), WechatVo.class);
		try {
			Map<String, String> refuntNotifyCode = WechatUtils.getWechatRefuntNotifyCode(wechatVo.getPaySecret(), reqInfo);
			String tradPayOrder = refuntNotifyCode.get("out_trade_no");
			String tradrefundNo = refuntNotifyCode.get("out_refund_no");
			String refundStatus = refuntNotifyCode.get("refund_status");
			String returnMesg = "";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("tradeNo", tradPayOrder); //平台订单号
			params.put("refundNo", tradrefundNo); //平台退费单号
			TradeOrder order = orderService.getTradeOrderByAppIdAndOutTradeNo(params);
			if (returnCode.equals("SUCCESS") && refundStatus.equals("SUCCESS")) {
				String content = String.format("第3方退款成功");
				logger.info(content);
				order.setTradeStatus(TradeConstant.TRADE_STATUS_REFUND); //状态改为已退费
				order.formatHandleLog(content);
				orderService.updateTradeOrderForDbAndCache(order);
				returnMesg = "<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>";
				resHandler.sendToCFT(returnMesg);
			} else {
				String content = String.format("第3方退款失败");
				logger.info(content);
				order.formatHandleLog(content);
				orderService.updateTradeOrderForDbAndCache(order);
				returnMesg = "<xml><return_code><![CDATA[FAIL]]></return_code></xml>";
				resHandler.sendToCFT(returnMesg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("退款回调处理出错：{}", e);
		}
	}

	/**
	 * 微信支付回调
	 * @param request
	 * @param response
	 */
	@RequestMapping("/test")
	public void test(HttpServletRequest request, HttpServletResponse response) {
		//logger.info("测试异步回调数据:{}", JSON.toJSONString(request.getParameterMap()));

		logger.info("测试异步回调数据:{}", JSON.toJSONString(RestUtils.getRequestMap(request.getParameterMap())));
		try {
			PrintWriter out = response.getWriter();
			out.println("成功");
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 微信H5支付同步跳转
	 * @param request
	 * @param response
	 */
	@RequestMapping("/wechatSync")
	public ModelAndView wechatSync(HttpServletRequest request, HttpServletResponse response) {
		StringBuffer url = new StringBuffer("redirect:");
		String cashierId = request.getParameter(GlobalConstant.MOTHED_INVOKE_RES_CASHIER_ID);
		TradeOrder order = orderService.findById(cashierId);
		url.append(order.getReturnUrl());
		return new ModelAndView(url.toString());
	}
}

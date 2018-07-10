/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年9月27日</p>
 *  <p> Created by hqy</p>
 *  </body>
 * </html>
 */
package com.sunshine.trade.controller;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.sunshine.common.GlobalConstant;
import com.sunshine.mobileapp.order.entity.TradeOrder;
import com.sunshine.mobileapp.order.service.TradeOrderService;
import com.sunshine.payment.TradeConstant;
import com.sunshine.payment.alipay.AlipayCore;
import com.sunshine.payment.alipay.AlipayNotify;
import com.sunshine.payment.alipay.AlipayUtils;
import com.sunshine.restful.RestConstant;
import com.sunshine.thread.ExecuteNotifyUrlRunnable;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.trade.controller
 * @ClassName: NotifyAlipayController
 * @Description: <p>支付宝回调处理类</p>
 * @JDK version used: 
 * @Author: 百部
 * @Create Date: 2017年9月27日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
@Controller
@RequestMapping("/notifyAlipay")
public class NotifyAlipayController {
	private Logger logger = LoggerFactory.getLogger(NotifyWechatController.class);

	@Autowired
	private TradeOrderService orderService;

	/**
	 * 支付回调
	 * @param request
	 * @param response
	 */
	@RequestMapping("/callback")
	public void callback(HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> params = AlipayCore.dealNotify(request.getParameterMap());
		logger.info("支付宝支付异步回调参数params：{}", params);
		try {
			if (!params.isEmpty()) {
				boolean verifyNotify = AlipayNotify.verifyQr(params, params.get("notify_id"), params.get("seller_id"), AlipayUtils.ALI_PUBLIC_KEY);
				String result = "fail";
				if (verifyNotify) {
					result = "success";
					String agtTradeNo = params.get("trade_no");
					// 交易状态
					String tradeStatus = params.get("trade_status");
					String tradeTime = params.get("gmt_payment");
					String passbackParams = params.get("passback_params");
					Map<String, Object> passbackParamsMap = JSON.parseObject(passbackParams);
					String cashierId = passbackParamsMap.get(GlobalConstant.MOTHED_INVOKE_RES_CASHIER_ID).toString();
					String channelCode = passbackParamsMap.get(RestConstant.PARAM_CHANNELCODE).toString();
					TradeOrder order = orderService.findById(cashierId);
					if (order != null) {
						order.setPayJson(JSON.toJSONString(params));//存支付宝回调结果
						StringBuffer logContent = new StringBuffer("支付宝回调通知");
						if (AlipayUtils.STATUS_WAIT_BUYER_PAY.equals(tradeStatus)) {//支付中
							logContent.append("支付中:").append(AlipayUtils.tradeStatusMap.get(tradeStatus));
						} else if (AlipayUtils.STATUS_TRADE_SUCCESS.equals(tradeStatus) || AlipayUtils.STATUS_TRADE_FINISHED.equals(tradeStatus)) {//支付成功
							order.setAgtTradeNo(agtTradeNo);
							order.setPayTime(GlobalConstant.parseYYYYMMDDHHMMSS(tradeTime));
							order.setTradeStatus(TradeConstant.TRADE_STATUS_PAYMENT);
							if (AlipayUtils.STATUS_TRADE_FINISHED.equals(tradeStatus)) {//支付成功,不能退款
								logContent.append("交易结束，不可退款.");
							} else {
								logContent.append("交易支付成功.");
							}
							logContent.append("支付宝订单号:").append(agtTradeNo);
							logContent.append("支付时间:").append(GlobalConstant.formatYYYYMMDDHHMMSS(order.getPayTime()));

						} else if (AlipayUtils.STATUS_TRADE_CLOSED.equals(tradeStatus)) {//退费成功
							order.setTradeStatus(TradeConstant.TRADE_STATUS_REFUND);
							logContent.append("订单关闭(退费成功),支付宝订单号:").append(agtTradeNo).append("支付时间:")
									.append(GlobalConstant.formatYYYYMMDDHHMMSS(order.getPayTime()));
						} else {
							logContent.append("第3方支付失败，失败原因:").append(AlipayUtils.tradeStatusMap.get(tradeStatus));
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
					logger.info("回复支付宝结果:{}.", result);
					PrintWriter out = response.getWriter();
					response.setCharacterEncoding(TradeConstant.INPUT_CHARSET);
					response.setContentType("text/xml; charset=" + TradeConstant.INPUT_CHARSET);
					out.print(result);
				} else {
					logger.info("验签失败:{}.", request.getRequestURI());
				}
			} else {
				logger.info("请求参数为空，URL:{}, 获取参数:{}.", request.getRequestURI(), request.getQueryString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

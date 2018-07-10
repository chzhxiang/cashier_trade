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
import com.sunshine.mobileapp.order.service.TradeOrderService;
import com.sunshine.payment.TradeConstant;
import com.sunshine.payment.unionpay.UnionPayUtils;
import com.sunshine.thread.UnionExecuteNotifyUrlRunnable;

/**
 * @Project: cashier_trade
 * @Package: com.sunshine.trade.controller
 * @ClassName: NotifyUnionPayController
 * @Description: 银联回调处理类
 * @JDK version used: 
 * @Author: 熊胆
 * @Create Date: 2017年10月23日下午2:05:53
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
@Controller
@RequestMapping("/notifyUnionPay")
public class NotifyUnionPayController {

	private Logger logger = LoggerFactory.getLogger(NotifyUnionPayController.class);

	@Autowired
	private TradeOrderService orderService;

	/**
	 * 银联商户二维码生成异步回调
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年10月23日下午2:07:34
	 */
	@RequestMapping("/qrCode/callback/")
	public void callback(HttpServletRequest request, HttpServletResponse response) {
		try {
			// 获取银联通知服务器发送的后台通知参数
			Map<String, String> valideData = UnionPayUtils.getAllRequestParam(request);
			logger.info("银联商户主扫二维码回调验签参数(valideData):{}", JSON.toJSONString(valideData));
			String result = "ok";
			boolean validate = UnionPayUtils.unionValidate(valideData);
			logger.info("验签结果:{}", validate);
			if (validate) {
				new Thread(new UnionExecuteNotifyUrlRunnable(valideData)).start();
			} else {
				result = "fail";
			}
			// 返回给银联服务器http 200 状态码
			PrintWriter out = response.getWriter();
			response.setCharacterEncoding(TradeConstant.INPUT_CHARSET);
			response.setContentType("text/xml; charset=" + TradeConstant.INPUT_CHARSET);
			logger.info("商户银联二维码异步回调通知银联结果:{}", result);
			out.print(result);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("生成银联主扫二维码异步接口异常错误日志：" + e.getMessage());
		}
	}

}

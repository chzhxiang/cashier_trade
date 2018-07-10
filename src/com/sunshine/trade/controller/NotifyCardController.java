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
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

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
import com.sunshine.framework.config.SystemConfig;
import com.sunshine.mobileapp.bankCard.entity.BankCard;
import com.sunshine.mobileapp.bankCard.service.BankCardService;
import com.sunshine.payment.CardConstant;
import com.sunshine.payment.TradeConstant;
import com.sunshine.payment.unionpay.AcpService;
import com.sunshine.payment.unionpay.SDKConstants;
import com.sunshine.payment.unionpay.UnionCardUtils;
import com.sunshine.payment.unionpay.UnionPayUtils;

/**
 * @Project: cashier_trade
 * @Package: com.sunshine.trade.controller
 * @ClassName: NotifyCardController
 * @Description: 绑卡异步回调处理类
 * @JDK version used: 
 * @Author: 熊胆
 * @Create Date: 2017年10月30日上午9:23:26
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
@Controller
@RequestMapping("/notifyCard")
public class NotifyCardController {

	private Logger logger = LoggerFactory.getLogger(NotifyCardController.class);

	@Autowired
	private BankCardService bankCardService;

	/**
	 * 银联绑卡异步回调处理类
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年10月30日上午9:24:15
	 */
	@RequestMapping("/union/callback/")
	public void unionCallback(HttpServletRequest request, HttpServletResponse response) {
		try {
			String encoding = request.getParameter(SDKConstants.param_encoding);
			// 获取银联通知服务器发送的后台通知参数
			Map<String, String> reqParam = getAllRequestParam(request);
			Map<String, String> valideData = null;
			if (null != reqParam && !reqParam.isEmpty()) {
				Iterator<Entry<String, String>> it = reqParam.entrySet().iterator();
				valideData = new HashMap<String, String>(reqParam.size());
				while (it.hasNext()) {
					Entry<String, String> e = it.next();
					String key = (String) e.getKey();
					String value = (String) e.getValue();
					value = new String(value.getBytes(encoding), encoding);
					valideData.put(key, value);
				}
			}
			logger.info("银联异步回调验签参数(valideData):{}", JSON.toJSONString(valideData));
			String result = "ok";
			//重要！验证签名前不要修改reqParam中的键值对的内容，否则会验签不过
			String dir = SystemConfig.getStringValue("acpsdk_validateCert_dir");
			logger.info("acpsdk_validateCert_dir:{}", dir);
			boolean validate = AcpService.validate(valideData, encoding, dir);
			logger.info("验签结果:{}", validate);
			if (validate) {
				logger.info("银联绑卡异步回调验签参数(valideData):{}", JSON.toJSONString(valideData));

				//获取后台通知的数据，其他字段也可用类似方式获取
				String respCode = valideData.get("respCode"); //获取应答码，收到后台通知了respCode的值一般是00，可以不需要根据这个应答码判断。
				String reqReserved = valideData.get("reqReserved");
				String respMsg = valideData.get("respMsg");
				String payCardType = valideData.get("payCardType");
				Integer activateStatus = Integer.parseInt(valideData.get("activateStatus"));
				String customerInfo = valideData.get("customerInfo");
				@SuppressWarnings("unchecked")
				Map<String, String> attachMap = (Map<String, String>) JSON.parse(URLDecoder.decode(reqReserved, TradeConstant.INPUT_CHARSET));
				logger.info("银联异步回调透传参数(attachMap):{}", JSON.toJSONString(attachMap));
				String cardId = attachMap.get("cardId");
				BankCard bankCard = bankCardService.findById(cardId);
				bankCard.setCardJson(JSON.toJSONString(valideData));

				StringBuffer logContent = new StringBuffer("银联绑卡回调通知");
				if (UnionPayUtils.RESPCODE_SUCCESS.equalsIgnoreCase(respCode)) {

					Map<String, String> map =
							AcpService.parseCustomerInfo(customerInfo, TradeConstant.INPUT_CHARSET, UnionCardUtils.BANK_CARD_SIGNCERTPATH,
									UnionCardUtils.BANK_CARD_SIGNCERTPWD, GlobalConstant.SIGNCERT_TYPE);
					String mobile = "";
					for (Map.Entry<String, String> entry : map.entrySet()) {
						if ("phoneNo".equals(entry.getKey())) {
							mobile = entry.getValue().trim();
						}
					}
					bankCard.setCardPhone(mobile);

					if (activateStatus.equals(CardConstant.ACTIVATE_STATUS_BUNDLING)) {
						bankCard.setActivateStatus(CardConstant.ACTIVATE_STATUS_BUNDLING);

						//设置银行卡类型
						if (payCardType == null) {
							Map<String, Object> bindCardParam = UnionCardUtils.unionBindCardQueryService(bankCard);
							payCardType = (String) bindCardParam.get("payCardType");
						}
						bankCard.setBankCardType(payCardType);

					}
					bankCard.setIsSuccess(TradeConstant.ORDER_SUCCESS_TRUE);
					logContent.append("绑卡成功,银行卡号为:").append(bankCard.getBankCardNo());
				} else {
					logContent.append("绑卡成功，失败原因").append("return_msg:").append(respMsg);
				}
				bankCardService.updateCardForDbAndCache(bankCard);

			}
			//返回给银联服务器http 200  状态码
			PrintWriter out = response.getWriter();
			response.setCharacterEncoding(TradeConstant.INPUT_CHARSET);
			response.setContentType("text/xml; charset=" + TradeConstant.INPUT_CHARSET);
			logger.info("银联绑卡异步回调通知银联结果:{}", result);
			out.print(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/union/returnSync/")
	public ModelAndView returnSync(HttpServletRequest request) {
		ModelAndView view = new ModelAndView("trade/bankCardTest");
		return view;
	}

	/**
	 * 获取请求参数中所有的信息
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年7月10日上午11:06:59
	 */
	public static Map<String, String> getAllRequestParam(final HttpServletRequest request) {
		Map<String, String> res = new HashMap<String, String>();
		Enumeration<?> temp = request.getParameterNames();
		if (null != temp) {
			while (temp.hasMoreElements()) {
				String en = (String) temp.nextElement();
				String value = request.getParameter(en);
				res.put(en, value);
				// 在报文上送时，如果字段的值为空，则不上送<下面的处理为在获取所有参数数据时，判断若值为空，则删除这个字段>
				// System.out.println("ServletUtil类247行  temp数据的键=="+en+"     值==="+value);
				if (null == res.get(en) || "".equals(res.get(en))) {
					res.remove(en);
				}
			}
		}
		return res;
	}

}

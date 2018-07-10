/**
 * <html>
 * <body>
 *  <P> Copyright 2017 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年10月27日</p>
 *  <p> Created by 熊胆</p>
 *  </body>
 * </html>
 */
package com.sunshine.trade.controller;

import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.UnknownHostException;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.sunshine.common.GlobalConstant;
import com.sunshine.mobileapp.bankCard.entity.BankCard;
import com.sunshine.payment.CardConstant;
import com.sunshine.payment.unionpay.UnionCardUtils;
import com.sunshine.platform.merchant.entity.Merchant;
import com.sunshine.platform.merchant.service.MerchantService;
import com.sunshine.trade.dto.RequestBindCardVo;
import com.sunshine.trade.service.BindCardService;

/**
 * @Project: cashier_trade
 * @Package: com.sunshine.trade.controller
 * @ClassName: BindCardController
 * @Description: 绑卡控制器
 * @JDK version used: 
 * @Author: 熊胆
 * @Create Date: 2017年10月27日上午11:54:51
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
@Controller
@RequestMapping("/bindCard")
public class BindCardController {
	private Logger logger = LoggerFactory.getLogger(BindCardController.class);

	@Autowired
	private BindCardService bindCardService;

	@Autowired
	protected MerchantService merchantService;

	@RequestMapping("/toCard")
	public ModelAndView toCard(HttpServletRequest request) {
		ModelAndView view = new ModelAndView("/platform/bankCard/bankCardAdd");
		List<Merchant> allMerchant = merchantService.getAllMerchant();
		view.addObject("merchant", allMerchant);
		return view;
	}

	/**
	 * 绑卡申请
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年10月27日下午2:11:45
	 */
	@RequestMapping("/applyBindCard")
	public ModelAndView applyBindCard(RequestBindCardVo vo, HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		ModelAndView view = new ModelAndView();
		Map<String, Object> retMap = new HashMap<String, Object>();
		try {
			BankCard card = vo.convertUnionCard();
			Double balance = Double.parseDouble(vo.getCardBalance()) * 100;
			card.setCardBalance(balance.intValue());
			Double singleQuota = Double.parseDouble(vo.getSingleQuota()) * 100;
			card.setSingleQuota(singleQuota.intValue());
			Double dayQuota = Double.parseDouble(vo.getDayQuota()) * 100;
			card.setDayQuota(dayQuota.intValue());
			Double monthlyQuota = Double.parseDouble(vo.getMonthlyQuota()) * 100;
			card.setMonthlyQuota(monthlyQuota.intValue());
			card.setUserName(URLDecoder.decode(vo.getUserName(), "UTF-8"));
			card.setBankCardName(URLDecoder.decode(vo.getBankCardName(), "UTF-8"));
			card.setSourceIp(getIpAddr(request));
			retMap = bindCardService.polymerizationBindCard(card, vo.getChannelCode());
			Boolean isException = (Boolean) retMap.get(GlobalConstant.TRADE_IS_EXCEPTION);
			Boolean isSuccess = (Boolean) retMap.get(GlobalConstant.TRADE_IS_SUCCESS);
			if (!isException && isSuccess) {
				@SuppressWarnings("unchecked")
				Map<String, String> requestData = (Map<String, String>) retMap.get("requestData");
				if (requestData != null) {
					view = new ModelAndView("/platform/bankCard/transferBindCard");
					modelMap.put(CardConstant.TRADE_PARAMS_MAP_BIND_CARD, requestData);
					modelMap.put(CardConstant.UNION_BINDCARD_REQUEST_URL, UnionCardUtils.UNION_QUERY_FRONTTRANSURL);
				} else {
					String returnUrl = (String) retMap.get("returnUrl");
					StringBuffer url = new StringBuffer("redirect:");
					url.append(returnUrl);
					logger.info("同步跳转到银行绑卡的地址:{}", url.toString());
					view = new ModelAndView(url.toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;
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
}

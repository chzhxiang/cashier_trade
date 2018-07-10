/**
 * <html>
 * <body>
 *  <P> Copyright 2017 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年10月27日</p>
 *  <p> Created by 党参</p>
 *  </body>
 * </html>
 */
package com.sunshine.mobileapp.recharge.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sunshine.common.GlobalConstant;
import com.sunshine.mobileapp.recharge.entity.MerchantRecharge;
import com.sunshine.mobileapp.recharge.service.MerchantRechargeService;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.mobileapp.recharge.controller
 * @ClassName: RechargeController
 * @Description: <p></p>
 * @JDK version used: 
 * @Author: 党参
 * @Create Date: 2017年10月27日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
@Controller
@RequestMapping("/recharge")
public class RechargeController {

	private Logger logger = LoggerFactory.getLogger(RechargeController.class);

	@Autowired
	private MerchantRechargeService merchantRechargeService;

	@RequestMapping(value = "/list")
	public ModelAndView list(HttpServletRequest request, ModelMap modelMap) {
		logger.info("跳转至充值首页");
		return new ModelAndView("mobileapp/recharge/list");
	}

	@RequestMapping(value = "/rechargeEdit")
	public ModelAndView rechargeEdit(HttpServletRequest request, ModelMap modelMap) {
		logger.info("跳转至充值申请页");
		return new ModelAndView("mobileapp/recharge/rechargeEdit");
	}

	@ResponseBody
	@RequestMapping(value = "/applyRecharge")
	public Object applyRecharge(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		try {
			MerchantRecharge recharge = new MerchantRecharge();
			recharge.setSubMerchantName("12321321");
			merchantRechargeService.insert(recharge);
			retMap.put(GlobalConstant.MOTHED_INVOKE_RES_IS_SUCCESS, true);
		} catch (Exception e) {
			retMap.put(GlobalConstant.MOTHED_INVOKE_RES_IS_SUCCESS, false);
			e.printStackTrace();
		}
		return retMap;
	}

}

/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年8月22日</p>
 *  <p> Created by 赤芍</p>
 *  </body>
 * </html>
 */
package com.sunshine.platform.merchant.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.sunshine.common.GlobalConstant;
import com.sunshine.common.controller.BasePlatformController;
import com.sunshine.common.datas.cache.platform.merchant.MerchantCache;
import com.sunshine.framework.common.spring.ext.SpringContextHolder;
import com.sunshine.framework.mvc.controller.RespBody;
import com.sunshine.framework.mvc.controller.RespBody.StatusEnum;
import com.sunshine.framework.mvc.service.BaseService;
import com.sunshine.platform.merchant.entity.Merchant;
import com.sunshine.platform.merchant.service.MerchantService;
import com.sunshine.platform.security.entity.User;

/**
 * 
 * @Project: cashier_trade 
 * @Package: com.sunshine.platform.merchant.controller
 * @ClassName: MerchantController
 * @Description: <p>商户管理</p>
 * @JDK version used: 
 * @Author: 赤芍
 * @Create Date: 2017年9月12日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
@Controller
@RequestMapping("/merchant")
public class MerchantController extends BasePlatformController<Merchant, String> {

	protected Logger logger = LoggerFactory.getLogger(MerchantController.class);

	protected final static String MECNO_PRE_FIX = "968309";

	@Autowired
	protected MerchantService merchantService;

	/* (non-Javadoc)
	 * @see com.sunshine.framework.mvc.controller.BaseController#getService()
	 */
	@Override
	protected BaseService<Merchant, String> getService() {
		return merchantService;
	}

	/**
	 * 商户列表
	 * @param merchant
	 * @param request
	 * @return
	 */
	@RequestMapping("/list")
	public ModelAndView list() {
		ModelAndView modelAndView = new ModelAndView("/platform/merchant/merchantList");
		return modelAndView;
	}

	/**
	 * 跳转添加商户
	 * @param request
	 * @return
	 */
	@RequestMapping("/toAdd")
	public ModelAndView toAdd() {
		return new ModelAndView("/platform/merchant/merchantAdd");
	}

	/**
	 * 跳转修改商户
	 * @param merchantNo
	 * @return
	 */
	@RequestMapping("/toEdit")
	public ModelAndView toEdit(String merchantNo) {
		ModelAndView modelAndView = new ModelAndView("/platform/merchant/merchantEdit");

		//		MerchantCache merchantCache = SpringContextHolder.getBean(MerchantCache.class);
		//		Merchant merchant = merchantCache.getMerchantByMerchantNo(merchantNo);
		Merchant merchant = merchantService.findByMerchantNo(merchantNo);
		modelAndView.addObject("entity", merchant);

		return modelAndView;
	}

	/**
	 * 跳转商户应用设置
	 * @param merchantNo
	 * @return
	 */
	@RequestMapping("/toSetting")
	public ModelAndView toSetting(String merchantNo) {
		ModelAndView modelAndView = new ModelAndView("/platform/merchant/merchantSetting");
		modelAndView.addObject("merchantNo", merchantNo);

		return modelAndView;
	}

	/**
	 * 保存
	 * @param request
	 * @return
	 */
	@Override
	@ResponseBody
	@RequestMapping("/save")
	public RespBody save(Merchant merchant, HttpServletRequest request) {
		User user = getPlatformUser(request);
		setInformation(user, merchant);
		logger.info("保存商户:{}", JSON.toJSONString(merchant));
		if (StringUtils.isNotBlank(merchant.getId())) {
			getService().update(merchant);
		} else {
			merchant.setStatus(1);
			//TODO 
			StringBuffer randomMechNo = randomMechNo();
			merchant.setMerchantNo(MECNO_PRE_FIX + randomMechNo);
			merchant.setId(getService().insert(merchant));
		}
		MerchantCache merchantCache = SpringContextHolder.getBean(MerchantCache.class);
		merchantCache.updateMerchant(merchant);

		return new RespBody(StatusEnum.OK, "保存成功!");
	}

	/**
	 * 修改状态,0停用、1启用
	 * @param merchantNo
	 * @param status
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/changeStatus")
	public RespBody changeStatus(String merchantNo, Integer status) {
		String resultMessage = null;
		MerchantCache merchantCache = SpringContextHolder.getBean(MerchantCache.class);
		Merchant merchant = merchantCache.getMerchantByMerchantNo(merchantNo);
		merchant.setStatus(status);

		merchantService.update(merchant);
		merchantCache.updateMerchant(merchant);

		if (status == GlobalConstant.STATUS_VALID) {
			resultMessage = "【" + merchant.getMerchantName() + "】已启用";
		} else {
			resultMessage = "【" + merchant.getMerchantName() + "】已停用";
		}

		return new RespBody(StatusEnum.OK, resultMessage);
	}

	public StringBuffer randomMechNo() {
		StringBuffer stringBuffer = new StringBuffer("0123456789");
		StringBuffer sb = new StringBuffer();
		Random r = new Random();
		for (int i = 0; i < 10; i++) {
			sb.append(stringBuffer.charAt(r.nextInt(10)));
		}
		return sb;
	}

	/**
	 * 加载所有商户
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年11月14日下午4:39:59
	 */
	@ResponseBody
	@RequestMapping(value = "/loadAllMerchat", method = RequestMethod.POST)
	public RespBody loadAllMerchat(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Merchant> allMerchant = merchantService.getAllMerchant();
		map.put(GlobalConstant.MOTHED_INVOKE_RES_ENTITIES, allMerchant);
		return new RespBody(StatusEnum.OK, map);
	}
}

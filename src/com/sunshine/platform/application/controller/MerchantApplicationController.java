/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年9月6日</p>
 *  <p> Created by 赤芍</p>
 *  </body>
 * </html>
 */
package com.sunshine.platform.application.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sunshine.common.controller.BasePlatformController;
import com.sunshine.common.datas.cache.platform.merchant.MerchantCache;
import com.sunshine.framework.common.PKGenerator;
import com.sunshine.framework.common.spring.ext.SpringContextHolder;
import com.sunshine.framework.mvc.controller.RespBody;
import com.sunshine.framework.mvc.controller.RespBody.StatusEnum;
import com.sunshine.framework.mvc.service.BaseService;
import com.sunshine.platform.application.entity.MerchantApplication;
import com.sunshine.platform.application.service.MerchantApplicationService;
import com.sunshine.platform.security.entity.User;
import com.sunshine.restful.sign.SignatureUtils;

/**
 * 
 * @Project: cashier_trade 
 * @Package: com.sunshine.platform.application.controller
 * @ClassName: MerchantApplicationController
 * @Description: <p>商户应用管理</p>
 * @JDK version used: 
 * @Author: 赤芍
 * @Create Date: 2017年9月12日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
@Controller
@RequestMapping("/merchantApplication")
public class MerchantApplicationController extends BasePlatformController<MerchantApplication, String> {

	private Logger logger = LoggerFactory.getLogger(MerchantApplicationController.class);
	//APPid 前缀
	private final static String APPID_PER_FIX = "sun";
	//时间校验 默认 1 开启
	private final static int VALIDATE_TIME = 1;

	@Autowired
	protected MerchantApplicationService merchantApplicationService;

	/* (non-Javadoc)
	 * @see com.sunshine.framework.mvc.controller.BaseController#getService()
	 */
	@Override
	protected BaseService<MerchantApplication, String> getService() {
		return merchantApplicationService;
	}

	/**
	 * 获取所有应用列表
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getMerchantApplications")
	public RespBody getMerchantApplications(String merchantNo) {
		List<MerchantApplication> merchantApplications = merchantApplicationService.findByMerchantNo(merchantNo);

		return new RespBody(StatusEnum.OK, merchantApplications, "");
	}

	/**
	 * 保存
	 */
	@Override
	@ResponseBody
	@RequestMapping("/save")
	public RespBody save(MerchantApplication merchantApplication, HttpServletRequest request) {
		MerchantCache merchantCache = SpringContextHolder.getBean(MerchantCache.class);
		User user = getPlatformUser(request);
		setInformation(user, merchantApplication);
		logger.info("保存应用:{}", JSON.toJSONString(merchantApplication));
		if (StringUtils.isNotBlank(merchantApplication.getId())) {
			merchantApplicationService.update(merchantApplication);
			//更新缓存
			MerchantApplication newMerchantApplication = merchantApplicationService.findById(merchantApplication.getId());
			logger.info("更新缓存商户应用信息，fieldName：{}，value:{}", merchantApplication.getAppId(), JSON.toJSONString(merchantApplication));

			merchantCache.cacheMerchantApplication(newMerchantApplication);
		} else {
			String ranndId = PKGenerator.generateId();
			merchantApplication.setAppId(APPID_PER_FIX + ranndId.substring(0, 15));
			merchantApplication.setValidateTime(VALIDATE_TIME);
			merchantApplication.setAppSecret(PKGenerator.generateId());
			merchantApplication.setMasterSecret(PKGenerator.generateId());
			merchantApplication.setPublicKey(merchantApplication.getPublicKey());
			try {
				Map<String, Object> map = SignatureUtils.genKeyPair();
				merchantApplication.setAppPrivateKey(SignatureUtils.getPrivateKey(map));
				merchantApplication.setAppPublicKey(SignatureUtils.getPublicKey(map));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			merchantApplication.setId(merchantApplicationService.insert(merchantApplication));
			logger.info("缓存商户应用信息，fieldName：{}，value:{}", merchantApplication.getAppId(), JSON.toJSONString(merchantApplication));
			merchantCache.cacheMerchantApplication(merchantApplication);
		}
		return new RespBody(StatusEnum.OK, merchantApplication, "保存成功!");
	}

	@ResponseBody
	@RequestMapping("/findByAppId")
	public RespBody findByAppId(String appId, HttpServletRequest request) {
		MerchantApplication merchantApplication = merchantApplicationService.findByAppId(appId);
		return new RespBody(StatusEnum.OK, merchantApplication, "");
	}
}

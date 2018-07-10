/**
 * <html>
 * <body>
 *  <P> Copyright 2017 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年9月14日</p>
 *  <p> Created by 党参</p>
 *  </body>
 * </html>
 */
package com.sunshine.trade.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.sunshine.common.GlobalConstant;
import com.sunshine.payment.TradeConstant;
import com.sunshine.platform.application.entity.MerchantApplication;
import com.sunshine.platform.application.service.MerchantApplicationService;
import com.sunshine.platform.applicationchannel.service.ApplicationChannelService;
import com.sunshine.platform.merchant.entity.Merchant;
import com.sunshine.platform.merchant.service.MerchantService;
import com.sunshine.restful.RestConstant;
import com.sunshine.restful.sign.SignatureUtils;
import com.sunshine.restful.utils.RestUtils;
import com.sunshine.trade.service.RestAuthorizationService;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.restful.trade.service.impl
 * @ClassName: RestAuthorizationServiceImpl
 * @Description: <p>rest请求签名处理的请求集中函数</p>
 * @JDK version used: 
 * @Author: 党参
 * @Create Date: 2017年9月14日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
@Service
public class RestAuthorizationServiceImpl implements RestAuthorizationService {

	private static Logger logger = LoggerFactory.getLogger(RestAuthorizationServiceImpl.class);
	@Autowired
	private MerchantApplicationService applicationService;

	@Autowired
	private MerchantService merchantService;

	@Autowired
	private ApplicationChannelService channelService;

	@Override
	public Map<String, String> authorization(Map<String, String> params) {
		Map<String, String> retMap = RestUtils.validateParams(params);
		if (retMap.isEmpty()) {
			String merchantNo = params.get(RestConstant.PARAMS_MERCHANTNO);
			String appId = params.get(RestConstant.PARAMS_APPID);
			//1.验签a:检查商户号、appId以及商户上传公钥
			String merchantPublicKey = getMerchantPublicKey(merchantNo, appId);
			if (StringUtils.isEmpty(merchantPublicKey)) {
				retMap.put(RestUtils.RETURN_CODE_KEY, RestConstant.RETURN_INVALID_APPID_OR_MERCHANTNO[0]);
				retMap.put(RestUtils.RETURN_MSG_KEY, RestConstant.RETURN_INVALID_APPID_OR_MERCHANTNO[1]);
			} else {
				//1.验签2:检查商户号、appId以及商户上传公钥
				String content = SignatureUtils.buildSortJson(params);
				//logger.info("加签字符串:{}", content);
				boolean isSign = SignatureUtils.rsa256CheckContent(content, params.get(RestConstant.PARAM_SIGN), merchantPublicKey);
				/*if (!isSign) {
					retMap.put(RestUtils.RETURN_CODE_KEY, RestConstant.RETURN_SIGN_FAIL[0]);
					retMap.put(RestUtils.RETURN_MSG_KEY, RestConstant.RETURN_SIGN_FAIL[1]);
				} else {
					//2.检查商户号是否启用
					Boolean status = isMerchantStatus(merchantNo);
					if (!status) {
						retMap.put(RestUtils.RETURN_CODE_KEY, RestConstant.RETURN_INVALID_MERCHANTNO[0]);
						retMap.put(RestUtils.RETURN_MSG_KEY, RestConstant.RETURN_INVALID_MERCHANTNO[1]);
					}
				}*/
			}
		}
		return retMap;
	}

	@Override
	public Map<String, String> responseAuthorization(Map<String, String> retMap) {
		logger.info("加签Map:{}", JSON.toJSONString(retMap));
		try {
			Map<String, String> dataMap = (Map<String, String>) JSON.parse(JSON.toJSONString(retMap));
			String merchantNo = dataMap.get(RestConstant.PARAMS_MERCHANTNO);
			String appId = dataMap.get(RestConstant.PARAMS_APPID);
			if (!StringUtils.isEmpty(merchantNo) && !StringUtils.isEmpty(appId)) {
				String applicationPrivateKey = getMerchantApplicationPrivateKey(merchantNo, appId);
				if (StringUtils.isEmpty(applicationPrivateKey)) {
					retMap.put(RestUtils.RETURN_CODE_KEY, RestConstant.RETURN_SYSTEMERROR[0]);
					retMap.put(RestUtils.RETURN_MSG_KEY, RestConstant.RETURN_SYSTEMERROR[1]);
				} else {
					retMap.put(RestConstant.PARAM_SIGN_MODE, TradeConstant.SIGN_TYPE_RSA);
					String content = SignatureUtils.buildSortJson(retMap);
					logger.info("加签字符串:{}", content);
					String sign = SignatureUtils.rsa256Sign(content, applicationPrivateKey);
					retMap.put(RestConstant.PARAM_SIGN, sign);
					retMap.putAll(dataMap);
				}
			} else {
				retMap.put(RestUtils.RETURN_CODE_KEY, RestConstant.RETURN_SYSTEMERROR[0]);
				retMap.put(RestUtils.RETURN_MSG_KEY, RestConstant.RETURN_SYSTEMERROR[1]);
			}
		} catch (Exception e) {
			retMap.clear();
			retMap.put(RestUtils.RETURN_CODE_KEY, RestConstant.RETURN_SYSTEMERROR[0]);
			retMap.put(RestUtils.RETURN_MSG_KEY, RestConstant.RETURN_SYSTEMERROR[1]);
			e.printStackTrace();
		}
		return retMap;
	}

	@Override
	public String getMerchantPublicKey(String merchantNo, String appId) {
		MerchantApplication merchantApplication = applicationService.findApplicationByAppId(merchantNo, appId);
		if (merchantApplication != null && !StringUtils.isEmpty(merchantApplication.getPublicKey())) {
			return merchantApplication.getPublicKey();
		} else {
			return null;
		}
	}

	@Override
	public boolean isMerchantStatus(String merchantNo) {
		Merchant merchant = merchantService.findByMerchantNo(merchantNo);
		if (merchant != null && GlobalConstant.YES == merchant.getStatus()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String getMerchantApplicationPrivateKey(String merchantNo, String appId) {
		MerchantApplication merchantApplication = applicationService.findApplicationByAppId(merchantNo, appId);
		if (merchantApplication != null && !StringUtils.isEmpty(merchantApplication.getPublicKey())) {
			return merchantApplication.getAppPrivateKey();
		} else {
			return null;
		}
	}

}

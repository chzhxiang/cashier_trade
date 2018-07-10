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
package com.sunshine.trade.service;

import java.util.Map;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.restful.trade.service
 * @ClassName: RestAuthorizationService
 * @Description: <p></p>
 * @JDK version used: 
 * @Author: 党参
 * @Create Date: 2017年9月14日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public interface RestAuthorizationService {

	/**
	 * 鉴权
	 * @param params
	 * @return
	 */
	Map<String, String> authorization(Map<String, String> params);

	/**
	 * 返回参数加签
	 * @param params
	 * @return
	 */
	Map<String, String> responseAuthorization(Map<String, String> params);

	/**
	 * 根据商户号和appId获取商户公钥
	 * @param merchantNo
	 * @param appId
	 * @return
	 */
	String getMerchantPublicKey(String merchantNo, String appId);

	/**
	 * 根据商户号和appId获取平台商户公钥
	 * @param merchantNo
	 * @param appId
	 * @return
	 */
	String getMerchantApplicationPrivateKey(String merchantNo, String appId);

	/**
	 * 检查商户是否启用
	 * @param merchantNo
	 * @return
	 */
	boolean isMerchantStatus(String merchantNo);

}

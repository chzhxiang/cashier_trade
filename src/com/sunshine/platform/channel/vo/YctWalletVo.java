/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年9月15日</p>
 *  <p> Created by hqy</p>
 *  </body>
 * </html>
 */
package com.sunshine.platform.channel.vo;

/**
 * @Project: cashier_trade
 * @Package: com.sunshine.platform.channel.vo
 * @ClassName: YctWalletVo
 * @Description:
 *               <p>
 *               （阳光康众钱包支付）医程通钱包相关支付渠道参数映射实体类
 *               </p>
 * @JDK version used:
 * @Author: 百部
 * @Create Date: 2017年9月15日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class YctWalletVo {
	/**
	 * 公众号ID
	 */
	private String appId;

	/**
	 * 商户号
	 */
	private String merchantNo;

	/**
	 * 支付密钥
	 */
	private String paySecret;

	/**
	 * 临时保存accessToken
	 */
	protected String accessToken;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getPaySecret() {
		return paySecret;
	}

	public void setPaySecret(String paySecret) {
		this.paySecret = paySecret;
	}

	/**
	 * 获取临时保存accessToken
	 * 
	 * @return 临时保存accessToken
	 */
	public String getAccessToken() {
		return accessToken;
	}

	/**
	 * 设置临时保存accessToken
	 * 
	 * @param 临时保存accessToken
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

}

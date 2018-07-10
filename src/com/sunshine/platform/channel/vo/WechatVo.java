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
 * @ClassName: WechatVo
 * @Description:
 *               <p>
 *               微信相关支付渠道参数映射实体类
 *               </p>
 * @JDK version used:
 * @Author: 百部
 * @Create Date: 2017年9月15日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class WechatVo {

	/**
	 * 父商户appId
	 */
	private String parentAppId;

	/**
	 * 公众号ID
	 */
	private String appId;

	/**
	 * 商户号
	 */
	private String mchId;

	/**
	 * 支付密钥
	 */
	private String paySecret;

	/**
	 * 商户secret
	 */
	private String secret;

	/**
	 * 证书位置
	 */
	private String certificatePath;

	/**
	 * 子商户mchId
	 */
	private String subMchId;

	/**
	 * 子商户appid
	 */
	private String subAppId;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public String getPaySecret() {
		return paySecret;
	}

	public void setPaySecret(String paySecret) {
		this.paySecret = paySecret;
	}

	public String getCertificatePath() {
		return certificatePath;
	}

	public void setCertificatePath(String certificatePath) {
		this.certificatePath = certificatePath;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	/**
	 * 获取父商户appId
	 * 
	 * @return parentAppId
	 */
	public String getParentAppId() {
		return parentAppId;
	}

	/**
	 * 设置父商户appId
	 * 
	 * @param parentAppId
	 */
	public void setParentAppId(String parentAppId) {
		this.parentAppId = parentAppId;
	}

	/**
	 * 获取子商户mchId
	 * 
	 * @return subMchId
	 */
	public String getSubMchId() {
		return subMchId;
	}

	/**
	 * 设置子商户mchId
	 * 
	 * @param subMchId
	 */
	public void setSubMchId(String subMchId) {
		this.subMchId = subMchId;
	}

	public String getSubAppId() {
		return subAppId;
	}

	public void setSubAppId(String subAppId) {
		this.subAppId = subAppId;
	}

}

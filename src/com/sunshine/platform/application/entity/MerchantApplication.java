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
package com.sunshine.platform.application.entity;

import com.sunshine.framework.mvc.mysql.entity.BaseSQLEntity;

/**
 * 
 * @Project: cashier_trade 
 * @Package: com.sunshine.platform.application.entity
 * @ClassName: MerchantApplication
 * @Description: <p>商户应用</p>
 * @JDK version used: 
 * @Author: 赤芍
 * @Create Date: 2017年9月12日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class MerchantApplication extends BaseSQLEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2550794788235461417L;

	/**
	 * 商户号
	 */
	private String merchantNo;

	/**
	 * 应用标识
	 */
	private String appIdentify;

	/**
	 * 应用名称
	 */
	private String appName;

	/**
	 * 项目备注
	 */
	private String comment;

	/**
	 * 是否校验时间，0否、1是
	 */
	private Integer validateTime;

	/**
	 * APP ID
	 */
	private String appId;

	/**
	 * APP Secret
	 */
	private String appSecret;

	/**
	 * Master Secret
	 */
	private String masterSecret;

	/**
	 * 公钥
	 */
	private String publicKey;

	/**
	 * 阳光康众公钥
	 */
	private String appPublicKey;

	/**
	 * 阳光康众私钥
	 */
	private String appPrivateKey;

	/**
	 * 状态，0停用、1启用
	 */
	private Integer status;

	/**
	 * @return the merchantNo
	 */
	public String getMerchantNo() {
		return merchantNo;
	}

	/**
	 * @param merchantNo the merchantNo to set
	 */
	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	/**
	 * @return the appIdentify
	 */
	public String getAppIdentify() {
		return appIdentify;
	}

	/**
	 * @param appIdentify the appIdentify to set
	 */
	public void setAppIdentify(String appIdentify) {
		this.appIdentify = appIdentify;
	}

	/**
	 * @return the appName
	 */
	public String getAppName() {
		return appName;
	}

	/**
	 * @param appName the appName to set
	 */
	public void setAppName(String appName) {
		this.appName = appName;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the validateTime
	 */
	public Integer getValidateTime() {
		return validateTime;
	}

	/**
	 * @param validateTime the validateTime to set
	 */
	public void setValidateTime(Integer validateTime) {
		this.validateTime = validateTime;
	}

	/**
	 * @return the appId
	 */
	public String getAppId() {
		return appId;
	}

	/**
	 * @param appId the appId to set
	 */
	public void setAppId(String appId) {
		this.appId = appId;
	}

	/**
	 * @return the appSecret
	 */
	public String getAppSecret() {
		return appSecret;
	}

	/**
	 * @param appSecret the appSecret to set
	 */
	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	/**
	 * @return the masterSecret
	 */
	public String getMasterSecret() {
		return masterSecret;
	}

	/**
	 * @param masterSecret the masterSecret to set
	 */
	public void setMasterSecret(String masterSecret) {
		this.masterSecret = masterSecret;
	}

	/**
	 * @return the publicKey
	 */
	public String getPublicKey() {
		return publicKey;
	}

	/**
	 * @param publicKey the publicKey to set
	 */
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getAppPublicKey() {
		return appPublicKey;
	}

	public void setAppPublicKey(String appPublicKey) {
		this.appPublicKey = appPublicKey;
	}

	public String getAppPrivateKey() {
		return appPrivateKey;
	}

	public void setAppPrivateKey(String appPrivateKey) {
		this.appPrivateKey = appPrivateKey;
	}

}

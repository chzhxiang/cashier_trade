/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年9月21日</p>
 *  <p> Created by hqy</p>
 *  </body>
 * </html>
 */
package com.sunshine.platform.channel.vo;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.platform.channel.vo
 * @ClassName: UnionPayVo
 * @Description: <p>银联相关支付渠道参数映射实体类</p>
 * @JDK version used: 
 * @Author: 百部
 * @Create Date: 2017年9月21日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class UnionPayVo {
	/**
	 * 商户号
	 */
	private String mchId;

	/**
	 * 商户名称
	
	 */
	private String mchAccount;

	/**
	 * 证书位置
	 */
	private String certificatePath;

	/**
	 * 商户密钥
	 */
	private String payPrivateKey;

	/**
	 * 商户密码
	 */
	private String payKey;

	/**
	 * 付款账号
	 */
	private String accNo;

	/**
	 * 付款方名称
	 */
	private String accName;

	/**
	 * 卡属性  
	 */
	private String cardAttr;

	/**
	 * 设备标识
	 */
	private String deviceId;

	/**
	 * 银行预留手机号
	 */
	private String mobile;

	/**
	 * 应用提供方账户ID
	 */
	private String accountId;

	/**
	 * IP地址
	 */
	private String sourceIp;

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public String getMchAccount() {
		return mchAccount;
	}

	public void setMchAccount(String mchAccount) {
		this.mchAccount = mchAccount;
	}

	public String getCertificatePath() {
		return certificatePath;
	}

	public void setCertificatePath(String certificatePath) {
		this.certificatePath = certificatePath;
	}

	public String getPayPrivateKey() {
		return payPrivateKey;
	}

	public void setPayPrivateKey(String payPrivateKey) {
		this.payPrivateKey = payPrivateKey;
	}

	public String getPayKey() {
		return payKey;
	}

	public void setPayKey(String payKey) {
		this.payKey = payKey;
	}

	/** 
	* 获取付款账号 
	* @return accNo
	*/
	public String getAccNo() {
		return accNo;
	}

	/** 
	* 设置付款账号 
	* @param accNo
	*/
	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}

	/** 
	* 获取付款方名称 
	* @return accName
	*/
	public String getAccName() {
		return accName;
	}

	/** 
	* 设置付款方名称 
	* @param accName
	*/
	public void setAccName(String accName) {
		this.accName = accName;
	}

	/** 
	* 获取卡属性 
	* @return cardAttr
	*/
	public String getCardAttr() {
		return cardAttr;
	}

	/** 
	* 设置卡属性 
	* @param cardAttr
	*/
	public void setCardAttr(String cardAttr) {
		this.cardAttr = cardAttr;
	}

	/** 
	* 获取设备标识 
	* @return deviceId
	*/
	public String getDeviceId() {
		return deviceId;
	}

	/** 
	* 设置设备标识 
	* @param deviceId
	*/
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	/** 
	* 获取银行预留手机号 
	* @return mobile
	*/
	public String getMobile() {
		return mobile;
	}

	/** 
	* 设置银行预留手机号 
	* @param mobile
	*/
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/** 
	* 获取应用提供方账户ID 
	* @return accountId
	*/
	public String getAccountId() {
		return accountId;
	}

	/** 
	* 设置应用提供方账户ID 
	* @param accountId
	*/
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	/** 
	* 获取IP地址 
	* @return sourceIp
	*/
	public String getSourceIp() {
		return sourceIp;
	}

	/** 
	* 设置IP地址 
	* @param sourceIp
	*/
	public void setSourceIp(String sourceIp) {
		this.sourceIp = sourceIp;
	}

}

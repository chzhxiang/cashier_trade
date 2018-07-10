/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年10月27日下午3:56:09</p>
 *  <p> Created by 熊胆</p>
 *  </body>
 * </html>
 */
package com.sunshine.trade.dto;

import org.springframework.beans.BeanUtils;

import com.sunshine.mobileapp.bankCard.entity.BankCard;

/**
 * @Project: cashier_trade
 * @Package: com.sunshine.trade.dto
 * @ClassName: RequestBindCard
 * @Description: 绑卡实体类
 * @JDK version used: 
 * @Author: 熊胆
 * @Create Date: 2017年10月27日下午3:56:09
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class RequestBindCardVo {

	/**
	 * 支付渠道编码
	 */
	private String channelCode;

	/**
	 * 应用平台Id
	 */
	private String appId;

	/**
	 * 商户号。即平台提供的医院编码
	 */
	private String merchantNo;

	/**
	 * 证件号码
	 */
	private String certificateNo;

	/**
	 * 绑卡姓名
	 */
	private String userName;

	/**
	 * 银行卡号
	 */
	private String bankCardNo;

	/**
	 * 证件类型：01二代身份证、02军官证、03护照、04回乡证、5台胞证、6警官证、7士兵证、8其它证
	 */
	private String certificateType;

	/**
	 * 设备标识
	 */
	private String deviceId;

	/**
	 * 应用提供方账户ID
	 */
	private String accountId;

	/**
	 * 银行卡余额
	 */
	private String cardBalance;

	/**
	 * 单笔限额
	 */
	private String singleQuota;

	/**
	 * 单日限额
	 */
	private String dayQuota;

	/**
	 * 单月限额
	 */
	private String monthlyQuota;

	/**
	 * 银行名称
	 */
	private String bankCardName;

	/** 
	* 获取支付渠道编码 
	* @return channelCode
	*/
	public String getChannelCode() {
		return channelCode;
	}

	/** 
	* 设置支付渠道编码 
	* @param channelCode
	*/
	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	/** 
	* 获取应用平台Id 
	* @return appId
	*/
	public String getAppId() {
		return appId;
	}

	/** 
	* 设置应用平台Id 
	* @param appId
	*/
	public void setAppId(String appId) {
		this.appId = appId;
	}

	/** 
	* 获取商户号。即平台提供的医院编码 
	* @return merchantNo
	*/
	public String getMerchantNo() {
		return merchantNo;
	}

	/** 
	* 设置商户号。即平台提供的医院编码 
	* @param merchantNo
	*/
	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	/** 
	* 获取证件号码 
	* @return certificateNo
	*/
	public String getCertificateNo() {
		return certificateNo;
	}

	/** 
	* 设置证件号码 
	* @param certificateNo
	*/
	public void setCertificateNo(String certificateNo) {
		this.certificateNo = certificateNo;
	}

	/** 
	* 获取绑卡姓名 
	* @return userName
	*/
	public String getUserName() {
		return userName;
	}

	/** 
	* 设置绑卡姓名 
	* @param userName
	*/
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/** 
	* 获取银行卡号 
	* @return bankCardNo
	*/
	public String getBankCardNo() {
		return bankCardNo;
	}

	/** 
	* 设置银行卡号 
	* @param bankCardNo
	*/
	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}

	/** 
	* 获取证件类型：01二代身份证、02军官证、03护照、04回乡证、5台胞证、6警官证、7士兵证、8其它证 
	* @return certificateType
	*/
	public String getCertificateType() {
		return certificateType;
	}

	/** 
	* 设置证件类型：01二代身份证、02军官证、03护照、04回乡证、5台胞证、6警官证、7士兵证、8其它证 
	* @param certificateType
	*/
	public void setCertificateType(String certificateType) {
		this.certificateType = certificateType;
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
	* 转化为银联卡信息
	* @return
	*/
	public BankCard convertUnionCard() {
		BankCard card = new BankCard();
		BeanUtils.copyProperties(this, card);
		return card;
	}

	/** 
	* 获取银行卡余额 
	* @return cardBalance
	*/
	public String getCardBalance() {
		return cardBalance;
	}

	/** 
	* 设置银行卡余额 
	* @param cardBalance
	*/
	public void setCardBalance(String cardBalance) {
		this.cardBalance = cardBalance;
	}

	/** 
	* 获取单笔限额 
	* @return singleQuota
	*/
	public String getSingleQuota() {
		return singleQuota;
	}

	/** 
	* 设置单笔限额 
	* @param singleQuota
	*/
	public void setSingleQuota(String singleQuota) {
		this.singleQuota = singleQuota;
	}

	/** 
	* 获取单月限额 
	* @return monthlyQuota
	*/
	public String getMonthlyQuota() {
		return monthlyQuota;
	}

	/** 
	* 设置单月限额 
	* @param monthlyQuota
	*/
	public void setMonthlyQuota(String monthlyQuota) {
		this.monthlyQuota = monthlyQuota;
	}

	/** 
	* 获取银行名称 
	* @return bankCardName
	*/
	public String getBankCardName() {
		return bankCardName;
	}

	/** 
	* 设置银行名称 
	* @param bankCardName
	*/
	public void setBankCardName(String bankCardName) {
		this.bankCardName = bankCardName;
	}

	/** 
	* 获取单日限额 
	* @return dayQuota
	*/
	public String getDayQuota() {
		return dayQuota;
	}

	/** 
	* 设置单日限额 
	* @param dayQuota
	*/
	public void setDayQuota(String dayQuota) {
		this.dayQuota = dayQuota;
	}

}

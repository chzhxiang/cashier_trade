/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年10月31日</p>
 *  <p> Created by 熊胆</p>
 *  </body>
 * </html>
 */
package com.sunshine.platform.bankCard.entity;

import com.sunshine.mobileapp.bankCard.entity.BankCard;

/**
 * @Project: cashier_trade
 * @Package: com.sunshine.platform.bankCard.entity
 * @ClassName: BankCardParamsVo
 * @Description: 银行卡导出excel实体类
 * @JDK version used: 
 * @Author: 熊胆
 * @Create Date: 2017年10月31日上午10:05:59
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class CardParamsVo extends BankCard {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8831563027863707458L;
	/**
	 * 创建订单时间
	 */
	private String createTimeVo;

	/**
	 * 开通状态
	 */
	private String activateStatusVo;

	/**
	 * 商户名称
	 */
	private String merchantName;

	/**
	 * 应用名称
	 */
	private String appName;

	/**
	 * 银行卡余额
	 */
	private String cardBalanceByYuan;

	/**
	 * 单笔限额
	 */
	private String singleQuotaByYuan;

	/**
	 * 单天限额
	 */
	private String dayQuotaByYuan;

	/**
	 * 单月限额
	 */
	private String monthlyQuotaByYuan;

	/**
	 * 加*的身份证
	 */
	private String certificateNoStr;

	/**
	 * 加密的卡号
	 */
	private String bankCardNoStr;

	/**
	 * 当月消费金额
	 */
	private String payMoneyByCurMonth;

	/**
	 * 当月退费金额
	 */
	private String refundMoneyByCurMonth;

	/**
	 * 当天消费金额
	 */
	private String payMoneyByCurDay;

	/**
	 * 当天退费金额
	 */
	private String refundMoneyByCurDay;

	/**
	 * 记账日期
	 */
	private String recordDate;

	/** 
	* 获取创建订单时间 
	* @return createTimeVo
	*/
	public String getCreateTimeVo() {
		return createTimeVo;
	}

	/** 
	* 设置创建订单时间 
	* @param createTimeVo
	*/
	public void setCreateTimeVo(String createTimeVo) {
		this.createTimeVo = createTimeVo;
	}

	/** 
	* 获取开通状态 
	* @return activateStatusVo
	*/
	public String getActivateStatusVo() {
		return activateStatusVo;
	}

	/** 
	* 设置开通状态 
	* @param activateStatusVo
	*/
	public void setActivateStatusVo(String activateStatusVo) {
		this.activateStatusVo = activateStatusVo;
	}

	/** 
	* 获取商户名称 
	* @return merchantName
	*/
	public String getMerchantName() {
		return merchantName;
	}

	/** 
	* 设置商户名称 
	* @param merchantName
	*/
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	/** 
	* 获取应用名称 
	* @return appName
	*/
	public String getAppName() {
		return appName;
	}

	/** 
	* 设置应用名称 
	* @param appName
	*/
	public void setAppName(String appName) {
		this.appName = appName;
	}

	/** 
	* 获取银行卡余额 
	* @return cardBalanceByYuan
	*/
	public String getCardBalanceByYuan() {
		return cardBalanceByYuan;
	}

	/** 
	* 设置银行卡余额 
	* @param cardBalanceByYuan
	*/
	public void setCardBalanceByYuan(String cardBalanceByYuan) {
		this.cardBalanceByYuan = cardBalanceByYuan;
	}

	/** 
	* 获取单笔限额 
	* @return singleQuotaByYuan
	*/
	public String getSingleQuotaByYuan() {
		return singleQuotaByYuan;
	}

	/** 
	* 设置单笔限额 
	* @param singleQuotaByYuan
	*/
	public void setSingleQuotaByYuan(String singleQuotaByYuan) {
		this.singleQuotaByYuan = singleQuotaByYuan;
	}

	/** 
	* 获取单月限额 
	* @return monthlyQuotaByYuan
	*/
	public String getMonthlyQuotaByYuan() {
		return monthlyQuotaByYuan;
	}

	/** 
	* 设置单月限额 
	* @param monthlyQuotaByYuan
	*/
	public void setMonthlyQuotaByYuan(String monthlyQuotaByYuan) {
		this.monthlyQuotaByYuan = monthlyQuotaByYuan;
	}

	/** 
	* 获取加的身份证 
	* @return certificateNoStr
	*/
	public String getCertificateNoStr() {
		return certificateNoStr;
	}

	/** 
	* 设置加的身份证 
	* @param certificateNoStr
	*/
	public void setCertificateNoStr(String certificateNoStr) {
		this.certificateNoStr = certificateNoStr;
	}

	/** 
	* 获取加密的卡号 
	* @return bankCardNoStr
	*/
	public String getBankCardNoStr() {
		return bankCardNoStr;
	}

	/** 
	* 设置加密的卡号 
	* @param bankCardNoStr
	*/
	public void setBankCardNoStr(String bankCardNoStr) {
		this.bankCardNoStr = bankCardNoStr;
	}

	/** 
	* 获取当月消费金额 
	* @return payMoneyByCurMonth
	*/
	public String getPayMoneyByCurMonth() {
		return payMoneyByCurMonth;
	}

	/** 
	* 设置当月消费金额 
	* @param payMoneyByCurMonth
	*/
	public void setPayMoneyByCurMonth(String payMoneyByCurMonth) {
		this.payMoneyByCurMonth = payMoneyByCurMonth;
	}

	/** 
	* 获取当月退费金额 
	* @return refundMoneyByCurMonth
	*/
	public String getRefundMoneyByCurMonth() {
		return refundMoneyByCurMonth;
	}

	/** 
	* 设置当月退费金额 
	* @param refundMoneyByCurMonth
	*/
	public void setRefundMoneyByCurMonth(String refundMoneyByCurMonth) {
		this.refundMoneyByCurMonth = refundMoneyByCurMonth;
	}

	/** 
	* 获取当天消费金额 
	* @return payMoneyByCurDay
	*/
	public String getPayMoneyByCurDay() {
		return payMoneyByCurDay;
	}

	/** 
	* 设置当天消费金额 
	* @param payMoneyByCurDay
	*/
	public void setPayMoneyByCurDay(String payMoneyByCurDay) {
		this.payMoneyByCurDay = payMoneyByCurDay;
	}

	/** 
	* 获取当天退费金额 
	* @return refundMoneyByCurDay
	*/
	public String getRefundMoneyByCurDay() {
		return refundMoneyByCurDay;
	}

	/** 
	* 设置当天退费金额 
	* @param refundMoneyByCurDay
	*/
	public void setRefundMoneyByCurDay(String refundMoneyByCurDay) {
		this.refundMoneyByCurDay = refundMoneyByCurDay;
	}

	/** 
	* 获取记账日期 
	* @return recordDate
	*/
	public String getRecordDate() {
		return recordDate;
	}

	/** 
	* 设置记账日期 
	* @param recordDate
	*/
	public void setRecordDate(String recordDate) {
		this.recordDate = recordDate;
	}

	/** 
	* 获取单天限额 
	* @return dayQuotaByYuan
	*/
	public String getDayQuotaByYuan() {
		return dayQuotaByYuan;
	}

	/** 
	* 设置单天限额 
	* @param dayQuotaByYuan
	*/
	public void setDayQuotaByYuan(String dayQuotaByYuan) {
		this.dayQuotaByYuan = dayQuotaByYuan;
	}

}

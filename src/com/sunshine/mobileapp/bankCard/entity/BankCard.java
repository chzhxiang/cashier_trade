/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年10月26日上午11:01:45</p>
 *  <p> Created by 熊胆</p>
 *  </body>
 * </html>
 */
package com.sunshine.mobileapp.bankCard.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.annotation.JSONField;
import com.sunshine.framework.mvc.mongodb.entity.BaseMongoEntity;
import com.sunshine.payment.TradeConstant;
import com.sunshine.restful.utils.RestUtils;

/**
 * @Project: cashier_trade
 * @Package: com.sunshine.mobileapp.order.entity
 * @ClassName: BankCard
 * @Description: 绑卡信息
 * @JDK version used: 
 * @Author: 熊胆
 * @Create Date: 2017年10月26日上午11:01:45
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class BankCard extends BaseMongoEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7598883350507942459L;

	/**
	 * 应用平台Id
	 */
	private String appId;

	/**
	 * 商户号。即平台提供的医院编码
	 */
	private String merchantNo;

	/**
	 * 绑卡流水号
	 */
	private String cardSerialNo;

	/**
	 * 绑卡时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/**
	 * 修改时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;

	/**
	 * 用户标识
	 */
	private String userId;

	/**
	 * 证件类型：01二代身份证、02军官证、03护照、04回乡证、5台胞证、6警官证、7士兵证、8其它证
	 */
	private String certificateType;

	/**
	 * 证件号码
	 */
	private String certificateNo;

	/**
	 * 绑卡姓名
	 */
	private String userName;

	/**
	 * 银行卡名称
	 */
	private String bankCardName;

	/**
	 * 银行卡分类 01借记卡 02 贷记卡 03 准贷记卡 04社保卡
	 */
	private String bankCardType;

	/**
	 * 银行卡号
	 */
	private String bankCardNo;

	/**
	 * 银行卡号预留手机号码
	 */
	private String cardPhone;

	/**
	 * 是否默认支付卡 1默认 0非默认
	 */
	private String isDefault;

	/**
	 * 0普通 1 免密支付卡 2解绑
	 */
	private String isAuthorize;

	/**
	 * 信用卡后三位
	 */
	private String creditCardAfterThree;

	/**
	 * 信用卡到期时间
	 */
	private String creditDueTime;

	/**
	 * 医保卡号
	 */
	private String medicardNo;

	/**
	 * 开通状态 0 待开通 1 已开通 2 解绑 3 余额不足  4 支付受限
	 */
	private Integer activateStatus;

	/**
	 * 医保卡绑卡类型 1线上 2线下
	 */
	private String medicardType;

	/**
	 * 绑卡回调结果
	 */
	private String cardJson;

	/**
	 * 查询绑卡json
	 */
	private String cardQueryJson;

	/**
	 * 回调地址
	 */
	private String notifyUrl;

	/**
	 * 回调是否成功 0:否  1:是
	 */
	private Integer isNotify = TradeConstant.ORDER_NOTIFY_FALSE;

	/**
	 * 回调次数
	 */
	private Integer notifyCount = 0;

	/**
	 * 前端跳转地址
	 */
	private String returnUrl;

	/**
	 * 附加数据
	 */
	private String attach;

	/**
	 * 是否发生异常 0 否 1是
	 */
	private Integer isException = TradeConstant.ORDER_EXCEPTION_FALSE;

	/**
	 * 处理是否成功
	 */
	private Integer isSuccess = TradeConstant.ORDER_SUCCESS_FALSE;

	/**
	 * 设备标识
	 */
	private String deviceId;

	/**
	 * 应用提供方账户ID
	 */
	private String accountId;

	/**
	 * IP地址
	 */
	private String sourceIp;

	/**
	 * 银行卡余额
	 */
	private Integer cardBalance;

	/**
	 * 日志记录
	 */
	private List<BankCardLog> logs;

	/**
	 * 单笔限额
	 */
	private Integer singleQuota;

	/**
	 * 单天限额
	 */
	private Integer dayQuota;

	/**
	 * 单月限额
	 */
	private Integer monthlyQuota;

	/**
	 * 充值金额
	 */
	private String rechargeBalance;

	/**
	 * 记账日期
	 */
	private String recordDate;

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
	* 获取绑卡流水号 
	* @return cardSerialNo
	*/
	public String getCardSerialNo() {
		return cardSerialNo;
	}

	/** 
	* 设置绑卡流水号 
	* @param cardSerialNo
	*/
	public void setCardSerialNo(String cardSerialNo) {
		this.cardSerialNo = cardSerialNo;
	}

	/** 
	* 获取绑卡时间 
	* @return createTime
	*/
	public Date getCreateTime() {
		return createTime;
	}

	/** 
	* 设置绑卡时间 
	* @param createTime
	*/
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/** 
	* 获取用户标识 
	* @return userId
	*/
	public String getUserId() {
		return userId;
	}

	/** 
	* 设置用户标识 
	* @param userId
	*/
	public void setUserId(String userId) {
		this.userId = userId;
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
	* 获取银行卡名称 
	* @return bankCardName
	*/
	public String getBankCardName() {
		return bankCardName;
	}

	/** 
	* 设置银行卡名称 
	* @param bankCardName
	*/
	public void setBankCardName(String bankCardName) {
		this.bankCardName = bankCardName;
	}

	/** 
	* 获取银行卡分类1借记卡2贷记卡3准贷记卡4社保卡 
	* @return bankCardType
	*/
	public String getBankCardType() {
		return bankCardType;
	}

	/** 
	* 设置银行卡分类1借记卡2贷记卡3准贷记卡4社保卡 
	* @param bankCardType
	*/
	public void setBankCardType(String bankCardType) {
		this.bankCardType = bankCardType;
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
	* 获取银行卡号预留手机号码 
	* @return cardPhone
	*/
	public String getCardPhone() {
		return cardPhone;
	}

	/** 
	* 设置银行卡号预留手机号码 
	* @param cardPhone
	*/
	public void setCardPhone(String cardPhone) {
		this.cardPhone = cardPhone;
	}

	/** 
	* 获取是否默认支付卡1默认0非默认 
	* @return isDefault
	*/
	public String getIsDefault() {
		return isDefault;
	}

	/** 
	* 设置是否默认支付卡1默认0非默认 
	* @param isDefault
	*/
	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}

	/** 
	* 获取0普通1免密支付卡2解绑 
	* @return isAuthorize
	*/
	public String getIsAuthorize() {
		return isAuthorize;
	}

	/** 
	* 设置0普通1免密支付卡2解绑 
	* @param isAuthorize
	*/
	public void setIsAuthorize(String isAuthorize) {
		this.isAuthorize = isAuthorize;
	}

	/** 
	* 获取信用卡后三位 
	* @return creditCardAfterThree
	*/
	public String getCreditCardAfterThree() {
		return creditCardAfterThree;
	}

	/** 
	* 设置信用卡后三位 
	* @param creditCardAfterThree
	*/
	public void setCreditCardAfterThree(String creditCardAfterThree) {
		this.creditCardAfterThree = creditCardAfterThree;
	}

	/** 
	* 获取信用卡到期时间 
	* @return creditDueTime
	*/
	public String getCreditDueTime() {
		return creditDueTime;
	}

	/** 
	* 设置信用卡到期时间 
	* @param creditDueTime
	*/
	public void setCreditDueTime(String creditDueTime) {
		this.creditDueTime = creditDueTime;
	}

	/** 
	* 获取医保卡号 
	* @return medicardNo
	*/
	public String getMedicardNo() {
		return medicardNo;
	}

	/** 
	* 设置医保卡号 
	* @param medicardNo
	*/
	public void setMedicardNo(String medicardNo) {
		this.medicardNo = medicardNo;
	}

	/** 
	* 获取开通状态 
	* @return activateStatus
	*/
	public Integer getActivateStatus() {
		return activateStatus;
	}

	/** 
	* 设置开通状态 
	* @param activateStatus
	*/
	public void setActivateStatus(Integer activateStatus) {
		this.activateStatus = activateStatus;
	}

	/** 
	* 获取医保卡绑卡类型1线上2线下 
	* @return medicardType
	*/
	public String getMedicardType() {
		return medicardType;
	}

	/** 
	* 设置医保卡绑卡类型1线上2线下 
	* @param medicardType
	*/
	public void setMedicardType(String medicardType) {
		this.medicardType = medicardType;
	}

	/** 
	* 获取绑卡返回信息 
	* @return cardJson
	*/
	public String getCardJson() {
		return cardJson;
	}

	/** 
	* 设置绑卡返回信息 
	* @param cardJson
	*/
	public void setCardJson(String cardJson) {
		this.cardJson = cardJson;
	}

	/** 
	* 获取回调地址 
	* @return notifyUrl
	*/
	public String getNotifyUrl() {
		return notifyUrl;
	}

	/** 
	* 设置回调地址 
	* @param notifyUrl
	*/
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	/** 
	* 获取回调是否成功0:否1:是 
	* @return isNotify
	*/
	public Integer getIsNotify() {
		return isNotify;
	}

	/** 
	* 设置回调是否成功0:否1:是 
	* @param isNotify
	*/
	public void setIsNotify(Integer isNotify) {
		this.isNotify = isNotify;
	}

	/** 
	* 获取回调次数 
	* @return notifyCount
	*/
	public Integer getNotifyCount() {
		return notifyCount;
	}

	/** 
	* 设置回调次数 
	* @param notifyCount
	*/
	public void setNotifyCount(Integer notifyCount) {
		this.notifyCount = notifyCount;
	}

	/** 
	* 获取前端跳转地址 
	* @return returnUrl
	*/
	public String getReturnUrl() {
		return returnUrl;
	}

	/** 
	* 设置前端跳转地址 
	* @param returnUrl
	*/
	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	/** 
	* 获取是否发生异常0否1是 
	* @return isException
	*/
	public Integer getIsException() {
		return isException;
	}

	/** 
	* 设置是否发生异常0否1是 
	* @param isException
	*/
	public void setIsException(Integer isException) {
		this.isException = isException;
	}

	/** 
	* 获取附加数据 
	* @return attach
	*/
	public String getAttach() {
		return attach;
	}

	/** 
	* 设置附加数据 
	* @param attach
	*/
	public void setAttach(String attach) {
		this.attach = attach;
	}

	/** 
	* 获取处理是否成功 
	* @return isSuccess
	*/
	public Integer getIsSuccess() {
		return isSuccess;
	}

	/** 
	* 设置处理是否成功 
	* @param isSuccess
	*/
	public void setIsSuccess(Integer isSuccess) {
		this.isSuccess = isSuccess;
	}

	/** 
	* 获取查询绑卡json 
	* @return cardQueryJson
	*/
	public String getCardQueryJson() {
		return cardQueryJson;
	}

	/** 
	* 设置查询绑卡json 
	* @param cardQueryJson
	*/
	public void setCardQueryJson(String cardQueryJson) {
		this.cardQueryJson = cardQueryJson;
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

	/** 
	* 获取银行卡余额 
	* @return cardBalance
	*/
	public Integer getCardBalance() {
		return cardBalance;
	}

	/** 
	* 获取单笔限额 
	* @return singleQuota
	*/
	public Integer getSingleQuota() {
		return singleQuota;
	}

	/** 
	* 设置单笔限额 
	* @param singleQuota
	*/
	public void setSingleQuota(Integer singleQuota) {
		this.singleQuota = singleQuota;
	}

	/** 
	* 获取单月限额 
	* @return monthlyQuota
	*/
	public Integer getMonthlyQuota() {
		return monthlyQuota;
	}

	/** 
	* 设置单月限额 
	* @param monthlyQuota
	*/
	public void setMonthlyQuota(Integer monthlyQuota) {
		this.monthlyQuota = monthlyQuota;
	}

	/** 
	* 设置银行卡余额 
	* @param cardBalance
	*/
	public void setCardBalance(Integer cardBalance) {
		this.cardBalance = cardBalance;
	}

	public List<BankCardLog> getLogs() {
		if (CollectionUtils.isEmpty(this.logs)) {
			this.logs = new ArrayList<BankCardLog>();
		}
		return logs;
	}

	public void setLogs(List<BankCardLog> logs) {
		this.logs = logs;
	}

	/** 
	* 获取充值金额 
	* @return rechargeBalance
	*/
	public String getRechargeBalance() {
		return rechargeBalance;
	}

	/** 
	* 设置充值金额 
	* @param rechargeBalance
	*/
	public void setRechargeBalance(String rechargeBalance) {
		this.rechargeBalance = rechargeBalance;
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
	* @return dayQuota
	*/
	public Integer getDayQuota() {
		return dayQuota;
	}

	/** 
	* 设置单天限额 
	* @param dayQuota
	*/
	public void setDayQuota(Integer dayQuota) {
		this.dayQuota = dayQuota;
	}

	/** 
	* 获取修改时间 
	* @return updateTime
	*/
	public Date getUpdateTime() {
		return updateTime;
	}

	/** 
	* 设置修改时间 
	* @param updateTime
	*/
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * 设置日志
	 */
	public void formatHandleLog(String content) {
		List<BankCardLog> logs = this.getLogs();
		logs.add(new BankCardLog(RestUtils.getLocalHost(), new Date(), content));
	}

}

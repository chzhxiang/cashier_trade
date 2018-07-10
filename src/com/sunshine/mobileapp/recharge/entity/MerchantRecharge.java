/**
 * <html>
 * <body>
 *  <P> Copyright 2017 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年10月26日</p>
 *  <p> Created by 党参</p>
 *  </body>
 * </html>
 */
package com.sunshine.mobileapp.recharge.entity;

import java.io.Serializable;
import java.util.Date;

import com.sunshine.framework.mvc.mongodb.entity.BaseMongoEntity;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.mobileapp.deposit.entity
 * @ClassName: Deposit
 * @Description: <p>商户申请充值、审核</p>
 * @JDK version used: 
 * @Author: 党参
 * @Create Date: 2017年10月26日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class MerchantRecharge extends BaseMongoEntity implements Serializable {
	/**
	 *  
	 */
	private static final long serialVersionUID = 6554078805391781425L;

	/**
	 * 商户号
	 */
	private String merchantNo;

	/**
	 * 商户名称
	 */
	private String merchantName;

	/**
	 * 子商户号
	 */
	private String subMerchantNo;

	/**
	 * 子商户名称
	 */
	private String subMerchantName;

	/**
	 * 商户充值时当前余额
	 */
	private Integer currentBalance;

	/**
	 * 充值金额
	 */
	private Integer rechargeAmount;

	/**
	 * 申请人ID
	 */
	private String applyUserId;

	/**
	 * 申请人姓名
	 */
	private String applyUserName;

	/**
	 * 申请人帐号
	 */
	private String applyAccount;

	/**
	 * 申请时间
	 */
	private Date applyDate;

	/**
	 * 申请备注
	 */
	private String applyRemark;

	/**
	 * 审核人ID
	 */
	private String approvalUserId;

	/**
	 * 审核人姓名
	 */
	private String approvalUserName;

	/**
	 * 审核人帐号
	 */
	private String approvalAccount;

	/**
	 * 审批时间
	 */
	private Date approvalDate;

	/**
	 * 审批备注
	 */
	private String approvalRemark;

	/**
	 * 审批状态
	 * 1:待审核 2:审核通过 3:审核不通过
	 */
	private Integer approvalStatus;

	/**
	 * 
	 */
	public MerchantRecharge() {
		super();
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getSubMerchantNo() {
		return subMerchantNo;
	}

	public void setSubMerchantNo(String subMerchantNo) {
		this.subMerchantNo = subMerchantNo;
	}

	public String getSubMerchantName() {
		return subMerchantName;
	}

	public void setSubMerchantName(String subMerchantName) {
		this.subMerchantName = subMerchantName;
	}

	public Integer getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(Integer currentBalance) {
		this.currentBalance = currentBalance;
	}

	public Integer getRechargeAmount() {
		return rechargeAmount;
	}

	public void setRechargeAmount(Integer rechargeAmount) {
		this.rechargeAmount = rechargeAmount;
	}

	public String getApplyUserId() {
		return applyUserId;
	}

	public void setApplyUserId(String applyUserId) {
		this.applyUserId = applyUserId;
	}

	public String getApplyUserName() {
		return applyUserName;
	}

	public void setApplyUserName(String applyUserName) {
		this.applyUserName = applyUserName;
	}

	public String getApplyAccount() {
		return applyAccount;
	}

	public void setApplyAccount(String applyAccount) {
		this.applyAccount = applyAccount;
	}

	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	public String getApplyRemark() {
		return applyRemark;
	}

	public void setApplyRemark(String applyRemark) {
		this.applyRemark = applyRemark;
	}

	public String getApprovalUserId() {
		return approvalUserId;
	}

	public void setApprovalUserId(String approvalUserId) {
		this.approvalUserId = approvalUserId;
	}

	public String getApprovalUserName() {
		return approvalUserName;
	}

	public void setApprovalUserName(String approvalUserName) {
		this.approvalUserName = approvalUserName;
	}

	public String getApprovalAccount() {
		return approvalAccount;
	}

	public void setApprovalAccount(String approvalAccount) {
		this.approvalAccount = approvalAccount;
	}

	public Date getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}

	public String getApprovalRemark() {
		return approvalRemark;
	}

	public void setApprovalRemark(String approvalRemark) {
		this.approvalRemark = approvalRemark;
	}

	public Integer getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(Integer approvalStatus) {
		this.approvalStatus = approvalStatus;
	}
}

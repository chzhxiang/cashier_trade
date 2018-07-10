/**
 * <html>
 * <body>
 *  <P> Copyright 2017 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年9月12日</p>
 *  <p> Created by 党参</p>
 *  </body>
 * </html>
 */
package com.sunshine.mobileapp.order.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @Project: cashier_trade
 * @Package: com.sunshine.mobileapp.order.entity
 * @ClassName: TradeOrder
 * @Description:
 *               <p>支付交易异常订单</p>
 * @JDK version used:
 * @Author: 党参
 * @Create Date: 2017年9月12日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class ExceptionTradeOrder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2251199027772884231L;

	/**
	 * 订单id
	 */
	private String id;

	/**
	 * 交易状态
	 */
	private Integer tradeStatus;

	/**
	 * 创建时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/**
	 * 支付申请时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date payApplyTime;

	/**
	 * 商户支付交易业务订单号
	 */
	private String outTradeNo;

	/**
	 * 平台支付交易订单号
	 */
	private String tradeNo;

	/**
	 * 第三方支付平台支付交易订单号
	 */
	private String agtTradeNo;

	/**
	 * 支付金额(总金额).单位分 退费时需检查总金额是否正确
	 */
	private Integer totalFee;

	/**
	 * 支付时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date payTime;

	/**
	 * 订单标题
	 */
	private String subject;

	/**
	 * 附加数据
	 */
	private String attach;

	/**
	 * 商户退费业务订单号
	 */
	private String outRefundNo;

	/**
	 * 退费交易订单号
	 */
	private String refundNo;

	/**
	 * 第三方支付平台退费订单号
	 */
	private String agtRefundNo;

	/**
	 * 退费金额,单位分
	 */
	private Integer refundFee;

	/**
	 * 退费时间，毫秒时间戳
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date refundTime;

	/**
	 * 退款原因
	 */
	private String refundDesc;

	/**
	 * 商户号。即平台提供的医院编码
	 */
	private String merchantNo;

	/**
	 * 商户名称
	 */
	private String merchantName;

	/**
	 * 应用平台Id
	 */
	private String appId;

	/**
	 * 应用平台名称
	 */
	private String appName;

	/**
	 * 渠道编码
	 */
	private String channelCode;

	/**
	 * 第三方平台编码
	 */
	private String platformCode;

	/**
	 * 渠道名称
	 */
	private String channelName;

	/**
	 * 异常处理次数
	 */
	private Integer handleCount;

	/**
	 * 是否发生异常 0 否 1是
	 */
	private Integer isException;

	/**
	 * 处理是否成功
	 */
	private Integer isSuccess;

	/**
	 * 回调地址
	 */
	private String notifyUrl;

	/**
	 * 回调是否成功 0:否  1:是
	 */
	private Integer isNotify;

	/**
	 * 回调次数
	 */
	private Integer notifyCount;

	/**
	 * 前端跳转地址
	 */
	private String returnUrl;

	/**
	 * 第三方支付回调结果存储
	 */
	private String payJson;

	/**
	 * 第三方退费回调结果存储
	 */
	private String refundJson;

	/**
	 * 支付查询第三方结果存储
	 */
	private String payQueryJson;

	/**
	 * 支付查询标识 ，标记查询是否成功
	 */
	private Integer isPayQuery;

	/**
	 * 退费查询第三方结果存储
	 */
	private String refundQueryJson;

	/**
	 * 退费查询标识 ，标记查询是否成功
	 */
	private Integer isRefundQuery;

	/**
	 * 第三方退费标识，标识第三方退费是否成功
	 */
	private Integer isRefund;

	/***********************************微信特殊参数 start***********************************************/
	/**
	 * opneId 微信jsapi(服务窗)支付特有参数
	 */
	private String openId;

	/**
	 * 创建者ip 微信MWEB(H5)支付特有参数
	 */
	private String spbillCreateIp;

	/***********************************微信特殊参数 end***********************************************/

	/***********************************医程通钱包特殊参数 start***********************************************/
	/**
	 * 用户id
	 */
	private String userId;

	/***********************************医程通钱包特殊参数 end***********************************************/

	/**
	 * 
	 */
	public ExceptionTradeOrder() {
		super();
	}

	public Integer getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(Integer tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getPayApplyTime() {
		return payApplyTime;
	}

	public void setPayApplyTime(Date payApplyTime) {
		this.payApplyTime = payApplyTime;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getAgtTradeNo() {
		return agtTradeNo;
	}

	public void setAgtTradeNo(String agtTradeNo) {
		this.agtTradeNo = agtTradeNo;
	}

	public Integer getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(Integer totalFee) {
		this.totalFee = totalFee;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getOutRefundNo() {
		return outRefundNo;
	}

	public void setOutRefundNo(String outRefundNo) {
		this.outRefundNo = outRefundNo;
	}

	public String getRefundNo() {
		return refundNo;
	}

	public void setRefundNo(String refundNo) {
		this.refundNo = refundNo;
	}

	public String getAgtRefundNo() {
		return agtRefundNo;
	}

	public void setAgtRefundNo(String agtRefundNo) {
		this.agtRefundNo = agtRefundNo;
	}

	public Integer getRefundFee() {
		return refundFee;
	}

	public void setRefundFee(Integer refundFee) {
		this.refundFee = refundFee;
	}

	public Date getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(Date refundTime) {
		this.refundTime = refundTime;
	}

	public String getRefundDesc() {
		return refundDesc;
	}

	public void setRefundDesc(String refundDesc) {
		this.refundDesc = refundDesc;
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

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getPlatformCode() {
		return platformCode;
	}

	public void setPlatformCode(String platformCode) {
		this.platformCode = platformCode;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public Integer getHandleCount() {
		return handleCount;
	}

	public void setHandleCount(Integer handleCount) {
		this.handleCount = handleCount;
	}

	public Integer getIsException() {
		return isException;
	}

	public void setIsException(Integer isException) {
		this.isException = isException;
	}

	public Integer getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(Integer isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public Integer getIsNotify() {
		return isNotify;
	}

	public void setIsNotify(Integer isNotify) {
		this.isNotify = isNotify;
	}

	public Integer getNotifyCount() {
		return notifyCount;
	}

	public void setNotifyCount(Integer notifyCount) {
		this.notifyCount = notifyCount;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPayJson() {
		return payJson;
	}

	public void setPayJson(String payJson) {
		this.payJson = payJson;
	}

	public String getRefundJson() {
		return refundJson;
	}

	public void setRefundJson(String refundJson) {
		this.refundJson = refundJson;
	}

	public String getPayQueryJson() {
		return payQueryJson;
	}

	public void setPayQueryJson(String payQueryJson) {
		this.payQueryJson = payQueryJson;
	}

	public String getRefundQueryJson() {
		return refundQueryJson;
	}

	public void setRefundQueryJson(String refundQueryJson) {
		this.refundQueryJson = refundQueryJson;
	}

	public Integer getIsPayQuery() {
		return isPayQuery;
	}

	public void setIsPayQuery(Integer isPayQuery) {
		this.isPayQuery = isPayQuery;
	}

	public Integer getIsRefundQuery() {
		return isRefundQuery;
	}

	public void setIsRefundQuery(Integer isRefundQuery) {
		this.isRefundQuery = isRefundQuery;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getSpbillCreateIp() {
		return spbillCreateIp;
	}

	public void setSpbillCreateIp(String spbillCreateIp) {
		this.spbillCreateIp = spbillCreateIp;
	}

	public Integer getIsRefund() {
		return isRefund;
	}

	public void setIsRefund(Integer isRefund) {
		this.isRefund = isRefund;
	}

	/** 
	* 获取订单id 
	* @return 订单id
	*/
	public String getId() {
		return id;
	}

	/** 
	* 设置订单id 
	* @param 订单id
	*/
	public void setId(String id) {
		this.id = id;
	}

	public TradeOrder convertObj() {
		TradeOrder order = new TradeOrder();
		BeanUtils.copyProperties(this, order);
		return order;
	}

}

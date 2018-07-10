package com.sunshine.restful.dto.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.sunshine.restful.dto.TradeParams;

/**
 * 
 * @Project: cashier_trade 
 * @Package: com.sunshine.restful.dto.request
 * @ClassName: PayParamsVo
 * @Description: <p>支付交易请求参数</p>
 * @JDK version used: 
 * @Author: 党参
 * @Create Date: 2017年9月20日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RequestPay extends TradeParams {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5074119586122897367L;

	/**
	 * 商户订单号
	 */
	private String outOrderNo;

	/**
	 * 商品信息
	 */
	private String subject;

	/**
	 * 支付超时时间
	 */
	private String outTime;

	/**
	 * 附加参数
	 */
	private String attach;

	/**
	 * 金额 单位:分
	 */
	private Integer tradeTotalFee;

	/**
	 * 异步通知地址
	 */
	private String notifyUrl;

	/**
	 * 支付后，前端跳转地址
	 */
	private String returnUrl;

	/**
	 * 微信openid,微信公众号支付时使用
	 */
	private String openid;

	/**
	 * 医院id
	 */
	private String hospitalId;

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

	public RequestPay() {
		super();
	}

	public String getOutOrderNo() {
		return outOrderNo;
	}

	public void setOutOrderNo(String outOrderNo) {
		this.outOrderNo = outOrderNo;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getOutTime() {
		return outTime;
	}

	public void setOutTime(String outTime) {
		this.outTime = outTime;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public Integer getTradeTotalFee() {
		return tradeTotalFee;
	}

	public void setTradeTotalFee(Integer tradeTotalFee) {
		this.tradeTotalFee = tradeTotalFee;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	/** 
	* 获取医院id 
	* @return hospitalId
	*/
	public String getHospitalId() {
		return hospitalId;
	}

	/** 
	* 设置医院id 
	* @param hospitalId
	*/
	public void setHospitalId(String hospitalId) {
		this.hospitalId = hospitalId;
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

}

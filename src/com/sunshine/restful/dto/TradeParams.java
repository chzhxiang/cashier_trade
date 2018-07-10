package com.sunshine.restful.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @Project YiChenTong_Server
 * @Package com.sunshine.mobileapp.api.dto
 * @ClassName RegParamsVo.java
 * @Description 交易参数请求基类对象
 * @JDK version used 1.8
 * @Author 于策/yu.ce@foxmail.com
 * @Create Date 2017年8月31日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class TradeParams implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1164872315132577613L;

	/**
	 * appId
	 */
	public String appId;

	/**
	 * 商户号
	 */
	public String merchantNo;

	/**
	 * 渠道编码
	 */
	public String channelCode;

	/**
	 * 随机数
	 */
	public String nonceStr;

	/**
	 * 请求时间戳
	 */
	public String timeStamp;

	/**
	 * 签名字符串
	 */
	private String sign;

	/**
	 * 签名方式:MD5、RSA
	 */
	private String signMode;

	public TradeParams() {
		super();
	}

	/**
	 * @param appId
	 * @param merchantNo
	 * @param channelCode
	 * @param nonceStr
	 * @param timeStamp
	 */
	public TradeParams(String appId, String merchantNo, String channelCode, String nonceStr, String timeStamp) {
		super();
		this.appId = appId;
		this.merchantNo = merchantNo;
		this.channelCode = channelCode;
		this.nonceStr = nonceStr;
		this.timeStamp = timeStamp;
	}

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

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSignMode() {
		return signMode;
	}

	public void setSignMode(String signMode) {
		this.signMode = signMode;
	}

}

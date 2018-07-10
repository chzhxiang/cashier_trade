/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年9月13日</p>
 *  <p> Created by hqy</p>
 *  </body>
 * </html>
 */
package com.sunshine.platform.applicationchannel.entity;

import com.sunshine.framework.mvc.mysql.entity.BaseSQLEntity;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.platform.application_channel.entity
 * @ClassName: ApplicationChannel
 * @Description: <p>应用支付渠道</p>
 * @JDK version used: 
 * @Author: 百部
 * @Create Date: 2017年9月13日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class ApplicationChannel extends BaseSQLEntity {

	private static final long serialVersionUID = -5561677008225511553L;

	/**
	 * 商户号
	 */
	private String merchantNo;

	/**
	 * 支付渠道标识值
	 */
	private int channelValue;

	/**
	 * 应用的appid
	 */
	private String appId;

	/**
	 * 子应用APPID
	 */
	private String subAppId;

	/**
	 * 支付渠道编码
	 */
	private String channelCode;

	/**
	 * 支付渠道名称
	 */
	private String channelName;

	/**
	 * 前端显示名称
	 */
	private String channelViewName;

	/**
	 * 同步跳转地址 0:平台地址(默认)   1：商户地址
	 */
	private Integer urlStatus;

	/**
	 * 支付渠道参数json
	 */
	private String paramsJson;

	/**
	 * 序号
	 */
	private int seq;

	/**
	 * 0:未开启      1：开启
	 */
	private int isOpen;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getSubAppId() {
		return subAppId;
	}

	public void setSubAppId(String subAppId) {
		this.subAppId = subAppId;
	}

	public String getChannelViewName() {
		return channelViewName;
	}

	public void setChannelViewName(String channelViewName) {
		this.channelViewName = channelViewName;
	}

	public String getParamsJson() {
		return paramsJson;
	}

	public void setParamsJson(String paramsJson) {
		this.paramsJson = paramsJson;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

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
	 * @return the channelCode
	 */
	public String getChannelCode() {
		return channelCode;
	}

	/**
	 * @param channelCode the channelCode to set
	 */
	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public int getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(int isOpen) {
		this.isOpen = isOpen;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public int getChannelValue() {
		return channelValue;
	}

	public void setChannelValue(int channelValue) {
		this.channelValue = channelValue;
	}

	public Integer getUrlStatus() {
		return urlStatus;
	}

	public void setUrlStatus(Integer urlStatus) {
		this.urlStatus = urlStatus;
	}

}

/**
 * <html>
 * <body>
 *  <P> Copyright 2017 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年11月20日</p>
 *  <p> Created by 党参</p>
 *  </body>
 * </html>
 */
package com.sunshine.platform.order.entity;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.platform.order.entity
 * @ClassName: AggregatedOrderStatistics
 * @Description: <p>订单统计</p>
 * @JDK version used: 
 * @Author: 党参
 * @Create Date: 2017年11月20日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class OrderStatistics {

	/**
	 * 商户名称
	 */
	private String merchantName;

	/**
	 * 商户号
	 */
	private String merchantNo;

	/**
	 * 应用平台名称
	 */
	private String appName;

	/**
	 * 应用平台ID
	 */
	private String appId;

	/**
	 * 渠道名称
	 */
	private String channelName;

	/**
	 * 渠道编码
	 */
	private String channelCode;

	/**
	 * 总交易金额
	 */
	private double totalPrice = 0.00;

	/**
	 * 成功交易金额
	 */
	private double payPrice = 0.00;

	/**
	 * 退款金额
	 */
	private double refundPrice = 0.00;

	/**
	 * 发起笔数
	 */
	private int launchCount = 0;

	/**
	 * 成功笔数
	 */
	private int payCount = 0;

	/**
	 * 退款笔数
	 */
	private int refundCount = 0;

	/**
	 * 转化率
	 */
	private double conversion = 0.00;

	/**
	 * 平均订单金额
	 */
	private double averagePrice = 0.00;

	/**
	 * 
	 */
	public OrderStatistics() {
		super();
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public double getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(double payPrice) {
		this.payPrice = payPrice;
	}

	public double getRefundPrice() {
		return refundPrice;
	}

	public void setRefundPrice(double refundPrice) {
		this.refundPrice = refundPrice;
	}

	public int getLaunchCount() {
		return launchCount;
	}

	public void setLaunchCount(int launchCount) {
		this.launchCount = launchCount;
	}

	public int getPayCount() {
		return payCount;
	}

	public void setPayCount(int payCount) {
		this.payCount = payCount;
	}

	public int getRefundCount() {
		return refundCount;
	}

	public void setRefundCount(int refundCount) {
		this.refundCount = refundCount;
	}

	public double getConversion() {
		return conversion;
	}

	public void setConversion(double conversion) {
		this.conversion = conversion;
	}

	public double getAveragePrice() {
		return averagePrice;
	}

	public void setAveragePrice(double averagePrice) {
		this.averagePrice = averagePrice;
	}

}

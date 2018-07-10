/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年10月13日</p>
 *  <p> Created by hqy</p>
 *  </body>
 * </html>
 */
package com.sunshine.platform.order.entity;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.platform.order.entity
 * @ClassName: OrderDataView
 * @Description: <p>订单总览实体类</p>
 * @JDK version used: 
 * @Author: 百部
 * @Create Date: 2017年10月13日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class OrderDataView {
	/**
	 * 商户名称
	 */
	private String merchantName;

	/**
	 * 应用平台名称
	 */
	private String appName;

	/**
	 * 渠道名称
	 */
	private String channelName;

	/**
	 * 总交易金额
	 */
	private double total_free;

	/**
	 * 退款金额
	 */
	private double refund_free;

	/**
	 * 成功交易金额
	 */
	private double success_free;

	/**
	 * 发起笔数
	 */
	private int sponsor_frequency;

	/**
	 * 退款笔数
	 */
	private int refund_frequency;

	/**
	 * 成功笔数
	 */
	private int success_frequency;

	/**
	 * 转化率
	 */
	private double conversion;

	/**
	 * 平均订单金额
	 */
	private double average_free;

	public double getTotal_free() {
		return total_free;
	}

	public void setTotal_free(double total_free) {
		this.total_free = total_free;
	}

	public double getRefund_free() {
		return refund_free;
	}

	public void setRefund_free(double refund_free) {
		this.refund_free = refund_free;
	}

	public double getSuccess_free() {
		return success_free;
	}

	public void setSuccess_free(double success_free) {
		this.success_free = success_free;
	}

	public int getSponsor_frequency() {
		return sponsor_frequency;
	}

	public void setSponsor_frequency(int sponsor_frequency) {
		this.sponsor_frequency = sponsor_frequency;
	}

	public int getRefund_frequency() {
		return refund_frequency;
	}

	public void setRefund_frequency(int refund_frequency) {
		this.refund_frequency = refund_frequency;
	}

	public int getSuccess_frequency() {
		return success_frequency;
	}

	public void setSuccess_frequency(int success_frequency) {
		this.success_frequency = success_frequency;
	}

	public double getConversion() {
		return conversion;
	}

	public void setConversion(double conversion) {
		this.conversion = conversion;
	}

	public double getAverage_free() {
		return average_free;
	}

	public void setAverage_free(double average_free) {
		this.average_free = average_free;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

}

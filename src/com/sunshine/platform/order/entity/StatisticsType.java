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

import java.util.Map;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.platform.order.entity
 * @ClassName: StatisticsType
 * @Description: <p>统计查询实体类</p>
 * @JDK version used: 
 * @Author: 党参
 * @Create Date: 2017年11月20日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class StatisticsType {
	/**
	 * 统计ID，一般做分组字段用
	 */
	private String _id;
	/**
	 * 总金额
	 */
	private Float totalPrice;

	/**
	 * 支付金额
	 */
	private Float payPrice;

	/**
	 * 退费金额
	 */
	private Float refundPrice;

	/**
	 * 平均金额
	 */
	private Float avgPrice;

	/**
	 * 笔数
	 */
	private Integer count;

	/**
	 * 商户号
	 */
	private String merchantNo;

	/**
	 * 应用ID
	 */
	private String appId;

	/**
	 * 支付渠道编码
	 */
	private String channelCode;

	/**
	 * 交易状态
	 */
	private String tradeStatus;

	/**
	 * 
	 */
	public StatisticsType() {
		super();
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
		if (!StringUtils.isEmpty(_id)) {
			Map<String, Object> map = JSON.parseObject(_id);
			if (map.containsKey("merchantNo")) {
				this.merchantNo = map.get("merchantNo").toString();
			}
			if (map.containsKey("appId")) {
				this.appId = map.get("appId").toString();
			}
			if (map.containsKey("channelCode")) {
				this.channelCode = map.get("channelCode").toString();
			}
			if (map.containsKey("tradeStatus")) {
				this.tradeStatus = map.get("tradeStatus").toString();
			}
		}

	}

	public Float getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Float totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Float getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(Float payPrice) {
		this.payPrice = payPrice;
	}

	public Float getRefundPrice() {
		return refundPrice;
	}

	public void setRefundPrice(Float refundPrice) {
		this.refundPrice = refundPrice;
	}

	public Float getAvgPrice() {
		return avgPrice;
	}

	public void setAvgPrice(Float avgPrice) {
		this.avgPrice = avgPrice;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

}

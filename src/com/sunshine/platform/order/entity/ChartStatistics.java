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

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.platform.order.entity
 * @ClassName: StatisticsType
 * @Description: <p>图形统计分析查询实体类</p>
 * @JDK version used: 
 * @Author: 党参
 * @Create Date: 2017年11月20日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class ChartStatistics {
	/**
	 * 统计ID，一般做分组字段用
	 */
	private String _id;

	/**
	 * 商户号
	 */
	private String merchantNo;

	/**
	 * 应用ID
	 */
	private String appId;

	/**
	 * 日期
	 */
	private String day;

	/**
	 * 支付金额
	 */
	private String payPrice;

	/**
	 * 退费金额
	 */
	private String refundPrice;

	/**
	 * 笔数
	 */
	private Integer count;

	/**
	 * 
	 */
	public ChartStatistics() {
		super();
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
		if (!StringUtils.isEmpty(_id)) {
			Map<String, Object> map = new HashMap<String, Object>();
			try {
				map = JSON.parseObject(_id);
			} catch (Exception e) {

			}

			if (map.containsKey("day")) {
				this.day = map.get("day").toString();
			} else {
				this.day = this._id;
			}

			if (map.containsKey("merchantNo")) {
				this.merchantNo = map.get("merchantNo").toString();
			}
			if (map.containsKey("appId")) {
				this.appId = map.get("appId").toString();
			}
		}

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

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(String payPrice) {
		this.payPrice = payPrice;
	}

	public String getRefundPrice() {
		return refundPrice;
	}

	public void setRefundPrice(String refundPrice) {
		this.refundPrice = refundPrice;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

}

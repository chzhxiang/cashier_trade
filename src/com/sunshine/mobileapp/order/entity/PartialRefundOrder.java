/**
 * <html>
 * <body>
 *  <P> Copyright 2017 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年11月23日</p>
 *  <p> Created by 党参</p>
 *  </body>
 * </html>
 */
package com.sunshine.mobileapp.order.entity;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.sunshine.framework.mvc.mongodb.entity.BaseMongoEntity;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.mobileapp.order.entity
 * @ClassName: PartialRefundOrder
 * @Description: <p>部分退费记录</p>
 * @JDK version used: 
 * @Author: 党参
 * @Create Date: 2017年11月23日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class PartialRefundOrder extends BaseMongoEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7740507329830341390L;

	/**
	 * 退费状态
	 */
	private Integer refundStatus;

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
	 * 退费结果描述
	 */
	private String refundDetail;

	/**
	 * 
	 */
	public PartialRefundOrder() {
		super();
	}

	/**
	 * @param refundStatus
	 * @param outRefundNo
	 * @param refundNo
	 * @param agtRefundNo
	 * @param refundFee
	 * @param refundTime
	 * @param refundDesc
	 */
	public PartialRefundOrder(Integer refundStatus, String outRefundNo, String refundNo, String agtRefundNo, Integer refundFee, Date refundTime,
			String refundDesc) {
		super();
		this.refundStatus = refundStatus;
		this.outRefundNo = outRefundNo;
		this.refundNo = refundNo;
		this.agtRefundNo = agtRefundNo;
		this.refundFee = refundFee;
		this.refundTime = refundTime;
		this.refundDesc = refundDesc;
	}

	/**
	 * @param outRefundNo
	 * @param refundNo
	 * @param refundFee
	 * @param refundDesc
	 */
	public PartialRefundOrder(String outRefundNo, String refundNo, Integer refundFee, String refundDesc) {
		super();
		this.outRefundNo = outRefundNo;
		this.refundNo = refundNo;
		this.refundFee = refundFee;
		this.refundDesc = refundDesc;
	}

	public Integer getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(Integer refundStatus) {
		this.refundStatus = refundStatus;
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

	public String getRefundDetail() {
		return refundDetail;
	}

	public void setRefundDetail(String refundDetail) {
		this.refundDetail = refundDetail;
	}

}

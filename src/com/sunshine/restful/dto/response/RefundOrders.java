/**
 * <html>
 * <body>
 *  <P> Copyright 2017 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年9月20日</p>
 *  <p> Created by 党参</p>
 *  </body>
 * </html>
 */
package com.sunshine.restful.dto.response;

import java.io.Serializable;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.restful.dto.response
 * @ClassName: ResponsePay
 * @Description: <p>部分退费交易返回参数</p>
 * @JDK version used: 
 * @Author: 党参
 * @Create Date: 2017年9月20日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class RefundOrders implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7450659656279158121L;

	/**
	 * 退费状态状态
	 */
	private String refundStates;

	/**
	 * 退费交易订单号
	 */
	private String refundNo;

	/**
	 * 商户退费业务订单号
	 */
	private String outRefundNo;

	/**
	 * 退费金额
	 */
	private String refundFee;

	/**
	 * 退费时间
	 */
	private String refundTime;

	/**
	 * 退款原因
	 */
	private String refundDesc;

	/**
	 * 
	 */
	public RefundOrders() {
		super();
	}

	/**
	 * @param refundNo
	 * @param outRefundNo
	 * @param refundFee
	 * @param refundTime
	 * @param refundDesc
	 */
	public RefundOrders(String refundNo, String outRefundNo, String refundFee, String refundTime, String refundDesc) {
		super();
		this.refundNo = refundNo;
		this.outRefundNo = outRefundNo;
		this.refundFee = refundFee;
		this.refundTime = refundTime;
		this.refundDesc = refundDesc;
	}

	public String getRefundStates() {
		return refundStates;
	}

	public void setRefundStates(String refundStates) {
		this.refundStates = refundStates;
	}

	public String getRefundNo() {
		return refundNo;
	}

	public void setRefundNo(String refundNo) {
		this.refundNo = refundNo;
	}

	public String getOutRefundNo() {
		return outRefundNo;
	}

	public void setOutRefundNo(String outRefundNo) {
		this.outRefundNo = outRefundNo;
	}

	public String getRefundFee() {
		return refundFee;
	}

	public void setRefundFee(String refundFee) {
		this.refundFee = refundFee;
	}

	public String getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(String refundTime) {
		this.refundTime = refundTime;
	}

	public String getRefundDesc() {
		return refundDesc;
	}

	public void setRefundDesc(String refundDesc) {
		this.refundDesc = refundDesc;
	}

}

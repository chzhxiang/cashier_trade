/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年9月22日</p>
 *  <p> Created by hqy</p>
 *  </body>
 * </html>
 */
package com.sunshine.restful.dto.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.sunshine.restful.dto.TradeParams;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.restful.dto.request
 * @ClassName: RequestRefund
 * @Description: <p>退费交易请求参数</p>
 * @JDK version used: 
 * @Author: 百部
 * @Create Date: 2017年9月22日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RequestRefund extends TradeParams {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4448979738445502435L;

	/**
	 * 商户订单号
	 */
	private String outOrderNo;

	/**
	 * 平台订单号
	 */
	private String tradeNo;

	/**
	 * 商户退费订单号
	 */
	private String outRefundNo;

	/**
	 * 平台退费订单号
	 */
	private String refundNo;

	/**
	 * 订单交易总金额
	 */
	private Integer tradeTotalFee;

	/**
	 * 退费金额
	 */
	private Integer refundFee;

	/**
	 * 退款原因
	 */
	private String refundDesc;

	/**
	 * 透传参数
	 */
	private String refundAttach;

	/**
	 * 商户的异步回调地址
	 */
	private String refundNotifyUrl;

	public String getOutOrderNo() {
		return outOrderNo;
	}

	public void setOutOrderNo(String outOrderNo) {
		this.outOrderNo = outOrderNo;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
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

	public Integer getTradeTotalFee() {
		return tradeTotalFee;
	}

	public void setTradeTotalFee(Integer tradeTotalFee) {
		this.tradeTotalFee = tradeTotalFee;
	}

	public Integer getRefundFee() {
		return refundFee;
	}

	public void setRefundFee(Integer refundFee) {
		this.refundFee = refundFee;
	}

	public String getRefundDesc() {
		return refundDesc;
	}

	public void setRefundDesc(String refundDesc) {
		this.refundDesc = refundDesc;
	}

	public String getRefundAttach() {
		return refundAttach;
	}

	public void setRefundAttach(String refundAttach) {
		this.refundAttach = refundAttach;
	}

	public String getRefundNotifyUrl() {
		return refundNotifyUrl;
	}

	public void setRefundNotifyUrl(String refundNotifyUrl) {
		this.refundNotifyUrl = refundNotifyUrl;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}

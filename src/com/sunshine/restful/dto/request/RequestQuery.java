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
 * @Description:
 *               <p>
 *               查询交易请求参数
 *               </p>
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
public class RequestQuery extends TradeParams {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5074119586122897367L;

	/**
	 * 平台支付交易订单号
	 */
	private String tradeNo;

	/**
	 * 商户订单号
	 */
	private String outOrderNo;

	/**
	 * 退费交易订单号
	 */
	private String refundNo;

	/**
	 * 商户退费业务订单号
	 */
	private String outRefundNo;

	public RequestQuery() {
		super();
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
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

	public String getOutOrderNo() {
		return outOrderNo;
	}

	public void setOutOrderNo(String outOrderNo) {
		this.outOrderNo = outOrderNo;
	}

}

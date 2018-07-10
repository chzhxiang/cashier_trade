/**
 * <html>
 * <body>
 *  <P> Copyright 2017 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年10月26日</p>
 *  <p> Created by 熊胆</p>
 *  </body>
 * </html>
 */
package com.sunshine.restful.dto.response;

import com.sunshine.restful.dto.TradeParams;

/**
 * @Project: cashier_trade
 * @Package: com.sunshine.restful.dto.response
 * @ClassName: ResponseLimitPay
 * @Description: 小额支付交易返回参数
 * @JDK version used: 
 * @Author: 熊胆
 * @Create Date: 2017年10月26日下午4:18:26
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class ResponseLimitPay extends TradeParams {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4717862058266728073L;

	/**
	 * 支付状态
	 */
	private String tradeStatus;

	/**
	 * 订单号
	 */
	private String tradeNo;

	/**
	 * 商户订单号
	 */
	private String outOrderNo;

	/**
	 * 支付返回信息
	 */
	private String respMsg;

	/**
	 * 
	 */
	public ResponseLimitPay() {
		super();
	}

	/** 
	* 获取支付状态 
	* @return tradeStatus
	*/
	public String getTradeStatus() {
		return tradeStatus;
	}

	/** 
	* 设置支付状态 
	* @param tradeStatus
	*/
	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	/** 
	* 获取支付返回信息 
	* @return respMsg
	*/
	public String getRespMsg() {
		return respMsg;
	}

	/** 
	* 设置支付返回信息 
	* @param respMsg
	*/
	public void setRespMsg(String respMsg) {
		this.respMsg = respMsg;
	}

	/** 
	* 获取订单号 
	* @return tradeNo
	*/
	public String getTradeNo() {
		return tradeNo;
	}

	/** 
	* 设置订单号 
	* @param tradeNo
	*/
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	/** 
	* 获取商户订单号 
	* @return outOrderNo
	*/
	public String getOutOrderNo() {
		return outOrderNo;
	}

	/** 
	* 设置商户订单号 
	* @param outOrderNo
	*/
	public void setOutOrderNo(String outOrderNo) {
		this.outOrderNo = outOrderNo;
	}

}

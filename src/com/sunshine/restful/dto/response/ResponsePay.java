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

import com.sunshine.restful.dto.TradeParams;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.restful.dto.response
 * @ClassName: ResponsePay
 * @Description: <p>支付交易返回参数</p>
 * @JDK version used: 
 * @Author: 党参
 * @Create Date: 2017年9月20日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class ResponsePay extends TradeParams {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4717862058266728073L;

	/**
	 * 支付请求地址
	 */
	private String cashierUrl;

	/**
	 * 支付请求Id
	 */
	private String cashierId;

	/**
	 * 
	 */
	public ResponsePay() {
		super();
	}

	public String getCashierUrl() {
		return cashierUrl;
	}

	public void setCashierUrl(String cashierUrl) {
		this.cashierUrl = cashierUrl;
	}

	public String getCashierId() {
		return cashierId;
	}

	public void setCashierId(String cashierId) {
		this.cashierId = cashierId;
	}

}

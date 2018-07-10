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
 * 
 * @Project: cashier_trade 
 * @Package: com.sunshine.restful.dto.response
 * @ClassName: ResponsePayQRCode
 * @Description: <p>支付二维码返回参数 </p>
 * @JDK version used: 
 * @Author: 沙参
 * @Create Date: 2017年12月8日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class ResponsePayQRCode extends TradeParams {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4717862058266728073L;

	/**
	 * 二维码支付地址。用于生成二维码后提供给用户微信/支付宝APP扫码支付
	 */
	private String cashierUrl;

	/**
	 * 支付订单Id
	 */
	private String cashierId;

	/**
	 * 
	 */
	public ResponsePayQRCode() {
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

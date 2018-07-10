/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年1月10日</p>
 *  <p> Created by 党参</p>
 *  </body>
 * </html>
 */
package com.sunshine.payment.yctWallet;

import java.io.Serializable;

/**
 * @Project: sunshine_trade 
 * @Package: com.sunshine.trade.payrefund.utils.yctWallet
 * @ClassName: WalletResponse
 * @Description: <p></p>
 * @JDK version used: 
 * @Author: 党参
 * @Create Date: 2017年1月10日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class WalletResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7487873935410538266L;

	/**
	 * 返回状态
	 */
	private String code;

	/**
	 * 返回数据
	 */
	private Object data;

	/**
	 * 返回消息提示
	 */
	private String message;

	/**
	 * 
	 */
	public WalletResponse() {
		super();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}

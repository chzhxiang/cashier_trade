/**
 * <html>
 * <body>
 *  <P> Copyright 2017 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年9月15日</p>
 *  <p> Created by 党参</p>
 *  </body>
 * </html>
 */
package com.sunshine.restful;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.restful
 * @ClassName: RestResponse
 * @Description: <p></p>
 * @JDK version used: 
 * @Author: 党参
 * @Create Date: 2017年9月15日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RestResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1035989621889068498L;

	/**
	 * 返回状态码
	 */
	private String returnCode;

	/**
	 * 返回信息
	 */
	private String returnMsg;

	/**
	 * 返回结果集
	 */
	private Object data;

	/**
	 * 
	 */
	public RestResponse() {
		super();
	}

	/**
	 * @param returnCode
	 * @param returnMsg
	 */
	public RestResponse(String returnCode, String returnMsg) {
		super();
		this.returnCode = returnCode;
		this.returnMsg = returnMsg;
	}

	/**
	 * @param returnCode
	 * @param returnMsg
	 * @param date
	 */
	public RestResponse(String returnCode, String returnMsg, Object data) {
		super();
		this.returnCode = returnCode;
		this.returnMsg = returnMsg;
		this.data = data;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}

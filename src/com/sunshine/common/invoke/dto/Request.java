/**
 * <html>
 * <body>
 *  <P>  Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p>  All rights reserved.</p>
 *  <p> Created on 2015年4月28日</p>
 *  <p> Created by 周鉴斌</p>
 *  </body>
 * </html>
 */
package com.sunshine.common.invoke.dto;

import java.io.Serializable;

/**
 * 对外接口入参
 * @Project: cashier_trade 
 * @Package: com.sunshine.common.invoke.dto
 * @ClassName: Request
 * @Description: <p> </p>
 * @JDK version used: 
 * @Author: 沙参
 * @Create Date: 2017年12月21日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class Request implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6891202373238554744L;
	/**
	 * 函数code
	 */
	private String methodCode;
	/**
	 * 返回参数格式 0：xml 1：json 默认为0
	 */
	private String responseType;
	/**
	 * 参数，json格式
	 */
	private String methodParams;

	public Request() {
		super();
	}

	public Request(String methodCode, String methodParams) {
		super();
		this.methodCode = methodCode;
		this.methodParams = methodParams;
	}

	/**
	 * @return the methodCode
	 */
	public String getMethodCode() {
		return methodCode;
	}

	/**
	 * @param methodCode the methodCode to set
	 */
	public void setMethodCode(String methodCode) {
		this.methodCode = methodCode;
	}

	/**
	 * @return the responseType
	 */
	public String getResponseType() {
		return responseType;
	}

	/**
	 * @param responseType the responseType to set
	 */
	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}

	/**
	 * @return the methodParams
	 */
	public String getMethodParams() {
		return methodParams;
	}

	/**
	 * @param methodParams the methodParams to set
	 */
	public void setMethodParams(String methodParams) {
		this.methodParams = methodParams;
	}

}

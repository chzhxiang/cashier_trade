/**
 * <html>
 * <body>
 *  <P>  Copyright © 版权所有2016 广东天泽阳光康众医疗投资管理有限公司.</p>
 *  <p>  All rights reserved.</p>
 *  <p> Created on 2015年4月29日</p>
 *  <p> Created by 申午武</p>
 *  </body>
 * </html>
 */
package com.sunshine.common.vo;

import java.io.Serializable;

/**
 * 统一的响应格式
 * @Package: com.sunshine.framework.mvc.controller
 * @ClassName: AjaxResponse
 * @Statement: <p>
 *             </p>
 * @JDK version used:
 * @Author: 申姜
 * @Create Date: 2016-4-2
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class Response implements Serializable {

	private static final long serialVersionUID = 5905715228490291386L;
	/**
	 * 结果状态码
	 */
	private String resultCode;
	/**
	 * 描述
	 */
	private String resultMessage;
	/**
	 * 结果
	 */
	private Object result;

	public Response() {
		super();
	}

	public Response(String resultCode) {
		super();
		this.resultCode = resultCode;
	}

	public Response(String resultCode, String resultMessage) {
		super();
		this.resultCode = resultCode;
		this.resultMessage = resultMessage;
	}

	public Response(String resultCode, Object result) {
		super();
		this.resultCode = resultCode;
		this.result = result;
	}

	public Response(String resultCode, String resultMessage, Object result) {
		super();
		this.resultCode = resultCode;
		this.resultMessage = resultMessage;
		this.result = result;
	}

	/**
	 * 添加成功结果信息
	 * @param resultMessage
	 */
	public void addSuccess(String resultMessage) {
		this.resultCode = ResponseCode.SUCCESS;
		this.resultMessage = resultMessage;
	}

	/**
	 * 添加成功结果信息
	 * @param result
	 */
	public void addSuccess(Object result) {
		this.resultCode = ResponseCode.SUCCESS;
		this.result = result;
	}

	/**
	 * 添加成功结果信息
	 * @param resultMessage
	 * @param result
	 */
	public void addSuccess(String resultMessage, Object result) {
		this.resultCode = ResponseCode.SUCCESS;
		this.resultMessage = resultMessage;
		this.result = result;
	}

	/**
	 * 添加错误消息
	 */
	public void addFailure(String resultMessage) {
		this.resultCode = ResponseCode.FAILURE;
		this.resultMessage = resultMessage;
	}

	/**
	 * @return the resultCode
	 */
	public String getResultCode() {
		return resultCode;
	}

	/**
	 * @param resultCode the resultCode to set
	 */
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	/**
	 * @return the resultMessage
	 */
	public String getResultMessage() {
		return resultMessage;
	}

	/**
	 * @param resultMessage the resultMessage to set
	 */
	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}

	/**
	 * @return the result
	 */
	public Object getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(Object result) {
		this.result = result;
	}

}

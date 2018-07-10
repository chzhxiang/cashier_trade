
package com.sunshine.common.invoke;

/**
 * 对外接口返回状态
 * @Project: cashier_trade 
 * @Package: com.sunshine.common.invoke
 * @ClassName: InvokeConstant
 * @Description: <p> </p>
 * @JDK version used: 
 * @Author: 沙参
 * @Create Date: 2017年12月21日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */

public class InvokeConstant {
	/**
	 * 失败
	 */
	public static final String[] REQUEST_INTERFACE_ERROR = { "-1", "请求接口失败" };
	/**
	 * 成功
	 */
	public static final String[] SUCCESS = { "0", "成功" };
	/**
	 * 成功,但未查询到记录
	 */
	public static final String[] SUCCESS_NO_DATA = { "1", "未查询到记录" };
	/**
	 * 成功,但不符合限定
	 */
	public static final String[] SUCCESS_NOT_LIMIT = { "2", "不符合限定" };
	/**
	 * 成功,但重复操作
	 */
	public static final String[] SUCCESS_REPEAT_OPERATION = { "3", "重复操作" };

	public static final String[] ERROR_DESC_CHECK_SIGN_FAIL = { "4", "签名错误" };

	/** 接口返回参数格式 0：xml, 1：json 默认为0 */
	public static final String RESPONSE_TYPE_XML = "0";
	public static final String RESPONSE_TYPE_JSON = "1";

}

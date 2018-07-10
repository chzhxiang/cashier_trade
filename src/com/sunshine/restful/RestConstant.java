/**
 * <html>
 * <body>
 *  <P> Copyright 2017 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年9月8日</p>
 *  <p> Created by 党参</p>
 *  </body>
 * </html>
 */
package com.sunshine.restful;

/**
 * @Project: cashier_desk 
 * @Package: com.sunshine.framework.rest.auth
 * @ClassName: RestConstant
 * @Description: <p>rest常量参数及返回参数</p>
 * @JDK version used: 
 * @Author: 党参
 * @Create Date: 2017年9月8日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class RestConstant {

	/**
	 * 商户应用appId标识字符
	 */
	public static final String PARAMS_APPID = "appId";

	/**
	 * 商户号标识字符
	 */
	public static final String PARAMS_MERCHANTNO = "merchantNo";

	/**
	 * 签名字符串标识字符
	 */
	public static final String PARAM_SIGN = "sign";

	/**
	 * 签名方式字符串标识字符
	 */
	public static final String PARAM_SIGN_MODE = "signMode";

	/**
	 * 随机数标识字符
	 */
	public static final String PARAM_NONCESTR = "nonceStr";

	/**
	 * 渠道编码标识字符
	 */
	public static final String PARAM_CHANNELCODE = "channelCode";

	/**
	 * 请求时间戳标识字符
	 */
	public static final String PARAM_TIMESTAMP = "timeStamp";

	/**
	 * 必传参数集合
	 */
	public static final String[] MAST_PARAMS_ITEM = { PARAMS_APPID, PARAMS_MERCHANTNO, PARAM_NONCESTR, PARAM_TIMESTAMP, PARAM_SIGN, PARAM_SIGN_MODE };

	/**
	 * 请求成功
	 */
	public static final String RETURN_SUCCESS[] = { "SUCCESS", "请求成功" };

	/**
	 * 请求失败
	 */
	public static final String RETURN_FAIL[] = { "FAIL", "请求失败" };

	/**
	 * 接口后台错误
	 */
	public static final String RETURN_SYSTEMERROR[] = { "SYSTEMERROR", "接口后台错误" };

	/**
	 * 请求参数错误
	 */
	public static final String RETURN_PARAM_ERROR[] = { "PARAM_ERROR", "请求参数错误" };

	/**
	 * 请求参数JSON格式错误
	 */
	public static final String RETURN_PARAM_JSON_FORMAT_ERROR[] = { "PARAM_JSON_FORMAT_ERROR", "请求参数JSON格式错误" };

	/**
	 * 权限验证失败
	 */
	public static final String RETURN_SIGN_FAIL[] = { "SIGN_FAIL", "签名验证失败" };

	/**
	 * 时间戳校验失败
	 */
	public static final String RETURN_TIME_STAMP_ERROR[] = { "TIME_STAMP_ERROR", "时间戳校验失败" };

	/**
	 * 不支持的请求方式
	 */
	public static final String RETURN_METHOD_ERROR[] = { "METHOD_ERROR", "不支持的请求方式" };

	/**
	 * appId或者商户号错误
	 */
	public static final String RETURN_INVALID_APPID_OR_MERCHANTNO[] = { "INVALID_APPID_OR_MERCHANTNO", "appId或者商户号错误" };

	/**
	 * 商户订单号重复
	 */
	public static final String RETURN_OUTORDERNO_USED[] = { "OUTORDERNO_USED", "商户订单号重复" };

	/**
	 * 商户号错误或者未启用
	 */
	public static final String RETURN_INVALID_MERCHANTNO[] = { "INVALID_MERCHANTNO", "商户号错误或商户未启用" };

	/**
	 * 服务正在升级中，敬请期待
	 */
	public static final String RETURN_SERVICE_UPGRADE[] = { "SERVICE_UPGRADE", "服务正在升级中，敬请期待" };

	/**
	 * 商户订单号不存在
	 */
	public static final String RETURN_OUTORDERNO_NO_EXIST[] = { "OUTORDERNO_NO_EXIST", "商户订单不存在" };

	/**
	 * 商户订单状态不支持退款
	 */
	public static final String RETURN_OUTORDERTRADE_ERROR[] = { "OUTORDERTRADE_ERROR", "商户订单状态不支持退款" };

	/**
	 * 商户订单异常
	 */
	public static final String RETURN_OUTORDERTRADE_EXCEPTION[] = { "OUTORDERTRADE_EXCEPTION", "商户订单异常" };

	/**
	 * 商户订单金额错误
	 */
	public static final String RETURN_OUTORDERTRADE_FREE_ERROR[] = { "OUTORDERTRADE_FREE_ERROR", "商户订单金额错误" };

	/**
	 * 银联交易金额超限
	 */
	public static final String RETURN_TRADEFEE_OVERRUN_ERROR[] = { "TRADEFEE_OVERRUN_ERROR", "银联交易金额超限" };
}

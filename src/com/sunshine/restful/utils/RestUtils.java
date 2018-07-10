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
package com.sunshine.restful.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.sunshine.framework.config.SystemConfig;
import com.sunshine.mobileapp.order.entity.TradeOrder;
import com.sunshine.payment.TradeConstant;
import com.sunshine.restful.RestConstant;
import com.sunshine.restful.dto.request.RequestPay;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.restful.utils
 * @ClassName: RestUtils
 * @Description: <p>rest请求工具类</p>
 * @JDK version used: 
 * @Author: 党参
 * @Create Date: 2017年9月15日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class RestUtils {

	/**
	 * 返回状态码map集合中的key
	 */
	public static final String RETURN_CODE_KEY = "returnCode";

	/**
	 * 返回状态消息map集合中的key
	 */
	public static final String RETURN_MSG_KEY = "returnMsg";

	/**
	 * 返回内容map集合中的key
	 */
	public static final String RETURN_DATA_KEY = "data";

	/**
	 * http协议/https协议
	 */
	public static final String SYSTEM_REQUEST_SCHEME = SystemConfig.getStringValue("system.request.scheme");

	/**
	 * api接口域名
	 */
	public static final String API_TRADE_DOMAIN_NAME = SystemConfig.getStringValue("api.trade.domain.name");

	/**
	 * 微信普通商户公众号支付域名
	 */
	public static final String WECHAT_TRADE_DOMAIN_NAME = SystemConfig.getStringValue("wechat.trade.domain.name");

	/**
	 * 验证标识
	 */
	public static final String CHECK_IS_VALID = "isValid";

	/**
	 * 验证结果消息
	 */
	public static final String CHECK_RES_MSG = "checkResMsg";

	/**
	 * 验证不能为空常量
	 */
	public static final String NOT_BLANK = "不能为空";

	/**
	 * 验证必传参数常量
	 */
	public static final String MAST_PARAMS = "为必传参数";

	/**
	 * 验证必传参数和必传参数值是否为空
	 * @param param
	 */
	public static Map<String, String> validateParams(Map<String, String> param) {
		Map<String, String> retMap = new HashMap<String, String>();
		for (String item : RestConstant.MAST_PARAMS_ITEM) {
			if (!param.containsKey(item)) {
				retMap.put(RETURN_CODE_KEY, RestConstant.RETURN_PARAM_ERROR[0]);
				retMap.put(RETURN_MSG_KEY, item.concat(MAST_PARAMS));
				break;
			}
			if (StringUtils.isEmpty(param.get(item))) {
				retMap.put(RETURN_CODE_KEY, RestConstant.RETURN_PARAM_ERROR[0]);
				retMap.put(RETURN_MSG_KEY, item.concat(NOT_BLANK));
				break;
			}
		}
		if (retMap.isEmpty()) {
			String signMode = param.get(RestConstant.PARAM_SIGN_MODE);
			if (!TradeConstant.SIGN_TYPE_RSA.equals(signMode) || TradeConstant.SIGN_TYPE_MD5.equals(signMode)) {
				retMap.put(RETURN_CODE_KEY, RestConstant.RETURN_PARAM_ERROR[0]);
				retMap.put(RETURN_MSG_KEY, RestConstant.PARAM_SIGN_MODE.concat("错误，根据签名方式填写固定值RSA/MD5"));
			}
			if (TradeConstant.SIGN_TYPE_MD5.equals(signMode)) {
				retMap.put(RETURN_CODE_KEY, RestConstant.RETURN_SERVICE_UPGRADE[0]);
				retMap.put(RETURN_MSG_KEY, RestConstant.RETURN_SERVICE_UPGRADE[1].concat("，签名方式请使用RSA"));
			}
		}
		return retMap;
	}

	/**
	 * 检查参数是否为空
	 * @param obj
	 * @param propertyNames
	 * @return
	 */
	public static Map<String, Object> notBlank(Object obj, String... propertyNames) {
		Map<String, Object> resMap = new HashMap<>();
		StringBuffer sb = new StringBuffer();
		for (String propertyName : propertyNames) {
			Object val = ReflectionUtils.invokeGetterMethod(obj, propertyName);
			if (val == null || "".equalsIgnoreCase(val.toString())) {
				sb.append(propertyName).append(NOT_BLANK);
			}
		}

		if (sb.length() > 0) {
			resMap.put(CHECK_IS_VALID, false);
			resMap.put(CHECK_RES_MSG, sb.toString());
		} else {
			resMap.put(CHECK_IS_VALID, true);
		}
		return resMap;
	}

	/**
	 * 检查订单号是否重复
	 * @return
	 */
	public static Boolean isRepeatOutOrderNo(RequestPay vo, TradeOrder order) {
		if (order.getTotalFee().intValue() != vo.getTradeTotalFee().intValue()) {
			return true;
		}
		if (!order.getSubject().equals(vo.getSubject())) {
			return true;
		}
		//如果订单已支付、已退费、退费中、关闭，则通知用户订单重复
		if (TradeConstant.TRADE_STATUS_PAYMENT == order.getTradeStatus() || TradeConstant.TRADE_STATUS_REFUND == order.getTradeStatus()
				|| TradeConstant.TRADE_STATUS_REFUNDING == order.getTradeStatus() || TradeConstant.TRADE_STATUS_CLOSE == order.getTradeStatus()) {
			return true;
		}
		return false;
	}

	/**
	 * 返回请求协议接域名，即返回访问地址
	 * @return
	 */
	public static String getTradeDomainUrl() {
		return RestUtils.SYSTEM_REQUEST_SCHEME.concat(RestUtils.API_TRADE_DOMAIN_NAME);
	}

	/**
	 * 获取ip
	 * @return
	 */
	public static String getLocalHost() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return "该服务器没有设置IP地址";
		}
	}

	/**
	 * 获取请求参数并转MAP集合
	 * @param requestParams
	 * @return
	 */
	public static Map<String, String> getRequestMap(Map requestParams) {
		Map<String, String> params = new HashMap<String, String>();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = ( i == values.length - 1 ) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			params.put(name, valueStr);
		}
		return params;
	}

	public static void main(String[] args) {
		System.out.println(getLocalHost());
	}
}

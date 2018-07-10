package com.sunshine.payment.alipay;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 
 * @Project: YiChenTong_trade 
 * @Package: com.sunshine.framework.utils
 * @ClassName: HttpUtils
 * @Description: <p> http相关工具类</p>
 * @JDK version used: 
 * @Author: 沙参
 * @Create Date: 2017年9月1日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class HttpUtils {

	/**
	 * http请求返回状态. 成功
	 */
	public final static String HTTP_STATUS_SUCCESS = "200";

	/**
	 * 转换map
	 * @param requestMap
	 * @return
	 */
	public static Map<String, String> parseRequestMap(Map<String, String[]> requestMap) {
		return parseRequestMap(requestMap, ",");
	}

	/**
	 * 转换map
	 * @param requestMap
	 * @param valueSplit, 多值时的拼接符
	 * @return
	 */
	public static Map<String, String> parseRequestMap(Map<String, String[]> requestMap, String valueSplit) {
		Map<String, String> resultMap = new HashMap<String, String>();
		for (Iterator iter = requestMap.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestMap.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = valueStr + values[i];
				if (i < values.length - 1) {
					valueStr = valueStr + ",";
				}
			}
			//乱码解决，这段代码在出现乱码时使用。
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			resultMap.put(name, valueStr);
		}
		return resultMap;
	}

}

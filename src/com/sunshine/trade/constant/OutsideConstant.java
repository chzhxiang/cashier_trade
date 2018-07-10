package com.sunshine.trade.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @Project: YiChenTong_trade 
 * @Package: com.sunshine.trade.cashier.constants
 * @ClassName: OutsideConstant
 * @Description: <p>对外服务接口相关常量 </p>
 * @JDK version used: 
 * @Author: 沙参
 * @Create Date: 2017年8月17日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class OutsideConstant {

	/**
	 * 生成支付二维码接口
	 */
	public static final String METHOD_CREATE_QRCODE = "payQRCode";


	/**
	 * 接口方法Map
	 */
	public static Map<Object, Object> methodCodeParams = new HashMap<Object, Object>();

	static {
		methodCodeParams.put(METHOD_CREATE_QRCODE, METHOD_CREATE_QRCODE);
	}


}

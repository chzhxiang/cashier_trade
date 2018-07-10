/**
 * <html>
 * <body>
 *  <P> Copyright 2017 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年10月30日</p>
 *  <p> Created by 党参</p>
 *  </body>
 * </html>
 */
package com.sunshine.payment.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.sunshine.framework.common.spring.ext.SpringContextHolder;
import com.sunshine.mobileapp.invoke.InsideInvokeDataService;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.payment.utils
 * @ClassName: InvokeDubboUtils
 * @Description: <p></p>
 * @JDK version used: 
 * @Author: 党参
 * @Create Date: 2017年10月30日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
@SuppressWarnings("unchecked")
public class InvokeDubboUtils {

	private static Logger logger = LoggerFactory.getLogger(InvokeDubboUtils.class);

	public static final String PAY_INFO_METHOD_NAME = "getPayInfoByHospitalId";

	public static final String SEND_MSG_VALIDATE = "sendMsgValidate";

	/**
	 * 获取支付参数
	 * @param hospitalId
	 * @return
	 */
	public static Map<String, String> getPayInfo(String hospitalId) {
		String appCode = "yctApp";
		String tradeCode = "healthWallet";
		Map<String, String> tradeMap = new HashMap<String, String>();
		Method invokeMethod = getInvokeMethod(InsideInvokeDataService.class, PAY_INFO_METHOD_NAME);
		InsideInvokeDataService insideInvokeDataService = SpringContextHolder.getBean(InsideInvokeDataService.class);
		logger.info("请求业务平台获取支付参数,平台code:{},交易code:{}, 医院ID:{}", appCode, tradeCode, hospitalId);
		try {
			tradeMap = (Map<String, String>) invokeMethod.invoke(insideInvokeDataService, new Object[] { appCode, tradeCode, hospitalId });
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("请求业务平台获取支付参数:{}", JSON.toJSONString(tradeMap));
		return tradeMap;
	}

	/**
	 * 发送短信
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年11月19日上午10:29:24
	 */
	public static Map<String, Object> sendMsgValidate(String mobile, String sendContent) {
		String validCodeType = "sms";
		Map<String, Object> map = new HashMap<>();
		Method invokeMethod = getInvokeMethod(InsideInvokeDataService.class, SEND_MSG_VALIDATE);
		InsideInvokeDataService insideInvokeDataService = SpringContextHolder.getBean(InsideInvokeDataService.class);
		logger.info("请求业务平台发送短信,手机号:{},验证码:{},发送的内容：{}", mobile, validCodeType, sendContent);
		try {
			map = (Map<String, Object>) invokeMethod.invoke(insideInvokeDataService, new Object[] { mobile, validCodeType, sendContent });
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		logger.info("请求业务平台发送短信返回结果:{}", JSON.toJSONString(map));
		return map;
	}

	private static Method getInvokeMethod(Class<?> clazz, String methodName) {
		Method[] methods = clazz.getMethods();
		Method invokeMethod = null;
		for (Method method : methods) {
			//logger.info("methodName:{}", method.getName());
			if (method.getName().equalsIgnoreCase(methodName)) {
				invokeMethod = method;
				break;
			}
		}
		return invokeMethod;
	}

}

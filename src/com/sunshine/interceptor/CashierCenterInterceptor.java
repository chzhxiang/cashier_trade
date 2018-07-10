/**
 * <html>
 * <body>
 *  <P> Copyright 2017 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年9月26日</p>
 *  <p> Created by 党参</p>
 *  </body>
 * </html>
 */
package com.sunshine.interceptor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.sunshine.common.GlobalConstant;
import com.sunshine.framework.cache.redis.RedisService;
import com.sunshine.framework.common.spring.ext.SpringContextHolder;
import com.sunshine.framework.utils.MD5Utils;
import com.sunshine.restful.RestConstant;
import com.sunshine.restful.sign.SignatureUtils;
import com.sunshine.restful.utils.RestUtils;
import com.sunshine.trade.service.RestAuthorizationService;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.interceptor
 * @ClassName: CashierCenterInterceptor
 * @Description: <p></p>
 * @JDK version used: 
 * @Author: 党参
 * @Create Date: 2017年9月26日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class CashierCenterInterceptor implements HandlerInterceptor {
	private static Logger logger = LoggerFactory.getLogger(CashierCenterInterceptor.class);

	RestAuthorizationService authorizationService = SpringContextHolder.getBean(RestAuthorizationService.class);

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3) throws Exception {

	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3) throws Exception {
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
		//logger.info("请求地址:{}, 请求参数:{}", request.getRequestURI(), JSON.toJSONString(request.getParameterMap()));
		String method = request.getMethod();
		Map<String, String> retMap = new HashMap<String, String>();
		Map<String, String> params = RestUtils.getRequestMap(request.getParameterMap());

		/*if (GlobalConstant.METHOD_GET.equalsIgnoreCase(method)) {
			retMap.put(RestUtils.RETURN_CODE_KEY, RestConstant.RETURN_METHOD_ERROR[0]);
			retMap.put(RestUtils.RETURN_MSG_KEY, RestConstant.RETURN_METHOD_ERROR[1]);
		} else {*/
		logger.info("请求参数:{}", JSON.toJSONString(params));
		RedisService redisService = SpringContextHolder.getBean(RedisService.class);
		//检查必传参数及验签
		retMap = authorizationService.authorization(params);
		if (retMap.isEmpty()) {
			//收银台请求Id
			String cashierId = params.get(GlobalConstant.MOTHED_INVOKE_RES_CASHIER_ID);
			//生成临时鉴权码
			String temporaryAuth = MD5Utils.getMD5(SignatureUtils.buildSortJson(params).concat(params.get(RestConstant.PARAM_SIGN)));
			redisService.set(cashierId, temporaryAuth);
		}
		//}
		logger.info("拦截结果:{}", JSON.toJSONString(retMap));
		if (!retMap.isEmpty()) {
			response.getWriter().print(JSON.toJSONString(retMap));
			return false;
		} else {
			return true;
		}
	}
}

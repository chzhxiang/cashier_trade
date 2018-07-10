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
package com.sunshine.restful.auth;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.sunshine.common.GlobalConstant;
import com.sunshine.framework.common.spring.ext.SpringContextHolder;
import com.sunshine.restful.RestConstant;
import com.sunshine.restful.utils.RestUtils;
import com.sunshine.trade.service.RestAuthorizationService;

/**
 * @Project: cashier_desk 
 * @Package: com.sunshine.framework.rest.auth
 * @ClassName: AuthRequestFilter
 * @Description: <p>请求过滤器,检查请求参数和处理请求鉴权</p>
 * @JDK version used: 
 * @Author: 党参
 * @Create Date: 2017年9月8日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
@SuppressWarnings("unchecked")
public class AuthRequestFilter implements ContainerRequestFilter, ContainerResponseFilter {
	private static final Logger logger = LoggerFactory.getLogger(AuthRequestFilter.class);

	RestAuthorizationService authorizationService = SpringContextHolder.getBean(RestAuthorizationService.class);

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
		Long statrTime = Calendar.getInstance().getTimeInMillis();
		Map<String, String> retMap = new HashMap<String, String>();
		try {
			if (responseContext.getStatus() == 405) {
				retMap.put(RestUtils.RETURN_CODE_KEY, RestConstant.RETURN_METHOD_ERROR[0]);
				retMap.put(RestUtils.RETURN_MSG_KEY, RestConstant.RETURN_METHOD_ERROR[1]);
			} else {
				if (responseContext.getEntity() == null) {
					retMap.put(RestUtils.RETURN_CODE_KEY, RestConstant.RETURN_SYSTEMERROR[0]);
					retMap.put(RestUtils.RETURN_MSG_KEY, RestConstant.RETURN_SYSTEMERROR[1]);
				} else {
					logger.info("responseContext.getEntity():{}", responseContext.getEntity());
					retMap = (Map<String, String>) JSON.parse(JSON.toJSONString(responseContext.getEntity()));
					if (retMap.containsKey(RestUtils.RETURN_DATA_KEY)) {
						Object dataJson = retMap.get(RestUtils.RETURN_DATA_KEY);
						Map<String, String> dataMap = (Map<String, String>) JSON.parse(dataJson.toString());
						retMap.remove(RestUtils.RETURN_DATA_KEY);
						retMap.putAll(dataMap);
						retMap.putAll(authorizationService.responseAuthorization(retMap));
					}
				}
			}
		} catch (Exception e) {
			retMap.put(RestUtils.RETURN_CODE_KEY, RestConstant.RETURN_PARAM_JSON_FORMAT_ERROR[0]);
			retMap.put(RestUtils.RETURN_MSG_KEY, RestConstant.RETURN_PARAM_JSON_FORMAT_ERROR[1]);
			e.printStackTrace();
		}
		Long endTime = Calendar.getInstance().getTimeInMillis();
		logger.info("耗费时间:{} Millis,请求完成后返回集合:{}", ( endTime - statrTime ), JSON.toJSONString(retMap));
		responseContext.setEntity(retMap, null, MediaType.APPLICATION_JSON_TYPE);
	}

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		Long statrTime = Calendar.getInstance().getTimeInMillis();
		String method = requestContext.getMethod();
		Map<String, String> retMap = new HashMap<String, String>();
		// GET方法处理
		Map<String, String> params = null;
		if (GlobalConstant.METHOD_GET.equalsIgnoreCase(method)) {
			params = buildMapParam(requestContext);
		} else if (GlobalConstant.METHOD_POST.equalsIgnoreCase(method)) {
			byte[] buffer = IOUtils.toByteArray(requestContext.getEntityStream());
			byte[] copyBuffer = Arrays.copyOf(buffer, buffer.length);
			requestContext.setEntityStream(new ByteArrayInputStream(buffer));
			String requestData = new String(copyBuffer, "UTF-8");
			try {
				params = (Map<String, String>) JSON.parse(requestData);
			} catch (Exception e) {
				retMap.put(RestUtils.RETURN_CODE_KEY, RestConstant.RETURN_PARAM_JSON_FORMAT_ERROR[0]);
				retMap.put(RestUtils.RETURN_MSG_KEY, RestConstant.RETURN_PARAM_JSON_FORMAT_ERROR[1]);
				e.printStackTrace();
			}
		} else {
			retMap.put(RestUtils.RETURN_CODE_KEY, RestConstant.RETURN_METHOD_ERROR[0]);
			retMap.put(RestUtils.RETURN_MSG_KEY, RestConstant.RETURN_METHOD_ERROR[1]);
		}

		logger.info("请求URL:{}, RESTFULL请求参数:{}", requestContext.getUriInfo().getRequestUri(), JSON.toJSONString(params));
		if (params != null && !params.isEmpty()) {
			retMap = authorizationService.authorization(params);
		} else {
			if (!retMap.containsKey(RestUtils.RETURN_CODE_KEY) || !retMap.containsKey(RestUtils.RETURN_CODE_KEY)) {
				retMap.put(RestUtils.RETURN_CODE_KEY, RestConstant.RETURN_PARAM_ERROR[0]);
				retMap.put(RestUtils.RETURN_MSG_KEY, RestConstant.RETURN_PARAM_ERROR[1]);
			}
		}
		Long endTime = Calendar.getInstance().getTimeInMillis();
		logger.info("耗费时间:{} Millis,请求后,过滤器处理返回结果:{}", ( endTime - statrTime ), JSON.toJSONString(retMap));
		if (!retMap.isEmpty()) {
			requestContext.abortWith(Response.status(200).entity(retMap).build());
		}

	}

	/**
	 * 
	 * @Description 构建参数map集合
	 * @param requestContext
	 * @return
	 * @date 2017年9月6日
	 */
	private Map<String, String> buildMapParam(ContainerRequestContext requestContext) {
		Map<String, String> resultMap = new HashMap<String, String>();
		MultivaluedMap<String, String> valMap = requestContext.getUriInfo().getPathParameters();
		if (!CollectionUtils.isEmpty(valMap)) {
			for (Map.Entry<String, List<String>> entry : valMap.entrySet()) {
				StringBuffer reqDataSb = new StringBuffer();
				if (entry.getValue().size() > 1) {
					for (int i = 0; i < entry.getValue().size(); i++) {
						reqDataSb.append(entry.getValue().get(i));
						if (i < entry.getValue().size() - 1) {
							reqDataSb.append(GlobalConstant.STRING_SPLIT_CHAR);
						}
					}
				} else {
					reqDataSb.append(entry.getValue().get(0));
				}
				resultMap.put(entry.getKey(), reqDataSb.toString());
			}
		}
		return resultMap;
	}

}

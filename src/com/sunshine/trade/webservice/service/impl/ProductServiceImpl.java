package com.sunshine.trade.webservice.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sunshine.common.invoke.InvokeConstant;
import com.sunshine.common.invoke.dto.Request;
import com.sunshine.common.invoke.dto.Response;
import com.sunshine.common.utils.XmlUtil;
import com.sunshine.framework.common.spring.ext.SpringContextHolder;
import com.sunshine.restful.RestResponse;
import com.sunshine.restful.dto.request.RequestPay;
import com.sunshine.restful.trade.service.PayRestService;
import com.sunshine.trade.constant.OutsideConstant;
import com.sunshine.trade.webservice.service.ProductService;

/**
 * 对外服务接口实现
 * @Project: cashier_trade 
 * @Package: com.sunshine.webservice.trade.service.impl
 * @ClassName: RemoteProductServiceImpl
 * @Description: <p> </p>
 * @JDK version used: 
 * @Author: 沙参
 * @Create Date: 2017年12月21日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class ProductServiceImpl implements ProductService {
	private static Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
	PayRestService payServie = SpringContextHolder.getBean(PayRestService.class);

	@SuppressWarnings("unchecked")
	@Override
	public Response openService(Request request) {
		logger.info("接口入参:{}.", JSON.toJSONString(request));
		if (StringUtils.isEmpty(request.getMethodCode())) {
			return new Response(InvokeConstant.SUCCESS_NOT_LIMIT[0], "接口名不能为空");
		}

		if (StringUtils.isEmpty(request.getMethodParams())) {
			return new Response(InvokeConstant.SUCCESS_NOT_LIMIT[0], "接口参数不能为空");
		}
		Response response = new Response();
		try {

			if (!OutsideConstant.methodCodeParams.containsKey(request.getMethodCode().trim())) {
				response = new Response(InvokeConstant.SUCCESS_NOT_LIMIT[0], "接口名不存在");
				logger.info("对外服务接口结果。{}", JSON.toJSONString(response));
				return response;
			}
			
			String responseType = request.getResponseType();
			JSONObject parseObject = null;
			if (InvokeConstant.RESPONSE_TYPE_XML.equals(responseType)) {
				parseObject = XmlUtil.xmlToJson(request.getMethodParams());
			} else {
				parseObject = JSON.parseObject(request.getMethodParams());
			}

			if (OutsideConstant.METHOD_CREATE_QRCODE.equals(request.getMethodCode().trim())) {
				// 生成支付二维码接口
				RequestPay qrCodeParams = JSON.toJavaObject(parseObject, RequestPay.class);
				logger.info("生成支付二维码接口入参:{}.", JSON.toJSONString(qrCodeParams));
				RestResponse restResponse = payServie.payQRCode(qrCodeParams);
				logger.info("生成支付二维码接口回参参:{}.", JSON.toJSONString(restResponse));
				if (null == restResponse.getData()) {
					logger.info("接口处理出错");
					response = new Response(InvokeConstant.REQUEST_INTERFACE_ERROR[0], restResponse.getReturnMsg());
					return response;
				}
				Map<String, String> resultMap = XmlUtil.toMap(restResponse.getData());
				String result = "";
				if (InvokeConstant.RESPONSE_TYPE_XML.equals(responseType)) {
					result = XmlUtil.map2Xml(resultMap);
				} else {
					result = JSON.toJSONString(resultMap, SerializerFeature.WriteNullStringAsEmpty);
				}
				// logger.info("生成支付二维码接口结果:{}.", result);
				response = new Response(InvokeConstant.SUCCESS[0], InvokeConstant.SUCCESS[1], result);
			} else {
				logger.info("没有匹配的接口方法。{}", request.getMethodCode());
				response = new Response(InvokeConstant.SUCCESS_NOT_LIMIT[0], "接口正在建设中");
			}
		} catch (Exception e) {
			logger.error("对外服务接口异常。", e);
			response = new Response(InvokeConstant.REQUEST_INTERFACE_ERROR[0], "接口异常");
		}

		logger.info("对外服务接口结果。{}", JSON.toJSONString(response));
		return response;
	}
}

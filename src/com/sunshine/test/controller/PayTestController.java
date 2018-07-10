package com.sunshine.test.controller;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.sunshine.common.invoke.dto.Request;
import com.sunshine.common.invoke.dto.Response;
import com.sunshine.common.utils.XmlUtil;
import com.sunshine.framework.common.spring.ext.SpringContextHolder;
import com.sunshine.framework.mvc.controller.RespBody;
import com.sunshine.framework.mvc.controller.RespBody.StatusEnum;
import com.sunshine.payment.alipay.HttpUtils;
import com.sunshine.payment.utils.http.HttpClient;
import com.sunshine.restful.sign.SignatureUtils;
import com.sunshine.trade.webservice.service.ProductService;

@Controller
@RequestMapping(value = { "/test" })
public class PayTestController {
	Logger logger = LoggerFactory.getLogger(PayTestController.class);

	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request) {
		try {
			logger.info("test/index. ");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("trade/index");
	}

	/* 
	 * 获取签名和随机数
	 *  */
	@ResponseBody
	@RequestMapping(value = "/getSign", method = RequestMethod.POST)
	public RespBody getSign(HttpServletRequest request, HttpServletResponse response) {
		RespBody respBody = new RespBody(StatusEnum.OK);
		try {
			response.setHeader("Access-Control-Allow-Origin", "*");
			Map<String, String> paramsMap = HttpUtils.parseRequestMap(request.getParameterMap());
			logger.info("getSign 请求参数param:{}", JSON.toJSONString(paramsMap));
			// 服务方平台公钥
			String sPublicKey = paramsMap.get("sPublicKey");
			// "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlLTXHBP9I1a9dkV54j+jb4lB0/VBB7lRi8Yk/FhaEMMNhY7AGLOVNWz3UovxElff0TpBgCuE8cOOtkYFPHyZ5Ng6PKTHgm5Uzt17iagRT+t0hA4igGe+SQH4QaIYDJy+IPSiW9qvHG/olWtbMm1j1l3cqP9Hn1x/WrrNz3gmRxv8OY9MSA76UIuLFCJenU5O6DblGqKOV2WYTCi1rNz2Yv0NskRuH0pRKkA+Kgvx4Yb/UwXcaFHKFhIdqM9bJ1KLKFx0nOYl7cncftQpXYDicc1shD2t3A/IRCkzoGHGpF8NOLMj86QUVZcfqYwmyUP8kjnInwD/MrwZkBbvDqg10QIDAQAB";
			// 商户方私钥
			String mPrivateKey = paramsMap.get("mPrivateKey");
			// "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKVxapmnfi5o5+gdBVoVPPt3N68mrKoscFNWy+S+g8ZV9egKtENLSKYDbtcH868EKggXuhLrYXSQI69ZzkYr4WqiFnBiaXgSCihsG+uE4eCdwXR4bnJTV1uc7ABR+alrg4VXUjI9wYPoMt2bCQWAb3qiZWJ+n31Jj4NKEsJN7U17AgMBAAECgYEAlJpE9/McQa907mZy0jFVb0HsQGTqnof/9Lt3tOTzFAlOMbi7pd5PSN64hmjcFw4UeuRk/G10QfDePLjbsOflNiJo2as8vKp6ijOHGpVJlBt4X6KX81KAgQpt0XetT1RzSnZVHbCPIf8wkeu2TOZYWGIVP3+uET/8oO/MUGSrQyECQQDYYn8LrYsrk1XKu4khA5MbMBaj1C7fbV6S8SLFzFbDV5c8qOjYjbbcQksmrIRueoZOglErwPzkU1aZjpwhrpnjAkEAw7tjPC2dHFvSdMTXr5TRgIfNjGnPEQ51wCZjprXdL8H2OIOA8vaBdkjHj8RTgji7idTz/a+A862bMTPFzD2xiQJBAMx0pN5/6BQQbuvUL0oGeqUh1wKNpoxuZwIo3lZPMVv6E2i5oOmdXb7Y1PGB6NAK7YSZFeFlxI5pQFZvrY8Vz0kCQGZGk3sNhqwb2uPDO8T2qWZVD/YQD/Aksyi9ppKlMzfSTVuIc6gBYPKy4vxfpRdFl9FU4k2sGGC8VXynRfMMRakCQEXa+SBnX3jd5TbIE1VzK8wH74rU8jZxjKikZ8nPev0OmOrDmKxpGq81czk9/RfbOSdOl0JyW5tr2qUkylA7BMU=";
			// 服务地址
			String tradeUrl = "http://apicashiertest.968309.net";
			Map<String, String> map = new HashMap<String, String>();
			map.put("merchantNo", paramsMap.get("merchantNo"));
			map.put("appId", paramsMap.get("appId"));
			map.put("signMode", "RSA");
			map.put("timeStamp", paramsMap.get("timeStamp"));
			map.put("nonceStr", paramsMap.get("nonceStr"));
			map.put("outOrderNo", paramsMap.get("outOrderNo"));
			map.put("tradeTotalFee", paramsMap.get("tradeTotalFee"));
			map.put("notifyUrl", paramsMap.get("notifyUrl"));
			map.put("returnUrl", paramsMap.get("returnUrl"));
			map.put("subject", paramsMap.get("subject"));
			map.put("attach", paramsMap.get("attach"));
			map.put("outTime", paramsMap.get("outTime"));
			String content = SignatureUtils.buildSortJson(map);
			String sign = SignatureUtils.rsa256Sign(content, mPrivateKey);
			map.put("sign", sign);
			String jsonContent = JSON.toJSONString(map);
			String xmlContent = XmlUtil.map2Xml(map);
			System.out.println("请求参数json：" + jsonContent);
			System.out.println("请求参数xml：" + xmlContent);
			Map<String, String> resultMap = new HashMap<String, String>();
			resultMap.put("paramJson", jsonContent);
			resultMap.put("paramXml", xmlContent);
			respBody.setResult(resultMap);
			respBody.setMessage("生成签名成功！");
		} catch (Exception ex) {
			respBody = new RespBody(StatusEnum.ERROR);
			respBody.setMessage("生成签名失败！");
			logger.error("获取签名和随机数");
		}
		logger.info("getSign result:{}", JSON.toJSONString(respBody));
		return respBody;
	}

	/* 
	 * 获取聚合支付二维码
	 *  */
	@ResponseBody
	@RequestMapping(value = "/payQRCode", method = RequestMethod.POST)
	public RespBody getQRCode(HttpServletRequest request, HttpServletResponse response, String paramJson, String paramXml) {
		RespBody respBody = new RespBody(StatusEnum.OK);
		try {
			response.setHeader("Access-Control-Allow-Origin", "*");
			logger.info("getQrCode 请求参数paramJson:{},paramXml:{}", paramJson, paramXml);
			ProductService productService = SpringContextHolder.getBean(ProductService.class);
			Request requestJson = new Request();
			requestJson.setMethodCode("payQRCode");
			requestJson.setResponseType("1");
			requestJson.setMethodParams(paramJson);
			Response responseJson = productService.openService(requestJson);

			Request requestXml = new Request();
			requestXml.setMethodCode("payQRCode");
			requestXml.setResponseType("0");
			requestXml.setMethodParams(paramXml);
			Response responseXml = productService.openService(requestXml);

			Map<String, String> resultMap = new HashMap<String, String>();
			if (null != responseJson.getResult()) {
				resultMap.put("responseJson", responseJson.getResult());
			} else {
				resultMap.put("responseJson", responseJson.getResultMessage());
			}
			if (null != responseXml.getResult()) {
				resultMap.put("responseXml", responseXml.getResult());
			} else {
				resultMap.put("responseXml", responseXml.getResultMessage());
			}
			respBody.setResult(resultMap);
			respBody.setMessage("生成二维码完成！");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		logger.info("getQrCode result:{}", JSON.toJSONString(respBody));
		return respBody;
	}

	public static String sha256Platform(Map<String, String> jObj, String secretKey) {
		try {
			StringBuffer stringSignTemp = new StringBuffer();
			stringSignTemp.append(jObj.get("platformId"));
			stringSignTemp.append( ( jObj.containsKey("platformSeqId") && !"".equals(jObj.get("platformSeqId")) ) ? jObj.get("platformSeqId") : "");
			stringSignTemp.append(jObj.get("platformTransDate"));
			stringSignTemp.append(jObj.get("platformTransTime"));
			stringSignTemp.append(secretKey);
			stringSignTemp.append(jObj.get("dataString"));
			System.out.println("stringSignTemp:" + stringSignTemp);
			MessageDigest sha1 = MessageDigest.getInstance("SHA-256");
			sha1.update(stringSignTemp.toString().getBytes("utf-8"));
			byte[] sha1Bytes = sha1.digest();

			String sign = bin2hex(sha1Bytes);

			return sign;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String bin2hex(byte[] bin) {
		char hex[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bin.length; i++) {
			char upper = hex[ ( bin[i] & 0xf0 ) >>> 4];
			char lower = hex[ ( bin[i] & 0x0f )];
			sb = sb.append(upper).append(lower);
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		Map<String, String> object = new HashMap<String, String>();
		object.put("platformId", "ygtest01");
		object.put("platformTransDate", "2018-01-30");
		object.put("platformTransTime", "14:15:38");
		object.put("transCode", "P2100031");
		object.put("platformSeqId", String.valueOf(System.currentTimeMillis()));

		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("idType", "01");//证件类型（缺省为：01）
		dataMap.put("idNo", "430223199001038016");//证件号码
		dataMap.put("name", "周鉴斌");//姓名
		dataMap.put("cardIssueDate", "2018-01-30");//发卡日期
		dataMap.put("accNo", "45234523472");//社保卡金融账号
		dataMap.put("prov", "广东省");//发卡省份
		dataMap.put("city", "广州市");//发卡地市
		dataMap.put("notifyUrl", "http://ysczf.968309.cn/trade/sunshine/pay/toPay");//通知地址
		//System.out.println(JSON.toJSONString(dataMap));
		object.put("dataString", JSON.toJSONString(dataMap));
		String secretKey = "8o51t6dlr0gp8sdfgk1gbzjqcc1pa3ga";
		String sign = sha256Platform(object, secretKey);
		object.put("sign", sign);
		System.out.println(JSON.toJSONString(object));
		String url = "https://61.28.113.182:2014/sisp/platformSendMsg.action";
		Map<String, Object> retMap = HttpClient.post(url, object, null);
		System.out.println("返回结果:" + JSON.toJSONString(retMap));

	}

}

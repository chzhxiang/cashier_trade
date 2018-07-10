/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2016年4月20日</p>
 *  <p> Created by 党参</p>
 *  </body>
 * </html>
 */
package com.sunshine.payment.utils.http;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.sunshine.payment.TradeConstant;

/**
 * @Project: sunshine_trade 
 * @Package: com.sunshine.trade.payrefund.utils.httpClient
 * @ClassName: HttpClient
 * @Description: <p>http模拟请求类</p>
 * @JDK version used: 
 * @Author: 党参
 * @Create Date: 2016年4月20日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class HttpClient {

	private static final Logger logger = LoggerFactory.getLogger(HttpClient.class);

	/**
	 * http请求返回状态值
	 */
	public final static String HTTP_STATUS = "status";

	/**
	 * http请求返回结果
	 */
	public final static String HTTP_DATA = "data";

	/**
	 * HTTP_POST
	 * @param url URL
	 * @param map 参数名:key,参数值:value
	 * @param reqStr 无参数名传递参数
	 * @param charset 编码
	 * @return String[]([0]=HttpStatus, [1]=返回值)
	 */
	public static String[] httpPost(String url, java.util.Map<String, String> map, String reqStr, String charset) {
		return httpPost(url, map, null, reqStr, charset);
	}

	/**
	 * HTTP_POST
	 * @param url URL
	 * @param map 参数名:key,参数值:value
	 * @param repHearderMap 请求头参数 (如：repHearderMap.put("Content-Type", "text/xml;charset=utf-8");)
	 * @param reqStr 无参数名传递参数
	 * @param charset 编码
	 * @return String[]([0]=HttpStatus, [1]=返回值)
	 */
	public static String[] httpPost(String url, java.util.Map<String, String> map, java.util.Map<String, String> repHearderMap, String reqStr,
			String charset) {
		String ret_array[] = new String[2];
		org.apache.commons.httpclient.HttpClient client = null;
		org.apache.commons.httpclient.methods.PostMethod method = null;

		try {
			client = new org.apache.commons.httpclient.HttpClient();
			method = new org.apache.commons.httpclient.methods.PostMethod(url);
			method.setRequestHeader("Connection", "close");

			if (repHearderMap != null) {
				java.util.Set<String> keys = repHearderMap.keySet();
				for (String k : keys) {
					method.setRequestHeader(k, repHearderMap.get(k));
				}
			}

			if (org.apache.commons.lang.StringUtils.isBlank(reqStr) && map != null) {
				java.util.Set<String> keys = map.keySet();
				for (String k : keys) {
					method.setParameter(k, map.get(k));
				}
			} else {
				method.setRequestEntity(new org.apache.commons.httpclient.methods.StringRequestEntity(reqStr, "text/html", charset));
			}
			client.getParams().setContentCharset(charset);
			//client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);

			int ret = client.executeMethod(method);
			ret_array[0] = ret + "";
			if (ret == org.apache.commons.httpclient.HttpStatus.SC_OK) {//HTTP REQUEST SUCCESS
				//GET RES
				java.io.InputStream resStream = method.getResponseBodyAsStream();
				java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(resStream, charset));
				StringBuffer resBuffer = new StringBuffer();
				String resTemp = "";
				while ( ( resTemp = br.readLine() ) != null) {
					resBuffer.append(resTemp);
				}
				br.close();
				resStream.close();

				ret_array[1] = resBuffer.toString(); //返回值
			} else {
				ret_array[1] = "HTTP:" + ret;
			}
		} catch (Exception e) {
			ret_array[0] = "-1";
			ret_array[1] = "http post request failed, exception:" + e;
		} finally {
			//System.out.println("finally!!!");
			if (method != null) {
				try {
					method.releaseConnection();
				} catch (Exception e) {
					System.out.println("-------> Release HTTP connection exception:" + e.toString());
				}
			}
			if (client != null) {
				try {
					//此项目的httpclient.jar不支持
					//((org.apache.commons.httpclient.SimpleHttpConnectionManager) client.getHttpConnectionManager()).shutdown();
				} catch (Exception e) {
					System.out.println("-------> Close HTTP connection exception:" + e.toString());
				}
				client = null;
			}
		}
		return ret_array;
	}

	/**
	 * HTTP GET
	 * @param url URL
	 * @param map 参数名:key,参数值:value
	 * @param charset 编码
	 * @return String[]([0]=HttpStatus, [1]=返回值)
	 */
	public static String[] httpGet(String url, java.util.Map<String, String> map, String charset, org.apache.commons.httpclient.HttpClient client) {
		String ret_array[] = new String[2];
		org.apache.commons.httpclient.methods.GetMethod method = null;
		boolean nclient = false;
		if (client == null) {
			client = new org.apache.commons.httpclient.HttpClient();
			nclient = true;
		}
		try {
			if (map != null) {
				url += "?";
				java.util.Set<String> keys = map.keySet();
				for (String k : keys) {
					url += k + "=" + java.net.URLEncoder.encode(map.get(k), charset) + "&";
				}
				url = url.substring(0, url.length() - 1);
			}
			System.out.println(url);
			method = new org.apache.commons.httpclient.methods.GetMethod(url);
			method.setRequestHeader("Connection", "close");

			client.getParams().setContentCharset(charset);
			//client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);

			int ret = client.executeMethod(method);
			ret_array[0] = ret + "";
			if (ret == org.apache.commons.httpclient.HttpStatus.SC_OK) {//HTTP REQUEST SUCCESS
				//GET RES
				java.io.InputStream resStream = method.getResponseBodyAsStream();
				java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(resStream, charset));
				StringBuffer resBuffer = new StringBuffer();
				String resTemp = "";
				while ( ( resTemp = br.readLine() ) != null) {
					resBuffer.append(resTemp);
				}
				br.close();
				resStream.close();

				ret_array[1] = resBuffer.toString(); //返回值
			} else {
				ret_array[1] = "HTTP:" + ret;
			}
		} catch (Exception e) {
			e.printStackTrace();
			ret_array[0] = "-1";
			ret_array[1] = "http get request failed, exception:" + e;
		} finally {
			//System.out.println("finally!!!");
			if (method != null) {
				try {
					method.releaseConnection();
				} catch (Exception e) {
					System.out.println("-------> Release HTTP connection exception:" + e.toString());
				}
			}
			if (nclient && client != null) {
				try {
					//此项目的httpclient.jar不支持
					//((org.apache.commons.httpclient.SimpleHttpConnectionManager) client.getHttpConnectionManager()).shutdown();
				} catch (Exception e) {
					System.out.println("-------> Close HTTP connection exception:" + e.toString());
				}
				client = null;
			}
		}
		return ret_array;
	}

	/**
	 * 
	 * @param url 请求url
	 * @param postMap 请求参数集合
	 * @param content 请求参数
	 * @return
	 */
	public static Map<String, Object> post(String url, Map<String, String> postMap, String content) {
		logger.info("请求地址:{}, 请求参数:{}", url, JSON.toJSONString(postMap));
		Map<String, Object> retMap = new HashMap<String, Object>();
		org.apache.commons.httpclient.HttpClient client = null;
		org.apache.commons.httpclient.methods.PostMethod method = null;

		try {
			client = new org.apache.commons.httpclient.HttpClient();
			method = new org.apache.commons.httpclient.methods.PostMethod(url);
			method.setRequestHeader("Connection", "close");

			if (StringUtils.isEmpty(content) && !postMap.isEmpty()) {
				java.util.Set<String> keys = postMap.keySet();
				for (String k : keys) {
					method.setParameter(k, postMap.get(k));
				}
			} else {
				method.setRequestEntity(new org.apache.commons.httpclient.methods.StringRequestEntity(content, "text/html",
						TradeConstant.INPUT_CHARSET));
			}
			logger.info("method.getParameters():{}", method.getParameters());
			client.getParams().setContentCharset(TradeConstant.INPUT_CHARSET);
			//client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);

			int status = client.executeMethod(method);
			retMap.put(HTTP_STATUS, status);
			if (status == org.apache.commons.httpclient.HttpStatus.SC_OK) {//HTTP REQUEST SUCCESS
				//GET RES
				java.io.InputStream resStream = method.getResponseBodyAsStream();
				java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(resStream, TradeConstant.INPUT_CHARSET));
				StringBuffer resBuffer = new StringBuffer();
				String resTemp = "";
				while ( ( resTemp = br.readLine() ) != null) {
					resBuffer.append(resTemp);
				}
				br.close();
				resStream.close();
				retMap.put(HTTP_DATA, resBuffer.toString());
			} else {
				retMap.put(HTTP_DATA, "HTTP:".concat(String.valueOf(status)));
			}
		} catch (Exception e) {
			retMap.put(HTTP_STATUS, -1);
			retMap.put(HTTP_DATA, "http post request failed, exception".concat(e.getMessage()));
		} finally {
			//System.out.println("finally!!!");
			if (method != null) {
				try {
					method.releaseConnection();
				} catch (Exception e) {
					logger.info("Release HTTP connection exception:" + e.toString());
				}
			}
			if (client != null) {
				try {
					//此项目的httpclient.jar不支持
					//((org.apache.commons.httpclient.SimpleHttpConnectionManager) client.getHttpConnectionManager()).shutdown();
				} catch (Exception e) {
					logger.info("Close HTTP connection exception:" + e.toString());
				}
				client = null;
			}
		}
		return retMap;

	}
}

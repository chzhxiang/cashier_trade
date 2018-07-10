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
package com.sunshine.payment.wechat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.SSLContext;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sunshine.common.GlobalConstant;
import com.sunshine.common.utils.sign.AES;
import com.sunshine.framework.common.PKGenerator;
import com.sunshine.framework.utils.DateUtils;
import com.sunshine.mobileapp.order.entity.TradeOrder;
import com.sunshine.payment.TradeConstant;
import com.sunshine.payment.TradeUtils;
import com.sunshine.payment.utils.http.HttpClient;
import com.sunshine.payment.utils.xml.XmlUtils;
import com.sunshine.platform.TradeChannelConstants;
import com.sunshine.platform.channel.vo.WechatVo;
import com.sunshine.restful.utils.RestUtils;

/**
 * 
 * @Project: cashier_trade
 * @Package: com.sunshine.payment.wechat.utils
 * @ClassName: WechatUtils
 * @Description:
 *               <p>
 *               微信支付工具类
 *               </p>
 * @JDK version used:
 * @Author: 党参
 * @Create Date: 2017年9月22日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class WechatUtils {

	private static Logger logger = LoggerFactory.getLogger(WechatUtils.class);

	/**
	 * 微信 统一支付接口
	 */
	public final static String UNIFIEDORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

	/**
	 * 微信 退款申请地址
	 */
	public final static String REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";

	/**
	 * 微信 退款查询接口
	 */
	public final static String REFUNDQUERY_URL = "https://api.mch.weixin.qq.com/pay/refundquery";

	/**
	 * 微信 支付订单查询
	 */
	public final static String ORDERQUERY_URL = "https://api.mch.weixin.qq.com/pay/orderquery";

	/**
	 * 微信 订单关闭
	 */
	public final static String ORDERCLOSE_URL = "https://api.mch.weixin.qq.com/pay/closeorder";

	/**
	 * 微信 现金红包接口地址
	 */
	public final static String SENDREDPACK_URL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack";

	/**
	 * 微信现金红包查询接口地址
	 */
	public final static String GETHBINFO_URL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/gethbinfo";

	/**
	 * 微信交易类型：JSAPI 即网页支付
	 */
	public static final String WECHAT_TRADE_MODE_JSAPI = "JSAPI";

	/**
	 * 微信交易类型：NATIVE 即原生支付
	 */
	public static final String WECHAT_TRADE_MODE_NATIVE = "NATIVE";

	/**
	 * 微信交易类型：MICROPAY
	 */
	public static final String WECHAT_TRADE_MODE_MICROPAY = "MICROPAY";

	/**
	 * 微信交易类型：APP
	 */
	public static final String WECHAT_TRADE_MODE_APP = "APP";

	/**
	 * 微信交易类型：MWEB
	 */
	public static final String WECHAT_TRADE_MODE_MWEB = "MWEB";

	/**
	 * 微信签名算法
	 */
	public static final String WECHAT_SIGN_TYPE = "MD5";

	/**
	 * 交易结果成功
	 */
	public static final String TRADE_IS_SUCCESS = "SUCCESS";

	/**
	 * 微信支付回调地址
	 */
	public static final String WECHAT_NOTIFY_URL = "/notifyWechat/callback/";

	/**
	 * 微信签名字符串标识
	 */
	public static final String WECHAT_SIGN_MARK = "sign";

	/**
	 * 微信http请求支付下单
	 */
	public static final String WECHAT_HTTP_PAY = "httpPay";

	/**
	 * 微信http请求返回退费
	 */
	public static final String WECHAT_HTTP_REFUND = "httpRefund";

	/**
	 * 微信http请求关闭订单
	 */
	public static final String WECHAT_HTTP_CLOSE = "httpClose";

	/**
	 * 微信http请求返回查询
	 */
	public static final String WECHAT_HTTP_QUERY = "httpQuery";

	/**
	 * 微信http请求返回状态
	 */
	public static final String WECHAT_HTTP_STATUS = "status";

	/**
	 * 微信http请求返回结果
	 */
	public static final String WECHAT_HTTP_XML = "xml";

	/**
	 * 查询支付订单
	 */
	public static final String ORDER_PAY_QUERY = "payQuery";

	/**
	 * 查询退费订单
	 */
	public static final String ORDER_REFUND_QUERY = "refundQuery";

	/**
	 * 微信退费订单集合缓存前缀
	 */
	public static final String CACHE_WECHAT_REFUND_ORDER_SET_KEY_PREFIX = "cache.wechat.refund.order.set";

	/**
	 * 微信退费订单集合缓存有效时常
	 */
	public static final int WECHAT_REFUND_ORDER_SET_EXPIRE_IN = 5 * 60;

	/**
	 * 生成支付签名
	 * @param params
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String getSign(Map<String, String> params, String key) throws Exception {
		StringBuilder sign = new StringBuilder();
		String[] keys = new String[params.size()];
		params.keySet().toArray(keys);
		Arrays.sort(keys);
		for (String k : keys) {
			String value = params.get(k);
			if (!StringUtils.isEmpty(value)) {
				sign.append(k).append(GlobalConstant.STRING_ASSIGN_CHAR).append(params.get(k)).append(GlobalConstant.STRING_AND_CHAR);
			}
		}
		sign.append("key").append(GlobalConstant.STRING_ASSIGN_CHAR).append(key);
		logger.info("签名字符串:{}", sign);
		return DigestUtils.md5Hex(sign.toString()).toUpperCase();
	}

	/**
	 * 换取openId
	 * 
	 * @param appId
	 * @param appSecret
	 * @param code
	 * @return
	 */
	public static String getOpenId(String appId, String appSecret, String code) {
		StringBuffer buffer = new StringBuffer("https://api.weixin.qq.com/sns/oauth2/access_token?");
		buffer.append("appid=").append(appId).append("&secret=").append(appSecret).append("&code=").append(code)
				.append("&grant_type=authorization_code");
		String[] rs = HttpClient.httpGet(buffer.toString(), null, "utf-8", new org.apache.commons.httpclient.HttpClient());
		logger.info("换取openId 状态：{} , 结果 : {}", rs[0], rs[1]);
		if (rs != null && rs[0].equals("200")) {
			JSONObject jo = JSONObject.parseObject(rs[1]);
			return jo.getString("openid");
		} else {
			return null;
		}
	}

	/**
	 * 构建授权请求地址
	 * 
	 * @param appId
	 * @param redirectUrl
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getOAuth2(String appId, String redirectUrl) throws UnsupportedEncodingException {
		StringBuffer buffer = new StringBuffer("https://open.weixin.qq.com/connect/oauth2/authorize?response_type=code");
		buffer.append("&scope=snsapi_base&state=getOpenId&appid=").append(appId).append("&redirect_uri=")
				.append(URLEncoder.encode(redirectUrl, "utf-8")).append("#wechat_redirect");
		return buffer.toString();
	}

	/**
	 * 微信统一下单请求
	 * 
	 * @return
	 */
	public static Map<String, Object> wechatUnifiedorder(TradeOrder order, WechatVo wechatVo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {

			Map<String, String> params = WechatUtils.wechatPayGenertor(order, wechatVo);
			String reqStr = bReqXML(params);
			logger.info("订单号:{},预支付XML(reqStr)：{}", new Object[] { order.getTradeNo(), reqStr });
			// 调用微信统一支付接口
			Map<String, Object> retMap = HttpClient.post(UNIFIEDORDER_URL, null, reqStr);
			logger.info("订单号:{},微信统一支付接口返回:{}", new Object[] { order.getTradeNo(), JSON.toJSONString(retMap) });
			int status = (int) retMap.get(HttpClient.HTTP_STATUS);
			String xmlStr = (String) retMap.get(HttpClient.HTTP_DATA);
			Map<String, String> checkParams = XmlUtils.xmlMap(xmlStr);
			logger.info("请求结果:{}", JSON.toJSONString(checkParams));
			if (TradeConstant.HTTP_IS_SUCCESS == status && TRADE_IS_SUCCESS.equals(checkParams.get("return_code"))) {
				boolean checkSign = checkSign(checkParams, wechatVo.getPaySecret());
				if (TRADE_IS_SUCCESS.equals(checkParams.get("return_code")) && TRADE_IS_SUCCESS.equals(checkParams.get("result_code")) && checkSign) {
					String timeStamp = String.valueOf(new Date().getTime() / 1000);
					checkParams.put("timeStamp", timeStamp);
					// 支付签名生成
					String paySign = "";
					String tradeType = checkParams.get("trade_type");
					if (WECHAT_TRADE_MODE_APP.equals(tradeType)) {
						paySign = wechatPaySignAPPGenertor(checkParams, wechatVo);
					} else if (WECHAT_TRADE_MODE_JSAPI.equals(tradeType) || WECHAT_TRADE_MODE_MWEB.equals(tradeType)) {
						paySign = wechatPaySignJSAPIGenertor(checkParams, wechatVo);
					}
					checkParams.put(WECHAT_SIGN_MARK, paySign);
					logger.info("订单号:{},支付签名(paySign):{},prepay_id:{}", new Object[] { order.getTradeNo(), paySign, checkParams.get("prepay_id") });

					//如果是h5支付，则添加跳转地址并且转码
					if (!StringUtils.isEmpty(order.getReturnUrl()) && TradeChannelConstants.TRADE_CHANNEL_WECHAT_H5.equals(order.getChannelCode())) {
						String redirectUrl = URLEncoder.encode(TradeConstant.getSynchroCallback(order.getId()), TradeConstant.INPUT_CHARSET);
						String mwebUrl = checkParams.get("mweb_url").concat("&redirect_url=").concat(redirectUrl);
						checkParams.put("mweb_url", mwebUrl);
					}
					resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
					resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, true);
					resultMap.put(GlobalConstant.TRADE_SUCCESS_DATA, checkParams);
				} else {
					resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
					resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
					resultMap.put(GlobalConstant.TRADE_FAIL_MSG, checkParams.get("err_code_des"));
					logger.error("订单号:{},接口调用失败，返回结果:{}", new Object[] { order.getTradeNo(), resultMap.get(GlobalConstant.TRADE_FAIL_MSG) });
				}
			} else {
				resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
				resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
				resultMap.put(GlobalConstant.TRADE_FAIL_MSG, checkParams.get("return_msg"));
			}
		} catch (Exception e) {
			resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, true);
			resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
			resultMap.put(GlobalConstant.TRADE_FAIL_MSG, e.getMessage());
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 微信退费
	 * 
	 * @param order
	 * @param wechatVo
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> wechatRefund(TradeOrder order, WechatVo wechatVo) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		// 退费证书绝对路径
		wechatVo.setCertificatePath(GlobalConstant.FILE_PATH.concat(wechatVo.getCertificatePath()));
		try {
			logger.info("订单退费请求开始，订单信息：{}.", JSON.toJSON(order));
			Map<String, String> paramsMap = WechatUtils.wechatRefundGenertor(order, wechatVo);
			logger.info("退费接口请求参数。{}.", JSON.toJSON(paramsMap));
			Map<String, Object> resultMap = WechatUtils.cancelPay(paramsMap, wechatVo.getPaySecret(), wechatVo.getCertificatePath());
			logger.info("退费申请完成。商户订单号:{}, 结果:{}.", order.getOutTradeNo(), JSON.toJSON(resultMap));
			Boolean status = (Boolean) resultMap.get("status");
			String result = (String) resultMap.get("result");
			if (status) {
				Document doc = DocumentHelper.parseText(result);
				Element root = doc.getRootElement();
				String returnCode = root.valueOf("return_code");
				if (WechatUtils.TRADE_IS_SUCCESS.equalsIgnoreCase(returnCode)) {
					String resultCode = root.valueOf("result_code");
					if (WechatUtils.TRADE_IS_SUCCESS.equalsIgnoreCase(resultCode)) {
						returnMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
						returnMap.put(GlobalConstant.TRADE_IS_SUCCESS, true);
						returnMap.put(GlobalConstant.TRADE_SUCCESS_AGTREFUNDNO, root.valueOf("refund_id"));
						logger.info("微信退费申请成功，商户订单号：{}", order.getOutTradeNo());
					} else {
						returnMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
						returnMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
						returnMap.put(GlobalConstant.TRADE_FAIL_MSG, root.valueOf("err_code_des"));
						logger.info("微信退费申请失败，商户订单号：{}，返回结果：{}", order.getOutTradeNo(), root.valueOf("err_code_des"));
					}
				} else {
					returnMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
					returnMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
					returnMap.put(GlobalConstant.TRADE_FAIL_MSG, root.valueOf("return_msg"));
					logger.info("微信退费申请失败，商户订单号：{}，返回结果：{}", order.getOutTradeNo(), root.valueOf("return_msg"));
				}
			} else {
				returnMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
				returnMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
				returnMap.put(GlobalConstant.TRADE_FAIL_MSG, resultMap.get("result").toString());
				logger.info("微信退费申请失败，商户订单号：{}，返回结果：{}", order.getOutTradeNo(), resultMap.get("result").toString());
			}
		} catch (Exception e) {
			returnMap.put(GlobalConstant.TRADE_IS_EXCEPTION, true);
			returnMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
			returnMap.put(GlobalConstant.TRADE_FAIL_MSG, e.getMessage());
			logger.info("微信退费申请异常，商户订单号：{}", order.getOutTradeNo());
			e.printStackTrace();
			return returnMap;
		}
		return returnMap;
	}

	/**
	 * 退款接口 调用 微信
	 * 
	 * @param order
	 * @param wechatVo
	 * @return
	 * @throws KeyStoreException
	 */
	public static Map<String, Object> cancelPay(Map<String, String> map, String key, String realPath) throws Exception {
		logger.info("-------------------------------退款 start-------------------------------------");
		logger.info("密钥文件路径:{}", realPath);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "";
		String charset = "utf-8";
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		String mchId = map.get("mch_id").toString();
		// String filePath = realPath +
		// SystemConfig.getStringValue("refund_file_path") + "/" +
		// map.get("appid").toString() + ".p12";
		FileInputStream instream = null;
		try {
			instream = new FileInputStream(new File(realPath));
			keyStore.load(instream, mchId.toCharArray());
		} catch (Exception e) {
			resultMap.put("status", false);
			resultMap.put("result", "密钥文件错误，请联系客服!");
			logger.error("密钥文件加载错误。", e);
		} finally {
			if (null != instream) {
				instream.close();
			}
		}
		CloseableHttpClient httpclient = null;
		try {
			SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, mchId.toCharArray()).build();
			SSLConnectionSocketFactory sslsf =
					new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,
							SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
			httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			logger.info("-------------------------------加载密钥 end-------------------------------------");

			HttpPost httppost = new HttpPost(WechatUtils.REFUND_URL);
			String sign = WechatUtils.getSign(map, key);
			map.put("sign", sign);
			logger.info("退款签名:{}", sign);
			String reqXML = WechatUtils.bReqXML(map);
			logger.info("退款XML:{}", reqXML);
			httppost.setEntity(new StringEntity(reqXML, charset));
			CloseableHttpResponse response = httpclient.execute(httppost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent(), charset));
					String tempText;
					while ( ( tempText = bufferedReader.readLine() ) != null) {
						result += tempText;
					}
				}
				EntityUtils.consume(entity);
			} catch (Exception e) {
				resultMap.put("status", false);
				resultMap.put("result", "微信返回读取错误");
				logger.error("微信返回读取错误", e);
			} finally {
				response.close();
			}
		} catch (Exception e) {
			resultMap.put("status", false);
			resultMap.put("result", e.getMessage());
			logger.error("微信错误", e);

		} finally {
			if (null != httpclient) {
				httpclient.close();
			}
		}
		resultMap.put("status", true);
		resultMap.put("result", result);
		logger.info("-------------------------------申请退款 end-------------------------------------");
		return resultMap;
	}

	/**
	 * 微信订单关闭
	 * @param order
	 * @param wechatVo
	 * @return
	 */
	public static Map<String, Object> wechatClose(TradeOrder order, WechatVo wechatVo) {
		logger.info("微信关闭订单请求开始！");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Map<String, String> dataMap = wechatHttp(order, wechatVo, WECHAT_HTTP_CLOSE, ORDERCLOSE_URL, "");
			Map<String, String> checkParams = XmlUtils.xmlMap(dataMap.get(WECHAT_HTTP_XML));
			resultMap.put(GlobalConstant.TRADE_SUCCESS_DATA, JSON.toJSONString(checkParams));
			if (GlobalConstant.HTTP_IS_SUCCESS.equals(dataMap.get(WECHAT_HTTP_STATUS))) {
				boolean checkSign = checkSign(checkParams, wechatVo.getPaySecret());
				String returnCode = checkParams.get("return_code");
				String resultCode = checkParams.get("result_code");
				if (returnCode.equals(GlobalConstant.TRADE_SUCCESS) && resultCode.equals(GlobalConstant.TRADE_SUCCESS) && checkSign) {//成功关闭
					resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
					resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, true);
					resultMap.put(GlobalConstant.TRADE_SUCCESS_DATA, resultCode);
					logger.error("订单号:{}, 微信订单关闭成功！", new Object[] { order.getTradeNo() });
				} else {
					resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
					resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
					resultMap.put(GlobalConstant.TRADE_FAIL_MSG, checkParams.get("err_code_des"));
					logger.error("订单号:{}, 微信订单关闭失败,原因：{}", new Object[] { order.getTradeNo(), checkParams.get("err_code_des") });
				}
			} else {
				resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
				resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
				resultMap.put(GlobalConstant.TRADE_FAIL_MSG, checkParams.get("err_code_des"));
				logger.error("订单号:{}, 微信订单查询接口失败,原因：{}", new Object[] { order.getTradeNo(), "http请求失败！" });
			}
		} catch (Exception e) {
			resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, true);
			resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
			resultMap.put(GlobalConstant.TRADE_FAIL_MSG, e.getMessage());
			logger.error("订单号:{}, 微信订单关闭失败,原因：{}", new Object[] { order.getTradeNo(), e.getMessage() });
		}
		return resultMap;
	}

	/**
	 * 微信支付订单查询
	 * 
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年9月25日上午11:28:32
	 */
	public static Map<String, Object> orderQueryWechatPay(TradeOrder order, WechatVo wechatVo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Map<String, String> dataMap = wechatHttp(order, wechatVo, WECHAT_HTTP_QUERY, ORDERQUERY_URL, ORDER_PAY_QUERY);
			Map<String, String> checkParams = XmlUtils.xmlMap(dataMap.get(WECHAT_HTTP_XML));

			resultMap.put(GlobalConstant.TRADE_SUCCESS_DATA, JSON.toJSONString(checkParams));

			if (GlobalConstant.HTTP_IS_SUCCESS.equals(dataMap.get(WECHAT_HTTP_STATUS))) {
				boolean checkSign = checkSign(checkParams, wechatVo.getPaySecret());
				String returnCode = checkParams.get("return_code");
				String resultCode = checkParams.get("result_code");
				if (returnCode.equals(GlobalConstant.TRADE_SUCCESS) && resultCode.equals(GlobalConstant.TRADE_SUCCESS) && checkSign) {
					// 则表示该查询状态可以改变
					String tradeState = checkParams.get("trade_state");
					String agtOrderNo = checkParams.get("transaction_id");
					String tradeTime = checkParams.get("time_end");
					resultMap.put(GlobalConstant.TRADE_SUCCESS_AGTPAYNO, agtOrderNo);// 写入微信支付订单号
					/**
					 * 3:SUCCESS—支付成功 4:REFUND—转入退款 5:NOTPAY—未支付 6:CLOSED—已关闭
					 * 7:REVOKED—已撤销 8:USERPAYING--用户支付中
					 * 9:NOPAY--未支付(输入密码或确认支付超时) 10:PAYERROR--支付失败(其他原因，如银行返回失败)
					 */
					if ("success".equalsIgnoreCase(tradeState)) {

						resultMap.put(GlobalConstant.TRADE_ORDER_STATE, TradeConstant.TRADE_STATUS_PAYMENT);
						resultMap.put(GlobalConstant.TRADE_DATE, GlobalConstant.formatYYYYMMDDHHMMSS(GlobalConstant.parseYYYYMMDDHHMMSS(tradeTime)));
						logger.info("订单号:{},微信订单查询接口结果：已支付 ,agtOrderNum:{}", new Object[] { order.getTradeNo(), agtOrderNo });

					} else if ("REFUND".equalsIgnoreCase(tradeState)) {
						resultMap.put(GlobalConstant.TRADE_ORDER_STATE, TradeConstant.TRADE_STATUS_REFUNDING);
						logger.info("订单号:{},微信订单查询接口结果：退费中,agtOrderNum:{}", new Object[] { order.getTradeNo(), agtOrderNo });

					} else if ("NOTPAY".equalsIgnoreCase(tradeState)) {
						resultMap.put(GlobalConstant.TRADE_ORDER_STATE, TradeConstant.TRADE_STATUS_NO_PAYMENT);
						logger.info("订单号:{},微信订单查询接口结果：未支付", order.getTradeNo());

					} else if ("CLOSED".equalsIgnoreCase(tradeState) || "REVOKED".equalsIgnoreCase(tradeState)) {
						resultMap.put(GlobalConstant.TRADE_ORDER_STATE, TradeConstant.TRADE_STATUS_CLOSE);
						logger.info("订单号:{},微信订单查询接口结果：已经关闭,agtOrderNum:{}", new Object[] { order.getTradeNo(), agtOrderNo });

					} else if ("USERPAYING".equalsIgnoreCase(tradeState)) {
						resultMap.put(GlobalConstant.TRADE_ORDER_STATE, TradeConstant.TRADE_STATUS_PAYMENTING);
						logger.info("订单号:{},微信订单查询接口结果：支付中", order.getTradeNo());

					} else if ("NOPAY".equalsIgnoreCase(tradeState) || "PAYERROR".equalsIgnoreCase(tradeState)) {
						resultMap.put(GlobalConstant.TRADE_ORDER_STATE, TradeConstant.TRADE_STATUS_NO_PAYMENT);
						logger.info("订单号:{},未支付(输入密码或确认支付超时)", order.getTradeNo());

					}
					resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
					resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, true);
					resultMap.put(GlobalConstant.TRADE_FAIL_MSG, checkParams.get("err_code_des"));
				} else {
					if ("ORDERNOTEXIST".equals(checkParams.get("err_code"))) {
						resultMap.put(GlobalConstant.TRADE_ORDER_STATE, TradeConstant.TRADE_STATUS_NOT_EXIST);
						resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
						resultMap.put(GlobalConstant.TRADE_FAIL_MSG, checkParams.get("err_code_des"));
					} else {
						resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
						resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
						resultMap.put(GlobalConstant.TRADE_FAIL_MSG, checkParams.get("err_code_des"));
					}
					logger.error("订单号:{}, 微信订单查询接口失败,原因：{}", new Object[] { order.getTradeNo(), checkParams.get("err_code_des") });
				}
			} else {
				resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
				resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
				resultMap.put(GlobalConstant.TRADE_FAIL_MSG, checkParams.get("err_code_des"));
				logger.error("订单号:{}, 微信订单查询接口失败,原因：{}", new Object[] { order.getTradeNo(), checkParams.get("err_code_des") });
			}
		} catch (Exception e) {
			resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, true);
			resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
			resultMap.put(GlobalConstant.TRADE_FAIL_MSG, e.getMessage());
			logger.error("订单号:{}, 微信订单查询接口失败,原因：{}", new Object[] { order.getTradeNo(), e.getMessage() });
		}
		return resultMap;
	}

	/**
	 * 微信退费订单查询
	 * 
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年9月27日下午5:28:24
	 */
	public static Map<String, Object> orderQueryWechatRefund(TradeOrder order, WechatVo wechatVo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Map<String, String> dataMap = wechatHttp(order, wechatVo, WECHAT_HTTP_QUERY, REFUNDQUERY_URL, ORDER_REFUND_QUERY);

			String xml = dataMap.get(WECHAT_HTTP_XML);
			Map<String, String> checkParams = XmlUtils.xmlMap(xml);

			resultMap.put(GlobalConstant.TRADE_SUCCESS_DATA, JSON.toJSONString(checkParams));

			if (GlobalConstant.HTTP_IS_SUCCESS.equals(dataMap.get(WECHAT_HTTP_STATUS))) {

				boolean checkSign = checkSign(checkParams, wechatVo.getPaySecret());
				String returnCode = checkParams.get("return_code");
				String resultCode = checkParams.get("result_code");

				if (returnCode.equals(GlobalConstant.TRADE_SUCCESS) && resultCode.equals(GlobalConstant.TRADE_SUCCESS) && checkSign) {
					Integer refundCount =
							StringUtils.isEmpty(checkParams.get("refund_count")) ? 0 : Integer.parseInt(checkParams.get("refund_count"));
					/**
					 * SUCCES—退款成功 FAIL—退款失败 PROCESSING—退款处理中
					 * NOTSURE—未确定，需要商户原退款单号重新发起 CHANGE—转入代发，退款到银行发现用户的卡作废或者
					 * 冻结了，导致原路退款银行 卡失败，资金回流到商户的 现金帐号，需要商户人工干 预，通过线下或者财付通转
					 * 账的方式进行退款。
					 */
					String tradeState = checkParams.get("trade_state");
					resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
					resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, true);
					if ("SUCCESS".equalsIgnoreCase(tradeState)) {
						if (refundCount > 0) {
							resultMap.put(GlobalConstant.TRADE_ORDER_STATE, TradeConstant.TRADE_STATUS_REFUND);
							//resultMap.put(GlobalConstant.TRADE_SUCCESS_AGTPAYNO, checkParams.get("transaction_id"));// 写入微信支付订单号
							resultMap.put(GlobalConstant.TRADE_SUCCESS_AGTREFUNDNO, checkParams.get("refund_id"));// 写入微信支付订单号
							resultMap.put("tradeTime", DateUtils.formatDate(checkParams.get("time_end"), "yyyyMMddHHmmss", "yyyy-MM-dd HH:mm:ss"));// 写入微信支付时间
						} else {
							resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
							logger.info("订单号：{}, 微信订单查询接口失败,原因退款记录为：{}", order.getTradeNo(), refundCount);
						}
					} else if ("FAIL".equalsIgnoreCase(tradeState)) {
						resultMap.put(GlobalConstant.TRADE_ORDER_STATE, TradeConstant.TRADE_STATUS_PAYMENT);

					} else if ("PROCESSING".equalsIgnoreCase(tradeState)) {
						resultMap.put(GlobalConstant.TRADE_ORDER_STATE, TradeConstant.TRADE_STATUS_REFUNDING);

					} else if ("NOTSURE".equalsIgnoreCase(tradeState) || "CHANGE".equalsIgnoreCase(tradeState)) {
						resultMap.put(GlobalConstant.TRADE_ORDER_STATE, TradeConstant.TRADE_STATUS_CLOSE);
						resultMap.put(GlobalConstant.TRADE_FAIL_MSG, "NOTSURE or CHANGE 需要人工操作");

					}
				} else {
					resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
					resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
					logger.info("订单号:{} 微信订单查询接口失败,原因:{}", order.getTradeNo(), checkParams.get("return_msg"));
				}
			} else {
				resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
				resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
				logger.error("订单号:{} 微信订单查询接口失败,原因：{}", new Object[] { order.getTradeNo(), xml });
			}
		} catch (Exception e) {
			resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, true);
			resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
			e.printStackTrace();
			logger.error("微信退费查询异常,订单号:{},exception:{}", order.getTradeNo(), e.getMessage());
		}
		return resultMap;
	}

	/**
	 * 验证微信返回支付信息
	 * 
	 * @param xml
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static boolean checkSign(Map<String, String> checkParams, String key) throws Exception {
		String sign = checkParams.remove(WECHAT_SIGN_MARK);
		logger.debug("key:{} , 验签参数：{}", key, JSON.toJSONString(checkParams));
		String checkSign = getSign(checkParams, key);
		logger.debug("sign:{} , checkSign：{}", sign, checkSign);
		return sign.equalsIgnoreCase(checkSign);
	}

	/**
	 * 生成预支付请求XML
	 * 
	 * @param params
	 * @return
	 */
	public static String bReqXML(Map<String, String> params) {
		Element xml = DocumentHelper.createElement("xml");
		for (String key : params.keySet()) {
			xml.addElement(key).addCDATA(params.get(key));
		}
		return xml.asXML();
	}

	/**
	 * 生成支付签名 JSAPI
	 * @param pay
	 * @return
	 * @throws Exception 
	 */
	public static String wechatPaySignJSAPIGenertor(Map<String, String> signParams, WechatVo wechatVo) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		if (!StringUtils.isEmpty(wechatVo.getSubMchId())) {
			params.put("appId", wechatVo.getParentAppId());
		} else {
			params.put("appId", wechatVo.getAppId());
		}
		params.put("timeStamp", signParams.get("timeStamp"));
		params.put("nonceStr", signParams.get("nonce_str"));
		params.put("package", "prepay_id=" + signParams.get("prepay_id"));
		params.put("signType", WECHAT_SIGN_TYPE);
		logger.info("生成支付签名 JSAPI,prepay_id:{},签名参数:{}", new Object[] { signParams.get("prepay_id"), JSON.toJSONString(params) });
		return getSign(params, wechatVo.getPaySecret());
	}

	/**
	 * 生成支付签名 APP
	 * @param pay
	 * @return
	 * @throws Exception 
	 */
	public static String wechatPaySignAPPGenertor(Map<String, String> signParams, WechatVo wechatVo) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		if (!StringUtils.isEmpty(wechatVo.getSubMchId())) {
			params.put("appid", wechatVo.getParentAppId());
		} else {
			params.put("appid", wechatVo.getAppId());
		}
		params.put("timestamp", signParams.get("timeStamp"));
		params.put("noncestr", signParams.get("nonce_str"));
		params.put("package", "Sign=WXPay");
		params.put("prepayid", signParams.get("prepay_id"));
		params.put("partnerid", wechatVo.getMchId());
		logger.info("生成支付签名 APP,prepay_id:{},签名参数:{}", new Object[] { signParams.get("prepay_id"), JSON.toJSONString(params) });
		return getSign(params, wechatVo.getPaySecret());
	}

	/**
	 * 微信支付参数组装
	 * 
	 * @param pay
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> wechatPayGenertor(TradeOrder order, WechatVo wechatVo) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("appid", wechatVo.getAppId());
		params.put("mch_id", wechatVo.getMchId()); // 微信支付分配的商户号
		params.put("nonce_str", PKGenerator.generateId()); // 随机字符串，不长于32位
		params.put("body", order.getSubject());// 商品描述
		params.put("out_trade_no", order.getTradeNo()); // 商户系统内部的订单号,32个字符内、可包含字母,确保在商户系统唯一
		params.put("total_fee", String.valueOf(order.getTotalFee()));
		params.put("notify_url", RestUtils.getTradeDomainUrl().concat(WECHAT_NOTIFY_URL));// 异步接收微信支付成功通知URL
		params.put("attach", JSON.toJSONString(TradeUtils.buildAttach(order))); // 附加数据，原样返回
		params.put("spbill_create_ip", order.getSpbillCreateIp());
		if (TradeChannelConstants.TRADE_CHANNEL_WECHAT_JSAPI.equals(order.getChannelCode())) {
			params.put("trade_type", WECHAT_TRADE_MODE_JSAPI);
			params.put("openid", order.getOpenId());
		} else if (TradeChannelConstants.TRADE_CHANNEL_WECHAT_H5.equals(order.getChannelCode())) {
			params.put("trade_type", WECHAT_TRADE_MODE_MWEB);
		} else if (TradeChannelConstants.TRADE_CHANNEL_WECHAT_APP.equals(order.getChannelCode())) {
			params.put("trade_type", WECHAT_TRADE_MODE_APP);
		}
		logger.info("订单号:{},预支付签名参数：{}", new Object[] { order.getTradeNo(), JSON.toJSONString(params) });
		String sign = getSign(params, wechatVo.getPaySecret());
		logger.info("订单号:{},预支付签名(sign)：{}", new Object[] { order.getTradeNo(), sign });
		params.put("sign", sign);
		return params;
	}

	/**
	 * 组装退费请求map 微信
	 * 
	 * @param refund
	 * @return
	 */
	public static Map<String, String> wechatRefundGenertor(TradeOrder order, WechatVo wechatVo) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("mch_id", wechatVo.getMchId());
		params.put("appid", wechatVo.getAppId());
		if (!StringUtils.isEmpty(wechatVo.getSubMchId())) {
			// 如果子商户号不为空，则表示为服务商父子商户退款
			params.put("sub_mch_id", wechatVo.getSubMchId());
		}
		params.put("nonce_str", PKGenerator.generateId());// 随机字符串，不长于32位
		params.put("transaction_id", order.getAgtTradeNo());
		params.put("out_trade_no", order.getTradeNo());
		params.put("out_refund_no", order.getRefundNo());
		params.put("total_fee", order.getTotalFee().toString());
		params.put("refund_fee", order.getRefundFee().toString());
		params.put("op_user_id", wechatVo.getMchId());
		return params;
	}

	/**
	 * 微信HTTP请求
	 * 
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年9月25日上午11:30:14
	 */
	public static Map<String, String> wechatHttp(TradeOrder order, WechatVo wechatVo, String opType, String reuqestUrl, String orderType) {
		String key = wechatVo.getPaySecret();
		Map<String, String> params = new HashMap<String, String>();
		try {
			if (WECHAT_HTTP_PAY.equalsIgnoreCase(opType)) {
				params = wechatPayGenertor(order, wechatVo);
			} else if (WECHAT_HTTP_REFUND.equalsIgnoreCase(opType)) {

			} else if (WECHAT_HTTP_CLOSE.equalsIgnoreCase(opType)) {
				params = wechatCloseGenertor(order, wechatVo);
			} else {
				params = wechatQueryGenertor(order, wechatVo, orderType);
			}
			logger.info("订单号:{}, 请求参数:{}", new Object[] { order.getTradeNo(), JSON.toJSONString(params) });
			String sign = getSign(params, key);
			logger.info("订单号:{}, 请求类型:{} ,微信HTTP请求签名(sign)：{} ", new Object[] { order.getTradeNo(), opType, sign });
			params.put("sign", sign);
			String reqStr = bReqXML(params);
			logger.info("订单号:{}, 请求类型:{} ,微信HTTP请求XML(reqStr)：{}", new Object[] { order.getTradeNo(), opType, reqStr });
			String result[] = HttpClient.httpPost(reuqestUrl, null, reqStr, TradeConstant.INPUT_CHARSET);
			params.clear();
			params.put(WECHAT_HTTP_STATUS, result[0]);
			params.put(WECHAT_HTTP_XML, result[1]);
			logger.info("订单号:{}, 请求类型:{} ,微信HTTP请求接口返回,状态:{} 值：{}", new Object[] { order.getTradeNo(), opType, result[0], result[1] });
		} catch (Exception e) {
			e.printStackTrace();
		}
		return params;
	}

	/**
	 * 微信订单查询组装
	 * 
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年9月25日上午11:41:38
	 */
	public static Map<String, String> wechatQueryGenertor(TradeOrder order, WechatVo wechatVo, String orderType) {
		Map<String, String> params = new HashMap<String, String>();
		if (!StringUtils.isEmpty(wechatVo.getSubMchId())) {
			params.put("sub_mch_id", wechatVo.getSubMchId());
			params.put("appid", wechatVo.getParentAppId());
		} else {
			params.put("appid", wechatVo.getAppId());
		}
		params.put("mch_id", wechatVo.getMchId()); // 微信支付分配的商户号
		if (!StringUtils.isEmpty(order.getAgtTradeNo())) {
			params.put("transaction_id", order.getAgtTradeNo()); // 微信支付订单号
		}
		params.put("nonce_str", PKGenerator.generateId()); // 随机字符串，不长于32位
		params.put("out_trade_no", order.getTradeNo()); // 商户系统内部的订单号,32个字符内、可包含字母,确保在商户系统唯一
		if (ORDER_REFUND_QUERY.equals(orderType)) {
			params.put("out_refund_no", order.getRefundNo());
			params.put("refund_id", order.getAgtRefundNo());
		}
		return params;
	}

	/**
	 * 微信关闭订单组装
	 * @param order
	 * @param wechatVo
	 * @return
	 */
	public static Map<String, String> wechatCloseGenertor(TradeOrder order, WechatVo wechatVo) {
		Map<String, String> params = new HashMap<String, String>();
		if (!StringUtils.isEmpty(wechatVo.getSubMchId())) { //子父商户
			params.put("sub_mch_id", wechatVo.getSubMchId());
			params.put("sub_appid", wechatVo.getSubAppId());
		}
		params.put("appid", wechatVo.getAppId());
		params.put("mch_id", wechatVo.getMchId());
		params.put("out_trade_no", order.getTradeNo());
		params.put("nonce_str", PKGenerator.generateId());
		return params;
	}

	/**
	 * 
	 * @Description: (描述)-解密微信退款回调加密数据
	 * @return
	 * @return Map<String,Object>
	 * @throws DocumentException
	 *
	 */
	public static Map<String, String> getWechatRefuntNotifyCode(String privateKey, String secritData) throws Exception {
		byte[] decodeFast = java.util.Base64.getDecoder().decode(secritData);
		String key = MD5Util.MD5Encode(privateKey, "UTF-8").toLowerCase();
		String decode = AES.Aes256Decode(decodeFast, key);
		logger.debug("退款回调解密后的明文：{}", decode);
		Map<String, String> xmlMap = WechatUtils.xmlMap(decode);
		// byte[] decodeFast = java.util.Base64.getDecoder().decode(secritData);
		// String decodeStr = new String(decodeFast);
		// SecretKeySpec keySpec = new
		// SecretKeySpec(MD5Util.MD5Encode(privateKey,
		// "UTF-8").toLowerCase().getBytes(), AESUtils.ALGORITHM);
		// String decryptData = AESUtils.decryptData(decodeStr, keySpec);
		// logger.debug("退款回调解密后的明文：{}", decryptData);
		// Map<String, String> xmlMap = WechatUtils.xmlMap(decryptData);
		return xmlMap;
	}

	/**
	 * XML转MAP
	 * 
	 * @param xml
	 * @return
	 * @throws DocumentException
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> xmlMap(String xml) throws DocumentException {
		Document retDoc = DocumentHelper.parseText(xml);
		return (Map<String, String>) xml2map(retDoc.getRootElement());
	}

	/**
	 * Element转MAP
	 * 
	 * @param element
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static Object xml2map(Element element) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Element> elements = element.elements();
		if (elements.size() == 0) {
			map.put(element.getName(), element.getText());
			if (!element.isRootElement()) {
				return element.getText();
			}
		} else if (elements.size() == 1) {
			map.put(elements.get(0).getName(), xml2map(elements.get(0)));
		} else if (elements.size() > 1) {
			// 多个子节点的话就得考虑list的情况了，比如多个子节点有节点名称相同的
			// 构造一个map用来去重
			Map<String, Element> tempMap = new HashMap<String, Element>();
			for (Element ele : elements) {
				tempMap.put(ele.getName(), ele);
			}
			Set<String> keySet = tempMap.keySet();
			for (String string : keySet) {
				Namespace namespace = tempMap.get(string).getNamespace();
				List<Element> elements2 = element.elements(new QName(string, namespace));
				// 如果同名的数目大于1则表示要构建list
				if (elements2.size() > 1) {
					List<Object> list = new ArrayList<Object>();
					for (Element ele : elements2) {
						list.add(xml2map(ele));
					}
					map.put(string, list);
				} else {
					// 同名的数量不大于1则直接递归去
					map.put(string, xml2map(elements2.get(0)));
				}
			}
		}
		return map;
	}
}

package com.sunshine.restful.sign;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;

import com.alibaba.fastjson.JSON;
import com.sunshine.common.GlobalConstant;
import com.sunshine.framework.common.PKGenerator;
import com.sunshine.framework.exception.SystemException;
import com.sunshine.framework.utils.DateUtils;
import com.sunshine.restful.RestConstant;

/**
 * 加签与验签工具类
 * @Project: cashier_desk 
 * @Package: com.sunshine.api.util
 * @ClassName: SignatureUtil
 * @Description: <p></p>
 * @JDK version used: 
 * @Author: 申姜
 * @Create Date: 2016年12月29日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class SignatureUtils {
	private static Logger logger = LoggerFactory.getLogger(SignatureUtils.class);
	/**
	 * 公/私钥生成算法:RSA,强制要求RSA密钥的长度至少为2048,因SHA256WithRSA签名算法强制要求密钥长度至少为2048
	 */
	public static final String SIGN_TYPE_RSA = "RSA";
	/**
	 * 密钥的长度
	 */
	public static final int KEY_LENGTH = 2048;
	/**
	 * 数据签名算法:SHA256WithRSA
	 */
	public static final String SIGN_SHA256RSA_ALGORITHMS = "SHA256WithRSA";
	/**
	 * RSA最大加密明文大小
	 */
	public static final int MAX_ENCRYPT_BLOCK = 117;
	/**
	 * RSA最大解密密文大小
	 */
	public static final int MAX_DECRYPT_BLOCK = 128;
	/**
	 * 获取公钥的key
	 */
	private static final String PUBLIC_KEY = "publicKey";

	/**
	 * 获取私钥的key
	 */
	private static final String PRIVATE_KEY = "privateKey";
	/**
	 * UTF-8字符集
	 */
	private static final String CHARSET_UTF8 = "UTF-8";

	/**
	 * 消息加签
	 * @param paramsMap
	 * @return
	 */
	public static String rsa256Sign(Map<String, String> paramsMap) {
		//支付平台公钥字符串不参与加签,只作为进行消息加签的钥匙
		String ygkzPrivateKey = paramsMap.get("ygkzPrivateKey");
		paramsMap.remove("ygkzPrivateKey");
		if (StringUtils.isBlank(ygkzPrivateKey)) {
			return null;
		}
		//排序
		String sortContent = getSortContent(paramsMap);
		//加签
		String sign = SignatureUtils.rsa256Sign(sortContent, ygkzPrivateKey);
		return sign;

	}

	/**
	 * 将集合内非空参数值的参数按照参数名ASCII码从小到大排序（字典序），使用URL键值对的格式（即key1=value1&key2=value2…）拼接成字符串
	 * @param paramsMap
	 * @return
	 */
	public static String getSortContent(Map<String, String> paramsMap) {
		StringBuffer content = new StringBuffer();
		List<String> keys = new ArrayList<String>(paramsMap.keySet());
		Collections.sort(keys);
		int index = 0;
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = paramsMap.get(key);
			if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
				content.append( ( index == 0 ? "" : "&" ) + key + "=" + value);
				index++;
			}
		}
		return content.toString();
	}

	/**
	 * 生成密钥对(公钥和私钥)
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> genKeyPair() throws Exception {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(SIGN_TYPE_RSA);
		keyPairGen.initialize(KEY_LENGTH);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		Map<String, Object> map = new HashMap<String, Object>(2);
		map.put(PUBLIC_KEY, publicKey);
		map.put(PRIVATE_KEY, privateKey);
		return map;
	}

	/**
	 * 获取私钥
	 * @param keyMap
	 * @return
	 * @throws Exception
	 */
	public static String getPrivateKey(Map<String, Object> map) throws Exception {
		Key key = (Key) map.get(PRIVATE_KEY);
		return new String(Base64Utils.encode(key.getEncoded()));
	}

	/**
	 * 获取公钥
	 * @param keyMap
	 * @return
	 * @throws Exception
	 */
	public static String getPublicKey(Map<String, Object> map) throws Exception {
		Key key = (Key) map.get(PUBLIC_KEY);
		return new String(Base64Utils.encode(key.getEncoded()));
	}

	/**
	 * SHA256WithRSA 验签
	 * @param content
	 * @param sign
	 * @param publicKey
	 * @return
	 * @throws SystemException
	 */
	public static boolean rsa256CheckContent(String content, String sign, String publicKey) throws SystemException {
		try {
			logger.info("验签字符串:{}", content);
			//获取公钥对象
			byte[] keyBytes = Base64Utils.decodeFromString(publicKey);
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance(SIGN_TYPE_RSA);
			PublicKey pubKey = keyFactory.generatePublic(keySpec);
			//SHA256WithRSA 验签
			Signature signature = Signature.getInstance(SIGN_SHA256RSA_ALGORITHMS);
			signature.initVerify(pubKey);
			signature.update(content.getBytes(CHARSET_UTF8));
			return signature.verify(Base64.decodeBase64(sign.getBytes()));
		} catch (Exception e) {
			logger.error("rsa256CheckContent failure!RSAcontent:{},sign:{}", content, sign, e.getStackTrace());
			e.printStackTrace();
			return false;

		}
	}

	/**
	 * SHA256WithRSA 加签
	 * @param content
	 * @param privateKey
	 * @return
	 * @throws SystemException
	 */
	public static String rsa256Sign(String content, String privateKey) {
		try {
			//获取私钥对象
			byte[] keyBytes = Base64Utils.decodeFromString(privateKey);
			PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance(SIGN_TYPE_RSA);
			PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);

			//SHA256WithRSA 加签
			Signature signature = Signature.getInstance(SIGN_SHA256RSA_ALGORITHMS);
			signature.initSign(priKey);
			signature.update(content.getBytes(CHARSET_UTF8));
			byte[] signed = signature.sign();
			return new String(Base64.encodeBase64(signed));
		} catch (Exception e) {
			logger.error("rsa256Sign failure!RSAcontent:{},sign:{}", content, e.getStackTrace());
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
	 * @param jsonData
	 * @return
	 */
	public static String buildSortJson(Map<String, String> dataMap) {
		StringBuffer sortSb = new StringBuffer();
		dataMap = paraFilter(dataMap);
		List<String> jsonKeys = new ArrayList<>();
		for (String key : dataMap.keySet()) {
			if (!RestConstant.PARAM_SIGN.equals(key)) {//签名标识字段不参与签名
				jsonKeys.add(key);
			}
		}
		Collections.sort(jsonKeys);

		for (String jsonKey : jsonKeys) {
			if (sortSb.length() > 0) {
				sortSb.append(GlobalConstant.STRING_AND_CHAR);
			}
			sortSb.append(jsonKey).append(GlobalConstant.STRING_ASSIGN_CHAR).append(dataMap.get(jsonKey));
		}

		return sortSb.toString();
	}

	/**
	 * 除去数组中的空值和签名参数
	 * @param sArray
	 * @return
	 */
	public static Map<String, String> paraFilter(Map<String, String> sArray) {
		Map<String, String> result = new HashMap<String, String>();
		if (sArray == null || sArray.size() <= 0) {
			return result;
		}
		for (String key : sArray.keySet()) {
			String value = "";
			if (sArray.get(key) instanceof String) {
				value = sArray.get(key);
			} else {
				value = String.valueOf(sArray.get(key));
			}
			if (value == null || value.equals("") || RestConstant.PARAM_SIGN.equalsIgnoreCase(key)) {
				continue;
			}
			result.put(key, value);
		}
		return result;
	}

	public static void main(String[] args) {
		String url = "http://apicashier.968309.cn/services/rest/v1/trade/refund/";
		String privateKey =
				"MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCV/bcRVStGbb/6U9sNgWxQxi4sE82z/3sTKTLuo2j7qYoR7HptE7NTIr9jjQX56zYxGj0caGwxxiGZ8N09i8AZEnsJiNHISPG0zqWA3Yte+t0qCop2Jm3Xzj1WFwGMBgMklJYZXHNSBh1f2CNsZYKy4EdoyShpdYzCRw1uC79qm83Q9rVp8o1Tbvq+zm5ah1cnPGYqeReoEi8GDnnDDvUFK0Hfptl6am0lWNRoWUeR2u8Lg758F0g3bRo6asV9/JJG+mQSZ0pkRXHMKnm3hDRpTZs6k3q2TSsYDoQEVR1BGITlftmbxoDg8tX4hnlFVWtmp7blzLg1PVkTzslIaKYxAgMBAAECggEAQxLJWePnTUBo+E2liJ2WzWxCW42jkFmwNPpYf2Tt3pSLXMaOXNbRe1zvj1oSERfBDSY5q8l6kKuUfR28Ifirt8qO4M2hWlpcVEmQs1bfK2Mtoc5C6uD2f+oHfccd9vr9Ts9va/nuzABWBHVIKgIe91TaOh6yhq59eqeBdvOj7sz8DuEMoaap501x8oqfzLEc9qh0u5Ao24xkaDQ3+czgM8eohJVPv8XUCWwDKdd1rtA2laFUgvK6e9f5OPhNcn/nPUBlFhcqtSz5w6pBtx6t8ygzhCtw5zrvg1Qrk26YZPhA0BQJY6/T5bU9K/siVobpOoweCpV0ubTc6uNk5Rr6AQKBgQDRlEL3KuZvOLi2AwvEAnH8MHRLr7T7bfylx75EOWYw4u5oAyQ65Nm6jd0dthQKY/qKSBi0iIYAPNbyu6DuJZxuMPNKhwPEMkVTsluqcmqouDmmVyvMjMhtpfVErHApDfGkfl59qxEPT2e4wMc7ubORb1gW5sUHbiFqJ8AKN7mV0QKBgQC3NqOWY/xNXJJbM0Tt31stmIauyL7QJ8OwP80yxL6uuhuM130xKt6XHhvA2QyGqlOdcmaS8RojoIYFGwmfEQzfE8k3n71Hfd4hOO5OHjLpbf2vVglJvMkEE1WJvCwGOWIRTs8lCuCUzSIXyFhvHsOqzRmVnN6E7RNVHcrpTcdCYQKBgQDRBdDAGR5YYTb/+Qu8A0BR6LWkYurbNTgc05l1C9Za/YoPXMq+nk51HFYi8t0L++j+D6fsLpmPFR7AWtfHjOV2lQWhGAVuoUC5mBKKbbDo80w0FY7OZL7ldPao9l6q44hUqeBt77aFEYn+Hu7WJ4DrlSRDOwfZQEK9UEXdKvTCcQKBgQCU79n0O7uyt/pSIU97gAMDAeca1x4m4Ox3rqYFC2NnbutOhy7CdXMVregp0UgZgQUQDRvNgCobpDbOl7gukxG/lroQec7I+01D/J/c6TfCAyQjEPHj+4/9vCnhGhM/zA4ou8Qw/LKsxEVeyAL78ipmAu01bpyFuqPooZt+JThCgQKBgCoeKDrBqr6mmEHMhV7hIpqrLQxLflWH+zjZRlrpw4Yr+3lA/fhI8+nAX6EH1KQ9kuwrMOGCo/n5a+wdaRafRy/W0Y7RxkfI0vj9cvQrOuzeF+fci0zhjZJB2O9S+Wv5UEP8TtKrp8E2dNysilB1k64mlcEOpTEdxPmXPNv4pylh";
		Map<String, String> dataMap = new HashMap<>();
		dataMap.put("appId", "sune08e7af3454a4ec");
		dataMap.put("merchantNo", "9683095222276217");
		dataMap.put("nonceStr", PKGenerator.generateId());
		dataMap.put("timeStamp", DateUtils.dateToString(new Date()));
		dataMap.put("signMode", "RSA");
		dataMap.put("outOrderNo", "Z1191201801230000012");
		//dataMap.put("tradeNo", "1010102201801209999818");
		dataMap.put("outRefundNo", "Z1192201801230000012");
		dataMap.put("tradeTotalFee", "6900");
		dataMap.put("refundDesc", "用户不懂操作，下错订单");
		dataMap.put("refundFee", "6900");
		//dataMap.put("refundNotifyUrl", "www.baidu.com");

		/*dataMap.put("notifyUrl", "http://cashierdev.968309.cn/notifyAlipay/callback/");
		dataMap.put("returnUrl", "http://cashierdev.968309.cn/trade/test/");
		dataMap.put("subject", "iPhone8");
		dataMap.put("tradeTotalFee", "1");
		dataMap.put("refundNotifyUrl", "www.baidu.com");*/

		String content = buildSortJson(dataMap);
		//content = content.replaceAll(" ", "");
		String sign = rsa256Sign(content, privateKey);
		dataMap.put("sign", sign);
		//logger.info("退费参数:{}", JSON.toJSONString(dataMap));
		System.out.println(JSON.toJSONString(dataMap));
		/*Map<String, Object> retMap = HttpClient.post(url, dataMap, "");
		logger.info("退费结果:{}", JSON.toJSONString(retMap));*/

	}
}

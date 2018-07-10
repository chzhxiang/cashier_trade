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
package com.sunshine.payment.alipay;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.sunshine.common.utils.sign.MD5;
import com.sunshine.common.utils.sign.RSA;
import com.sunshine.payment.TradeConstant;

/* *
 *类名：AlipayNotify
 *功能：支付宝通知处理类
 *详细：处理支付宝各接口通知返回
 *版本：3.3
 *日期：2012-08-17
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考

 *************************注意*************************
 *调试通知返回时，可查看或改写log日志的写入TXT里的数据，来检查通知返回是否正常
 */
public class AlipayNotify {
	private static Logger logger = LoggerFactory.getLogger(AlipayNotify.class);

	/**
	 * 支付宝消息验证地址
	 */
	private static final String HTTPS_VERIFY_URL = "https://mapi.alipay.com/gateway.do?service=notify_verify&";

	/**
	 * 验证消息是否是支付宝发出的合法消息
	 * @param params 通知返回来的参数数组
	 * @return 验证结果
	 */
	public static boolean verify(Map<String, String> params, String notifyId, String partner, String aliPublicKey) {

		//判断responsetTxt是否为true，isSign是否为true
		//responsetTxt的结果不是true，与服务器设置问题、合作身份者ID、notify_id一分钟失效有关
		//isSign不是true，与安全校验码、请求时的参数格式（如：带自定义参数等）、编码格式有关
		logger.debug("验证参数:" + params);
		String responseTxt = "true";
		logger.debug("notifyId: " + notifyId + ", partner:" + partner);
		if (StringUtils.isNotBlank(notifyId)) {
			responseTxt = verifyResponse(notifyId, partner);
		}
		String sign = "";
		if (params.get("sign") != null) {
			sign = params.get("sign");
		}
		boolean isSign = getSignVeryfy(params, sign, aliPublicKey);

		logger.debug("-----验证消息是否是支付宝发出的合法消息isSign: " + isSign);
		logger.debug("-----验证消息是否是支付宝发出的合法消息responseTxt: " + responseTxt);
		if (isSign && responseTxt.equals("true")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 根据反馈回来的信息，生成签名结果
	 * @param Params 通知返回来的参数数组
	 * @param sign 比对的签名结果
	 * @return 生成的签名结果
	 */
	private static boolean getSignVeryfy(Map<String, String> params, String sign, String aliPublicKey) {
		//过滤空值、sign与sign_type参数
		Map<String, String> sParaNew = AlipayCore.paraFilter(params);
		//获取待签名字符串
		String preSignStr = "service=" + sParaNew.get("service");
		preSignStr += "&v=" + sParaNew.get("v");
		preSignStr += "&sec_id=" + sParaNew.get("sec_id");
		preSignStr += "&notify_data=" + sParaNew.get("notify_data");
		logger.debug("获取待签名字符串:{}", preSignStr);
		return RSA.verify(preSignStr, sign, aliPublicKey, TradeConstant.INPUT_CHARSET);
	}

	/**
	 * 验证消息是否是支付宝发出的合法消息 非服务窗验证
	 * @param params 通知返回来的参数数组
	 * @return 验证结果
	 */
	public static boolean verifyQr(Map<String, String> params, String notifyId, String partner, String aliPublicKey) {

		//判断responsetTxt是否为true，isSign是否为true
		//responsetTxt的结果不是true，与服务器设置问题、合作身份者ID、notify_id一分钟失效有关
		//isSign不是true，与安全校验码、请求时的参数格式（如：带自定义参数等）、编码格式有关
		logger.info("验证参数:" + params);
		/*String responseTxt = "true";
		logger.info("notifyId: " + notifyId + ", partner:" + partner);
		if (StringUtils.isNotBlank(notifyId)) {
			responseTxt = verifyResponse(notifyId, partner);
		}*/
		String sign = "";
		if (params.get("sign") != null) {
			sign = params.get("sign");
		}
		boolean isSign = getSignVeryfyQr(params, sign, aliPublicKey);

		logger.info("-----验证消息是否是支付宝发出的合法消息isSign: " + isSign);
		//logger.info("-----验证消息是否是支付宝发出的合法消息responseTxt: " + responseTxt);
		if (isSign) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean getSignVeryfyQr(Map<String, String> Params, String sign, String ali_public_key) {
		String sign_type = Params.get("sign_type");
		//过滤空值、sign与sign_type参数
		Map<String, String> sParaNew = AlipayCore.paraFilter(Params);
		//获取待签名字符串
		String preSignStr = AlipayCore.createLinkString(sParaNew);
		logger.debug("sign:{}", sign);
		logger.debug("ali_public_key:{}", ali_public_key);
		logger.debug("获取待签名字符串:{}", preSignStr);
		boolean isSign = false;
		if (sign_type.equals(TradeConstant.SIGN_TYPE_RSA)) {
			isSign = RSA.verify(preSignStr, sign, ali_public_key, TradeConstant.INPUT_CHARSET);
		} else if (sign_type.equals(TradeConstant.SIGN_TYPE_MD5)) {
			isSign = MD5.verify(preSignStr, sign, ali_public_key, TradeConstant.INPUT_CHARSET);
		}
		//获得签名验证结果
		return isSign;
	}

	public static void main(String[] args) {
		Map<String, String> params = new HashMap<String, String>();
		String json =
				"sign=6e73f3fe59710619d6c0085023ed9079, result_details=2015120321001004140212185415^0.01^SUCCESS, notify_time=2015-12-03 11:31:57, sign_type=MD5, notify_type=batch_refund_notify, notify_id=38a87da988973c8379a923bfcaf2558mbs, batch_no=20151203113120122, success_num=1";

		String[] item = json.split(", ");
		if (item.length > 0) {
			for (String string : item) {
				int i = string.indexOf("=");
				params.put(string.substring(0, i), string.substring(i + 1, string.length()));
			}
		}
		System.out.println(JSON.toJSONString(params));

		//		String notify_data =
		//				"<notify><payment_type>8</payment_type><subject>挂号：产科(门)-产科(门)</subject><trade_no>2015111600001000890065485031</trade_no><buyer_email>13711343330</buyer_email><gmt_create>2015-11-16 14:53:30</gmt_create><notify_type>trade_status_sync</notify_type><quantity>1</quantity><out_trade_no>reg144765679288956183058</out_trade_no><notify_time>2015-11-16 16:17:27</notify_time><seller_id>2088711569199259</seller_id><trade_status>TRADE_SUCCESS</trade_status><is_total_fee_adjust>N</is_total_fee_adjust><total_fee>4.00</total_fee><gmt_payment>2015-11-16 14:53:30</gmt_payment><seller_email>3025515@qq.com</seller_email><price>4.00</price><buyer_id>2088212732719891</buyer_id><notify_id>5f931db198dae6d58f761203fdf415726y</notify_id><use_coupon>N</use_coupon></notify>";

		/*params.put("service", "alipay.wap.trade.create.direct");
		params.put("v", "1.0");
		params.put("sec_id", "0001");
		params.put("notify_data", notify_data);*/

		String sign = params.get("sign");
		String aliPublicKey = "gvbhzxaz21hasxcsgw9a96bzmnpaknue";
		//		String aliPublicKey =
		//				"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";
		//		String aliPublicKey =
		//				"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
		//		String aliPublicKey =
		//				"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";

		System.out.println(getSignVeryfyQr(params, sign, aliPublicKey));

	}

	/**
	* 获取远程服务器ATN结果,验证返回URL
	* @param notify_id 通知校验ID
	* @return 服务器ATN结果
	* 验证结果集：
	* invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空 
	* true 返回正确信息
	* false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
	*/
	private static String verifyResponse(String notify_id, String partner) {
		//获取远程服务器ATN结果，验证是否是支付宝服务器发来的请求

		//String partner = AlipayConfig.partner;
		String veryfy_url = HTTPS_VERIFY_URL + "partner=" + partner + "&notify_id=" + notify_id;

		return checkUrl(veryfy_url);
	}

	/**
	* 获取远程服务器ATN结果
	* @param urlvalue 指定URL路径地址
	* @return 服务器ATN结果
	* 验证结果集：
	* invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空 
	* true 返回正确信息
	* false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
	*/
	private static String checkUrl(String urlvalue) {
		String inputLine = "";

		try {
			URL url = new URL(urlvalue);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			inputLine = in.readLine().toString();
		} catch (Exception e) {
			e.printStackTrace();
			inputLine = "";
		}

		return inputLine;
	}
}

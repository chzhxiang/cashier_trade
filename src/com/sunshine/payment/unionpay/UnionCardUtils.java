/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年11月20日上午11:07:42</p>
 *  <p> Created by 熊胆</p>
 *  </body>
 * </html>
 */
package com.sunshine.payment.unionpay;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.sunshine.common.GlobalConstant;
import com.sunshine.framework.config.SystemConfig;
import com.sunshine.framework.utils.DateUtils;
import com.sunshine.mobileapp.bankCard.entity.BankCard;
import com.sunshine.payment.TradeConstant;
import com.sunshine.restful.utils.RestUtils;

/**
 * @Project: cashier_trade
 * @Package: com.sunshine.payment.unionpay
 * @ClassName: 银联绑卡工具类
 * @Description: <p></p>
 * @JDK version used: 
 * @Author: 熊胆
 * @Create Date: 2017年11月20日上午11:07:42
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class UnionCardUtils {

	private static Logger logger = LoggerFactory.getLogger(UnionCardUtils.class);

	/**
	 * 交易类型 :银联绑卡 11-代收
	 */
	public static final String TXNTYPE_UNION_BIND_CARD = "79";

	/**
	 * 业务类型:认证支付2.0
	 */
	public static final String BIZTYPE_UNION = "000301";

	/**
	 * 银联绑卡商户号
	 */
	public static final String BANK_CARD_MERID = SystemConfig.getStringValue("bank_card_merId");

	/**
	 *测试环境固定trId=62000000001&tokenType=01，生产环境由业务分配。测试环境因为所有商户都使用同一个trId，所以同一个卡获取的token号都相同，任一人发起更新token或者解除token请求都会导致原token号失效，所以之前成功、突然出现3900002报错时请先尝试重新开通一下。
	 */
	public static final String TOKEN_PAY_DATA = SystemConfig.getStringValue("tokenPayData");

	/**
	 * 银联绑卡异步回调地址
	 */
	public static final String UNION_CARD_NOTIFY_URL = "/notifyCard/union/callback/";

	/**
	 * 银联绑卡同步回调地址
	 */
	public static final String UNION_CARD_RETURN_URL = "/bankCard/bankCardList";

	/**
	 * 银联绑卡证书
	 */
	public static final String BANK_CARD_SIGNCERTPATH = SystemConfig.getStringValue("bank_card_signCertPath");

	/**
	 * 银联绑卡证书密码
	 */
	public static final String BANK_CARD_SIGNCERTPWD = SystemConfig.getStringValue("bank_card_signCertPwd");

	/**
	 * 请求前台交易地址【测试】：查询
	 */
	public static final String UNION_QUERY_FRONTTRANSURL = SystemConfig.getStringValue("union_query_frontTransUrl");

	/**
	 * 请求交易地址【测试】：查询
	 */
	public static final String UNION_QUERY_BACKTRANSURL = SystemConfig.getStringValue("union_query_backTransUrl");

	/**
	 * 交易类型 :银联绑卡查询
	 */
	public static final String TXNTYPE_UNION_QUERY = "78";

	/**
	 * 银联绑卡
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年11月1日上午11:31:50
	 */
	public static Map<String, Object> unionBindCardService(BankCard card) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			logger.info("银联绑卡签名入参:{}", JSON.toJSONString(card));

			Map<String, String> attachMap = new HashMap<String, String>();
			attachMap.put("cardId", card.getId());

			logger.info("银联绑卡透传参数:{}", JSON.toJSONString(attachMap));
			//调用银联SDK支付, 1，必须是否医程通App或者银联钱包App支付 
			card.setAttach(URLEncoder.encode(JSON.toJSONString(attachMap), TradeConstant.INPUT_CHARSET));

			Map<String, String> requestData = unionRemoveBindCardGenertor(card);
			resultMap.put("requestData", requestData);
			resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, true);
			resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
		} catch (Exception e) {
			resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
			resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, true);
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 银联绑卡参数组装
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年11月1日上午11:33:29
	 */
	public static Map<String, String> unionRemoveBindCardGenertor(BankCard card) {
		Map<String, String> params = new HashMap<String, String>();

		/***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
		params.put("version", UnionPayUtils.VERSION);//版本号
		params.put("encoding", TradeConstant.INPUT_CHARSET);//字符集编码 可以使用UTF-8,GBK两种方式
		params.put("signMethod", UnionPayUtils.SIGNMETHOD); //签名方法
		params.put("txnType", TXNTYPE_UNION_BIND_CARD);//交易类型 11-代收
		params.put("txnSubType", UnionPayUtils.TXNTYPE_QUERY);//交易子类型 00-默认开通
		params.put("bizType", BIZTYPE_UNION);//业务类型 Token支付,备注：修改为认证支付2.0，显示完整的卡号
		params.put("channelType", UnionPayUtils.CHANNELTYPE_PHONE);//渠道类型07-PC

		/***商户接入参数***/
		params.put("merId", BANK_CARD_MERID);//商户号码（本商户号码仅做为测试调通交易使用，该商户号配置了需要对敏感信息加密）测试时请改成自己申请的商户号，【自己注册的测试777开头的商户号不支持代收产品】
		params.put("accessType", UnionPayUtils.ACCESSTYPE_DIRECT);//接入类型，商户接入固定填0，不需修改	
		params.put("orderId", card.getCardSerialNo());//商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则	
		String txnTime = DateUtils.formatDate(DateUtils.dateToString(card.getCreateTime()), "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss");
		params.put("txnTime", txnTime); //订单发送时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效

		//测试环境固定trId=62000000001&tokenType=01，生产环境由业务分配。测试环境因为所有商户都使用同一个trId，所以同一个卡获取的token号都相同，任一人发起更新token或者解除token请求都会导致原token号失效，所以之前成功、突然出现3900002报错时请先尝试重新开通一下。
		params.put("tokenPayData", TOKEN_PAY_DATA);

		//只支持贷记卡 必送：卡号、手机号、CVN2、有效期；验证码看业务配置（默认不要短信验证码,本测试商户777290058110097配置了需要）。
		//		Map<String,String> customerInfoMap = new HashMap<String,String>();
		//		customerInfoMap.put("phoneNo", "13552535506");			        //手机号
		//		customerInfoMap.put("cvn2", "123");           			        //卡背面的cvn2三位数字
		//		customerInfoMap.put("expired", "1711");  				        //有效期 年在前月在后
		//		customerInfoMap.put("certifTp", "01");						//证件类型
		//		customerInfoMap.put("certifId", "341126197709218366");		//证件号码

		//选送卡号、手机号、证件类型+证件号、姓名 
		//也可以都不送,在界面输入这些要素
		//此测试商户号777290058110097 后台开通业务只支持 贷记卡
		Map<String, String> customerInfoMap = new HashMap<String, String>();
		if (card.getCertificateNo() != null && !"".equals(card.getCertificateNo())) {
			customerInfoMap.put("certifTp", card.getCertificateType()); //证件类型
			customerInfoMap.put("certifId", card.getCertificateNo()); //证件号码
		}
		customerInfoMap.put("customerNm", card.getUserName()); //姓名
		//customerInfoMap.put("phoneNo", "13552535506");			    //手机号

		////////////如果商户号开通了【商户对敏感信息加密】的权限那么需要对 accNo，pin和phoneNo，cvn2，expired加密（如果这些上送的话），对敏感信息加密使用：
		String accNo = AcpService.encryptData(card.getBankCardNo(), TradeConstant.INPUT_CHARSET, UnionPayUtils.ENCRYPTCERT_PATH); //这里测试的时候使用的是测试卡号，正式环境请使用真实卡号
		params.put("accNo", accNo);
		String customerInfoStr =
				AcpService.getCustomerInfoWithEncrypt(customerInfoMap, null, TradeConstant.INPUT_CHARSET, UnionPayUtils.ENCRYPTCERT_PATH);
		//////////

		/////////如果商户号未开通【商户对敏感信息加密】权限那么不需对敏感信息加密使用：
		//params.put("accNo", "6216261000000000018");
		//String customerInfoStr = AcpService.getCustomerInfo(customerInfoMap,null,DemoBase.encoding_UTF8);   //前台实名认证送支付验证要素 customerInfo中要素不要加密  
		////////
		params.put("customerInfo", customerInfoStr);

		params.put("encryptCertId", AcpService.getEncryptCertId(UnionPayUtils.ENCRYPTCERT_PATH)); //加密证书的certId，配置在acp_sdk.properties文件 acpsdk.encryptCert.path属性下

		//前台通知地址 （需设置为外网能访问 http https均可），支付成功后的页面 点击“返回商户”的时候将异步通知报文post到该地址
		//如果想要实现过几秒中自动跳转回商户页面权限，需联系银联业务申请开通自动返回商户权限
		//注：如果开通失败的“返回商户”按钮也是触发frontUrl地址，点击时是按照get方法返回的，没有通知数据返回商户
		params.put("frontUrl", card.getReturnUrl());

		//后台通知地址（需设置为【外网】能访问 http https均可），支付成功后银联会自动将异步通知报文post到商户上送的该地址，失败的交易银联不会发送后台通知
		//后台通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费交易 商户通知
		//注意:1.需设置为外网能访问，否则收不到通知    2.http https均可  3.收单后台通知后需要10秒内返回http200或302状态码 
		//    4.如果银联通知服务器发送通知后10秒内未收到返回状态码或者应答码非http200，那么银联会间隔一段时间再次发送。总共发送5次，每次的间隔时间为0,1,2,4分钟。
		//    5.后台通知地址如果上送了带有？的参数，例如：http://abc/web?a=b&c=d 在后台通知处理程序验证签名之前需要编写逻辑将这些字段去掉再验签，否则将会验签失败
		params.put("backUrl", RestUtils.getTradeDomainUrl().concat(UNION_CARD_NOTIFY_URL));

		// params.put("reserved", "{customPage=true}");         	//如果开通页面需要使用嵌入页面的话，请上送此用法		

		// 请求方保留域，
		// 透传字段，查询、通知、对账文件中均会原样出现，如有需要请启用并修改自己希望透传的数据。
		// 出现部分特殊字符时可能影响解析，请按下面建议的方式填写：
		// 1. 如果能确定内容不会出现&={}[]"'等符号时，可以直接填写数据，建议的方法如下。
		params.put("reqReserved", card.getAttach());
		// 2. 内容可能出现&={}[]"'符号时：
		// 1) 如果需要对账文件里能显示，可将字符替换成全角＆＝｛｝【】“‘字符（自己写代码，此处不演示）；
		// 2) 如果对账文件没有显示要求，可做一下base64（如下）。
		//    注意控制数据长度，实际传输的数据长度不能超过1024位。
		//    查询、通知等接口解析时使用new String(Base64.decodeBase64(reqReserved), DemoBase.encoding);解base64后再对数据做后续解析。
		//		params.put("reqReserved", Base64.encodeBase64String("任意格式的信息都可以".toString().getBytes(DemoBase.encoding)));

		// 订单超时时间。
		// 超过此时间后，除网银交易外，其他交易银联系统会拒绝受理，提示超时。 跳转银行网银交易如果超时后交易成功，会自动退款，大约5个工作日金额返还到持卡人账户。
		// 此时间建议取支付时的北京时间加15分钟。
		// 超过超时时间调查询接口应答origRespCode不是A6或者00的就可以判断为失败。
		params.put("payTimeout", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date().getTime() + 15 * 60 * 1000));

		Map<String, String> reqData = AcpService.sign(params, BANK_CARD_SIGNCERTPATH, BANK_CARD_SIGNCERTPWD, TradeConstant.INPUT_CHARSET); //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
		return reqData;
	}

	/**
	 * 银联绑卡请求html
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年10月27日上午9:46:35
	 */
	public static Map<String, Object> unionBindCardByHtmlService(BankCard card) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			logger.info("银联绑卡签名入参:{}", JSON.toJSONString(card));

			Map<String, String> attachMap = new HashMap<String, String>();
			attachMap.put("cardId", card.getId());

			logger.info("银联绑卡透传参数:{}", JSON.toJSONString(attachMap));
			//调用银联SDK支付, 1，必须是否医程通App或者银联钱包App支付 
			card.setAttach(URLEncoder.encode(JSON.toJSONString(attachMap), TradeConstant.INPUT_CHARSET));

			String html = unionRemoveBindCardByHtmlGenertor(card);
			logger.info("打印请求HTML，此为请求报文，为联调排查问题的依据：{}", html);
			resultMap.put("html", html);
			resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, true);
			resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
		} catch (Exception e) {
			resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
			resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, true);
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 请求绑卡接口生成html
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年10月27日上午9:54:13
	 */
	public static String unionRemoveBindCardByHtmlGenertor(BankCard card) {
		Map<String, String> params = new HashMap<String, String>();

		/***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
		params.put("version", UnionPayUtils.VERSION);//版本号
		params.put("encoding", TradeConstant.INPUT_CHARSET);//字符集编码 可以使用UTF-8,GBK两种方式
		params.put("signMethod", UnionPayUtils.SIGNMETHOD); //签名方法
		params.put("txnType", TXNTYPE_UNION_BIND_CARD);//交易类型 11-代收
		params.put("txnSubType", UnionPayUtils.TXNTYPE_QUERY);//交易子类型 00-默认开通
		params.put("bizType", BIZTYPE_UNION);//业务类型 Token支付,备注：修改为认证支付2.0，显示完整的卡号
		params.put("channelType", UnionPayUtils.CHANNELTYPE_PHONE);//渠道类型07-PC

		/***商户接入参数***/
		params.put("merId", BANK_CARD_MERID);//商户号码（本商户号码仅做为测试调通交易使用，该商户号配置了需要对敏感信息加密）测试时请改成自己申请的商户号，【自己注册的测试777开头的商户号不支持代收产品】
		params.put("accessType", UnionPayUtils.ACCESSTYPE_DIRECT);//接入类型，商户接入固定填0，不需修改	
		params.put("orderId", card.getCardSerialNo());//商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则	
		String txnTime = DateUtils.formatDate(DateUtils.dateToString(card.getCreateTime()), "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss");
		params.put("txnTime", txnTime); //订单发送时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效

		//测试环境固定trId=62000000001&tokenType=01，生产环境由业务分配。测试环境因为所有商户都使用同一个trId，所以同一个卡获取的token号都相同，任一人发起更新token或者解除token请求都会导致原token号失效，所以之前成功、突然出现3900002报错时请先尝试重新开通一下。
		params.put("tokenPayData", TOKEN_PAY_DATA);

		//只支持贷记卡 必送：卡号、手机号、CVN2、有效期；验证码看业务配置（默认不要短信验证码,本测试商户777290058110097配置了需要）。
		//		Map<String,String> customerInfoMap = new HashMap<String,String>();
		//		customerInfoMap.put("phoneNo", "13552535506");			        //手机号
		//		customerInfoMap.put("cvn2", "123");           			        //卡背面的cvn2三位数字
		//		customerInfoMap.put("expired", "1711");  				        //有效期 年在前月在后
		//		customerInfoMap.put("certifTp", "01");						//证件类型
		//		customerInfoMap.put("certifId", "341126197709218366");		//证件号码

		//选送卡号、手机号、证件类型+证件号、姓名 
		//也可以都不送,在界面输入这些要素
		//此测试商户号777290058110097 后台开通业务只支持 贷记卡
		Map<String, String> customerInfoMap = new HashMap<String, String>();
		if (card.getCertificateNo() != null && !"".equals(card.getCertificateNo())) {
			customerInfoMap.put("certifTp", card.getCertificateType()); //证件类型
			customerInfoMap.put("certifId", card.getCertificateNo()); //证件号码
		}
		customerInfoMap.put("customerNm", card.getUserName()); //姓名
		//customerInfoMap.put("phoneNo", "13552535506");			    //手机号

		////////////如果商户号开通了【商户对敏感信息加密】的权限那么需要对 accNo，pin和phoneNo，cvn2，expired加密（如果这些上送的话），对敏感信息加密使用：
		String accNo = AcpService.encryptData(card.getBankCardNo(), TradeConstant.INPUT_CHARSET, UnionPayUtils.ENCRYPTCERT_PATH); //这里测试的时候使用的是测试卡号，正式环境请使用真实卡号
		params.put("accNo", accNo);
		String customerInfoStr =
				AcpService.getCustomerInfoWithEncrypt(customerInfoMap, null, TradeConstant.INPUT_CHARSET, UnionPayUtils.ENCRYPTCERT_PATH);
		//////////

		/////////如果商户号未开通【商户对敏感信息加密】权限那么不需对敏感信息加密使用：
		//params.put("accNo", "6216261000000000018");
		//String customerInfoStr = AcpService.getCustomerInfo(customerInfoMap,null,DemoBase.encoding_UTF8);   //前台实名认证送支付验证要素 customerInfo中要素不要加密  
		////////
		params.put("customerInfo", customerInfoStr);

		params.put("encryptCertId", AcpService.getEncryptCertId(UnionPayUtils.ENCRYPTCERT_PATH)); //加密证书的certId，配置在acp_sdk.properties文件 acpsdk.encryptCert.path属性下

		//前台通知地址 （需设置为外网能访问 http https均可），支付成功后的页面 点击“返回商户”的时候将异步通知报文post到该地址
		//如果想要实现过几秒中自动跳转回商户页面权限，需联系银联业务申请开通自动返回商户权限
		//注：如果开通失败的“返回商户”按钮也是触发frontUrl地址，点击时是按照get方法返回的，没有通知数据返回商户
		params.put("frontUrl", card.getReturnUrl());

		//后台通知地址（需设置为【外网】能访问 http https均可），支付成功后银联会自动将异步通知报文post到商户上送的该地址，失败的交易银联不会发送后台通知
		//后台通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费交易 商户通知
		//注意:1.需设置为外网能访问，否则收不到通知    2.http https均可  3.收单后台通知后需要10秒内返回http200或302状态码 
		//    4.如果银联通知服务器发送通知后10秒内未收到返回状态码或者应答码非http200，那么银联会间隔一段时间再次发送。总共发送5次，每次的间隔时间为0,1,2,4分钟。
		//    5.后台通知地址如果上送了带有？的参数，例如：http://abc/web?a=b&c=d 在后台通知处理程序验证签名之前需要编写逻辑将这些字段去掉再验签，否则将会验签失败
		params.put("backUrl", RestUtils.getTradeDomainUrl().concat(UNION_CARD_NOTIFY_URL));

		// params.put("reserved", "{customPage=true}");         	//如果开通页面需要使用嵌入页面的话，请上送此用法		

		// 请求方保留域，
		// 透传字段，查询、通知、对账文件中均会原样出现，如有需要请启用并修改自己希望透传的数据。
		// 出现部分特殊字符时可能影响解析，请按下面建议的方式填写：
		// 1. 如果能确定内容不会出现&={}[]"'等符号时，可以直接填写数据，建议的方法如下。
		params.put("reqReserved", card.getAttach());
		// 2. 内容可能出现&={}[]"'符号时：
		// 1) 如果需要对账文件里能显示，可将字符替换成全角＆＝｛｝【】“‘字符（自己写代码，此处不演示）；
		// 2) 如果对账文件没有显示要求，可做一下base64（如下）。
		//    注意控制数据长度，实际传输的数据长度不能超过1024位。
		//    查询、通知等接口解析时使用new String(Base64.decodeBase64(reqReserved), DemoBase.encoding);解base64后再对数据做后续解析。
		//		params.put("reqReserved", Base64.encodeBase64String("任意格式的信息都可以".toString().getBytes(DemoBase.encoding)));

		// 订单超时时间。
		// 超过此时间后，除网银交易外，其他交易银联系统会拒绝受理，提示超时。 跳转银行网银交易如果超时后交易成功，会自动退款，大约5个工作日金额返还到持卡人账户。
		// 此时间建议取支付时的北京时间加15分钟。
		// 超过超时时间调查询接口应答origRespCode不是A6或者00的就可以判断为失败。
		params.put("payTimeout", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date().getTime() + 15 * 60 * 1000));

		Map<String, String> reqData = AcpService.sign(params, BANK_CARD_SIGNCERTPATH, BANK_CARD_SIGNCERTPWD, TradeConstant.INPUT_CHARSET); //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
		String requestFrontUrl = UNION_QUERY_FRONTTRANSURL; //获取请求银联的前台地址：对应属性文件acp_sdk.properties文件中的acpsdk.frontTransUrl
		//String html = AcpService.createAutoFormHtml(requestFrontUrl, reqData, TradeConstant.INPUT_CHARSET); //生成自动跳转的Html表单
		String html = createBindAutoFormHtml(requestFrontUrl, reqData, TradeConstant.INPUT_CHARSET); //生成自动跳转的Html表单
		return html;
	}

	/**
	 * 功能：前台交易构造HTTP POST自动提交表单<br>
	 * @param action 表单提交地址<br>
	 * @param hiddens 以MAP形式存储的表单键值<br>
	 * @param encoding 上送请求报文域encoding字段的值<br>
	 * @return 构造好的HTTP POST交易表单<br>
	 */
	public static String createBindAutoFormHtml(String reqUrl, Map<String, String> hiddens, String encoding) {
		StringBuffer sf = new StringBuffer();
		sf.append(
				"<html><head><title>绑卡页面</title><meta name=\"viewport\" content=\"initial-scale=1, maximum-scale=1, user-scalable=no, width=device-width\"><meta http-equiv=\"Content-Type\" content=\"text/html; charset="
						+ encoding + "\"/></head>");
		sf.append(
				"<style>.am-icon-loading:before { content: \"\"; display: block; background: url(../../mobileapp/newViewSrc/images/loading.png) no-repeat scroll 0 0; background-size: 100%; height: 20px; width: 20px; -webkit-animation: rotate1 linear 1s infinite;animation: rotate1 linear 1s infinite; }.am-loading-text { font-size: 1.0rem; position: fixed; top: 40%; background: #000; background-color: rgba(0,0,0,0.5); border-radius: 5px; color: #fff; display: inline-block; padding: 30px 20px; z-index: 999; left: 50%; -webkit-transform: translate3d(-50%,0,0); transform: translate3d(-50%,0,0); min-width: 236px; }.am-icon-loading { display: inline-block; height: 24px; vertical-align: middle; width: 24px; margin-right: 3px; }html { background: #edf3ff; } @-webkit-keyframes rotate1 {0% {-webkit-transform:rotate(0deg);} 100% {-webkit-transform:rotate(360deg);} }</style>");
		sf.append(
				"<body><div class=\"header\" style=\"display:none;\"><a class=\"back\" href=\"javascript:void(0);\"  onclick=\"backPage()\"><i class=\"icon icon-back middle\"></i></a><div class=\"list-type\">绑卡页面</div><a href=\"javascript:SystemUtil.goHomeIndex();\" class=\"back-home\">首页</a></div><div class=\"am-loading-text\"><span class=\"am-icon-loading\"></span><b id=\"am-loading_txt\" style=\"font-weight: normal\">正在进入提交银行卡信息页...</b></div>");
		sf.append("<form id = \"pay_form\" action=\"" + reqUrl + "\" method=\"post\">");
		if (null != hiddens && 0 != hiddens.size()) {
			Set<Entry<String, String>> set = hiddens.entrySet();
			Iterator<Entry<String, String>> it = set.iterator();
			while (it.hasNext()) {
				Entry<String, String> ey = it.next();
				String key = ey.getKey();
				String value = ey.getValue();
				sf.append("<input type=\"hidden\" name=\"" + key + "\" id=\"" + key + "\" value=\"" + value + "\"/>");
			}
		}
		sf.append("</form>");
		sf.append("</body>");
		sf.append("<script type=\"text/javascript\">");
		sf.append("setTimeout(function(){document.all.pay_form.submit();},800)");
		sf.append("</script>");
		sf.append("</html>");
		return sf.toString();
	}

	/**
	 * 查询银行卡信息
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年10月29日上午11:24:15
	 */
	public static Map<String, Object> unionBindCardQueryService(BankCard card) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			logger.info("银联绑卡查询签名入参:{}", JSON.toJSONString(card));
			Map<String, String> requestData = unionRemoveBindGenertor(card);
			Map<String, String> rspData = unionBackTrans(requestData, BANK_CARD_SIGNCERTPATH, BANK_CARD_SIGNCERTPWD);
			logger.info("银联绑卡查询返回参数:{}", JSON.toJSONString(rspData));
			if (!rspData.isEmpty()) {
				resultMap.put(GlobalConstant.TRADE_SUCCESS_DATA, JSON.toJSONString(rspData));
				resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
				if (UnionPayUtils.unionValidate(rspData)) {
					if (UnionPayUtils.RESPCODE_SUCCESS.equals(rspData.get("respCode"))) {
						resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, true);
						resultMap.put("activateStatus", rspData.get("activateStatus"));
						resultMap.put("payCardType", rspData.get("payCardType"));
					} else {
						resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
					}
				} else {
					resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
					resultMap.put(GlobalConstant.TRADE_FAIL_MSG, "验签失败");
				}
			} else {
				resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
				resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
			}
		} catch (Exception e) {
			resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, true);
			resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 银联绑卡查询参数组装
	 * @param unionQuery
	 * @return
	 */
	public static Map<String, String> unionRemoveBindGenertor(BankCard card) {
		Map<String, String> params = new HashMap<String, String>();

		/***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
		params.put("version", UnionPayUtils.VERSION);//版本号
		params.put("encoding", TradeConstant.INPUT_CHARSET);//字符集编码 可以使用UTF-8,GBK两种方式
		params.put("signMethod", UnionPayUtils.SIGNMETHOD); //签名方法
		params.put("txnType", TXNTYPE_UNION_QUERY);//交易类型 78-开通查询
		params.put("txnSubType", UnionPayUtils.TXNTYPE_QUERY);//交易子类型 00-根据账号accNo查询(默认）
		params.put("bizType", BIZTYPE_UNION);//业务类型 认证支付2.0
		params.put("channelType", UnionPayUtils.CHANNELTYPE_PHONE);//渠道类型07-PC

		/***商户接入参数***/
		params.put("merId", BANK_CARD_MERID); //商户号码（本商户号码仅做为测试调通交易使用，该商户号配置了需要对敏感信息加密）测试时请改成自己申请的商户号，【自己注册的测试777开头的商户号不支持代收产品】
		params.put("orderId", DateUtils.getCurrentTime());//商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则	
		params.put("txnTime", DateUtils.getCurrentTime()); //订单发送时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
		params.put("accessType", UnionPayUtils.ACCESSTYPE_DIRECT);//接入类型，商户接入固定填0，不需修改	

		////////////如果商户号开通了【商户对敏感信息加密】的权限那么需要对 accNo,phoneNo加密使用：
		String accNo = AcpService.encryptData(card.getBankCardNo(), "UTF-8", UnionPayUtils.ENCRYPTCERT_PATH);//这里测试的时候使用的是测试卡号，正式环境请使用真实卡号
		params.put("accNo", accNo);
		params.put("encryptCertId", AcpService.getEncryptCertId(UnionPayUtils.ENCRYPTCERT_PATH)); //加密证书的certId，配置在acp_sdk.properties文件 acpsdk.encryptCert.path属性下
		logger.info("银联：绑卡参数:{}", JSON.toJSONString(params));
		return params;
	}

	/**
	 * 银联绑卡查询
	 * @param requestData
	 * @param signCertPath
	 * @param signCertPwd
	 * @return
	 */
	public static Map<String, String> unionBackTrans(Map<String, String> requestData, String signCertPath, String signCertPwd) {
		/**对请求参数进行签名并发送http post请求，接收同步应答报文**/
		Map<String, String> reqData = AcpService.sign(requestData, signCertPath, signCertPwd, TradeConstant.INPUT_CHARSET);
		String requestUrl = UNION_QUERY_BACKTRANSURL;//"https://gateway.95516.com/gateway/api/backTransReq.do"; //
		return AcpService.post(reqData, requestUrl, TradeConstant.INPUT_CHARSET);
	}

}

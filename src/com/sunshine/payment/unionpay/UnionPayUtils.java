/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年9月25日</p>
 *  <p> Created by 熊胆</p>
 *  </body>
 * </html>
 */
package com.sunshine.payment.unionpay;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.sunshine.framework.config.SystemConfig;
import com.sunshine.mobileapp.order.entity.TradeOrder;
import com.sunshine.payment.TradeConstant;
import com.sunshine.platform.TradeChannelConstants;
import com.sunshine.platform.channel.vo.UnionPayVo;

/**
 * @Project: cashier_trade
 * @Package: com.sunshine.payment.unionpay.utils
 * @ClassName: UnionPayUtils
 * @Description: 银联支付工具类
 * @JDK version used:
 * @Author: 熊胆
 * @Create Date: 2017年9月25日上午10:21:33
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class UnionPayUtils {

	private static Logger logger = LoggerFactory.getLogger(UnionPayUtils.class);

	/**
	 * 请求交易地址：查询
	 */
	public static final String UNION_SINGLEQUERYURL = SystemConfig.getStringValue("union_singleQueryUrl");

	/**
	 * 是否启用多证书模式(true:单证书|false:多证书---没有配置此项时,默认为单证书模式)
	 */
	public static final String ACPSDK_SINGLEMODE = "acpsdk_singleMode";

	/**
	 * 验签文件路径
	 */
	public static final String ACPSDK_VALIDATECERT_DIR = "acpsdk_validateCert_dir";

	/**
	 * 银联支付 全渠道固定值
	 */
	public static final String VERSION = "5.0.0";

	/**
	 * 银联支付 全渠道固定值
	 */
	public static final String VERSION_5_1_0 = "5.1.0";

	/**
	 * 签名方法
	 */
	public static final String SIGNMETHOD = "01";

	/**
	 * 交易类型 :消费
	 */
	public static final String TXNTYPE_CONSUME = "01";

	/**
	 * 交易类型 :查询
	 */
	public static final String TXNTYPE_QUERY = "00";

	/**
	 * 交易类型 :退货
	 */
	public static final String TXNTYPE_REFUND = "04";

	/**
	 * 业务类型:B2C网关支付，手机wap支付
	 */
	public static final String BIZTYPE_GATEWAY_MOBILE = "000201";

	/**
	 * 业务类型:代收
	 */
	public static final String BIZTYPE_DAISHOU = "000501";

	/**
	 * 渠道类型:PC,平板
	 */
	public static final String CHANNELTYPE_PC = "07";

	/**
	 * 渠道类型:手机
	 */
	public static final String CHANNELTYPE_PHONE = "08";

	/**
	 * 接入类型：直连商户
	 */
	public static final String ACCESSTYPE_DIRECT = "0";

	/**
	 * s 货币类型：人名币
	 */
	public static final String CURRENCYCODE = "156";

	/**
	 * 银联应答码
	 */
	public static final String RESPCODE_SUCCESS = "00";

	/**
	 * 同步回调重定向地址
	 */
	public static final String REDIRECT_URL = "redirect_url";

	/**
	 * 敏感信息加密证书路径
	 */
	public static final String ENCRYPTCERT_PATH = SystemConfig.getStringValue("acpsdk.encryptCert.path");

	/**
	 * 支付后返回地址参数
	 */
	public static final String TRADE_RETURN_URL = "returnUrl";

	/**
	 * applePayMerchantID
	 */
	public static final String TRADE_APPLE_PAY_MERCHANTID = "applePayMerchantID";

	/**
	 * 交易金额
	 */
	public static final String TRADE_UNION_PAY_TOTALFEE = "unionPayTotalFee";

	/************************************************银联接口 start******************************************************/
	/**
	 * 银联聚合支付
	 * @param order
	 * @param unionPayVo
	 * @return
	 */
	public static Map<String, Object> unionPay(TradeOrder order, UnionPayVo vo) {
		if (TradeChannelConstants.TRADE_CHANNEL_UNIONPAY_SMALL_QUICK.equals(order.getChannelCode())) {
			return UnionQrPayUtils.dealWithQrCode(order, vo);
		} else if (TradeChannelConstants.TRADE_CHANNEL_UNIONPAY_H5.equals(order.getChannelCode())) {
			return UnionH5PayUtils.unionPayH5(order, vo);
		} else if (TradeChannelConstants.TRADE_CHANNEL_UNIONPAY_SDK.equals(order.getChannelCode())) {
			return UnionH5PayUtils.unionPaySDK(order, vo);
		} else if (TradeChannelConstants.TRADE_CHANNEL_UNIONPAY_APPLE_PAY.equals(order.getChannelCode())) {
			Map<String, Object> resultMap = UnionH5PayUtils.unionPaySDK(order, vo);
			resultMap.put(TRADE_APPLE_PAY_MERCHANTID, vo.getPayPrivateKey());
			resultMap.put(TRADE_UNION_PAY_TOTALFEE, order.getTotalFee());
			return resultMap;
		} else {
			return null;
		}
	}

	/**
	 * 银联聚合退费
	 * @param order
	 * @param unionPayVo
	 * @return
	 */
	public static Map<String, Object> unionRefund(TradeOrder order, UnionPayVo vo) {
		if (TradeChannelConstants.TRADE_CHANNEL_UNIONPAY_SMALL_QUICK.equals(order.getChannelCode())) {
			return UnionQrPayUtils.unionQrCodeMerRefund(order, vo);
		} else if (TradeChannelConstants.TRADE_CHANNEL_UNIONPAY_H5.equals(order.getChannelCode())) {
			return UnionH5PayUtils.unionRefundH5(order, vo);
		} else if (TradeChannelConstants.TRADE_CHANNEL_UNIONPAY_SDK.equals(order.getChannelCode())) {
			return UnionH5PayUtils.unionRefundH5(order, vo);
		} else if (TradeChannelConstants.TRADE_CHANNEL_UNIONPAY_APPLE_PAY.equals(order.getChannelCode())) {
			return UnionH5PayUtils.unionRefundH5(order, vo);
		} else {
			return null;
		}
	}

	/**
	 * 银联聚合支付查询
	 * @param order
	 * @param unionPayVo
	 * @return
	 */
	public static Map<String, Object> unionPayQuery(TradeOrder order, UnionPayVo vo) {
		if (TradeChannelConstants.TRADE_CHANNEL_UNIONPAY_SMALL_QUICK.equals(order.getChannelCode())) {
			return UnionQrPayUtils.orderQueryServiceUnionQrPay(order, vo);
		} else if (TradeChannelConstants.TRADE_CHANNEL_UNIONPAY_H5.equals(order.getChannelCode())) {
			return UnionH5PayUtils.unionPayH5Query(order, vo);
		} else if (TradeChannelConstants.TRADE_CHANNEL_UNIONPAY_SDK.equals(order.getChannelCode())) {
			return UnionH5PayUtils.unionPayH5Query(order, vo);
		} else if (TradeChannelConstants.TRADE_CHANNEL_UNIONPAY_APPLE_PAY.equals(order.getChannelCode())) {
			return UnionH5PayUtils.unionPayH5Query(order, vo);
		} else {
			return null;
		}
	}

	/**
	 * 银联聚合退费查询
	 * @param order
	 * @param unionPayVo
	 * @return
	 */
	public static Map<String, Object> unionRefundQuery(TradeOrder order, UnionPayVo vo) {
		if (TradeChannelConstants.TRADE_CHANNEL_UNIONPAY_SMALL_QUICK.equals(order.getChannelCode())) {
			return UnionQrPayUtils.orderQueryServiceUnionQrPay(order, vo);
		} else if (TradeChannelConstants.TRADE_CHANNEL_UNIONPAY_H5.equals(order.getChannelCode())) {
			return UnionH5PayUtils.unionRefundH5Query(order, vo);
		} else if (TradeChannelConstants.TRADE_CHANNEL_UNIONPAY_SDK.equals(order.getChannelCode())) {
			return UnionH5PayUtils.unionRefundH5Query(order, vo);
		} else if (TradeChannelConstants.TRADE_CHANNEL_UNIONPAY_APPLE_PAY.equals(order.getChannelCode())) {
			return UnionH5PayUtils.unionRefundH5Query(order, vo);
		} else {
			return null;
		}
	}

	/************************************************银联接口 end******************************************************/

	/**
	 * 是否启用多证书模式(true:单证书|false:多证书---没有配置此项时,默认为单证书模式)
	 * 
	 * @return
	 */
	public static boolean singleMode() {
		return SystemConfig.getBooleanValue(ACPSDK_SINGLEMODE, true);
	}

	/**
	 * 验签文件证书路径
	 * 
	 * @return
	 */
	public static String getValidateCertDir() {
		return SystemConfig.getStringValue(ACPSDK_VALIDATECERT_DIR);
	}

	/**
	 * 商户发送交易时间 格式:YYYYMMDDhhmmss
	 * 
	 * @return
	 */
	public static String getCurrentTime() {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	}

	/**
	 * base64加密
	 * 
	 * @param str
	 * @return
	 */
	public static String getBase64(String str) {
		byte[] b = null;
		String s = null;
		try {
			b = str.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (b != null) {
			s = new BASE64Encoder().encode(b);
		}
		return s.replaceAll("\r\n", "");
	}

	/**
	 * base64 解密
	 * 
	 * @param s
	 * @return
	 */
	public static String getFromBase64(String s) {
		byte[] b = null;
		String result = null;
		if (s != null) {
			BASE64Decoder decoder = new BASE64Decoder();
			try {
				b = decoder.decodeBuffer(s);
				result = new String(b, "utf-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 组装付款方信息
	 * @param encoding 编码方式
	 * @return 用{}连接并base64后的付款方信息
	 */
	public static String getPayerInfo(Map<String, String> payarInfoMap, String encoding) {
		return formInfoBase64(payarInfoMap, encoding);
	}

	/**
	 * 组装付款方信息(接入机构配置了敏感信息加密)
	 * @param encoding 编码方式
	 * @return 用{}连接并base64后的付款方信息
	 */
	public static String getPayerInfoWithEncrpyt(Map<String, String> payarInfoMap, String encoding) {
		return formInfoBase64WithEncrpyt(payarInfoMap, encoding);
	}

	/**
	 * 用{}连接并base64(接入机构配置了敏感信息加密)
	 * @param map
	 * @param encoding
	 * @return
	 */
	public static String formInfoBase64WithEncrpyt(Map<String, String> map, String encoding) {
		StringBuffer sf = new StringBuffer();
		String info = sf.append(SDKConstants.LEFT_BRACE).append(SDKUtil.coverMap2String(map)).append(SDKConstants.RIGHT_BRACE).toString();
		info = SecureUtil.EncryptData(info, encoding, CertUtil.getEncryptCertPublicKey(ENCRYPTCERT_PATH));
		return info;
	}

	/**
	 * 组装风控信息
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年5月29日上午11:01:59
	 */
	public static String getRiskInfo(Map<String, String> riskInfoMap, String encoding) {
		return formInfoBase64(riskInfoMap, encoding);
	}

	/**
	 * 验签
	 * 
	 * @param validateData
	 * @return
	 */
	public static boolean unionValidate(Map<String, String> validateData) {
		String dir = UnionPayUtils.getValidateCertDir();
		logger.info("验签文件夹路径:{}", dir);
		return AcpService.validate(validateData, TradeConstant.INPUT_CHARSET, dir);
	}

	/**
	 * 用{}连接并base64
	 * 
	 * @param map
	 * @param encoding
	 * @return
	 */
	public static String formInfoBase64(Map<String, String> map, String encoding) {
		StringBuffer sf = new StringBuffer();
		String info = sf.append(SDKConstants.LEFT_BRACE).append(SDKUtil.coverMap2String(map)).append(SDKConstants.RIGHT_BRACE).toString();
		try {
			info = new String(AcpService.base64Encode(info, encoding));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return info;
	}

	/**
	 * 将Map中的数据转换成按照Key的ascii码排序后的key1=value1&key2=value2的形式
	 * 
	 * @param data
	 *            待拼接的Map数据
	 * @return 拼接好后的字符串
	 */
	public static String coverMap2String(Map<String, String> data) {
		TreeMap<String, String> tree = new TreeMap<String, String>();
		Iterator<Entry<String, String>> it = data.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> en = it.next();
			tree.put(en.getKey(), en.getValue());
		}
		it = tree.entrySet().iterator();
		StringBuffer sf = new StringBuffer();
		while (it.hasNext()) {
			Entry<String, String> en = it.next();
			sf.append(en.getKey() + SDKConstants.EQUAL + en.getValue() + SDKConstants.AMPERSAND);
		}
		return sf.substring(0, sf.length() - 1);
	}

	/**
	 * 获取请求参数中所有的信息
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年7月10日上午11:06:59
	 */
	public static Map<String, String> getAllRequestParam(final HttpServletRequest request) {
		Map<String, String> res = new HashMap<String, String>();
		Enumeration<?> temp = request.getParameterNames();
		if (null != temp) {
			while (temp.hasMoreElements()) {
				String en = (String) temp.nextElement();
				String value = request.getParameter(en);
				res.put(en, value);
				// 在报文上送时，如果字段的值为空，则不上送<下面的处理为在获取所有参数数据时，判断若值为空，则删除这个字段>
				// System.out.println("ServletUtil类247行  temp数据的键=="+en+"     值==="+value);
				if (null == res.get(en) || "".equals(res.get(en))) {
					res.remove(en);
				}
			}
		}
		return res;
	}
}

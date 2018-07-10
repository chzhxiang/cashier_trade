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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.sunshine.common.GlobalConstant;
import com.sunshine.mobileapp.order.entity.TradeOrder;
import com.sunshine.payment.TradeConstant;
import com.sunshine.payment.TradeUtils;
import com.sunshine.platform.TradeChannelConstants;
import com.sunshine.platform.channel.vo.UnionPayVo;
import com.sunshine.restful.utils.RestUtils;

/**
 * @Project: cashier_trade
 * @Package: com.sunshine.payment.unionpay.utils
 * @ClassName: UnionPayUtils
 * @Description: 银联H5支付工具类
 * @JDK version used:
 * @Author: 熊胆
 * @Create Date: 2017年9月25日上午10:21:33
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class UnionH5PayUtils {

	private static Logger logger = LoggerFactory.getLogger(UnionH5PayUtils.class);

	/**
	 * 前端网页支付
	 */
	public static final String UNION_FRONTTRANSURL = "https://gateway.95516.com/gateway/api/frontTransReq.do";

	/**
	 * 退费地址
	 */
	public static final String UNION_BACK_TRANS_URL = "https://gateway.95516.com/gateway/api/backTransReq.do";

	/**
	 * 查询地址
	 */
	public static final String UNION_SINGLE_QUERY_URL = "https://gateway.95516.com/gateway/api/queryTrans.do";

	/**
	 * 批量交易
	 */
	public static final String UNION_BATCH_TRANS_URL = "https://gateway.95516.com/gateway/api/batchTrans.do";

	/**
	 * APP支付
	 */
	public static final String UNION_APP_TRANS_URL = "https://gateway.95516.com/gateway/api/appTransReq.do";

	/**
	 * 绑卡地址
	 */
	public static final String UNION_CARD_TRANS_URL = "https://gateway.95516.com/gateway/api/cardTransReq.do";

	/**
	 * 银联支付 全渠道固定值
	 */
	public static final String VERSION = "5.0.0";
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
	 * 支付回调地址
	 */
	public static final String NOTIFY_PAY_URL = "/notifyUnionPay/qrCode/callback/";

	/**
	 * 退费回调地址
	 */
	public static final String NOTIFY_REFUND_URL = "/notifyUnionPay/refund/callback/";

	/************************************************银联sdk end******************************************************/
	/**
	 * 银联支付参数组装
	 * @param pay
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> unionPayGenertor(TradeOrder order, UnionPayVo vo) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("version", VERSION); //版本号，全渠道默认值
		params.put("encoding", TradeConstant.INPUT_CHARSET); //字符集编码，可以使用UTF-8,GBK两种方式
		params.put("signMethod", SIGNMETHOD); //签名方法，只支持 01：RSA方式证书加密
		params.put("txnType", TXNTYPE_CONSUME); //交易类型 ，01：消费
		params.put("txnSubType", TXNTYPE_CONSUME); //交易子类型， 01：自助消费
		params.put("bizType", BIZTYPE_GATEWAY_MOBILE); //业务类型，B2C网关支付，手机wap支付

		//params.put("channelType", CHANNELTYPE_PHONE);
		if (TradeChannelConstants.TRADE_CHANNEL_UNIONPAY_H5.equals(order.getChannelCode())) {
			params.put("channelType", CHANNELTYPE_PC);
			try {
				params.put("reqReserved", URLEncoder.encode(JSON.toJSONString(TradeUtils.buildAttach(order)), TradeConstant.INPUT_CHARSET));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else {
			params.put("channelType", CHANNELTYPE_PHONE);
			params.put("reqReserved", JSON.toJSONString(TradeUtils.buildAttach(order)));
		}

		/***商户接入参数***/
		params.put("merId", vo.getMchId()); //商户号码，请改成自己申请的正式商户号或者open上注册得来的777测试商户号
		params.put("accessType", ACCESSTYPE_DIRECT); //接入类型，0：直连商户 
		params.put("orderId", order.getTradeNo()); //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则		
		params.put("txnTime", GlobalConstant.formatyyyymmddhhmmss(new Date())); //订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
		params.put("currencyCode", CURRENCYCODE); //交易币种（境内商户一般是156 人民币）		
		params.put("txnAmt", String.valueOf(order.getTotalFee())); //交易金额，单位分，不要带小数点
		//params.put("reqReserved", JSON.toJSONString(TradeUtils.buildAttach(order))); //请求方保留域，如需使用请启用即可；透传字段（可以实现商户自定义参数的追踪）本交易的后台通知,对本交易的交易状态查询交易、对账文件中均会原样返回，商户可以按需上传，长度为1-1024个字节		

		//前台通知地址 （需设置为外网能访问 http https均可），支付成功后的页面 点击“返回商户”按钮的时候将异步通知报文post到该地址
		//如果想要实现过几秒中自动跳转回商户页面权限，需联系银联业务申请开通自动返回商户权限
		params.put("frontUrl", TradeConstant.getSynchroCallback(order.getId()));

		//后台通知地址（需设置为【外网】能访问 http https均可），支付成功后银联会自动将异步通知报文post到商户上送的该地址，失败的交易银联不会发送后台通知
		//后台通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费交易 商户通知
		//注意:1.需设置为外网能访问，否则收不到通知    2.http https均可  3.收单后台通知后需要10秒内返回http200或302状态码 
		//    4.如果银联通知服务器发送通知后10秒内未收到返回状态码或者应答码非http200，那么银联会间隔一段时间再次发送。总共发送5次，每次的间隔时间为0,1,2,4分钟。
		//    5.后台通知地址如果上送了带有？的参数，例如：http://abc/web?a=b&c=d 在后台通知处理程序验证签名之前需要编写逻辑将这些字段去掉再验签，否则将会验签失败
		params.put("backUrl", RestUtils.getTradeDomainUrl().concat(NOTIFY_PAY_URL));
		logger.info("银联支付参数:{}", JSON.toJSONString(params));
		return params;
	}

	/**
	 * 银联退费参数组装
	 * @param order
	 * @param vo
	 * @return
	 */
	public static Map<String, String> unionRefundGenertor(TradeOrder order, UnionPayVo vo) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("version", VERSION); //版本号，全渠道默认值
		params.put("encoding", TradeConstant.INPUT_CHARSET); //字符集编码，可以使用UTF-8,GBK两种方式
		params.put("signMethod", SIGNMETHOD); //签名方法，只支持 01：RSA方式证书加密
		params.put("txnType", TXNTYPE_REFUND); //交易类型 ，04
		params.put("txnSubType", TXNTYPE_QUERY); //交易子类型， 00
		params.put("bizType", BIZTYPE_GATEWAY_MOBILE); //业务类型，B2C网关支付，手机wap支付
		//渠道类型，这个字段区分B2C网关支付和手机wap支付；07：PC,平板  08：手机
		if (TradeChannelConstants.TRADE_CHANNEL_UNIONPAY_H5.equals(order.getChannelCode())) {
			params.put("channelType", CHANNELTYPE_PC);
			try {
				params.put("reqReserved", URLEncoder.encode(JSON.toJSONString(TradeUtils.buildAttach(order)), TradeConstant.INPUT_CHARSET));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else {
			params.put("channelType", CHANNELTYPE_PHONE);
			params.put("reqReserved", JSON.toJSONString(TradeUtils.buildAttach(order)));
		}
		/***商户接入参数***/
		params.put("merId", vo.getMchId()); //商户号码，请改成自己申请的正式商户号或者open上注册得来的777测试商户号
		params.put("accessType", ACCESSTYPE_DIRECT); //接入类型，0：直连商户 
		params.put("currencyCode", CURRENCYCODE); //交易币种（境内商户一般是156 人民币）		
		params.put("txnAmt", String.valueOf(order.getRefundFee())); //交易金额，单位分，不要带小数点
		params.put("orderId", order.getRefundNo()); //****商户订单号，每次发交易测试需修改为被查询的交易的订单号
		params.put("txnTime", GlobalConstant.formatyyyymmddhhmmss(new Date())); //****订单发送时间，每次发交易测试需修改为被查询的交易的订单发送时间
		params.put("origQryId", order.getAgtTradeNo());
		params.put("backUrl", RestUtils.getTradeDomainUrl().concat(NOTIFY_REFUND_URL));
		logger.info("银联退费参数:{}", JSON.toJSONString(params));
		return params;
	}

	/**
	 * 银联支付查询参数组装
	 * @param pay
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> unionPayQueryGenertor(TradeOrder order, UnionPayVo vo) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("version", VERSION); //版本号，全渠道默认值
		params.put("encoding", TradeConstant.INPUT_CHARSET); //字符集编码，可以使用UTF-8,GBK两种方式
		params.put("signMethod", SIGNMETHOD); //签名方法，只支持 01：RSA方式证书加密
		params.put("txnType", TXNTYPE_QUERY); //交易类型 ，01：消费
		params.put("txnSubType", TXNTYPE_QUERY); //交易子类型， 01：自助消费
		params.put("bizType", BIZTYPE_GATEWAY_MOBILE); //业务类型，B2C网关支付，手机wap支付

		if (TradeChannelConstants.TRADE_CHANNEL_UNIONPAY_H5.equals(order.getChannelCode())) {
			params.put("channelType", CHANNELTYPE_PC);
			try {
				params.put("reqReserved", URLEncoder.encode(JSON.toJSONString(TradeUtils.buildAttach(order)), TradeConstant.INPUT_CHARSET));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else {
			params.put("reqReserved", JSON.toJSONString(TradeUtils.buildAttach(order)));
		}

		params.put("merId", vo.getMchId()); //商户号码，请改成自己申请的正式商户号或者open上注册得来的777测试商户号
		params.put("accessType", ACCESSTYPE_DIRECT); //接入类型，0：直连商户 
		params.put("orderId", order.getTradeNo());
		String txnTime = "";
		if (order.getPayTime() != null) {
			txnTime = GlobalConstant.formatyyyymmddhhmmss(order.getPayTime());
		} else {
			txnTime = GlobalConstant.formatyyyymmddhhmmss(order.getPayApplyTime());
		}
		params.put("txnTime", txnTime); //****订单发送时间，每次发交易测试需修改为被查询的交易的订单发送时间
		logger.info("银联支付查询参数:{}", JSON.toJSONString(params));
		return params;
	}

	/**
	 * 银联退费查询参数组装
	 * @param pay
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> unionRefundQueryGenertor(TradeOrder order, UnionPayVo vo) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("version", VERSION); //版本号，全渠道默认值
		params.put("encoding", TradeConstant.INPUT_CHARSET); //字符集编码，可以使用UTF-8,GBK两种方式
		params.put("signMethod", SIGNMETHOD); //签名方法，只支持 01：RSA方式证书加密
		params.put("txnType", TXNTYPE_QUERY); //交易类型 ，01：消费
		params.put("txnSubType", TXNTYPE_QUERY); //交易子类型， 01：自助消费
		params.put("bizType", BIZTYPE_GATEWAY_MOBILE); //业务类型，B2C网关支付，手机wap支付

		if (TradeChannelConstants.TRADE_CHANNEL_UNIONPAY_H5.equals(order.getChannelCode())) {
			params.put("channelType", CHANNELTYPE_PC);
			try {
				params.put("reqReserved", URLEncoder.encode(JSON.toJSONString(TradeUtils.buildAttach(order)), TradeConstant.INPUT_CHARSET));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else {
			params.put("reqReserved", JSON.toJSONString(TradeUtils.buildAttach(order)));
		}

		params.put("merId", vo.getMchId()); //商户号码，请改成自己申请的正式商户号或者open上注册得来的777测试商户号
		params.put("accessType", ACCESSTYPE_DIRECT); //接入类型，0：直连商户 
		params.put("orderId", order.getRefundNo());
		String txnTime = "";
		if (order.getRefundTime() != null) {
			txnTime = GlobalConstant.formatyyyymmddhhmmss(order.getRefundTime());
		} else {
			txnTime = GlobalConstant.formatyyyymmddhhmmss(order.getPayTime());
		}
		params.put("txnTime", txnTime); //****订单发送时间，每次发交易测试需修改为被查询的交易的订单发送时间
		logger.info("银联支付查询参数:{}", JSON.toJSONString(params));
		return params;
	}

	/**
	 * SDK支付
	 * @param order
	 * @param vo
	 * @return
	 */
	public static Map<String, Object> unionPaySDK(TradeOrder order, UnionPayVo vo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			//String cretificatePath = GlobalConstant.FILE_PATH.concat(vo.getCertificatePath());
			Map<String, String> submitFromData =
					AcpService.sign(unionPayGenertor(order, vo), vo.getCertificatePath(), vo.getPayKey(), TradeConstant.INPUT_CHARSET);
			submitFromData = AcpService.post(submitFromData, UNION_APP_TRANS_URL, TradeConstant.INPUT_CHARSET);
			resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
			resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
			resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, true);
			resultMap.put(UnionPayUtils.TRADE_RETURN_URL, TradeConstant.getSynchroCallback(order.getId()));
			resultMap.put(GlobalConstant.TRADE_SUCCESS_DATA, submitFromData);
		} catch (Exception e) {
			resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, true);
			resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, true);
			resultMap.put(GlobalConstant.TRADE_FAIL_MSG, e);
			logger.error("订单号:{},银联SDK支付异常:{}", new Object[] { order.getTradeNo(), e });
		}
		return resultMap;
	}

	/************************************************银联sdk end******************************************************/

	/************************************************银联H5 start******************************************************/
	/**
	 * H5支付
	 * @param order
	 * @param vo
	 * @return
	 */
	public static Map<String, Object> unionPayH5(TradeOrder order, UnionPayVo vo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Map<String, String> submitFromData =
					AcpService.sign(unionPayGenertor(order, vo), vo.getCertificatePath(), vo.getPayKey(), TradeConstant.INPUT_CHARSET);
			resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
			resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, true);

			StringBuffer sbHtml = new StringBuffer();
			sbHtml.append("<form id=\"punchout_form\" name=\"punchout_form\" action=\"" + UNION_FRONTTRANSURL + "\" method=\"post\">");
			for (String key : submitFromData.keySet()) {
				sbHtml.append("<input type=\"hidden\" name=\"" + key + "\" value=\"" + submitFromData.get(key) + "\"/>");
			}
			//submit按钮控件请不要含有name属性
			sbHtml.append("</form>");
			//sbHtml.append("<script>document.forms['unionpaysubmit'].submit();</script>");
			resultMap.put(GlobalConstant.TRADE_SUCCESS_DATA, sbHtml.toString());
		} catch (Exception e) {
			resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, true);
			resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, true);
			resultMap.put(GlobalConstant.TRADE_FAIL_MSG, e);
			logger.error("订单号:{},银联H5支付异常:{}", new Object[] { order.getTradeNo(), e });
		}
		return resultMap;
	}

	/**
	 * H5退费
	 * @param order
	 * @param vo
	 * @return
	 */
	public static Map<String, Object> unionRefundH5(TradeOrder order, UnionPayVo vo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Map<String, String> submitFromData =
					AcpService.sign(unionRefundGenertor(order, vo), vo.getCertificatePath(), vo.getPayKey(), TradeConstant.INPUT_CHARSET);
			Map<String, String> rspData = AcpService.post(submitFromData, UNION_BACK_TRANS_URL, TradeConstant.INPUT_CHARSET);

			if (!rspData.isEmpty()) {
				logger.info("平台订单号:{}, 商户订单号:{},  银联退费结果:{}", order.getTradeNo(), order.getOutTradeNo(), JSON.toJSONString(rspData));
				resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
				resultMap.put(GlobalConstant.TRADE_FAIL_MSG, rspData.get("respMsg"));
				if (UnionPayUtils.unionValidate(rspData)) {
					String respCode = rspData.get("respCode");
					if (UnionPayUtils.RESPCODE_SUCCESS.equals(respCode)) {//如果查询交易成功
						resultMap.put(GlobalConstant.TRADE_SUCCESS_AGTREFUNDNO, rspData.get("queryId"));
						resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, true);
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
				resultMap.put(GlobalConstant.TRADE_FAIL_MSG, "银联退费返回空");
				logger.info("平台订单号:{}, 商户订单号:{}, 银联退费返回空", order.getTradeNo(), order.getOutTradeNo());
			}

		} catch (Exception e) {
			resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, true);
			resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, true);
			resultMap.put(GlobalConstant.TRADE_FAIL_MSG, e);
			logger.error("订单号:{},银联H5退费异常:{}", new Object[] { order.getTradeNo(), e });
		}
		return resultMap;
	}

	/**
	 * H5支付查询
	 * @param order
	 * @param vo
	 * @return
	 */
	public static Map<String, Object> unionPayH5Query(TradeOrder order, UnionPayVo vo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Map<String, String> submitFromData =
					AcpService.sign(unionPayQueryGenertor(order, vo), vo.getCertificatePath(), vo.getPayKey(), TradeConstant.INPUT_CHARSET);
			Map<String, String> rspData = AcpService.post(submitFromData, UNION_SINGLE_QUERY_URL, TradeConstant.INPUT_CHARSET);

			if (!rspData.isEmpty()) {
				logger.info("平台订单号:{}, 商户订单号:{},  银联H5支付查询结果:{}", order.getTradeNo(), order.getOutTradeNo(), JSON.toJSONString(rspData));
				resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
				resultMap.put(GlobalConstant.TRADE_FAIL_MSG, rspData.get("respMsg"));
				if (UnionPayUtils.unionValidate(rspData)) {
					String respCode = rspData.get("respCode");
					String origRespCode = rspData.get("origRespCode");
					if (UnionPayUtils.RESPCODE_SUCCESS.equals(respCode) && UnionPayUtils.RESPCODE_SUCCESS.equals(origRespCode)) {//如果查询交易成功
						resultMap.put(GlobalConstant.TRADE_SUCCESS_AGTPAYNO, rspData.get("queryId"));
						resultMap.put(GlobalConstant.TRADE_ORDER_STATE, TradeConstant.TRADE_STATUS_PAYMENT);
						String txnTime = rspData.get("txnTime");
						resultMap.put(GlobalConstant.TRADE_DATE, GlobalConstant.parseyyyymmddhhmmss(txnTime));
						resultMap.put(GlobalConstant.TRADE_SUCCESS_PAY_TRACE_NO, rspData.get("traceNo"));// 返回银联系统跟踪号
						resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, true);
					} else {
						resultMap.put(GlobalConstant.TRADE_ORDER_STATE, TradeConstant.TRADE_STATUS_NO_PAYMENT);
						resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
					}
				} else {
					resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
					resultMap.put(GlobalConstant.TRADE_FAIL_MSG, "验签失败");
				}
				resultMap.put(GlobalConstant.TRADE_SUCCESS_DATA, JSON.toJSONString(rspData));
			} else {
				resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
				resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
				resultMap.put(GlobalConstant.TRADE_FAIL_MSG, "银联退费返回空");
				logger.info("平台订单号:{}, 商户订单号:{}, 银联H5支付查询返回空", order.getTradeNo(), order.getOutTradeNo());
			}
		} catch (Exception e) {
			resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, true);
			resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, true);
			resultMap.put(GlobalConstant.TRADE_FAIL_MSG, e);
			logger.error("订单号:{},银联H5支付查询异常:{}", new Object[] { order.getTradeNo(), e });
		}
		return resultMap;
	}

	/**
	 * H5退费查询
	 * @param order
	 * @param vo
	 * @return
	 */
	public static Map<String, Object> unionRefundH5Query(TradeOrder order, UnionPayVo vo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Map<String, String> submitFromData =
					AcpService.sign(unionPayQueryGenertor(order, vo), vo.getCertificatePath(), vo.getPayKey(), TradeConstant.INPUT_CHARSET);
			Map<String, String> rspData = AcpService.post(submitFromData, UNION_SINGLE_QUERY_URL, TradeConstant.INPUT_CHARSET);

			if (!rspData.isEmpty()) {
				logger.info("平台订单号:{}, 商户订单号:{},  银联退费查询结果:{}", order.getTradeNo(), order.getOutTradeNo(), JSON.toJSONString(rspData));
				resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
				resultMap.put(GlobalConstant.TRADE_FAIL_MSG, rspData.get("respMsg"));
				if (UnionPayUtils.unionValidate(rspData)) {
					String respCode = rspData.get("respCode");
					if (UnionPayUtils.RESPCODE_SUCCESS.equals(respCode)) {//如果查询交易成功
						resultMap.put(GlobalConstant.TRADE_SUCCESS_AGTREFUNDNO, rspData.get("queryId"));
						resultMap.put(GlobalConstant.TRADE_ORDER_STATE, TradeConstant.TRADE_STATUS_REFUND);
						resultMap.put(GlobalConstant.TRADE_DATE, GlobalConstant.formatYYYYMMDDHHMMSS(new Date()));
						String txnTime = rspData.get("txnTime");
						resultMap.put(GlobalConstant.TRADE_DATE, GlobalConstant.parseyyyymmddhhmmss(txnTime));
						resultMap.put(GlobalConstant.TRADE_SUCCESS_REFUND_TRACE_NO, rspData.get("traceNo"));// 返回银联系统跟踪号
						resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, true);
					} else {
						resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
					}
				} else {
					resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
					resultMap.put(GlobalConstant.TRADE_FAIL_MSG, "验签失败");
				}
				resultMap.put(GlobalConstant.TRADE_SUCCESS_DATA, JSON.toJSONString(rspData));
			} else {
				resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
				resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
				resultMap.put(GlobalConstant.TRADE_FAIL_MSG, "银联退费返回空");
				logger.info("平台订单号:{}, 商户订单号:{}, 银联退费查询返回空", order.getTradeNo(), order.getOutTradeNo());
			}
		} catch (Exception e) {
			resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, true);
			resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, true);
			resultMap.put(GlobalConstant.TRADE_FAIL_MSG, e);
			logger.error("订单号:{},银联退费查询异常:{}", new Object[] { order.getTradeNo(), e });
		}
		return resultMap;
	}

	/************************************************银联H5 end******************************************************/
}

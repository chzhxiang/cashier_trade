/**
 * <html>
 * <body>
 *  <P> Copyright 2017 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年10月19日</p>
 *  <p> Created by 党参</p>
 *  </body>
 * </html>
 */
package com.sunshine.payment;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.sunshine.common.GlobalConstant;
import com.sunshine.mobileapp.order.entity.PartialRefundOrder;
import com.sunshine.mobileapp.order.entity.TradeOrder;
import com.sunshine.payment.alipay.AlipayUtils;
import com.sunshine.payment.unionpay.UnionPayUtils;
import com.sunshine.payment.utils.InvokeDubboUtils;
import com.sunshine.payment.wechat.WechatUtils;
import com.sunshine.platform.TradeChannelConstants;
import com.sunshine.platform.channel.vo.AlipayVo;
import com.sunshine.platform.channel.vo.UnionPayVo;
import com.sunshine.platform.channel.vo.WechatVo;
import com.sunshine.restful.RestConstant;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.payment
 * @ClassName: TradeUtils
 * @Description: <p></p>
 * @JDK version used: 
 * @Author: 党参
 * @Create Date: 2017年10月19日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class TradeUtils {

	private static Logger logger = LoggerFactory.getLogger(TradeUtils.class);

	/**
	 * 支付第三方交易
	 * @param platformCode
	 * @param order
	 * @param paramsJson
	 * @return
	 */
	public static Map<String, Object> payment(TradeOrder order, String paramsJson) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		try {
			String platformCode = TradeChannelConstants.getChannelIdentityWithCode(order.getChannelCode());
			if (TradeChannelConstants.TRADE_WECHAT_IDENTITY.equals(platformCode)) {
				retMap = WechatUtils.wechatUnifiedorder(order, JSON.parseObject(paramsJson, WechatVo.class));
			} else if (TradeChannelConstants.TRADE_ALIPAY_IDENTITY.equals(platformCode)) {
				retMap = AlipayUtils.aliPayGenertor(order, JSON.parseObject(paramsJson, AlipayVo.class));
			} else if (TradeChannelConstants.TRADE_UNIONPAY_IDENTITY.equals(platformCode)) {
				retMap = UnionPayUtils.unionPay(order, buildUnionPay(order, paramsJson));
			} else {
				retMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
				retMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
			}
		} catch (Exception e) {
			retMap.put(GlobalConstant.TRADE_IS_EXCEPTION, true);
			retMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
			e.printStackTrace();
			logger.error("申请第三方支付出错:{}", e);
		}
		logger.info("申请第三方支付结果为:{}", JSON.toJSONString(retMap));
		return retMap;
	}

	/**
	 * 退费第三方交易
	 * @param platformCode
	 * @param order
	 * @param paramsJson
	 * @return
	 */
	public static Map<String, Object> refund(TradeOrder order, String paramsJson) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		try {
			String platformCode = TradeChannelConstants.getChannelIdentityWithCode(order.getChannelCode());
			if (TradeChannelConstants.TRADE_WECHAT_IDENTITY.equals(platformCode)) {
				retMap = WechatUtils.wechatRefund(order, JSON.parseObject(paramsJson, WechatVo.class));
			} else if (TradeChannelConstants.TRADE_ALIPAY_IDENTITY.equals(platformCode)) {
				retMap = AlipayUtils.aliRefundGenertor(order, JSON.parseObject(paramsJson, AlipayVo.class));
			} else if (TradeChannelConstants.TRADE_UNIONPAY_IDENTITY.equals(platformCode)) {
				retMap = UnionPayUtils.unionRefund(order, buildUnionPay(order, paramsJson));
			} else {
				retMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
				retMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
			}
		} catch (Exception e) {
			retMap.put(GlobalConstant.TRADE_IS_EXCEPTION, true);
			retMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
			e.printStackTrace();
			logger.error("申请第三方退费出错:{}", e);
		}
		logger.info("申请第三方退费结果为:{}", JSON.toJSONString(retMap));
		return retMap;
	}

	/**
	 * 部分退费第三方交易
	 * @param order
	 * @param paramsJson
	 * @param refundOrder
	 * @return
	 */
	public static Map<String, Object> partialRefund(TradeOrder order, String paramsJson, PartialRefundOrder partialRefundOrder) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		if (partialRefundOrder != null) {
			TradeOrder refundOrder = new TradeOrder();
			BeanUtils.copyProperties(order, refundOrder);
			refundOrder.setRefundDesc(partialRefundOrder.getRefundDesc());
			refundOrder.setRefundFee(partialRefundOrder.getRefundFee());
			refundOrder.setOutRefundNo(partialRefundOrder.getOutRefundNo());
			refundOrder.setRefundNo(partialRefundOrder.getRefundNo());
			retMap = refund(refundOrder, paramsJson);
		} else {
			logger.info("部分退费参数为空,平台支付订单号:{}, 商户支付订单号:{}", order.getTradeNo(), order.getOutTradeNo());
			retMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
			retMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
			retMap.put(GlobalConstant.TRADE_FAIL_MSG, "部分退费参数为空");
		}
		return retMap;
	}

	/**
	 * 支付查询第三方交易
	 * @param platformCode
	 * @param order
	 * @param paramsJson
	 * @return
	 */
	public static Map<String, Object> paymentQuery(TradeOrder order, String paramsJson) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		try {
			String platformCode = TradeChannelConstants.getChannelIdentityWithCode(order.getChannelCode());
			if (TradeChannelConstants.TRADE_WECHAT_IDENTITY.equals(platformCode)) {
				retMap = WechatUtils.orderQueryWechatPay(order, JSON.parseObject(paramsJson, WechatVo.class));
			} else if (TradeChannelConstants.TRADE_ALIPAY_IDENTITY.equals(platformCode)) {
				retMap = AlipayUtils.aliPayQueryGenertor(order, JSON.parseObject(paramsJson, AlipayVo.class));
			} else if (TradeChannelConstants.TRADE_UNIONPAY_IDENTITY.equals(platformCode)) {
				retMap = UnionPayUtils.unionPayQuery(order, buildUnionPay(order, paramsJson));
			} else {
				retMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
				retMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
			}
		} catch (Exception e) {
			retMap.put(GlobalConstant.TRADE_IS_EXCEPTION, true);
			retMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
			e.printStackTrace();
			logger.error("查询第三方支付出错:{}", e);
		}
		logger.info("查询第三方支付结果为:{}", JSON.toJSONString(retMap));
		return retMap;
	}

	/**
	 * 退费查询第三方交易
	 * @param platformCode
	 * @param order
	 * @param paramsJson
	 * @return
	 */
	public static Map<String, Object> refundQuery(TradeOrder order, String paramsJson) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		try {
			String platformCode = TradeChannelConstants.getChannelIdentityWithCode(order.getChannelCode());
			if (TradeChannelConstants.TRADE_WECHAT_IDENTITY.equals(platformCode)) {
				retMap = WechatUtils.orderQueryWechatRefund(order, JSON.parseObject(paramsJson, WechatVo.class));
			} else if (TradeChannelConstants.TRADE_ALIPAY_IDENTITY.equals(platformCode)) {
				retMap = AlipayUtils.orderQueryAlipayRefund(order, JSON.parseObject(paramsJson, AlipayVo.class));
			} else if (TradeChannelConstants.TRADE_UNIONPAY_IDENTITY.equals(platformCode)) {
				retMap = UnionPayUtils.unionRefundQuery(order, buildUnionPay(order, paramsJson));
			} else {
				retMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
				retMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
			}
		} catch (Exception e) {
			retMap.put(GlobalConstant.TRADE_IS_EXCEPTION, true);
			retMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
			e.printStackTrace();
			logger.error("查询第三方退费出错:{}", e);
		}
		logger.info("查询第三方退费结果为:{}", JSON.toJSONString(retMap));
		return retMap;
	}

	/**
	 * 关闭第三方交易
	 * @param order
	 * @param paramsJson
	 * @return
	 */
	public static Map<String, Object> orderClose(TradeOrder order, String paramsJson) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		try {
			String platformCode = TradeChannelConstants.getChannelIdentityWithCode(order.getChannelCode());
			if (TradeChannelConstants.TRADE_WECHAT_IDENTITY.equals(platformCode)) {
				retMap = WechatUtils.wechatClose(order, JSON.parseObject(paramsJson, WechatVo.class));
			} else if (TradeChannelConstants.TRADE_ALIPAY_IDENTITY.equals(platformCode)) {
				retMap = AlipayUtils.aliCloseGenertor(order, JSON.parseObject(paramsJson, AlipayVo.class));
			} else {
				retMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
				retMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
			}
		} catch (Exception e) {
			retMap.put(GlobalConstant.TRADE_IS_EXCEPTION, true);
			retMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
			e.printStackTrace();
			logger.error("关闭第三方退费出错:{}", e);
		}
		logger.info("关闭第三方订单结果为:{}", JSON.toJSONString(retMap));
		return retMap;
	}

	/**
	 * 构建透传参数
	 * @param order
	 * @return
	 */
	public static Map<String, Object> buildAttach(TradeOrder order) {
		Map<String, Object> attachMap = new HashMap<String, Object>();
		attachMap.put(GlobalConstant.MOTHED_INVOKE_RES_CASHIER_ID, order.getId());
		attachMap.put(RestConstant.PARAM_CHANNELCODE, order.getChannelCode());
		logger.info("构建透传参数:{}", JSON.toJSONString(attachMap));
		return attachMap;
	}

	/**
	 * 重新构建银联支付参数
	 * @param order
	 * @param paramsJson
	 * @return
	 */
	public static UnionPayVo buildUnionPay(TradeOrder order, String paramsJson) {
		UnionPayVo payVo = JSON.parseObject(paramsJson, UnionPayVo.class);
		if (!StringUtils.isEmpty(order.getHospitalId())) {//如果hospitalId不为空，标识需要通过dubbo获取支付信息，小额快捷、银联h5（大额支付）临时方案
			Map<String, String> payInfo = new HashMap<>();
			try {
				payInfo = InvokeDubboUtils.getPayInfo(order.getHospitalId());
			} catch (Exception e) {
				logger.error("获取支付参数异常:{}", e);
			}
			if (!payInfo.isEmpty()) {
				payVo.setMchId(payInfo.get("mchId"));
				payVo.setCertificatePath(GlobalConstant.FILE_PATH.concat(payInfo.get("certificatePath")));
				payVo.setPayKey(payInfo.get("payKey"));
			} else {
				logger.info("商户订单号:{}, 平台订单号:{}, 医院id:{},获取支付参数异常!", order.getOutTradeNo(), order.getTradeNo(), order.getHospitalId());
			}
		} else {
			payVo.setCertificatePath(GlobalConstant.FILE_PATH.concat(payVo.getCertificatePath()));
		}
		logger.info("商户订单号:{}, 平台订单号:{}, 重新构建银联支付参数:{}", order.getOutTradeNo(), order.getTradeNo(), JSON.toJSONString(payVo));
		return payVo;
	}

	public static void main(String[] args) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		TradeOrder order = new TradeOrder();
		order.setAgtTradeNo("2017111721001104120256499376");
		order.setTradeNo("1010209201711179998786");
		order.setRefundNo("1010209201711179998786");
		order.setTotalFee(50000);
		order.setRefundFee(44808);
		order.setRefundDesc("部分退费");
		/*WechatVo wechatVo = new WechatVo();
		wechatVo.setAppId("wx002d6538f5046519");
		wechatVo.setMchId("1423606002");
		wechatVo.setPaySecret("Gt0YbtwLxYqUXBdXNfZH5hYs6rZIolTb");
		wechatVo.setCertificatePath("D:\\certs\\1423606002\\apiclient_cert.p12");
		retMap = WechatUtils.wechatRefund(order, wechatVo);*/
		String privateKey =
				"MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALuDazxq2yXa44jTfY1trvdJXwmVcx/2IuvbJcQoicP6e9VSMR4ne8Ec61TiaHKNMV8ua9/baMNxgiZQqk+aIoCioYHkx9g0b3QoLbh0p6UM9jQ6HFhFh3Ws9OTmFC8LB76zJy0DQQ6YM6MZRsMVb15ZYlHb6LBZ2+CdZKeXUvyhAgMBAAECgYEApSivv1tvAvy1BMMjBvrg7TZW2x4jdLdbMtqnfQsnrNJnWBE46hJk0kcikHWLAbqTDf7ZTSb4M4060XyjRdHBKICeVFAIwWmOvGtQdnlmETvF3QRVohMAnI47110UQb4jlUA3jTN5t+4rMFJqEjU+UlYqWm58Rd9duhK4RvbwlPECQQDvVxgq1qusedqbRCEnmhs0PX5KoubFp/9S+Irf0VdLziRiZ8gWVFs7kUu7k05II4N0AtXSePc4kxLg0OMwWYnPAkEAyJDMr11TZ/BSh59FnqKFhxaDncEMIfcg/mT9iz0eO8TO3GhpJt3tGs5y1miFf3hrNJPCrBQmWrEEcYvLbjtejwI/NUovF1q03rJC+k8xEqA9bmRCuluLBndhYhf00+DIjxhXAZIZ+t7NcXXoZttlkqpVmn8HHNXSNcaJbYUVEK7fAkBtf8CALW+P24DzUl1sEkbcTcplojPBh26X9QMHtcJ/1x8o7kgaVCdS70t7C212/oNpHRK3Z/jYorROWYgjEn65AkEAub33KtjjwnbcrsKU2aaUKzRHTk3Uy68/OJn3HeR32ORPgW5yhUBFUc0DhCiNJ9kZMVkQ0kje8rvputCbNJPIxw==";
		AlipayVo vo = new AlipayVo();
		vo.setMchId("2016040701275174");
		vo.setPayPrivateKey(privateKey);
		retMap = AlipayUtils.aliRefundGenertor(order, vo);
		logger.info("retMap:{}", JSON.toJSONString(retMap));
	}
}

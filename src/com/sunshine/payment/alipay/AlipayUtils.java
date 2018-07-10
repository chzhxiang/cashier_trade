/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年9月27日</p>
 *  <p> Created by hqy</p>
 *  </body>
 * </html>
 */
package com.sunshine.payment.alipay;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeCloseModel;
import com.alipay.api.domain.AlipayTradeFastpayRefundQueryModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.domain.ExtendParams;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.sunshine.common.GlobalConstant;
import com.sunshine.mobileapp.order.entity.TradeOrder;
import com.sunshine.payment.TradeConstant;
import com.sunshine.payment.TradeUtils;
import com.sunshine.platform.TradeChannelConstants;
import com.sunshine.platform.channel.vo.AlipayVo;
import com.sunshine.restful.utils.RestUtils;

/**
 * @Project: cashier_trade
 * @Package: com.sunshine.payment.alipay
 * @ClassName: AlipayUtils
 * @Description:
 *               <p>
 *               支付宝支付相关工具类
 *               </p>
 * @JDK version used:
 * @Author: 百部
 * @Create Date: 2017年9月27日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class AlipayUtils {
	private static Logger logger = LoggerFactory.getLogger(AlipayUtils.class);

	/**
	 * 支付宝正式环境HTTPS请求地址
	 */
	public static final String OPENAPI_ALIPAY_COM = "https://openapi.alipay.com/gateway.do";

	/**
	 * 支付宝回调地址
	 */
	public static final String ALIPAY_NOTIFY_URL = "/notifyAlipay/callback/";

	/**
	 * 支付宝公钥 验签用
	 */
	public static final String ALI_PUBLIC_KEY =
			"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";

	/**
	 * 支付宝请求返回公共错误码 成功:1000
	 */
	public static final String ALIPAY_PUBLIC_CODE_10000 = "10000";

	/**
	 * 支付宝请求返回公共错误码 服务不可用:2000
	 */
	public static final String ALIPAY_PUBLIC_CODE_20000 = "20000";

	/**
	 * 支付宝请求返回公共错误码 授权权限不足:20001
	 */
	public static final String ALIPAY_PUBLIC_CODE_20001 = "20001";

	/**
	 * 支付宝请求返回公共错误码 缺少必选参数:40001
	 */
	public static final String ALIPAY_PUBLIC_CODE_40001 = "40001";

	/**
	 * 支付宝请求返回公共错误码 非法的参数:40002
	 */
	public static final String ALIPAY_PUBLIC_CODE_40002 = "40002";

	/**
	 * 支付宝请求返回公共错误码 业务处理失败:40004
	 */
	public static final String ALIPAY_PUBLIC_CODE_40004 = "40004";

	/**
	 * 支付宝请求返回公共错误码 权限不足:40006
	 */
	public static final String ALIPAY_PUBLIC_CODE_40006 = "40006";
	/**
	 * 交易创建，等待买家付款。
	 */
	public static final String STATUS_WAIT_BUYER_PAY = "WAIT_BUYER_PAY";

	/**
	 * 交易成功，且可对该交易做操作，如：多级分润、退款等。
	 */
	public static final String STATUS_TRADE_SUCCESS = "TRADE_SUCCESS";

	/**
	 * 等待卖家收款（买家付款后，如果卖家账号被冻结）。
	 */
	public static final String STATUS_TRADE_PENDING = "TRADE_PENDING";

	/**
	 * 在指定时间段内未支付时关闭的交易；在交易完成全额退款成功时关闭的交易。
	 */
	public static final String STATUS_TRADE_CLOSED = "TRADE_CLOSED";

	/**
	 * 交易成功且结束，即不可再做任何操作。
	 */
	public static final String STATUS_TRADE_FINISHED = "TRADE_FINISHED";

	/**
	 * 支付宝交易状态 异步返回时取值用
	 */
	public static Map<String, String> tradeStatusMap = new HashMap<String, String>();
	static {
		tradeStatusMap.put(STATUS_WAIT_BUYER_PAY, "交易创建，等待买家付款。");
		tradeStatusMap.put(STATUS_TRADE_CLOSED, "在指定时间段内未支付时关闭的交易；在交易完成全额退款成功时关闭的交易。");
		tradeStatusMap.put(STATUS_TRADE_SUCCESS, "交易成功，且可对该交易做操作，如：多级分润、退款等。");
		tradeStatusMap.put(STATUS_TRADE_PENDING, "等待卖家收款（买家付款后，如果卖家账号被冻结）。");
		tradeStatusMap.put(STATUS_TRADE_FINISHED, "交易成功且结束，即不可再做任何操作。");
	}

	public static Map<String, String> refundMap = new HashMap<String, String>();

	static {
		refundMap.put(STATUS_WAIT_BUYER_PAY, "等待买家付款");
		refundMap.put("WAIT_SYS_CONFIRM_PAY", "支付宝确认买家银行汇款中");
		refundMap.put("TRADE_REFUSE", "支付交易拒绝");
		refundMap.put("TRADE_REFUSE_DEALING", "支付交易拒绝中");
		refundMap.put("TRADE_CANCEL", "支付交易取消");
		refundMap.put("COD_WAIT_SELLER_SEND_GOODS", "等待卖家发货（货到付款）");
		refundMap.put("COD_WAIT_BUYER_PAY", "等待买家签收付款（货到付款）");
		refundMap.put("COD_WAIT_SYS_PAY_SELLER", "签收成功等待系统打款给卖家（货到付款）");
	}

	/**
	 * 支付宝支付
	 * @param order
	 * @param vo
	 * @return
	 */
	public static Map<String, Object> aliPayGenertor(TradeOrder order, AlipayVo vo) {
		if (TradeChannelConstants.TRADE_CHANNEL_ALIPAY_APP.equals(order.getChannelCode())) {
			return aliPayAppGenertor(order, vo);
		} else if (TradeChannelConstants.TRADE_CHANNEL_ALIPAY_H5.equals(order.getChannelCode())) {
			return aliPayH5Genertor(order, vo);
		} else {
			return null;
		}
	}

	/**
	 * 支付宝H5支付
	 * 
	 * @param order
	 * @param vo
	 * @return
	 */
	public static Map<String, Object> aliPayH5Genertor(TradeOrder order, AlipayVo vo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			AlipayClient alipayClient = new DefaultAlipayClient(OPENAPI_ALIPAY_COM, vo.getMchId(), vo.getPayPrivateKey());
			AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();// 创建API对应的request
			alipayRequest.setReturnUrl(TradeConstant.getSynchroCallback(order.getId()));
			//alipayRequest.setReturnUrl(order.getReturnUrl());
			alipayRequest.setNotifyUrl(RestUtils.getTradeDomainUrl().concat(ALIPAY_NOTIFY_URL));// 在公共参数中设置回跳和通知地址

			AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
			model.setOutTradeNo(order.getTradeNo());
			model.setSubject(order.getSubject());
			model.setTotalAmount(Double.valueOf(order.getTotalFee()) / 100.0 + "");
			model.setBody(order.getSubject());
			model.setProductCode("QUICK_WAP_PAY");
			model.setPassbackParams(JSON.toJSONString(TradeUtils.buildAttach(order)));
			model.setQuitUrl(TradeConstant.getSynchroCallback(order.getId()));
			ExtendParams extendParams = new ExtendParams();
			extendParams.setSysServiceProviderId("2088221619450222");
			model.setExtendParams(extendParams);
			/*SubMerchant subMerchant = new SubMerchant();
			subMerchant.setMerchantId("");
			model.setSubMerchant(subMerchant);*/

			alipayRequest.setBizModel(model);

			logger.info("支付宝支付请求参数:{}", JSON.toJSONString(alipayRequest));
			String html = alipayClient.pageExecute(alipayRequest).getBody();
			resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
			resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, true);
			resultMap.put(GlobalConstant.TRADE_SUCCESS_DATA, html);
		} catch (Exception e) {
			resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, true);
			resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
			resultMap.put(GlobalConstant.TRADE_FAIL_MSG, e.getMessage());
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 支付宝支付查询
	 * @param order
	 * @param vo
	 * @return
	 */
	public static Map<String, Object> aliPayQueryGenertor(TradeOrder order, AlipayVo vo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			AlipayClient client =
					new DefaultAlipayClient(OPENAPI_ALIPAY_COM, vo.getMchId(), vo.getPayPrivateKey(), "json", TradeConstant.INPUT_CHARSET,
							AlipayUtils.ALI_PUBLIC_KEY, TradeConstant.SIGN_TYPE_RSA);
			AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();// 创建API对应的request

			AlipayTradeQueryModel model = new AlipayTradeQueryModel();
			model.setOutTradeNo(order.getTradeNo());
			model.setTradeNo(order.getAgtTradeNo());
			request.setBizModel(model);
			AlipayTradeQueryResponse response = client.execute(request);
			resultMap.put(GlobalConstant.TRADE_FAIL_MSG, response.getMsg());
			if (ALIPAY_PUBLIC_CODE_10000.equals(response.getCode())) {
				if (STATUS_WAIT_BUYER_PAY.equalsIgnoreCase(response.getTradeStatus())) {
					resultMap.put(GlobalConstant.TRADE_ORDER_STATE, TradeConstant.TRADE_STATUS_NO_PAYMENT);
				} else if (STATUS_TRADE_CLOSED.equalsIgnoreCase(response.getTradeStatus())) {
					resultMap.put(GlobalConstant.TRADE_ORDER_STATE, TradeConstant.TRADE_STATUS_REFUND);
				} else if (STATUS_TRADE_SUCCESS.equalsIgnoreCase(response.getTradeStatus())) {
					resultMap.put(GlobalConstant.TRADE_ORDER_STATE, TradeConstant.TRADE_STATUS_PAYMENT);
				} else if (STATUS_TRADE_FINISHED.equalsIgnoreCase(response.getTradeStatus())) {
					resultMap.put(GlobalConstant.TRADE_ORDER_STATE, TradeConstant.TRADE_STATUS_CLOSE);
				}
				if (!StringUtils.isEmpty(response.getTradeNo())) {
					resultMap.put(GlobalConstant.TRADE_SUCCESS_AGTPAYNO, response.getTradeNo());
				}
				if (response.getSendPayDate() != null) {
					resultMap.put(GlobalConstant.TRADE_DATE, GlobalConstant.formatYYYYMMDDHHMMSS(response.getSendPayDate()));
				}
				resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, true);
			} else if (ALIPAY_PUBLIC_CODE_40004.equals(response.getCode())) {
				if ("ACQ.TRADE_NOT_EXIST".equals(response.getSubCode())) {//订单不存在
					resultMap.put(GlobalConstant.TRADE_ORDER_STATE, TradeConstant.TRADE_STATUS_NOT_EXIST);
					resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
					resultMap.put(GlobalConstant.TRADE_FAIL_MSG, response.getSubMsg());
				}
			} else {
				resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
			}
			resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);

			resultMap.put(GlobalConstant.TRADE_SUCCESS_DATA, JSON.toJSONString(response));
		} catch (Exception e) {
			resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, true);
			resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
			resultMap.put(GlobalConstant.TRADE_FAIL_MSG, e.getMessage());
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 支付宝退费
	 * 
	 * @param order
	 * @param vo
	 * @return
	 */
	public static Map<String, Object> aliRefundGenertor(TradeOrder order, AlipayVo vo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			AlipayClient client = new DefaultAlipayClient(OPENAPI_ALIPAY_COM, vo.getMchId(), vo.getPayPrivateKey());
			AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();// 创建API对应的request

			AlipayTradeRefundModel model = new AlipayTradeRefundModel();
			model.setOutTradeNo(order.getTradeNo());
			model.setTradeNo(order.getAgtTradeNo());
			model.setRefundAmount(Double.valueOf(order.getRefundFee()) / 100.0 + "");
			model.setRefundReason(order.getRefundDesc());
			model.setOutRequestNo(order.getRefundNo());
			request.setBizModel(model);

			AlipayTradeRefundResponse response = client.execute(request);
			logger.info("退费返回结果:{}", response.getBody());
			if (ALIPAY_PUBLIC_CODE_10000.equals(response.getCode())) {
				resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
				resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, true);
				resultMap.put(GlobalConstant.TRADE_SUCCESS_AGTREFUNDNO, order.getRefundNo());
			} else {
				resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
				resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
				resultMap.put(GlobalConstant.TRADE_FAIL_MSG, response.getMsg());
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
	 * 支付宝订单关闭
	 * @param order
	 * @param vo
	 * @return
	 */
	public static Map<String, Object> aliCloseGenertor(TradeOrder order, AlipayVo vo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			AlipayClient client = new DefaultAlipayClient(OPENAPI_ALIPAY_COM, vo.getMchId(), vo.getPayPrivateKey());
			AlipayTradeCloseRequest closeRequest = new AlipayTradeCloseRequest();
			AlipayTradeCloseModel closeModel = new AlipayTradeCloseModel();
			closeModel.setOutTradeNo(order.getTradeNo());
			closeRequest.setBizModel(closeModel);
			AlipayTradeCloseResponse response = client.execute(closeRequest);
			logger.info("支付宝关闭订单返回结果:{}", response.getBody());
			if (ALIPAY_PUBLIC_CODE_10000.equals(response.getCode())) {
				resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
				resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, true);
				resultMap.put(GlobalConstant.TRADE_SUCCESS_DATA, "");
			} else {
				resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
				resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
				resultMap.put(GlobalConstant.TRADE_FAIL_MSG, response.getMsg());
			}
		} catch (AlipayApiException e) {
			resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, true);
			resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
			resultMap.put(GlobalConstant.TRADE_FAIL_MSG, e.getMessage());
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 支付宝退费查询
	 * 
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年10月9日下午3:45:26
	 */
	public static Map<String, Object> orderQueryAlipayRefund(TradeOrder order, AlipayVo vo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			AlipayClient client =
					new DefaultAlipayClient(OPENAPI_ALIPAY_COM, vo.getMchId(), vo.getPayPrivateKey(), "json", TradeConstant.INPUT_CHARSET,
							AlipayUtils.ALI_PUBLIC_KEY, TradeConstant.SIGN_TYPE_RSA);
			AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();// 创建API对应的request
			AlipayTradeFastpayRefundQueryModel model = new AlipayTradeFastpayRefundQueryModel();
			model.setOutTradeNo(order.getTradeNo());
			model.setTradeNo(order.getAgtTradeNo());
			model.setOutRequestNo(order.getRefundNo());
			request.setBizModel(model);

			AlipayTradeFastpayRefundQueryResponse response = client.execute(request);
			logger.info("退费查询返回结果:{}", response.getBody());
			resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
			if (ALIPAY_PUBLIC_CODE_10000.equals(response.getCode())) {

				if (!StringUtils.isEmpty(response.getTradeNo())) {
					resultMap.put(GlobalConstant.TRADE_SUCCESS_AGTREFUNDNO, response.getTradeNo());
				}

				String refundAmount = response.getRefundAmount();
				if (refundAmount != null && !"".equals(refundAmount)) {

					Double refundAm = Double.parseDouble(refundAmount);
					if (refundAm > 0) {
						resultMap.put(GlobalConstant.TRADE_ORDER_STATE, TradeConstant.TRADE_STATUS_REFUND);
					} else {
						resultMap.put(GlobalConstant.TRADE_ORDER_STATE, TradeConstant.TRADE_STATUS_PAYMENT);
					}
					resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, true);
				} else {
					resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
				}

			} else {
				resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
			}
			resultMap.put(GlobalConstant.TRADE_FAIL_MSG, response.getMsg());
			resultMap.put(GlobalConstant.TRADE_SUCCESS_DATA, JSON.toJSONString(response));
		} catch (Exception e) {
			resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, true);
			resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
			resultMap.put(GlobalConstant.TRADE_FAIL_MSG, e.getMessage());
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 支付宝APP支付
	 * @param order
	 * @param vo
	 * @return
	 */
	public static Map<String, Object> aliPayAppGenertor(TradeOrder order, AlipayVo vo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			AlipayClient client =
					new DefaultAlipayClient(OPENAPI_ALIPAY_COM, vo.getMchId(), vo.getPayPrivateKey(), "json", TradeConstant.INPUT_CHARSET,
							AlipayUtils.ALI_PUBLIC_KEY, TradeConstant.SIGN_TYPE_RSA);
			AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
			request.setNotifyUrl(RestUtils.getTradeDomainUrl().concat(ALIPAY_NOTIFY_URL));// 在公共参数中设置回跳和通知地址

			AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
			model.setOutTradeNo(order.getTradeNo());
			model.setSubject(order.getSubject());
			model.setTotalAmount(Double.valueOf(order.getTotalFee()) / 100.0 + "");
			model.setBody(order.getSubject());
			model.setProductCode("QUICK_MSECURITY_PAY");
			model.setPassbackParams(JSON.toJSONString(TradeUtils.buildAttach(order)));

			request.setBizModel(model);
			logger.info("支付宝支付请求参数:{}", JSON.toJSONString(request));
			AlipayTradeAppPayResponse response = client.sdkExecute(request);
			resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
			resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, true);
			resultMap.put(GlobalConstant.TRADE_SUCCESS_DATA, response.getBody());
		} catch (Exception e) {
			resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, true);
			resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
			resultMap.put(GlobalConstant.TRADE_FAIL_MSG, e.getMessage());
			e.printStackTrace();
		}
		return resultMap;
	}
}

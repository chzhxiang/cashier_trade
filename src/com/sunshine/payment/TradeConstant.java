/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年9月15日</p>
 *  <p> Created by 赤芍</p>
 *  </body>
 * </html>
 */
package com.sunshine.payment;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.sunshine.common.GlobalConstant;
import com.sunshine.restful.utils.RestUtils;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.payment.constants
 * @ClassName: TradeConstant
 * @Description: <p>支付参数常量</p>
 * @JDK version used: 
 * @Author: 赤芍
 * @Create Date: 2017年9月15日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class TradeConstant {

	/**
	 * 1:待支付
	 */
	public final static int TRADE_STATUS_NO_PAYMENT = 1;

	/**
	 * 1:待支付
	 */
	public final static String TRADE_STATUS_NO_PAYMENT_MARK = "NO_PAYMENT";

	/**
	 * 1:待支付
	 */
	public final static String TRADE_STATUS_NO_PAYMENT_VIEW = "待支付";

	/**
	 * 2:已支付
	 */
	public final static int TRADE_STATUS_PAYMENT = 2;

	/**
	 * 2:已支付
	 */
	public final static String TRADE_STATUS_PAYMENT_MARK = "PAYMENT";

	/**
	 * 2:已支付
	 */
	public final static String TRADE_STATUS_PAYMENT_VIEW = "支付成功";

	/**
	 * 3:已退款
	 */
	public final static int TRADE_STATUS_REFUND = 3;

	/**
	 * 3:已退款
	 */
	public final static String TRADE_STATUS_REFUND_MARK = "REFUND";

	/**
	 * 3:已退款
	 */
	public final static String TRADE_STATUS_REFUND_VIEW = "退款成功";

	/**
	 * 4:支付中
	 */
	public final static int TRADE_STATUS_PAYMENTING = 4;

	/**
	 * 4:支付中
	 */
	public final static String TRADE_STATUS_PAYMENTING_MARK = "PAYMENTING";

	/**
	 * 4:支付中
	 */
	public final static String TRADE_STATUS_PAYMENTING_VIEW = "支付中";

	/**
	 * 5:退费中
	 */
	public final static int TRADE_STATUS_REFUNDING = 5;

	/**
	 * 5:退费中
	 */
	public final static String TRADE_STATUS_REFUNDING_MARK = "REFUNDING";

	/**
	 * 5:退费中
	 */
	public final static String TRADE_STATUS_REFUNDING_VIEW = "退款中";

	/**
	 * 6:关闭
	 */
	public final static int TRADE_STATUS_CLOSE = 6;

	/**
	 * 6:关闭
	 */
	public final static String TRADE_STATUS_CLOSE_MARK = "CLOSE";

	/**
	 * 6:关闭
	 */
	public final static String TRADE_STATUS_CLOSE_VIEW = "订单关闭";

	/**
	 * 7:部分退费
	 */
	public final static int TRADE_STATUS_PARTIAL_REFUND = 7;

	/**
	 * 7:部分退费
	 */
	public final static String TRADE_STATUS_PARTIAL_REFUND_MARK = "PARTIAL_REFUND";

	/**
	 * 7:部分退费
	 */
	public final static String TRADE_STATUS_PARTIAL_REFUND_VIEW = "部分退费";

	/**
	 * -1:订单不存在
	 */
	public final static int TRADE_STATUS_NOT_EXIST = -1;

	/**
	 * -1:订单不存在
	 */
	public final static String TRADE_STATUS_NOT_EXIST_MARK = "NOT_EXIST";

	/**
	 * -1:订单不存在
	 */
	public final static String TRADE_STATUS_NOT_EXIST_VIEW = "订单不存在";

	public static Map<Integer, String> tradeStatus = new HashMap<Integer, String>();

	static {
		tradeStatus.put(TRADE_STATUS_CLOSE, TRADE_STATUS_CLOSE_MARK);
		tradeStatus.put(TRADE_STATUS_NO_PAYMENT, TRADE_STATUS_NO_PAYMENT_MARK);
		tradeStatus.put(TRADE_STATUS_PAYMENT, TRADE_STATUS_PAYMENT_MARK);
		tradeStatus.put(TRADE_STATUS_PAYMENTING, TRADE_STATUS_PAYMENTING_MARK);
		tradeStatus.put(TRADE_STATUS_REFUND, TRADE_STATUS_REFUND_MARK);
		tradeStatus.put(TRADE_STATUS_REFUNDING, TRADE_STATUS_REFUNDING_MARK);
		tradeStatus.put(TRADE_STATUS_PARTIAL_REFUND, TRADE_STATUS_PARTIAL_REFUND_MARK);
	}

	/**
	 * 订单处理结果标识:成功
	 */
	public static final int ORDER_SUCCESS_TRUE = 1;

	/**
	 * 订单处理结果标识:失败
	 */
	public static final int ORDER_SUCCESS_FALSE = 0;

	/**
	 * 订单异常标识:异常
	 */
	public static final int ORDER_EXCEPTION_TRUE = 1;

	/**
	 * 订单异常标识:非异常
	 */
	public static final int ORDER_EXCEPTION_FALSE = 0;

	/**
	 * 订单回调标识:成功
	 */
	public static final int ORDER_NOTIFY_TRUE = 1;

	/**
	 * 订单回调标识:失败
	 */
	public static final int ORDER_NOTIFY_FALSE = 0;

	/**
	 * 订单支付查询标识:成功
	 */
	public static final int ORDER_PAY_QUERY_TRUE = 1;

	/**
	 * 订单支付查询标识:失败
	 */
	public static final int ORDER_PAY_QUERY_FALSE = 0;

	/**
	 * 退费标识:成功
	 */
	public static final int ORDER_REFUND_TRUE = 1;

	/**
	 * 退费标识:成功
	 */
	public static final String ORDER_REFUND_TRUE_MARK = "REFUND_SUCCESS";

	/**
	 * 退费标识:失败
	 */
	public static final int ORDER_REFUND_FALSE = 0;

	/**
	 * 退费标识:失败
	 */
	public static final String ORDER_REFUND_FALSE_MARK = "REFUND_FAIL";

	/**
	 * 退费标识:未发起退费
	 */
	public static final int ORDER_REFUND_NO_START = -1;

	/**
	 * 订单退费查询标识:成功
	 */
	public static final int ORDER_REFUND_QUERY_TRUE = 1;

	/**
	 * 订单退费查询标识:失败
	 */
	public static final int ORDER_REFUND_QUERY_FALSE = 0;

	/**
	 * 选择支付方式请求地址
	 */
	public static final String TRADE_CHOICE_PAYMENT_PATH = "/trade/cashierCenter";

	/**
	 * 同步回调地址
	 */
	public static final String TRADE_SYNCHRO_CALLBACK_URL = "/synchro/callback";
	/**
	 * 聚合二维码支付方式请求地址
	 */
	public static final String TRADE_QRCODE_PAYMENT_PATH = "/trade/qrCodeCenter";

	/**
	 * 签名类型-MD5
	 */
	public static final String SIGN_TYPE_MD5 = "MD5";

	/**
	 * 签名类型-RSA
	 */
	public static final String SIGN_TYPE_RSA = "RSA";

	/**
	 * 字符编码格式
	 */
	public final static String INPUT_CHARSET = "UTF-8";

	/**
	 * http请求返回成功状态值:200
	 */
	public final static int HTTP_IS_SUCCESS = 200;

	/**
	 * 获取同步回调地址
	 * @param cashierId
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getSynchroCallback(String cashierId) {
		String synchroParams = GlobalConstant.MOTHED_INVOKE_RES_CASHIER_ID.concat(GlobalConstant.STRING_ASSIGN_CHAR).concat(cashierId);
		String synchroUrl = TRADE_SYNCHRO_CALLBACK_URL.concat(GlobalConstant.STRING_QUESTION_MARK_CHAR).concat(synchroParams);
		return RestUtils.getTradeDomainUrl().concat(synchroUrl);
	}

	/**
	 * 获取支付跳转url
	 * @param channelCode
	 * @param cashierId
	 * @return
	 */
	public static String getPaymentUrl(String cashierId, String channelCode) {
		String queryStr =
				GlobalConstant.MOTHED_INVOKE_RES_CASHIER_ID.concat(GlobalConstant.STRING_ASSIGN_CHAR).concat(cashierId)
						.concat(GlobalConstant.STRING_AND_CHAR).concat(GlobalConstant.MOTHED_INVOKE_RES_CHANNEL_CODE)
						.concat(GlobalConstant.STRING_ASSIGN_CHAR).concat(channelCode);
		return RestUtils.getTradeDomainUrl().concat("/trade/payment/?").concat(queryStr);
	}

	/**
	 * 获取支付跳转url
	 * @param channelCode
	 * @param cashierId
	 * @return
	 */
	public static String getWechatPaymentUrl(String cashierId, String channelCode) {
		String queryStr =
				GlobalConstant.MOTHED_INVOKE_RES_CASHIER_ID.concat(GlobalConstant.STRING_ASSIGN_CHAR).concat(cashierId)
						.concat(GlobalConstant.STRING_AND_CHAR).concat(GlobalConstant.MOTHED_INVOKE_RES_CHANNEL_CODE)
						.concat(GlobalConstant.STRING_ASSIGN_CHAR).concat(channelCode);
		return RestUtils.WECHAT_TRADE_DOMAIN_NAME.concat("/trade/payment/?").concat(queryStr);
	}
}

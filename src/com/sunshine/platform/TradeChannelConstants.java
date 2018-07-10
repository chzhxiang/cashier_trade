/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年9月15日</p>
 *  <p> Created by hqy</p>
 *  </body>
 * </html>
 */
package com.sunshine.platform;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.platform
 * @ClassName: TradeChannelConstants
 * @Description: <p>支付渠道编码常量</p>
 * @JDK version used: 
 * @Author: 百部
 * @Create Date: 2017年9月15日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class TradeChannelConstants {

	/**
	 * 微信标识
	 */
	public final static String TRADE_WECHAT_IDENTITY = "wechat";

	/**
	 * 微信标识值
	 */
	public final static int TRADE_WECHAT_IDENTITY_VALUE = 1;

	/**
	 * 支付宝标识
	 */
	public final static String TRADE_ALIPAY_IDENTITY = "alipay";

	/**
	 * 支付宝标识值
	 */
	public final static int TRADE_ALIPAY_IDENTITY_VALUE = 2;

	/**
	 * 银联支付标识
	 */
	public final static String TRADE_UNIONPAY_IDENTITY = "unionpay";

	/**
	 * 银联支付标识值
	 */
	public final static int TRADE_UNIONPAY_IDENTITY_VALUE = 3;

	/**
	 * 阳光支付标识
	 */
	public final static String TRADE_SUNSHIEN_IDENTITY = "sunshien";

	/**
	 * 阳光支付标识值
	 */
	public final static int TRADE_SUNSHIEN_IDENTITY_VALUE = 4;

	/**
	 * 微信JSAPI（普通商户支付）
	 */
	public final static String TRADE_CHANNEL_WECHAT_JSAPI = TRADE_WECHAT_IDENTITY.concat("_jsapi");

	/**
	 * 微信JSAPI值（普通商户支付）
	 */
	public final static int TRADE_CHANNEL_WECHAT_JSAPI_VALUE = 1;

	/**
	 * 微信APP（普通商户支付）
	 */
	public final static String TRADE_CHANNEL_WECHAT_APP = TRADE_WECHAT_IDENTITY.concat("_app");

	/**
	 * 微信APP值（普通商户支付）
	 */
	public final static int TRADE_CHANNEL_WECHAT_APP_VALUE = 2;

	/**
	 * 微信医保
	 */
	public final static String TRADE_CHANNEL_WECHAT_INSTCARD = TRADE_WECHAT_IDENTITY.concat("_instcard");

	/**
	 * 微信医保值
	 */
	public final static int TRADE_CHANNEL_WECHAT_INSTCARD_VALUE = 3;

	/**
	 * 微信扫码
	 */
	public final static String TRADE_CHANNEL_WECHAT_SCAN_CODE = TRADE_WECHAT_IDENTITY.concat("_scan_code");

	/**
	 * 微信扫码值
	 */
	public final static int TRADE_CHANNEL_WECHAT_SCAN_CODE_VALUE = 4;

	/**
	 * 微信H5
	 */
	public final static String TRADE_CHANNEL_WECHAT_H5 = TRADE_WECHAT_IDENTITY.concat("_h5");

	/**
	 * 微信H5值
	 */
	public final static int TRADE_CHANNEL_WECHAT_H5_VALUE = 5;

	/**
	 * 微信JSAPI（特约商户支付）
	 */
	public final static String TRADE_CHANNEL_WECHAT_COMMON_JSAPI = TRADE_WECHAT_IDENTITY.concat("_common_jsapi");

	/**
	 * 微信JSAPI（特约商户支付）值
	 */
	public final static int TRADE_CHANNEL_WECHAT_COMMON_JSAPI_VALUE = 6;

	/**
	 * 微信APP（特约商户支付）
	 */
	public final static String TRADE_CHANNEL_WECHAT_COMMON_APP = TRADE_WECHAT_IDENTITY.concat("_common_app");

	/**
	 * 微信APP（特约商户支付）值
	 */
	public final static int TRADE_CHANNEL_WECHAT_COMMON_APP_VALUE = 7;

	/**
	 * 支付宝H5
	 */
	public final static String TRADE_CHANNEL_ALIPAY_H5 = TRADE_ALIPAY_IDENTITY.concat("_h5");

	/**
	 * 支付宝H5值
	 */
	public final static int TRADE_CHANNEL_ALIPAY_H5_VALUE = 8;

	/**
	 * 支付宝APP
	 */
	public final static String TRADE_CHANNEL_ALIPAY_APP = TRADE_ALIPAY_IDENTITY.concat("_app");

	/**
	 * 支付宝APP值
	 */
	public final static int TRADE_CHANNEL_ALIPAY_APP_VALUE = 9;

	/**
	 * 支付宝医保
	 */
	public final static String TRADE_CHANNEL_ALIPAY_INSTCARD = TRADE_ALIPAY_IDENTITY.concat("_instcard");

	/**
	 * 支付宝医保值
	 */
	public final static int TRADE_CHANNEL_ALIPAY_INSTCARD_VALUE = 10;

	/**
	 * 银联SDK支付
	 */
	public final static String TRADE_CHANNEL_UNIONPAY_SDK = TRADE_UNIONPAY_IDENTITY.concat("_sdk");

	/**
	 * 银联SDK支付值
	 */
	public final static int TRADE_CHANNEL_UNIONPAY_SDK_VALUE = 11;

	/**
	 * 银联applepay支付
	 */
	public final static String TRADE_CHANNEL_UNIONPAY_APPLE_PAY = TRADE_UNIONPAY_IDENTITY.concat("_apple_pay");

	/**
	 * 银联applepay支付值
	 */
	public final static int TRADE_CHANNEL_UNIONPAY_APPLE_PAY_VALUE = 12;

	/**
	 * 银联H5支付
	 */
	public final static String TRADE_CHANNEL_UNIONPAY_H5 = TRADE_UNIONPAY_IDENTITY.concat("_h5");

	/**
	 * 银联H5支付值
	 */
	public final static int TRADE_CHANNEL_UNIONPAY_H5_VALUE = 13;

	/**
	 * 银联代收支付
	 */
	public final static String TRADE_CHANNEL_UNIONPAY_DS = TRADE_UNIONPAY_IDENTITY.concat("_ds");

	/**
	 * 银联代收支付值
	 */
	public final static int TRADE_CHANNEL_UNIONPAY_DS_VALUE = 14;

	/**
	 * 阳光康众钱包支付
	 */
	public final static String TRADE_CHANNEL_SUNSHIEN_WALLET = TRADE_SUNSHIEN_IDENTITY.concat("_wallet");

	/**
	 * 阳光康众钱包支付值
	 */
	public final static int TRADE_CHANNEL_SUNSHIEN_WALLET_VALUE = 15;

	/**
	 * 阳光康众医保支付
	 */
	public final static String TRADE_CHANNEL_SUNSHIEN_INSTCARD = TRADE_SUNSHIEN_IDENTITY.concat("_instcard");

	/**
	 * 阳光康众医保支付值
	 */
	public final static int TRADE_CHANNEL_SUNSHIEN_INSTCARD_VALUE = 16;

	/**
	 * 银联小额快捷
	 */
	public final static String TRADE_CHANNEL_UNIONPAY_SMALL_QUICK = TRADE_UNIONPAY_IDENTITY.concat("_small_quick");

	/**
	 * 银联小额快捷值
	 */
	public final static int TRADE_CHANNEL_UNIONPAY_SMALL_QUICK_VALUE = 17;

	/**
	 * 根据渠道编码获取支付平台标识
	 * @param channelCode
	 * @return
	 */
	public static String getChannelIdentityWithCode(String channelCode) {
		String result = "";
		if (channelCode.indexOf(TradeChannelConstants.TRADE_WECHAT_IDENTITY) != -1) {
			result = TradeChannelConstants.TRADE_WECHAT_IDENTITY;
		} else if (channelCode.indexOf(TradeChannelConstants.TRADE_ALIPAY_IDENTITY) != -1) {
			result = TradeChannelConstants.TRADE_ALIPAY_IDENTITY;
		} else if (channelCode.indexOf(TradeChannelConstants.TRADE_UNIONPAY_IDENTITY) != -1) {
			result = TradeChannelConstants.TRADE_UNIONPAY_IDENTITY;
		} else if (channelCode.indexOf(TradeChannelConstants.TRADE_SUNSHIEN_IDENTITY) != -1) {
			result = TradeChannelConstants.TRADE_SUNSHIEN_IDENTITY;
		} else {
			result = "无法识别的支付平台";
		}
		return result;
	}

}

/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2016年12月30日</p>
 *  <p> Created by 党参</p>
 *  </body>
 * </html>
 */
package com.sunshine.payment.yctWallet;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.open.sdk.java.tools.SignatureUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.sunshine.common.GlobalConstant;
import com.sunshine.common.datas.cache.CacheConstants;
import com.sunshine.framework.cache.redis.RedisService;
import com.sunshine.framework.common.spring.ext.SpringContextHolder;
import com.sunshine.framework.config.SystemConfig;
import com.sunshine.mobileapp.order.entity.TradeOrder;
import com.sunshine.payment.TradeConstant;
import com.sunshine.platform.channel.vo.YctWalletVo;

/**
 * @Project: sunshine_trade
 * @Package: com.sunshine.trade.payrefund.utils.yctWallet
 * @ClassName: HttpTest
 * @Description:
 *               <p>
 *               </p>
 * @JDK version used:
 * @Author: 党参
 * @Create Date: 2016年12月30日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class YctWalletUtils {
	private static Logger logger = LoggerFactory.getLogger(YctWalletUtils.class);

	/**
	 * 医程通钱包accessToken缓存键前缀
	 */
	public static final String YCT_WALLET_ACCESS_TOKEN_CACHE = "yct.wallet.access.token.cache";

	/**
	 * 医程通钱包重定向地址缓存键前缀
	 */
	public static final String YCT_WALLET_REDIRECT_URL_CACHE = "wallet.redirect.url.cache";

	/**
	 * 医程通钱包accessToken缓存有效时常
	 */
	public static final int YCT_WALLET_ACCESS_TOKEN_EXPIRE_IN = 7000;

	/**
	 * 请求地址域名
	 */
	public static final String YCT_WALLET_APP_URL = SystemConfig.getStringValue("yct_wallet_app_url");

	/**
	 * 获取accessToken接口网端
	 */
	public static final String SERVICE_ACCESS_TOKEN_URL = YCT_WALLET_APP_URL.concat("/auth/service_access_token");

	/**
	 * 获取URL接口网端
	 */
	public static final String WIDGET_PAGE_URL = YCT_WALLET_APP_URL.concat("/service/widget_page");

	/**
	 * 获取收银台页面URL接口网端
	 */
	public static final String CASH_DESK_URL = YCT_WALLET_APP_URL.concat("/service/cash_desk");

	/**
	 * 获取会员余额网端
	 */
	public static final String QUERY_CUSTOMER_BLANCE = YCT_WALLET_APP_URL.concat("/service/queryCustomerBlance");

	/**
	 * 获取会员预付费账户余额网端
	 */
	public static final String QUERY_CUSTOMER_PREPAID = YCT_WALLET_APP_URL.concat("/service/general/pp/queryCustomerCardList");

	/**
	 * 支付结果查询
	 */
	public static final String PAY_RESULT = YCT_WALLET_APP_URL.concat("/service/pay_result");

	/**
	 * 退费接口网端
	 */
	public static final String REFUND_URL = YCT_WALLET_APP_URL.concat("/service/refund");

	/**
	 * 修改密码网端
	 */
	public static final String MODIFY_TRADE_PASS_WORD_URL = YCT_WALLET_APP_URL.concat("/widget/info");

	/**
	 * 获取预付费网页URL 1.获取购买预付卡页面URL：buyPrepaidCardGenertor
	 * 2.获取用户卡包页面：getUserCardPackGenertor 3.
	 */
	public static final String PREPAYMENT_WIDGET_URL = YCT_WALLET_APP_URL.concat("/service/prepayment_widget");

	/**
	 * 预付费 POST 1.获取预付费充值页面:prepaidRechargePageGenertor 2.
	 */
	public static final String GENERAL_POST_URL = YCT_WALLET_APP_URL.concat("/service/general_post");

	/**
	 * 预付费 GET 1.获取资产以及昨日收益累计收益:getAssetsAndIncomeGenertor
	 * 2.获取预付账户充值订单状态:prepaidRechargeStatusGenertor
	 */
	public static final String GENERAL_GET_URL = YCT_WALLET_APP_URL.concat("/service/general_get");

	/**
	 * 获取充值页面标识 code
	 */
	public static final String WIDGET_PAGE_TYPE_RECHARGE = "recharge";

	/**
	 * 获取提现页面标识 code
	 */
	public static final String WIDGET_PAGE_TYPE_WITHDRAWALS = "withdrawals";

	/**
	 * 获取我的银行卡页面标识 code
	 */
	public static final String WIDGET_PAGE_TYPE_BANK_CARD = "bankCard";

	/**
	 * 获取绑卡页面标识 code
	 */
	public static final String WIDGET_PAGE_TYPE_BIND_CARD = "bindCard";

	/**
	 * 获取用户卡包页面 code
	 */
	public static final String WIDGET_PAGE_TYPE_GET_USER_CARD_PACK = "getUserCardPack";

	/**
	 * 获取交易记录页面标识 code
	 */
	public static final String WIDGET_PAGE_TYPE_TRANSACTION_RECORD = "transactionRecord";

	/**
	 * 修改交易密码
	 */
	public static final String WIDGET_PAGE_TYPE_MODIFY_TRADE_PASS_WORD = "modifyTradePassWord";

	/**
	 * 预付卡充值
	 */
	public static final String WIDGET_PAGE_TYPE_PREPAID_RECHARGE = "prepaidRecharge";

	/**
	 * 资产以及昨日收益累计收益
	 */
	public static final String WIDGET_PAGE_TYPE_INCOME = "income";

	/**
	 * 回调地址前缀
	 */
	public static final String NOTIFY_URL_PREFIX = SystemConfig.getStringValue("notify_url_prefix");

	/**
	 * 医程通钱包支付同步回调地址后缀
	 */
	public static final String YCT_WALLET_PAY_SYNC_URL_SUFFIX = SystemConfig.getStringValue("yct_wallet_pay_sync_url_suffix");

	/**
	 * 医程通钱包支付回调地址后缀
	 */
	public static final String YCT_WALLET_PAY_NOTIFY_URL_SUFFIX = SystemConfig.getStringValue("yct_wallet_pay_notify_url_suffix");

	/**
	 * 医程通钱包绑卡回调地址后缀
	 */
	public static final String YCT_WALLET_BINDCARD_NOTIFY_URL_SUFFIX = SystemConfig.getStringValue("yct_wallet_bindcard_notify_url_suffix");

	/**
	 * 医程通钱包存储回调地址后缀，此回调地址只为存储返回的结果 目前支持余额充值、余额提现、预付卡充值
	 */
	public static final String YCT_WALLET_STORE_NOTIFY_URL_SUFFIX = "yctWalletStoreNotify";

	/**
	 * 医程通钱包渠道号 预付卡特殊参数
	 */
	public static final String YCT_WALLET_CHANNEL_NO = SystemConfig.getStringValue("yct_wallet_channel_no");

	/**
	 * 医程通钱包充值/提现订单超时时间 单位:分钟
	 */
	public static final int YCT_ORDER_EXPIRE_TIME = 10;

	public static final String STATUS_CODE = "code";
	public static final String RESULT_DATA = "data";

	public static final String SUCCESS_CODE = "1";

	/**
	 * modify by 赤芍 2017年3月19日 中顺易没有回调结果，再次调用开通接口时，返回此代码，平台更新开通状态
	 */
	public static final String HAD_OPEN_CODE = "85060";

	/**
	 * 医程通钱包支付订单查询
	 * 
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年9月25日下午3:37:49
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> orderQueryYctWallet(TradeOrder order, YctWalletVo yctWalletVo) {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			yctWalletVo.setAccessToken(getYctWalletAccessToken(yctWalletVo));
			Map<String, String> params = YctWalletUtils.payResultGenertor(order, yctWalletVo);
			params.put("signature", SignatureUtils.signature(params, yctWalletVo.getPaySecret()));
			String result = WalletHC.doPost(YctWalletUtils.PAY_RESULT, params, TradeConstant.INPUT_CHARSET);
			logger.info("接口返回结果:{}", result);
			WalletResponse response = JSON.parseObject(result, WalletResponse.class);
			if (YctWalletUtils.SUCCESS_CODE.equalsIgnoreCase(response.getCode())) {
				resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
				resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, true);
				resultMap.put(GlobalConstant.TRADE_SUCCESS_DATA, response.getData());
				// 1:未支付;2:交易进行中;3:成功;4:失败;5:关闭
				String tradeStatus = ( (Map<String, Object>) response.getData() ).get("tradeStatus").toString();
				if ("1".equals(tradeStatus)) {
					resultMap.put(GlobalConstant.TRADE_ORDER_STATE, TradeConstant.TRADE_STATUS_NO_PAYMENT);

				} else if ("2".equals(tradeStatus)) {
					resultMap.put(GlobalConstant.TRADE_ORDER_STATE, TradeConstant.TRADE_STATUS_PAYMENTING);

				} else if ("3".equals(tradeStatus)) {
					resultMap.put(GlobalConstant.TRADE_ORDER_STATE, TradeConstant.TRADE_STATUS_PAYMENT);

				} else if ("4".equals(tradeStatus)) {
					resultMap.put(GlobalConstant.TRADE_ORDER_STATE, TradeConstant.TRADE_STATUS_CLOSE);

				} else {
					resultMap.put(GlobalConstant.TRADE_ORDER_STATE, TradeConstant.TRADE_STATUS_CLOSE);
				}
				resultMap.put(GlobalConstant.TRADE_FAIL_MSG, response.getMessage());
			} else {
				resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
				resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
				resultMap.put(GlobalConstant.TRADE_FAIL_MSG, response.getMessage());
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
	 * 构建 支付结果查询 Map集合
	 * 
	 * @param yctWalletPay
	 * @return
	 */
	public static Map<String, String> payResultGenertor(TradeOrder order, YctWalletVo yctWalletVo) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("appId", yctWalletVo.getAppId());
		params.put("merchantNo", yctWalletVo.getMerchantNo());
		params.put("accessToken", yctWalletVo.getAccessToken());
		params.put("timestamp", String.valueOf(System.currentTimeMillis()));
		params.put("outCustomerId", order.getUserId());
		params.put("sourceNo", yctWalletVo.getMerchantNo());
		params.put("outTradeNo", order.getTradeNo());
		logger.info("构建 支付结果查询 Map集合:{}", JSON.toJSONString(params));
		return params;
	}

	/**
	 * 构建 获取accessToken Map集合
	 * 
	 * @param walletPay
	 * @return
	 */
	public static Map<String, String> accessTokenGenertor(YctWalletVo yctWalletVo) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("appId", yctWalletVo.getAppId());
		params.put("scope", "ACCESSTOKEN");
		params.put("sourceNo", yctWalletVo.getMerchantNo());
		params.put("timestamp", String.valueOf(System.currentTimeMillis()));
		logger.info("构建 获取accessToken Map集合:{}", JSON.toJSONString(params));
		return params;
	}

	/**
	 * 通过HttpClien获取accessToken
	 * 
	 * @param walletPay
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getAccessTokenHttpClient(YctWalletVo yctWalletVo) {
		Map<String, String> params = accessTokenGenertor(yctWalletVo);
		params.put("signature", SignatureUtils.signature(params, yctWalletVo.getPaySecret()));
		String result = WalletHC.doPost(SERVICE_ACCESS_TOKEN_URL, params, TradeConstant.INPUT_CHARSET);
		WalletResponse response = JSON.parseObject(result, WalletResponse.class);
		if (SUCCESS_CODE.equalsIgnoreCase(response.getCode())) {
			return (Map<String, Object>) response.getData();
		} else {
			return null;
		}
	}

	/**
	 * 验签
	 * 
	 * @return
	 */
	public static boolean checkWalletSignature(Map<String, String> signMap, String appKey) {
		String signature = signMap.remove("signature");
		String str = SignatureUtils.signature(signMap, appKey);
		logger.info("签名字符串:{}, 验签字符串:{}", signature, str);
		return signature.equals(str);
	}

	/**
	 * 获取accessToken，如果缓存中不存在，则通过程序获取
	 * 
	 * @param cacheKey
	 * @param field
	 * @return
	 */
	public static String getYctWalletAccessToken(YctWalletVo yctWalletVo) {
		String accessToken = "";
		try {
			String key = YctWalletUtils.YCT_WALLET_ACCESS_TOKEN_CACHE.concat(CacheConstants.CACHE_KEY_SPLIT_CHAR).concat(yctWalletVo.getAppId());
			logger.info("缓存中accessToken键:{}", key);
			RedisService redisSvc = SpringContextHolder.getBean(RedisService.class);
			if (redisSvc.get(key) != null) {
				accessToken = redisSvc.get(key);
				logger.info("获取缓存中accessToken结果:{}", accessToken);
			} else {
				Map<String, Object> accessTokenMap = YctWalletUtils.getAccessTokenHttpClient(yctWalletVo);
				logger.info("通过接口获取accessToken结果:{}", JSON.toJSONString(accessTokenMap));
				accessToken = accessTokenMap.get("access_token").toString();
				redisSvc.set(key, accessToken);
				redisSvc.expire(key, YCT_WALLET_ACCESS_TOKEN_EXPIRE_IN);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return accessToken;
	}

	public static String datePulsMinute(int minute) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MINUTE, minute);
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateformat.format(c.getTime());
	}

}

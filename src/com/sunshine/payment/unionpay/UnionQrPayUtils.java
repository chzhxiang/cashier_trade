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

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sunshine.common.GlobalConstant;
import com.sunshine.common.OrderNoGenerator;
import com.sunshine.framework.cache.redis.RedisLock;
import com.sunshine.framework.cache.redis.RedisService;
import com.sunshine.framework.common.spring.ext.SpringContextHolder;
import com.sunshine.framework.config.SystemConfig;
import com.sunshine.framework.mvc.mongodb.vo.Condition;
import com.sunshine.framework.mvc.mongodb.vo.Restrictions;
import com.sunshine.framework.utils.DateUtils;
import com.sunshine.mobileapp.bankCard.entity.BankCard;
import com.sunshine.mobileapp.bankCard.service.BankCardService;
import com.sunshine.mobileapp.bankCard.service.impl.BankCardServiceImpl;
import com.sunshine.mobileapp.order.entity.TradeOrder;
import com.sunshine.payment.CardConstant;
import com.sunshine.payment.TradeConstant;
import com.sunshine.payment.utils.InvokeDubboUtils;
import com.sunshine.platform.RuleConstant;
import com.sunshine.platform.applicationchannel.service.impl.ApplicationChannelServiceImpl;
import com.sunshine.platform.channel.vo.UnionPayVo;
import com.sunshine.platform.rule.entity.SysRuleConfig;
import com.sunshine.platform.rule.service.SysRuleConfigService;
import com.sunshine.platform.rule.service.impl.SysRuleConfigServiceImpl;
import com.sunshine.restful.RestConstant;
import com.sunshine.restful.utils.RestUtils;

/**
 * @Project: cashier_trade
 * @Package: com.sunshine.payment.unionpay
 * @ClassName: 银联二维码支付工具类
 * @Description: <p></p>
 * @JDK version used: 
 * @Author: 熊胆
 * @Create Date: 2017年11月20日上午11:07:42
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class UnionQrPayUtils {

	private static Logger logger = LoggerFactory.getLogger(UnionQrPayUtils.class);

	private static BankCardService bankCardService = SpringContextHolder.getBean(BankCardServiceImpl.class);

	/**
	 * 银联商户申请消费二维码
	 */
	public static final String TXNSUBTYPE = "07";

	/**
	 * 银联消费二维码商户接入
	 */
	public static final String BIZTYPE = "000000";

	/**
	 * 银联主扫二维码生成回调地址
	 */
	public static final String QRCODE_NOTIFY_URL = "/notifyUnionPay/qrCode/callback/";

	/**
	 * 银联主扫二维码支付回调地址
	 */
	public static final String QRCODE_PAY_NOTIFY_URL = "/notifyUnionPay/payQrCode/callback/";

	/**
	 * 银联主扫二维码退费回调地址
	 */
	public static final String QRCODE_REFUND_NOTIFY_URL = "/notifyQrCode/callRefundback/";

	/**
	 * 银联后台请求地址
	 */
	public static final String UNION_BACKTRANSURL = SystemConfig.getStringValue("union_backTransUrl");

	/**
	 * c2b机构（银行）端交易地址
	 */
	public static final String QRC_B2C_ISSBACKTRANSURL = SystemConfig.getStringValue("qrc_B2c_IssBackTransUrl");

	/**
	 * 商户二维码订单查询
	 */
	public static final String QR_MER_QUERYTRANS_URL = SystemConfig.getStringValue("qr_mer_queryTrans_url");

	/**
	 * 银联二维码支付版本
	 */
	public static final String QR_VERSION = "1.0.0";

	/**
	 * 银联主扫二维码订单信息交易类型
	 */
	public static final String QR_ORDER_INFO = "0120000903";

	/**
	 * 银联二维码机构号
	 */
	public static final String QR_ISSCODE = SystemConfig.getStringValue("qr_issCode");

	/**
	 * 银联二维码支付证书
	 */
	public static final String QRPAY_SIGNCERTPATH = SystemConfig.getStringValue("qrPay_signCertPath");

	/**
	 * 银联二维码支付密码
	 */
	public static final String QRPAY_SIGNCERTPWD = SystemConfig.getStringValue("qrPay_signCertPwd");

	/**
	 * 银联主扫二维码支付交易类型
	 */
	public static final String QR_PAY_TYPE = "0130000903";

	/**
	 * 发送短信的手机
	 */
	public static final String LIMIT_PAY_MOBILE = SystemConfig.getStringValue("limit_pay_mobile");

	/**
	 * 小额快捷支付
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年10月20日下午5:40:26
	 */
	public static Map<String, Object> dealWithQrCode(TradeOrder order, UnionPayVo unionPayVo) {
		Map<String, Object> resultData = new HashMap<>();
		resultData = dealLimitPay(order, unionPayVo);
		return resultData;
	}

	/**
	 * 处理不同卡的支付情况
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年11月18日下午3:40:52
	 */
	public static Map<String, Object> dealLimitPay(TradeOrder order, UnionPayVo unionPayVo) {
		//获取余额预警阈值
		SysRuleConfigService sysRuleConfigService = SpringContextHolder.getBean(SysRuleConfigServiceImpl.class);
		SysRuleConfig sysRuleConfig = sysRuleConfigService.findByRuleCode(RuleConstant.BANKCARD_BALANCE_LIMIT_RULE);
		Double str = Double.parseDouble(sysRuleConfig.getRuleValue()) * 100;
		Integer ruleValue = str.intValue();

		Map<String, Object> resultData = new HashMap<>();
		List<Condition> conditions = new ArrayList<>();
		Integer totalFee = order.getTotalFee();
		Condition condition = null;
		if (totalFee.intValue() >= RuleConstant.ORDER_THRESHOLD_LIMIT) {
			condition = Restrictions.gte("singleQuota", RuleConstant.ORDER_THRESHOLD_LIMIT);
			conditions.add(condition);
		}
		condition = Restrictions.eq("activateStatus", CardConstant.ACTIVATE_STATUS_BUNDLING);
		conditions.add(condition);

		//获取除了日志之外的所有属性
		List<String> lists = new ArrayList<>();
		/** 获取目标对象中所有的Field */
		Field[] fields = BankCard.class.getDeclaredFields();
		/** 循环所有的Field */
		for (Field field : fields) {
			String attr = field.getName();
			if (!attr.equals("logs")) {
				lists.add(attr);
			}
		}
		logger.info("银行卡查询入参：{}", JSON.toJSONString(conditions));
		List<BankCard> cards = bankCardService.findByAndCondition(conditions, lists);
		logger.info("银行卡查询返回结果：{}", JSON.toJSONString(cards));
		if (cards.size() > 0) {
			int count = 0;
			Collections.shuffle(cards); //随机取值
			for (BankCard card : cards) {
				count++;
				Integer cardBalance = card.getCardBalance();
				if (cardBalance - totalFee >= 0) {

					if (totalFee.intValue() <= card.getSingleQuota().intValue()) {
						//修改金额
						String lockKey = cardBalance.toString();
						boolean isLock = false;
						RedisService redisService = SpringContextHolder.getBean(RedisService.class);
						RedisLock redisLock = new RedisLock(redisService.getRedisPool());

						try {
							do {
								//300000 毫秒数 5分钟
								isLock = redisLock.singleLock(lockKey, GlobalConstant.REDIS_PAY_LOCKED_TIME);
								logger.info("银行卡金额加锁ID:{},解锁时间:{}", lockKey, GlobalConstant.formatYYYYMMDDHHMMSS(new Date()));
							} while (!isLock);

							unionPayVo.setAccNo(card.getBankCardNo());
							unionPayVo.setAccName(card.getUserName());
							unionPayVo.setCardAttr(card.getBankCardType());
							unionPayVo.setMobile(card.getCardPhone());

							if (order.getAccountId() == null) {
								order.setAccountId(card.getAccountId());
							}

							if (order.getSourceIp() == null) {
								order.setSourceIp(card.getSourceIp());
							}

							if (order.getDeviceId() == null) {
								order.setDeviceId(card.getDeviceId());
							}
							order.setCardId(card.getId());

							//1、生成二维码
							Map<String, Object> qrCodeMap = unionBuildMerQrCodeService(order, unionPayVo);

							String respMsg = (String) qrCodeMap.get(GlobalConstant.TRADE_FAIL_MSG);
							if (!StringUtils.isEmpty(respMsg) && respMsg.indexOf("重复交易") != -1) {

								ApplicationChannelServiceImpl channelService = SpringContextHolder.getBean(ApplicationChannelServiceImpl.class);
								// 生成订单号
								int channelVal = channelService.channeCodeConvertVal(order.getChannelCode());
								int tradePlatformVal = channelService.getChannelIdentityWithValue(order.getChannelCode());
								order.setTradeNo(OrderNoGenerator.genOrderNo(OrderNoGenerator.BIZ_CODE_PAY, tradePlatformVal, channelVal));
								resultData.put(GlobalConstant.UNIONPAY_SMALL_QUICK_TRADENO, order.getTradeNo());
								qrCodeMap = unionBuildMerQrCodeService(order, unionPayVo);
							}

							String qrCode = (String) qrCodeMap.get("qrCode");

							if (qrCode != null && !"".equals(qrCode)) {

								//2、获取商户信息
								Map<String, Object> qrMerMap = unionOrderMessageService(qrCode, order);
								String txnNo = qrMerMap.get("txnNo").toString();
								if (txnNo != null && !"".equals(txnNo)) {

									logger.info("交易单号:{}", txnNo);
									Map<String, Object> params = new HashMap<>();
									params.put("txnNo", txnNo);

									//3、获取优惠信息

									//4、主扫二维码付款
									resultData = unionActiveQrPayService(params, order, unionPayVo);
									logger.info("银联主扫二维码支付结果:{}", JSON.toJSONString(resultData));
									String voucherNum = (String) resultData.get("voucherNum");
									if (voucherNum != null) {
										//减少金额
										BankCard updateBank = card;
										updateBank.setCardBalance(cardBalance - totalFee);
										bankCardService.updateCardForCacheByParams(updateBank);

										//状态置为支付中
										resultData.put(GlobalConstant.TRADE_ORDER_STATE, TradeConstant.TRADE_STATUS_PAYMENTING);
										resultData.put(GlobalConstant.UNIONPAY_CARDID, card.getId());
										break;
									} else {
										String failMsg = (String) resultData.get(GlobalConstant.TRADE_FAIL_MSG);
										String respCode = (String) resultData.get(GlobalConstant.TRADE_RESP_CODE);
										logger.info("银联主扫二维码支付异常,订单号：{}, 卡号：{},失败的原因：{},失败错误码：{}", order.getTradeNo(), card.getBankCardNo(), failMsg,
												respCode);
										if (!StringUtils.isEmpty(failMsg)) {
											String content = "";
											if (totalFee.intValue() < ruleValue.intValue()) {
												if ("33".equals(respCode) || "38".equals(respCode)) { //交易金额超限
													BankCard updateStatus = card;
													updateStatus.setActivateStatus(CardConstant.ACTIVATE_STATUS_PAYMENT_CONSTRAINT);
													bankCardService.updateCardForCacheByParams(updateStatus);
													content = "健康钱包：" + card.getUserName() + "的" + card.getBankCardNo() + "银行卡当日消费金额超过阀值，请登录后台核查，谢谢";
													/*content =
															"健康钱包：" + card.getUserName() + "的" + card.getBankCardNo() + "银行卡（日限额"
																	+ card.getSingleQuota() / 100 + "，月限额" + card.getMonthlyQuota() / 100
																	+ "）存在支付异常，请登录后台核查，谢谢";*/
													if (count == cards.size()) {
														//状态置为待支付
														resultData.put(GlobalConstant.TRADE_ORDER_STATE, TradeConstant.TRADE_STATUS_NO_PAYMENT);
													}
												} else if ("64".equals(respCode)) { //卡上的余额不足
													BankCard updateStatus = card;
													updateStatus.setActivateStatus(CardConstant.ACTIVATE_STATUS_BALANCE_NOTENOUGH);
													bankCardService.updateCardForCacheByParams(updateStatus);
													content = "健康钱包：" + card.getUserName() + "的" + card.getBankCardNo() + "银行卡余额不足，请登录后台核查，谢谢。";
												}
											} else {
												// 所有银行卡支付失败
												if (count == cards.size()) {
													if (totalFee.intValue() < RuleConstant.ORDER_THRESHOLD_LIMIT
															&& totalFee.intValue() >= ruleValue.intValue()) {
														content = "健康钱包：后台银行卡均支付失败，请登录后台核查，谢谢";
													}
													//状态置为支付中
													resultData.put(GlobalConstant.TRADE_ORDER_STATE, TradeConstant.TRADE_STATUS_PAYMENTING);
												}
											}

											if (!"".equals(content)) {
												String[] mobiles = LIMIT_PAY_MOBILE.split(",");
												for (String mobile : mobiles) {
													Map<String, Object> result = InvokeDubboUtils.sendMsgValidate(mobile, content);
													logger.info("发送短信返回的结果：{}，手机号：{}", JSON.toJSONString(result), mobile);
												}
											}
										}
										continue;
									}
								} else {
									logger.info("根据二维码获取交易单号出现异常！");
									resultData.put(GlobalConstant.TRADE_IS_SUCCESS, false);
									resultData.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
									resultData.put(GlobalConstant.TRADE_FAIL_MSG, "根据二维码获取交易单号出现异常！");
								}
							} else {
								logger.info("获取银联商户二维码异常！");
								resultData.put(GlobalConstant.TRADE_IS_SUCCESS, false);
								resultData.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
								resultData.put(GlobalConstant.TRADE_FAIL_MSG, "获取银联商户二维码异常！");
							}

						} catch (Exception e) {
							e.printStackTrace();
							logger.error("reids加锁异常的日志：{}", e);
						} finally {
							redisLock.singleUnlock(lockKey);
							logger.info("银行卡金额解锁ID:{},解锁时间:{}", lockKey, GlobalConstant.formatYYYYMMDDHHMMSS(new Date()));
						}

					} else {
						resultData.put(GlobalConstant.TRADE_FAIL_MSG, "银行卡金额超限！");
						resultData.put(GlobalConstant.TRADE_IS_SUCCESS, false);
						resultData.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
						String content = "";
						if (count == cards.size()) {
							if (totalFee.intValue() >= ruleValue.intValue() && totalFee.intValue() < RuleConstant.ORDER_THRESHOLD_LIMIT) {
								content = "健康钱包：后台银行卡均支付失败，请登录后台核查，谢谢";
							}
							//状态置为支付中
							resultData.put(GlobalConstant.TRADE_ORDER_STATE, TradeConstant.TRADE_STATUS_PAYMENTING);
						}
						if (!"".equals(content)) {
							String[] mobiles = LIMIT_PAY_MOBILE.split(",");
							for (String mobile : mobiles) {
								Map<String, Object> result = InvokeDubboUtils.sendMsgValidate(mobile, content);
								logger.info("发送短信返回的结果：{}，手机号：{}", JSON.toJSONString(result), mobile);
							}
						}
						continue;
					}
				} else { //卡上的余额不足
					BankCard updateStatus = card;
					updateStatus.setActivateStatus(CardConstant.ACTIVATE_STATUS_BALANCE_NOTENOUGH);
					bankCardService.updateCardForCacheByParams(updateStatus);

					String content = "健康钱包：" + card.getUserName() + "的" + card.getBankCardNo() + "银行卡余额不足，请登录后台核查，谢谢。";
					String[] mobiles = LIMIT_PAY_MOBILE.split(",");
					for (String mobile : mobiles) {
						Map<String, Object> result = InvokeDubboUtils.sendMsgValidate(mobile, content);
						logger.info("发送短信返回的结果：{}，手机号：{}", JSON.toJSONString(result), mobile);
					}
					logger.info("银行卡余额不足！,银行卡号：{}, 银行卡余额为：{}, 支付金额：{}", card.getBankCardNo(), cardBalance, totalFee);
					resultData.put(GlobalConstant.TRADE_IS_SUCCESS, false);
					resultData.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
					continue;
				}
			}
		} else {
			resultData.put(GlobalConstant.TRADE_FAIL_MSG, "没有可以支付的银行卡！");
			resultData.put(GlobalConstant.TRADE_IS_SUCCESS, false);
			resultData.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
			String content = "";
			if (totalFee.intValue() >= ruleValue.intValue() && totalFee.intValue() < RuleConstant.ORDER_THRESHOLD_LIMIT) {
				content = "健康钱包：后台银行卡均支付失败，请登录后台核查，谢谢";
			}
			String[] mobiles = LIMIT_PAY_MOBILE.split(",");
			for (String mobile : mobiles) {
				Map<String, Object> result = InvokeDubboUtils.sendMsgValidate(mobile, content);
				logger.info("发送短信返回的结果：{}，手机号：{}", JSON.toJSONString(result), mobile);
			}
		}

		return resultData;
	}

	/**
	 * 商户银联主扫二维码生成
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年10月20日下午5:40:18
	 */
	public static Map<String, Object> unionBuildMerQrCodeService(TradeOrder order, UnionPayVo unionPayVo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			logger.info("订单号:{}, 商户银联二维码生成 start", order.getTradeNo());
			if (!StringUtils.isEmpty(unionPayVo.getCertificatePath())) {

				Map<String, String> requestData = unionBuildMerQrCodeGenertor(order, unionPayVo);
				requestData = AcpService.sign(requestData, unionPayVo.getCertificatePath(), unionPayVo.getPayKey(), TradeConstant.INPUT_CHARSET); //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
				Map<String, String> rspData = AcpService.post(requestData, UNION_BACKTRANSURL, TradeConstant.INPUT_CHARSET); //发送请求报文并接受同步应答（默认连接超时时间30秒，读取返回结果超时时间30秒）;这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过

				if (!rspData.isEmpty()) {
					logger.info("银联商户返回的主扫二维码:{}, 银联商户生成主扫二维码返回结果:{}", rspData.get("qrCode"), JSON.toJSONString(rspData));
					resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
					if (UnionPayUtils.unionValidate(rspData)) {
						logger.info("验证签名成功");
						if (UnionPayUtils.RESPCODE_SUCCESS.equals(rspData.get("respCode"))) {//如果查询交易成功
							resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, true);

							//处理被查询交易的应答码逻辑
							String qrCode = rspData.get("qrCode");
							resultMap.put("qrCode", qrCode);
						} else {
							resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
							resultMap.put(GlobalConstant.TRADE_FAIL_MSG, rspData.get("respMsg"));
						}
					} else {
						logger.info("验证签名失败");
						resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
						resultMap.put(GlobalConstant.TRADE_FAIL_MSG, "验签失败");
					}
				} else {
					//未返回正确的http状态
					logger.info("未获取到返回报文或返回http状态码非200");
					resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
					resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
				}

			} else {
				resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
				resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
				resultMap.put(GlobalConstant.TRADE_FAIL_MSG, "商户银联证书不能为空");
			}
		} catch (Exception e) {
			resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, true);
			resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
			e.printStackTrace();
			logger.error("获取二维码异常:{}", e);
		}
		return resultMap;
	}

	/**
	 * 银联商户主扫二维码参数拼装
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年10月20日下午5:40:11
	 */
	public static Map<String, String> unionBuildMerQrCodeGenertor(TradeOrder order, UnionPayVo unionPayVo) {
		Map<String, String> params = new HashMap<>();
		try {
			/***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
			params.put("version", UnionPayUtils.VERSION_5_1_0); //版本号 全渠道默认值
			params.put("encoding", TradeConstant.INPUT_CHARSET); //字符集编码 可以使用UTF-8,GBK两种方式
			params.put("signMethod", UnionPayUtils.SIGNMETHOD); //签名方法
			params.put("txnType", UnionPayUtils.TXNTYPE_CONSUME); //交易类型 01:消费
			params.put("txnSubType", TXNSUBTYPE); //交易子类 07：申请消费二维码
			params.put("bizType", BIZTYPE); //填写000000
			params.put("channelType", UnionPayUtils.CHANNELTYPE_PHONE); //渠道类型 08手机

			/***商户接入参数***/
			params.put("merId", unionPayVo.getMchId()); //商户号码，请改成自己申请的商户号或者open上注册得来的777商户号测试
			params.put("accessType", UnionPayUtils.ACCESSTYPE_DIRECT); //接入类型，商户接入填0 ，不需修改（0：直连商户， 1： 收单机构 2：平台商户）
			params.put("orderId", order.getTradeNo());//商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则

			params.put("txnTime", DateUtils.getCurrentTime()); //订单发送时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
			params.put("txnAmt", order.getTotalFee().toString()); //交易金额 单位为分，不能带小数点
			params.put("currencyCode", UnionPayUtils.CURRENCYCODE); //境内商户固定 156 人民币

			// 请求方保留域，
			// 透传字段，查询、通知、对账文件中均会原样出现，如有需要请启用并修改自己希望透传的数据。
			// 出现部分特殊字符时可能影响解析，请按下面建议的方式填写：
			// 1. 如果能确定内容不会出现&={}[]"'等符号时，可以直接填写数据，建议的方法如下。
			//		params.put("reqReserved", "透传信息1|透传信息2|透传信息3");
			// 2. 内容可能出现&={}[]"'符号时：
			// 1) 如果需要对账文件里能显示，可将字符替换成全角＆＝｛｝【】“‘字符（自己写代码，此处不演示）；
			// 2) 如果对账文件没有显示要求，可做一下base64（如下）。
			//    注意控制数据长度，实际传输的数据长度不能超过1024位。
			//    查询、通知等接口解析时使用new String(Base64.decodeBase64(reqReserved), DemoBase.encoding);解base64后再对数据做后续解析。

			Map<String, String> attachMap = new HashMap<String, String>();
			logger.info("银联商户主扫二维码透传参数:{}", JSON.toJSONString(attachMap));
			attachMap.put(GlobalConstant.MOTHED_INVOKE_RES_CASHIER_ID, order.getId());
			attachMap.put(RestConstant.PARAM_CHANNELCODE, order.getChannelCode());
			attachMap.put(GlobalConstant.UNIONPAY_CARDID, order.getCardId());
			String attach = URLEncoder.encode(JSON.toJSONString(attachMap), TradeConstant.INPUT_CHARSET);
			params.put("reqReserved", attach);

			//后台通知地址（需设置为外网能访问 http https均可），支付成功后银联会自动将异步通知报文post到商户上送的该地址，【支付失败的交易银联不会发送后台通知】
			//后台通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费交易 商户通知
			//注意:1.需设置为外网能访问，否则收不到通知    2.http https均可  3.收单后台通知后需要10秒内返回http200或302状态码 
			//    4.如果银联通知服务器发送通知后10秒内未收到返回状态码或者应答码非http200或302，那么银联会间隔一段时间再次发送。总共发送5次，银联后续间隔1、2、4、5 分钟后会再次通知。
			//    5.后台通知地址如果上送了带有？的参数，例如：http://abc/web?a=b&c=d 在后台通知处理程序验证签名之前需要编写逻辑将这些字段去掉再验签，否则将会验签失败
			params.put("backUrl", RestUtils.getTradeDomainUrl().concat(QRCODE_NOTIFY_URL));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return params;
	}

	/**
	 * 获取银联主扫二维码商户信息
	 * @param qrPay
	 * @return
	 */
	public static Map<String, Object> unionOrderMessageService(String qrCode, TradeOrder order) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Map<String, String> requestData = unionOrderMessageGenertor(qrCode);

			requestData = AcpService.sign(requestData, QRPAY_SIGNCERTPATH, QRPAY_SIGNCERTPWD, TradeConstant.INPUT_CHARSET); //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
			Map<String, String> rspData = AcpService.post(requestData, QRC_B2C_ISSBACKTRANSURL, TradeConstant.INPUT_CHARSET); //发送请求报文并接受同步应答（默认连接超时时间30秒，读取返回结果超时时间30秒）;这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过

			if (!rspData.isEmpty()) {
				logger.info("银联主扫二维码商户信息:{}, 银联主扫二维码商户信息返回结果:{}", rspData.get("txnNo"), JSON.toJSONString(rspData));
				resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
				if (UnionPayUtils.unionValidate(rspData)) {
					logger.info("验证签名成功");
					if (UnionPayUtils.RESPCODE_SUCCESS.equals(rspData.get("respCode"))) {//如果查询交易成功
						resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, true);

						//处理被查询交易的应答码逻辑
						String txnNo = rspData.get("txnNo");
						resultMap.put("txnNo", txnNo);
					} else {
						resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
						resultMap.put(GlobalConstant.TRADE_FAIL_MSG, rspData.get("respMsg"));
					}
				} else {
					logger.info("验证签名失败");
					resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
					resultMap.put(GlobalConstant.TRADE_FAIL_MSG, "验签失败");
				}
			} else {
				//未返回正确的http状态
				logger.info("未获取到返回报文或返回http状态码非200");
				resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
				resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
			}
		} catch (Exception e) {
			resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
			resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, true);
			e.printStackTrace();
			logger.error("银联主扫二维码商户信息异常:{}", e);
		}
		return resultMap;
	}

	/**
	 * 拼装获取银联主扫商户信息参数
	 * @return
	 */
	public static Map<String, String> unionOrderMessageGenertor(String qrCode) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("version", QR_VERSION);
		params.put("reqType", QR_ORDER_INFO);
		params.put("issCode", QR_ISSCODE);
		params.put("qrCode", qrCode);
		return params;
	}

	/**
	 * 银联主扫二维码付款
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年10月20日上午9:48:39
	 */
	public static Map<String, Object> unionActiveQrPayService(Map<String, Object> params, TradeOrder order, UnionPayVo unionPayVo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			logger.info("银联主扫二维码请求参数，订单信息：{}, 银联相关参数：{}", JSON.toJSONString(order), JSON.toJSONString(unionPayVo));
			Map<String, String> requestData = unionActiveQrPayGenertor(params, order, unionPayVo);
			requestData = AcpService.sign(requestData, QRPAY_SIGNCERTPATH, QRPAY_SIGNCERTPWD, TradeConstant.INPUT_CHARSET); //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
			Map<String, String> rspData = AcpService.post(requestData, QRC_B2C_ISSBACKTRANSURL, TradeConstant.INPUT_CHARSET); //发送请求报文并接受同步应答（默认连接超时时间30秒，读取返回结果超时时间30秒）;这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过

			if (!rspData.isEmpty()) {
				logger.info("银联主扫二维码付款凭证号:{}, 银联主扫二维码付款返回结果:{}", rspData.get("voucherNum"), JSON.toJSONString(rspData));

				resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
				if (UnionPayUtils.unionValidate(rspData)) {
					logger.info("银联主扫二维码付款验证签名成功");
					if (UnionPayUtils.RESPCODE_SUCCESS.equals(rspData.get("respCode"))) {//如果查询交易成功

						//处理被查询交易的应答码逻辑
						String voucherNum = rspData.get("voucherNum");
						resultMap.put("voucherNum", voucherNum);

						String couponOffstAmt = (String) params.get("couponOffstAmt");
						if (!StringUtils.isEmpty(couponOffstAmt)) {
							resultMap.put("origTxnAmt", order.getTotalFee());
							Integer realTxnAmt = order.getTotalFee() - Integer.parseInt(couponOffstAmt);
							resultMap.put("totalFee", realTxnAmt);
						}

						resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, true);
					} else {
						resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
						resultMap.put(GlobalConstant.TRADE_FAIL_MSG, rspData.get("respMsg"));
						resultMap.put(GlobalConstant.TRADE_RESP_CODE, rspData.get("respCode"));
					}
				} else {
					logger.info("银联主扫二维码付款验证签名失败");
					resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
					resultMap.put(GlobalConstant.TRADE_FAIL_MSG, "银联主扫二维码付款验签失败");
				}
			} else {
				//未返回正确的http状态
				logger.info("未获取到银联主扫二维码付款返回报文或返回http状态码非200");
				resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
				resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
			}

		} catch (Exception e) {
			resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
			resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, true);
			e.printStackTrace();
			logger.error("银联主扫二维码付款:{}", e);
		}
		return resultMap;
	}

	/**
	 * 拼装银联主扫二维码付款拼装
	 * @param qrPay
	 * @return
	 */
	public static Map<String, String> unionActiveQrPayGenertor(Map<String, Object> params, TradeOrder order, UnionPayVo unionPayVo) {
		Map<String, String> result = new HashMap<String, String>();
		try {
			result.put("version", QR_VERSION);
			result.put("reqType", QR_PAY_TYPE);
			result.put("issCode", QR_ISSCODE);
			result.put("txnNo", params.get("txnNo").toString());

			String couponOffstAmt = (String) params.get("couponOffstAmt");
			if (StringUtils.isEmpty(couponOffstAmt)) {
				result.put("txnAmt", order.getTotalFee().toString());

			} else {
				String couponInfo = params.get("couponInfo").toString();
				JSONObject couponInfoMap = JSONObject.parseObject(couponInfo);
				JSONArray jsonArray = new JSONArray();
				jsonArray.add(couponInfoMap);

				result.put("couponInfo", AcpService.base64Encode(jsonArray.toString(), TradeConstant.INPUT_CHARSET));

				result.put("origTxnAmt", order.getTotalFee().toString()); //初始金额

				Integer realTxnAmt = order.getTotalFee() - Integer.parseInt(couponOffstAmt);
				result.put("txnAmt", realTxnAmt.toString());
			}

			result.put("currencyCode", UnionPayUtils.CURRENCYCODE);

			Map<String, String> payerInfoMap = new HashMap<String, String>();

			payerInfoMap.put("accNo", unionPayVo.getAccNo());
			payerInfoMap.put("name", unionPayVo.getAccName()); //付款方名称
			payerInfoMap.put("issCode", QR_ISSCODE);//目前不校验
			//payerInfoMap.put("acctClass", qrPay.getAcctClass());
			payerInfoMap.put("cardAttr", unionPayVo.getCardAttr());//01 – 借记卡 02 – 贷记卡（含准贷记卡）
			// 付款方敏感信息
			if (GlobalConstant.isPayTestSign()) { //测试
				result.put("payerInfo", UnionPayUtils.getPayerInfo(payerInfoMap, TradeConstant.INPUT_CHARSET));
			} else {
				result.put("encryptCertId", AcpService.getEncryptCertId(UnionPayUtils.ENCRYPTCERT_PATH)); //加密证书的certId，配置在acp_sdk.properties文件 acpsdk.encryptCert.path属性下
				String payerInfoStr = UnionPayUtils.getPayerInfoWithEncrpyt(payerInfoMap, TradeConstant.INPUT_CHARSET);
				result.put("payerInfo", payerInfoStr);
			}
			result.put("backUrl", RestUtils.getTradeDomainUrl().concat(QRCODE_PAY_NOTIFY_URL));

			//风控字段
			Map<String, String> riskInfoMap = new HashMap<String, String>();
			riskInfoMap.put("deviceID", order.getDeviceId());
			riskInfoMap.put("mobile", unionPayVo.getMobile());
			riskInfoMap.put("accountIdHash", order.getAccountId());
			riskInfoMap.put("sourceIP", order.getSourceIp());

			result.put("riskInfo", UnionPayUtils.getRiskInfo(riskInfoMap, TradeConstant.INPUT_CHARSET));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 商户银联二维码退款
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年10月20日下午5:39:56
	 */
	public static Map<String, Object> unionQrCodeMerRefund(TradeOrder order, UnionPayVo unionPayVo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {

			Map<String, String> payInfo = InvokeDubboUtils.getPayInfo(order.getHospitalId());
			if (payInfo != null) {
				logger.info("银联商户参数返回结果: {}", JSON.toJSONString(payInfo));

				unionPayVo.setMchId(payInfo.get("mchId"));
				unionPayVo.setCertificatePath(GlobalConstant.FILE_PATH.concat(payInfo.get("certificatePath")));
				unionPayVo.setPayKey(payInfo.get("payKey"));

				logger.info("订单号:{}, 是否测试：{}, 商户银联二维码退款 start", order.getTradeNo(), GlobalConstant.isPayTestSign());

				if (!StringUtils.isEmpty(unionPayVo.getCertificatePath())) {

					Map<String, String> attachMap = new HashMap<String, String>();
					logger.info("原交易流水号:{}, 银联商户二维码退费透传参数:{}", order.getAgtTradeNo(), JSON.toJSONString(attachMap));
					order.setAttach(URLEncoder.encode(JSON.toJSONString(attachMap), TradeConstant.INPUT_CHARSET));

					Map<String, String> requestData = unionQrCodeMerRefundGenertor(order, unionPayVo);
					requestData = AcpService.sign(requestData, unionPayVo.getCertificatePath(), unionPayVo.getPayKey(), TradeConstant.INPUT_CHARSET);
					logger.info("订单号:{}, 商户银联二维码退款签名后参数:{}", order.getTradeNo(), JSON.toJSONString(requestData));
					String requestUrl = UNION_BACKTRANSURL;

					Map<String, String> rspData = AcpService.post(requestData, requestUrl, TradeConstant.INPUT_CHARSET);

					if (!rspData.isEmpty()) {
						logger.info("订单号:{}, 商户银联二维码退款结果:{}", order.getTradeNo(), JSON.toJSONString(rspData));
						resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
						resultMap.put(GlobalConstant.TRADE_FAIL_MSG, rspData.get("respMsg"));
						if (UnionPayUtils.unionValidate(rspData)) {
							String respCode = rspData.get("respCode");
							if (UnionPayUtils.RESPCODE_SUCCESS.equals(respCode)) {//如果查询交易成功
								//退还余额
								BankCard bankCard = bankCardService.findById(order.getCardId());
								bankCard.setCardBalance(bankCard.getCardBalance() + order.getRefundFee());
								bankCardService.updateCardForCacheByParams(bankCard);

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
						resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
						resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
						resultMap.put(GlobalConstant.TRADE_FAIL_MSG, "商户银联二维码退款返回空");
						logger.info("订单号:{}, 商户银联二维码退款返回空", order.getTradeNo());
					}

				} else {
					resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
					resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
					resultMap.put(GlobalConstant.TRADE_FAIL_MSG, "商户银联二维码退款加密证书不能为空");
				}
			} else {
				resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
				resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
				resultMap.put(GlobalConstant.TRADE_FAIL_MSG, "银联商户参数获取异常！");
			}
		} catch (Exception e) {
			resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
			resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, true);
			e.printStackTrace();
			logger.error("订单号:{} ,商户银联二维码退款异常:{}", order.getTradeNo(), e);
		}
		return resultMap;
	}

	/**
	 * 商户二维码退款参数组装
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年10月20日下午5:48:11
	 */
	public static Map<String, String> unionQrCodeMerRefundGenertor(TradeOrder order, UnionPayVo unionPayVo) {
		Map<String, String> params = new HashMap<String, String>();

		/***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
		params.put("version", UnionPayUtils.VERSION_5_1_0); //版本号
		params.put("encoding", TradeConstant.INPUT_CHARSET); //字符集编码 可以使用UTF-8,GBK两种方式
		params.put("signMethod", UnionPayUtils.SIGNMETHOD); //签名方法
		params.put("txnType", UnionPayUtils.TXNTYPE_REFUND); //交易类型 04-退货		
		params.put("txnSubType", UnionPayUtils.TXNTYPE_QUERY); //交易子类型  默认00		
		params.put("bizType", BIZTYPE); //填写000000
		params.put("channelType", UnionPayUtils.CHANNELTYPE_PHONE); //渠道类型，07-PC，08-手机		

		/***商户接入参数***/
		params.put("merId", unionPayVo.getMchId()); //商户号码，请改成自己申请的商户号或者open上注册得来的777商户号测试
		params.put("accessType", UnionPayUtils.ACCESSTYPE_DIRECT); //接入类型，商户接入固定填0，不需修改		
		params.put("orderId", order.getRefundNo()); //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则，重新产生，不同于原消费		
		params.put("txnTime", UnionPayUtils.getCurrentTime()); //订单发送时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效		
		params.put("currencyCode", UnionPayUtils.CURRENCYCODE); //交易币种（境内商户一般是156 人民币）		
		params.put("txnAmt", order.getRefundFee().toString()); //****退货金额，单位分，不要带小数点。退货金额小于等于原消费金额，当小于的时候可以多次退货至退货累计金额等于原消费金额		
		params.put("backUrl", RestUtils.getTradeDomainUrl().concat(QRCODE_REFUND_NOTIFY_URL)); //后台通知地址，后台通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 退货交易 商户通知,其他说明同消费交易的后台通知

		/***要调通交易以下字段必须修改***/
		params.put("origOrderId", order.getTradeNo());
		String date = DateUtils.dateToString(order.getPayTime());
		String payTime = DateUtils.formatDate(date, "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss");
		params.put("origTxnTime", payTime);

		// 请求方保留域，
		// 透传字段，查询、通知、对账文件中均会原样出现，如有需要请启用并修改自己希望透传的数据。
		// 出现部分特殊字符时可能影响解析，请按下面建议的方式填写：
		// 1. 如果能确定内容不会出现&={}[]"'等符号时，可以直接填写数据，建议的方法如下。
		//params.put("reqReserved", "透传信息1|透传信息2|透传信息3");
		// 2. 内容可能出现&={}[]"'符号时：
		// 1) 如果需要对账文件里能显示，可将字符替换成全角＆＝｛｝【】“‘字符（自己写代码，此处不演示）；
		// 2) 如果对账文件没有显示要求，可做一下base64（如下）。
		//    注意控制数据长度，实际传输的数据长度不能超过1024位。
		//    查询、通知等接口解析时使用new String(Base64.decodeBase64(reqReserved), DemoBase.encoding);解base64后再对数据做后续解析。
		params.put("reqReserved", order.getAttach());
		logger.info("商户二维码退款参数拼装: {}", JSON.toJSONString(params));
		return params;
	}

	/**
	 * 银联小额快捷支付（银联二维码）查询
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年10月20日下午5:50:26
	 */
	public static Map<String, Object> orderQueryServiceUnionQrPay(TradeOrder order, UnionPayVo unionPayVo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Map<String, String> payInfo = InvokeDubboUtils.getPayInfo(order.getHospitalId());

			if (payInfo != null) {
				logger.info("银联商户参数返回结果: {}", JSON.toJSONString(payInfo));

				unionPayVo.setMchId(payInfo.get("mchId"));
				unionPayVo.setCertificatePath(GlobalConstant.FILE_PATH.concat(payInfo.get("certificatePath")));
				unionPayVo.setPayKey(payInfo.get("payKey"));

				if (!StringUtils.isEmpty(unionPayVo.getCertificatePath())) {

					Map<String, String> requestData = unionQrPayQueryGenertor(order, unionPayVo);
					requestData = AcpService.sign(requestData, unionPayVo.getCertificatePath(), unionPayVo.getPayKey(), TradeConstant.INPUT_CHARSET);
					Map<String, String> rspData = AcpService.post(requestData, QR_MER_QUERYTRANS_URL, TradeConstant.INPUT_CHARSET);
					if (!rspData.isEmpty()) {
						logger.info("支付订单号:{}, 银联小额快捷支付查询返回结果:{}", order.getTradeNo(), JSON.toJSONString(rspData));
						resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
						if (UnionPayUtils.unionValidate(rspData)) {
							resultMap.put(GlobalConstant.TRADE_SUCCESS_DATA, JSON.toJSONString(rspData));
							resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, true);
							resultMap.put(GlobalConstant.TRADE_FAIL_MSG, rspData.get("respMsg"));
							resultMap.put(GlobalConstant.TRADE_ORDER_STATE, TradeConstant.TRADE_STATUS_NO_PAYMENT);

							if (UnionPayUtils.RESPCODE_SUCCESS.equals(rspData.get("respCode"))) {//如果查询交易成功
								//处理被查询交易的应答码逻辑
								String origRespCode = rspData.get("origRespCode");
								if (UnionPayUtils.RESPCODE_SUCCESS.equals(origRespCode)) {
									resultMap.put(GlobalConstant.TRADE_FAIL_MSG, rspData.get("origRespMsg"));
									if (rspData.get("origOrderId") != null) {
										resultMap.put(GlobalConstant.TRADE_ORDER_STATE, TradeConstant.TRADE_STATUS_REFUND);
										resultMap.put(GlobalConstant.TRADE_SUCCESS_AGTREFUNDNO, rspData.get("queryId"));// 写入支付订单号
									} else {
										resultMap.put(GlobalConstant.TRADE_ORDER_STATE, TradeConstant.TRADE_STATUS_PAYMENT);
										resultMap.put(GlobalConstant.TRADE_SUCCESS_AGTPAYNO, rspData.get("queryId"));// 写入支付订单号
										resultMap.put("traceNo", rspData.get("traceNo"));// 返回银联系统跟踪号

										// 返回银联支付时间
										String txnTime = rspData.get("txnTime");
										txnTime = DateUtils.formatDate(txnTime, "yyyyMMddHHmmss", "yyyy-MM-dd HH:mm:ss");
										resultMap.put(GlobalConstant.TRADE_DATE, txnTime);// 返回银联系统跟踪号
									}
								}
							}
						} else {
							resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
							resultMap.put(GlobalConstant.TRADE_FAIL_MSG, "验签失败");
						}
					} else {
						resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
						resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
					}
				}
			} else {
				resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
				resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
				resultMap.put(GlobalConstant.TRADE_FAIL_MSG, "银联商户参数获取异常！");
			}

		} catch (Exception e) {
			resultMap.put(GlobalConstant.TRADE_IS_EXCEPTION, true);
			resultMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
			logger.info("银联小额快捷支付查询错误日志：" + e.getMessage());
			logger.error("银联小额快捷支付查询:{}", e);
		}
		return resultMap;
	}

	/**
	 * 商户二维码查询
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年10月20日下午5:53:07
	 */
	public static Map<String, String> unionQrPayQueryGenertor(TradeOrder order, UnionPayVo unionPayVo) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("version", UnionPayUtils.VERSION); //版本号
		params.put("encoding", TradeConstant.INPUT_CHARSET); //字符集编码 可以使用UTF-8,GBK两种方式
		params.put("signMethod", UnionPayUtils.SIGNMETHOD); //签名方法
		params.put("txnType", UnionPayUtils.TXNTYPE_QUERY); //交易类型 00-默认
		params.put("txnSubType", UnionPayUtils.TXNTYPE_QUERY); //交易子类型  默认00
		params.put("bizType", UnionPayUtils.BIZTYPE_GATEWAY_MOBILE); //业务类型 

		/***商户接入参数***/
		params.put("merId", unionPayVo.getMchId()); //商户号码，请改成自己申请的商户号或者open上注册得来的777商户号测试
		params.put("accessType", UnionPayUtils.ACCESSTYPE_DIRECT); //接入类型，商户接入固定填0，不需修改

		/***要调通交易以下字段必须修改***/

		params.put("orderId", order.getTradeNo()); //****商户订单号，每次发交易测试需修改为被查询的交易的订单号

		String date = DateUtils.dateToString(order.getPayTime());
		String payTime = DateUtils.formatDate(date, "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss");
		params.put("txnTime", payTime); //****订单发送时间，每次发交易测试需修改为被查询的交易的订单发送时间
		logger.info("商户二维码查询拼装: {}", JSON.toJSONString(params));
		return params;
	}

}

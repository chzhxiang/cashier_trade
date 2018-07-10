/**
 * <html>
 * <body>
 *  <P> Copyright 2017 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年9月14日</p>
 *  <p> Created by 党参</p>
 *  </body>
 * </html>
 */
package com.sunshine.trade.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.sunshine.common.GlobalConstant;
import com.sunshine.common.OrderNoGenerator;
import com.sunshine.common.datas.cache.component.PaymentOvertimeExceptionCache;
import com.sunshine.common.datas.cache.component.RefundExceptionCache;
import com.sunshine.framework.cache.redis.RedisLock;
import com.sunshine.framework.cache.redis.RedisService;
import com.sunshine.framework.common.spring.ext.SpringContextHolder;
import com.sunshine.framework.utils.DateUtils;
import com.sunshine.mobileapp.bankCard.entity.BankCard;
import com.sunshine.mobileapp.bankCard.service.BankCardService;
import com.sunshine.mobileapp.bankCard.service.impl.BankCardServiceImpl;
import com.sunshine.mobileapp.order.entity.PartialRefundOrder;
import com.sunshine.mobileapp.order.entity.TradeOrder;
import com.sunshine.mobileapp.order.service.impl.TradeOrderServiceImpl;
import com.sunshine.payment.TradeConstant;
import com.sunshine.payment.TradeUtils;
import com.sunshine.platform.TradeChannelConstants;
import com.sunshine.platform.applicationchannel.entity.ApplicationChannel;
import com.sunshine.platform.applicationchannel.service.impl.ApplicationChannelServiceImpl;
import com.sunshine.restful.RestConstant;
import com.sunshine.restful.dto.request.RequestRefund;
import com.sunshine.trade.service.PayService;

/**
 * @Project: cashier_trade
 * @Package: com.sunshine.trade.service.impl
 * @ClassName: PayServiceImpl
 * @Description:
 *               <p>
 *               支付业务处理类
 *               </p>
 * @JDK version used:
 * @Author: 党参
 * @Create Date: 2017年9月14日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
@Service
public class PayServiceImpl implements PayService {
	private static Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);

	@Autowired
	private TradeOrderServiceImpl orderService;

	@Autowired
	private ApplicationChannelServiceImpl channelService;

	@Override
	public Map<String, Object> polymerizationPay(TradeOrder order, String channelCode) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		try {
			ApplicationChannel channel = channelService.getTradeChannel(order.getMerchantNo(), order.getAppId(), channelCode);
			String platformCode = channelService.getChannelIdentityWithCode(channelCode);
			if (TradeConstant.TRADE_STATUS_PAYMENTING == order.getTradeStatus()) {//支付中，确认是否已经支付
				Map<String, Object> queryMap = TradeUtils.paymentQuery(order, channel.getParamsJson());
				Boolean isException = (Boolean) queryMap.get(GlobalConstant.TRADE_IS_EXCEPTION);
				Boolean isSuccess = (Boolean) queryMap.get(GlobalConstant.TRADE_IS_SUCCESS);
				if (!isException && isSuccess) {
					order.setTradeStatus(Integer.parseInt(queryMap.get(GlobalConstant.TRADE_ORDER_STATE).toString()));
				}
			}

			if (TradeConstant.TRADE_STATUS_NO_PAYMENT == order.getTradeStatus() || TradeConstant.TRADE_STATUS_PAYMENTING == order.getTradeStatus()) {//订单未支付，执行支付流程

				// 生成订单号
				int channelVal = channelService.channeCodeConvertVal(channel.getChannelCode());
				int tradePlatformVal = channelService.getChannelIdentityWithValue(channel.getChannelCode());
				if (!StringUtils.isEmpty(order.getChannelCode()) && !channel.getChannelCode().equals(order.getChannelCode())) {//如果订单支付渠道不同，则更新订单号
					order.setTradeNo(OrderNoGenerator.genOrderNo(OrderNoGenerator.BIZ_CODE_PAY, tradePlatformVal, channelVal));
				} else {
					if (StringUtils.isEmpty(order.getTradeNo())) {
						order.setTradeNo(OrderNoGenerator.genOrderNo(OrderNoGenerator.BIZ_CODE_PAY, tradePlatformVal, channelVal));
					}
				}

				order.setChannelCode(channel.getChannelCode());
				order.setChannelName(channel.getChannelName());
				order.setPlatformCode(platformCode);

				retMap = TradeUtils.payment(order, channel.getParamsJson());

				StringBuffer content = new StringBuffer("支付申请请求,");
				Boolean isException = (Boolean) retMap.get(GlobalConstant.TRADE_IS_EXCEPTION);
				Boolean isSuccess = (Boolean) retMap.get(GlobalConstant.TRADE_IS_SUCCESS);
				if (isException) {
					order.setIsException(TradeConstant.ORDER_EXCEPTION_TRUE);
					content.append("发生异常，异常原因:").append(retMap.get(GlobalConstant.TRADE_FAIL_MSG));
				} else {
					order.setIsException(TradeConstant.ORDER_EXCEPTION_FALSE);
					if (isSuccess) {

						if (TradeChannelConstants.TRADE_CHANNEL_UNIONPAY_SMALL_QUICK.equalsIgnoreCase(order.getChannelCode())) {
							if (retMap.get(GlobalConstant.UNIONPAY_SMALL_QUICK_TRADENO) != null
									&& !"".equals(retMap.get(GlobalConstant.UNIONPAY_SMALL_QUICK_TRADENO))) {
								order.setTradeNo(retMap.get(GlobalConstant.UNIONPAY_SMALL_QUICK_TRADENO).toString());
							}

							if (retMap.get(GlobalConstant.UNIONPAY_CARDID) != null && !"".equals(retMap.get(GlobalConstant.UNIONPAY_CARDID))) {
								order.setCardId(retMap.get(GlobalConstant.UNIONPAY_CARDID).toString());
							}
						}

						retMap.put(GlobalConstant.MOTHED_INVOKE_RES_RETURN_URL, order.getReturnUrl());
						order.setTradeStatus(TradeConstant.TRADE_STATUS_PAYMENTING);// 设置订单为支付中
						order.setPayApplyTime(new Date());//设置支付申请时间
						content.append("成功，商户订单号:").append(order.getOutTradeNo()).append(",平台订单号:").append(order.getTradeNo()).append("支付渠道:")
								.append(channel.getChannelName());
						//写入支付超时异常队列
						PaymentOvertimeExceptionCache overtimeExceptionCache = SpringContextHolder.getBean(PaymentOvertimeExceptionCache.class);
						order.setIsSuccess(TradeConstant.ORDER_SUCCESS_FALSE);
						overtimeExceptionCache.setExceptionPayTradeOrderToCache(order.convertExceptionObj());
					} else {
						content.append("失败，失败原因:").append(retMap.get(GlobalConstant.TRADE_FAIL_MSG));
					}
				}
				order.formatHandleLog(content.toString());
			} else {
				retMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
				retMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
			}
			orderService.updateTradeOrderForDbAndCache(order);
			retMap.put(GlobalConstant.TRADE_ORDER_STATE, order.getTradeStatus());
			logger.info("支付请求结果返回:{}", JSON.toJSONString(retMap));
		} catch (Exception e) {
			retMap.put(GlobalConstant.TRADE_IS_EXCEPTION, true);
			retMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
			retMap.put(GlobalConstant.TRADE_FAIL_MSG, e.getMessage());
			e.printStackTrace();
		}

		return retMap;
	}

	@Override
	public Map<String, Object> polymerizationLimitPay(TradeOrder order, ApplicationChannel channel) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		try {
			String lockKey = order.getId();
			boolean isLock = false;
			RedisService redisService = SpringContextHolder.getBean(RedisService.class);
			RedisLock redisLock = new RedisLock(redisService.getRedisPool());
			try {
				do {
					//300000 毫秒数 5分钟
					isLock = redisLock.singleLock(lockKey, GlobalConstant.REDIS_PAY_LOCKED_TIME);
					logger.info("redisLock加锁ID:{},加锁时间:{}", lockKey, GlobalConstant.formatYYYYMMDDHHMMSS(new Date()));
				} while (!isLock);

				if (TradeConstant.TRADE_STATUS_PAYMENTING == order.getTradeStatus()) {//支付中，确认是否已经支付
					Map<String, Object> queryMap = TradeUtils.paymentQuery(order, channel.getParamsJson());
					Boolean isException = (Boolean) queryMap.get(GlobalConstant.TRADE_IS_EXCEPTION);
					Boolean isSuccess = (Boolean) queryMap.get(GlobalConstant.TRADE_IS_SUCCESS);
					if (!isException && isSuccess) {
						order.setTradeStatus(Integer.parseInt(queryMap.get(GlobalConstant.TRADE_ORDER_STATE).toString()));
					}
				}

				if (TradeConstant.TRADE_STATUS_NO_PAYMENT == order.getTradeStatus()) {//订单未支付，执行支付流程
					// 生成订单号
					int channelVal = channelService.channeCodeConvertVal(channel.getChannelCode());
					int tradePlatformVal = channelService.getChannelIdentityWithValue(channel.getChannelCode());
					if (StringUtils.isEmpty(order.getTradeNo())) {
						order.setTradeNo(OrderNoGenerator.genOrderNo(OrderNoGenerator.BIZ_CODE_PAY, tradePlatformVal, channelVal));
					}
					retMap = TradeUtils.payment(order, channel.getParamsJson());
					Boolean isException = (Boolean) retMap.get(GlobalConstant.TRADE_IS_EXCEPTION);
					Boolean isSuccess = (Boolean) retMap.get(GlobalConstant.TRADE_IS_SUCCESS);
					StringBuffer content = new StringBuffer("支付申请请求,");
					if (isException) {
						order.setIsException(TradeConstant.ORDER_EXCEPTION_TRUE);
						content.append("发生异常，异常原因:").append(retMap.get(GlobalConstant.TRADE_FAIL_MSG));
					} else {
						Integer tradeStatus = (Integer) retMap.get(GlobalConstant.TRADE_ORDER_STATE);
						if (tradeStatus != null) {
							order.setTradeStatus(tradeStatus);
						}
						order.setIsException(TradeConstant.ORDER_EXCEPTION_FALSE);
						if (isSuccess) {

							if (TradeChannelConstants.TRADE_CHANNEL_UNIONPAY_SMALL_QUICK.equalsIgnoreCase(order.getChannelCode())) {
								if (!StringUtils.isEmpty(retMap.get(GlobalConstant.UNIONPAY_SMALL_QUICK_TRADENO))) { //重复交易的问题
									order.setTradeNo(retMap.get(GlobalConstant.UNIONPAY_SMALL_QUICK_TRADENO).toString());
								}

								//添加银行卡信息
								String cardId = (String) retMap.get(GlobalConstant.UNIONPAY_CARDID);
								if (cardId != null) {
									order.setCardId(cardId);
									BankCardService bankCardService = SpringContextHolder.getBean(BankCardServiceImpl.class);
									BankCard bankCard = bankCardService.findById(cardId);
									order.setCardJson(JSON.toJSONString(bankCard));
									//order.setBankCard(bankCard);
								}
							}

							retMap.put(GlobalConstant.MOTHED_INVOKE_RES_RETURN_URL, order.getReturnUrl());
							order.setIsSuccess(TradeConstant.ORDER_SUCCESS_FALSE);
							order.setPayTime(new Date());
							content.append("成功，商户订单号:").append(order.getOutTradeNo()).append(",平台订单号:").append(order.getTradeNo()).append("支付渠道:")
									.append(channel.getChannelName());
							//写入支付超时异常队列
							PaymentOvertimeExceptionCache overtimeExceptionCache = SpringContextHolder.getBean(PaymentOvertimeExceptionCache.class);
							overtimeExceptionCache.setExceptionPayTradeOrderToCache(order.convertExceptionObj());
						} else {
							content.append("失败，失败原因:").append(retMap.get(GlobalConstant.TRADE_FAIL_MSG));
						}
					}
					order.formatHandleLog(content.toString());
				} else {
					retMap.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
					retMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
				}
				orderService.updateTradeOrderForDbAndCache(order);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				redisLock.singleUnlock(lockKey);
				logger.info("redisLock解锁ID:{},解锁时间:{}", lockKey, GlobalConstant.formatYYYYMMDDHHMMSS(new Date()));
			}
			retMap.put(GlobalConstant.TRADE_ORDER_STATE, order.getTradeStatus());
			logger.info("支付请求结果返回:{}", JSON.toJSONString(retMap));
		} catch (Exception e) {
			retMap.put(GlobalConstant.TRADE_IS_EXCEPTION, true);
			retMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
			retMap.put(GlobalConstant.TRADE_FAIL_MSG, e.getMessage());
			e.printStackTrace();
		}

		return retMap;
	}

	@Override
	public Map<String, Object> polymerizationRefund(TradeOrder order, RequestRefund refundParamsVo) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		try {
			ApplicationChannel channel = channelService.findTradeChannelByAppIdChannelCode(order.getAppId(), order.getChannelCode());
			if (StringUtils.isEmpty(order.getRefundNo())) {// 生成订单号
				int channelVal = channelService.channeCodeConvertVal(channel.getChannelCode());
				int tradePlatformVal = channelService.getChannelIdentityWithValue(channel.getChannelCode());
				order.setRefundNo(OrderNoGenerator.genOrderNo(OrderNoGenerator.BIZ_CODE_REFUND, tradePlatformVal, channelVal));
			}
			order.setRefundFee(refundParamsVo.getRefundFee());
			retMap = TradeUtils.refund(order, channel.getParamsJson());
			StringBuffer content = new StringBuffer("退费申请请求,");
			Boolean isException = (Boolean) retMap.get(GlobalConstant.TRADE_IS_EXCEPTION);
			Boolean isSuccess = (Boolean) retMap.get(GlobalConstant.TRADE_IS_SUCCESS);
			if (isException) {
				order.setIsException(TradeConstant.ORDER_EXCEPTION_TRUE);
				order.setIsRefund(TradeConstant.ORDER_REFUND_FALSE);
				content.append("发生异常，异常原因:").append(retMap.get(GlobalConstant.TRADE_FAIL_MSG));
				logger.info("订单退费请求申请失败！加入异常队列！");
				RefundExceptionCache refundExceptionCache = SpringContextHolder.getBean(RefundExceptionCache.class);
				refundExceptionCache.setRefundExceptionToCache(order.convertExceptionObj());
			} else {
				if (isSuccess) { // 退费申请成功处理
					String agtRefundNo = (String) retMap.get(GlobalConstant.TRADE_SUCCESS_AGTREFUNDNO);
					order.setIsException(TradeConstant.ORDER_EXCEPTION_FALSE);
					order.setAgtRefundNo(agtRefundNo);
					order.setOutRefundNo(refundParamsVo.getOutRefundNo());
					order.setRefundTime(DateUtils.StringToDate(refundParamsVo.getTimeStamp()));
					order.setTradeStatus(TradeConstant.TRADE_STATUS_REFUND); // 状态改为：已退费
					order.setIsRefund(TradeConstant.ORDER_REFUND_TRUE);
					content.append("成功，第三方退费订单号:").append(agtRefundNo).append("退费渠道:").append(channel.getChannelName());
				} else {
					content.append("失败，失败原因:").append(retMap.get(GlobalConstant.TRADE_FAIL_MSG));
				}
			}
			order.formatHandleLog(content.toString());
			orderService.updateTradeOrderForDbAndCache(order);
			retMap.put(GlobalConstant.TRADE_SUCCESS_DATA, JSON.toJSONString(order));
			logger.info("退费申请请求结果:{}", content);
		} catch (Exception e) {
			retMap.put(GlobalConstant.TRADE_IS_EXCEPTION, true);
			retMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
			retMap.put(GlobalConstant.TRADE_FAIL_MSG, RestConstant.RETURN_SYSTEMERROR[1]);
			RefundExceptionCache refundExceptionCache = SpringContextHolder.getBean(RefundExceptionCache.class);
			refundExceptionCache.setRefundExceptionToCache(order.convertExceptionObj());
			e.printStackTrace();
		}
		return retMap;
	}

	@Override
	public Map<String, Object> polymerizationPartialRefund(TradeOrder order, RequestRefund refundParamsVo) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		try {
			ApplicationChannel channel = channelService.findTradeChannelByAppIdChannelCode(order.getAppId(), order.getChannelCode());
			int channelVal = channelService.channeCodeConvertVal(channel.getChannelCode());
			int tradePlatformVal = channelService.getChannelIdentityWithValue(channel.getChannelCode());
			PartialRefundOrder refundOrder = new PartialRefundOrder();
			refundOrder.setRefundDesc(refundParamsVo.getRefundDesc());
			refundOrder.setRefundFee(refundParamsVo.getRefundFee());
			refundOrder.setOutRefundNo(refundParamsVo.getOutRefundNo());
			refundOrder.setRefundNo(OrderNoGenerator.genOrderNo(OrderNoGenerator.BIZ_CODE_REFUND, tradePlatformVal, channelVal));
			retMap = TradeUtils.partialRefund(order, channel.getParamsJson(), refundOrder);
			order.setTradeStatus(TradeConstant.TRADE_STATUS_PARTIAL_REFUND);
			StringBuffer content = new StringBuffer("部分退费申请请求,");
			Boolean isException = (Boolean) retMap.get(GlobalConstant.TRADE_IS_EXCEPTION);
			Boolean isSuccess = (Boolean) retMap.get(GlobalConstant.TRADE_IS_SUCCESS);
			if (isException) {
				order.setIsException(TradeConstant.ORDER_EXCEPTION_TRUE);
				order.setIsRefund(TradeConstant.ORDER_REFUND_FALSE);
				content.append("发生异常，异常原因:").append(retMap.get(GlobalConstant.TRADE_FAIL_MSG));
				refundOrder.setRefundStatus(TradeConstant.ORDER_REFUND_FALSE);
				refundOrder.setRefundDetail(retMap.get(GlobalConstant.TRADE_FAIL_MSG).toString());
				logger.info("订单退费请求申请失败！");
				/*logger.info("订单退费请求申请失败！加入异常队列！");
				RefundExceptionCache refundExceptionCache = SpringContextHolder.getBean(RefundExceptionCache.class);
				refundExceptionCache.setRefundExceptionToCache(order.convertExceptionObj());*/
			} else {
				if (isSuccess) { // 退费申请成功处理
					order.setIsException(TradeConstant.ORDER_EXCEPTION_FALSE);
					String agtRefundNo = (String) retMap.get(GlobalConstant.TRADE_SUCCESS_AGTREFUNDNO);
					order.setIsRefund(TradeConstant.ORDER_REFUND_TRUE);
					content.append("成功，退费金额:").append(order.getRefundFee()).append(",第三方退费订单号:").append(agtRefundNo).append("退费渠道:")
							.append(channel.getChannelName());
					if (StringUtils.isEmpty(order.getRefundFee())) {
						order.setRefundFee(refundOrder.getRefundFee());
					} else {
						order.setRefundFee(order.getRefundFee() + refundOrder.getRefundFee());
					}
					//已经退费金额加上此次退费金额等于订单总金额，则表示退费已经完成
					if (order.getRefundFee().intValue() == order.getTotalFee().intValue()) {
						order.setTradeStatus(TradeConstant.TRADE_STATUS_REFUND);
						order.setAgtRefundNo(agtRefundNo);//讲最后一笔部分退费订单号写入到订单中
						order.setRefundNo(refundOrder.getRefundNo());
						content.append("金额已经全部退完，不再允许操作退费");
					}
					refundOrder.setAgtRefundNo(agtRefundNo);
					refundOrder.setRefundStatus(TradeConstant.ORDER_REFUND_TRUE);
					refundOrder.setRefundTime(new Date());
					refundOrder.setRefundDetail("部分退费成功");
				} else {
					content.append("失败，失败原因:").append(retMap.get(GlobalConstant.TRADE_FAIL_MSG));
					refundOrder.setRefundStatus(TradeConstant.ORDER_REFUND_FALSE);
					refundOrder.setRefundDetail(retMap.get(GlobalConstant.TRADE_FAIL_MSG).toString());
				}
			}
			//写入退费记录信息
			order.insetPartialRefunds(refundOrder);
			order.formatHandleLog(content.toString());
			orderService.updateTradeOrderForDbAndCache(order);
			retMap.put(GlobalConstant.TRADE_SUCCESS_DATA, JSON.toJSONString(order));
			logger.info("退费申请请求结果:{}", content);
		} catch (Exception e) {
			retMap.put(GlobalConstant.TRADE_IS_EXCEPTION, true);
			retMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
			retMap.put(GlobalConstant.TRADE_FAIL_MSG, RestConstant.RETURN_SYSTEMERROR[1]);
			RefundExceptionCache refundExceptionCache = SpringContextHolder.getBean(RefundExceptionCache.class);
			refundExceptionCache.setRefundExceptionToCache(order.convertExceptionObj());
			e.printStackTrace();
		}
		return retMap;
	}

	@Override
	public String getTradeParams(String cashierId) {
		String payJson = "";
		try {
			TradeOrder order = orderService.findById(cashierId);
			ApplicationChannel channel = channelService.findTradeChannelByAppIdChannelCode(order.getAppId(), order.getChannelCode());
			payJson = channel.getParamsJson();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return payJson;
	}

	@Override
	public Map<String, Object> polymerizationPayQuery(TradeOrder order) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		try {
			ApplicationChannel channel = channelService.findTradeChannelByAppIdChannelCode(order.getAppId(), order.getChannelCode());
			retMap = TradeUtils.paymentQuery(order, channel.getParamsJson());
			// 保存第三方返回的JSON报文
			if (StringUtils.isEmpty(order.getPayQueryJson())) {
				order.setPayQueryJson(retMap.get(GlobalConstant.TRADE_SUCCESS_DATA).toString());
			}

			StringBuffer content = new StringBuffer("支付查询请求");
			Boolean isException = (Boolean) retMap.get(GlobalConstant.TRADE_IS_EXCEPTION);
			Boolean isSuccess = (Boolean) retMap.get(GlobalConstant.TRADE_IS_SUCCESS);
			if (isException) {
				order.setIsException(TradeConstant.ORDER_EXCEPTION_TRUE);
				content.append("发生异常，异常原因:").append(retMap.get(GlobalConstant.TRADE_FAIL_MSG));
			} else {
				order.setIsException(TradeConstant.ORDER_EXCEPTION_FALSE);
				if (isSuccess) {
					// 保存第三方支付订单号
					if (StringUtils.isEmpty(order.getAgtTradeNo())) {
						order.setAgtTradeNo(retMap.get(GlobalConstant.TRADE_SUCCESS_AGTPAYNO).toString());
					}
					retMap.put(GlobalConstant.MOTHED_INVOKE_RES_RETURN_URL, order.getReturnUrl());
					order.setIsPayQuery(TradeConstant.ORDER_PAY_QUERY_TRUE);
					content.append("查询成功，订单状态:").append(retMap.get(GlobalConstant.TRADE_ORDER_STATE));
				} else {
					content.append("查询失败，失败原因:").append(retMap.get(GlobalConstant.TRADE_FAIL_MSG));
				}
			}

			if (retMap.get(GlobalConstant.TRADE_ORDER_STATE) != null && !"".equals(retMap.get(GlobalConstant.TRADE_ORDER_STATE))) {
				String tradeStatus = retMap.get(GlobalConstant.TRADE_ORDER_STATE).toString();
				// 订单状态不一致时修改订单状态
				if (!tradeStatus.equals(order.getTradeStatus())) {
					order.setTradeStatus(Integer.parseInt(retMap.get(GlobalConstant.TRADE_ORDER_STATE).toString()));
					order.setPayTime((Date) retMap.get(GlobalConstant.TRADE_DATE));
				}
			}
			//添加订单处理次数
			//order.setHandleCount(order.getHandleCount() + 1);
			order.formatHandleLog(content.toString());
			orderService.updateTradeOrderForDbAndCache(order);
			retMap.put(GlobalConstant.TRADE_SUCCESS_DATA, order);
			logger.info("支付查询请求结果返回:{}", JSON.toJSONString(retMap));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return retMap;
	}

	/**
	 * 聚合支付退费订单查询
	 * 
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年9月27日下午5:22:06
	 */
	@Override
	public Map<String, Object> polymerizationRefundQuery(TradeOrder order) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		try {
			ApplicationChannel channel = channelService.findTradeChannelByAppIdChannelCode(order.getAppId(), order.getChannelCode());
			retMap = TradeUtils.refundQuery(order, channel.getParamsJson());

			StringBuffer content = new StringBuffer("退费查询请求");
			Boolean isException = (Boolean) retMap.get(GlobalConstant.TRADE_IS_EXCEPTION);
			Boolean isSuccess = (Boolean) retMap.get(GlobalConstant.TRADE_IS_SUCCESS);
			if (isException) {
				order.setIsException(TradeConstant.ORDER_EXCEPTION_TRUE);
				content.append("发生异常，异常原因:").append(retMap.get(GlobalConstant.TRADE_FAIL_MSG));
			} else {
				order.setIsException(TradeConstant.ORDER_EXCEPTION_FALSE);
				if (isSuccess) {
					retMap.put(GlobalConstant.MOTHED_INVOKE_RES_RETURN_URL, order.getReturnUrl());
					order.setIsRefundQuery(TradeConstant.ORDER_REFUND_QUERY_TRUE);
					if (retMap.containsKey(GlobalConstant.TRADE_SUCCESS_AGTREFUNDNO)) {
						order.setAgtRefundNo(retMap.get(GlobalConstant.TRADE_SUCCESS_AGTREFUNDNO).toString());
					}
					content.append("查询成功，订单状态:").append(retMap.get(GlobalConstant.TRADE_ORDER_STATE));
				} else {
					content.append("查询失败，失败原因:").append(retMap.get(GlobalConstant.TRADE_FAIL_MSG));
				}
			}
			// 保存第三方返回的JSON报文
			if (StringUtils.isEmpty(order.getRefundQueryJson())) {
				order.setRefundQueryJson(retMap.get(GlobalConstant.TRADE_SUCCESS_DATA).toString());
			}

			String tradeStatus = retMap.get(GlobalConstant.TRADE_ORDER_STATE).toString();
			// 订单状态不一致时修改订单状态
			if (!tradeStatus.equals(order.getTradeStatus())) {
				order.setTradeStatus(Integer.parseInt(retMap.get(GlobalConstant.TRADE_ORDER_STATE).toString()));
				// order.setPayTime(DateUtils.StringToDate(retMap.get("tradeTime").toString()));
			}

			order.formatHandleLog(content.toString());
			orderService.updateTradeOrderForDbAndCache(order);
			retMap.put(GlobalConstant.TRADE_SUCCESS_DATA, order);
			logger.info("聚合支付退费订单查询请求结果返回:{}", JSON.toJSONString(retMap));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return retMap;
	}

	@Override
	public Map<String, Object> polymerizationCloseOrder(TradeOrder order) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		try {
			ApplicationChannel channel = channelService.findTradeChannelByAppIdChannelCode(order.getAppId(), order.getChannelCode());
			retMap = TradeUtils.orderClose(order, channel.getParamsJson());
			StringBuffer content = new StringBuffer("关闭订单申请请求,");
			Boolean isException = (Boolean) retMap.get(GlobalConstant.TRADE_IS_EXCEPTION);
			Boolean isSuccess = (Boolean) retMap.get(GlobalConstant.TRADE_IS_SUCCESS);
			if (isException) {
				order.setIsException(TradeConstant.ORDER_EXCEPTION_TRUE);
				content.append("发生异常，异常原因:").append(RestConstant.RETURN_SYSTEMERROR);
				retMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
			} else {
				if (isSuccess) {
					order.setIsException(TradeConstant.ORDER_EXCEPTION_FALSE);
					order.setTradeStatus(TradeConstant.TRADE_STATUS_CLOSE);
					content.append("成功，商户订单号:").append(order.getOutTradeNo()).append("第三方渠道:").append(channel.getChannelName());
					retMap.put(GlobalConstant.TRADE_IS_SUCCESS, true);
				} else {
					content.append("失败，失败原因:").append(retMap.get(GlobalConstant.TRADE_FAIL_MSG));
					retMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
				}
			}
			order.formatHandleLog(content.toString());
			orderService.updateTradeOrderForDbAndCache(order);
		} catch (Exception e) {
			retMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
		}
		return retMap;
	}
}

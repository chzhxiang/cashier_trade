/**
 * <html>
 * <body>
 *  <P> Copyright 2017 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年9月8日</p>
 *  <p> Created by 党参</p>
 *  </body>
 * </html>
 */
package com.sunshine.restful.trade.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.fastjson.JSON;
import com.sunshine.common.GlobalConstant;
import com.sunshine.framework.common.PKGenerator;
import com.sunshine.mobileapp.order.entity.PartialRefundOrder;
import com.sunshine.mobileapp.order.entity.TradeOrder;
import com.sunshine.mobileapp.order.service.TradeOrderService;
import com.sunshine.payment.TradeConstant;
import com.sunshine.platform.applicationchannel.entity.ApplicationChannel;
import com.sunshine.platform.applicationchannel.service.impl.ApplicationChannelServiceImpl;
import com.sunshine.restful.RestConstant;
import com.sunshine.restful.RestResponse;
import com.sunshine.restful.dto.request.RequestPay;
import com.sunshine.restful.dto.request.RequestQuery;
import com.sunshine.restful.dto.request.RequestRefund;
import com.sunshine.restful.dto.response.RefundOrders;
import com.sunshine.restful.dto.response.ResponseLimitPay;
import com.sunshine.restful.dto.response.ResponsePay;
import com.sunshine.restful.dto.response.ResponsePayQRCode;
import com.sunshine.restful.dto.response.ResponseQuery;
import com.sunshine.restful.dto.response.ResponseRefund;
import com.sunshine.restful.trade.service.PayRestService;
import com.sunshine.restful.utils.RestUtils;
import com.sunshine.trade.service.PayService;

/**
 * @Project: cashier_desk
 * @Package: com.sunshine.restful.trade.service.impl
 * @ClassName: PayServiceImpl
 * @Description:
 *               <p>RESTFULL请求处理类</p>
 * @JDK version used:
 * @Author: 党参
 * @Create Date: 2017年9月8日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
@Path("v1/trade")
@Consumes({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
@Produces({ ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8 })
public class PayRestServiceImpl implements PayRestService {

	private static Logger logger = LoggerFactory.getLogger(PayRestServiceImpl.class);
	@Autowired
	private TradeOrderService tradeOrderService;

	@Autowired
	private PayService payService;

	@Autowired
	private ApplicationChannelServiceImpl channelService;

	@POST
	@Path("pay")
	@Override
	public RestResponse pay(RequestPay vo) {
		logger.info("订单支付请求参数:{}", JSON.toJSONString(vo));
		RestResponse restResponse = null;
		try {
			// 检查参数是否为空，部分必传参数已经验证，只需要验证当前业务所需参数
			Map<String, Object> validResMap =
					RestUtils.notBlank(vo, new String[] { "outOrderNo", "subject", "tradeTotalFee", "notifyUrl", "returnUrl" });
			Boolean isValid = Boolean.valueOf(validResMap.get(RestUtils.CHECK_IS_VALID).toString());
			if (isValid) {
				TradeOrder order = tradeOrderService.getTradeOrderByAppIdAndOutTradeNo(vo.getAppId(), vo.getOutOrderNo());
				if (order != null) {
					//如果订单是支付中,则查询第三方确认是否可以支付
					if (TradeConstant.TRADE_STATUS_PAYMENTING == order.getTradeStatus()) {
						Map<String, Object> retMap = payService.polymerizationPayQuery(order);
						Boolean isException = (Boolean) retMap.get(GlobalConstant.TRADE_IS_EXCEPTION);
						Boolean isSuccess = (Boolean) retMap.get(GlobalConstant.TRADE_IS_SUCCESS);
						if (!isException && isSuccess) {
							order.setTradeStatus(Integer.parseInt(retMap.get(GlobalConstant.TRADE_ORDER_STATE).toString()));
						}
					}

					//检查是否重复订单
					if (RestUtils.isRepeatOutOrderNo(vo, order)) {
						restResponse = new RestResponse(RestConstant.RETURN_OUTORDERNO_USED[0], RestConstant.RETURN_OUTORDERNO_USED[1]);
					}
				} else {
					order = new TradeOrder();
					logger.info("订单不存在,创建点单对象, 商户订单号:{}, 商户appId:{}", vo.getOutOrderNo(), vo.getAppId());
				}

				if (restResponse == null) {
					if (!StringUtils.isEmpty(vo.getHospitalId())) {
						order.setHospitalId(vo.getHospitalId());
					}
					if (!StringUtils.isEmpty(vo.getOpenid())) {
						order.setOpenId(vo.getOpenid());
					}
					// 创建或者更新订单
					order = tradeOrderService.createOrder(order, vo);
					String cashierUrl = RestUtils.getTradeDomainUrl().concat(TradeConstant.TRADE_CHOICE_PAYMENT_PATH);
					ResponsePay responsePay = new ResponsePay();
					responsePay.setAppId(order.getAppId());
					responsePay.setMerchantNo(order.getMerchantNo());
					responsePay.setNonceStr(PKGenerator.generateId());
					responsePay.setTimeStamp(GlobalConstant.getNowYYYYMMDDHHMMSS());

					responsePay.setCashierId(order.getId());
					responsePay.setCashierUrl(cashierUrl);
					restResponse = new RestResponse(RestConstant.RETURN_SUCCESS[0], RestConstant.RETURN_SUCCESS[1], responsePay);
				}
			} else {
				restResponse = new RestResponse(RestConstant.RETURN_PARAM_ERROR[0], validResMap.get(RestUtils.CHECK_RES_MSG).toString());
			}
		} catch (Exception e) {
			restResponse = new RestResponse(RestConstant.RETURN_SYSTEMERROR[0], RestConstant.RETURN_SYSTEMERROR[1]);
			e.printStackTrace();
		}
		return restResponse;
	}

	@POST
	@Path("refund")
	@Override
	public RestResponse refund(RequestRefund refundParamsVo) {
		logger.info("订单退费请求参数:{}", JSON.toJSONString(refundParamsVo));
		RestResponse restResponse = null;
		try {
			// 检验参数
			Map<String, Object> validResMap = RestUtils.notBlank(refundParamsVo, new String[] { "outRefundNo", "tradeTotalFee", "refundFee" });
			Boolean isValid = Boolean.valueOf(validResMap.get(RestUtils.CHECK_IS_VALID).toString());
			if (isValid) {
				if (StringUtils.isEmpty(refundParamsVo.getOutOrderNo()) && StringUtils.isEmpty(refundParamsVo.getTradeNo())) { // 商户订单号跟平台订单号二选一
					restResponse = new RestResponse(RestConstant.RETURN_PARAM_ERROR[0], "tradeNo或outOrderNo必传一个");
				} else {
					if (refundParamsVo.getRefundFee().intValue() <= 0) {
						restResponse = new RestResponse(RestConstant.RETURN_PARAM_ERROR[0], "退费金额必须大于0");
					}
				}
				if (restResponse == null) {
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("appId", refundParamsVo.getAppId());
					if (!StringUtils.isEmpty(refundParamsVo.getTradeNo())) {
						params.put("tradeNo", refundParamsVo.getTradeNo());
					}

					if (!StringUtils.isEmpty(refundParamsVo.getOutOrderNo())) {
						params.put("outTradeNo", refundParamsVo.getOutOrderNo());
					}

					// 1.查询订单（判断订单是否存在、是否异常、能否退费）
					TradeOrder order = tradeOrderService.getTradeOrderByAppIdAndOutTradeNo(params);
					if (order == null) {
						logger.info("退费请求订单不存在，不允许退款操作！商户订单号为：{}", refundParamsVo.getOutOrderNo());
						restResponse = new RestResponse(RestConstant.RETURN_OUTORDERNO_NO_EXIST[0], RestConstant.RETURN_OUTORDERNO_NO_EXIST[1]);
					} else {
						StringBuffer content = new StringBuffer();
						if (TradeConstant.TRADE_STATUS_PAYMENT != order.getTradeStatus().intValue()
								&& TradeConstant.TRADE_STATUS_PARTIAL_REFUND != order.getTradeStatus().intValue()) { // 已支付和部分退费状态才可以退费
							logger.info("退费请求订单状态为:{}，不允许退款操作！商户订单号为：{}", order.getTradeStatus(), order.getOutTradeNo());
							content.append("查询到退费订单,退费申请请求,退费订单状态为:" + TradeConstant.tradeStatus.get(order.getTradeStatus()) + "不允许退费操作！");
							restResponse = new RestResponse(RestConstant.RETURN_OUTORDERTRADE_ERROR[0], RestConstant.RETURN_OUTORDERTRADE_ERROR[1]);
						} else if (TradeConstant.TRADE_STATUS_PARTIAL_REFUND == order.getTradeStatus().intValue()) {
							logger.info("退费请求订单状态部分退费，判断商户提供的部分退费订单号是否重复！商户订单号为：{}", order.getOutTradeNo());
							params.clear();
							params.put("appId", refundParamsVo.getAppId());
							params.put("partialRefunds.outRefundNo", refundParamsVo.getOutRefundNo());
							List<TradeOrder> item = tradeOrderService.findByParams(params);
							if (!CollectionUtils.isEmpty(item)) {
								restResponse = new RestResponse(RestConstant.RETURN_OUTORDERNO_USED[0], RestConstant.RETURN_OUTORDERNO_USED[1]);
								content.append("查询到退费订单,退费申请请求,退费订单状态为部分退费，经检查发现部分退费订单号重复:").append(refundParamsVo.getOutRefundNo());
							}
							Integer refundFee = 0;
							if (!StringUtils.isEmpty(order.getRefundFee())) {
								refundFee = order.getRefundFee();
							}
							if (order.getTotalFee() - ( refundFee + refundParamsVo.getRefundFee() ) < 0) {
								content.append("查询到退费订单,退费申请请求,退费金额大于可退款金额，不允许退费操作！");
								PartialRefundOrder refundOrder = new PartialRefundOrder();
								refundOrder.setRefundNo(order.getRefundNo());
								refundOrder.setRefundDesc(refundParamsVo.getRefundDesc());
								refundOrder.setRefundFee(refundParamsVo.getRefundFee());
								refundOrder.setOutRefundNo(refundParamsVo.getOutRefundNo());
								refundOrder.setRefundStatus(TradeConstant.ORDER_REFUND_FALSE);
								refundOrder.setRefundTime(new Date());
								refundOrder.setRefundDetail("部分退费失败,退费金额大于可退款金额，不允许退费操作！");
								order.insetPartialRefunds(refundOrder);
								restResponse = new RestResponse(RestConstant.RETURN_OUTORDERTRADE_FREE_ERROR[0], "退费金额大于可退款金额");
							}
						} else if (refundParamsVo.getTradeTotalFee().intValue() != order.getTotalFee().intValue()) {
							logger.info("退费请求订单总金额错误，不允许退款操作！商户订单号为：{}", order.getOutTradeNo());
							content.append("查询到退费订单,退费申请请求,退费订单总金额错误，不允许退费操作！");
							restResponse =
									new RestResponse(RestConstant.RETURN_OUTORDERTRADE_FREE_ERROR[0], RestConstant.RETURN_OUTORDERTRADE_FREE_ERROR[1]);
						} else if (refundParamsVo.getTradeTotalFee().intValue() < refundParamsVo.getRefundFee().intValue()) {// 退款金额不能大于支付总金额
							logger.info("退费请求订单总金额小于退款金额，不允许退款操作！商户订单号为：{}", order.getOutTradeNo());
							content.append("查询到退费订单,退费申请请求,退费订单总金额小于退款金额，不允许退费操作！");
							restResponse =
									new RestResponse(RestConstant.RETURN_OUTORDERTRADE_FREE_ERROR[0], RestConstant.RETURN_OUTORDERTRADE_FREE_ERROR[1]);
						}
						if (!StringUtils.isEmpty(content.toString())) {
							order.formatHandleLog(content.toString());
							tradeOrderService.updateTradeOrderForDbAndCache(order);
						}
					}

					if (restResponse == null) {
						Map<String, Object> retMap = new HashMap<String, Object>();
						// 2.处理退费申请、更新订单（支付状态：退费中）
						if (refundParamsVo.getRefundFee().intValue() < order.getTotalFee().intValue()) {
							retMap = payService.polymerizationPartialRefund(order, refundParamsVo);
						} else {
							retMap = payService.polymerizationRefund(order, refundParamsVo);
						}
						Boolean isException = (Boolean) retMap.get(GlobalConstant.TRADE_IS_EXCEPTION);
						Boolean isSuccess = (Boolean) retMap.get(GlobalConstant.TRADE_IS_SUCCESS);
						if (isException) {
							restResponse = new RestResponse(RestConstant.RETURN_SYSTEMERROR[0], RestConstant.RETURN_SYSTEMERROR[1]);
						} else {
							if (isSuccess) {
								logger.info("订单退费请求申请成功！");
								order = JSON.parseObject(retMap.get(GlobalConstant.TRADE_SUCCESS_DATA).toString(), TradeOrder.class);
								ResponseRefund refund = new ResponseRefund();
								refund.setAppId(order.getAppId());
								refund.setMerchantNo(order.getMerchantNo());
								refund.setNonceStr(PKGenerator.generateId());
								refund.setTimeStamp(GlobalConstant.getNowYYYYMMDDHHMMSS());
								refund.setTradeNo(order.getTradeNo());
								refund.setOutOrderNo(order.getOutTradeNo());
								refund.setRefundFee(String.valueOf(refundParamsVo.getRefundFee()));
								refund.setRefundDesc(refundParamsVo.getRefundDesc());
								if (refundParamsVo.getRefundFee().intValue() < order.getTotalFee().intValue()) {
									if (!CollectionUtils.isEmpty(order.getPartialRefunds())) {
										for (PartialRefundOrder prOrder : order.getPartialRefunds()) {
											if (prOrder.getOutRefundNo().equals(refundParamsVo.getOutRefundNo())) {
												refund.setRefundNo(prOrder.getRefundNo());
												refund.setOutRefundNo(prOrder.getOutRefundNo());
												refund.setRefundTime(GlobalConstant.formatYYYYMMDDHHMMSS(prOrder.getRefundTime()));
												break;
											}
										}
									}
								} else {
									refund.setRefundNo(order.getRefundNo());
									refund.setOutRefundNo(order.getOutRefundNo());
									refund.setRefundTime(GlobalConstant.formatYYYYMMDDHHMMSS(order.getRefundTime()));
								}

								restResponse = new RestResponse(RestConstant.RETURN_SUCCESS[0], RestConstant.RETURN_SUCCESS[1], refund);
							} else {
								logger.info("订单退费请求申请失败！");
								restResponse = new RestResponse(RestConstant.RETURN_FAIL[0], retMap.get(GlobalConstant.TRADE_FAIL_MSG).toString());
							}
						}
					}
				}
			} else {
				logger.info("订单退费请求参数校验不通过！");
				restResponse = new RestResponse(RestConstant.RETURN_PARAM_ERROR[0], validResMap.get(RestUtils.CHECK_RES_MSG).toString());
			}
		} catch (Exception e) {
			restResponse = new RestResponse(RestConstant.RETURN_SYSTEMERROR[0], RestConstant.RETURN_SYSTEMERROR[1]);
			e.printStackTrace();
			logger.error("订单退费请求参数校验不通过！");
		}
		return restResponse;
	}

	@POST
	@Path("payQuery")
	@Override
	public RestResponse payQuery(RequestQuery query) {
		logger.info("支付订单查询请求参数:{}", JSON.toJSONString(query));
		// 1,两部分查询a：支付查询
		RestResponse restResponse = null;
		try {
			// 检查参数是否为空，部分必传参数已经验证，只需要验证当前业务所需参数
			Boolean isValid = Boolean.TRUE;
			if (StringUtils.isEmpty(query.getOutOrderNo()) && StringUtils.isEmpty(query.getTradeNo())) { // 商户订单号跟平台订单号二选一
				isValid = Boolean.FALSE;
			}

			if (isValid) {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("appId", query.getAppId());
				if (!StringUtils.isEmpty(query.getOutOrderNo())) {
					params.put("outTradeNo", query.getOutOrderNo());
				} else {
					params.put("tradeNo", query.getTradeNo());
				}

				TradeOrder order = tradeOrderService.getTradeOrderByAppIdAndOutTradeNo(params);
				if (order == null) {
					restResponse = new RestResponse(RestConstant.RETURN_OUTORDERNO_NO_EXIST[0], RestConstant.RETURN_OUTORDERNO_NO_EXIST[1]);
				} else {
					ResponseQuery responseQuery = new ResponseQuery();
					responseQuery.setAppId(order.getAppId());
					responseQuery.setMerchantNo(order.getMerchantNo());
					responseQuery.setNonceStr(PKGenerator.generateId());
					responseQuery.setTimeStamp(GlobalConstant.getNowYYYYMMDDHHMMSS());
					responseQuery.setChannelCode(order.getChannelCode());
					responseQuery.setOutOrderNo(order.getOutTradeNo());
					responseQuery.setTradeNo(order.getTradeNo());
					responseQuery.setTotalFee(String.valueOf(order.getTotalFee()));
					responseQuery.setAttach(order.getAttach());
					responseQuery.setPlatformCode(order.getPlatformCode());
					responseQuery.setChannelCode(order.getChannelCode());
					// 1、查询订单标识异常
					/*if (order.getIsPayQuery() == GlobalConstant.NO) {
						Map<String, Object> retMap = payService.polymerizationPayQuery(order);
						order = (TradeOrder) retMap.get(GlobalConstant.TRADE_SUCCESS_DATA);
					}*/

					if (order.getPayTime() != null) {
						responseQuery.setPayTime(GlobalConstant.formatYYYYMMDDHHMMSS(order.getPayTime()));
					}
					responseQuery.setTradeState(TradeConstant.tradeStatus.get(order.getTradeStatus()));
					restResponse = new RestResponse(RestConstant.RETURN_SUCCESS[0], RestConstant.RETURN_SUCCESS[1], responseQuery);
				}
			} else {
				logger.info("支付订单查询请求参数校验不通过，支付订单号为空！");
				restResponse = new RestResponse(RestConstant.RETURN_PARAM_ERROR[0], RestConstant.RETURN_PARAM_ERROR[1]);
			}
		} catch (Exception e) {
			restResponse = new RestResponse(RestConstant.RETURN_SYSTEMERROR[0], RestConstant.RETURN_SYSTEMERROR[1]);
			e.printStackTrace();
		}
		return restResponse;
	}

	@POST
	@Path("refundQuery")
	@Override
	public RestResponse refundQuery(RequestQuery query) {
		logger.info("退费订单查询请求参数:{}", JSON.toJSONString(query));
		// 1: 退费查询
		RestResponse restResponse = null;
		try {
			// 检查参数是否为空，部分必传参数已经验证，只需要验证当前业务所需参数
			if (StringUtils.isEmpty(query.getOutRefundNo()) && StringUtils.isEmpty(query.getRefundNo()) && StringUtils.isEmpty(query.getOutOrderNo())
					&& StringUtils.isEmpty(query.getTradeNo())) { // 商户订单号跟平台订单号二选一
				logger.info("订单查询请求参数校验不通过，退费订单号为空！");
				restResponse = new RestResponse(RestConstant.RETURN_PARAM_ERROR[0], RestConstant.RETURN_PARAM_ERROR[1]);
			}

			if (restResponse == null) {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("appId", query.getAppId());
				if (!StringUtils.isEmpty(query.getRefundNo())) {
					params.put("refundNo", query.getRefundNo());
				} else if (!StringUtils.isEmpty(query.getOutRefundNo())) {
					params.put("outRefundNo", query.getOutRefundNo());
				} else if (!StringUtils.isEmpty(query.getTradeNo())) {
					params.put("tradeNo", query.getTradeNo());
				} else if (!StringUtils.isEmpty(query.getOutOrderNo())) {
					params.put("outTradeNo", query.getOutOrderNo());
				}

				TradeOrder order = tradeOrderService.getTradeOrderByAppIdAndOutTradeNo(params);

				//如果订单中没有查到退费记录，则通过部分退费订单号查询退费记录
				if (order == null && ( !StringUtils.isEmpty(query.getRefundNo()) || !StringUtils.isEmpty(query.getOutRefundNo()) )) {
					params.clear();
					params.put("appId", query.getAppId());
					if (!StringUtils.isEmpty(query.getRefundNo())) {
						params.put("partialRefunds.refundNo", query.getRefundNo());
					}
					if (!StringUtils.isEmpty(query.getOutRefundNo())) {
						params.put("partialRefunds.outRefundNo", query.getOutRefundNo());
					}
					List<TradeOrder> item = tradeOrderService.findByParams(params);
					if (!CollectionUtils.isEmpty(item) && item.size() == 1) {
						order = item.get(0);
						/*if (!CollectionUtils.isEmpty(order.getPartialRefunds())) {
							for (PartialRefundOrder prOrder : order.getPartialRefunds()) {
								if (prOrder.getOutRefundNo().equals(query.getOutRefundNo())) {
									order.setOutRefundNo(prOrder.getOutRefundNo());
									order.setRefundNo(prOrder.getRefundNo());
									order.setRefundFee(prOrder.getRefundFee());
									order.setRefundDesc(prOrder.getRefundDesc());
									break;
								}
							}
						}*/
					}
				}
				if (order == null) {
					restResponse = new RestResponse(RestConstant.RETURN_OUTORDERNO_NO_EXIST[0], RestConstant.RETURN_OUTORDERNO_NO_EXIST[1]);
				} else {
					ResponseQuery responseQuery = new ResponseQuery();
					responseQuery.configQuery(order);

					if (!CollectionUtils.isEmpty(order.getPartialRefunds())) {
						if (!StringUtils.isEmpty(query.getRefundNo()) || !StringUtils.isEmpty(query.getOutRefundNo())) {
							for (PartialRefundOrder refundOrder : order.getPartialRefunds()) {
								Boolean refundNoFlag =
										!StringUtils.isEmpty(query.getRefundNo()) && refundOrder.getRefundNo().equals(query.getRefundNo());
								Boolean outRefundNoFlag =
										!StringUtils.isEmpty(query.getOutRefundNo()) && refundOrder.getOutRefundNo().equals(query.getOutRefundNo());
								if (refundNoFlag || outRefundNoFlag) {
									responseQuery.setOutRefundNo(refundOrder.getOutRefundNo());
									responseQuery.setRefundNo(refundOrder.getRefundNo());
									responseQuery.setRefundFee(String.valueOf(refundOrder.getRefundFee()));
									responseQuery.setRefundDesc(refundOrder.getRefundDesc());
									responseQuery.setRefundTime(GlobalConstant.formatYYYYMMDDHHMMSS(refundOrder.getRefundTime()));
									if (TradeConstant.ORDER_REFUND_TRUE == refundOrder.getRefundStatus().intValue()) {
										responseQuery.setTradeState(TradeConstant.TRADE_STATUS_REFUND_MARK);
									} else {
										responseQuery.setTradeState(TradeConstant.ORDER_REFUND_FALSE_MARK);
									}
								}
							}
						} else {
							List<RefundOrders> refundOrders = new ArrayList<RefundOrders>();
							for (PartialRefundOrder ro : order.getPartialRefunds()) {
								RefundOrders orders = new RefundOrders();
								orders.setRefundNo(ro.getRefundNo());
								orders.setOutRefundNo(ro.getOutRefundNo());
								orders.setRefundFee(String.valueOf(ro.getRefundFee()));
								orders.setRefundDesc(ro.getRefundDesc());
								orders.setRefundTime(GlobalConstant.formatYYYYMMDDHHMMSS(ro.getRefundTime()));
								if (TradeConstant.ORDER_REFUND_TRUE == ro.getRefundStatus().intValue()) {
									orders.setRefundStates(TradeConstant.ORDER_REFUND_TRUE_MARK);
								} else {
									orders.setRefundStates(TradeConstant.ORDER_REFUND_FALSE_MARK);
								}
								refundOrders.add(orders);
							}
							responseQuery.setRefundOrders(refundOrders);
						}
					} else {
						responseQuery.setOutRefundNo(order.getOutRefundNo());
						responseQuery.setRefundNo(order.getRefundNo());
						responseQuery.setRefundFee(String.valueOf(order.getRefundFee()));
						responseQuery.setRefundDesc(order.getRefundDesc());
						if (order.getRefundTime() != null) {
							responseQuery.setRefundTime(GlobalConstant.formatYYYYMMDDHHMMSS(order.getRefundTime()));
						}
						responseQuery.setTradeState(TradeConstant.tradeStatus.get(order.getTradeStatus()));
					}
					if (order.getPayTime() != null) {
						responseQuery.setPayTime(GlobalConstant.formatYYYYMMDDHHMMSS(order.getPayTime()));
					}
					restResponse = new RestResponse(RestConstant.RETURN_SUCCESS[0], RestConstant.RETURN_SUCCESS[1], responseQuery);
				}
			}
		} catch (Exception e) {
			restResponse = new RestResponse(RestConstant.RETURN_SYSTEMERROR[0], RestConstant.RETURN_SYSTEMERROR[1]);
			e.printStackTrace();
		}
		return restResponse;
	}

	@POST
	@Path("limitPay")
	@Override
	public RestResponse limitPay(RequestPay vo) {
		RestResponse restResponse = null;
		try {
			// 检查参数是否为空，部分必传参数已经验证，只需要验证当前业务所需参数
			Map<String, Object> validResMap =
					RestUtils.notBlank(vo, new String[] { "outOrderNo", "subject", "tradeTotalFee", "notifyUrl", "returnUrl" });
			Boolean isValid = Boolean.valueOf(validResMap.get(RestUtils.CHECK_IS_VALID).toString());
			if (isValid) {
				TradeOrder order = tradeOrderService.getTradeOrderByAppIdAndOutTradeNo(vo.getAppId(), vo.getOutOrderNo());
				if (order != null) {
					logger.info("查询订单结果:{}", JSON.toJSONString(order));
					//查询第三方确认是否可以支付
					if (TradeConstant.TRADE_STATUS_PAYMENTING == order.getTradeStatus()) {
						Map<String, Object> retMap = payService.polymerizationPayQuery(order);
						Boolean isException = (Boolean) retMap.get(GlobalConstant.TRADE_IS_EXCEPTION);
						Boolean isSuccess = (Boolean) retMap.get(GlobalConstant.TRADE_IS_SUCCESS);
						if (!isException && isSuccess) {
							order.setTradeStatus(Integer.parseInt(retMap.get(GlobalConstant.TRADE_ORDER_STATE).toString()));
						}
					}

					//检查是否重复订单
					if (RestUtils.isRepeatOutOrderNo(vo, order)) {
						restResponse = new RestResponse(RestConstant.RETURN_OUTORDERNO_USED[0], RestConstant.RETURN_OUTORDERNO_USED[1]);
					}
				} else {
					order = new TradeOrder();
					logger.info("订单不存在,创建点单对象, 商户订单号:{}, 商户appId:{}", vo.getOutOrderNo(), vo.getAppId());
				}

				if (restResponse == null) {
					// 创建或者更新订单
					String platformCode = channelService.getChannelIdentityWithCode(vo.getChannelCode());
					ApplicationChannel channel = channelService.getTradeChannel(vo.getMerchantNo(), vo.getAppId(), vo.getChannelCode());
					order.setChannelCode(channel.getChannelCode());
					order.setChannelName(channel.getChannelName());
					order.setPlatformCode(platformCode);
					//20171030 xiongdan 添加小额快捷支付相关参数
					order.setHospitalId(vo.getHospitalId());
					order.setAccountId(vo.getAccountId());
					order.setDeviceId(vo.getDeviceId());
					order.setSourceIp(vo.getSourceIp());
					order = tradeOrderService.createOrder(order, vo);
					ResponseLimitPay respLimitPay = new ResponseLimitPay();

					Map<String, Object> retMap = payService.polymerizationLimitPay(order, channel);
					logger.info("返回支付的结果：{}", JSON.toJSONString(retMap));
					Boolean isException = (Boolean) retMap.get(GlobalConstant.TRADE_IS_EXCEPTION);
					Boolean isSuccess = (Boolean) retMap.get(GlobalConstant.TRADE_IS_SUCCESS);
					if (!isException && isSuccess) {
						Integer status = Integer.parseInt(retMap.get(GlobalConstant.TRADE_ORDER_STATE).toString());
						respLimitPay.setTradeStatus(TradeConstant.tradeStatus.get(status));
						respLimitPay.setOutOrderNo(order.getOutTradeNo());
						respLimitPay.setTradeNo(order.getTradeNo());
						restResponse = new RestResponse(RestConstant.RETURN_SUCCESS[0], RestConstant.RETURN_SUCCESS[1], respLimitPay);
					} else {
						String failMsg = retMap.get(GlobalConstant.TRADE_FAIL_MSG).toString();
						if (failMsg == null) {
							failMsg = RestConstant.RETURN_FAIL[1];
						}
						respLimitPay.setRespMsg(failMsg);
						if (failMsg.indexOf("交易金额超限") != -1) {
							restResponse =
									new RestResponse(RestConstant.RETURN_TRADEFEE_OVERRUN_ERROR[0], RestConstant.RETURN_TRADEFEE_OVERRUN_ERROR[1],
											respLimitPay);
						} else {
							restResponse = new RestResponse(RestConstant.RETURN_FAIL[0], failMsg, respLimitPay);
						}
					}
					respLimitPay.setAppId(order.getAppId());
					respLimitPay.setMerchantNo(order.getMerchantNo());
					respLimitPay.setNonceStr(PKGenerator.generateId());
					respLimitPay.setTimeStamp(GlobalConstant.getNowYYYYMMDDHHMMSS());
				}

			} else {
				restResponse = new RestResponse(RestConstant.RETURN_PARAM_ERROR[0], validResMap.get(RestUtils.CHECK_RES_MSG).toString());
			}
		} catch (Exception e) {
			restResponse = new RestResponse(RestConstant.RETURN_SYSTEMERROR[0], RestConstant.RETURN_SYSTEMERROR[1]);
			e.printStackTrace();
		}
		logger.info("小额快捷支付返回结果：{}", JSON.toJSONString(restResponse));
		return restResponse;
	}

	@POST
	@Path("payQRCode")
	@Override
	public RestResponse payQRCode(RequestPay vo) {
		logger.info("订单支付二维码请求参数:{}", JSON.toJSONString(vo));
		RestResponse restResponse = null;
		try {
			// 检查参数是否为空，部分必传参数已经验证，只需要验证当前业务所需参数
			Map<String, Object> validResMap = RestUtils.notBlank(vo, new String[] { "outOrderNo", "subject", "tradeTotalFee", "notifyUrl" });
			Boolean isValid = Boolean.valueOf(validResMap.get(RestUtils.CHECK_IS_VALID).toString());
			if (isValid) {
				TradeOrder order = tradeOrderService.getTradeOrderByAppIdAndOutTradeNo(vo.getAppId(), vo.getOutOrderNo());
				if (order != null) {
					//如果订单是支付中,则查询第三方确认是否可以支付
					if (TradeConstant.TRADE_STATUS_PAYMENTING == order.getTradeStatus()) {
						Map<String, Object> retMap = payService.polymerizationPayQuery(order);
						Boolean isException = (Boolean) retMap.get(GlobalConstant.TRADE_IS_EXCEPTION);
						Boolean isSuccess = (Boolean) retMap.get(GlobalConstant.TRADE_IS_SUCCESS);
						if (!isException && isSuccess) {
							order.setTradeStatus(Integer.parseInt(retMap.get(GlobalConstant.TRADE_ORDER_STATE).toString()));
						}
					}

					//检查是否重复订单
					if (RestUtils.isRepeatOutOrderNo(vo, order)) {
						restResponse = new RestResponse(RestConstant.RETURN_OUTORDERNO_USED[0], RestConstant.RETURN_OUTORDERNO_USED[1]);
					}
				} else {
					order = new TradeOrder();
					logger.info("订单不存在,创建定单对象, 商户订单号:{}, 商户appId:{}", vo.getOutOrderNo(), vo.getAppId());
				}

				if (restResponse == null) {
					if (!StringUtils.isEmpty(vo.getHospitalId())) {
						order.setHospitalId(vo.getHospitalId());
					}
					// 创建或者更新订单
					order = tradeOrderService.createOrder(order, vo);
					StringBuilder qrCodeUrl = new StringBuilder();
					qrCodeUrl.append(RestUtils.getTradeDomainUrl()).append(TradeConstant.TRADE_QRCODE_PAYMENT_PATH);
					qrCodeUrl.append("?").append(GlobalConstant.MOTHED_INVOKE_RES_CASHIER_ID).append("=").append(order.getId());
					qrCodeUrl.append("&").append(RestConstant.PARAMS_MERCHANTNO).append("=").append(vo.getMerchantNo());
					qrCodeUrl.append("&").append(RestConstant.PARAMS_APPID).append("=").append(vo.getAppId());
					//String cashierUrl = RestUtils.getTradeDomainUrl().concat(TradeConstant.TRADE_CHOICE_PAYMENT_PATH).concat("?id=" + order.getId());
					ResponsePayQRCode responsePay = new ResponsePayQRCode();
					responsePay.setAppId(order.getAppId());
					responsePay.setMerchantNo(order.getMerchantNo());
					responsePay.setCashierId(order.getId());
					responsePay.setNonceStr(PKGenerator.generateId());
					responsePay.setTimeStamp(GlobalConstant.getNowYYYYMMDDHHMMSS());
					responsePay.setCashierUrl(qrCodeUrl.toString());
					restResponse = new RestResponse(RestConstant.RETURN_SUCCESS[0], RestConstant.RETURN_SUCCESS[1], responsePay);
				}
			} else {
				restResponse = new RestResponse(RestConstant.RETURN_PARAM_ERROR[0], validResMap.get(RestUtils.CHECK_RES_MSG).toString());
			}
		} catch (Exception e) {
			restResponse = new RestResponse(RestConstant.RETURN_SYSTEMERROR[0], RestConstant.RETURN_SYSTEMERROR[1]);
			logger.error("订单支付二维码接口处理异常", e);
		}
		return restResponse;
	}
}

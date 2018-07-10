package com.sunshine.trade.service;

import java.util.Map;

import com.sunshine.mobileapp.order.entity.TradeOrder;
import com.sunshine.platform.applicationchannel.entity.ApplicationChannel;
import com.sunshine.restful.dto.request.RequestRefund;

/**
 * @Project ChuFangLiuZhuan_PlatForm
 * @Package com.sunshine.trade.service
 * @ClassName PayService.java
 * @Description 支付Service接口API
 * @JDK version used 1.8
 * @Author 于策/yu.ce@foxmail.com
 * @Create Date 2017年6月28日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
public interface PayService {
	/**
	 * 聚合支付接口
	 * 
	 * @param cashierId
	 * @return
	 */
	Map<String, Object> polymerizationPay(TradeOrder order, String channelCode);

	/**
	 * 小额快捷支付接口
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年11月7日下午8:58:32
	 */
	Map<String, Object> polymerizationLimitPay(TradeOrder order, ApplicationChannel channel);

	/**
	 * 聚合退费接口
	 * 
	 * @param order
	 * @return
	 */
	Map<String, Object> polymerizationRefund(TradeOrder order, RequestRefund refundParamsVo);

	/**
	 * 聚合部分退费接口
	 * 
	 * @param order
	 * @return
	 */
	Map<String, Object> polymerizationPartialRefund(TradeOrder order, RequestRefund refundParamsVo);

	/**
	 * 聚合支付订单查询接口
	 * 
	 * @param order
	 * @return
	 */
	Map<String, Object> polymerizationPayQuery(TradeOrder order);

	/**
	 * 聚合退费订单查询接口
	 * 
	 * @param order
	 * @return
	 */
	Map<String, Object> polymerizationRefundQuery(TradeOrder order);

	/**
	 * 聚合关闭第三方订单接口
	 * @param order
	 * @return
	 */
	Map<String, Object> polymerizationCloseOrder(TradeOrder order);

	/**
	 * 查询支付参数
	 * 
	 * @param cashierId
	 * @return
	 */
	String getTradeParams(String cashierId);
}

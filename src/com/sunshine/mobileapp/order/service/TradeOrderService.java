/**
 * <html>
 * <body>
 *  <P> Copyright 2017 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年9月12日</p>
 *  <p> Created by 党参</p>
 *  </body>
 * </html>
 */
package com.sunshine.mobileapp.order.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.sunshine.framework.mvc.mongodb.service.BaseMongoService;
import com.sunshine.mobileapp.order.entity.TradeOrder;
import com.sunshine.restful.dto.request.RequestPay;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.mobileapp.order.service
 * @ClassName: TradeOrder
 * @Description: <p></p>
 * @JDK version used: 
 * @Author: 党参
 * @Create Date: 2017年9月12日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public interface TradeOrderService extends BaseMongoService<TradeOrder, Serializable> {

	/**
	 * 创建订单
	 * @param pay
	 * @return
	 */
	TradeOrder createOrder(TradeOrder order, RequestPay pay);

	/**
	 * 根据appId、外部订单号获取订单信息
	 * @param params
	 * @return
	 */
	TradeOrder getTradeOrderByAppIdAndOutTradeNo(Map<String, Object> params);

	/**
	 * 根据appId、外部订单号获取订单信息
	 * @param params
	 * @return
	 */
	TradeOrder getTradeOrderByAppIdAndOutTradeNo(String appId, String outTradeNo);

	/**
	 * 更新订单及缓存
	 * @param order
	 */
	void updateTradeOrderForDbAndCache(TradeOrder order);

	/**
	 * 根据id更新交易订单日志信息
	 * @param id
	 * @param map
	 */
	void updateTradeOrderLogsById(String id, Map<String, Object> map);

	/**
	 * 查询订单(加载到缓存)
	 * @param day
	 * @return
	 */
	List<TradeOrder> getTradeOrderForCache(int day);

	/**
	 * 获取所有需要处理的支付异常订单
	 * @return
	 */
	List<TradeOrder> getAllPaymentException();

	/**
	 * 获取所有需要处理的退费异常订单
	 * @return
	 */
	List<TradeOrder> getAllRefundException();

	/**
	 * 获取所有需要处理的支付超时异常订单
	 * @return
	 */
	List<TradeOrder> getAllPaymentOvertimeException();

	/**
	 * @param cashierId
	 * @return
	 */
	TradeOrder findById(String cashierId);
}

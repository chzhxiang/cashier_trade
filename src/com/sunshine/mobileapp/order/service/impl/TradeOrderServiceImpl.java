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
package com.sunshine.mobileapp.order.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.sunshine.common.GlobalConstant;
import com.sunshine.common.datas.cache.component.TradeOrderCache;
import com.sunshine.framework.common.spring.ext.SpringContextHolder;
import com.sunshine.framework.mvc.mongodb.DBConstant.QueryOperationalEnum;
import com.sunshine.framework.mvc.mongodb.dao.BaseMongoDao;
import com.sunshine.framework.mvc.mongodb.service.impl.BaseMongoServiceImpl;
import com.sunshine.framework.mvc.mongodb.vo.Condition;
import com.sunshine.framework.mvc.mongodb.vo.LogicalCondition;
import com.sunshine.framework.mvc.mongodb.vo.QueryCondition;
import com.sunshine.mobileapp.order.dao.TradeOrderDao;
import com.sunshine.mobileapp.order.entity.TradeOrder;
import com.sunshine.mobileapp.order.service.TradeOrderService;
import com.sunshine.payment.TradeConstant;
import com.sunshine.platform.merchant.entity.Merchant;
import com.sunshine.platform.merchant.service.MerchantService;
import com.sunshine.restful.dto.request.RequestPay;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.mobileapp.order.service
 * @ClassName: TradeOrderServiceImpl
 * @Description: <p></p>
 * @JDK version used: 
 * @Author: 党参
 * @Create Date: 2017年9月12日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
@Service
public class TradeOrderServiceImpl extends BaseMongoServiceImpl<TradeOrder, Serializable> implements TradeOrderService {

	private static Logger logger = LoggerFactory.getLogger(TradeOrderServiceImpl.class);

	@Autowired
	private TradeOrderDao tradeOrderDao;

	@Autowired
	private MerchantService merchantService;
	private TradeOrderCache tradeOrderCache = SpringContextHolder.getBean(TradeOrderCache.class);

	@Override
	protected BaseMongoDao<TradeOrder, Serializable> getDao() {
		return tradeOrderDao;
	}

	@Override
	public TradeOrder createOrder(TradeOrder order, RequestPay pay) {
		if (order == null) {
			order = new TradeOrder();
		}

		order.setAppId(pay.getAppId());
		Merchant merchant = merchantService.findByMerchantNo(pay.getMerchantNo());
		order.setMerchantNo(merchant.getMerchantNo());
		order.setMerchantName(merchant.getMerchantName());
		order.setOutTradeNo(pay.getOutOrderNo());
		order.setSubject(pay.getSubject());
		order.setTotalFee(pay.getTradeTotalFee());
		order.setAttach(pay.getAttach());
		order.setNotifyUrl(pay.getNotifyUrl());
		order.setReturnUrl(pay.getReturnUrl());
		order.setTradeStatus(TradeConstant.TRADE_STATUS_NO_PAYMENT);
		order.setIsException(TradeConstant.ORDER_EXCEPTION_FALSE);
		if (pay.getOutTime() != null && !pay.getOutTime().isEmpty()) {
			order.setOutTime(com.sunshine.framework.utils.DateUtils.StringToDate(pay.getOutTime()));
		}
		try {
			if (StringUtils.isEmpty(order.getId())) {
				order.setCreateTime(new Date());
				order.formatHandleLog("交易订单(未支付)生成");
				this.insert(order);
			} else {
				order.formatHandleLog("交易订单(未支付)更新");
				this.update(order);
			}
			tradeOrderCache.setTradeOrder(order);
			logger.info("创建订单详情:{}", JSON.toJSONString(order));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return order;
	}

	@Override
	public TradeOrder getTradeOrderByAppIdAndOutTradeNo(Map<String, Object> params) {
		logger.info("订单查询入参:{}", JSON.toJSONString(params));
		TradeOrder order = null;
		try {
			if (params.containsKey("appId") && params.containsKey("outTradeNo")) {
				String appId = params.get("appId").toString();
				String outTradeNo = params.get("outTradeNo").toString();
				order = tradeOrderCache.getTradeOrderForCache(appId, outTradeNo);
			}
			if (order == null) {
				List<TradeOrder> item = this.findByParams(params);
				if (item.size() > 0) {
					order = item.get(0);
				}
			}
		} catch (Exception e) {
			logger.error("订单查询异常:{}", e);
			e.printStackTrace();
		}
		return order;
	}

	@Override
	public TradeOrder getTradeOrderByAppIdAndOutTradeNo(String appId, String outTradeNo) {
		logger.info("订单查询入参appId:{}, outTradeNo:{}", appId, outTradeNo);
		TradeOrder order = null;
		try {
			if (!StringUtils.isEmpty(outTradeNo) && !StringUtils.isEmpty(appId)) {
				order = tradeOrderCache.getTradeOrderForCache(appId, outTradeNo);
			}
			if (order == null) {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("appId", appId);
				params.put("outTradeNo", outTradeNo);
				List<TradeOrder> item = this.findByParams(params);
				if (item.size() > 0) {
					order = item.get(0);
				}
			}
		} catch (Exception e) {
			logger.error("订单查询异常:{}", e);
			e.printStackTrace();
		}
		return order;
	}

	@Override
	public void updateTradeOrderForDbAndCache(TradeOrder order) {
		logger.info("商户订单号:{}, 平台订单号:{},更新数据库和缓存:{}", order.getOutTradeNo(), order.getTradeNo(), JSON.toJSONString(order));
		try {
			this.updateNotWithNull(order);
			tradeOrderCache.setTradeOrder(order);
		} catch (Exception e) {
			logger.error("更新订单缓存和DB异常:{}", e);
			e.printStackTrace();
		}
	}

	@Override
	public void updateTradeOrderLogsById(String id, Map<String, Object> map) {
		logger.info("订单ＩＤ:{}, 更新数据库和缓存:{}", id, JSON.toJSONString(map));
		try {
			tradeOrderDao.updateByIdForDataMap(id, map);
			TradeOrder tradeOrder = tradeOrderDao.findById(id);
			tradeOrderCache.setTradeOrder(tradeOrder);
		} catch (Exception e) {
			logger.error("根据id更新交易订单日志信息异常:{}", e);
			e.printStackTrace();
		}
	}

	@Override
	public List<TradeOrder> getTradeOrderForCache(int day) {
		List<TradeOrder> item = null;
		try {
			List<Condition> conditions = new ArrayList<>();
			LogicalCondition logicalCondition = new LogicalCondition();
			Date date = new Date();
			QueryCondition condition = new QueryCondition("createTime", GlobalConstant.formatYYYYMMDDHHMMSS(date), QueryOperationalEnum.LTE);
			conditions.add(condition);
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.DATE, day);
			condition = new QueryCondition("createTime", GlobalConstant.formatYYYYMMDDHHMMSS(c.getTime()), QueryOperationalEnum.GTE);
			conditions.add(condition);
			logicalCondition.setConditions(conditions);
			logicalCondition.setQueryOperator(QueryOperationalEnum.AND);
			item = tradeOrderDao.findByLogicalCondition(logicalCondition);
		} catch (Exception e) {
			logger.error("查询订单(加载到缓存)异常:{}", e);
			e.printStackTrace();
		}
		return item;
	}

	@Override
	public List<TradeOrder> getAllPaymentException() {
		List<TradeOrder> item = null;
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("isException", TradeConstant.ORDER_EXCEPTION_TRUE);
			params.put("tradeStatus", TradeConstant.TRADE_STATUS_NO_PAYMENT);
			params.put("isSuccess", TradeConstant.ORDER_SUCCESS_FALSE);
			item = this.findByParams(params);
		} catch (Exception e) {
			logger.error("获取所有需要处理的支付异常订单异常:{}", e);
			e.printStackTrace();
		}
		return item;
	}

	@Override
	public List<TradeOrder> getAllRefundException() {
		List<TradeOrder> item = null;
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("isException", TradeConstant.ORDER_EXCEPTION_TRUE);
			params.put("tradeStatus", TradeConstant.TRADE_STATUS_REFUNDING);
			params.put("isRefund", TradeConstant.ORDER_REFUND_FALSE);
			item = this.findByParams(params);
		} catch (Exception e) {
			logger.error("获取所有需要处理的退费异常订单:{}", e);
			e.printStackTrace();
		}
		return item;
	}

	@Override
	public List<TradeOrder> getAllPaymentOvertimeException() {
		List<TradeOrder> item = null;
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			//params.put("isException", TradeConstant.ORDER_EXCEPTION_TRUE);
			params.put("tradeStatus", TradeConstant.TRADE_STATUS_PAYMENTING);
			params.put("isSuccess", TradeConstant.ORDER_REFUND_FALSE);
			item = this.findByParams(params);
		} catch (Exception e) {
			logger.error("获取所有需要处理的支付超时异常订单:{}", e);
			e.printStackTrace();
		}
		return item;
	}

	@Override
	public TradeOrder findById(String cashierId) {
		return super.findById(cashierId);
	}
}

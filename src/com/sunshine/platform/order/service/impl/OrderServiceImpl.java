/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年10月10日</p>
 *  <p> Created by hqy</p>
 *  </body>
 * </html>
 */
package com.sunshine.platform.order.service.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.sunshine.common.utils.CommonUtil;
import com.sunshine.framework.mvc.mongodb.DBConstant.QueryOperationalEnum;
import com.sunshine.framework.mvc.mongodb.DBConstant.SortOperationalEnum;
import com.sunshine.framework.mvc.mongodb.dao.BaseMongoDao;
import com.sunshine.framework.mvc.mongodb.service.impl.BaseMongoServiceImpl;
import com.sunshine.framework.mvc.mongodb.vo.Condition;
import com.sunshine.framework.mvc.mongodb.vo.LogicalCondition;
import com.sunshine.framework.mvc.mongodb.vo.QueryCondition;
import com.sunshine.framework.mvc.mongodb.vo.Restrictions;
import com.sunshine.framework.utils.DateUtils;
import com.sunshine.mobileapp.bankCard.entity.BankCard;
import com.sunshine.mobileapp.bankCard.service.BankCardService;
import com.sunshine.mobileapp.order.entity.TradeOrder;
import com.sunshine.payment.TradeConstant;
import com.sunshine.platform.application.entity.MerchantApplication;
import com.sunshine.platform.application.service.MerchantApplicationService;
import com.sunshine.platform.channel.entity.TradeChannel;
import com.sunshine.platform.channel.service.TradeChannelService;
import com.sunshine.platform.merchant.entity.Merchant;
import com.sunshine.platform.merchant.service.MerchantService;
import com.sunshine.platform.order.dao.impl.OrderDaoImpl;
import com.sunshine.platform.order.entity.ChartStatistics;
import com.sunshine.platform.order.entity.OrderDataView;
import com.sunshine.platform.order.entity.OrderParamsVo;
import com.sunshine.platform.order.entity.OrderStatistics;
import com.sunshine.platform.order.entity.StatisticsType;
import com.sunshine.platform.order.service.OrderService;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.platform.order.service.impl
 * @ClassName: OrderServiceImpl
 * @Description: <p>订单管理service实现</p>
 * @JDK version used: 
 * @Author: 百部
 * @Create Date: 2017年10月10日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
@Service
public class OrderServiceImpl extends BaseMongoServiceImpl<TradeOrder, Serializable> implements OrderService {

	private static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

	private static final String ACTURE_PAY_TYPE = "1"; //实付

	private static final String ACTURE_PAY_GET = "2"; //实收

	private static final String DEFALUT_PAY_FREE = "0.00";

	private static final String SEARCH_TRADESTATUS_KEY = "tradeStatus";

	private static final String SEARCH_STARTDATE_KEY = "startDate";

	private static final String SEARCH_ENDDATE_KEY = "endDate";

	private static final String SEARCH_KEY = "createTime";

	private static final String SEARCH_MERCHANNO_KEY = "merchantNo";

	private static final String SEARCH_APPID_KEY = "appId";

	private static final String SEARCH_CHANNEL_KEY = "channelCode";

	private static final String SORT_KEY = "createTime";
	//时间范围后缀
	private static final String START_TIME_SUFFIX = " 00:00:00";

	private static final String END_TIME_SUFFIX = " 23:59:59";

	@Autowired
	private OrderDaoImpl orderDao;

	@Autowired
	protected MerchantApplicationService merchantApplicationService;

	@Autowired
	protected MerchantService merchantService;

	@Autowired
	protected TradeChannelService tradeChannelService;

	@Autowired
	protected BankCardService bankCardService;

	@Override
	protected BaseMongoDao<TradeOrder, Serializable> getDao() {
		return orderDao;
	}

	@Override
	public PageInfo<OrderParamsVo> queryByPage(Map<String, Object> map, Page<TradeOrder> page) {
		List<Condition> conditions = new ArrayList<>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String key = entry.getKey();
			String value = String.valueOf(entry.getValue());
			QueryCondition condition = null;
			if (key.equals(SEARCH_TRADESTATUS_KEY)) {
				int states = Integer.parseInt(value);
				condition = new QueryCondition(key, states, QueryOperationalEnum.EQ);
			} else {
				if (key.equals(SEARCH_STARTDATE_KEY)) {
					condition = new QueryCondition(SEARCH_KEY, value + START_TIME_SUFFIX, QueryOperationalEnum.GTE);
				} else if (key.equals(SEARCH_ENDDATE_KEY)) {
					condition = new QueryCondition(SEARCH_KEY, value + END_TIME_SUFFIX, QueryOperationalEnum.LTE);
				} else {
					condition = new QueryCondition(key, value, QueryOperationalEnum.EQ);
				}
			}
			conditions.add(condition);
		}
		Map<String, Integer> sortMap = new HashMap<>();
		sortMap.put(SORT_KEY, SortOperationalEnum.DESC.getSortVal());
		PageInfo<TradeOrder> listByPage = orderDao.findListByPage(conditions, sortMap, page);
		//格式化显示数据
		List<TradeOrder> list = listByPage.getList();
		List<OrderParamsVo> resultList = new ArrayList<OrderParamsVo>();
		for (TradeOrder tradeOrder : list) {
			OrderParamsVo orderFormate = dataFormate(tradeOrder);
			resultList.add(orderFormate);
		}
		PageInfo<OrderParamsVo> resultPageInfo = new PageInfo<>(resultList);
		long pages = listByPage.getTotal();
		int pageSize = listByPage.getPageSize();
		resultPageInfo.setTotal(pages);
		resultPageInfo.setPageSize(pageSize);
		resultPageInfo.setPageNum(listByPage.getPageNum());
		if (pages % pageSize == 0) {
			resultPageInfo.setPages((int) ( pages / pageSize ));
		} else {
			resultPageInfo.setPages((int) ( pages / pageSize ) + 1);
		}
		resultPageInfo.setPrePage(listByPage.getPageNum() - 1);
		int nextPage = listByPage.getPageNum() + 1;
		if (nextPage > resultPageInfo.getPages()) {
			resultPageInfo.setNextPage(0);
		} else {
			resultPageInfo.setNextPage(nextPage);
		}
		return resultPageInfo;
	}

	@Override
	public OrderParamsVo dataFormate(TradeOrder order) {
		OrderParamsVo paramsVo = new OrderParamsVo();
		try {
			BeanUtils.copyProperties(paramsVo, order);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (order.getCreateTime() != null) {
			paramsVo.setCreateTimeVo(DateUtils.dateToString(order.getCreateTime()));
		} else {
			paramsVo.setCreateTimeVo("");
		}
		if (order.getPayTime() != null) {
			paramsVo.setPayTimeVo(DateUtils.dateToString(order.getPayTime()));
		} else {
			paramsVo.setPayTimeVo("");
		}
		if (order.getRefundTime() != null) {
			paramsVo.setRefundTimeVo(DateUtils.dateToString(order.getRefundTime()));
		} else {
			paramsVo.setRefundTimeVo("");
		}

		if (order.getCardId() != null) {
			String cardId = order.getCardId();
			BankCard bankCard = bankCardService.findById(cardId);
			if (bankCard != null) {
				bankCard.setBankCardNo(CommonUtil.formatMedicalCard(bankCard.getBankCardNo()));
				paramsVo.setBankCard(bankCard);
			}
		}

		//格式化订单状态
		int tradeStatus = order.getTradeStatus();
		switch (tradeStatus) {
		case TradeConstant.TRADE_STATUS_NO_PAYMENT:
			paramsVo.setTradeStatusVo(TradeConstant.TRADE_STATUS_NO_PAYMENT_VIEW);
			break;
		case TradeConstant.TRADE_STATUS_PAYMENT:
			paramsVo.setTradeStatusVo(TradeConstant.TRADE_STATUS_PAYMENT_VIEW);
			break;
		case TradeConstant.TRADE_STATUS_REFUND:
			paramsVo.setTradeStatusVo(TradeConstant.TRADE_STATUS_REFUND_VIEW);
			break;
		case TradeConstant.TRADE_STATUS_PAYMENTING:
			paramsVo.setTradeStatusVo(TradeConstant.TRADE_STATUS_PAYMENTING_VIEW);
			break;
		case TradeConstant.TRADE_STATUS_REFUNDING:
			paramsVo.setTradeStatusVo(TradeConstant.TRADE_STATUS_REFUNDING_VIEW);
			break;
		case TradeConstant.TRADE_STATUS_CLOSE:
			paramsVo.setTradeStatusVo(TradeConstant.TRADE_STATUS_CLOSE_VIEW);
			break;
		case TradeConstant.TRADE_STATUS_PARTIAL_REFUND:
			paramsVo.setTradeStatusVo(TradeConstant.TRADE_STATUS_PARTIAL_REFUND_VIEW);
			break;
		default:
			break;
		}
		//格式化相关金额
		DecimalFormat df = new DecimalFormat(DEFALUT_PAY_FREE);
		Integer totalFree = order.getTotalFee() == null ? 0 : order.getTotalFee();
		Integer refundFee = order.getRefundFee() == null ? 0 : order.getRefundFee();
		String formatTotalFree = df.format((float) totalFree / 100);
		String formatRefundFee = df.format((float) refundFee / 100);
		paramsVo.setTotalFeeVo(formatTotalFree);
		paramsVo.setRefundFeeVo(formatRefundFee);
		/*paramsVo.setActualPayFreeVo(countActualFree(ACTURE_PAY_TYPE, formatTotalFree, tradeStatus));
		paramsVo.setActualGetFreeVo(countActualFree(ACTURE_PAY_GET, formatTotalFree, tradeStatus));*/
		paramsVo.setActualPayFreeVo(formatTotalFree);
		String actualGetFreeVo = df.format( ( (float) totalFree - refundFee ) / 100);
		paramsVo.setActualGetFreeVo(actualGetFreeVo);
		//查询商户名称 和 应用名称（如创建订单时已入库则不需再次查询）
		String merchantNo = order.getMerchantNo();
		Merchant merchant = merchantService.findByMerchantNo(merchantNo);
		paramsVo.setMerchantName(merchant.getMerchantName());
		String appId = order.getAppId();
		MerchantApplication merchantApplication = merchantApplicationService.findByAppId(appId);
		paramsVo.setAppName(merchantApplication.getAppName());
		return paramsVo;
	}

	@Override
	public OrderDataView countTotalFree(Map<String, Object> paramsMap) {
		OrderDataView orderDataView = null;
		if (paramsMap == null || paramsMap.size() < 1) { //不存在条件
			List<TradeOrder> allTradeOrder = orderDao.findAll();
			orderDataView = countFree(allTradeOrder);
		} else {
			List<Condition> queryList = formatDateQueryStr(paramsMap);
			List<TradeOrder> allTradeOrder = super.findByAndCondition(queryList);
			orderDataView = countFree(allTradeOrder);
		}
		return orderDataView;
	}

	@Override
	public List<OrderDataView> countMechanTotalFree(List<Merchant> merchanList, Map<String, Object> paramsMap) {
		List<OrderDataView> list = new ArrayList<>();
		for (Merchant merchan : merchanList) {
			List<Condition> conditions = new ArrayList<>();
			List<TradeOrder> merchanOrderList = null;
			//根据商户号查询订单
			QueryCondition condition = new QueryCondition(SEARCH_MERCHANNO_KEY, merchan.getMerchantNo(), QueryOperationalEnum.EQ);
			conditions.add(condition);
			if (paramsMap == null || paramsMap.size() < 1) { //不存在条件
				merchanOrderList = super.findByAndCondition(conditions);
			} else {
				List<Condition> queryList = formatDateQueryStr(paramsMap);
				queryList.add(condition);
				merchanOrderList = super.findByAndCondition(queryList);
			}
			OrderDataView orderDataView = countFree(merchanOrderList);
			orderDataView.setMerchantName(merchan.getMerchantName());
			list.add(orderDataView);
		}

		return list;
	}

	@Override
	public List<OrderDataView> countMechanApplicationTotalFree(List<Merchant> merchanList, Map<String, Object> paramsMap) {
		List<OrderDataView> list = new ArrayList<>();
		for (Merchant merchan : merchanList) {
			//根据商户号查询订单
			List<MerchantApplication> appList = new ArrayList<>();
			if (paramsMap != null && !StringUtils.isEmpty(paramsMap.get("appId"))) {
				String appId = (String) paramsMap.get("appId");
				MerchantApplication application = merchantApplicationService.findByAppId(appId);
				appList.add(application);
			} else {
				appList = merchantApplicationService.findApplicationByMechNo(merchan.getMerchantNo());
			}

			for (MerchantApplication app : appList) {
				List<Condition> queryList = new ArrayList<>();
				if (paramsMap != null) {
					queryList = formatDateQueryStr(paramsMap);
				}
				QueryCondition condition = new QueryCondition(SEARCH_MERCHANNO_KEY, merchan.getMerchantNo(), QueryOperationalEnum.EQ);
				queryList.add(condition);
				QueryCondition condition2 = new QueryCondition(SEARCH_APPID_KEY, app.getAppId(), QueryOperationalEnum.EQ);
				queryList.add(condition2);
				List<TradeOrder> merchanOrderList = super.findByAndCondition(queryList);
				OrderDataView orderDataView = countFree(merchanOrderList);
				orderDataView.setMerchantName(merchan.getMerchantName());
				orderDataView.setAppName(app.getAppName());
				list.add(orderDataView);
			}
		}
		return list;
	}

	@Override
	public List<OrderDataView> countChannelTotalFree(List<TradeChannel> channelList, Map<String, Object> paramsMap) {
		List<OrderDataView> list = new ArrayList<>();
		for (TradeChannel channel : channelList) {
			//List<QueryCondition> conditions = new ArrayList<>();
			List<Condition> queryList = new ArrayList<>();
			QueryCondition condition = new QueryCondition(SEARCH_CHANNEL_KEY, channel.getCode(), QueryOperationalEnum.EQ);
			if (paramsMap != null) {
				queryList = formatDateQueryStr(paramsMap);
				String merchantNo = (String) paramsMap.get("merchantNo");
				String appId = (String) paramsMap.get("appId");
				if (!StringUtils.isEmpty(merchantNo)) {
					QueryCondition condition2 = new QueryCondition(SEARCH_MERCHANNO_KEY, merchantNo, QueryOperationalEnum.EQ);
					queryList.add(condition2);
				}
				if (!StringUtils.isEmpty(appId)) {
					QueryCondition condition3 = new QueryCondition(SEARCH_APPID_KEY, appId, QueryOperationalEnum.EQ);
					queryList.add(condition3);
				}
			}
			queryList.add(condition);
			List<TradeOrder> channelOrderList = super.findByAndCondition(queryList);
			OrderDataView orderDataView = countFree(channelOrderList);
			orderDataView.setChannelName(channel.getName());
			list.add(orderDataView);
		}
		return list;
	}

	@Override
	public Map<String, Object> countOneMechanApplicationTotalFree(Map<String, Object> map) {

		return null;
	}

	/**
	 * 根据map组装订单总览查询的日期查询条件
	 */
	public List<Condition> formatDateQueryStr(Map<String, Object> map) {
		List<Condition> conditions = new ArrayList<>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String key = entry.getKey();
			String value = String.valueOf(entry.getValue());
			QueryCondition condition = null;
			if (key.equals(SEARCH_STARTDATE_KEY)) {
				condition = new QueryCondition(SEARCH_KEY, value + START_TIME_SUFFIX, QueryOperationalEnum.GTE);
				conditions.add(condition);
			}
			if (key.equals(SEARCH_ENDDATE_KEY)) {
				condition = new QueryCondition(SEARCH_KEY, value + END_TIME_SUFFIX, QueryOperationalEnum.LTE);
				conditions.add(condition);
			}
		}
		return conditions;
	}

	/**
	 * 计算实付金额 实收金额
	 */
	public String countActualFree(String type, String totalFee, int tradeStatus) {
		if (ACTURE_PAY_TYPE.equals(type)) { //实付()
			if (tradeStatus == TradeConstant.TRADE_STATUS_NO_PAYMENT || tradeStatus == TradeConstant.TRADE_STATUS_PAYMENTING) {
				return DEFALUT_PAY_FREE;
			} else {
				return totalFee;
			}
		} else if (ACTURE_PAY_GET.equals(type)) { //实收
			if (tradeStatus == TradeConstant.TRADE_STATUS_NO_PAYMENT || tradeStatus == TradeConstant.TRADE_STATUS_PAYMENTING) {
				return DEFALUT_PAY_FREE;
			} else {
				return totalFee;
			}
		}
		return DEFALUT_PAY_FREE;
	}

	/**
	 * 统计方法
	 * @param merchanOrderList
	 * @return
	 */
	public OrderDataView countFree(List<TradeOrder> merchanOrderList) {
		OrderDataView orderDataView = new OrderDataView();
		double total_free = 0.00;
		double refund_free = 0.00;
		double success_free = 0.00;
		int sponsor_frequency = 0; //发起笔数
		int refund_frequency = 0;
		int success_frequency = 0;
		if (merchanOrderList == null || merchanOrderList.size() < 1) {
			return orderDataView;
		}
		for (TradeOrder tradeOrder : merchanOrderList) {
			if (tradeOrder.getTradeStatus() != TradeConstant.TRADE_STATUS_NO_PAYMENT
					&& tradeOrder.getTradeStatus() != TradeConstant.TRADE_STATUS_PAYMENTING) { //排除待支付和支付中状态订单
				total_free = ( (double) tradeOrder.getTotalFee() ) / 100.00 + total_free;
				sponsor_frequency = sponsor_frequency + 1;
				if (tradeOrder.getTradeStatus() == TradeConstant.TRADE_STATUS_REFUND
						|| tradeOrder.getTradeStatus() == TradeConstant.TRADE_STATUS_REFUNDING) {
					refund_free = ( (double) tradeOrder.getRefundFee() ) / 100.00 + refund_free;
					refund_frequency = refund_frequency + 1;
				}
			}
		}
		success_frequency = sponsor_frequency - refund_frequency;
		success_free = total_free - refund_free;
		DecimalFormat df = new DecimalFormat(DEFALUT_PAY_FREE);
		double conversion = 0.00;
		if (sponsor_frequency != 0 && success_frequency != 0) {
			conversion = ( (double) success_frequency / sponsor_frequency ) * 100;
		}
		double average_free = 0.00;
		if (success_frequency != 0) {
			average_free = ( (double) success_free / success_frequency );
		}
		//double average_free = success_frequency != 0 ? ( (double) success_free / success_frequency ) : 0.00;
		orderDataView.setTotal_free(total_free);
		orderDataView.setRefund_free(refund_free);
		orderDataView.setSuccess_free(success_free);
		orderDataView.setSponsor_frequency(sponsor_frequency);
		orderDataView.setRefund_frequency(refund_frequency);
		orderDataView.setSuccess_frequency(success_frequency);
		orderDataView.setConversion(conversion);
		orderDataView.setAverage_free(average_free);
		return orderDataView;
	}

	/**
	 * 聚合统计
	 */
	@Override
	public Map<String, Object> polymerizationStatistics(Map<String, Object> params) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			List<Condition> conditions = new ArrayList<Condition>();
			conditions.add(Restrictions.or(Restrictions.eq("tradeStatus", 2), Restrictions.eq("tradeStatus", 3)));

			if (!params.isEmpty()) {
				if (params.containsKey("startDate")) {
					Condition condition = Restrictions.gte("payTime", params.get("startDate").toString().concat(START_TIME_SUFFIX));
					conditions.add(condition);
				}
				if (params.containsKey("endDate")) {
					Condition condition = Restrictions.lte("payTime", params.get("endDate").toString().concat(END_TIME_SUFFIX));
					conditions.add(condition);
				}

				if (params.containsKey("startDate")) {
					Condition condition = Restrictions.gte("refundTime", params.remove("startDate").toString().concat(START_TIME_SUFFIX));
					conditions.add(condition);
				}
				if (params.containsKey("endDate")) {
					Condition condition = Restrictions.lte("refundTime", params.remove("endDate").toString().concat(END_TIME_SUFFIX));
					conditions.add(condition);

				}

				for (String key : params.keySet()) {
					Condition condition = Restrictions.eq(key, params.get(key));
					conditions.add(condition);
				}
			}
			LogicalCondition logicalCondition = new LogicalCondition(conditions, QueryOperationalEnum.AND);
			List<Merchant> merchants = new ArrayList<Merchant>();
			List<MerchantApplication> applications = new ArrayList<MerchantApplication>();
			if (params.containsKey("merchantNo")) {
				String merchantNo = params.get("merchantNo").toString();
				merchants.add(merchantService.findByMerchantNo(merchantNo));
				if (params.containsKey("appId")) {
					applications.add(merchantApplicationService.findApplicationByAppId(merchantNo, params.get("appId").toString()));
				} else {
					applications = merchantApplicationService.findApplicationByMechNo(merchantNo);
				}
			} else {
				merchants = merchantService.findAll();
				applications = merchantApplicationService.findAll();
			}

			/**
			 * 统计查询渠道支付数据
			 */
			resultMap.put("channelStatistics", statisticsChannel(logicalCondition));
			Map<String, Object> merchantMap = merchants.stream().collect(Collectors.toMap(Merchant::getMerchantNo, Merchant::getMerchantName));
			resultMap.put("applicationsStatistics", statisticsApplication(logicalCondition, applications, merchantMap));
			resultMap.putAll(statisticsMerchant(logicalCondition, merchants));
			//resultMap.put("application", applications);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("聚合统计所有订单结果异常:{}", e);
		}
		return resultMap;
	}

	/**
	 * 根据支付渠道统计订单
	 * @param logicalCondition
	 * @return
	 */
	public List<OrderStatistics> statisticsChannel(LogicalCondition logicalCondition) {
		List<OrderStatistics> channelStatistics = new ArrayList<OrderStatistics>();
		try {
			/**
			 * 统计查询渠道支付数据
			 */
			String channelJson =
					"{$group : { _id :{channelCode:\"$channelCode\",channelName:\"$channelName\",tradeStatus:\"$tradeStatus\"},payPrice: { $sum:\"$totalFee\" },refundPrice: {$sum:\"$refundFee\"},avgPrice: { $avg: \"$totalFee\" },count: { $sum: 1 }}}";
			List<StatisticsType> channelList = orderDao.aggregatesQuery(logicalCondition, channelJson, null, null, null, null, StatisticsType.class);
			logger.info("根据支付渠道统计结果:{}", JSON.toJSONString(channelList));
			Map<String, Object> channelMap = new HashMap<String, Object>();
			List<TradeChannel> allChannel = tradeChannelService.findAll();
			if (!CollectionUtils.isEmpty(channelList)) {
				for (StatisticsType item : channelList) {
					String key = item.getChannelCode();
					OrderStatistics os = new OrderStatistics();
					if (channelMap.containsKey(key)) {
						os = (OrderStatistics) channelMap.get(key);
					} else {
						os.setAppId(item.getAppId());
						os.setMerchantNo(item.getMerchantNo());
						os.setChannelCode(item.getChannelCode());
					}
					if (TradeConstant.TRADE_STATUS_PAYMENT == Integer.valueOf(item.getTradeStatus())) {//支付金额
						os.setPayPrice(item.getPayPrice() / 100);
						os.setPayCount(item.getCount());
					} else {//退费金额
						os.setRefundPrice(item.getRefundPrice() / 100);
						os.setRefundCount(item.getCount());
					}
					channelMap.put(key, os);
				}
			}
			if (!CollectionUtils.isEmpty(allChannel)) {
				for (TradeChannel tc : allChannel) {
					OrderStatistics statistics = new OrderStatistics();
					if (channelMap.containsKey(tc.getCode())) {
						statistics = (OrderStatistics) channelMap.get(tc.getCode());
						int launchCount = statistics.getPayCount() + statistics.getRefundCount();
						statistics.setLaunchCount(launchCount);
						if (statistics.getPayCount() != 0) {
							double conversion = (double) ( Double.valueOf(statistics.getPayCount()) / Double.valueOf(launchCount) * 100 );
							statistics.setConversion(conversion);
						}
						statistics.setTotalPrice(statistics.getPayPrice() + statistics.getRefundPrice());
						if (statistics.getPayPrice() != 0) {
							double averagePrice = (double) ( statistics.getPayPrice() / statistics.getPayCount() );
							statistics.setAveragePrice(averagePrice);
						}
					} else {
						statistics.setChannelCode(tc.getCode());
					}
					statistics.setChannelName(tc.getName());
					channelStatistics.add(statistics);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return channelStatistics;
	}

	/**
	 * 根据支付商户统计订单
	 * 同时统计总订单
	 * @param logicalCondition
	 * @return
	 */
	public Map<String, Object> statisticsMerchant(LogicalCondition logicalCondition, List<Merchant> merchants) {
		Map<String, Object> statisticsMap = new HashMap<String, Object>();
		OrderStatistics allStatistics = new OrderStatistics();
		List<OrderStatistics> merchantStatistics = new ArrayList<OrderStatistics>();
		try {
			/**
			 * 统计查询渠道支付数据
			 */
			String channelJson =
					"{$group : { _id :{merchantNo:\"$merchantNo\",tradeStatus:\"$tradeStatus\"},payPrice: { $sum:\"$totalFee\" },refundPrice: {$sum:\"$refundFee\"},avgPrice: { $avg: \"$totalFee\" },count: { $sum: 1 }}}";
			List<StatisticsType> merchantList = orderDao.aggregatesQuery(logicalCondition, channelJson, null, null, null, null, StatisticsType.class);
			logger.info("根据支付商户统计结果:{}", JSON.toJSONString(merchantList));
			if (!CollectionUtils.isEmpty(merchants)) {
				Map<String, Object> channelMap = new HashMap<String, Object>();
				for (StatisticsType item : merchantList) {
					String key = item.getMerchantNo();
					OrderStatistics os = new OrderStatistics();
					if (channelMap.containsKey(key)) {
						os = (OrderStatistics) channelMap.get(key);
					} else {
						os.setAppId(item.getAppId());
						os.setMerchantNo(item.getMerchantNo());
						os.setChannelCode(item.getChannelCode());
					}
					if (TradeConstant.TRADE_STATUS_PAYMENT == Integer.valueOf(item.getTradeStatus())) {//支付金额
						os.setPayPrice(item.getPayPrice() / 100);
						os.setPayCount(item.getCount());
						allStatistics.setTotalPrice(allStatistics.getTotalPrice() + os.getPayPrice());
						allStatistics.setPayPrice(allStatistics.getPayPrice() + os.getPayPrice());
						allStatistics.setLaunchCount(allStatistics.getLaunchCount() + os.getPayCount());
						allStatistics.setPayCount(allStatistics.getPayCount() + os.getPayCount());
					} else {//退费金额
						os.setRefundPrice(item.getRefundPrice() / 100);
						os.setRefundCount(item.getCount());
						allStatistics.setTotalPrice(allStatistics.getTotalPrice() + os.getRefundPrice());
						allStatistics.setRefundPrice(allStatistics.getRefundPrice() + os.getRefundPrice());
						allStatistics.setLaunchCount(allStatistics.getLaunchCount() + os.getRefundCount());
						allStatistics.setRefundCount(allStatistics.getRefundCount() + os.getRefundCount());
					}
					double conversion =
							(double) ( Double.valueOf(allStatistics.getPayCount()) / Double.valueOf(allStatistics.getLaunchCount()) * 100 );
					allStatistics.setConversion(conversion);
					if (allStatistics.getPayPrice() != 0) {
						double averagePrice = (double) ( allStatistics.getPayPrice() / allStatistics.getPayCount() );
						allStatistics.setAveragePrice(averagePrice);
					}
					channelMap.put(key, os);
				}
				if (!CollectionUtils.isEmpty(merchants)) {
					for (Merchant m : merchants) {
						OrderStatistics statistics = new OrderStatistics();
						if (channelMap.containsKey(m.getMerchantNo())) {
							statistics = (OrderStatistics) channelMap.get(m.getMerchantNo());
							int launchCount = statistics.getPayCount() + statistics.getRefundCount();
							statistics.setLaunchCount(launchCount);
							if (statistics.getPayCount() != 0) {
								double conversion = (double) ( Double.valueOf(statistics.getPayCount()) / Double.valueOf(launchCount) * 100 );
								statistics.setConversion(conversion);
							}
							statistics.setTotalPrice(statistics.getPayPrice() + statistics.getRefundPrice());
							if (statistics.getPayPrice() != 0) {
								double averagePrice = (double) ( statistics.getPayPrice() / statistics.getPayCount() );
								statistics.setAveragePrice(averagePrice);
							}
						}
						statistics.setMerchantNo(m.getMerchantNo());
						statistics.setMerchantName(m.getMerchantName());
						merchantStatistics.add(statistics);
					}
				}
			}
			statisticsMap.put("merchantStatistics", merchantStatistics);
			statisticsMap.put("statistics", allStatistics);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return statisticsMap;
	}

	/**
	 * 根据支付应用统计订单
	 * @param logicalCondition
	 * @return
	 */
	public List<OrderStatistics> statisticsApplication(LogicalCondition logicalCondition, List<MerchantApplication> applications,
			Map<String, Object> merchantMap) {
		List<OrderStatistics> applicationStatistics = new ArrayList<OrderStatistics>();
		try {
			/**
			 * 统计查询渠道支付数据
			 */
			String channelJson =
					"{$group : { _id :{appId:\"$appId\",tradeStatus:\"$tradeStatus\"},payPrice: { $sum:\"$totalFee\" },refundPrice: {$sum:\"$refundFee\"},avgPrice: { $avg: \"$totalFee\" },count: { $sum: 1 }}}";
			List<StatisticsType> applicationsList =
					orderDao.aggregatesQuery(logicalCondition, channelJson, null, null, null, null, StatisticsType.class);
			logger.info("根据支付应用统计结果:{}", JSON.toJSONString(applicationsList));
			Map<String, Object> channelMap = new HashMap<String, Object>();
			if (!CollectionUtils.isEmpty(applicationsList)) {
				for (StatisticsType item : applicationsList) {
					String key = item.getAppId();
					OrderStatistics os = new OrderStatistics();
					if (channelMap.containsKey(key)) {
						os = (OrderStatistics) channelMap.get(key);
					} else {
						os.setAppId(item.getAppId());
						os.setMerchantNo(item.getMerchantNo());
						os.setChannelCode(item.getChannelCode());
					}
					if (TradeConstant.TRADE_STATUS_PAYMENT == Integer.valueOf(item.getTradeStatus())) {//支付金额
						os.setPayPrice(item.getPayPrice() / 100);
						os.setPayCount(item.getCount());
					} else {//退费金额
						os.setRefundPrice(item.getRefundPrice() / 100);
						os.setRefundCount(item.getCount());
					}
					channelMap.put(key, os);
				}
			}
			if (!CollectionUtils.isEmpty(applications)) {
				for (MerchantApplication ma : applications) {
					OrderStatistics statistics = new OrderStatistics();
					if (channelMap.containsKey(ma.getAppId())) {
						statistics = (OrderStatistics) channelMap.get(ma.getAppId());
						int launchCount = statistics.getPayCount() + statistics.getRefundCount();
						statistics.setLaunchCount(launchCount);
						if (statistics.getPayCount() != 0) {
							double conversion = (double) ( Double.valueOf(statistics.getPayCount()) / Double.valueOf(launchCount) * 100 );
							statistics.setConversion(conversion);
						}
						statistics.setTotalPrice(statistics.getPayPrice() + statistics.getRefundPrice());
						if (statistics.getPayPrice() != 0) {
							double averagePrice = (double) ( statistics.getPayPrice() / statistics.getPayCount() );
							statistics.setAveragePrice(averagePrice);
						}
					}
					statistics.setAppId(ma.getAppId());
					statistics.setAppName(ma.getAppName());
					statistics.setMerchantName(merchantMap.get(ma.getMerchantNo()).toString());
					applicationStatistics.add(statistics);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return applicationStatistics;
	}

	@Override
	public Map<String, Object> chartsStatistics(Map<String, Object> params) {
		Map<String, Object> retMap = new HashMap<String, Object>();

		Map<String, String> applicationsMap = new HashMap<String, String>();
		try {
			StringBuffer groupJson = new StringBuffer("{$group : {_id : {day:{$substr:[\"$payTime\",5,5]}");
			/*if (params.containsKey("startDate") && params.containsKey("endDate")) {
				Date startDate = GlobalConstant.parseYYYYMMDD(params.get("startDate").toString());
				Date endDate = GlobalConstant.parseYYYYMMDD(params.get("endDate").toString());
				if (DateUtils.getIntervalDays(startDate, endDate) <= 30) {
					groupJson = new StringBuffer("{$group : {_id : {day:{$substr:[\"$payTime\",5,5]}");
				}
			}*/

			if (params.containsKey("merchantNo")) {
				groupJson.append(",appId:\"$appId\"");
				List<MerchantApplication> applications = merchantApplicationService.findApplicationByMechNo(params.get("merchantNo").toString());
				if (!CollectionUtils.isEmpty(applications)) {
					for (MerchantApplication ma : applications) {
						applicationsMap.put(ma.getAppId(), ma.getAppName());
					}
				}
			} else {
				applicationsMap.put(null, "金额");
			}
			groupJson.append("}, payPrice: { $sum:\"$totalFee\" },refundPrice: { $sum:\"$refundFee\" },count: { $sum: 1 }}}");
			logger.info("groupJson:{}", groupJson.toString());
			List<Condition> conditions = new ArrayList<Condition>();
			conditions.add(Restrictions.or(Restrictions.eq("tradeStatus", 2), Restrictions.eq("tradeStatus", 3)));
			conditions.add(Restrictions.ne("payTime", null));
			if (!params.isEmpty()) {
				if (params.containsKey("startDate")) {
					Condition condition = Restrictions.gte("payTime", params.get("startDate").toString().concat(START_TIME_SUFFIX));
					conditions.add(condition);
				}
				if (params.containsKey("endDate")) {
					Condition condition = Restrictions.lte("payTime", params.get("endDate").toString().concat(END_TIME_SUFFIX));
					conditions.add(condition);
				}
				if (params.containsKey("merchantNo")) {
					Condition condition = Restrictions.eq("merchantNo", params.get("merchantNo").toString());
					conditions.add(condition);
				}

				/*if (params.containsKey("appIds")) {
					List<String> appIds = (List<String>) params.remove("appIds");
					Condition condition = Restrictions.in("appId", appIds);
					conditions.add(condition);
					groupJson =
							"{$group : {_id : {day:{$substr:[\"$payTime\",0,7]},appId:\"$appId\"},  payPrice: { $sum:\"$totalFee\" },refundPrice: { $sum:\"$refundFee\" },count: { $sum: 1 }}}";
				}*/

				/*for (String key : params.keySet()) {
					Condition condition = Restrictions.eq(key, params.get(key));
					conditions.add(condition);
				}*/
			}
			LogicalCondition logicalCondition = new LogicalCondition(conditions, QueryOperationalEnum.AND);

			List<ChartStatistics> item =
					orderDao.aggregatesQuery(logicalCondition, groupJson.toString(), null, null, null, null, ChartStatistics.class);
			logger.info("item:{}", JSON.toJSONString(item));
			if (!CollectionUtils.isEmpty(item)) {
				Set<String> daySet = new HashSet<String>();
				Map<String, Object> appIdsMap = new HashMap<String, Object>();
				for (int i = 0; i < item.size(); i++) {
					List<ChartStatistics> list = new ArrayList<ChartStatistics>();
					ChartStatistics chartStatistics = item.get(i);
					String key = chartStatistics.getAppId();
					if (appIdsMap.containsKey(key)) {
						list = (List<ChartStatistics>) appIdsMap.get(key);
					}
					list.add(chartStatistics);
					appIdsMap.put(key, list);
					daySet.add(chartStatistics.getDay());
				}

				logger.info("appIdsMap:{}", JSON.toJSONString(appIdsMap));
				List<String> dayList = new ArrayList<String>(daySet);
				Collections.sort(dayList);
				Object[] objArray = dayList.toArray();
				logger.info("daySet:{}", JSON.toJSONString(objArray));
				retMap.put("dayArray", objArray);
				List<Object[]> dataList = new ArrayList<Object[]>();
				Map<String, Object> appIdStatistics = new HashMap<String, Object>();
				for (String key : appIdsMap.keySet()) {
					List<ChartStatistics> list = (List<ChartStatistics>) appIdsMap.get(key);
					String[] priceArray = new String[dayList.size()];
					for (int i = 0; i < dayList.size(); i++) {
						String day = dayList.get(i);
						for (ChartStatistics statistics : list) {
							if (day.equals(statistics.getDay())) {
								logger.info("day:{}, getDay:{}, 交易金额:{}, 退费金额:{}", day, statistics.getDay(), statistics.getPayPrice(),
										statistics.getRefundPrice());
								Long payPrice = Long.valueOf(statistics.getPayPrice());
								Long refundPrice = Long.valueOf(statistics.getRefundPrice());
								String price = BigDecimal.valueOf(payPrice - refundPrice).divide(new BigDecimal(100)).toString();
								priceArray[i] = price;
							}
							if (StringUtils.isEmpty(priceArray[i])) {
								priceArray[i] = "0.00";
							}
						}
					}
					dataList.add(priceArray);
					logger.info("priceArray:{}", JSON.toJSONString(priceArray));
					appIdStatistics.put(applicationsMap.get(key), priceArray);
				}
				retMap.put("dataList", appIdStatistics);
			}

			logger.info("retMap:{}", JSON.toJSONString(retMap));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retMap;
	}
}

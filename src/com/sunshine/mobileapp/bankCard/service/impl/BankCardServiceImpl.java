/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年10月26日下午2:14:15</p>
 *  <p> Created by 熊胆</p>
 *  </body>
 * </html>
 */
package com.sunshine.mobileapp.bankCard.service.impl;

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
import com.sunshine.common.datas.cache.component.BankCardCache;
import com.sunshine.framework.common.spring.ext.SpringContextHolder;
import com.sunshine.framework.mvc.mongodb.DBConstant.QueryOperationalEnum;
import com.sunshine.framework.mvc.mongodb.dao.BaseMongoDao;
import com.sunshine.framework.mvc.mongodb.service.impl.BaseMongoServiceImpl;
import com.sunshine.framework.mvc.mongodb.vo.Condition;
import com.sunshine.framework.mvc.mongodb.vo.LogicalCondition;
import com.sunshine.framework.mvc.mongodb.vo.QueryCondition;
import com.sunshine.framework.mvc.mongodb.vo.Restrictions;
import com.sunshine.mobileapp.bankCard.dao.impl.BankCardDaoImpl;
import com.sunshine.mobileapp.bankCard.entity.BankCard;
import com.sunshine.mobileapp.bankCard.service.BankCardService;
import com.sunshine.mobileapp.order.service.TradeOrderService;
import com.sunshine.payment.CardConstant;
import com.sunshine.platform.TradeChannelConstants;
import com.sunshine.platform.order.entity.CardStatistics;

/**
 * @Project: cashier_trade
 * @Package: com.sunshine.mobileapp.bankCard.service.impl
 * @ClassName: BankCardServiceImpl
 * @Description: 绑卡业务实现类
 * @JDK version used: 
 * @Author: 熊胆
 * @Create Date: 2017年10月26日下午2:14:15
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
@Service
public class BankCardServiceImpl extends BaseMongoServiceImpl<BankCard, Serializable> implements BankCardService {

	private static Logger logger = LoggerFactory.getLogger(BankCardServiceImpl.class);

	@Autowired
	private BankCardDaoImpl bankCardDao;

	private BankCardCache bankCardCache = SpringContextHolder.getBean(BankCardCache.class);

	@Override
	protected BaseMongoDao<BankCard, Serializable> getDao() {
		return bankCardDao;
	}

	@Override
	public BankCard createBankCard(BankCard card) {
		card.setActivateStatus(CardConstant.ACTIVATE_STATUS_NOBUNDLING);
		try {
			if (StringUtils.isEmpty(card.getId())) {
				card.setCreateTime(new Date());
				this.insert(card);
			} else {
				this.update(card);
			}
			bankCardCache.setBankCard(card);
			logger.info("创建银行卡信息:{}", JSON.toJSONString(card));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return card;
	}

	@Override
	public BankCard getBankCardByAppIdAndBankCardNo(Map<String, Object> params) {
		List<BankCard> item = this.findByParams(params);
		if (item.size() > 0) {
			return item.get(0);
		} else {
			return null;
		}
	}

	@Override
	public void updateCardForDbAndCache(BankCard bankCard) {
		bankCard.setUpdateTime(new Date());
		logger.info("银行卡号:{}, 银行卡开通状态:{},更新数据库和缓存", bankCard.getBankCardNo(), bankCard.getActivateStatus());
		this.updateNotWithNull(bankCard);
		bankCardCache.setBankCard(bankCard);
	}

	@Override
	public void updateCardForCacheByParams(BankCard bankCard) {
		Map<String, Object> conditionMap = new HashMap<>();
		conditionMap.put("_id", bankCard.getId());

		Map<String, Object> updateMap = new HashMap<>();
		updateMap.put("cardBalance", bankCard.getCardBalance());
		updateMap.put("activateStatus", bankCard.getActivateStatus());
		this.updateByParams(conditionMap, updateMap);
		bankCardCache.setBankCard(bankCard);
	}

	@Override
	public List<BankCard> getBankCardForCache(int day) {
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
		List<BankCard> item = bankCardDao.findByLogicalCondition(logicalCondition);
		return item;
	}

	@Override
	public BankCard findById(String cardId) {
		return super.findById(cardId);
	}

	/**
	 * 根据卡号和日期获取消费金额
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年11月27日下午5:02:19
	 */
	@Override
	public Integer queryTotalFeeByDate(String firstday, String lastday, String cardId, List<Integer> tradeStatus) {
		List<Condition> conditions = new ArrayList<Condition>();
		Condition condition = null;
		if (StringUtils.isNotBlank(firstday)) {
			condition = Restrictions.gte("payTime", firstday + " 00:00:00");
			conditions.add(condition);
		}
		if (StringUtils.isNotBlank(lastday)) {
			condition = Restrictions.lte("payTime", lastday + " 23:59:59");
			conditions.add(condition);
		}
		if (StringUtils.isNotBlank(cardId)) {
			condition = Restrictions.eq("cardId", cardId);
			conditions.add(condition);
		}
		if (tradeStatus.size() > 0) {
			condition = Restrictions.in("tradeStatus", tradeStatus);
			conditions.add(condition);
		}

		condition = Restrictions.eq("channelCode", TradeChannelConstants.TRADE_CHANNEL_UNIONPAY_SMALL_QUICK);
		conditions.add(condition);
		LogicalCondition logicalCondition = new LogicalCondition(conditions, QueryOperationalEnum.AND);

		String groupJson = "{ $group : { _id : null, totalPrice : { $sum:\"$totalFee\" } } }";
		TradeOrderService orderService = SpringContextHolder.getBean(TradeOrderService.class);
		List<CardStatistics> cardStatistics = orderService.aggregatesQuery(logicalCondition, groupJson, null, null, null, null, CardStatistics.class);
		Integer totalPrice = 0;
		if (cardStatistics.size() > 0) {
			totalPrice = cardStatistics.get(0).getTotalPrice();
		}
		return totalPrice;
	}

}

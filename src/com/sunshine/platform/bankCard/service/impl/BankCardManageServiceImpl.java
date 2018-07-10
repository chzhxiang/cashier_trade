/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年10月31日上午10:24:21</p>
 *  <p> Created by 熊胆</p>
 *  </body>
 * </html>
 */
package com.sunshine.platform.bankCard.service.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.sunshine.common.utils.CommonUtil;
import com.sunshine.framework.common.spring.ext.SpringContextHolder;
import com.sunshine.framework.mvc.mongodb.DBConstant.QueryOperationalEnum;
import com.sunshine.framework.mvc.mongodb.DBConstant.SortOperationalEnum;
import com.sunshine.framework.mvc.mongodb.dao.BaseMongoDao;
import com.sunshine.framework.mvc.mongodb.service.impl.BaseMongoServiceImpl;
import com.sunshine.framework.mvc.mongodb.vo.Condition;
import com.sunshine.framework.mvc.mongodb.vo.QueryCondition;
import com.sunshine.framework.utils.DateUtils;
import com.sunshine.mobileapp.bankCard.entity.BankCard;
import com.sunshine.mobileapp.bankCard.service.BankCardService;
import com.sunshine.mobileapp.bankCard.service.impl.BankCardServiceImpl;
import com.sunshine.payment.CardConstant;
import com.sunshine.payment.TradeConstant;
import com.sunshine.platform.application.entity.MerchantApplication;
import com.sunshine.platform.application.service.MerchantApplicationService;
import com.sunshine.platform.bankCard.dao.BankCardManageDao;
import com.sunshine.platform.bankCard.entity.CardParamsVo;
import com.sunshine.platform.bankCard.service.BankCardManageService;
import com.sunshine.platform.channel.service.TradeChannelService;
import com.sunshine.platform.merchant.entity.Merchant;
import com.sunshine.platform.merchant.service.MerchantService;

/**
 * @Project: cashier_trade
 * @Package: com.sunshine.platform.bankCard.service.impl
 * @ClassName: BankCardManageServiceImpl
 * @Description: 银行卡管理service实现
 * @JDK version used: 
 * @Author: 熊胆
 * @Create Date: 2017年10月31日上午10:24:21
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
@Service
public class BankCardManageServiceImpl extends BaseMongoServiceImpl<BankCard, Serializable> implements BankCardManageService {

	@Autowired
	private BankCardManageDao bankCardManageDao;

	@Autowired
	protected MerchantApplicationService merchantApplicationService;

	@Autowired
	protected MerchantService merchantService;

	@Autowired
	protected TradeChannelService tradeChannelService;

	private static final String SEARCH_ACTIVATESTATUS_KEY = "activateStatus";

	private static final String SEARCH_STARTDATE_KEY = "startDate";

	private static final String SEARCH_ENDDATE_KEY = "endDate";

	private static final String SEARCH_KEY = "createTime";

	private static final String SORT_KEY = "updateTime";
	//时间范围后缀
	private static final String START_TIME_SUFFIX = " 00:00:00";

	private static final String END_TIME_SUFFIX = " 23:59:59";

	@Override
	protected BaseMongoDao<BankCard, Serializable> getDao() {
		return bankCardManageDao;
	}

	@Override
	public PageInfo<CardParamsVo> queryByPage(Map<String, Object> params, Page<BankCard> page) {
		PageInfo<CardParamsVo> resultPageInfo = new PageInfo<>();
		try {
			List<Condition> conditions = new ArrayList<>();
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				String key = entry.getKey();
				String value = String.valueOf(entry.getValue());
				QueryCondition condition = null;
				if (key.equals(SEARCH_ACTIVATESTATUS_KEY)) {
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

			//只查询未删除的卡
			QueryCondition condition = new QueryCondition("activateStatus", CardConstant.ACTIVATE_STATUS_DEL, QueryOperationalEnum.NE);
			conditions.add(condition);

			Map<String, Integer> sortMap = new LinkedHashMap<>();
			sortMap.put(SEARCH_ACTIVATESTATUS_KEY, SortOperationalEnum.ASC.getSortVal());
			sortMap.put(SORT_KEY, SortOperationalEnum.DESC.getSortVal());
			PageInfo<BankCard> listByPage = bankCardManageDao.findListByPage(conditions, sortMap, page);
			//格式化显示数据
			List<BankCard> bankCards = listByPage.getList();
			List<CardParamsVo> resultList = new ArrayList<CardParamsVo>();
			for (BankCard bankCard : bankCards) {
				CardParamsVo cardFormate = dataFormate(bankCard);
				resultList.add(cardFormate);
			}
			resultPageInfo = new PageInfo<>(resultList);
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultPageInfo;
	}

	@Override
	public CardParamsVo dataFormate(BankCard bankCard) {
		CardParamsVo cardParamsVo = new CardParamsVo();

		if (bankCard.getCardBalance() != null) {
			String cardBalanceByYuan = BigDecimal.valueOf(Long.valueOf(bankCard.getCardBalance())).divide(new BigDecimal(100)).toString();
			cardParamsVo.setCardBalanceByYuan(cardBalanceByYuan);
		}

		if (bankCard.getSingleQuota() != null) {
			String singleQuotaByYuan = BigDecimal.valueOf(Long.valueOf(bankCard.getSingleQuota())).divide(new BigDecimal(100)).toString();
			cardParamsVo.setSingleQuotaByYuan(singleQuotaByYuan);
		}

		if (bankCard.getMonthlyQuota() != null) {
			String monthlyQuotaByYuan = BigDecimal.valueOf(Long.valueOf(bankCard.getMonthlyQuota())).divide(new BigDecimal(100)).toString();
			cardParamsVo.setMonthlyQuotaByYuan(monthlyQuotaByYuan);
		}

		if (bankCard.getDayQuota() != null) {
			String dayQuotaByYuan = BigDecimal.valueOf(Long.valueOf(bankCard.getDayQuota())).divide(new BigDecimal(100)).toString();
			cardParamsVo.setDayQuotaByYuan(dayQuotaByYuan);
		}

		//格式化身份证
		cardParamsVo.setCertificateNoStr(CommonUtil.idCardNumFront(bankCard.getCertificateNo()));

		//格式化银行卡
		cardParamsVo.setBankCardNoStr(CommonUtil.formatMedicalCard(bankCard.getBankCardNo()));

		try {
			BeanUtils.copyProperties(cardParamsVo, bankCard);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (cardParamsVo.getCreateTime() != null) {
			cardParamsVo.setCreateTimeVo(DateUtils.dateToString(cardParamsVo.getCreateTime()));
		} else {
			cardParamsVo.setCreateTimeVo("");
		}

		//格式化订单状态
		int activateStatus = cardParamsVo.getActivateStatus();
		switch (activateStatus) {
		case CardConstant.ACTIVATE_STATUS_NOBUNDLING: //待开通
			cardParamsVo.setActivateStatusVo(CardConstant.ACTIVATE_STATUS_NOBUNDLING_REMARK);
			break;
		case CardConstant.ACTIVATE_STATUS_BUNDLING: //使用中
			cardParamsVo.setActivateStatusVo(CardConstant.ACTIVATE_STATUS_BUNDLING_REMARK);
			break;
		case CardConstant.ACTIVATE_STATUS_UNBUNDLING: //停止使用
			cardParamsVo.setActivateStatusVo(CardConstant.ACTIVATE_STATUS_UNBUNDLING_REMARK);
			break;
		case CardConstant.ACTIVATE_STATUS_BALANCE_NOTENOUGH: //余额不足
			cardParamsVo.setActivateStatusVo(CardConstant.ACTIVATE_STATUS_BALANCE_NOTENOUGH_REMARK);
			break;
		case CardConstant.ACTIVATE_STATUS_PAYMENT_CONSTRAINT: //支付受限
			cardParamsVo.setActivateStatusVo(CardConstant.ACTIVATE_STATUS_PAYMENT_CONSTRAINT_REMARK);
			break;
		default:
			break;
		}
		//查询商户名称 和 应用名称（如创建订单时已入库则不需再次查询）
		String merchantNo = cardParamsVo.getMerchantNo();
		Merchant merchant = merchantService.findByMerchantNo(merchantNo);
		cardParamsVo.setMerchantName(merchant.getMerchantName());
		String appId = cardParamsVo.getAppId();
		MerchantApplication merchantApplication = merchantApplicationService.findByAppId(appId);
		cardParamsVo.setAppName(merchantApplication.getAppName());
		return cardParamsVo;
	}

	/**
	 * 查询消费的金额
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年11月27日下午5:34:16
	 */
	@Override
	public CardParamsVo queryTotalFee(CardParamsVo cardParamsVo) {
		BankCardService bankCardService = SpringContextHolder.getBean(BankCardServiceImpl.class);

		Map<String, String> months = com.sunshine.common.utils.DateUtils.getCurMonthFirstAndLastDay();
		String firstday = months.get("firstday");
		String lastday = months.get("lastday");

		List<Integer> payTradeStatu = new ArrayList<>();
		payTradeStatu.add(TradeConstant.TRADE_STATUS_PAYMENT);
		payTradeStatu.add(TradeConstant.TRADE_STATUS_REFUND);

		List<Integer> refundTradeStatu = new ArrayList<>();
		refundTradeStatu.add(TradeConstant.TRADE_STATUS_REFUND);

		//获取单月消费的总金额
		Integer payMoneyByCurMonth = bankCardService.queryTotalFeeByDate(firstday, lastday, cardParamsVo.getId(), payTradeStatu);
		String pyByMonth = BigDecimal.valueOf(Long.valueOf(payMoneyByCurMonth)).divide(new BigDecimal(100)).toString();
		cardParamsVo.setPayMoneyByCurMonth(pyByMonth);

		//获取单月退费的总金额
		Integer refundMoneyByCurMonth = bankCardService.queryTotalFeeByDate(firstday, lastday, cardParamsVo.getId(), refundTradeStatu);
		String ryByMonth = BigDecimal.valueOf(Long.valueOf(refundMoneyByCurMonth)).divide(new BigDecimal(100)).toString();
		cardParamsVo.setRefundMoneyByCurMonth(ryByMonth);

		String curDay = DateUtils.getDateStr(new Date());
		//获取单天消费的总金额
		Integer payMoneyByCurDay = bankCardService.queryTotalFeeByDate(curDay, curDay, cardParamsVo.getId(), payTradeStatu);
		String pyByDay = BigDecimal.valueOf(Long.valueOf(payMoneyByCurDay)).divide(new BigDecimal(100)).toString();
		cardParamsVo.setPayMoneyByCurDay(pyByDay);

		//获取单天退费的总金额
		Integer refundMoneyByCurDay = bankCardService.queryTotalFeeByDate(curDay, curDay, cardParamsVo.getId(), refundTradeStatu);
		String ryByDay = BigDecimal.valueOf(Long.valueOf(refundMoneyByCurDay)).divide(new BigDecimal(100)).toString();
		cardParamsVo.setRefundMoneyByCurDay(ryByDay);
		return cardParamsVo;
	}

}

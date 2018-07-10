/**
 * <html>
 * <body>
 *  <P> Copyright 2017 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年10月11日</p>
 *  <p> Created by 党参</p>
 *  </body>
 * </html>
 */
package com.sunshine.task.biz.taskitem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunshine.common.utils.DateUtils;
import com.sunshine.framework.common.spring.ext.SpringContextHolder;
import com.sunshine.mobileapp.bankCard.entity.BankCard;
import com.sunshine.mobileapp.bankCard.service.BankCardService;
import com.sunshine.mobileapp.bankCard.service.impl.BankCardServiceImpl;
import com.sunshine.payment.CardConstant;
import com.sunshine.payment.TradeConstant;

/**
 * @Project: cashier_trade
 * @Package: com.sunshine.task.biz.taskitem
 * @ClassName: HandleCardStatusTask
 * @Description: 处理银行卡状态
 * @JDK version used: 
 * @Author: 熊胆
 * @Create Date: 2017年11月21日下午3:51:57
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class HandleCardStatusTask {
	public static Logger logger = LoggerFactory.getLogger(HandleCardStatusTask.class);

	public HandleCardStatusTask() {
		super();
	}

	public void startUp() {
		// TODO Auto-generated method stub
		logger.info("处理银行卡状态开始...................................");
		Long statrTime = Calendar.getInstance().getTimeInMillis();

		BankCardService bankCardService = SpringContextHolder.getBean(BankCardServiceImpl.class);
		Map<String, Object> map = new HashMap<>();
		map.put("activateStatus", CardConstant.ACTIVATE_STATUS_PAYMENT_CONSTRAINT);
		List<BankCard> cards = bankCardService.findByParams(map);
		for (BankCard card : cards) {
			Map<String, String> months = DateUtils.getCurMonthFirstAndLastDay();
			String firstday = months.get("firstday");
			String lastday = months.get("lastday");

			List<Integer> tradeStatus = new ArrayList<>();
			tradeStatus.add(TradeConstant.TRADE_STATUS_PAYMENT);
			Integer totalPrice = bankCardService.queryTotalFeeByDate(firstday, lastday, card.getId(), tradeStatus);
			//消费当月的总金额小于等于月限额
			if (totalPrice.intValue() <= card.getMonthlyQuota().intValue()) {
				card.setActivateStatus(CardConstant.ACTIVATE_STATUS_BUNDLING);
				bankCardService.updateCardForDbAndCache(card);
			}

		}

		Long endTime = Calendar.getInstance().getTimeInMillis();
		logger.info("处理银行卡状态结束 ,耗费时间" + ( endTime - statrTime ) + " Millis");
	}

}

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
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.sunshine.framework.common.spring.ext.SpringContextHolder;
import com.sunshine.framework.config.SystemConfig;
import com.sunshine.framework.mvc.mongodb.vo.Condition;
import com.sunshine.framework.mvc.mongodb.vo.Restrictions;
import com.sunshine.framework.utils.DateUtils;
import com.sunshine.mobileapp.bankCard.entity.BankCard;
import com.sunshine.mobileapp.bankCard.service.BankCardService;
import com.sunshine.mobileapp.bankCard.service.impl.BankCardServiceImpl;
import com.sunshine.payment.CardConstant;
import com.sunshine.payment.TradeConstant;
import com.sunshine.payment.utils.InvokeDubboUtils;
import com.sunshine.platform.RuleConstant;
import com.sunshine.platform.rule.entity.SysRuleConfig;
import com.sunshine.platform.rule.service.SysRuleConfigService;
import com.sunshine.platform.rule.service.impl.SysRuleConfigServiceImpl;

/**
 * @Project: cashier_trade
 * @Package: com.sunshine.task.biz.taskitem
 * @ClassName: HandleCardPayLimitTask
 * @Description: 处理银行卡支付受限的状态
 * @JDK version used: 
 * @Author: 熊胆
 * @Create Date: 2017年12月6日下午2:01:42
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class HandleCardPayLimitTask {
	public static Logger logger = LoggerFactory.getLogger(HandleCardPayLimitTask.class);

	/**
	 * 发送短信的手机
	 */
	public static final String LIMIT_PAY_MOBILE = SystemConfig.getStringValue("limit_pay_mobile");

	public HandleCardPayLimitTask() {
		super();
	}

	public void startUp() {
		// TODO Auto-generated method stub
		logger.info("银行卡支付受限处理开始...................................");
		Long statrTime = Calendar.getInstance().getTimeInMillis();

		SysRuleConfigService sysRuleConfigService = SpringContextHolder.getBean(SysRuleConfigServiceImpl.class);
		SysRuleConfig sysRuleConfig = sysRuleConfigService.findByRuleCode(RuleConstant.CONSUMPTION_AMOUNT_LIMIT_RULE);
		Double str = Double.parseDouble(sysRuleConfig.getRuleValue()) * 100;
		Integer ruleValue = str.intValue();

		BankCardService bankCardService = SpringContextHolder.getBean(BankCardServiceImpl.class);
		List<Integer> activateStatus = new ArrayList<>();
		activateStatus.add(CardConstant.ACTIVATE_STATUS_BUNDLING);
		Condition condition = Restrictions.in("activateStatus", activateStatus);
		List<BankCard> cards = bankCardService.findByCondition(condition);
		for (BankCard card : cards) {
			String curDay = DateUtils.getDateStr(new Date());
			List<Integer> tradeStatus = new ArrayList<>();
			tradeStatus.add(TradeConstant.TRADE_STATUS_PAYMENT);
			tradeStatus.add(TradeConstant.TRADE_STATUS_REFUND);
			Integer totalPrice = bankCardService.queryTotalFeeByDate(curDay, curDay, card.getId(), tradeStatus);
			logger.info("时间：{}, 银行卡号：{}, 当天消费的总金额：{}, 消费金额上限：{},是否超过限额：{}", curDay, card.getBankCardNo(), totalPrice.intValue(), ruleValue.intValue(),
					totalPrice.intValue() > ruleValue.intValue());
			//消费当天的总金额大于消费金额上限
			if (totalPrice.intValue() > ruleValue.intValue()) {
				card.setActivateStatus(CardConstant.ACTIVATE_STATUS_PAYMENT_CONSTRAINT);
				bankCardService.updateCardForDbAndCache(card);

				String content = "健康钱包：" + card.getUserName() + "的" + card.getBankCardNo() + "银行卡当日消费金额超过阀值，请登录后台核查，谢谢";
				String[] mobiles = LIMIT_PAY_MOBILE.split(",");
				for (String mobile : mobiles) {
					Map<String, Object> result = InvokeDubboUtils.sendMsgValidate(mobile, content);
					logger.info("发送短信返回的结果：{}，手机号：{}", JSON.toJSONString(result), mobile);
				}

			}
		}

		Long endTime = Calendar.getInstance().getTimeInMillis();
		logger.info("银行卡支付受限处理结束 ,耗费时间" + ( endTime - statrTime ) + " Millis");
	}

}

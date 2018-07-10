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

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.sunshine.common.GlobalConstant;
import com.sunshine.common.OrderNoGenerator;
import com.sunshine.mobileapp.bankCard.entity.BankCard;
import com.sunshine.mobileapp.bankCard.service.BankCardService;
import com.sunshine.payment.CardConstant;
import com.sunshine.payment.TradeConstant;
import com.sunshine.payment.unionpay.UnionCardUtils;
import com.sunshine.platform.TradeChannelConstants;
import com.sunshine.platform.applicationchannel.service.impl.ApplicationChannelServiceImpl;
import com.sunshine.restful.utils.RestUtils;
import com.sunshine.trade.service.BindCardService;

/**
 * @Project: cashier_trade
 * @Package: com.sunshine.trade.service.impl
 * @ClassName: BindCardServiceImpl
 * @Description: 绑卡业务处理类
 * @JDK version used: 
 * @Author: 熊胆
 * @Create Date: 2017年10月27日下午3:36:58
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
@Service
public class BindCardServiceImpl implements BindCardService {

	private static Logger logger = LoggerFactory.getLogger(BindCardServiceImpl.class);

	@Autowired
	private BankCardService bankCardService;

	@Autowired
	private ApplicationChannelServiceImpl channelService;

	@Override
	public Map<String, Object> polymerizationBindCard(BankCard card, String channelCode) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("appId", card.getAppId());
		params.put("bankCardNo", card.getBankCardNo());

		card.setReturnUrl(RestUtils.getTradeDomainUrl().concat(UnionCardUtils.UNION_CARD_RETURN_URL));
		BankCard bankCard = bankCardService.getBankCardByAppIdAndBankCardNo(params);
		if (bankCard != null) {
			logger.info("获取银行卡信息: {}", JSON.toJSONString(bankCard));
			if (bankCard.getActivateStatus().equals(CardConstant.ACTIVATE_STATUS_UNBUNDLING)) {//绑卡状态为解绑

				Map<String, Object> queryMap = new HashMap<>();
				if (TradeChannelConstants.TRADE_CHANNEL_UNIONPAY_SMALL_QUICK.equals(channelCode)) {
					queryMap = UnionCardUtils.unionBindCardQueryService(card);
				}

				Boolean isException = (Boolean) queryMap.get(GlobalConstant.TRADE_IS_EXCEPTION);
				Boolean isSuccess = (Boolean) queryMap.get(GlobalConstant.TRADE_IS_SUCCESS);
				Integer activateStatus = Integer.parseInt((String) queryMap.get("activateStatus"));
				if (!isException && isSuccess && activateStatus.equals(CardConstant.ACTIVATE_STATUS_BUNDLING)) {
					bankCard.setActivateStatus(CardConstant.ACTIVATE_STATUS_BUNDLING);
				}
				String queryJson = (String) queryMap.get(GlobalConstant.TRADE_SUCCESS_DATA);
				bankCard.setCardQueryJson(queryJson);
				bankCardService.updateCardForDbAndCache(bankCard);

				params.put("returnUrl", bankCard.getReturnUrl());
				params.putAll(queryMap);
			} else if (bankCard.getActivateStatus().equals(CardConstant.ACTIVATE_STATUS_NOBUNDLING)
					|| bankCard.getActivateStatus().equals(CardConstant.ACTIVATE_STATUS_DEL)) {//绑卡状态为未开通或删除

				Map<String, Object> retMap = UnionCardUtils.unionBindCardService(bankCard);
				Boolean isException = (Boolean) retMap.get(GlobalConstant.TRADE_IS_EXCEPTION);
				Boolean isSuccess = (Boolean) retMap.get(GlobalConstant.TRADE_IS_SUCCESS);
				if (!isException && isSuccess) {
					params.putAll(retMap);
				}
			} else { //绑卡状态为已开通
				params.put(GlobalConstant.TRADE_IS_EXCEPTION, false);
				params.put(GlobalConstant.TRADE_IS_SUCCESS, true);
				params.put("returnUrl", bankCard.getReturnUrl());
			}
		} else {
			// 生成订单号
			int channelVal = channelService.channeCodeConvertVal(channelCode);
			int tradePlatformVal = channelService.getChannelIdentityWithValue(channelCode);
			card.setCardSerialNo(OrderNoGenerator.genOrderNo(OrderNoGenerator.BIZ_CODE_TIE_CARD, tradePlatformVal, channelVal));
			card.setIsSuccess(TradeConstant.ORDER_SUCCESS_FALSE);
			BankCard newCard = bankCardService.createBankCard(card);
			Map<String, Object> retMap = UnionCardUtils.unionBindCardService(newCard);
			Boolean isException = (Boolean) retMap.get(GlobalConstant.TRADE_IS_EXCEPTION);
			Boolean isSuccess = (Boolean) retMap.get(GlobalConstant.TRADE_IS_SUCCESS);
			if (!isException && isSuccess) {
				params.putAll(retMap);
			}
		}
		return params;
	}

}

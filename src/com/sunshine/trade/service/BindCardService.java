package com.sunshine.trade.service;

import java.util.Map;

import com.sunshine.mobileapp.bankCard.entity.BankCard;

/**
 * @Project: cashier_trade
 * @Package: com.sunshine.trade.service
 * @ClassName: BindCardService
 * @Description: 绑卡Service接口API
 * @JDK version used: 
 * @Author: 熊胆
 * @Create Date: 2017年10月27日下午3:31:15
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public interface BindCardService {

	/**
	 * 聚合绑卡申请
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年10月27日下午3:37:51
	 */
	Map<String, Object> polymerizationBindCard(BankCard card, String channelCode);

}

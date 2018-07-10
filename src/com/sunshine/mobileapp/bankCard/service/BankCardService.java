/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年10月26日下午2:05:06</p>
 *  <p> Created by 熊胆</p>
 *  </body>
 * </html>
 */
package com.sunshine.mobileapp.bankCard.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.sunshine.framework.mvc.mongodb.service.BaseMongoService;
import com.sunshine.mobileapp.bankCard.entity.BankCard;

/**
 * @Project: cashier_trade
 * @Package: com.sunshine.mobileapp.bankCard.service
 * @ClassName: BankCardService
 * @Description: 绑卡业务接口类
 * @JDK version used: 
 * @Author: 熊胆
 * @Create Date: 2017年10月26日下午2:05:06
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public interface BankCardService extends BaseMongoService<BankCard, Serializable> {

	/**
	 * 根据卡号查询银行卡信息
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年10月27日下午4:53:33
	 */
	BankCard getBankCardByAppIdAndBankCardNo(Map<String, Object> params);

	/**
	 * 更新银行卡及缓存
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年10月29日上午10:34:54
	 */
	void updateCardForDbAndCache(BankCard bankCard);

	/**
	 * 修改指定字段
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年12月18日下午5:33:27
	 */
	void updateCardForCacheByParams(BankCard bankCard);

	/**
	 * 查询银行卡信息(加载到缓存)
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年10月29日上午10:44:23
	 */
	List<BankCard> getBankCardForCache(int day);

	/**
	 * 创建银行卡信息
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年10月29日上午11:48:13
	 */
	BankCard createBankCard(BankCard card);

	/**
	 * 根据id查询银行卡信息
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年10月30日上午9:36:06
	 */
	BankCard findById(String cardId);

	/**
	 * 根据卡号和日期获取消费金额
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年12月6日下午1:51:47
	 */
	Integer queryTotalFeeByDate(String firstday, String lastday, String cardId, List<Integer> tradeStatus);

}

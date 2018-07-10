/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年10月31日上午10:15:53</p>
 *  <p> Created by 熊胆</p>
 *  </body>
 * </html>
 */
package com.sunshine.platform.bankCard.service;

import java.io.Serializable;
import java.util.Map;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.sunshine.framework.mvc.mongodb.service.BaseMongoService;
import com.sunshine.mobileapp.bankCard.entity.BankCard;
import com.sunshine.platform.bankCard.entity.CardParamsVo;

/**
 * @Project: cashier_trade
 * @Package: com.sunshine.platform.bankCard.service
 * @ClassName: BankCardManageService
 * @Description: <p>银行卡service接口层</p>
 * @JDK version used: 
 * @Author: 熊胆
 * @Create Date: 2017年10月31日上午10:15:53
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public interface BankCardManageService extends BaseMongoService<BankCard, Serializable> {

	/**
	 * 分页查询
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年10月31日上午10:16:57
	 */
	PageInfo<CardParamsVo> queryByPage(Map<String, Object> params, Page<BankCard> page);

	/**
	 * 格式化银行卡信息
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年10月31日下午2:49:22
	 */
	CardParamsVo dataFormate(BankCard bankCard);

	/**
	 * 查询消费的金额
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年11月27日下午5:34:16
	 */
	CardParamsVo queryTotalFee(CardParamsVo cardParamsVo);

}

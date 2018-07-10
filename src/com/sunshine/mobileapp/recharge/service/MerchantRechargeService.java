/**
 * <html>
 * <body>
 *  <P> Copyright 2017 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年10月27日</p>
 *  <p> Created by 党参</p>
 *  </body>
 * </html>
 */
package com.sunshine.mobileapp.recharge.service;

import java.io.Serializable;
import java.util.List;

import com.sunshine.framework.mvc.mongodb.service.BaseMongoService;
import com.sunshine.mobileapp.recharge.entity.MerchantRecharge;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.mobileapp.deposit.service
 * @ClassName: DepositService
 * @Description: <p></p>
 * @JDK version used: 
 * @Author: 党参
 * @Create Date: 2017年10月27日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public interface MerchantRechargeService extends BaseMongoService<MerchantRecharge, Serializable> {
	List<MerchantRecharge> findParams(List<String> fileds);
}

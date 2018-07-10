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
package com.sunshine.mobileapp.recharge.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sunshine.framework.mvc.mongodb.dao.BaseMongoDao;
import com.sunshine.framework.mvc.mongodb.service.impl.BaseMongoServiceImpl;
import com.sunshine.mobileapp.recharge.dao.MerchantRechargeDao;
import com.sunshine.mobileapp.recharge.entity.MerchantRecharge;
import com.sunshine.mobileapp.recharge.service.MerchantRechargeService;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.mobileapp.deposit.service.impl
 * @ClassName: DepositServiceImpl
 * @Description: <p></p>
 * @JDK version used: 
 * @Author: 党参
 * @Create Date: 2017年10月27日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
@Service
public class MerchantRechargeServiceImpl extends BaseMongoServiceImpl<MerchantRecharge, Serializable> implements MerchantRechargeService {

	@Autowired
	private MerchantRechargeDao depositDao;

	@Override
	protected BaseMongoDao<MerchantRecharge, Serializable> getDao() {
		return depositDao;
	}

	@Override
	public List<MerchantRecharge> findParams(List<String> fileds) {
		return depositDao.findAll(fileds);
	}

}

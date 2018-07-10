/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年8月22日</p>
 *  <p> Created by 赤芍</p>
 *  </body>
 * </html>
 */
package com.sunshine.platform.merchant.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sunshine.common.datas.cache.platform.merchant.MerchantCache;
import com.sunshine.framework.common.spring.ext.SpringContextHolder;
import com.sunshine.framework.mvc.mysql.dao.BaseDao;
import com.sunshine.framework.mvc.mysql.service.impl.BaseServiceImpl;
import com.sunshine.platform.merchant.dao.MerchantDao;
import com.sunshine.platform.merchant.entity.Merchant;
import com.sunshine.platform.merchant.service.MerchantService;

/**
 * 
 * @Project: cashier_trade 
 * @Package: com.sunshine.platform.merchant.service.impl
 * @ClassName: MerchantServiceImpl
 * @Description: <p></p>
 * @JDK version used: 
 * @Author: 赤芍
 * @Create Date: 2017年9月12日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
@Service
public class MerchantServiceImpl extends BaseServiceImpl<Merchant, String> implements MerchantService {
	private MerchantCache merchantCache = SpringContextHolder.getBean(MerchantCache.class);
	@Autowired
	protected MerchantDao merchantDao;

	@Override
	protected BaseDao<Merchant, String> getDao() {
		return merchantDao;
	}

	@Override
	public Merchant findByMerchantNo(String merchantNo) {
		MerchantCache merchantCache = SpringContextHolder.getBean(MerchantCache.class);
		Merchant merchant = merchantCache.getMerchantByMerchantNo(merchantNo);
		if (merchant == null) {
			merchant = merchantDao.findByMerchantNo(merchantNo);
		}

		return merchant;
	}

	@Override
	public List<Merchant> getAllMerchant() {
		List<Merchant> allMerchant = null;
		allMerchant = merchantCache.getAllMerchant();
		if (allMerchant == null || allMerchant.size() < 1) {
			//从数据库中再次获取
			allMerchant = merchantDao.findAll();
		}
		return allMerchant;
	}

}

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
package com.sunshine.platform.merchant.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.sunshine.framework.exception.SystemException;
import com.sunshine.framework.mvc.mysql.dao.impl.BaseDaoImpl;
import com.sunshine.platform.merchant.dao.MerchantDao;
import com.sunshine.platform.merchant.entity.Merchant;

/**
 * 
 * @Project: cashier_trade 
 * @Package: com.sunshine.platform.merchant.dao.impl
 * @ClassName: MerchantDaoImpl
 * @Description: <p></p>
 * @JDK version used: 
 * @Author: 赤芍
 * @Create Date: 2017年9月12日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
@Repository
public class MerchantDaoImpl extends BaseDaoImpl<Merchant, String> implements MerchantDao {

	private static Logger logger = LoggerFactory.getLogger(MerchantDaoImpl.class);

	private final static String SQLNAME_FIND_BY_MERCHANT_NO = "findByMerchantNo";

	/* (non-Javadoc)
	 * @see com.sunshine.platform.merchant.dao.MerchantDao#findByMerchantNo(java.lang.String)
	 */
	@Override
	public Merchant findByMerchantNo(String merchantNo) {
		try {
			return sqlSession.selectOne(getSqlName(SQLNAME_FIND_BY_MERCHANT_NO), merchantNo);
		} catch (Exception e) {
			logger.error(String.format("根据merchantNo查询商户出错！语句：%s", getSqlName(SQLNAME_FIND_BY_MERCHANT_NO)), e);
			throw new SystemException(String.format("根据merchantNo查询商户出错！语句：%s", getSqlName(SQLNAME_FIND_BY_MERCHANT_NO)), e);
		}
	}

}

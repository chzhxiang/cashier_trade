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
package com.sunshine.platform.application.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.sunshine.framework.exception.SystemException;
import com.sunshine.framework.mvc.mysql.dao.impl.BaseDaoImpl;
import com.sunshine.platform.application.dao.MerchantApplicationDao;
import com.sunshine.platform.application.entity.MerchantApplication;

/**
 * 
 * @Project: cashier_trade 
 * @Package: com.sunshine.platform.application.dao.impl
 * @ClassName: MerchantApplicationDaoImpl
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
public class MerchantApplicationDaoImpl extends BaseDaoImpl<MerchantApplication, String> implements MerchantApplicationDao {

	private static Logger logger = LoggerFactory.getLogger(MerchantApplicationDaoImpl.class);

	private final static String SQLNAME_FIND_BY_MERCHANT_NO = "findByMerchantNo";

	private final static String SQLNAME_FIND_BY_APPID = "findByAppId";

	private final static String SQLNAME_FIND_BY_MERCHANT_NO_APPID = "findByMerchantNoAndAppID";

	/* (non-Javadoc)
	 * @see com.sunshine.platform.merchant.dao.MerchantDao#findByMerchantNo(java.lang.String)
	 */
	@Override
	public List<MerchantApplication> findByMerchantNo(String merchantNo) {
		try {
			return sqlSession.selectList(getSqlName(SQLNAME_FIND_BY_MERCHANT_NO), merchantNo);
		} catch (Exception e) {
			logger.error(String.format("根据merchantNo查询应用出错！语句：%s", getSqlName(SQLNAME_FIND_BY_MERCHANT_NO)), e);
			throw new SystemException(String.format("根据merchantNo查询应用出错！语句：%s", getSqlName(SQLNAME_FIND_BY_MERCHANT_NO)), e);
		}
	}

	/* (non-Javadoc)
	 * @see com.sunshine.platform.application.dao.MerchantApplicationDao#findByMerchantNoAndAppID(java.lang.String, java.lang.String)
	 */
	@Override
	public MerchantApplication findByMerchantNoAndAppID(String merchantNo, String appId) {
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("merchantNo", merchantNo);
			map.put("appId", appId);
			return sqlSession.selectOne(getSqlName(SQLNAME_FIND_BY_MERCHANT_NO_APPID), map);
		} catch (Exception e) {
			logger.error(String.format("根据merchantNo查询应用出错！语句：%s", getSqlName(SQLNAME_FIND_BY_MERCHANT_NO_APPID)), e);
			throw new SystemException(String.format("根据merchantNo查询应用出错！语句：%s", getSqlName(SQLNAME_FIND_BY_MERCHANT_NO_APPID)), e);
		}
	}

	/* (non-Javadoc)
	 * @see com.sunshine.platform.application.dao.MerchantApplicationDao#findAppId(java.lang.String)
	 */
	@Override
	public MerchantApplication findByAppId(String appId) {
		try {
			return sqlSession.selectOne(getSqlName(SQLNAME_FIND_BY_APPID), appId);
		} catch (Exception e) {
			logger.error(String.format("根据AppId查询应用出错！语句：%s", getSqlName(SQLNAME_FIND_BY_APPID)), e);
			throw new SystemException(String.format("根据AppId查询应用出错！语句：%s", getSqlName(SQLNAME_FIND_BY_APPID)), e);
		}
	}

}

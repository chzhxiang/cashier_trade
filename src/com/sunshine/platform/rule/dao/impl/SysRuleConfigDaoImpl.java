/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年11月29日</p>
 *  <p> Created by 熊胆</p>
 *  </body>
 * </html>
 */
package com.sunshine.platform.rule.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.sunshine.framework.exception.SystemException;
import com.sunshine.framework.mvc.mysql.dao.impl.BaseDaoImpl;
import com.sunshine.platform.rule.dao.SysRuleConfigDao;
import com.sunshine.platform.rule.entity.SysRuleConfig;

/**
 * @Project: cashier_trade
 * @Package: com.sunshine.platform.rule.dao.impl
 * @ClassName: SysRuleConfigDaoImpl
 * @Description: 规则配置dao层
 * @JDK version used: 
 * @Author: 熊胆
 * @Create Date: 2017年11月29日上午10:31:40
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
@Repository
public class SysRuleConfigDaoImpl extends BaseDaoImpl<SysRuleConfig, String> implements SysRuleConfigDao {

	private static Logger logger = LoggerFactory.getLogger(SysRuleConfigDaoImpl.class);

	private final static String SQLNAME_FIND_BY_RULE_CODE = "findByRuleCode";

	/* (non-Javadoc)
	 * @see com.sunshine.platform.merchant.dao.MerchantDao#findByMerchantNo(java.lang.String)
	 */
	@Override
	public SysRuleConfig findByRuleCode(String ruleCode) {
		try {
			return sqlSession.selectOne(getSqlName(SQLNAME_FIND_BY_RULE_CODE), ruleCode);
		} catch (Exception e) {
			logger.error(String.format("根据ruleCode查询规则出错！语句：%s", getSqlName(SQLNAME_FIND_BY_RULE_CODE)), e);
			throw new SystemException(String.format("根据merchantNo查询规则出错！语句：%s", getSqlName(SQLNAME_FIND_BY_RULE_CODE)), e);
		}
	}

}

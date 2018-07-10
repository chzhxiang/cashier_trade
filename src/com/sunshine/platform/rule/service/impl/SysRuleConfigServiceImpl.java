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
package com.sunshine.platform.rule.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sunshine.framework.mvc.mysql.dao.BaseDao;
import com.sunshine.framework.mvc.mysql.service.impl.BaseServiceImpl;
import com.sunshine.platform.rule.dao.SysRuleConfigDao;
import com.sunshine.platform.rule.entity.SysRuleConfig;
import com.sunshine.platform.rule.service.SysRuleConfigService;

/**
 * @Project: cashier_trade
 * @Package: com.sunshine.platform.rule.service.impl
 * @ClassName: SysRuleConfigServiceImpl
 * @Description: 规则配置实现类
 * @JDK version used: 
 * @Author: 熊胆
 * @Create Date: 2017年11月29日上午10:25:11
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
@Service
public class SysRuleConfigServiceImpl extends BaseServiceImpl<SysRuleConfig, String> implements SysRuleConfigService {
	@Autowired
	protected SysRuleConfigDao sysRuleConfigDao;

	@Override
	protected BaseDao<SysRuleConfig, String> getDao() {
		return sysRuleConfigDao;
	}

	@Override
	public SysRuleConfig findByRuleCode(String ruleCode) {
		return sysRuleConfigDao.findByRuleCode(ruleCode);
	}

}

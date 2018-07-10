/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年11月29日上午10:24:14</p>
 *  <p> Created by 熊胆</p>
 *  </body>
 * </html>
 */
package com.sunshine.platform.rule.dao;

import com.sunshine.framework.mvc.mysql.dao.BaseDao;
import com.sunshine.platform.rule.entity.SysRuleConfig;

/**
 * @Project: cashier_trade
 * @Package: com.sunshine.platform.rule.dao
 * @ClassName: SysRuleConfigDao
 * @Description: <p></p>
 * @JDK version used: 
 * @Author: 熊胆
 * @Create Date: 2017年11月29日上午10:24:14
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public interface SysRuleConfigDao extends BaseDao<SysRuleConfig, String> {

	/**
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年11月29日下午2:33:55
	 */
	SysRuleConfig findByRuleCode(String ruleCode);

}

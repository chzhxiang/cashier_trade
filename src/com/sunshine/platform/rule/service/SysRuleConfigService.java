/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年11月29日上午10:02:39</p>
 *  <p> Created by 熊胆</p>
 *  </body>
 * </html>
 */
package com.sunshine.platform.rule.service;

import com.sunshine.framework.mvc.service.BaseService;
import com.sunshine.platform.rule.entity.SysRuleConfig;

/**
 * @Project: cashier_trade
 * @Package: com.sunshine.platform.rule.service
 * @ClassName: SysRuleConfigService
 * @Description: 规则配置接口类
 * @JDK version used: 
 * @Author: 熊胆
 * @Create Date: 2017年11月29日上午10:02:39
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public interface SysRuleConfigService extends BaseService<SysRuleConfig, String> {

	/**
	 * 根据规则code添加
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年11月29日下午2:11:21
	 */
	SysRuleConfig findByRuleCode(String ruleCode);

}

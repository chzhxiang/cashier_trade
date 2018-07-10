/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年11月29日下午2:54:14</p>
 *  <p> Created by 熊胆</p>
 *  </body>
 * </html>
 */
package com.sunshine.platform;

/**
 * @Project: cashier_trade
 * @Package: com.sunshine.platform
 * @ClassName: RuleConstant
 * @Description: 规则配置常量
 * @JDK version used: 
 * @Author: 熊胆
 * @Create Date: 2017年11月29日下午2:54:14
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class RuleConstant {

	/**
	* 银行余额限制
	*/
	public final static String BANKCARD_BALANCE_LIMIT_RULE = "bankcard_balance_limit_rule";

	/**
	 * 当日消费金额上限设置
	 */
	public final static String CONSUMPTION_AMOUNT_LIMIT_RULE = "consumption_amount_limit_rule";

	/**
	 * 订单金额阀值设置
	 */
	public final static Integer ORDER_THRESHOLD_LIMIT = 100000;

}

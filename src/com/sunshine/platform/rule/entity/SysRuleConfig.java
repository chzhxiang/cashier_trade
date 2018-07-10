/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年11月29日上午9:49:41</p>
 *  <p> Created by 熊胆</p>
 *  </body>
 * </html>
 */
package com.sunshine.platform.rule.entity;

import com.sunshine.framework.mvc.mysql.entity.BaseSQLEntity;

/**
 * @Project: cashier_trade
 * @Package: com.sunshine.platform.rule.entity
 * @ClassName: SysRuleConfig
 * @Description: 规则配置
 * @JDK version used: 
 * @Author: 熊胆
 * @Create Date: 2017年11月29日上午9:49:41
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class SysRuleConfig extends BaseSQLEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2116005972494992467L;

	/**
	 * 规则编码
	 */
	private String ruleCode;

	/**
	 * 规则值
	 */
	private String ruleValue;

	/**
	 * 扩展属性1
	 */
	private String attr1;

	/**
	 * 扩展属性2
	 */
	private String attr2;

	/**
	 * 扩展属性3
	 */
	private String attr3;

	/**
	 * 扩展属性4
	 */
	private String attr4;

	/**
	 * 扩展属性5
	 */
	private String attr5;

	/**
	 * 扩展属性6
	 */
	private String attr6;

	/**
	 * 是否有效 1 有效 0 无效
	 */
	private Integer flag;

	/**
	 * 备注
	 */
	private String remark;

	/** 
	* 获取规则编码 
	* @return ruleCode
	*/
	public String getRuleCode() {
		return ruleCode;
	}

	/** 
	* 设置规则编码 
	* @param ruleCode
	*/
	public void setRuleCode(String ruleCode) {
		this.ruleCode = ruleCode;
	}

	/** 
	* 获取规则值 
	* @return ruleValue
	*/
	public String getRuleValue() {
		return ruleValue;
	}

	/** 
	* 设置规则值 
	* @param ruleValue
	*/
	public void setRuleValue(String ruleValue) {
		this.ruleValue = ruleValue;
	}

	/** 
	* 获取扩展属性1 
	* @return attr1
	*/
	public String getAttr1() {
		return attr1;
	}

	/** 
	* 设置扩展属性1 
	* @param attr1
	*/
	public void setAttr1(String attr1) {
		this.attr1 = attr1;
	}

	/** 
	* 获取扩展属性2 
	* @return attr2
	*/
	public String getAttr2() {
		return attr2;
	}

	/** 
	* 设置扩展属性2 
	* @param attr2
	*/
	public void setAttr2(String attr2) {
		this.attr2 = attr2;
	}

	/** 
	* 获取扩展属性3 
	* @return attr3
	*/
	public String getAttr3() {
		return attr3;
	}

	/** 
	* 设置扩展属性3 
	* @param attr3
	*/
	public void setAttr3(String attr3) {
		this.attr3 = attr3;
	}

	/** 
	* 获取扩展属性4 
	* @return attr4
	*/
	public String getAttr4() {
		return attr4;
	}

	/** 
	* 设置扩展属性4 
	* @param attr4
	*/
	public void setAttr4(String attr4) {
		this.attr4 = attr4;
	}

	/** 
	* 获取扩展属性5 
	* @return attr5
	*/
	public String getAttr5() {
		return attr5;
	}

	/** 
	* 设置扩展属性5 
	* @param attr5
	*/
	public void setAttr5(String attr5) {
		this.attr5 = attr5;
	}

	/** 
	* 获取扩展属性6 
	* @return attr6
	*/
	public String getAttr6() {
		return attr6;
	}

	/** 
	* 设置扩展属性6 
	* @param attr6
	*/
	public void setAttr6(String attr6) {
		this.attr6 = attr6;
	}

	/** 
	* 获取是否有效1有效0无效 
	* @return flag
	*/
	public Integer getFlag() {
		return flag;
	}

	/** 
	* 设置是否有效1有效0无效 
	* @param flag
	*/
	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	/** 
	* 获取备注 
	* @return remark
	*/
	public String getRemark() {
		return remark;
	}

	/** 
	* 设置备注 
	* @param remark
	*/
	public void setRemark(String remark) {
		this.remark = remark;
	}

}

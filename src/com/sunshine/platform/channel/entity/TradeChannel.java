/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年7月25日</p>
 *  <p> Created by 赤芍</p>
 *  </body>
 * </html>
 */
package com.sunshine.platform.channel.entity;

import com.sunshine.framework.mvc.mysql.entity.BaseSQLEntity;

/**
 * 
 * @Project: cashier_trade 
 * @Package: com.sunshine.platform.channel.entity
 * @ClassName: TradeChannel
 * @Description: <p>支付渠道</p>
 * @JDK version used: 
 * @Author: 赤芍
 * @Create Date: 2017年9月12日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class TradeChannel extends BaseSQLEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2580867177292768587L;

	/**
	 * 支付渠道名称
	 */
	private String name;

	/**
	 * 支付渠道编码
	 */
	private String code;

	/**
	 * 支付渠道标识值
	 */
	private int value;

	/**
	 * 支付渠道icon图标
	 */
	private String icon;

	/**
	 * 应用场景
	 */
	private String scenarios;

	/**
	 * 结算周期
	 */
	private String settlementCycle;

	/**
	 * 1手机WAP、2APP、3公众号、4服务窗
	 */
	private Integer type;

	/**
	 * 序号
	 */
	private Integer seq;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * @param icon the icon to set
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * @return the type
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * @return the seq
	 */
	public Integer getSeq() {
		return seq;
	}

	/**
	 * @param seq the seq to set
	 */
	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	/**
	 * @return the scenarios
	 */
	public String getScenarios() {
		return scenarios;
	}

	/**
	 * @param scenarios the scenarios to set
	 */
	public void setScenarios(String scenarios) {
		this.scenarios = scenarios;
	}

	/**
	 * @return the settlementCycle
	 */
	public String getSettlementCycle() {
		return settlementCycle;
	}

	/**
	 * @param settlementCycle the settlementCycle to set
	 */
	public void setSettlementCycle(String settlementCycle) {
		this.settlementCycle = settlementCycle;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}

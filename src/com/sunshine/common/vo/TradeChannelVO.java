/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年9月14日</p>
 *  <p> Created by hqy</p>
 *  </body>
 * </html>
 */
package com.sunshine.common.vo;

import java.io.Serializable;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.common.vo
 * @ClassName: TradeChannelVO
 * @Description: <p></p>
 * @JDK version used: 
 * @Author: hqy
 * @Create Date: 2017年9月14日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class TradeChannelVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1364669608208755631L;

	private String id;

	/**
	 * 支付渠道名称
	 */
	private String name;

	/**
	 * 支付渠道编码
	 */
	private String code;

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
	 * 0:未开启      1：开启
	 */
	private int isOpen;

	/**
	 * 是否已经配置
	 */
	private int hasConfig;

	/**
	 * 关联表id （只有当支付渠道已配置的时候才有值）
	 */
	private String connect_id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getScenarios() {
		return scenarios;
	}

	public void setScenarios(String scenarios) {
		this.scenarios = scenarios;
	}

	public String getSettlementCycle() {
		return settlementCycle;
	}

	public void setSettlementCycle(String settlementCycle) {
		this.settlementCycle = settlementCycle;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public int getHasConfig() {
		return hasConfig;
	}

	public void setHasConfig(int hasConfig) {
		this.hasConfig = hasConfig;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(int isOpen) {
		this.isOpen = isOpen;
	}

	public String getConnect_id() {
		return connect_id;
	}

	public void setConnect_id(String connect_id) {
		this.connect_id = connect_id;
	}

}

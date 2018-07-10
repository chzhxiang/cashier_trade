/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年10月11日</p>
 *  <p> Created by hqy</p>
 *  </body>
 * </html>
 */
package com.sunshine.platform.order.entity;

import com.sunshine.mobileapp.bankCard.entity.BankCard;
import com.sunshine.mobileapp.order.entity.TradeOrder;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.platform.order.entity
 * @ClassName: OrderExcelParams
 * @Description: <p>订单导出excel实体类</p>
 * @JDK version used: 
 * @Author: 百部
 * @Create Date: 2017年10月11日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class OrderParamsVo extends TradeOrder {
	/**
	 * 支付时间
	 */
	private String payTimeVo;

	/**
	 * 退费时间
	 */
	private String refundTimeVo;

	/**
	 * 创建订单时间
	 */
	private String createTimeVo;

	/**
	 * 订单状态
	 */
	private String tradeStatusVo;

	/**
	 * 订单金额vo
	 */
	private String totalFeeVo;

	/**
	 * 实付金额vo
	 */
	private String actualPayFreeVo;

	/**
	 * 实收金额vo
	 */
	private String actualGetFreeVo;

	/**
	 * 退款金额vo
	 */
	private String refundFeeVo;

	/**
	 * 银行卡信息
	 */
	private BankCard bankCard;

	public String getPayTimeVo() {
		return payTimeVo;
	}

	public void setPayTimeVo(String payTimeVo) {
		this.payTimeVo = payTimeVo;
	}

	public String getRefundTimeVo() {
		return refundTimeVo;
	}

	public void setRefundTimeVo(String refundTimeVo) {
		this.refundTimeVo = refundTimeVo;
	}

	public String getCreateTimeVo() {
		return createTimeVo;
	}

	public void setCreateTimeVo(String createTimeVo) {
		this.createTimeVo = createTimeVo;
	}

	public String getTradeStatusVo() {
		return tradeStatusVo;
	}

	public void setTradeStatusVo(String tradeStatusVo) {
		this.tradeStatusVo = tradeStatusVo;
	}

	public String getTotalFeeVo() {
		return totalFeeVo;
	}

	public void setTotalFeeVo(String totalFeeVo) {
		this.totalFeeVo = totalFeeVo;
	}

	public String getActualPayFreeVo() {
		return actualPayFreeVo;
	}

	public void setActualPayFreeVo(String actualPayFreeVo) {
		this.actualPayFreeVo = actualPayFreeVo;
	}

	public String getActualGetFreeVo() {
		return actualGetFreeVo;
	}

	public void setActualGetFreeVo(String actualGetFreeVo) {
		this.actualGetFreeVo = actualGetFreeVo;
	}

	public String getRefundFeeVo() {
		return refundFeeVo;
	}

	public void setRefundFeeVo(String refundFeeVo) {
		this.refundFeeVo = refundFeeVo;
	}

	/** 
	* 获取银行卡信息 
	* @return bankCard
	*/
	public BankCard getBankCard() {
		return bankCard;
	}

	/** 
	* 设置银行卡信息 
	* @param bankCard
	*/
	public void setBankCard(BankCard bankCard) {
		this.bankCard = bankCard;
	}

}

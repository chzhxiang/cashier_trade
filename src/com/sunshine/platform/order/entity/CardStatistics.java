/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年11月23日下午4:50:50</p>
 *  <p> Created by 熊胆</p>
 *  </body>
 * </html>
 */
package com.sunshine.platform.order.entity;

/**
 * @Project: cashier_trade
 * @Package: com.sunshine.platform.order.entity
 * @ClassName: CardStatistics
 * @Description: 卡消费金额汇总实体
 * @JDK version used: 
 * @Author: 熊胆
 * @Create Date: 2017年11月23日下午4:50:50
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class CardStatistics {

	private String cardId;

	private Integer totalPrice;

	/** 
	* 获取cardId 
	* @return cardId
	*/
	public String getCardId() {
		return cardId;
	}

	/** 
	* 设置cardId 
	* @param cardId
	*/
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	/** 
	* 获取totalPrice 
	* @return totalPrice
	*/
	public Integer getTotalPrice() {
		return totalPrice;
	}

	/** 
	* 设置totalPrice 
	* @param totalPrice
	*/
	public void setTotalPrice(Integer totalPrice) {
		this.totalPrice = totalPrice;
	}

}

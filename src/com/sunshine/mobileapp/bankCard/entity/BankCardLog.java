/**
 * <html>
 * <body>
 *  <P> Copyright 2017 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年11月6日</p>
 *  <p> Created by 熊胆</p>
 *  </body>
 * </html>
 */
package com.sunshine.mobileapp.bankCard.entity;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.sunshine.framework.mvc.mongodb.entity.BaseMongoEntity;

/**
 * @Project: cashier_trade
 * @Package: com.sunshine.mobileapp.bankCard.entity
 * @ClassName: BankCardLog
 * @Description: <p>银行卡日志记录</p>
 * @JDK version used: 
 * @Author: 熊胆
 * @Create Date: 2017年11月6日下午2:07:09
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class BankCardLog extends BaseMongoEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1523761067432347025L;

	private String ip;

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date date;

	private String log;

	/**
	 * 
	 */
	public BankCardLog() {
		super();
	}

	/**
	 * @param ip
	 * @param date
	 * @param log
	 */
	public BankCardLog(String ip, Date date, String log) {
		super();
		this.ip = ip;
		this.date = date;
		this.log = log;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

}

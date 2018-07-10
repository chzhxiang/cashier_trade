/**
 * <html>
 * <body>
 *  <P> Copyright 2017 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年9月21日</p>
 *  <p> Created by 党参</p>
 *  </body>
 * </html>
 */
package com.sunshine.mobileapp.order.entity;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.sunshine.framework.mvc.mongodb.entity.BaseMongoEntity;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.mobileapp.order.entity
 * @ClassName: OrderLog
 * @Description: <p>日志记录</p>
 * @JDK version used: 
 * @Author: 党参
 * @Create Date: 2017年9月21日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class OrderLog extends BaseMongoEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6360128551504300960L;

	private String ip;

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date date;

	private String log;

	/**
	 * 
	 */
	public OrderLog() {
		super();
	}

	/**
	 * @param ip
	 * @param date
	 * @param log
	 */
	public OrderLog(String ip, Date date, String log) {
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

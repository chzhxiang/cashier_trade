/**
 * <html>
 * <body>
 *  <P>  Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p>  All rights reserved.</p>
 *  <p> Created on 2016-4-13</p>
 *  <p> Created by 无名子</p>
 *  </body>
 * </html>
 */
package com.sunshine.task.biz.vo;

import java.io.Serializable;

/**
 * @Package com.sunshine.task.vo
 * @ClassName TaskParamsVo
 * @Statement <p>任务参数对象</p>
 * @JDK version used: 1.7
 * @Author: 无名子
 * @Create Date: 2016-4-13
 * @modify-Author: 
 * @modify-Date:   
 * @modify-Why/What: 
 * @Version 1.0
 */
public class TaskParamsVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4709829787614681051L;
	private String merchantNo;
	private String merchantName;

	private Object entity;

	public TaskParamsVo() {
		super();
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public Object getEntity() {
		return entity;
	}

	public void setEntity(Object entity) {
		this.entity = entity;
	}

}

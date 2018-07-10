/**
 * <html>
 * <body>
 *  <P>  Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p>  All rights reserved.</p>
 *  <p> Created on 2015-5-18</p>
 *  <p> Created by Yuce</p>
 *  </body>
 * </html>
 */
package com.sunshine.task.biz.vo;

/**
 * @Package: com.sunshine.task.vo
 * @ClassName: TaskResultInfo
 * @Statement: <p>任务执行返回的结果值对象</p>
 * @JDK version used: 1.6
 * @Author: Yuce
 * @Create Date: 2015-5-18
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class TaskResultInfo {

	/**
	 * 商户号
	 */
	private String merchantNo;

	/**
	 * 任务结果对象
	 */
	private Object taskResult;

	/**
	 * 采集描述信息
	 */
	private String collectCallMsg;

	public TaskResultInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TaskResultInfo(TaskParamsVo vo) {
		// TODO Auto-generated constructor stub
		this.merchantNo = vo.getMerchantNo();
	}

	/**
	 * @param merchantNo
	 * @param taskResult
	 * @param collectCallMsg
	 */
	public TaskResultInfo(String merchantNo, Object taskResult, String collectCallMsg) {
		super();
		this.merchantNo = merchantNo;
		this.taskResult = taskResult;
		this.collectCallMsg = collectCallMsg;
	}

	public Object getTaskResult() {
		return taskResult;
	}

	public void setTaskResult(Object taskResult) {
		this.taskResult = taskResult;
	}

	public String getCollectCallMsg() {
		return collectCallMsg;
	}

	public void setCollectCallMsg(String collectCallMsg) {
		this.collectCallMsg = collectCallMsg;
	}

	/** 
	* 获取商户号 
	* @return merchantNo
	*/
	public String getMerchantNo() {
		return merchantNo;
	}

	/** 
	* 设置商户号 
	* @param merchantNo
	*/
	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

}

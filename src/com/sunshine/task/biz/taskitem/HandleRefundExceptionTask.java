/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年10月19日</p>
 *  <p> Created by hqy</p>
 *  </body>
 * </html>
 */
package com.sunshine.task.biz.taskitem;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunshine.task.biz.collector.HandleRefundExceptionCollector;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.task.biz.taskitem
 * @ClassName: HandleRefundExceptionTask
 * @Description: <p>异常退费订单处理任务</p>
 * @JDK version used: 
 * @Author: 百部
 * @Create Date: 2017年10月19日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class HandleRefundExceptionTask {
	public static Logger logger = LoggerFactory.getLogger(HandleRefundExceptionTask.class);
	/**
	 * 计数器
	 */
	private final AtomicLong idGen = new AtomicLong();

	public HandleRefundExceptionTask() {
		super();
	}

	public void startUp() {
		// TODO Auto-generated method stub
		long count = idGen.incrementAndGet();
		if (logger.isInfoEnabled()) {
			logger.info("第 " + count + " 次退费异常订单处理开始....................");
		}
		Long statrTime = Calendar.getInstance().getTimeInMillis();

		HandleRefundExceptionCollector collector = new HandleRefundExceptionCollector();
		collector.start();

		Long endTime = Calendar.getInstance().getTimeInMillis();
		if (logger.isInfoEnabled()) {
			logger.info("第 " + count + " 次退费异常订单处理结束 ,耗费时间" + ( endTime - statrTime ) + " Millis");
		}
	}
}

/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年9月5日</p>
 *  <p> Created by 51397</p>
 *  </body>
 * </html>
 */
package com.sunshine.task.biz;

/**
 * @Project: prescription 
 * @Package: com.sunshine.task.biz
 * @ClassName: TaskConstant
 * @Description: <p>任务常量类</p>
 * @JDK version used: 
 * @Author: 天竹子
 * @Create Date: 2017年9月5日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class TaskConstant {

	/**
	 * 每核Cpu负载的最大线程队列数
	 */
	public static final float POOL_SIZE = 1.5f;

	public static final int threadNum;

	static {
		int cpuNums = Runtime.getRuntime().availableProcessors();
		float MathNum = cpuNums * TaskConstant.POOL_SIZE;
		threadNum = (int) MathNum;
	}
}

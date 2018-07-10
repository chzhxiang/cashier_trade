/**
 * <html>
 * <body>
 *  <P> Copyright 2017 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年10月11日</p>
 *  <p> Created by 党参</p>
 *  </body>
 * </html>
 */
package com.sunshine.task.biz.collector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.sunshine.common.GlobalConstant;
import com.sunshine.framework.common.spring.ext.SpringContextHolder;
import com.sunshine.framework.common.threadpool.SimpleThreadFactory;
import com.sunshine.platform.application.entity.MerchantApplication;
import com.sunshine.platform.application.service.MerchantApplicationService;
import com.sunshine.platform.merchant.entity.Merchant;
import com.sunshine.platform.merchant.service.MerchantService;
import com.sunshine.task.biz.TaskConstant;
import com.sunshine.task.biz.callable.HandleOrderExceptionCollectorCall;
import com.sunshine.task.biz.vo.TaskResultInfo;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.task.biz.collector
 * @ClassName: HandleOrderExceptionCollector
 * @Description: <p>异常订单处理任务</p>
 * @JDK version used: 
 * @Author: 党参
 * @Create Date: 2017年10月11日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class HandleOrderExceptionCollector {
	public static Logger logger = LoggerFactory.getLogger(HandleOrderExceptionCollector.class);

	private MerchantService merchantService = SpringContextHolder.getBean(MerchantService.class);

	private MerchantApplicationService maService = SpringContextHolder.getBean(MerchantApplicationService.class);

	public void start() {
		// 根据采集的机器配置得出默认的线程数
		int threadNum = TaskConstant.threadNum;
		logger.info("定任务执行,threadNum:{}", threadNum);

		//获取所有的商户
		List<Merchant> merchants = merchantService.getAllMerchant();

		List<MerchantApplication> applications = new ArrayList<MerchantApplication>();
		if (!CollectionUtils.isEmpty(merchants)) {
			for (Merchant merchant : merchants) {
				if (GlobalConstant.YES == merchant.getStatus()) {
					//根据商户获取所有的应用
					List<MerchantApplication> list = maService.findApplicationByMechNo(merchant.getMerchantNo());
					if (!CollectionUtils.isEmpty(list)) {
						applications.addAll(list);
					}
				}
			}
			logger.info("需处理支付异常订单应用:{}", JSON.toJSONString(applications));
		} else {
			logger.info("需处理支付异常订单商户为空");
		}

		if (applications.size() < threadNum) {
			threadNum = applications.size();
		}

		if (threadNum > 0) {
			// 设置线程池的数量
			ExecutorService collectExec = Executors.newFixedThreadPool(threadNum, new SimpleThreadFactory("payOrderExce"));
			// map中的key为2种
			List<FutureTask<Object>> taskList = new ArrayList<FutureTask<Object>>();

			for (MerchantApplication application : applications) {
				HandleOrderExceptionCollectorCall collectCall = new HandleOrderExceptionCollectorCall(application);

				// 创建每条指令的采集任务对象
				FutureTask<Object> collectTask = new FutureTask<Object>(collectCall);

				// 添加到list,方便后面取得结果
				taskList.add(collectTask);
				// 提交给线程池 exec.submit(task);
				collectExec.submit(collectTask);
			}

			// 阻塞主线程,等待采集所有子线程结束,获取所有子线程的执行结果,get方法阻塞主线程,再继续执行主线程逻辑
			try {
				List<TaskResultInfo> updateTradeOrder = new ArrayList<TaskResultInfo>();
				for (FutureTask<Object> taskF : taskList) {
					// 防止某个子线程查询时间过长 超过默认时间没有拿到抛出异常
					Object info = taskF.get(Long.MAX_VALUE, TimeUnit.DAYS);
					if (info != null) {
						//updateRecord.add(record);
					}
				}

				if (updateTradeOrder.size() > 0) {
					//批量更新RegisterRecord
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 处理完毕,关闭线程池,这个不能在获取子线程结果之前关闭,因为如果线程多的话,执行中的可能被打断
			collectExec.shutdown();
		}

	}
}

/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年10月10日</p>
 *  <p> Created by hqy</p>
 *  </body>
 * </html>
 */
package com.sunshine.platform.order.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.sunshine.framework.mvc.mongodb.service.BaseMongoService;
import com.sunshine.mobileapp.order.entity.TradeOrder;
import com.sunshine.platform.channel.entity.TradeChannel;
import com.sunshine.platform.merchant.entity.Merchant;
import com.sunshine.platform.order.entity.OrderDataView;
import com.sunshine.platform.order.entity.OrderParamsVo;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.platform.order.service
 * @ClassName: OrderService
 * @Description: <p>订单管理service接口</p>
 * @JDK version used: 
 * @Author: 百部
 * @Create Date: 2017年10月10日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public interface OrderService extends BaseMongoService<TradeOrder, Serializable> {
	/**
	 * 分页查询
	 */
	public PageInfo<OrderParamsVo> queryByPage(Map<String, Object> map, Page<TradeOrder> page);

	/**
	 * 格式化前端数据
	 */
	public OrderParamsVo dataFormate(TradeOrder order);

	/**
	 * 计算总汇总数据
	 */
	public OrderDataView countTotalFree(Map<String, Object> paramsMap);

	/**
	 * 计算各商户汇总数据
	 */
	public List<OrderDataView> countMechanTotalFree(List<Merchant> merchanList, Map<String, Object> paramsMap);

	/**
	 * 计算各商户中各个应用的汇总数据
	 */
	public List<OrderDataView> countMechanApplicationTotalFree(List<Merchant> merchanLis, Map<String, Object> paramsMap);

	/**
	 * 计算各个支付渠道的汇总数据
	 */
	public List<OrderDataView> countChannelTotalFree(List<TradeChannel> channelList, Map<String, Object> paramsMap);

	/**
	 * 计算某商户的汇总数据或某个商户的某应用的汇总数据
	 */
	public Map<String, Object> countOneMechanApplicationTotalFree(Map<String, Object> map);

	/**
	 * 聚合统计所有
	 */
	public Map<String, Object> polymerizationStatistics(Map<String, Object> params);

	/**
	 * 图形报表统计
	 * @param params
	 * @return
	 */
	public Map<String, Object> chartsStatistics(Map<String, Object> params);
}

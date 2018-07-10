/**
 * <html>
 * <body>
 *  <P> Copyright 2017 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年9月12日</p>
 *  <p> Created by 党参</p>
 *  </body>
 * </html>
 */
package com.sunshine.mobileapp.order.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.sunshine.framework.mvc.mongodb.dao.BaseMongoDao;
import com.sunshine.mobileapp.order.entity.TradeOrder;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.mobileapp.order.dao
 * @ClassName: TradeOrderDao
 * @Description: <p></p>
 * @JDK version used: 
 * @Author: 党参
 * @Create Date: 2017年9月12日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public interface TradeOrderDao extends BaseMongoDao<TradeOrder, Serializable> {

	List<TradeOrder> getParams(Map<String, Object> params, List<String> fieldList);

}

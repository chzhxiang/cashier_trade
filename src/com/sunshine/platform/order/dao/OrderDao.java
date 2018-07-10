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
package com.sunshine.platform.order.dao;

import java.io.Serializable;

import com.sunshine.framework.mvc.mongodb.dao.BaseMongoDao;
import com.sunshine.mobileapp.order.entity.TradeOrder;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.platform.order.dao
 * @ClassName: OrderDao
 * @Description: <p>订单管理dao接口</p>
 * @JDK version used: 
 * @Author: 百部
 * @Create Date: 2017年10月10日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public interface OrderDao extends BaseMongoDao<TradeOrder, Serializable> {

}

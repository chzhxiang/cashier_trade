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
package com.sunshine.platform.order.dao.impl;

import java.io.Serializable;

import org.springframework.stereotype.Service;

import com.sunshine.framework.mvc.mongodb.dao.impl.BaseMongoDaoImpl;
import com.sunshine.mobileapp.order.entity.TradeOrder;
import com.sunshine.platform.order.dao.OrderDao;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.platform.order.dao.impl
 * @ClassName: OrderDaoImpl
 * @Description: <p>dao实现类</p>
 * @JDK version used: 
 * @Author: 百部
 * @Create Date: 2017年10月10日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
@Service
public class OrderDaoImpl extends BaseMongoDaoImpl<TradeOrder, Serializable> implements OrderDao {

}

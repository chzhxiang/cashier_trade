/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年10月26日下午1:56:09</p>
 *  <p> Created by 熊胆</p>
 *  </body>
 * </html>
 */
package com.sunshine.mobileapp.bankCard.dao;

import java.io.Serializable;

import com.sunshine.framework.mvc.mongodb.dao.BaseMongoDao;
import com.sunshine.mobileapp.bankCard.entity.BankCard;

/**
 * @Project: cashier_trade
 * @Package: com.sunshine.mobileapp.bankCard.dao
 * @ClassName: BankCardDao
 * @Description: 绑卡dao层
 * @JDK version used: 
 * @Author: 熊胆
 * @Create Date: 2017年10月26日下午1:56:09
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public interface BankCardDao extends BaseMongoDao<BankCard, Serializable> {

}

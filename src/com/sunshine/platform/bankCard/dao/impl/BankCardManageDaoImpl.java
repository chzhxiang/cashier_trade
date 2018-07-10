/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年10月31日下午2:30:17</p>
 *  <p> Created by 熊胆</p>
 *  </body>
 * </html>
 */
package com.sunshine.platform.bankCard.dao.impl;

import java.io.Serializable;

import org.springframework.stereotype.Service;

import com.sunshine.framework.mvc.mongodb.dao.impl.BaseMongoDaoImpl;
import com.sunshine.mobileapp.bankCard.entity.BankCard;
import com.sunshine.platform.bankCard.dao.BankCardManageDao;

/**
 * @Project: cashier_trade
 * @Package: com.sunshine.platform.bankCard.dao
 * @ClassName: BankCardManageDaoImpl
 * @Description: <p></p>
 * @JDK version used: 
 * @Author: 熊胆
 * @Create Date: 2017年10月31日下午2:30:17
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
@Service
public class BankCardManageDaoImpl extends BaseMongoDaoImpl<BankCard, Serializable> implements BankCardManageDao {

}

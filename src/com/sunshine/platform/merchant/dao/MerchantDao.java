/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年8月22日</p>
 *  <p> Created by 赤芍</p>
 *  </body>
 * </html>
 */
package com.sunshine.platform.merchant.dao;

import com.sunshine.framework.mvc.mysql.dao.BaseDao;
import com.sunshine.platform.merchant.entity.Merchant;

/**
 * 
 * @Project: cashier_trade 
 * @Package: com.sunshine.platform.merchant.dao
 * @ClassName: MerchantDao
 * @Description: <p></p>
 * @JDK version used: 
 * @Author: 赤芍
 * @Create Date: 2017年9月12日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public interface MerchantDao extends BaseDao<Merchant, String> {

	/**
	 * @param merchantNo
	 * @return
	 */
	public Merchant findByMerchantNo(String merchantNo);

}

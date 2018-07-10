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
package com.sunshine.platform.application.dao;

import java.util.List;

import com.sunshine.framework.mvc.mysql.dao.BaseDao;
import com.sunshine.platform.application.entity.MerchantApplication;

/**
 * 
 * @Project: cashier_trade 
 * @Package: com.sunshine.platform.application.dao
 * @ClassName: MerchantApplicationDao
 * @Description: <p></p>
 * @JDK version used: 
 * @Author: 赤芍
 * @Create Date: 2017年9月12日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public interface MerchantApplicationDao extends BaseDao<MerchantApplication, String> {

	/**
	 * @param merchantNo
	 * @return
	 */
	public List<MerchantApplication> findByMerchantNo(String merchantNo);

	MerchantApplication findByAppId(String AppId);

	MerchantApplication findByMerchantNoAndAppID(String merchantNo, String appId);

}

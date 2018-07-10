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
package com.sunshine.platform.merchant.service;

import java.util.List;

import com.sunshine.framework.mvc.service.BaseService;
import com.sunshine.platform.merchant.entity.Merchant;

/**
 * 
 * @Project: cashier_trade 
 * @Package: com.sunshine.platform.merchant.service
 * @ClassName: MerchantService
 * @Description: <p></p>
 * @JDK version used: 
 * @Author: 赤芍
 * @Create Date: 2017年9月12日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public interface MerchantService extends BaseService<Merchant, String> {

	/**
	 * @return
	 */
	public Merchant findByMerchantNo(String merchantNo);

	/**
	 * 获取所有商户信息
	 * @return
	 */
	public List<Merchant> getAllMerchant();
}

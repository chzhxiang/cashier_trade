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
package com.sunshine.platform.application.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sunshine.common.datas.cache.platform.merchant.MerchantCache;
import com.sunshine.framework.common.spring.ext.SpringContextHolder;
import com.sunshine.framework.mvc.mysql.dao.BaseDao;
import com.sunshine.framework.mvc.mysql.service.impl.BaseServiceImpl;
import com.sunshine.platform.application.dao.MerchantApplicationDao;
import com.sunshine.platform.application.entity.MerchantApplication;
import com.sunshine.platform.application.service.MerchantApplicationService;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.platform.application.service.impl
 * @ClassName: MerchantApplicationServiceImpl
 * @Description: <p></p>
 * @JDK version used: 
 * @Author: 赤芍
 * @Create Date: 2017年9月12日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
@Service
public class MerchantApplicationServiceImpl extends BaseServiceImpl<MerchantApplication, String> implements MerchantApplicationService {
	private MerchantCache merchantCache = SpringContextHolder.getBean(MerchantCache.class);
	private Logger logger = LoggerFactory.getLogger(MerchantApplicationServiceImpl.class);

	@Autowired
	protected MerchantApplicationDao merchantApplicationDao;

	@Override
	protected BaseDao<MerchantApplication, String> getDao() {
		return merchantApplicationDao;
	}

	@Override
	public List<MerchantApplication> findByMerchantNo(String merchantNo) {
		return merchantApplicationDao.findByMerchantNo(merchantNo);
	}

	@Override
	public MerchantApplication findByAppId(String appId) {
		return merchantApplicationDao.findByAppId(appId);
	}

	/**
	 * 根据商户号 和 appID获取商户应用信息
	 */
	@Override
	public MerchantApplication findApplicationByAppId(String merchantNo, String appId) {
		logger.info("从缓存中获取商户应用信息，商户号:{},appId:{}", merchantNo, appId);
		MerchantApplication merchantApplication = merchantCache.getMerchantApplicationInfoByAppId(merchantNo, appId);
		if (merchantApplication == null) {
			logger.info("尝试从数据库中获取相关应用信息，merchantNo：{}，appId：{}", merchantNo, appId);
			//从数据中查询
			MerchantApplication dataMerchantApplication = merchantApplicationDao.findByMerchantNoAndAppID(merchantNo, appId);
			if (dataMerchantApplication != null && dataMerchantApplication.getAppId().equals(appId)) {
				//从新写入缓存
				merchantCache.cacheMerchantApplication(dataMerchantApplication);
			}
			return dataMerchantApplication;
		}
		return merchantApplication;
	}

	@Override
	public List<MerchantApplication> findApplicationByMechNo(String mechNo) {
		List<MerchantApplication> merchantApplicationList = null;
		merchantApplicationList = merchantCache.getMerchantApplicationByMechNo(mechNo);
		if (merchantApplicationList == null || merchantApplicationList.size() < 1) {
			//从数据库中获取
			merchantApplicationList = merchantApplicationDao.findByMerchantNo(mechNo);
		}
		return merchantApplicationList;
	}
}

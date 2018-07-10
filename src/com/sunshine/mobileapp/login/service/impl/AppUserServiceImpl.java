package com.sunshine.mobileapp.login.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sunshine.framework.mvc.mysql.dao.BaseDao;
import com.sunshine.framework.mvc.mysql.service.impl.BaseServiceImpl;
import com.sunshine.mobileapp.login.dao.AppUserDao;
import com.sunshine.mobileapp.login.entity.AppUser;
import com.sunshine.mobileapp.login.service.AppUserService;

/**
 * 
 * @Project: Prescription_PlatForm 
 * @Package: com.sunshine.mobileapp.login.service.impl
 * @ClassName: AppUserServiceImpl
 * @Description: <p>	用户账号service实现类	</p>
 * @JDK version used: 
 * @Author: 香薷
 * @Create Date: 2017年9月6日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
@Service
public class AppUserServiceImpl extends BaseServiceImpl<AppUser, String> implements AppUserService {

	@Autowired
	 private AppUserDao AppUserDao;
	
	@Override
	protected BaseDao<AppUser, String> getDao() {
		return AppUserDao;
	}

	@Override
	public AppUser findByAccount(Map map) {
		return AppUserDao.findByAccount(map);
	}


}

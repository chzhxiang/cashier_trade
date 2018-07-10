package com.sunshine.mobileapp.login.dao;

import java.util.Map;

import com.sunshine.framework.mvc.mysql.dao.BaseDao;
import com.sunshine.mobileapp.login.entity.AppUser;

/**
 * 
 * @Project: Prescription_PlatForm 
 * @Package: com.sunshine.mobileapp.login.dao
 * @ClassName: AppUserDao
 * @Description: <p>	用户账号dao	</p>
 * @JDK version used: 
 * @Author: 香薷
 * @Create Date: 2017年9月6日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public interface AppUserDao extends BaseDao<AppUser, String> {
	
	/**
	 * 根据账号登录
	 * @param map
	 * @return
	 */
	public AppUser findByAccount(Map map);
	
}
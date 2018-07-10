package com.sunshine.mobileapp.login.dao.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import com.sunshine.framework.exception.SystemException;
import com.sunshine.framework.mvc.mysql.dao.impl.BaseDaoImpl;
import com.sunshine.mobileapp.login.dao.AppUserDao;
import com.sunshine.mobileapp.login.entity.AppUser;

/**
 * 
 * @Project: Prescription_PlatForm 
 * @Package: com.sunshine.mobileapp.login.dao.impl
 * @ClassName: AppUserDaoImpl
 * @Description: <p></p>
 * @JDK version used: 
 * @Author: 香薷
 * @Create Date: 2017年9月6日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
@Repository
public class AppUserDaoImpl extends BaseDaoImpl<AppUser, String> implements AppUserDao {

	private static Logger logger = LoggerFactory.getLogger(AppUserDaoImpl.class);
	private final static String SQLNAME_FIND_BY_ACCOUNT = "findByAccount";
	
	@Override
	public AppUser findByAccount(Map map) {
		try {
			return sqlSession.selectOne(SQLNAME_FIND_BY_ACCOUNT, map);
		} catch (Exception e) {
			logger.error(String.format("根据账号登录出错！语句：%s", getSqlName(SQLNAME_FIND_BY_ACCOUNT)), e);
			throw new SystemException(String.format("根据账号登录出错！语句：%s", getSqlName(SQLNAME_FIND_BY_ACCOUNT)), e);
		}
	}


}

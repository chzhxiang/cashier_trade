package com.sunshine.platform.security.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.sunshine.framework.exception.SystemException;
import com.sunshine.framework.mvc.mysql.dao.impl.BaseDaoImpl;
import com.sunshine.platform.security.dao.UserDao;
import com.sunshine.platform.security.entity.User;

@Repository
public class UserDaoImpl extends BaseDaoImpl<User, String> implements UserDao {

	private static Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

	private final static String SQLNAME_FIND_USER_BY_ACCOUNT = "findUserByAccount";
	private final static String SQLNAME_UPDATE_ROLE_IDS = "updateRoleIds";
	private final static String SQLNAME_UPDATE_DEPTNAME_BY_DEPTID = "updateDeptNameByDeptId";
	private final static String SQLNAME_REMOVE_DEPT_INFO = "removeDeptInfo";

	@Override
	public User findUserByAccount(String account) {
		try {
			Assert.notNull(account);
			return sqlSession.selectOne(getSqlName(SQLNAME_FIND_USER_BY_ACCOUNT), account);
		} catch (Exception e) {
			logger.error(String.format("根据帐号查询用户出错！语句：%s", getSqlName(SQLNAME_FIND_USER_BY_ACCOUNT)), e);
			throw new SystemException(String.format("根据帐号查询用户出错！语句：%s", getSqlName(SQLNAME_FIND_USER_BY_ACCOUNT)), e);
		}
	}

	@Override
	public void updateRoleIds(String userId, String roleIds) {
		// TODO Auto-generated method stub
		try {
			Assert.notNull(userId);
			Assert.notNull(roleIds);
			Map<String, String> paramMap = new HashMap<>();
			paramMap.put("id", userId);
			paramMap.put("roleIds", roleIds);
			sqlSession.update(getSqlName(SQLNAME_UPDATE_ROLE_IDS), paramMap);
		} catch (Exception e) {
			logger.error(String.format("更新用户的角色信息出错！语句：%s", getSqlName(SQLNAME_UPDATE_ROLE_IDS)), e);
			throw new SystemException(String.format("更新用户的角色信息出错！语句：%s", getSqlName(SQLNAME_UPDATE_ROLE_IDS)), e);
		}
	}

	@Override
	public void updateDeptNameByDeptId(String deptId, String deptName) {
		// TODO Auto-generated method stub
		try {
			Assert.notNull(deptId, deptName);
			Map<String, Object> paraMap = new HashMap<>();
			paraMap.put("deptId", deptId);
			paraMap.put("deptName", deptName);
			sqlSession.update(getSqlName(SQLNAME_UPDATE_DEPTNAME_BY_DEPTID), paraMap);
		} catch (Exception e) {
			logger.error(String.format("根据部门ID 更新部门名称出错！语句：%s", getSqlName(SQLNAME_UPDATE_DEPTNAME_BY_DEPTID)), e);
			throw new SystemException(String.format("根据部门ID 更新部门名称出错！语句：%s", getSqlName(SQLNAME_UPDATE_DEPTNAME_BY_DEPTID)), e);
		}
	}

	@Override
	public Long removeDeptInfo(List<String> deptIds) {
		// TODO Auto-generated method stub
		Long updateCount = 0L;
		try {
			Assert.notEmpty(deptIds);
			updateCount = Long.valueOf(sqlSession.update(getSqlName(SQLNAME_REMOVE_DEPT_INFO), deptIds));
		} catch (Exception e) {
			logger.error(String.format("根据部门ID去除人员的部门信息！语句：%s", getSqlName(SQLNAME_REMOVE_DEPT_INFO)), e);
			throw new SystemException(String.format("根据部门ID去除人员的部门信息！语句：%s", getSqlName(SQLNAME_REMOVE_DEPT_INFO)), e);
		}
		return updateCount;
	}

}

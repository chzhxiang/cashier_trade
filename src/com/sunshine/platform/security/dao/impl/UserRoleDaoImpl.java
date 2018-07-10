package com.sunshine.platform.security.dao.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.sunshine.framework.exception.SystemException;
import com.sunshine.framework.mvc.mysql.dao.impl.BaseDaoImpl;
import com.sunshine.platform.security.dao.UserRoleDao;
import com.sunshine.platform.security.entity.UserRole;

@Repository
public class UserRoleDaoImpl extends BaseDaoImpl<UserRole, String> implements UserRoleDao {

	private static Logger logger = LoggerFactory.getLogger(RoleDaoImpl.class);

	private final static String SQLNAME_FIND_USERROLE_BY_ROLE = "findUserRoleByRole";

	private final static String SQLNAME_FIND_USERROLE_BY_USER = "findUserRoleByUser";

	private final static String SQLNAME_FIND_USERROLE_BY_ROLELIST = "findUserRoleByRoleList";

	private final static String SQLNAME_FIND_USERROLE_BY_USERLIST = "findUserRoleByUserList";

	private final static String SQLNAME_DELETE_BY_ROLE_IDS = "deleteByRoleIds";

	private final static String SQLNAME_DELETE_BY_USER_ID = "deleteByUserId";

	@Override
	public List<UserRole> findUserRoleByRole(String roleId) {
		try {
			Assert.notNull(roleId);
			return sqlSession.selectList(getSqlName(SQLNAME_FIND_USERROLE_BY_ROLE), roleId);
		} catch (Exception e) {
			logger.error(String.format("根据用户查询用户角色出错！语句：%s", getSqlName(SQLNAME_FIND_USERROLE_BY_ROLE)), e);
			throw new SystemException(String.format("根据用户查询用户角色出错！语句：%s", getSqlName(SQLNAME_FIND_USERROLE_BY_ROLE)), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sunshine.platform.security.dao.RoleDao#findRoleByCode(java.lang.String)
	 */
	@Override
	public List<UserRole> findUserRoleByUser(String userId) {
		try {
			Assert.notNull(userId);
			return sqlSession.selectList(getSqlName(SQLNAME_FIND_USERROLE_BY_USER), userId);
		} catch (Exception e) {
			logger.error(String.format("根据角色查询用户角色出错！语句：%s", getSqlName(SQLNAME_FIND_USERROLE_BY_USER)), e);
			throw new SystemException(String.format("根据角色查询用户角色出错！语句：%s", getSqlName(SQLNAME_FIND_USERROLE_BY_USER)), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sunshine.platform.security.dao.UserRoleDao#findUserRoleByRoleList(java.util.List)
	 */
	@Override
	public List<UserRole> findUserRoleByRoleList(List<String> roleIds) {
		// TODO Auto-generated method stub
		try {
			Assert.notNull(roleIds);
			return sqlSession.selectList(getSqlName(SQLNAME_FIND_USERROLE_BY_ROLELIST), roleIds);
		} catch (Exception e) {
			logger.error(String.format("根据角色查询用户角色出错！语句：%s", getSqlName(SQLNAME_FIND_USERROLE_BY_ROLELIST)), e);
			throw new SystemException(String.format("根据角色查询用户角色出错！语句：%s", getSqlName(SQLNAME_FIND_USERROLE_BY_ROLELIST)), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sunshine.platform.security.dao.UserRoleDao#findUserRoleByUserList(java.util.List)
	 */
	@Override
	public List<UserRole> findUserRoleByUserList(List<String> userIds) {
		try {
			Assert.notNull(userIds);
			return sqlSession.selectList(getSqlName(SQLNAME_FIND_USERROLE_BY_USERLIST), userIds);
		} catch (Exception e) {
			logger.error(String.format("根据用户查询用户角色出错！语句：%s", getSqlName(SQLNAME_FIND_USERROLE_BY_USERLIST)), e);
			throw new SystemException(String.format("根据用户查询用户角色出错！语句：%s", getSqlName(SQLNAME_FIND_USERROLE_BY_USERLIST)), e);
		}
	}

	@Override
	public void deleteByRoleIds(List<String> roleIds) {
		// TODO Auto-generated method stub
		try {
			Assert.notNull(roleIds);
			sqlSession.delete(getSqlName(SQLNAME_DELETE_BY_ROLE_IDS), roleIds);
		} catch (Exception e) {
			logger.error(String.format("根据角色列表删除人员与角色关系出错！语句：%s", getSqlName(SQLNAME_DELETE_BY_ROLE_IDS)), e);
			throw new SystemException(String.format("根据角色列表删除人员与角色关系出错！语句：%s", getSqlName(SQLNAME_DELETE_BY_ROLE_IDS)), e);
		}
	}

	@Override
	public void deleteByUserId(String userId) {
		try {
			Assert.notNull(userId);
			sqlSession.delete(getSqlName(SQLNAME_DELETE_BY_USER_ID), userId);
		} catch (Exception e) {
			logger.error(String.format("根据人员ID删除人员与角色关系出错！语句：%s", getSqlName(SQLNAME_DELETE_BY_USER_ID)), e);
			throw new SystemException(String.format("根据人员ID删除人员与角色关系出错！语句：%s", getSqlName(SQLNAME_DELETE_BY_USER_ID)), e);
		}
	}
}

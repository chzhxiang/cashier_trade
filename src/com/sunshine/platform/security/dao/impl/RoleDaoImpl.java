package com.sunshine.platform.security.dao.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.sunshine.framework.exception.SystemException;
import com.sunshine.framework.mvc.mysql.dao.impl.BaseDaoImpl;
import com.sunshine.platform.security.dao.RoleDao;
import com.sunshine.platform.security.entity.Role;

@Repository
public class RoleDaoImpl extends BaseDaoImpl<Role, String> implements RoleDao {

	private static Logger logger = LoggerFactory.getLogger(RoleDaoImpl.class);

	private final static String SQLNAME_FIND_ROLE_BY_PARAM = "findRoleByProperties";

	private final static String SQLNAME_FIND_ROLE_BY_CODE = "findRoleByCode";

	private final static String SQLNAME_FIND_ALL_ROLE = "findAllRole";

	private final static String SQLNAME_FIND_ROLE_BY_USER_ID = "findRoleByUserId";

	private final static String SQLNAME_FIND_ALL_ROLE_WITH_USER_INFO = "findAllRoleWithUserInfo";

	@Override
	public List<Role> findRoleByProperties(Map<String, Object> paramMap) {
		try {
			Assert.notNull(paramMap);
			return sqlSession.selectList(getSqlName(SQLNAME_FIND_ROLE_BY_PARAM), paramMap);
		} catch (Exception e) {
			logger.error(String.format("根据角色属性查询角色出错！语句：%s", getSqlName(SQLNAME_FIND_ROLE_BY_PARAM)), e);
			throw new SystemException(String.format("根据角色属性查询角色出错！语句：%s", getSqlName(SQLNAME_FIND_ROLE_BY_PARAM)), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sunshine.platform.security.dao.RoleDao#findRoleByCode(java.lang.String)
	 */
	@Override
	public Role findRoleByCode(String roleCode) {
		try {
			Assert.notNull(roleCode);
			return sqlSession.selectOne(getSqlName(SQLNAME_FIND_ROLE_BY_CODE), roleCode);
		} catch (Exception e) {
			logger.error(String.format("根据code查询角色出错！语句：%s", getSqlName(SQLNAME_FIND_ROLE_BY_CODE)), e);
			throw new SystemException(String.format("根据code查询角色出错！语句：%s", getSqlName(SQLNAME_FIND_ROLE_BY_CODE)), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sunshine.platform.security.dao.RoleDao#findAllRole()
	 */
	@Override
	public List<Role> findAllRole() {
		try {
			return sqlSession.selectList(getSqlName(SQLNAME_FIND_ALL_ROLE));
		} catch (Exception e) {
			logger.error(String.format("查询角色出错！语句：%s", getSqlName(SQLNAME_FIND_ALL_ROLE)), e);
			throw new SystemException(String.format("查询角色出错！语句：%s", getSqlName(SQLNAME_FIND_ALL_ROLE)), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sunshine.platform.security.dao.RoleDao#findUserAllRole(java.lang.String)
	 */
	@Override
	public List<Role> findRoleByUserId(String userId) {
		try {
			Assert.notNull(userId);
			return sqlSession.selectList(getSqlName(SQLNAME_FIND_ROLE_BY_USER_ID), userId);
		} catch (Exception e) {
			logger.error(String.format("根据用户查询角色出错！语句：%s", getSqlName(SQLNAME_FIND_ROLE_BY_USER_ID)), e);
			throw new SystemException(String.format("根据用户查询角色出错！语句：%s", getSqlName(SQLNAME_FIND_ROLE_BY_USER_ID)), e);
		}
	}

	@Override
	public List<Role> findAllRoleWithUserInfo(String userId) {
		try {
			Assert.notNull(userId);
			return sqlSession.selectList(getSqlName(SQLNAME_FIND_ALL_ROLE_WITH_USER_INFO), userId);
		} catch (Exception e) {
			logger.error(String.format("根据用户查询角色出错！语句：%s", getSqlName(SQLNAME_FIND_ALL_ROLE_WITH_USER_INFO)), e);
			throw new SystemException(String.format("根据用户查询角色出错！语句：%s", getSqlName(SQLNAME_FIND_ALL_ROLE_WITH_USER_INFO)), e);
		}
	}

}

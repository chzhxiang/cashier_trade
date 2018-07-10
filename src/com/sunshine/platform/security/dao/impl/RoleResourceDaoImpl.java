/**
 * <html>
 * <body>
 *  <P> Copyright 2017 阳光康众</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2016年4月11日</p>
 *  <p> Created by x-lan</p>
 *  </body>
 * </html>
 */
package com.sunshine.platform.security.dao.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.sunshine.framework.exception.SystemException;
import com.sunshine.framework.mvc.mysql.dao.impl.BaseDaoImpl;
import com.sunshine.platform.security.dao.RoleResourceDao;
import com.sunshine.platform.security.entity.RoleResource;

@Repository
public class RoleResourceDaoImpl extends BaseDaoImpl<RoleResource, String> implements RoleResourceDao {

	private static Logger logger = LoggerFactory.getLogger(RoleDaoImpl.class);

	private final static String SQLNAME_FIND_ROLE_RESOURCE = "findRoleResource";

	private final static String SQLNAME_FIND_RESOURCE_ROLE = "findResourceRole";

	private final static String SQLNAME_FIND_ROLE_RESOURCELIST = "findRoleResourceList";

	private final static String SQLNAME_FIND_RESOURCE_ROLELIST = "findResourceRoleList";
	private final static String SQLNAME_DELETE_BY_RESOURCE_IDS = "deleteByResourceIds";
	private final static String SQLNAME_DELETE_BY_ROLE_IDS = "deleteByRoleIds";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sunshine.platform.security.dao.RoleResourceDao#findRoleResource(java.lang.String)
	 */
	@Override
	public List<RoleResource> findRoleResource(String roleId) {
		try {
			Assert.notNull(roleId);
			return sqlSession.selectList(getSqlName(SQLNAME_FIND_ROLE_RESOURCE), roleId);
		} catch (Exception e) {
			logger.error(String.format("根据角色查询角色资源出错！语句：%s", getSqlName(SQLNAME_FIND_ROLE_RESOURCE)), e);
			throw new SystemException(String.format("根据角色查询角色资源出错！语句：%s", getSqlName(SQLNAME_FIND_ROLE_RESOURCE)), e);
		}
	}

	@Override
	public List<RoleResource> findRoleResourceList(List<String> roleIds) {
		try {
			Assert.notNull(roleIds);
			return sqlSession.selectList(getSqlName(SQLNAME_FIND_ROLE_RESOURCELIST), roleIds);
		} catch (Exception e) {
			logger.error(String.format("根据角色查询角色资源出错！语句：%s", getSqlName(SQLNAME_FIND_ROLE_RESOURCELIST)), e);
			throw new SystemException(String.format("根据角色查询角色资源出错！语句：%s", getSqlName(SQLNAME_FIND_ROLE_RESOURCELIST)), e);
		}
	}

	@Override
	public List<RoleResource> findResourceRole(String resourceId) {
		try {
			Assert.notNull(resourceId);
			return sqlSession.selectList(getSqlName(SQLNAME_FIND_RESOURCE_ROLE), resourceId);
		} catch (Exception e) {
			logger.error(String.format("根据资源查询角色资源出错！语句：%s", getSqlName(SQLNAME_FIND_RESOURCE_ROLE)), e);
			throw new SystemException(String.format("根据资源查询角色资源出错！语句：%s", getSqlName(SQLNAME_FIND_RESOURCE_ROLE)), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sunshine.platform.security.dao.RoleResourceDao#findResourceRoleList(java.util.List)
	 */
	@Override
	public List<RoleResource> findResourceRoleList(List<String> resourceIds) {
		try {
			Assert.notNull(resourceIds);
			return sqlSession.selectList(getSqlName(SQLNAME_FIND_RESOURCE_ROLELIST), resourceIds);
		} catch (Exception e) {
			logger.error(String.format("根据资源查询角色资源出错！语句：%s", getSqlName(SQLNAME_FIND_RESOURCE_ROLELIST)), e);
			throw new SystemException(String.format("根据资源查询角色资源出错！语句：%s", getSqlName(SQLNAME_FIND_RESOURCE_ROLELIST)), e);
		}
	}

	@Override
	public void deleteByResourceIds(List<String> resourceIds) {
		// TODO Auto-generated method stub
		try {
			Assert.notNull(resourceIds);
			sqlSession.delete(getSqlName(SQLNAME_DELETE_BY_RESOURCE_IDS), resourceIds);
		} catch (Exception e) {
			logger.error(String.format("根据资源删除角色资源关系出错！语句：%s", getSqlName(SQLNAME_DELETE_BY_RESOURCE_IDS)), e);
			throw new SystemException(String.format("根据资源删除角色资源关系出错！语句：%s", getSqlName(SQLNAME_DELETE_BY_RESOURCE_IDS)), e);
		}
	}

	@Override
	public void deleteByRoleIds(List<String> roleIds) {
		// TODO Auto-generated method stub
		try {
			Assert.notNull(roleIds);
			sqlSession.delete(getSqlName(SQLNAME_DELETE_BY_ROLE_IDS), roleIds);
		} catch (Exception e) {
			logger.error(String.format("根据角色删除角色资源关系出错！语句：%s", getSqlName(SQLNAME_DELETE_BY_ROLE_IDS)), e);
			throw new SystemException(String.format("根据角色删除角色资源关系出错！语句：%s", getSqlName(SQLNAME_DELETE_BY_ROLE_IDS)), e);
		}
	}

}

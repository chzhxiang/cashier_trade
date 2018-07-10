package com.sunshine.platform.security.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sunshine.framework.common.spring.ext.SpringContextHolder;
import com.sunshine.framework.mvc.mysql.dao.BaseDao;
import com.sunshine.framework.mvc.mysql.service.impl.BaseServiceImpl;
import com.sunshine.platform.security.dao.RoleResourceDao;
import com.sunshine.platform.security.entity.RoleResource;
import com.sunshine.platform.security.service.RoleResourceService;

@Service(value = "roleResourceService")
public class RoleResourceServiceImpl extends BaseServiceImpl<RoleResource, String> implements RoleResourceService {
	private RoleResourceDao roleResourceDao = SpringContextHolder.getBean(RoleResourceDao.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sunshine.framework.mvc.service.impl.BaseServiceImpl#getDao()
	 */
	@Override
	protected BaseDao<RoleResource, String> getDao() {
		// TODO Auto-generated method stub
		return roleResourceDao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sunshine.platform.security.service.RoleResourceService#findRoleResource(java.lang.String)
	 */
	@Override
	public List<RoleResource> findRoleResource(String roleid) {
		// TODO Auto-generated method stub
		return roleResourceDao.findRoleResource(roleid);
	}

	@Override
	public List<RoleResource> findResourceRole(String resourceId) {
		return roleResourceDao.findResourceRole(resourceId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sunshine.platform.security.service.RoleResourceService#findResourceRoleList(java.util.List)
	 */
	@Override
	public List<RoleResource> findResourceRoleList(List<String> resourceIds) {
		// TODO Auto-generated method stub
		return roleResourceDao.findResourceRoleList(resourceIds);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sunshine.platform.security.service.RoleResourceService#findRoleResourceList(java.util.List)
	 */
	@Override
	public List<RoleResource> findRoleResourceList(List<String> roleIds) {
		// TODO Auto-generated method stub
		return roleResourceDao.findRoleResourceList(roleIds);
	}

	@Override
	public void deleteByRoleIds(List<String> roleIds) {
		// TODO Auto-generated method stub

	}

}

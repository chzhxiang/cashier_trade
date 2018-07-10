package com.sunshine.platform.security.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sunshine.framework.common.spring.ext.SpringContextHolder;
import com.sunshine.framework.mvc.mysql.dao.BaseDao;
import com.sunshine.framework.mvc.mysql.service.impl.BaseServiceImpl;
import com.sunshine.platform.security.dao.UserRoleDao;
import com.sunshine.platform.security.entity.UserRole;
import com.sunshine.platform.security.service.UserRoleService;

@Service(value = "userRoleService")
public class UserRoleServiceImpl extends BaseServiceImpl<UserRole, String> implements UserRoleService {
	private UserRoleDao userRoleDao = SpringContextHolder.getBean(UserRoleDao.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sunshine.framework.mvc.service.impl.BaseServiceImpl#getDao()
	 */
	@Override
	protected BaseDao<UserRole, String> getDao() {
		// TODO Auto-generated method stub
		return userRoleDao;
	}

	@Override
	public List<UserRole> findUserRoleByRole(String roleId) {
		return userRoleDao.findUserRoleByRole(roleId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sunshine.platform.security.dao.RoleDao#findRoleByCode(java.lang.String)
	 */
	@Override
	public List<UserRole> findUserRoleByUser(String userId) {
		return userRoleDao.findUserRoleByUser(userId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sunshine.platform.security.service.UserRoleService#findUserRoleByUserList(java.util.List)
	 */
	@Override
	public List<UserRole> findUserRoleByUserList(List<String> userIds) {
		// TODO Auto-generated method stub
		return userRoleDao.findUserRoleByUserList(userIds);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sunshine.platform.security.service.UserRoleService#findUserRoleByRoleList(java.util.List)
	 */
	@Override
	public List<UserRole> findUserRoleByRoleList(List<String> roleIds) {
		// TODO Auto-generated method stub
		return userRoleDao.findUserRoleByRoleList(roleIds);
	}

}

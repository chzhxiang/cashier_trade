package com.sunshine.platform.security.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.sunshine.framework.common.spring.ext.SpringContextHolder;
import com.sunshine.framework.mvc.mysql.dao.BaseDao;
import com.sunshine.framework.mvc.mysql.service.impl.BaseServiceImpl;
import com.sunshine.platform.security.dao.ResourceDao;
import com.sunshine.platform.security.dao.RoleDao;
import com.sunshine.platform.security.dao.RoleResourceDao;
import com.sunshine.platform.security.dao.UserRoleDao;
import com.sunshine.platform.security.entity.Role;
import com.sunshine.platform.security.entity.RoleResource;
import com.sunshine.platform.security.service.RoleService;

@Service(value = "roleService")
public class RoleServiceImpl extends BaseServiceImpl<Role, String> implements RoleService {
	private RoleDao roleDao = SpringContextHolder.getBean(RoleDao.class);
	private RoleResourceDao roleResourceDao = SpringContextHolder.getBean(RoleResourceDao.class);
	private ResourceDao resourceDao = SpringContextHolder.getBean(ResourceDao.class);
	private UserRoleDao userRoleDao = SpringContextHolder.getBean(UserRoleDao.class);

	@Override
	protected BaseDao<Role, String> getDao() {
		// TODO Auto-generated method stub
		return roleDao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sunshine.platform.security.service.RoleService#isUniqueName(com.sunshine.platform.security.entity.SysRole)
	 */
	@Override
	public boolean isUniqueRoleName(Role role) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("roleName", role.getRoleName());
		List<Role> roles = roleDao.findRoleByProperties(paramMap);
		if (CollectionUtils.isEmpty(roles)) {
			return true;
		} else {
			if (roles.size() == 1 && StringUtils.isNotBlank(role.getId())) {
				if (role.getId().equalsIgnoreCase(roles.get(0).getId())) {
					return true;
				}
			}
			return false;
		}
	}

	@Override
	public boolean isUniqueRoleCode(Role role) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("roleCode", role.getRoleCode());
		List<Role> roles = roleDao.findRoleByProperties(paramMap);
		if (CollectionUtils.isEmpty(roles)) {
			return true;
		} else {
			if (roles.size() == 1 && StringUtils.isNotBlank(role.getId())) {
				if (role.getId().equalsIgnoreCase(roles.get(0).getId())) {
					return true;
				}
			}
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sunshine.platform.security.service.RoleService#findAllRole()
	 */
	@Override
	public List<Role> findAllRole() {
		// TODO Auto-generated method stub
		return roleDao.findAllRole();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sunshine.platform.security.service.RoleService#findUserAllRole(java.lang.String)
	 */
	@Override
	public List<Role> findRoleByUserId(String userId) {
		// TODO Auto-generated method stub
		return roleDao.findRoleByUserId(userId);
	}

	@Override
	public String saveRole(Role role) {
		// TODO Auto-generated method stub
		String roleId = insert(role);
		saveRoleResource(role);
		return roleId;
	}

	private void saveRoleResource(Role role) {
		List<String> resourceList = role.getResourceIdList();
		if (!CollectionUtils.isEmpty(resourceList)) {
			List<RoleResource> roleResources = new ArrayList<>();
			for (String resourceId : resourceList) {
				RoleResource roleResource = new RoleResource();
				roleResource.setResourceId(resourceId);
				roleResource.setRoleId(role.getId());
				roleResource.setStatus(1);
				roleResources.add(roleResource);
			}
			roleResourceDao.batchInsert(roleResources);
		}
	}

	@Override
	public void updateRole(Role role) {
		// TODO Auto-generated method stub
		update(role);
		List<String> roleIds = new ArrayList<>();
		roleIds.add(role.getId());
		// 删除旧的角色与资源关系
		roleResourceDao.deleteByRoleIds(roleIds);
		// 写入新的角色与资源关系
		saveRoleResource(role);
	}

	@Override
	public Long deleteByIds(List<String> roleIds) {
		// TODO Auto-generated method stub
		// 删除用户角色数据
		userRoleDao.deleteByRoleIds(roleIds);

		// 删除角色资源数据
		roleResourceDao.deleteByRoleIds(roleIds);

		return super.deleteByIds(roleIds);

		// 更新用户的roleIds信息 因不更新也不影响业务结果而更新太复杂,故不更新
	}

	@Override
	public List<Role> findAllRoleWithUserInfo(String userId) {
		// TODO Auto-generated method stub
		return roleDao.findAllRoleWithUserInfo(userId);
	}

	/* (non-Javadoc)
	 * @see com.sunshine.platform.security.service.RoleService#findRoleByCode(java.lang.String)
	 */
	@Override
	public Role findRoleByCode(String roleCode) {
		// TODO Auto-generated method stub
		return roleDao.findRoleByCode(roleCode);
	}

}

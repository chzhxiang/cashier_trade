package com.sunshine.platform.security.service;

import java.util.List;

import com.sunshine.framework.mvc.mysql.service.BaseSQLService;
import com.sunshine.platform.security.entity.Role;

public interface RoleService extends BaseSQLService<Role, String> {

	/**
	 * @param role
	 * @return
	 */
	public boolean isUniqueRoleName(Role role);

	/**
	 * @param role
	 * @return
	 */
	public boolean isUniqueRoleCode(Role role);

	/**
	 * @return
	 */
	public List<Role> findAllRole();

	/**
	 * @param userId
	 * @return
	 */
	public List<Role> findRoleByUserId(String userId);

	public String saveRole(Role role);

	public void updateRole(Role role);

	@Override
	public Long deleteByIds(List<String> roleIds);

	/**
	 * @Description 查找所有角色信息,并为userId对应的role加入hasUser的判断信息
	 * @param userId
	 * @return
	 * @date 2017年7月6日
	 */
	public List<Role> findAllRoleWithUserInfo(String userId);

	/**
	 * @Description 根据角色编码查询角色
	 * @param roleCode
	 * @return
	 * @date 2017年7月5日
	 */
	public Role findRoleByCode(String roleCode);

}

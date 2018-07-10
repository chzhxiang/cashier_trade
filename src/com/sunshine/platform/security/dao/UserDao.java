package com.sunshine.platform.security.dao;

import java.util.List;

import com.sunshine.framework.mvc.mysql.dao.BaseDao;
import com.sunshine.platform.security.entity.User;

public interface UserDao extends BaseDao<User, String> {

	/**
	 * @param account
	 * @return
	 */
	public User findUserByAccount(String account);

	public void updateRoleIds(String userId, String roleIds);

	/**
	 * @Description 根据部门ID 更新部门名称
	 * @param deptId
	 * @date 2017年7月11日
	 */
	public void updateDeptNameByDeptId(String deptId, String deptName);

	/**
	 * @Description 去除人员的部门信息
	 * @param deptIds
	 * @date 2017年7月11日
	 */
	public Long removeDeptInfo(List<String> deptIds);
}

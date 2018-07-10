package com.sunshine.platform.security.service;

import java.util.Map;

import com.sunshine.framework.mvc.mysql.service.BaseSQLService;
import com.sunshine.platform.security.entity.User;

public interface UserService extends BaseSQLService<User, String> {

	/**
	 * @param account
	 * @return
	 */
	public User findUserByAccount(String account);

	/**
	 * @param user
	 * @return
	 */
	public boolean isUniqueAccount(User user);

	/**
	 * @Description 保存新用户
	 * @param user
	 * @return 用户Id
	 * @date 2017年7月3日
	 */
	public String saveUser(User user);

	/**
	 * @Description 更新用户信息
	 * @param user
	 * @date 2017年7月3日
	 */
	public void updateUser(User user);

	/**
	 * @Description 修改密码
	 * @param userId
	 *            用户ID
	 * @param oldPassword
	 *            原密码
	 * @param newPassword
	 *            新密码
	 * @date 2017年8月29日
	 */
	public Map<String, Object> updatePassword(String userId, String oldPassword, String newPassword);
}

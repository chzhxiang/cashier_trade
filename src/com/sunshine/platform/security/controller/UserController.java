package com.sunshine.platform.security.controller;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.sunshine.common.GlobalConstant;
import com.sunshine.common.controller.BasePlatformController;
import com.sunshine.framework.exception.SystemException;
import com.sunshine.framework.mvc.controller.RespBody;
import com.sunshine.framework.mvc.controller.RespBody.StatusEnum;
import com.sunshine.framework.mvc.mysql.service.BaseSQLService;
import com.sunshine.framework.utils.PWDEncryptUtil;
import com.sunshine.platform.security.entity.Role;
import com.sunshine.platform.security.entity.User;
import com.sunshine.platform.security.service.RoleService;
import com.sunshine.platform.security.service.UserService;

@Controller
@RequestMapping("/platform/user")
public class UserController extends BasePlatformController<User, String> {

	private static Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Override
	protected BaseSQLService<User, String> getService() {
		// TODO Auto-generated method stub
		return userService;
	}

	/**
	 * 用户管理列表
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @param search
	 * @param modelMap
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/list")
	public ModelAndView list() {
		return new ModelAndView("/platform/security/user/userList");
	}

	@RequestMapping(value = "/toModifyPassword")
	public ModelAndView toModifyPassword(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("/platform/security/user/modifyPassword");
		modelAndView.addObject("user", getPlatformUser(request));
		return modelAndView;
	}

	/**
	 * 修改用户密码
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/modifyPassword", method = RequestMethod.POST)
	public RespBody modifyPassword(String userId, String oldPassword, String newPassword) {
		Map<String, Object> resMap = userService.updatePassword(userId, oldPassword, newPassword);
		return new RespBody(StatusEnum.OK, resMap);
	}

	/**
	 * @Description 当前登录人的个人资料信息
	 * @param request
	 * @return
	 * @date 2017年8月28日
	 */
	@RequestMapping(value = "/curUserInfo")
	public ModelAndView curUserInfo(HttpServletRequest request) {
		User user = getPlatformUser(request);
		ModelAndView modelAndView = new ModelAndView("/platform/security/user/curUserInfo");
		modelAndView.addObject("user", user);
		return modelAndView;
	}

	/**
	 * 保存用户信息
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	public RespBody saveOrUpdate(User user) {
		if (logger.isDebugEnabled()) {
			logger.debug("保存用户信息, user:{}", JSON.toJSONString(user));
		}

		boolean isUnique = userService.isUniqueAccount(user);
		if (!isUnique) {
			return new RespBody(StatusEnum.ERROR, "保存用户失败，帐号" + user.getAccount() + "已经存在！");
		}
		if (StringUtils.isNotEmpty(user.getId())) {
			user.setEt(new Date());
			userService.updateUser(user);

			// 更新Session当前登录者的信息
			org.apache.shiro.subject.Subject subject = SecurityUtils.getSubject();
			User currentUser = (User) subject.getSession().getAttribute(GlobalConstant.SESSION_PLATFORM_USER_KEY); // 获取会话
			if (currentUser.getAccount().equalsIgnoreCase(user.getAccount())) {
				// 只替换帐号信息
				BeanUtils.copyProperties(user, currentUser, new String[] { "sex", "idCardNo", "birthDay", "title", "introduction" });
				subject.getSession().setAttribute(GlobalConstant.SESSION_PLATFORM_USER_KEY, user);
			}
		} else {
			user.setCt(new Date());
			user.setStatus(1);
			try {
				user.setPassword(PWDEncryptUtil.encrypt(user.getPassword()));
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				logger.error(String.format("MD5密码加密错误"), e);
				throw new SystemException(String.format("MD5密码加密错误"), e);
			}
			user.setId((userService.saveUser(user)));
		}
		return new RespBody(StatusEnum.OK, user);
	}

	/**
	 * 更新用户信息 区分saveOrUpdate 不进行帐号唯一判断
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/updateUserInfo", method = RequestMethod.POST)
	public RespBody updateUserInfo(User user) {
		if (logger.isDebugEnabled()) {
			logger.debug("updateUserInfo 更新个人资料, user:{}", JSON.toJSONString(user));
		}
		user.setEt(new Date());
		userService.updateUser(user);

		// 更新Session当前登录者的信息
		org.apache.shiro.subject.Subject subject = SecurityUtils.getSubject();
		User currentUser = (User) subject.getSession().getAttribute(GlobalConstant.SESSION_PLATFORM_USER_KEY); // 获取会话
		if (currentUser.getAccount().equalsIgnoreCase(user.getAccount())) {
			subject.getSession().setAttribute(GlobalConstant.SESSION_PLATFORM_USER_KEY, user);
		}
		return new RespBody(StatusEnum.OK, user);
	}

	/**
	 * 新增页面跳转
	 * 
	 * @param id
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/toAdd")
	public ModelAndView toAdd(HttpServletRequest request) {
		// 查询所有角色
		List<Role> list = roleService.findAllRole();
		String result = "/platform/security/user/addUser";
		ModelAndView view = new ModelAndView(result);
		User user = new User();
		view.addObject("user", user);
		view.addObject("roleList", list);
		return view;
	}

	/**
	 * 修改页面跳转
	 * 
	 * @param id
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/toEdit")
	public ModelAndView toEdit(HttpServletRequest request) {
		String userId = request.getParameter("id");
		List<Role> roleList = roleService.findAllRoleWithUserInfo(userId);
		User user = userService.findById(userId);
		String result = "/platform/security/user/editUser";
		ModelAndView view = new ModelAndView(result);
		view.addObject("roleList", roleList);
		view.addObject("user", user);
		return view;
	}

	/**
	 * 删除用户
	 * 
	 * @param id
	 * @param modelMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/toDelete", method = RequestMethod.POST)
	public RespBody toDelete(HttpServletRequest request) {
		String userIds = request.getParameter("ids");
		if (userIds == null || userIds.equals("")) {
			return new RespBody(StatusEnum.ERROR, "删除用户失败");
		}
		Set<String> idSet = new HashSet<>();
		idSet.addAll(Arrays.asList(userIds.split(",")));

		if (idSet.contains("1")) {
			return new RespBody(StatusEnum.ERROR, "删除用户失败,原因：admin用户不允许删除");
		}

		userService.deleteByIds(Arrays.asList(userIds.split(",")));

		return new RespBody(StatusEnum.OK, "删除用户成功");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sunshine.framework.mvc.controller.BaseController#getService()
	 */

	/**
	 * 个人设置页面
	 * 
	 * @return
	 */
	@RequestMapping("/setting")
	public ModelAndView setting(HttpServletRequest request) {

		String result = "/platform/security/personcenter/setting";
		ModelAndView view = new ModelAndView(result);

		String account = request.getParameter("account");
		User user = userService.findUserByAccount(account);
		view.addObject("user", user);

		List<Role> roleList = roleService.findRoleByUserId(user.getId());
		view.addObject("roleList", roleList);

		return view;
	}

	/**
	 * 显示修改密码页面
	 * 
	 * @param PersonCenterParamsVo
	 * @return
	 */
	@RequestMapping(value = "/changepassword")
	public ModelAndView changePassword(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("/platform/security/personcenter/changepassword");

		String account = request.getParameter("account");
		User user = userService.findUserByAccount(account);
		modelAndView.addObject("user", user);

		return modelAndView;
	}

}

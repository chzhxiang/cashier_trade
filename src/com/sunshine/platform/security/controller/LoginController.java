/**
 * <html>
 * <body>
 *  <P> Copyright 2017 阳光康众</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年6月28日</p>
 *  <p> Created by 于策/yu.ce@foxmail.com</p>
 *  </body>
 * </html>
 */
package com.sunshine.platform.security.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sunshine.common.GlobalConstant;
import com.sunshine.common.datas.cache.platform.security.LoginTimesCache;
import com.sunshine.framework.common.spring.ext.SpringContextHolder;
import com.sunshine.framework.config.SystemConfig;
import com.sunshine.platform.security.entity.Resource;
import com.sunshine.platform.security.entity.User;
import com.sunshine.platform.security.service.ResourceService;

/**
 * @Project ChuFangLiuZhuan_PlatForm
 * @Package com.sunshine.platform.login
 * @ClassName LoginController.java
 * @Description 后台系统登录
 * @JDK version used 1.8
 * @Author 于策/yu.ce@foxmail.com
 * @Create Date 2017年6月28日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
@Controller
public class LoginController {
	private static Logger logger = LoggerFactory.getLogger(LoginController.class);
	private ResourceService resorceService = SpringContextHolder.getBean(ResourceService.class);
	private LoginTimesCache loginTimesCache = SpringContextHolder.getBean(LoginTimesCache.class);

	/**
	 * 登录页面
	 * 
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/pf_toLogin")
	public ModelAndView toLogin(HttpServletRequest request) {
		logger.info("pf_doLogin....");
		ModelAndView view = new ModelAndView("/platform/login/login");
		return view;
	}

	/**
	 * 登录验证 返回主页
	 * 
	 * @param id
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/pf_doLogin")
	public ModelAndView doLogin(RedirectAttributes redirectAttributes, HttpServletRequest request) throws Exception {
		String userAccount = request.getParameter("account");
		String password = request.getParameter("password");
		int errorTimes = 0;

		ModelAndView view = null;
		String msg = null;
		try {
			UsernamePasswordToken token = new UsernamePasswordToken(userAccount, password);
			token.setRememberMe(true);
			Subject subject = SecurityUtils.getSubject();
			subject.login(token);

			if (subject.isAuthenticated()) {
				User user = (User) subject.getSession().getAttribute(GlobalConstant.SESSION_PLATFORM_USER_KEY);
				request.getSession().setAttribute(GlobalConstant.SESSION_PLATFORM_USER_KEY, user);
				request.getSession().setAttribute(GlobalConstant.SESSION_LOGIN_TIMES, 0);
				List<Resource> menuList = resorceService.findMenuList();

				String systemVersion = SystemConfig.getStringValue("system.publish.version");

				// 0开发，1测试，2正式
				if (GlobalConstant.SYSTEM_PUBLISH_VERSION_DEV.equals(systemVersion)) {
					systemVersion = "开发版";
				} else if (GlobalConstant.SYSTEM_PUBLISH_VERSION_TEST.equals(systemVersion)) {
					systemVersion = "测试版";
				} else if (GlobalConstant.SYSTEM_PUBLISH_VERSION_RC.equals(systemVersion)) {
					systemVersion = "正式版";
				}

				view = new ModelAndView("/platform/index/index");
				view.addObject("menuList", menuList);
				view.addObject("user", user);
				view.addObject("systemVersion", systemVersion);
				// 清空登录次数缓存
				loginTimesCache.clearTimes(userAccount);
				return view;
			} else {
				loginTimesCache.clearTimes(userAccount);
				return new ModelAndView("redirect:/pf_toLogin");
			}
		} catch (IncorrectCredentialsException e) {
			msg = "登录密码错误";
			errorTimes = loginTimesCache.loginErrorTimes(userAccount);
			logger.info("do login msg:{}", msg);
		} catch (ExcessiveAttemptsException e) {
			msg = "登录失败次数过多";
			logger.info("do login msg:{}", msg);
		} catch (LockedAccountException e) {
			msg = "帐号已被锁定.";
			logger.info("do login msg:{}", msg);
			System.out.println(msg);
		} catch (DisabledAccountException e) {
			msg = "帐号已被禁用.";
			logger.info("do login msg:{}", msg);
		} catch (ExpiredCredentialsException e) {
			msg = "帐号已过期.";
			logger.info("do login msg:{}", msg);
		} catch (UnknownAccountException e) {
			msg = "帐号不存在.";
			logger.info("do login msg:{}", msg);
		} catch (UnauthorizedException e) {
			msg = "您没有得到相应的授权！";
			logger.info("do login msg:{}", msg);
		} catch (Exception e) {
			logger.info("do login msg:{}", msg);
		}

		view = new ModelAndView("redirect:/pf_toLogin");
		redirectAttributes.addFlashAttribute("message", msg);
		redirectAttributes.addFlashAttribute("loginTims", errorTimes);
		return view;
	}

	@RequestMapping("/403")
	public String unauthorizedRole() {
		return "/403";
	}

	@RequestMapping("/pf_building")
	public ModelAndView building() {
		ModelAndView view = new ModelAndView("/platform/common/building");
		return view;
	}

	/*@RequestMapping(value = "/pf_logout")
	public ModelAndView logout(RedirectAttributes redirectAttributes, HttpServletRequest request) {
		// 使用权限管理工具进行用户的退出，跳出登录，给出提示信息
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		session.removeAttribute(GlobalConstant.SESSION_PLATFORM_USER_KEY);
		currentUser.logout();
		redirectAttributes.addFlashAttribute("message", "您已安全退出");
		return new ModelAndView("redirect:/pf_toLogin");
	}*/
}

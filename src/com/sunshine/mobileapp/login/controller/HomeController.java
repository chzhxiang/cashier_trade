package com.sunshine.mobileapp.login.controller;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sunshine.common.GlobalConstant;
import com.sunshine.common.controller.BasePlatformController;
import com.sunshine.framework.mvc.service.BaseService;
import com.sunshine.mobileapp.login.entity.AppUser;
import com.sunshine.mobileapp.login.service.AppUserService;

/**
 * 
 * @Project: Prescription_PlatForm 
 * @Package: com.sunshine.mobileapp.login
 * @ClassName: LoginController
 * @Description: <p>	app端手机登录     </p>
 * @JDK version used: 
 * @Author: 香薷
 * @Create Date: 2017年9月5日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
@Controller
@RequestMapping("/mobileapp")
public class HomeController extends BasePlatformController<AppUser, String> {

	@Autowired
	private AppUserService appUserService;
	
	/**
	 * 登录页面
	 * 
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/toLogin", method = RequestMethod.GET)
	public ModelAndView toLogin(HttpServletRequest request) {
		ModelAndView view = new ModelAndView("/mobileapp/login");
		return view;
	}
	
	/**
	 * 跳转首页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/doLogin")
	public ModelAndView doLogin(RedirectAttributes redirectAttributes, HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		String userAccount = request.getParameter("account");
		String password = request.getParameter("password");
		Map map=new HashMap();
		map.put("account", userAccount);
		map.put("password", password);
		AppUser appUser=appUserService.findByAccount(map);
		if( null == appUser) {
			view = new ModelAndView("redirect:/mobileapp/toLogin");
			redirectAttributes.addFlashAttribute("message", "账号密码错误");
			return view;
		}
		Subject subject = SecurityUtils.getSubject();
		subject.getSession().setAttribute(GlobalConstant.SESSION_MOBILE_USER_KEY, appUser);
		view.setViewName("/mobileapp/index/index");
		return view;
	}
	
	/**
	 * 跳转首页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/toHomePage")
	public ModelAndView toHomePage(HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		view.setViewName("/mobileapp/index/index");
		return view;
	}
	
	/**
	 * 建设中界面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/toBuild")
	public ModelAndView toBuild(HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		view.setViewName("/mobileapp/common/bulid");
		return view;
	}
	
	/**
	 * 建设中界面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/toPatient")
	public ModelAndView toPatient(HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		view.setViewName("/mobileapp/family/addFamily");
		return view;
	}
	
	
	@Override
	protected BaseService<AppUser, String> getService() {
		return appUserService;
	}

}

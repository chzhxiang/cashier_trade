package com.sunshine.platform.security.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sunshine.common.GlobalConstant;
import com.sunshine.common.controller.BasePlatformController;
import com.sunshine.framework.mvc.controller.RespBody;
import com.sunshine.framework.mvc.controller.RespBody.StatusEnum;
import com.sunshine.framework.mvc.mysql.service.BaseSQLService;
import com.sunshine.platform.security.entity.Role;
import com.sunshine.platform.security.service.RoleService;

@Controller
@RequestMapping("/platform/role")
public class RoleController extends BasePlatformController<Role, String> {

	@Autowired
	private RoleService roleService;

	@Override
	protected BaseSQLService<Role, String> getService() {
		return roleService;
	}

	/**
	 * 角色管理列表
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
		return new ModelAndView("/platform/security/role/roleList");
	}

	/**
	 * 保存角色信息
	 * 
	 * @param entity
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	public RespBody saveOrUpdate(Role role) {
		Map<String, Object> resMap = new HashMap<>();
		boolean isUniqueName = roleService.isUniqueRoleName(role);
		if (!isUniqueName) {
			resMap.put(GlobalConstant.MOTHED_INVOKE_RES_IS_SUCCESS, false);
			resMap.put(GlobalConstant.MOTHED_INVOKE_RES_MSG, "保存角色失败，角色名称".concat(role.getRoleName()).concat("已经存在！"));
		} else {
			boolean isUniqueCode = roleService.isUniqueRoleCode(role);
			if (!isUniqueCode) {
				resMap.put(GlobalConstant.MOTHED_INVOKE_RES_IS_SUCCESS, false);
				resMap.put(GlobalConstant.MOTHED_INVOKE_RES_MSG, "保存角色失败，角色编码".concat(role.getRoleCode()).concat("已经存在！"));
			} else {
				if (StringUtils.isNotEmpty(role.getId())) {
					role.setEt(new Date());
					roleService.updateRole(role);
				} else {
					role.setCt(new Date());
					role.setStatus(1);
					role.setId(roleService.saveRole(role));
				}
				resMap.put(GlobalConstant.MOTHED_INVOKE_RES_IS_SUCCESS, true);
				resMap.put(GlobalConstant.MOTHED_INVOKE_RES_MSG, "操作成功");
			}
		}
		return new RespBody(StatusEnum.OK, resMap);
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
		Role role = new Role();
		String result = "/platform/security/role/editRole";
		ModelAndView view = new ModelAndView(result);
		view.addObject("role", role);
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
		String roleId = request.getParameter("id");
		Role role = roleService.findById(roleId);
		String result = "/platform/security/role/editRole";
		ModelAndView view = new ModelAndView(result);
		view.addObject("role", role);
		return view;
	}

	/**
	 * 删除角色
	 * 
	 * @param id
	 * @param modelMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/toDelete", method = RequestMethod.POST)
	public RespBody toDelete(HttpServletRequest request) {
		String roleIds = request.getParameter("id");
		if (StringUtils.isBlank(roleIds)) {
			return new RespBody(StatusEnum.ERROR, "删除用户失败");
		}
		List<String> rolelist = new ArrayList<>();
		String[] roleArray = roleIds.split(",");
		for (String roleId : roleArray) {
			rolelist.add(roleId);
		}

		roleService.deleteByIds(rolelist);
		return new RespBody(StatusEnum.OK, "删除角色成功");
	}
}

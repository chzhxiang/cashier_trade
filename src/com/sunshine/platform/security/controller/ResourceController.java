package com.sunshine.platform.security.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.sunshine.common.GlobalConstant;
import com.sunshine.common.controller.BasePlatformController;
import com.sunshine.framework.mvc.controller.RespBody;
import com.sunshine.framework.mvc.controller.RespBody.StatusEnum;
import com.sunshine.framework.mvc.mysql.service.BaseSQLService;
import com.sunshine.platform.security.SecurityContant;
import com.sunshine.platform.security.entity.Resource;
import com.sunshine.platform.security.service.ResourceService;

@Controller
@RequestMapping("/platform/resource")
public class ResourceController extends BasePlatformController<Resource, String> {

	private static Logger logger = LoggerFactory.getLogger(ResourceController.class);
	@Autowired
	private ResourceService resorceService;

	@Override
	protected BaseSQLService<Resource, String> getService() {
		// TODO Auto-generated method stub
		return resorceService;
	}

	/**
	 * 菜单管理列表
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
		return new ModelAndView("/platform/security/resource/resourceList");
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
		String result = "/platform/security/resource/editResource";
		ModelAndView view = new ModelAndView(result);
		Resource resource = new Resource();
		view.addObject("resource", resource);
		view.addObject("subCount", 0);
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
		String resourceId = request.getParameter("id");
		List<Resource> subResources = resorceService.findResourceByParentId(resourceId);
		Resource resource = resorceService.findById(resourceId);
		String result = "/platform/security/resource/editResource";
		ModelAndView view = new ModelAndView(result);
		view.addObject("resource", resource);
		view.addObject("subResources", subResources);
		view.addObject("subCount", subResources.size());
		return view;
	}

	/**
	 * 保存菜单信息
	 * 
	 * @param entity
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	public RespBody saveOrUpdate(Resource resource) {
		resource.setType(SecurityContant.RESOURCE_TYPE_MENU);
		Map<String, Object> resMap = new HashMap<>();

		boolean isUniqueName = resorceService.isUniqueName(resource);
		if (!isUniqueName) {
			resMap.put(GlobalConstant.MOTHED_INVOKE_RES_IS_SUCCESS, false);
			resMap.put(GlobalConstant.MOTHED_INVOKE_RES_MSG, "保存资源失败，资源名称".concat(resource.getName()).concat("已经存在！"));
		} else {
			boolean isUniqueCode = resorceService.isUniqueCode(resource);
			if (!isUniqueCode) {
				resMap.put(GlobalConstant.MOTHED_INVOKE_RES_IS_SUCCESS, false);
				resMap.put(GlobalConstant.MOTHED_INVOKE_RES_MSG, "保存资源失败，资源编码".concat(resource.getCode()).concat("已经存在！"));
			} else {
				if (StringUtils.isNotEmpty(resource.getId())) {
					resource.setEt(new Date());
					resMap = resorceService.updateResource(resource);
				} else {
					resource.setStatus(1);
					resource.setCt(new Date());
					resMap = resorceService.saveResource(resource);
				}
			}
		}
		return new RespBody(StatusEnum.OK, resMap);
	}

	/**
	 * 保存按钮信息
	 * 
	 * @param entity
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveButton", method = RequestMethod.POST)
	public RespBody saveButton(Resource resource) {
		resource.setType(SecurityContant.RESOURCE_TYPE_BUTTON);
		logger.info("保存按钮信息, user:{}", JSON.toJSONString(resource));
		boolean isUniqueCode = resorceService.isUniqueCode(resource);
		if (!isUniqueCode) {
			return new RespBody(StatusEnum.ERROR, "保存资源失败，资源编号" + resource.getCode() + "已经存在！");
		}

		if (StringUtils.isNotEmpty(resource.getId())) {
			resource.setEt(new Date());
			resorceService.update(resource);
		} else {
			resource.setCt(new Date());
			resource.setId(resorceService.insert(resource));
		}
		return new RespBody(StatusEnum.OK);
	}

	/**
	 * 删除资源
	 * 
	 * @param id
	 * @param modelMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/toDelete", method = RequestMethod.POST)
	public RespBody toDelete(HttpServletRequest request) {
		String resourceIds = request.getParameter("id");
		if (resourceIds == null || resourceIds.equals("")) {
			return new RespBody(StatusEnum.ERROR, "删除菜单失败");
		}
		List<String> resourceIdList = Arrays.asList(resourceIds.split(","));
		resorceService.deleteWithSubByIds(resourceIdList);
		return new RespBody(StatusEnum.OK, "删除菜单成功");
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
	@RequestMapping(value = "/findMenuListByParentId")
	@ResponseBody
	public ModelAndView findMenuListByParentId(HttpServletRequest request) {
		String resourceId = request.getParameter("resourceId");
		logger.info("加载子菜单列表, search:{}", resourceId);
		List<Resource> list = resorceService.findMenuListByParentId(resourceId);
		ModelAndView view = new ModelAndView("/platform/childMenu");
		view.addObject("childMenuList", list);
		return view;
	}

	/**
	 * 菜单管理下拉列表
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @param search
	 * @param modelMap
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/findAllMenuList")
	@ResponseBody
	public RespBody findAllMenuList(HttpServletRequest request) {
		List<Resource> menus = resorceService.findMenuList();
		return new RespBody(StatusEnum.OK, menus);
	}
}

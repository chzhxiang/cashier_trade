package com.sunshine.common.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.sunshine.common.GlobalConstant;
import com.sunshine.framework.mvc.controller.BaseController;
import com.sunshine.framework.mvc.controller.CheckParams;
import com.sunshine.framework.mvc.controller.PageParams;
import com.sunshine.framework.mvc.controller.RespBody;
import com.sunshine.framework.mvc.controller.RespBody.StatusEnum;
import com.sunshine.framework.mvc.mysql.entity.BaseSQLEntity;
import com.sunshine.framework.utils.StringHelper;
import com.sunshine.platform.security.entity.User;

/**
 * @Project ChuFangLiuZhuan_PlatForm
 * @Package com.sunshine.common.controller
 * @ClassName BasePlatformController.java
 * @Description 后台控制类基类
 * @JDK version used 1.8
 * @Author 于策/yu.ce@foxmail.com
 * @Create Date 2017年6月29日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
public abstract class BasePlatformController<T extends BaseSQLEntity, PK extends Serializable> extends BaseController<T, PK> {
	private static Logger logger = LoggerFactory.getLogger(BasePlatformController.class);

	/**
	 * 保存
	 * 
	 * @param entity
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/save")
	public RespBody save(T entity, HttpServletRequest request) {
		User sysUser = getPlatformUser(request);
		setInformation(sysUser, entity);
		if (StringUtils.isNotBlank(entity.getId())) {
			getService().update(entity);
		} else {
			getService().insert(entity);
		}
		return new RespBody(StatusEnum.OK, entity, "保存成功!");
	}

	/**
	 * BaseEnity信息设置
	 * 
	 * @Description
	 * @param entity
	 * @param request
	 * @date 2017年5月23日
	 */
	protected void setCurUserInfo(BaseSQLEntity entity, HttpServletRequest request) {
		User sysUser = getPlatformUser(request);
		setInformation(sysUser, entity);
	}

	/**
	 * 删除
	 * 
	 * @param entity
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delete")
	public RespBody delete(PK id, HttpServletRequest request) {
		if (id == null) {
			logger.error("要删除的ID号为null或空字符串！对象：{}", this.getClass().getName());
			return new RespBody(StatusEnum.ERROR, "删除失败,ID不能为空!");
		}
		getService().deleteById(id);
		return new RespBody(StatusEnum.OK, "删除成功!");
	}

	/**
	 * 根据ID集合删除
	 * 
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteByIds")
	public RespBody deleteByIds(String ids, HttpServletRequest request) {
		if (StringUtils.isBlank(ids)) {
			logger.error("参数:{}不能为空！对象：{}", ids, this.getClass().getName());
			return new RespBody(StatusEnum.ERROR, "删除失败,ID集合不能为空!");
		}
		@SuppressWarnings("unchecked")
		List<PK> idsList = StringHelper.strToArrayList(ids);
		getService().deleteByIds(idsList);
		return new RespBody(StatusEnum.OK, "批量删除成功!");
	}

	/**
	 * 根据ID查询
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findById")
	public RespBody findById(PK id, HttpServletRequest request) {
		if (id == null) {
			logger.error("参数:{}不能为null或空字符串！对象:{}", id, this.getClass().getName());
			return new RespBody(StatusEnum.ERROR, "没有传入要查询的ID！");
		}
		T t = getService().findById(id);
		return new RespBody(StatusEnum.OK, t);
	}

	/**
	 * 根据ID集合查询
	 * 
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findByIds")
	public RespBody findByIds(List<PK> ids, HttpServletRequest request) {
		if (ids != null && ids.size() > 0) {
			logger.error("未设置批量删除对象的ID号！对象：{}", this.getClass().getName());
			return new RespBody(StatusEnum.ERROR, "没有传入要查询的ID集合！");
		}
		List<T> list = getService().findByIds(ids);
		return new RespBody(StatusEnum.OK, list);
	}

	/**
	 * 跳转列表页面
	 * 
	 * @param query
	 * @param pageBounds
	 * @return
	 */
	@RequestMapping(value = "/listView")
	public String listView(HttpServletRequest request) {
		return getBasePath(request) + "/list";
	}

	/**
	 * @Description 分页列表查询
	 * @param pageNum
	 * @param pageSize
	 * @param pageParams
	 * @return
	 * @date 2017年6月29日
	 */
	@ResponseBody
	@RequestMapping(value = "/findListByPage")
	public PageInfo<T> findListByPage(@RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize, PageParams pageParams) {
		PageInfo<T> pageInfo = getService().findListByPage(pageParams.getParams(), pageParams.getSortParams(), new Page<T>(pageNum, pageSize));
		return pageInfo;
	}

	/**
	 * @Description 查询所有记录
	 * @param request
	 * @return
	 * @date 2017年6月29日
	 */
	@ResponseBody
	@RequestMapping(value = "/findAll")
	public RespBody findAll(HttpServletRequest request) {
		List<T> list = getService().findAll();
		return new RespBody(StatusEnum.OK, list);
	}

	/**
	 * @Description 跳转编辑页面
	 * @param id
	 * @param request
	 * @return
	 * @date 2017年6月29日
	 */
	@RequestMapping(value = "/edit")
	public ModelAndView edit(PK id, HttpServletRequest request) {
		T t = null;
		if (id != null) {
			t = getService().findById(id);
		}
		return new ModelAndView(getBasePath(request) + "/edit", "entity", t);
	}

	/**
	 * @Description 检查数据是否已经存在
	 * @param checkParams
	 * @return
	 * @date 2017年6月29日
	 */
	@ResponseBody
	@RequestMapping(value = "/check")
	public RespBody check(CheckParams checkParams) {
		boolean status = getService().check(checkParams.getParams());
		if (status) {
			return new RespBody(StatusEnum.ERROR);
		} else {
			return new RespBody(StatusEnum.OK);
		}
	}

	/**
	 * @Description 设置创建人,更新人信息
	 * @param sysUser
	 * @param entity
	 * @date 2017年6月29日
	 */
	public void setInformation(User sysUser, BaseSQLEntity entity) {
		if (sysUser != null) {
			Date date = new Date();
			if (StringUtils.isNotBlank(entity.getId())) {
				entity.setEp(sysUser.getId());
				entity.setEpName(sysUser.getAccount());
				entity.setEt(date);
			} else {
				entity.setCp(sysUser.getId());
				entity.setCpName(sysUser.getAccount());
				entity.setCt(date);
				entity.setEp(sysUser.getId());
				entity.setEpName(sysUser.getAccount());
				entity.setEt(date);
			}
		}
	}

	/**
	 * @Description 设置创建人,更新人信息
	 * @param sysUser
	 * @param entities
	 * @date 2017年6月29日
	 */
	public void setInformations(User sysUser, List<BaseSQLEntity> entities) {
		if (sysUser != null) {
			for (BaseSQLEntity entity : entities) {
				if (entity.getId() != null) {
					entity.setEp(sysUser.getId());
					entity.setEpName(sysUser.getAccount());
					entity.setEt(new Date());
				} else {
					Date date = new Date();
					entity.setCp(sysUser.getId());
					entity.setCpName(sysUser.getAccount());
					entity.setCt(date);
					entity.setEp(sysUser.getId());
					entity.setEpName(sysUser.getAccount());
					entity.setEt(date);
				}
			}

		}
	}

	/**
	 * @Description 获取后台系统当前用户
	 * @param request
	 * @return
	 * @date 2017年6月29日
	 */
	public User getPlatformUser(HttpServletRequest request) {
		Object obj = request.getSession().getAttribute(GlobalConstant.SESSION_PLATFORM_USER_KEY);
		if (obj != null) {
			return (User) obj;
		} else {
			return null;
		}
	}
}

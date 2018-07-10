/**
 * <html>
 * <body>
 *  <P> Copyright 2017 阳光康众</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年7月10日</p>
 *  <p> Created by 于策/yu.ce@foxmail.com</p>
 *  </body>
 * </html>
 */
package com.sunshine.common.service;

import java.util.List;

import com.sunshine.common.vo.TreeNodeVo;

/**
 * @Project ChuFangLiuZhuan_PlatForm
 * @Package com.sunshine.common.service
 * @ClassName CommonService.java
 * @Description
 * @JDK version used 1.8
 * @Author 于策/yu.ce@foxmail.com
 * @Create Date 2017年7月10日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
public interface CommonService {
	/**
	 * @Description 父资源Id查找所有资源 其中带roleId已分配的资源checked信息
	 * @param roleId
	 * @return
	 * @date 2017年7月4日
	 */
	public List<TreeNodeVo> findResourceByRoleIdAndParentId(String roleId, String parentId);

	/**
	 * @Description 查找所有资源 其中带roleId已分配的资源checked信息
	 * @param roleId
	 * @return
	 * @date 2017年7月10日
	 */
	public List<TreeNodeVo> findResourceTreeVoByRoleId(String roleId);

	/**
	 * @Description 父部门Id查找所有部门 其中带userId已分配的资源checked信息
	 * @param userId
	 * @param parentId
	 * @return
	 * @date 2017年7月4日
	 */
	public List<TreeNodeVo> findDeptTreeByUserIdAndParentId(String userId, String parentId);

	/**
	 * @Description 查找所有部门 其中带userId已分配的资源checked信息
	 * @param userId
	 * @return
	 * @date 2017年7月10日
	 */
	public List<TreeNodeVo> findDeptTreeByUserId(String userId);

	/**
	 * @Description 查找所有菜单资源 其中带resourceId已分配的资源checked信息
	 * @return
	 */
	public List<TreeNodeVo> findMenuResourceTreeByResourceId(String resourceId);

}

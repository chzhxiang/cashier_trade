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
package com.sunshine.common.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sunshine.common.dao.CommonDao;
import com.sunshine.common.service.CommonService;
import com.sunshine.common.vo.TreeNodeVo;
import com.sunshine.framework.common.spring.ext.SpringContextHolder;

/**
 * @Project ChuFangLiuZhuan_PlatForm
 * @Package com.sunshine.common.service.impl
 * @ClassName CommonServiceImpl.java
 * @Description
 * @JDK version used 1.8
 * @Author 于策/yu.ce@foxmail.com
 * @Create Date 2017年7月10日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
@Service
public class CommonServiceImpl implements CommonService {
	private CommonDao commonDao = SpringContextHolder.getBean(CommonDao.class);

	@Override
	public List<TreeNodeVo> findResourceByRoleIdAndParentId(String roleId, String parentId) {
		// TODO Auto-generated method stub
		return commonDao.findResourceTreeByRoleIdAndParentId(parentId, roleId);
	}

	@Override
	public List<TreeNodeVo> findResourceTreeVoByRoleId(String roleId) {
		// TODO Auto-generated method stub
		return commonDao.findResourceTreeByRoleId(roleId);
	}

	@Override
	public List<TreeNodeVo> findDeptTreeByUserIdAndParentId(String userId, String parentId) {
		// TODO Auto-generated method stub
		return commonDao.findDeptTreeByUserIdAndParentId(userId, parentId);
	}

	@Override
	public List<TreeNodeVo> findDeptTreeByUserId(String userId) {
		// TODO Auto-generated method stub
		return commonDao.findDeptTreeByUserId(userId);
	}

    @Override
    public List<TreeNodeVo> findMenuResourceTreeByResourceId(String resourceId) {
	    // TODO Auto-generated method stub
	   return commonDao.findMenuResourceTreeByResourceId(resourceId);
    }



}

/**
 * <html>
 * <body>
 *  <P> Copyright 2017 阳光康众</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年3月10日</p>
 *  <p> Created by 于策/yu.ce@foxmail.com</p>
 *  </body>
 * </html>
 */
package com.sunshine.test.mongodb.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sunshine.framework.mvc.mongodb.dao.BaseMongoDao;
import com.sunshine.framework.mvc.mongodb.service.impl.BaseMongoServiceImpl;
import com.sunshine.test.mongodb.TestEntity;
import com.sunshine.test.mongodb.dao.TestEntityDao;
import com.sunshine.test.mongodb.service.TestEntityService;

/**
 * @Project ChuFangLiuZhuan_PlatForm
 * @Package com.sunshine.framework.mvc.mongodb.test.service.impl
 * @ClassName TestEntityServiceImpl.java
 * @Description
 * @JDK version used 1.8
 * @Author 于策/yu.ce@foxmail.com
 * @Create Date 2017年6月1日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
@Service(value = "testEntityService")
public class TestEntityServiceImpl extends BaseMongoServiceImpl<TestEntity, String> implements TestEntityService {
	/**
	 * 
	 */
	@Autowired
	private TestEntityDao testEntityDao;

	@Override
	protected BaseMongoDao<TestEntity, String> getDao() {
		// TODO Auto-generated method stub
		return testEntityDao;
	}
}

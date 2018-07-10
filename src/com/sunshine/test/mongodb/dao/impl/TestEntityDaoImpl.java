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
package com.sunshine.test.mongodb.dao.impl;

import org.springframework.stereotype.Repository;

import com.sunshine.framework.mvc.mongodb.dao.impl.BaseMongoDaoImpl;
import com.sunshine.test.mongodb.TestEntity;
import com.sunshine.test.mongodb.dao.TestEntityDao;

/**
 * @Project ChuFangLiuZhuan_PlatForm
 * @Package com.sunshine.framework.mvc.mongodb.test.dao.impl
 * @ClassName TestEntityDaoImpl.java
 * @Description
 * @JDK version used 1.8
 * @Author 于策/yu.ce@foxmail.com
 * @Create Date 2017年6月1日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
@Repository(value = "testEntityDao")
public class TestEntityDaoImpl extends BaseMongoDaoImpl<TestEntity, String> implements TestEntityDao {

}

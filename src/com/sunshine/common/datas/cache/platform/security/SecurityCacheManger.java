/**
 * <html>
 * <body>
 *  <P> Copyright 2017 阳光康众</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年7月1日</p>
 *  <p> Created by 于策/yu.ce@foxmail.com</p>
 *  </body>
 * </html>
 */
package com.sunshine.common.datas.cache.platform.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sunshine.common.GlobalConstant;
import com.sunshine.framework.cache.redis.RedisService;
import com.sunshine.framework.common.spring.ext.SpringContextHolder;

/**
 * @Project ChuFangLiuZhuan_PlatForm
 * @Package com.sunshine.cache.platform
 * @ClassName SecurityCacheManger.java
 * @Description
 * @JDK version used 1.8
 * @Author 于策/yu.ce@foxmail.com
 * @Create Date 2017年7月1日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
@Service
public class SecurityCacheManger {
	private static Logger logger = LoggerFactory.getLogger(SecurityCacheManger.class);
	private RedisService redisService = SpringContextHolder.getBean(RedisService.class);
	private static final String CHECK_INIT_CACHE_FILED = "security.init";

	public void initCache() {

	}

	/**
	 * @Description 缓存版本
	 * @return
	 * @date 2017年7月1日
	 */
	private String builCacheVersion() {
		return CHECK_INIT_CACHE_FILED.concat(GlobalConstant.getServerVersion());
	}

}

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
package com.sunshine.common.datas.cache;

import org.apache.commons.lang.StringUtils;

/**
 * @Project ChuFangLiuZhuan_PlatForm
 * @Package com.sunshine.cache
 * @ClassName CacheConstant.java
 * @Description
 * @JDK version used 1.8
 * @Author 于策/yu.ce@foxmail.com
 * @Create Date 2017年7月1日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
public class CacheConstants {
	/**
	 * 缓存key的分割符
	 */
	public static final String CACHE_KEY_SPLIT_CHAR = ":";

	/**
	 * key模糊匹配通配符
	 */
	public static final String CACHE_KEY_PATTERN_CHAR = "*";
	/**
	 * 缓存新增
	 */
	public static final String CACHE_OP_ADD = "cache_add";

	/**
	 * 缓存删除
	 */
	public static final String CACHE_OP_DEL = "cache_del";

	/**
	 * 缓存更新
	 */
	public static final String CACHE_OP_UPDATE = "cache_update";

	/**
	 * 标记为null的值
	 */
	public static final String CACHE_NULL_STRING = "null";

	/**
	 * redis 缓存key不存在返回的标志
	 */
	public static final String CACHE_KEY_NOT_EXIST = "nil";

	/**
	 * redis分布式锁 锁key的后缀
	 */
	public static final String REDIS_LOCK_KEY_PREFIX = "_lock";

	/**
	 * 一小时共计的秒数
	 */
	public static final int ONE_HOUR_SECONDS = 3600;
	/**
	 * 一天共计的秒数
	 */
	public static final int ONE_DAY_SECONDS = 86400;

	/**
	 * 7天共计的秒数
	 */
	public static final int SEVEN_DAY_SECONDS = 604800;

	/**
	 * 30天共计的秒数
	 */
	public static final int SEVEN_MONTH_SECONDS = 2592000;

	/**
	 * 3 * 60 * 1000 = 180000毫秒
	 */
	public static final int REDIS_LOCKED_DEF_TIME = 180000;

	/**
	 * 判断缓存是否有值
	 * 
	 * @param jsonData
	 * @return true 有值 <br>
	 *         false 没有值
	 */
	public static boolean isHadValue(String jsonData) {
		boolean isHadValue = false;
		if (StringUtils.isNotBlank(jsonData) && !CacheConstants.CACHE_KEY_NOT_EXIST.equalsIgnoreCase(jsonData)) {
			isHadValue = true;
		}
		return isHadValue;
	}

	public static final Integer HAD_INIT_YES = 1;
	public static final Integer HAD_INIT_NO = 0;
	public static final String DATA_CACHE_VERSION = "data.cache.version";

	/**
	 * 商户基本信息缓存前缀
	 */
	public static final String CACHE_MERCHANT_KEY_FREFIX = "trade.merchant";

	/**
	 * 应用支付渠道信息缓存前缀
	 */
	public static final String CACHE_APPLICATION_CHANNEL_KEY_FREFIX = "trade.application.channel";

	/**
	 * 商户应用信息缓存前缀
	 */
	public static final String CACHE_MERCHANTAPPLICATION_KEY_FREFIX = "trade.merchant.application";

	/**
	 * 订单缓存
	 */
	public static final String CACHE_TRADE_ORDER_KEY_FREFIX = "trade.order.cache";

	/**
	 * 支付异常订单缓存
	 */
	public static final String CACHE_PAYMENT_EXCEPTION_REFIX = "payment.exception.list:";

	/**
	 * 退费异常订单缓存
	 */
	public static final String CACHE_REFUND_EXCEPTION_REFIX = "refund.exception.list:";

	/**
	 * 支付超时异常订单缓存
	 */
	public static final String CACHE_PAYMENT_OVERTIME_EXCEPTION_REFIX = "payment.overtime.exception.list:";

	/**
	 * 银行卡信息缓存
	 */
	public static final String CACHE_BANKCARD_KEY_FREFIX = "bank.card.cache";

}

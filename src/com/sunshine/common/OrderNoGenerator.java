/**
 * <html>
 * <body>
 *  <P> Copyright 2017 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年9月14日</p>
 *  <p> Created by 党参</p>
 *  </body>
 * </html>
 */
package com.sunshine.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunshine.common.datas.cache.CacheConstants;
import com.sunshine.framework.cache.redis.RedisLock;
import com.sunshine.framework.cache.redis.RedisService;
import com.sunshine.framework.common.spring.ext.SpringContextHolder;
import com.sunshine.framework.config.SystemConfig;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.common
 * @ClassName: OrderNoGenerator
 * @Description: <p>交易订单编号生成器</p>
 * @JDK version used: 
 * @Author: 党参
 * @Create Date: 2017年9月14日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class OrderNoGenerator {
	private static Logger logger = LoggerFactory.getLogger(OrderNoGenerator.class);

	/**
	 * 业务编码:支付->1
	 */
	public static final int BIZ_CODE_PAY = 1;

	/**
	 * 业务编码:退费->2
	 */
	public static final int BIZ_CODE_REFUND = 2;

	/**
	 * 业务编码:绑卡->3
	 */
	public static final int BIZ_CODE_TIE_CARD = 3;

	/**
	 * 补位
	 */
	private static final String COVER_CHAR = "0";

	/**
	 * 服务器环境标识
	 */
	private static final String SERVER_ENVIRONMENT_IDENTIFIER = SystemConfig.getStringValue("server.environment.identifier");

	/**
	 * 服务器负载标识
	 */
	private static final String SERVER_LOAD_IDENTIFIER = SystemConfig.getStringValue("server.load.identifier");

	/**
	 * 时间格式化：YYYYMMDD
	 */
	public static DateFormat YYYYMMDD = new SimpleDateFormat("yyyyMMdd");
	/**
	 * 时间格式化：YYYYMMDDHHMMSS
	 */
	public static DateFormat YYYYMMDDHHMMSS = new SimpleDateFormat("yyyyMMddHHmmss");
	/**
	 * 时间格式化：YYYYMMDDHH
	 */
	public static DateFormat YYYYMMDDHH = new SimpleDateFormat("yyyyMMddHH");
	/**
	 * 订单日期标志位 判断流水号是否需要从1开始
	 */
	private static long orderDay = Long.valueOf(YYYYMMDD.format(new Date()));

	/**
	 * 订单流水号长度
	 */
	private static int ORDER_LEN = 7;

	/**
	 * 订单序列缓存
	 */
	private static final String ORDERNO_GEN_NUMBER_DATE = "trade.order.number.date";

	/**
	 * 订单锁
	 */
	private static final String ORDERNO_LOCKED_KEY = "trade.order.lock";

	/**
	 * 订单锁时长
	 */
	private static final long ORDERNO_LOCKED_TIME_OUT = 30000;

	/**
	 * 支付平台订单号流水缓存标识
	 */
	public static final String TRADE_COMMON_ORDER_SERIAL_NUMBER = "trade_common_order_serial_number";

	/**
	 * 支付平台医保订单号流水缓存标识
	 */
	public static final String TRADE_MEDICARE_ORDER_SERIAL_NUMBER = "trade_medicare_order_serial_number";

	/**
	 * 支付平台订单号流水缓存最大值
	 */
	private static final int TRADE_MAX_ORDER_SERIAL_NUMBER = 9999999;

	/**
	 * 生成规则(长度):服务器标识(2) + 业务编码(1) + 接入第三方平台(2) + 接入渠道(2) + 日期(8) + 流水号(7)
	 * 业务编码:支付：1;退费：2;绑卡：3;
	 * 接入第三方平台:微信：1;支付宝：2;银联：3;
	 * 接入渠道:微信JSPAI：1
	 * 日期：yyyyMMddhh 8位20170914
	 * @param bizCode 业务编码
	 * @param tradePlatform 接入第三方平台
	 * @param channel 接入渠道
	 * @return
	 */
	public static String genOrderNo(int bizCode, int tradePlatform, int channel) {
		if (bizCode != 0 && tradePlatform != 0 && channel != 0) {
			StringBuffer sb = new StringBuffer();
			//服务器环境标识
			sb.append(SERVER_ENVIRONMENT_IDENTIFIER);
			//服务器负载标识
			sb.append(SERVER_LOAD_IDENTIFIER);
			// 业务编码
			sb.append(bizCode);

			// 接入第三方平台
			if (tradePlatform < 10) {
				sb.append(COVER_CHAR + tradePlatform);
			} else {
				sb.append(tradePlatform);
			}

			// 接入第三方平台
			if (channel < 10) {
				sb.append(COVER_CHAR + channel);
			} else {
				sb.append(channel);
			}

			//日期
			sb.append(YYYYMMDD.format(new Date()));
			sb.append(getOrderBaseSerialNum(TRADE_COMMON_ORDER_SERIAL_NUMBER));
			return sb.toString();
		} else {
			return null;
		}
	}

	public static String getOrderBaseSerialNum(String orderCacheKey) {
		RedisService redisService = SpringContextHolder.getBean(RedisService.class);
		long nowDay = Long.valueOf(YYYYMMDD.format(new Date()));
		StringBuffer serialNumSb = new StringBuffer();
		if (nowDay > orderDay) {
			RedisLock redisLock = new RedisLock(redisService.getRedisPool());
			boolean isLock = false;
			String orderNoDate = null;
			try {
				do {
					isLock = redisLock.singleLock(ORDERNO_LOCKED_KEY, ORDERNO_LOCKED_TIME_OUT);
				} while (!isLock);
				if (nowDay > orderDay) {
					orderNoDate = redisService.get(ORDERNO_GEN_NUMBER_DATE);
					// 是否需要更新生产订单的日期和订单编号重新计数
					boolean isUpdateGenOrderConfig = false;
					if (StringUtils.isNotBlank(orderNoDate) && !CacheConstants.CACHE_KEY_NOT_EXIST.equalsIgnoreCase(orderNoDate)) {
						if (Long.valueOf(orderNoDate).longValue() < nowDay) {
							isUpdateGenOrderConfig = true;
						} else if (Long.valueOf(orderNoDate).longValue() == nowDay) {
							orderDay = nowDay;
						}
					} else {
						isUpdateGenOrderConfig = true;
					}

					if (isUpdateGenOrderConfig) {
						orderDay = nowDay;
						redisService.set(ORDERNO_GEN_NUMBER_DATE, nowDay);
						redisService.set(orderCacheKey, TRADE_MAX_ORDER_SERIAL_NUMBER);
					}
				}
			} finally {
				redisLock.singleUnlock(ORDERNO_LOCKED_KEY);
			}
		}

		// redis生产 订单流水号
		String serialNum = redisService.decr(orderCacheKey).toString();
		logger.info("订单流水号生成,time:{},redis decr:{}", YYYYMMDDHHMMSS.format(new Date()), serialNum);
		if (serialNum.length() < ORDER_LEN) {
			int length = serialNum.length();
			for (int i = 0; i < ORDER_LEN - length; i++) {
				serialNumSb.append(COVER_CHAR);
			}
		}
		serialNumSb.append(serialNum);
		return serialNumSb.toString();
	}
}

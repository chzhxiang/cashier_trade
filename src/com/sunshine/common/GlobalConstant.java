/**
 * <html>
 * <body>
 *  <P>  Copyright 2017 阳光康众</p>
 *  <p>  All rights reserved.</p>
 *  <p> Created on 2016-4-6</p>
 *  <p> Created by 于策</p>
 *  </body>
 * </html>
 */
package com.sunshine.common;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import com.sunshine.framework.config.SystemConfig;
import com.sunshine.framework.utils.DateUtils;

/**
 * @Package com.sunshine.common
 * @ClassName GlobalConstant
 * @Statement
 *            <p>
 *            全局常量
 *            </p>
 * @JDK version used: 1.7
 * @Author: 于策
 * @Create Date: 2016-4-6
 * @modify-Author:
 * @modify-Date:
 * @modify-Why/What:
 * @Version 1.0
 */
public class GlobalConstant {

	/**
	 * get请求标识字符
	 */
	public static final String METHOD_GET = "GET";

	/**
	 * post请求标识字符
	 */
	public static final String METHOD_POST = "POST";

	/**
	 * 文件上传路径
	 */
	public static final String UPLOAD_FILE_PATH = SystemConfig.getStringValue("upload_file_path");

	public final static String SESSION_PLATFORM_USER_ACCOUNT_KEY = "userAccount";

	/**
	 * 保存在session中的后台管理 用户登录信息键值常量
	 */
	public final static String SESSION_PLATFORM_USER_KEY = "platformUser";

	/**
	 * 保存在session中的APP管理 用户登录信息键值常量
	 */
	public final static String SESSION_MOBILE_USER_KEY = "mobileUser";

	/**
	 * 保存在session中的前端管理 用户登录信息键值常量
	 */
	public final static String SESSION_MOBILEAPP_USER_KEY = "mobileAppUser";

	/*** 启动版本定义参数 0开发 1测试 2正式 ***/
	public static String SYSTEM_PUBLISH_VERSION_RC = "2";
	public static String SYSTEM_PUBLISH_VERSION_TEST = "1";
	public static String SYSTEM_PUBLISH_VERSION_DEV = "0";

	/**
	 * ","字符串连接符
	 */
	public static final String STRING_SPLIT_CHAR = ",";

	/**
	 * "&"字符串连接符
	 */
	public static final String STRING_AND_CHAR = "&";

	/**
	 * "="字符串等号符
	 */
	public static final String STRING_ASSIGN_CHAR = "=";

	/**
	 * "?"字符串等号符
	 */
	public static final String STRING_QUESTION_MARK_CHAR = "?";

	/**
	 * 状态有效
	 */
	public static final int STATUS_VALID = 1;
	/**
	 * 状态无效
	 */
	public static final int STATUS_INVALID = 0;

	public static final int YES = 1;
	public static final int NO = 0;

	/**
	 * 返回前端页面常量标识：ID常量
	 */
	public static final String MOTHED_INVOKE_RES_ENTITY_ID = "entityId";

	/**
	 * 返回前端页面常量标识：实体常量
	 */
	public static final String MOTHED_INVOKE_RES_ENTITY = "entity";

	/**
	 * 返回前端页面常量标识：实体集合常量
	 */
	public static final String MOTHED_INVOKE_RES_ENTITIES = "entities";

	/**
	 * 返回前端页面常量标识：返回消息
	 */
	public static final String MOTHED_INVOKE_RES_MSG = "msg";

	/**
	 * 返回前端页面常量标识：状态判断
	 */
	public static final String MOTHED_INVOKE_RES_IS_SUCCESS = "isSuccess";

	/**
	 * 返回前端页面常量标识：map集合
	 */
	public static final String MOTHED_INVOKE_RES_MAP = "map";

	/**
	 * 返回前端页面常量标识：收银台订单ID
	 */
	public static final String MOTHED_INVOKE_RES_CASHIER_ID = "cashierId";

	/**
	 * 返回前端页面常量标识：收银台支付编码
	 */
	public static final String MOTHED_INVOKE_RES_CHANNEL_CODE = "channelCode";

	/**
	 * 第三方平台标识
	 */
	public static final String MOTHED_INVOKE_RES_PAY_IDENTITY = "payIdentity";

	/**
	 * 返回前端页面常量标识：收银台前端跳转地址
	 */
	public static final String MOTHED_INVOKE_RES_RETURN_URL = "returnUrl";
	/**
	 * 返回前端页面常量标识：收银台前端跳转地址
	 */
	public static final String MOTHED_INVOKE_RES_RETURN_TEXT = "returnText";

	/**
	 * 返回前端页面常量标识
	 */
	public static final String MOTHED_INVOKE_RES_APPLICATION_ID = "applicationId";

	/**
	 * 是否异常
	 */
	public static final String TRADE_IS_EXCEPTION = "isException";

	/**
	 * 是否成功
	 */
	public static final String TRADE_IS_SUCCESS = "isSuccess";

	/**
	 * 失败msg
	 */
	public static final String TRADE_FAIL_MSG = "failMsg";

	/**
	 * 返回的错误码
	 */
	public static final String TRADE_RESP_CODE = "respCode";

	/**
	 * 正常返回msg
	 */
	public static final String TRADE_RETURN_MSG = "returnMsg";

	/**
	 * 成功返回数据
	 */
	public static final String TRADE_SUCCESS_DATA = "data";

	/**
	 * 成功返回第三方支付订单号
	 */
	public static final String TRADE_SUCCESS_AGTPAYNO = "agtPayNo";

	/**
	 * 成功返回第三方退费订单号
	 */
	public static final String TRADE_SUCCESS_AGTREFUNDNO = "agtRefundNo";

	/**
	 * 银联支付系统追踪号
	 */
	public static final String TRADE_SUCCESS_PAY_TRACE_NO = "payTraceNo";

	/**
	 * 银联退费系统追踪号
	 */
	public static final String TRADE_SUCCESS_REFUND_TRACE_NO = "refundTraceNo";

	/**
	 * http请求返回成功状态
	 */
	public final static String HTTP_IS_SUCCESS = "200";

	/**
	 * 交易结果成功
	 */
	public static final String TRADE_SUCCESS = "SUCCESS";

	/**
	 * 订单状态
	 */
	public static final String TRADE_ORDER_STATE = "tradeState";

	/**
	 * 第三方订单号标识
	 
	public static final String TRADE_ORDER_NO = "tradeOrderNo";
	*/
	/**
	 * 第三方交易时间
	 */
	public static final String TRADE_DATE = "tradeDate";

	/**
	 * 订单json格式数据标识
	 */
	public static final String TRADE_SUCCESS_ORDER_JSON = "orderJson";

	/**
	 * 文件上传根目录 
	 */
	public static final String FILE_PATH = SystemConfig.getStringValue("file_path");

	/**
	 * 微信退费密钥文件上传路径
	 */
	public static final String WECHAT_SECRET = SystemConfig.getStringValue("wechat_secret");

	/**
	 * 银联密钥文件存放路劲
	 */
	public static final String UNIONPAY_SECRET = SystemConfig.getStringValue("unionpay_secret");

	/**
	 * 其他文件存放路劲
	 */
	public static final String OTHER_SECRET = SystemConfig.getStringValue("other_secret");

	/**
	 * 支付锁时长
	 */
	public static final long REDIS_PAY_LOCKED_TIME = 300000;

	/**
	 * 支付跳转地址0：平台地址
	 */
	public static final int PAY_LINK_PLATFORM = 0;

	/**
	 * 支付跳转地址1：商户地址
	 */
	public static final int PAY_LINK_MERCHANT = 1;

	/**
	 * 证书类型
	 */
	public static final String SIGNCERT_TYPE = "PKCS12";

	/**
	 * 小额快捷支付生成订单号
	 */
	public static final String UNIONPAY_SMALL_QUICK_TRADENO = "smallQuickTradeNo";

	/**
	 * 小额快捷支付银行卡号
	 */
	public static final String UNIONPAY_CARDID = "cardId";

	/**
	 * 登录次数记录
	 */
	public final static String SESSION_LOGIN_TIMES = "loginTimes";

	/**
	 * 判断是否是测试环境
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年10月20日下午2:20:39
	 */
	public static boolean isPayTestSign() {
		boolean flag = false;
		String publish_version = SystemConfig.getStringValue("system.publish.version");
		if (StringUtils.isNotBlank(publish_version) && SYSTEM_PUBLISH_VERSION_RC.equalsIgnoreCase(publish_version)) {
			flag = false;
		} else {
			flag = true;
		}
		return flag;
	}

	/**
	 * @Description 获取服务端版本号
	 * @return
	 * @date 2017年7月3日
	 */
	public static String getServerVersion() {
		String server_version = null;
		String publish_version = SystemConfig.getStringValue("system.publish.version");
		if (StringUtils.isNotBlank(publish_version) && SYSTEM_PUBLISH_VERSION_RC.equalsIgnoreCase(publish_version)) {
			server_version = SystemConfig.getStringValue("server.version", DateUtils.getCurrentDate());
		} else {
			server_version = DateUtils.getCurrentTime();
		}
		return server_version;
	}

	/**
	 * @Description 当前时间的 yyyy-MM-dd HH:mm:ss
	 * @return
	 * @date 2017年8月30日
	 */
	public static String getNowYYYYMMDDHHMMSS() {
		return formatYYYYMMDDHHMMSS(new Date());
	}

	/**
	 * 中文日期 yyyy-MM-dd E
	 */
	public static String formatYYYYMMDDE(Date date) {
		FastDateFormat faseDF = FastDateFormat.getInstance("yyyy-MM-dd E", Locale.CHINESE);
		return faseDF.format(date);
	}

	/**
	 * 中文日期 yyyy-MM-dd E
	 */
	public static Date parseYYYYMMDDE(String dateStr) throws ParseException {
		FastDateFormat faseDF = FastDateFormat.getInstance("yyyy-MM-dd E", Locale.CHINESE);
		return faseDF.parse(dateStr);
	}

	/**
	 * yyyy-MM-dd
	 */
	public static String formatYYYYMMDD(Date date) {
		FastDateFormat faseDF = FastDateFormat.getInstance("yyyy-MM-dd");
		return faseDF.format(date);
	}

	/**
	 * yyyyMMdd
	 */
	public static String formatyyyymmdd(Date date) {
		FastDateFormat faseDF = FastDateFormat.getInstance("yyyyMMdd");
		return faseDF.format(date);
	}

	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	public static String formatYYYYMMDDHHMMSS(Date date) {
		FastDateFormat faseDF = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
		return faseDF.format(date);
	}

	/**
	 * yyyyMMddHHmmss
	 */
	public static String formatyyyymmddhhmmss(Date date) {
		FastDateFormat faseDF = FastDateFormat.getInstance("yyyyMMddHHmmss");
		return faseDF.format(date);
	}

	/**
	 * yyyy-MM-dd HH:mm
	 */
	public static String formatYYYYMMDDHHMM(Date date) {
		FastDateFormat faseDF = FastDateFormat.getInstance("yyyy-MM-dd HH:mm");
		return faseDF.format(date);
	}

	/**
	 * yyyyMMddHHmm
	 */
	public static String formatyyyymmddhhmm(Date date) {
		FastDateFormat faseDF = FastDateFormat.getInstance("yyyyMMddHHmm");
		return faseDF.format(date);
	}

	/**
	 * HH:mm
	 */
	public static String formatHHMM(Date date) {
		FastDateFormat faseDF = FastDateFormat.getInstance("HH:mm");
		return faseDF.format(date);
	}

	/**
	 * HHmm
	 */
	public static String formathhmm(Date date) {
		FastDateFormat faseDF = FastDateFormat.getInstance("HHmm");
		return faseDF.format(date);
	}

	/**
	 * MMddHHmmss
	 */
	public static String formatmmddhhmmss(Date date) {
		FastDateFormat faseDF = FastDateFormat.getInstance("MMddHHmmss");
		return faseDF.format(date);
	}

	/**
	 * yyyy-MM-dd
	 */
	public static Date parseYYYYMMDD(String dateStr) throws ParseException {
		FastDateFormat faseDF = FastDateFormat.getInstance("yyyy-MM-dd");
		return faseDF.parse(dateStr);
	}

	/**
	 * yyyyMMdd
	 */
	public static Date parseyyyymmdd(String dateStr) throws ParseException {
		FastDateFormat faseDF = FastDateFormat.getInstance("yyyyMMdd");
		return faseDF.parse(dateStr);
	}

	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	public static Date parseYYYYMMDDHHMMSS(String dateStr) throws ParseException {
		FastDateFormat faseDF = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
		return faseDF.parse(dateStr);
	}

	/**
	 * yyyy-MM-dd HH:mm
	 */
	public static Date parseYYYYMMDDHHMM(String dateStr) throws ParseException {
		FastDateFormat faseDF = FastDateFormat.getInstance("yyyy-MM-dd HH:mm");
		return faseDF.parse(dateStr);
	}

	/**
	 * yyyyMMddHHmm
	 */
	public static Date parseyyyymmddhhmm(String dateStr) throws ParseException {
		FastDateFormat faseDF = FastDateFormat.getInstance("yyyyMMddHHmm");
		return faseDF.parse(dateStr);
	}

	/**
	 * yyyyMMddHHmmss
	 */
	public static Date parseyyyymmddhhmmss(String dateStr) throws ParseException {
		FastDateFormat faseDF = FastDateFormat.getInstance("yyyyMMddHHmmss");
		return faseDF.parse(dateStr);
	}

	/**
	 * HH:mm
	 */
	public static Date parseHHMM(String dateStr) throws ParseException {
		FastDateFormat faseDF = FastDateFormat.getInstance("HH:mm");
		return faseDF.parse(dateStr);
	}

	/**
	 * HHmm
	 */
	public static Date parsehhmm(String dateStr) throws ParseException {
		FastDateFormat faseDF = FastDateFormat.getInstance("HHmm");
		return faseDF.parse(dateStr);
	}

	/**
	 * MMddHHmmss
	 */
	public static Date parsemmddhhmmss(String dateStr) throws ParseException {
		FastDateFormat faseDF = FastDateFormat.getInstance("MMddHHmmss");
		return faseDF.parse(dateStr);
	}

	/**
	 * 支付服务url上下文
	 */
	public static String getContextTradeServer() {
		if (null == CONTEXT_TRADE_SERVER) {
			CONTEXT_TRADE_SERVER = SystemConfig.getStringValue("context.trade_server");
		}
		return CONTEXT_TRADE_SERVER;
	}

	private static String CONTEXT_TRADE_SERVER = null;

}

/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年1月2日</p>
 *  <p> Created by 申姜</p>
 *  </body>
 * </html>
 */
package com.sunshine.common.vo;

/**
 * 统一的响应状态码
 * @Project: cashier_desk 
 * @Package: com.sunshine.framework.vo
 * @ClassName: ResponseCode
 * @Description: <p></p>
 * @JDK version used: 
 * @Author: 申姜
 * @Create Date: 2017年1月2日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class ResponseCode {
	/**
	 * 成功标示
	 */
	public static final String SUCCESS = "0";
	/**
	 * 失败标示
	 */
	public static final String FAILURE = "-1";
	/**
	 * 网络异常
	 */
	public static final String NETWORK_ANOMALY = "-2";
	/**
	 * 参数错误
	 */
	public static final String PARAMS_ERROR = "1";
	/**
	 * 未查询到记录
	 */
	public static final String NO_DATA = "2";

}

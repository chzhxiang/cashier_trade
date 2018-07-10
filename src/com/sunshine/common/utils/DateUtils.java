/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年11月27日下午4:56:16</p>
 *  <p> Created by 熊胆</p>
 *  </body>
 * </html>
 */
package com.sunshine.common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @Project: cashier_trade
 * @Package: com.sunshine.common.utils
 * @ClassName: DateUtils
 * @Description: 日期工具类
 * @JDK version used: 
 * @Author: 熊胆
 * @Create Date: 2017年11月27日下午4:56:16
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class DateUtils {

	/**
	 * 获取单月最后一天和第一天
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年11月24日上午9:35:21
	 */
	public static Map<String, String> getCurMonthFirstAndLastDay() {
		Map<String, String> map = new HashMap<>();
		Calendar cale = Calendar.getInstance();
		// 获取当月第一天和最后一天  
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		// 获取当月的第一天  
		cale.add(Calendar.MONTH, 0);
		cale.set(Calendar.DAY_OF_MONTH, 1);
		String firstday = format.format(cale.getTime());
		map.put("firstday", firstday);
		// 获取当月的最后一天  
		cale.add(Calendar.MONTH, 1);
		cale.set(Calendar.DAY_OF_MONTH, 0);
		String lastday = format.format(cale.getTime());
		map.put("lastday", lastday);
		return map;

	}

}

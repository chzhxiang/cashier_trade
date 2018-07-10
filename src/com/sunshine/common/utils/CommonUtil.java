/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年11月27日上午11:15:32</p>
 *  <p> Created by 熊胆</p>
 *  </body>
 * </html>
 */
package com.sunshine.common.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @Project: cashier_trade
 * @Package: com.sunshine.common.utils
 * @ClassName: CommonUtil
 * @Description: <p></p>
 * @JDK version used: 
 * @Author: 熊胆
 * @Create Date: 2017年11月27日上午11:15:32
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class CommonUtil {

	/** 
	* [身份证号] 前六位，后四位，其他用星号隐藏每位1个星号<例子:6222600**********1234> 
	*  
	* @param cardNum 
	* @return 
	*/
	public static String idCardNumFront(String cardNum) {
		if (StringUtils.isBlank(cardNum)) {
			return "";
		}
		return StringUtils.left(cardNum, 6)
				.concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(cardNum, 4), StringUtils.length(cardNum), "*"), "******"));
	}

	public static String formatMedicalCard(String medicalCard) {
		return medicalCard.substring(0, 4) + "*****" + medicalCard.substring(medicalCard.length() - 4, medicalCard.length());
	}

}

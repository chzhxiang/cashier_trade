/**
 * <html>
 * <body>
 *  <P> Copyright 2017 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年9月8日</p>
 *  <p> Created by 党参</p>
 *  </body>
 * </html>
 */
package com.sunshine.restful.trade.service;

import com.sunshine.restful.RestResponse;
import com.sunshine.restful.dto.request.RequestPay;
import com.sunshine.restful.dto.request.RequestQuery;
import com.sunshine.restful.dto.request.RequestRefund;

/**
 * @Project: cashier_desk
 * @Package: com.sunshine.restful.pay.service
 * @ClassName: PayService
 * @Description:
 *               <p>
 *               </p>
 * @JDK version used:
 * @Author: 党参
 * @Create Date: 2017年9月8日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public interface PayRestService {

	/**
	 * 订单支付
	 * 
	 * @param payParamsVo
	 * @return
	 */
	RestResponse pay(RequestPay payParamsVo);

	/**
	 * 订单退费
	 * 
	 * @param payParamsVo
	 * @return
	 */
	RestResponse refund(RequestRefund refundParamsVo);

	/**
	 * 支付订单查询
	 * 
	 * @param query
	 * @return
	 */
	RestResponse payQuery(RequestQuery query);

	/**
	 * 退费订单查询
	 * 
	 * @param query
	 * @return
	 */
	RestResponse refundQuery(RequestQuery query);

	/**
	 * 小额快捷支付 
	 * @param payParamsVo
	 * @return
	 */
	RestResponse limitPay(RequestPay payParamsVo);

	/**
	 * 获取聚合支付二维码
	 */
	RestResponse payQRCode(RequestPay payParamsVo);
}

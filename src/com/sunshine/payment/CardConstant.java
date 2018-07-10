/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年10月27日下午5:08:37</p>
 *  <p> Created by 熊胆</p>
 *  </body>
 * </html>
 */
package com.sunshine.payment;

/**
 * @Project: cashier_trade
 * @Package: com.sunshine.payment
 * @ClassName: CardConstant
 * @Description: 绑卡参数常量
 * @JDK version used: 
 * @Author: 熊胆
 * @Create Date: 2017年10月27日下午5:08:37
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class CardConstant {

	/**
	 * 证件类型：二代身份证
	 */
	public final static String BANK_CARD_CERTIFICATETYPE_IDCARD = "01";

	/**
	 * 证件类型：军官证
	 */
	public final static String BANK_CARD_CERTIFICATETYPE_OFFICERS = "02";

	/**
	 * 证件类型：护照
	 */
	public final static String BANK_CARD_CERTIFICATETYPE_PASSPORT = "03";

	/**
	 * 证件类型：回乡证
	 */
	public final static String BANK_CARD_CERTIFICATETYPE_HOME = "04";

	/**
	 * 证件类型：台胞证
	 */
	public final static String BANK_CARD_CERTIFICATETYPE_MTP = "05";

	/**
	 * 证件类型：警官证
	 */
	public final static String BANK_CARD_CERTIFICATETYPE_POLICE = "06";

	/**
	 * 证件类型：士兵证
	 */
	public final static String BANK_CARD_CERTIFICATETYPE_SOLDIER = "07";

	/**
	 * 证件类型：其它证
	 */
	public final static String BANK_CARD_CERTIFICATETYPE_OTHER = "08";

	/**
	 * 借记卡
	 */
	public final static Integer BANK_CARDTYPE_DEBIT = 1;

	/**
	 * 贷记卡
	 */
	public final static Integer BANK_CARDTYPE_CREDIT = 2;

	/**
	 * 准贷记卡
	 */
	public final static Integer BANK_CARDTYPE_SEMICREDIT = 3;

	/**
	 * 社保卡
	 */
	public final static Integer BANK_CARDTYPE_MEDICAL = 4;

	/**
	 * 开通状态：使用中
	 */
	public final static int ACTIVATE_STATUS_BUNDLING = 1;
	public final static String ACTIVATE_STATUS_BUNDLING_REMARK = "使用中";

	/**
	 * 开通状态：余额不足
	 */
	public final static int ACTIVATE_STATUS_BALANCE_NOTENOUGH = 2;
	public final static String ACTIVATE_STATUS_BALANCE_NOTENOUGH_REMARK = "余额不足";

	/**
	 * 开通状态：支付受限
	 */
	public final static int ACTIVATE_STATUS_PAYMENT_CONSTRAINT = 3;
	public final static String ACTIVATE_STATUS_PAYMENT_CONSTRAINT_REMARK = "支付受限";

	/**
	 * 开通状态：停止使用
	 */
	public final static int ACTIVATE_STATUS_UNBUNDLING = 4;
	public final static String ACTIVATE_STATUS_UNBUNDLING_REMARK = "停止使用";

	/**
	 * 开通状态：待开通
	 */
	public final static int ACTIVATE_STATUS_NOBUNDLING = 5;
	public final static String ACTIVATE_STATUS_NOBUNDLING_REMARK = "待开通";

	/**
	 * 开通状态：已删除
	 */
	public final static int ACTIVATE_STATUS_DEL = 6;
	public final static String ACTIVATE_STATUS_DEL_REMARK = "已删除";

	/**
	 * 医保卡绑卡类型: 线上
	 */
	public final static Integer MEDICARD_TYPE_ONLINE = 1;

	/**
	 * 医保卡绑卡类型: 线下
	 */
	public final static Integer MEDICARD_TYPE_LINE = 2;

	/**
	 * 绑卡参数MAP实体
	 */
	public static final String TRADE_PARAMS_MAP_BIND_CARD = "bindCardMap";

	/**
	 * 银联请求参数
	 */
	public final static String UNION_BINDCARD_REQUEST_URL = "unionBindCardRequestUrl";

}

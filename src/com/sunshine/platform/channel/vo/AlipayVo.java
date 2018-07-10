/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年9月26日</p>
 *  <p> Created by hqy</p>
 *  </body>
 * </html>
 */
package com.sunshine.platform.channel.vo;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.platform.channel.vo
 * @ClassName: AlipayAppVo
 * @Description: <p>支付宝相关支付渠道参数映射实体类</p>
 * @JDK version used: 
 * @Author: 百部
 * @Create Date: 2017年9月26日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class AlipayVo {
	/**
	 * 合作者ID
	 */
	private String mchId;

	/**
	 * 卖家账号
	 */
	private String mchAccount;

	/**
	 * 支付公钥
	 */
	private String payPublicKey;

	/**
	 * 支付私钥
	 */
	private String payPrivateKey;

	/**
	 * 支付密钥
	 */
	private String payKey;

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public String getMchAccount() {
		return mchAccount;
	}

	public void setMchAccount(String mchAccount) {
		this.mchAccount = mchAccount;
	}

	public String getPayPublicKey() {
		return payPublicKey;
	}

	public void setPayPublicKey(String payPublicKey) {
		this.payPublicKey = payPublicKey;
	}

	public String getPayPrivateKey() {
		return payPrivateKey;
	}

	public void setPayPrivateKey(String payPrivateKey) {
		this.payPrivateKey = payPrivateKey;
	}

	public String getPayKey() {
		return payKey;
	}

	public void setPayKey(String payKey) {
		this.payKey = payKey;
	}
}

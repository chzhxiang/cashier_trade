/**
* <html>
*  <body>
*   <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
*   <p> All rights reserved.</p>
*   <p> Created on 2016年12月31日</p>
*   <p> Created by 姓名</p>
*  </body>
* </html>
*/
package com.sunshine.platform.merchant.entity;

import com.sunshine.framework.mvc.mysql.entity.BaseSQLEntity;

/**
 * 
 * @Project: cashier_trade 
 * @Package: com.sunshine.platform.merchant.entity
 * @ClassName: Merchant
 * @Description: <p></p>
 * @JDK version used: 
 * @Author: 赤芍
 * @Create Date: 2017年9月12日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class Merchant extends BaseSQLEntity {

	private static final long serialVersionUID = -6320814192557638179L;

	/**
	 * 商户号
	 */
	private String merchantNo;

	/**
	 * 商户名称
	 */
	private String merchantName;

	/**
	 * 商户联系地址
	 */
	private String merchantAddress;

	/**
	 * 联系人姓名
	 */
	private String linkmanName;

	/**
	 * 联系人电话
	 */
	private String mobile;

	/**
	 * 联系人QQ
	 */
	private String qq;

	/**
	 * 联系人email
	 */
	private String email;

	/**
	 * 商户状态:1.启用,0.停用
	 */
	private Integer status;

	/**
	 * @return the merchantNo
	 */
	public String getMerchantNo() {
		return merchantNo;
	}

	/**
	 * @param merchantNo the merchantNo to set
	 */
	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	/**
	 * @return the merchantName
	 */
	public String getMerchantName() {
		return merchantName;
	}

	/**
	 * @param merchantName the merchantName to set
	 */
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	/**
	 * @return the merchantAddress
	 */
	public String getMerchantAddress() {
		return merchantAddress;
	}

	/**
	 * @param merchantAddress the merchantAddress to set
	 */
	public void setMerchantAddress(String merchantAddress) {
		this.merchantAddress = merchantAddress;
	}

	/**
	 * @return the linkmanName
	 */
	public String getLinkmanName() {
		return linkmanName;
	}

	/**
	 * @param linkmanName the linkmanName to set
	 */
	public void setLinkmanName(String linkmanName) {
		this.linkmanName = linkmanName;
	}

	/**
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * @param mobile the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * @return the qq
	 */
	public String getQq() {
		return qq;
	}

	/**
	 * @param qq the qq to set
	 */
	public void setQq(String qq) {
		this.qq = qq;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

}
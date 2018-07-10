package com.sunshine.platform.security.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.sunshine.common.GlobalConstant;
import com.sunshine.framework.mvc.mysql.entity.BaseSQLEntity;

/**
 * @Project Prescription_Clinic
 * @Package com.sunshine.platform.security.entity
 * @ClassName User.java
 * @Description 用户帐号信息
 * @JDK version used 1.8
 * @Author 于策/yu.ce@foxmail.com
 * @Create Date 2017年8月26日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
public class User extends BaseSQLEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 帐号
	 */
	protected String account;

	/**
	 * 姓名
	 */
	protected String name;

	/**
	 * 手机号码
	 */
	protected String mobile;

	/**
	 * 身份证号
	 */
	protected String idCardNo;

	/**
	 * 出生年月
	 */
	protected Date birthDay;

	/**
	 * 1 男 0 女
	 */
	protected Integer sex;

	/**
	 * 职称
	 */
	protected String title;

	/**
	 * 医生介绍
	 */
	protected String introduction;

	/**
	 * 密码
	 */
	protected String password;

	/**
	 * 备注
	 */
	protected String remark;

	/**
	 * 状态
	 */
	protected Integer status;

	/**
	 * 角色 数据库存储为角色列表的Json格式
	 */
	protected String roleIds;
	/**
	 * 角色 角色列表的字符串格式逗号隔开
	 */
	private String roleIdStr;

	protected List<String> roleIdList;

	/**
	 * 原密码
	 */
	protected String oldPassword;

	/**
	 * 新密码
	 */
	protected String newPassword;

	/**
	 * 当值诊所Id
	 */
	protected String curClinicId;

	/**
	 * 当值诊所名称
	 */
	protected String curClinicName;

	/**
	 * 数据库存储为已关联诊所ID列表的Json格式
	 */
	protected String clinicIds;

	/**
	 * 已关联的诊所名称
	 */
	protected String clinicNames;

	/**
	 * 账号类型，1平台、2商户
	 */
	private Integer type;

	/**
	 * 所属商户(ID)
	 */
	private String ownedMerchant;

	/**
	 * 所属商户(NAME)
	 */
	private String ownedMerchantName;

	/**
	 * 所属应用(ID)
	 */
	private String ownedApplication;

	/**
	 * 所属商户(NAME)
	 */
	private String ownedApplicationName;

	/**
	 * @return the oldPassword
	 */
	public String getOldPassword() {
		return oldPassword;
	}

	/**
	 * @param oldPassword
	 *            the oldPassword to set
	 */
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	/**
	 * @return the newPassword
	 */
	public String getNewPassword() {
		return newPassword;
	}

	/**
	 * @param newPassword
	 *            the newPassword to set
	 */
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	/**
	 * @return the confirmNewPassword
	 */
	public String getConfirmNewPassword() {
		return confirmNewPassword;
	}

	/**
	 * @param confirmNewPassword
	 *            the confirmNewPassword to set
	 */
	public void setConfirmNewPassword(String confirmNewPassword) {
		this.confirmNewPassword = confirmNewPassword;
	}

	/**
	 * 确认新密码
	 */
	private String confirmNewPassword;

	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<String> getRoleIdList() {
		if (roleIdList == null) {
			if (StringUtils.isNotBlank(roleIds)) {
				roleIdList = JSON.parseArray(roleIds, String.class);
			} else {
				roleIdList = new ArrayList<>();
			}
		}
		return roleIdList;
	}

	public void setRoleIdList(List<String> roleIdList) {
		this.roleIdList = roleIdList;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCurClinicId() {
		return curClinicId;
	}

	public void setCurClinicId(String curClinicId) {
		this.curClinicId = curClinicId;
	}

	public String getCurClinicName() {
		return curClinicName;
	}

	public void setCurClinicName(String curClinicName) {
		this.curClinicName = curClinicName;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public Date getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	/**
	 * @return the clinicIds
	 */
	public String getClinicIds() {
		return clinicIds;
	}

	/**
	 * @return the clinicNames
	 */
	public String getClinicNames() {
		return clinicNames;
	}

	/**
	 * @param clinicIds the clinicIds to set
	 */
	public void setClinicIds(String clinicIds) {
		this.clinicIds = clinicIds;
	}

	/**
	 * @param clinicNames the clinicNames to set
	 */
	public void setClinicNames(String clinicNames) {
		this.clinicNames = clinicNames;
	}

	public String getRoleIdStr() {
		if (!CollectionUtils.isEmpty(getRoleIdList())) {
			StringBuilder sb = new StringBuilder();
			for (String roleId : getRoleIdList()) {
				if (sb.length() > 0) {
					sb.append(GlobalConstant.STRING_SPLIT_CHAR);
				}
				sb.append(roleId);
			}
			roleIdStr = sb.toString();
		}
		return roleIdStr;
	}

	public void setRoleIdStr(String roleIdStr) {
		this.roleIdStr = roleIdStr;
	}

	/** 
	* 获取账号类型，1平台、2商户 
	* @return type
	*/
	public Integer getType() {
		return type;
	}

	/** 
	* 设置账号类型，1平台、2商户 
	* @param type
	*/
	public void setType(Integer type) {
		this.type = type;
	}

	/** 
	* 获取所属商户(ID) 
	* @return ownedMerchant
	*/
	public String getOwnedMerchant() {
		return ownedMerchant;
	}

	/** 
	* 设置所属商户(ID) 
	* @param ownedMerchant
	*/
	public void setOwnedMerchant(String ownedMerchant) {
		this.ownedMerchant = ownedMerchant;
	}

	/** 
	* 获取所属商户(NAME) 
	* @return ownedMerchantName
	*/
	public String getOwnedMerchantName() {
		return ownedMerchantName;
	}

	/** 
	* 设置所属商户(NAME) 
	* @param ownedMerchantName
	*/
	public void setOwnedMerchantName(String ownedMerchantName) {
		this.ownedMerchantName = ownedMerchantName;
	}

	/** 
	* 获取所属应用(ID) 
	* @return ownedApplication
	*/
	public String getOwnedApplication() {
		return ownedApplication;
	}

	/** 
	* 设置所属应用(ID) 
	* @param ownedApplication
	*/
	public void setOwnedApplication(String ownedApplication) {
		this.ownedApplication = ownedApplication;
	}

	/** 
	* 获取所属商户(NAME) 
	* @return ownedApplicationName
	*/
	public String getOwnedApplicationName() {
		return ownedApplicationName;
	}

	/** 
	* 设置所属商户(NAME) 
	* @param ownedApplicationName
	*/
	public void setOwnedApplicationName(String ownedApplicationName) {
		this.ownedApplicationName = ownedApplicationName;
	}

}
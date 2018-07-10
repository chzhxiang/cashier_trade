package com.sunshine.platform.security.entity;

import com.sunshine.framework.mvc.mysql.entity.BaseSQLEntity;

public class RoleResource extends BaseSQLEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6082935648752950096L;

	private String roleId;

	private String resourceId;

	private Integer status;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId == null ? null : roleId.trim();
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId == null ? null : resourceId.trim();
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
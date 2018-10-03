package com.muiboot.shiro.system.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "M_USER_ROLE")
public class UserRole implements Serializable{
	
	private static final long serialVersionUID = -3166012934498268403L;

	@Column(name = "USER_ID")
	private Long userId;

	@Column(name = "ROLE_ID")
	private Long roleId;

	public UserRole(Long userId,Long roleId){
		this.userId=userId;
		this.roleId=roleId;
	}
	public UserRole(){
	}
	/**
	 * @return USER_ID
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * @return ROLE_ID
	 */
	public Long getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId
	 */
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
}
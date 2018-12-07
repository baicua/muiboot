package com.muiboot.shiro.system.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.muiboot.core.common.annotation.ExportConfig;
import com.muiboot.core.common.domain.BaseModel;

@Table(name = "M_USER")
public class User extends BaseModel implements Serializable{

	private static final long serialVersionUID = -4852732617765810959L;
	/**
	 * 账户状态
	 */
	public static final String STATUS_VALID = "1";

	public static final String STATUS_LOCK = "0";

	public static final String DEFAULT_THEME = "green";

	public static final String DEFAULT_AVATAR = "default.jpg";

	public static final String SUPPER_USER = "admin";
	/**
	 * 性别
	 */
	public static final String SEX_MALE = "1";

	public static final String SEX_FEMALE = "2";

	public static final String SEX_UNKNOW = "0";

	@Id
	@GeneratedValue(generator = "JDBC")
	@Column(name = "USER_ID")
	private Long userId;

	/*登录名*/
	@Column(name = "USERNAME")
	@ExportConfig(value = "登录名")
	private String username;

	/*用户名*/
	@Column(name = "REAL_NAME")
	@ExportConfig(value = "用户名")
	private String realName;

	@Column(name = "PASSWORD")
	private String password;

	@Column(name = "GROUP_ID")
	private Long groupId;

	@Transient
	@ExportConfig(value = "部门")
	private String groupName;

	@Transient
	private Long organId;

	@Column(name = "EMAIL")
	@ExportConfig(value = "邮箱")
	private String email;

	@Column(name = "MOBILE")
	@ExportConfig(value = "手机")
	private String mobile;

	@Column(name = "STATUS")
	@ExportConfig(value = "状态", convert = "s:0=禁用,1=启用")
	private String status;

	@Column(name = "CRATE_TIME")
	@ExportConfig(value = "创建时间", convert = "c:com.muiboot.shiro.common.util.poi.convert.TimeConvert")
	private Date crateTime;

	@Column(name = "MODIFY_TIME")
	private Date modifyTime;

	@Column(name = "LAST_LOGIN_TIME")
	private Date lastLoginTime;

	@Column(name = "SSEX")
	@ExportConfig(value = "性别", convert = "s:1=男,2=女")
	private String ssex;

	@Column(name = "THEME")
	private String theme;

	@Column(name = "AVATAR")
	private String avatar;

	@Column(name = "DESCRIPTION")
	private String description;

	@Transient
	private String roleName;

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
	 * @return USERNAME
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username == null ? null : username.trim();
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	/**
	 * @return PASSWORD
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password == null ? null : password.trim();
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Long getOrganId() {
		return organId;
	}

	public void setOrganId(Long organId) {
		this.organId = organId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * @return EMAIL
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email == null ? null : email.trim();
	}

	/**
	 * @return MOBILE
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * @param mobile
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile == null ? null : mobile.trim();
	}

	/**
	 * @return STATUS
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status == null ? null : status.trim();
	}

	/**
	 * @return CRATE_TIME
	 */
	public Date getCrateTime() {
		return crateTime;
	}

	/**
	 * @param crateTime
	 */
	public void setCrateTime(Date crateTime) {
		this.crateTime = crateTime;
	}

	/**
	 * @return MODIFY_TIME
	 */
	public Date getModifyTime() {
		return modifyTime;
	}

	/**
	 * @param modifyTime
	 */
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	/**
	 * @return LAST_LOGIN_TIME
	 */
	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	/**
	 * @param lastLoginTime
	 */
	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	/**
	 * @return SSEX
	 */
	public String getSsex() {
		return ssex;
	}

	/**
	 * @param ssex
	 */
	public void setSsex(String ssex) {
		this.ssex = ssex == null ? null : ssex.trim();
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/**
	 * 实体主键
	 */
	@Override
	public Object getPrimaryKey() {
		return userId;
	}
}
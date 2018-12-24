package com.muiboot.shiro.system.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.muiboot.core.annotation.ExportConfig;
import com.muiboot.core.entity.BaseModel;

@Table(name = "M_ROLE")
public class Role extends BaseModel implements Serializable {

	private static final long serialVersionUID = -1714476694755654924L;

	@Id
	@GeneratedValue(generator = "JDBC")
	@Column(name = "ROLE_ID")
	private Long roleId;

	@Column(name = "ROLE_NAME")
	@ExportConfig(value = "角色名称")
	private String roleName;

	@Column(name = "ROLE_KEY")
	@ExportConfig(value = "角色编号")
	private String roleKey;

	@Column(name = "ROLE_LEVEL")
	@ExportConfig(value = "角色级别",convert = "s:0=全局,1=本局")
	private Integer roleLevel=0;//0:全局角色，1机关角色

	@Column(name = "GROUP_ID")
	@ExportConfig(value = "所属机关ID")
	private Long groupId=0L;//机关ID

	@Column(name = "REMARK")
	@ExportConfig(value = "描述")
	private String remark;

	@Column(name = "CREATE_TIME")
	@ExportConfig(value = "创建时间", convert = "c:com.muiboot.shiro.common.util.poi.convert.TimeConvert")
	private Date createTime;

	@Column(name = "MODIFY_TIME")
	private Date modifyTime;

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

	/**
	 * @return ROLE_NAME
	 */
	public String getRoleName() {
		return roleName;
	}

	/**
	 * @param roleName
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName == null ? null : roleName.trim();
	}

	public String getRoleKey() {
		return roleKey;
	}

	public void setRoleKey(String roleKey) {
		this.roleKey = roleKey;
	}

	/**
	 * @return REMARK
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark
	 */
	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

	public Integer getRoleLevel() {
		return roleLevel;
	}

	public void setRoleLevel(Integer roleLevel) {
		this.roleLevel = roleLevel;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	/**
	 * @return CREATE_TIME
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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
	 * The identify of the object
	 */
	@Override
	public Object getPrimaryKey() {
		return roleId;
	}
}
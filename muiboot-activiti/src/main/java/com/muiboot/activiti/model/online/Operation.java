/*
 * Copyright (c) 2017 上海中昊软件开发有限公司. All Rights Reserved.
 */

package com.muiboot.activiti.model.online;

/**
 * <p>Title: </p>
 * <p>Description: 流程操作类</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: 中昊</p>
 *
 * @author jin
 * @version 1.0 2017年6月29日
 */
public class Operation {
	/**任务编号*/
	private String taskId;
	/**业务编号*/
	private Long businessId;
	/**操作编号*/
	private String operaId;
	/**操作意见*/
	private String opinion;
	/**下环节操作机关-监察机构*/
	private String organId;
	/**下环节操作部门-部门,分所*/
	private String deptId;
	/**下环节操作人,逗号分隔*/
	private String nextOperaUsers;

	/**扩展变量*/
	private String param;
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public Long getBusinessId() {
		return businessId;
	}
	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}
	public String getOperaId() {
		return operaId;
	}
	public void setOperaId(String operaId) {
		this.operaId = operaId;
	}
	public String getOpinion() {
		return opinion;
	}
	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}
	public String getOrganId() {
		return organId;
	}
	public void setOrganId(String organId) {
		this.organId = organId;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getNextOperaUsers() {
		return nextOperaUsers;
	}
	public void setNextOperaUsers(String nextOperaUsers) {
		this.nextOperaUsers = nextOperaUsers;
	}


	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}
}

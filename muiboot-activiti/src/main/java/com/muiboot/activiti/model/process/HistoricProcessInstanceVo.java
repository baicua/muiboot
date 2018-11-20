package com.muiboot.activiti.model.process;

import java.util.Date;

/**
 * 历史流程
* @ClassName: HistoricProcessInstanceVo 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author cpy
* @date 2016年11月18日 下午2:54:32 
*
 */
public class HistoricProcessInstanceVo {
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private String processDefinitionId;
	
	private Date startTime;
	
	private Date endTime;
	
	private String deleteReason;	
	
	public HistoricProcessInstanceVo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public HistoricProcessInstanceVo(String id, String processDefinitionId, Date startTime, Date endTime, String deleteReason) {
		super();
		this.id = id;
		this.processDefinitionId = processDefinitionId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.deleteReason = deleteReason;
	}



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getDeleteReason() {
		return deleteReason;
	}

	public void setDeleteReason(String deleteReason) {
		this.deleteReason = deleteReason;
	}

}

package com.muiboot.activiti.model.process;

import com.muiboot.activiti.model.online.TaskVo;

/**
 * 流程实例
* @ClassName: ProcessInstanceVo 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author cpy
* @date 2016年11月18日 下午2:55:05 
*
 */
public class ProcessInstanceVo {
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private String processInstanceId;
	
	private String processDefinitionId;
	
	private TaskVo task;

	public ProcessInstanceVo() {
		super();
	}

	public ProcessInstanceVo(String id, String processInstanceId, String processDefinitionId) {
		super();
		this.id = id;
		this.processInstanceId = processInstanceId;
		this.processDefinitionId = processDefinitionId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public TaskVo getTask() {
		return task;
	}

	public void setTask(TaskVo task) {
		this.task = task;
	}

}

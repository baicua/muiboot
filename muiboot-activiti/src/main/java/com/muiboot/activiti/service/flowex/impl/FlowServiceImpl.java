package com.muiboot.activiti.service.flowex.impl;

import com.muiboot.activiti.service.flowex.IFlowService;
import org.activiti.engine.*;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FlowServiceImpl implements IFlowService {
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private HistoryService historyService;

	@Override
	public String startFlow(String flowId, String businessKey,String userId) {
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("startUser", userId);
		identityService.setAuthenticatedUserId(userId);
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(flowId, businessKey, variables);
		String pId = processInstance.getId();
		return pId;
		
	}

	@Override
	public String startFlow(String flowId, String businessKey, Map<String, Object> variables,String userId) {
		identityService.setAuthenticatedUserId(userId);
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(flowId, businessKey, variables);
		String pId = processInstance.getId();
		return pId;
	}

	@Override
	public void completeTask(Task task, Map<String, Object> variables, String memo) {
		// 认领任务
		String currentUserId = "";
		taskService.claim(task.getId(), currentUserId);
		identityService.setAuthenticatedUserId(currentUserId);
		if (StringUtils.isNotBlank(memo)) {
			taskService.addComment(task.getId(), task.getProcessInstanceId(), memo);
		}
		// 完成任务
		taskService.complete(task.getId(), variables);
	}

	/**
	 * 完成任务
	 *
	 * @param taskId    任务编号
	 * @param variables 绑定变量
	 * @param pid       流程实例编号
	 * @param memo      操作备注
	 * @return void
	 * @Title:completeTask
	 * @date 2017年5月12日
	 */
	@Override
	public void completeTask(String taskId, Map<String, Object> variables, String pid, String memo) {

	}

	@Override
	public Task getTask(String pId) {
		return taskService.createTaskQuery().processInstanceId(pId).singleResult();
	}

	@Override
	public Task getTaskById(String taskId) {
		if (StringUtils.isBlank(taskId)||"null".equals(taskId))return null;
		return taskService.createTaskQuery().taskId(taskId).singleResult();
	}

	@Override
	public void completeTask(String taskId, Map<String, Object> variables) {
		// 认领任务
		String currentUserId = "";
		taskService.claim(taskId, currentUserId);
		identityService.setAuthenticatedUserId(currentUserId);
		// 完成任务
		taskService.complete(taskId, variables);
	}
	@Override
	public void completeTask(String taskId, Map<String, Object> variables, String memo) {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		if (null == task) {
			return;
		}
		completeTask(taskId, variables, task.getProcessInstanceId(), memo);
	}
}

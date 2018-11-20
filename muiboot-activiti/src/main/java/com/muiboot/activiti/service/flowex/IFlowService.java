package com.muiboot.activiti.service.flowex;

import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.task.Task;

import java.util.List;
import java.util.Map;

public interface IFlowService {
	/**
	 * 
	* @Title 启动流程
	* @param flowId      流程编号
	* @param businessKey 业务主键
	* @param variables 绑定变量
	* @return    taskid 流程任务id
	* @return String    实例编号 
	* @Description 
	* 流程启动人为当前操作人，并且流程变量是startUser
	 */

	String startFlow(String flowId, String businessKey, Map<String, Object> variables,String userId);
	/**
	 * 
	* @Title 完成任务 
	* @param  taskId 任务id
	* @param  variables 参数
	* @param @return    设定文件
	* @return String    返回类型 
	* @Description: 
	* 完成任务，variables里指定任务接收人
	 */
	void completeTask(String taskId, Map<String, Object> variables);
	/**
	 * 通过流程实例获取运行的任务
	* @Title: getTask 
	* @param @param pId 流程实例编号
	* @param @return    设定文件 
	* @return Task    返回类型 
	* @throws
	 */
	Task getTask(String pId);

	Task getTaskById(String taskId);
	/**
	 * 完成任务
	* @Title:completeTask
	* @param  task 任务实体
	* @param  variables 绑定变量
	* @param  memo 操作备注
	* @return void
	* @date 2017年5月11日
	*/
	void completeTask(Task task, Map<String, Object> variables, String memo);
	/**
	 * 完成任务
	* @Title:completeTask
	* @param  taskId 任务编号
	* @param  variables 绑定变量
	* @param  pid 流程实例编号
	* @param  memo 操作备注
	* @return void
	* @date 2017年5月12日
	 */
	void completeTask(String taskId, Map<String, Object> variables, String pid, String memo);
	/**
	 * 完成任务
	* @Title:completeTask
	* @param  taskId 任务编号
	* @param  variables 绑定变量
	* @param  memo 操作备注
	* @return void
	* @date 2017年5月12日
	 */
	void completeTask(String taskId, Map<String, Object> variables, String memo);
	/**
	 * 开启流程，返回流程实例ID
	* @Title:startFlow
	* @param  flowId 流程编号
	* @param  businessKey 业务表主键
	* @param @return
	* @return String
	* @date 2017年5月12日
	*/
	String startFlow(String flowId, String businessKey,String userId);
}

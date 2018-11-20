package com.muiboot.activiti.service.instance.impl;

import com.muiboot.activiti.model.online.TaskVo;
import com.muiboot.activiti.model.process.ProcessInstanceVo;
import com.muiboot.activiti.service.instance.InstanceService;
import com.muiboot.core.common.domain.QueryRequest;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InstanceServiceImpl implements InstanceService {
  @Autowired
  private RuntimeService runtimeService;
  @Autowired
  private TaskService taskService;
  
  @Override
  public List<ProcessInstanceVo> findByPage(QueryRequest request){
      ProcessInstanceQuery query=runtimeService.createProcessInstanceQuery();
      
      List<ProcessInstance> list =new ArrayList<ProcessInstance>();
      List<ProcessInstanceVo> vos =new ArrayList<ProcessInstanceVo>();
      list = query.orderByProcessInstanceId().desc().listPage((request.getPage() -1)* request.getLimit(), request.getLimit());
      for(ProcessInstance t:list){
        ProcessInstanceVo vo=new ProcessInstanceVo(t.getId(),t.getProcessInstanceId()
            ,t.getProcessDefinitionId());   
        
        // 设置当前任务信息
        Task task=taskService.createTaskQuery().processInstanceId(t.getProcessInstanceId()).active().orderByTaskCreateTime().desc().singleResult();
        if(task != null){
        TaskVo taskVo=new TaskVo(task.getId(),task.getTaskDefinitionKey(),task.getName(),task.getProcessDefinitionId()
            ,task.getProcessInstanceId(),task.getPriority(),task.getCreateTime(),task.getDueDate()
            ,task.getDescription(),task.getOwner(),task.getAssignee());   
        vo.setTask(taskVo); 
        vos.add(vo);     
        }
      }
      return vos;
  }

}

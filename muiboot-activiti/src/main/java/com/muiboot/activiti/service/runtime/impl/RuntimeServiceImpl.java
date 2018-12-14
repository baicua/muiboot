package com.muiboot.activiti.service.runtime.impl;

import com.muiboot.activiti.active.param.operation.CompleteParam;
import com.muiboot.activiti.active.param.operation.StartParam;
import com.muiboot.activiti.active.param.query.BusinessParam;
import com.muiboot.activiti.dao.BusinessTaskMapper;
import com.muiboot.activiti.entity.RuTask;
import com.muiboot.activiti.service.runtime.RuntimeService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>Description: </p>
 *
 * @version 1.0 2018/11/29
 */
@Service
@Transactional
public class RuntimeServiceImpl implements RuntimeService {
    @Autowired
    private org.activiti.engine.RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private IdentityService identityService;
    @Override
    public ProcessInstance start(StartParam param) {
        param.notNull();
        identityService.setAuthenticatedUserId(param.getUser().getUserId());
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
                param.getFlowKey(),param.getBusinessKey(),param.getVariable());
        identityService.setAuthenticatedUserId(param.getUser().getUserId());
        taskService.addComment(null, processInstance.getProcessInstanceId(),"OPINION", param.getOpinion());
        return processInstance;
    }

    @Override
    public void claim(CompleteParam param) {
        param.notNull();
        identityService.setAuthenticatedUserId(param.getUser().getUserId());
        taskService.claim(param.getTaskId(), param.getUser().getUserId());
    }

    @Override
    public void resolveTask(CompleteParam param) {
        param.notNull();
        identityService.setAuthenticatedUserId(param.getUser().getUserId());
        taskService.resolveTask(param.getTaskId(),param.getVariable());
        this.addComment(param);
    }

    @Override
    public void delegateTask(CompleteParam param) {
        param.notNull();
        identityService.setAuthenticatedUserId(param.getUser().getUserId());
        taskService.delegateTask(param.getTaskId(),param.getUser().getUserId());
        this.addComment(param);
    }

    @Override
    public void complete(CompleteParam param) {
        param.notNull();
        identityService.setAuthenticatedUserId(param.getUser().getUserId());
        taskService.complete(param.getTaskId(),param.getVariable());
        this.addComment(param);
    }

    public List<RuTask> getBusinessTasks(BusinessParam param, BusinessTaskMapper mapper){
        return mapper.getBusinessTasks(param);
    }

    private void addComment(CompleteParam param) {
        taskService.addComment(param.getTaskId(), param.getProcessDefinitionId(),"OPINION", param.getOpinion());
    }
}

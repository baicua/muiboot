package com.muiboot.activiti.active.runtime.service;

import com.muiboot.activiti.active.operation.arg.param.CompleteParam;
import com.muiboot.activiti.active.operation.arg.param.StartParam;
import com.muiboot.activiti.active.runtime.declar.FlowDeclar;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>Description: </p>
 *
 * @version 1.0 2018/11/29
 */
@Service
@Transactional
public class FlowService implements FlowDeclar {
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private IdentityService identityService;

    @Override
    public ProcessInstance start(StartParam param) {
        param.notNull();
        identityService.setAuthenticatedUserId(param.getUser().getUserId());
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
                param.getFlowId(),param.getBusinessKey(),param.getVariable().toMap());
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
        taskService.resolveTask(param.getTaskId(),param.getVariable().toMap());
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
        taskService.complete(param.getTaskId(),param.getVariable().toMap());
        this.addComment(param);
    }

    private void addComment(CompleteParam param) {
        this.addComment(param,null);
    }

    private void addComment(CompleteParam param,String processInstanceId) {
        if (StringUtils.isNotBlank(param.getOpinion())){
            if (StringUtils.isNotBlank(processInstanceId)){
                taskService.addComment(null, processInstanceId,"MEMO", param.getOpinion());
            }else {
                if (StringUtils.isNotBlank(param.getTaskId())){
                    taskService.addComment(param.getTaskId(), null,"OPINION", param.getOpinion());
                }
            }
        }
    }

}

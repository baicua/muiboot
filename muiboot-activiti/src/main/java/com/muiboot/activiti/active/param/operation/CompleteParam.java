package com.muiboot.activiti.active.param.operation;

import com.muiboot.activiti.active.group.User;
import com.muiboot.activiti.active.validation.Assert;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * <p>Description: </p>
 *
 * @version 1.0 2018/11/29
 */
public  class CompleteParam extends Param implements Assert {
    protected String processDefinitionId;

    protected String taskId;

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public void notNull(String msg) {
        if (StringUtils.isBlank(taskId)){
            throw new IllegalArgumentException(msg);
        }
    }

    public void notNull() {
        if (StringUtils.isBlank(taskId)||user==null){
            throw new IllegalArgumentException("[Assertion failed] -  taskId and user must be not null");
        }
        if (variable==null){
            throw new IllegalArgumentException("[Assertion failed] -  variable must be not null");
        }
    }
}

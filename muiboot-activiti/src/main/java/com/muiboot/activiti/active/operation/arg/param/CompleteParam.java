package com.muiboot.activiti.active.operation.arg.param;

import com.muiboot.activiti.active.group.User;
import com.muiboot.activiti.active.operation.arg.Variable;
import com.muiboot.activiti.active.validation.Assert;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>Description: </p>
 *
 * @version 1.0 2018/11/29
 */
public class CompleteParam<T extends Variable> implements Assert {
    protected String taskId;

    protected T variable;

    protected User user;

    protected String opinion;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public T getVariable() {
        return variable;
    }

    public void setVariable(T variable) {
        this.variable = variable;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    @Override
    public void notNull(String msg) {
        if (StringUtils.isBlank(taskId)){
            throw new IllegalArgumentException(msg);
        }
        variable.notNull(msg);
    }

    public void notNull() {
        if (StringUtils.isBlank(taskId)||user==null){
            throw new IllegalArgumentException("[Assertion failed] -  taskId and user must be not null");
        }
        if (variable==null){
            throw new IllegalArgumentException("[Assertion failed] -  variable must be not null");
        }
        variable.notNull("[Assertion failed] -  variable must be not null");
    }
}

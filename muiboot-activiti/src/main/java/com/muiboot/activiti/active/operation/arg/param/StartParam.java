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
public class StartParam<T extends Variable> implements Assert{
    protected String flowId;
    protected String businessKey;
    protected T variable;
    protected User user;
    protected String opinion;
    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
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
        if (StringUtils.isBlank(flowId)||StringUtils.isBlank(businessKey)||null==user){
            throw new IllegalArgumentException(msg);
        }
        variable.notNull(msg);
    }

    public void notNull() {
        if (StringUtils.isBlank(flowId)||StringUtils.isBlank(businessKey)||null==user){
            throw new IllegalArgumentException("[Assertion failed] -  flowId and businessKey and user must be not null");
        }
        if (variable==null){
            throw new IllegalArgumentException("[Assertion failed] -  variable must be not null");
        }
        variable.notNull("[Assertion failed] -  variable must be not null");
    }
}

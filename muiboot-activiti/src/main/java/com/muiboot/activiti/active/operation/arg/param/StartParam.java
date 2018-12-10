package com.muiboot.activiti.active.operation.arg.param;

import com.muiboot.activiti.active.group.User;
import com.muiboot.activiti.active.validation.Assert;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Description: </p>
 *
 * @version 1.0 2018/11/29
 */
public class StartParam implements Assert{
    protected String flowKey;
    protected String businessKey;
    protected Map variable;
    protected User user;
    protected String opinion="";
    public String getFlowKey() {
        return flowKey;
    }

    public void setFlowKey(String flowKey) {
        this.flowKey = flowKey;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public Map getVariable() {
        return variable;
    }

    public void setVariable(Map variable) {
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
        if (StringUtils.isBlank(flowKey)||StringUtils.isBlank(businessKey)||null==user){
            throw new IllegalArgumentException(msg);
        }
    }

    public void notNull() {
        if (StringUtils.isBlank(flowKey)||StringUtils.isBlank(businessKey)||null==user){
            throw new IllegalArgumentException("[Assertion failed] -  flowKey and businessKey and user must be not null");
        }
    }
}

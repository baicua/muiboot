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
public class StartParam extends Param implements Assert{
    protected String flowKey;
    protected String businessKey;

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

    @Override
    public void notNull(String msg) {
        if (StringUtils.isBlank(flowKey)||StringUtils.isBlank(businessKey)||null==user){
            throw new IllegalArgumentException(msg);
        }
    }

    public void notNull() {
        if (StringUtils.isBlank(flowKey)||StringUtils.isBlank(businessKey)||null==user||StringUtils.isBlank(user.getUserId())){
            throw new IllegalArgumentException("[Assertion failed] -  flowKey and businessKey and user must be not null");
        }
    }
}

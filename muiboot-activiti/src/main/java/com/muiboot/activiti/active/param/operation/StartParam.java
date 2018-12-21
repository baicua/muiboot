package com.muiboot.activiti.active.param.operation;

import com.muiboot.activiti.active.validation.Assert;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>Description: </p>
 *
 * @version 1.0 2018/11/29
 */
public class StartParam extends Param implements Assert{
    protected String processDefinitionKey;
    protected String businessKey;

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    @Override
    public void notNull(String msg) {
        if (StringUtils.isBlank(processDefinitionKey)||StringUtils.isBlank(businessKey)||null==user){
            throw new IllegalArgumentException(msg);
        }
    }

    public void notNull() {
        if (StringUtils.isBlank(processDefinitionKey)||StringUtils.isBlank(businessKey)||null==user||StringUtils.isBlank(user.getUserId())){
            throw new IllegalArgumentException("[Assertion failed] -  processDefinitionKey and businessKey and user must be not null");
        }
    }
}

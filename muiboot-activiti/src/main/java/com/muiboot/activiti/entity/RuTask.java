package com.muiboot.activiti.entity;

import java.util.Date;

/**
 * Created by 75631 on 2018/12/15.
 */
public interface RuTask {
    /**
     * @return ID_
     */
    public String getId();
    /**
     * @return REV_
     */
    public Integer getRev();
    /**
     * @return EXECUTION_ID_
     */
    public String getExecutionId();

    /**
     * @return PROC_INST_ID_
     */
    public String getProcInstId();
    /**
     * @return PROC_DEF_ID_
     */
    public String getProcDefId();
    /**
     * @return NAME_
     */
    public String getName();
    /**
     * @return PARENT_TASK_ID_
     */
    public String getParentTaskId();
    /**
     * @return DESCRIPTION_
     */
    public String getDescription();

    /**
     * @return TASK_DEF_KEY_
     */
    public String getTaskDefKey();
    /**
     * @return OWNER_
     */
    public String getOwner();
    /**
     * @return ASSIGNEE_
     */
    public String getAssignee();
    /**
     * @return DELEGATION_
     */
    public String getDelegation();
    /**
     * @return PRIORITY_
     */
    public Integer getPriority();
    /**
     * @return CREATE_TIME_
     */
    public Date getCreateTime();
    /**
     * @return DUE_DATE_
     */
    public Date getDueDate();
    /**
     * @return CATEGORY_
     */
    public String getCategory();
    /**
     * @return SUSPENSION_STATE_
     */
    public Integer getSuspensionState();
    /**
     * @return TENANT_ID_
     */
    public String getTenantId();
    /**
     * @return FORM_KEY_
     */
    public String getFormKey();
}

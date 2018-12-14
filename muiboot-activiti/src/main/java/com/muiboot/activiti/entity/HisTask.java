package com.muiboot.activiti.entity;

import java.util.Date;

/**
 * Created by 75631 on 2018/12/15.
 */
public interface HisTask {
    /**
     * @return ID_
     */
    public String getId();
    /**
     * @return PROC_DEF_ID_
     */
    public String getProcDefId();
    /**
     * @return TASK_DEF_KEY_
     */
    public String getTaskDefKey();
    /**
     * @return PROC_INST_ID_
     */
    public String getProcInstId();
    /**
     * @return EXECUTION_ID_
     */
    public String getExecutionId();
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
     * @return OWNER_
     */
    public String getOwner();
    /**
     * @return ASSIGNEE_
     */
    public String getAssignee();
    /**
     * @return START_TIME_
     */
    public Date getStartTime();
    /**
     * @return CLAIM_TIME_
     */
    public Date getClaimTime();
    /**
     * @return END_TIME_
     */
    public Date getEndTime();
    /**
     * @return DURATION_
     */
    public Long getDuration();
    /**
     * @return DELETE_REASON_
     */
    public String getDeleteReason();
    /**
     * @return PRIORITY_
     */
    public Integer getPriority();
    /**
     * @return DUE_DATE_
     */
    public Date getDueDate();
    /**
     * @return FORM_KEY_
     */
    public String getFormKey();
    /**
     * @return CATEGORY_
     */
    public String getCategory();
    /**
     * @return TENANT_ID_
     */
    public String getTenantId();
}

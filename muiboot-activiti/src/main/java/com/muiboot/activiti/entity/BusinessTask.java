package com.muiboot.activiti.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>Description: </p>
 *
 * @version 1.0 2018/12/10
 */
public class BusinessTask implements RuTask,HisTask{
    @Id
    @Column(name = "ID_")
    private String id;

    @Column(name = "REV_")
    private Integer rev;

    @Column(name = "EXECUTION_ID_")
    private String executionId;

    @Column(name = "PROC_INST_ID_")
    private String procInstId;

    @Column(name = "PROC_DEF_ID_")
    private String procDefId;

    @Column(name = "NAME_")
    private String name;

    @Column(name = "PARENT_TASK_ID_")
    private String parentTaskId;

    @Column(name = "DESCRIPTION_")
    private String description;

    @Column(name = "TASK_DEF_KEY_")
    private String taskDefKey;

    @Column(name = "OWNER_")
    private String owner;

    @Column(name = "ASSIGNEE_")
    private String assignee;

    @Column(name = "DELEGATION_")
    private String delegation;

    @Column(name = "PRIORITY_")
    private Integer priority;

    @Column(name = "CREATE_TIME_")
    private Date createTime;

    @Column(name = "DUE_DATE_")
    private Date dueDate;

    @Column(name = "CATEGORY_")
    private String category;

    @Column(name = "SUSPENSION_STATE_")
    private Integer suspensionState;

    @Column(name = "TENANT_ID_")
    private String tenantId;

    @Column(name = "FORM_KEY_")
    private String formKey;

    //-------------HisTask--------------
    @Column(name = "START_TIME_")
    private Date startTime;

    @Column(name = "CLAIM_TIME_")
    private Date claimTime;

    @Column(name = "END_TIME_")
    private Date endTime;

    @Column(name = "DURATION_")
    private Long duration;

    @Column(name = "DELETE_REASON_")
    private String deleteReason;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Integer getRev() {
        return rev;
    }

    public void setRev(Integer rev) {
        this.rev = rev;
    }

    @Override
    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    @Override
    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    @Override
    public String getProcDefId() {
        return procDefId;
    }

    public void setProcDefId(String procDefId) {
        this.procDefId = procDefId;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getParentTaskId() {
        return parentTaskId;
    }

    public void setParentTaskId(String parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    @Override
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    @Override
    public String getDelegation() {
        return delegation;
    }

    public void setDelegation(String delegation) {
        this.delegation = delegation;
    }

    @Override
    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public Integer getSuspensionState() {
        return suspensionState;
    }

    public void setSuspensionState(Integer suspensionState) {
        this.suspensionState = suspensionState;
    }

    @Override
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String getFormKey() {
        return formKey;
    }

    public void setFormKey(String formKey) {
        this.formKey = formKey;
    }

    @Override
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Override
    public Date getClaimTime() {
        return claimTime;
    }

    public void setClaimTime(Date claimTime) {
        this.claimTime = claimTime;
    }

    @Override
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    @Override
    public String getDeleteReason() {
        return deleteReason;
    }

    public void setDeleteReason(String deleteReason) {
        this.deleteReason = deleteReason;
    }
}

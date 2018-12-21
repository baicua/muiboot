package com.muiboot.activiti.entity;

import com.muiboot.activiti.active.group.User;
import org.activiti.engine.delegate.DelegateTask;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "ACT_RU_SHIP")
public class RuShip {
    @Id
    @Column(name = "ID")
    private String id;

    /**
     * 机关编号
     */
    @Column(name = "ORGAN_ID")
    private String organId;

    /**
     * 部门编号
     */
    @Column(name = "DEPT_ID")
    private String deptId;

    /**
     * 任务关联类型
     */
    @Column(name = "CATEGORY")
    private String category;

    public RuShip(){}
    public RuShip(DelegateTask delegateTask, User user){
        this.id=delegateTask.getId();
        this.organId=user.getOrganId();
        this.deptId=user.getDeptId();
        this.category=delegateTask.getCategory();
    }

    /**
     * @return ID
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 获取机关编号
     *
     * @return ORGAN_ID - 机关编号
     */
    public String getOrganId() {
        return organId;
    }

    /**
     * 设置机关编号
     *
     * @param organId 机关编号
     */
    public void setOrganId(String organId) {
        this.organId = organId == null ? null : organId.trim();
    }

    /**
     * 获取部门编号
     *
     * @return DEPT_ID - 部门编号
     */
    public String getDeptId() {
        return deptId;
    }

    /**
     * 设置部门编号
     *
     * @param deptId 部门编号
     */
    public void setDeptId(String deptId) {
        this.deptId = deptId == null ? null : deptId.trim();
    }

    /**
     * 获取任务关联类型
     *
     * @return CATEGORY - 任务关联类型
     */
    public String getCategory() {
        return category;
    }

    /**
     * 设置任务关联类型
     *
     * @param category 任务关联类型
     */
    public void setCategory(String category) {
        this.category = category == null ? null : category.trim();
    }
}
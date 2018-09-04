package com.muiboot.shiro.system.domain;

import java.util.Date;
import javax.persistence.*;

@Table(name = "T_DEPT")
public class Dept {
    /**
     * 部门ID
     */
    @Id
    @Column(name = "DEPT_ID")
    private Long deptId;

    /**
     * 上级部门ID
     */
    @Column(name = "PARENT_ID")
    private Long parentId;

    /**
     * 部门名称
     */
    @Column(name = "DEPT_NAME")
    private String deptName;

    /**
     * 排序
     */
    @Column(name = "ORDER_NUM")
    private Long orderNum;

    /**
     * 创建时间
     */
    @Column(name = "CREATE_TIME")
    private Date createTime;

    /**
     * 部门级别
     */
    @Column(name = "DEPT_LEVEL")
    private int deptLevel;

    /**
     * 部门全称
     */
    @Column(name = "DEPT_FULL_NAME")
    private String deptFullName;

    /**
     * 部门描述
     */
    @Column(name = "DESCRIPTION")
    private String description;

    /**
     * 有效性，有效：禁用否，无效：禁用是
     */
    @Column(name = "VALID")
    private String valid;

    /**
     * 电话号码
     */
    @Column(name = "TEL")
    private String tel;

    /**
     * 电子邮箱
     */
    @Column(name = "EMAIL")
    private String email;

    /**
     * 行政区划
     */
    @Column(name = "AREA_CODE")
    private String areaCode;

    /**
     * 获取部门ID
     *
     * @return DEPT_ID - 部门ID
     */
    public Long getDeptId() {
        return deptId;
    }

    /**
     * 设置部门ID
     *
     * @param deptId 部门ID
     */
    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    /**
     * 获取上级部门ID
     *
     * @return PARENT_ID - 上级部门ID
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * 设置上级部门ID
     *
     * @param parentId 上级部门ID
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取部门名称
     *
     * @return DEPT_NAME - 部门名称
     */
    public String getDeptName() {
        return deptName;
    }

    /**
     * 设置部门名称
     *
     * @param deptName 部门名称
     */
    public void setDeptName(String deptName) {
        this.deptName = deptName == null ? null : deptName.trim();
    }

    /**
     * 获取排序
     *
     * @return ORDER_NUM - 排序
     */
    public Long getOrderNum() {
        return orderNum;
    }

    /**
     * 设置排序
     *
     * @param orderNum 排序
     */
    public void setOrderNum(Long orderNum) {
        this.orderNum = orderNum;
    }

    /**
     * 获取创建时间
     *
     * @return CREATE_TIME - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getDeptLevel() {
        return deptLevel;
    }

    public void setDeptLevel(int deptLevel) {
        this.deptLevel = deptLevel;
    }

    /**
     * 获取部门全称
     *
     * @return DEPT_FULL_NAME - 部门全称
     */
    public String getDeptFullName() {
        return deptFullName;
    }

    /**
     * 设置部门全称
     *
     * @param deptFullName 部门全称
     */
    public void setDeptFullName(String deptFullName) {
        this.deptFullName = deptFullName == null ? null : deptFullName.trim();
    }

    /**
     * 获取部门描述
     *
     * @return DESCRIPTION - 部门描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置部门描述
     *
     * @param description 部门描述
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    /**
     * 获取有效性，有效：禁用否，无效：禁用是
     *
     * @return VALID - 有效性，有效：禁用否，无效：禁用是
     */
    public String getValid() {
        return valid;
    }

    /**
     * 设置有效性，有效：禁用否，无效：禁用是
     *
     * @param valid 有效性，有效：禁用否，无效：禁用是
     */
    public void setValid(String valid) {
        this.valid = valid == null ? null : valid.trim();
    }

    /**
     * 获取电话号码
     *
     * @return TEL - 电话号码
     */
    public String getTel() {
        return tel;
    }

    /**
     * 设置电话号码
     *
     * @param tel 电话号码
     */
    public void setTel(String tel) {
        this.tel = tel == null ? null : tel.trim();
    }

    /**
     * 获取电子邮箱
     *
     * @return EMAIL - 电子邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置电子邮箱
     *
     * @param email 电子邮箱
     */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    /**
     * 获取行政区划
     *
     * @return AREA_CODE - 行政区划
     */
    public String getAreaCode() {
        return areaCode;
    }

    /**
     * 设置行政区划
     *
     * @param areaCode 行政区划
     */
    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode == null ? null : areaCode.trim();
    }
}
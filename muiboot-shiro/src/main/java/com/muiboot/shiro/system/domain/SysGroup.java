package com.muiboot.shiro.system.domain;

import com.muiboot.core.common.annotation.ExportConfig;
import com.muiboot.core.common.domain.BaseModel;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "M_GROUP")
public class SysGroup extends BaseModel implements Serializable {
    /**
     * 机构ID
     */
    @Id
    @Column(name = "GROUP_ID")
    @ExportConfig(value = "机构编号")
    private Long groupId;

    /**
     * 上级机构ID
     */
    @Column(name = "PARENT_ID")
    @ExportConfig(value = "上级机构")
    private Long parentId;

    /**
     * 机构名称
     */
    @Column(name = "GROUP_NAME")
    @ExportConfig(value = "机构名称")
    private String groupName;

    /**
     * 排序
     */
    @Column(name = "ORDER_NUM")
    private Long orderNum;

    /**
     * 创建时间
     */
    @Column(name = "CREATE_TIME")
    @ExportConfig(value = "创建时间",convert = "c:com.muiboot.shiro.common.util.poi.convert.TimeConvert")
    private Date createTime;

    /**
     * 机构类型
     */
    @Column(name = "GROUP_TYPE")
    @ExportConfig(value = "机构类型", convert = "s:1=机关,2=部门")
    private String groupType;

    /**
     * 机构全称
     */
    @Column(name = "GROUP_FULL_NAME")
    @ExportConfig(value = "机构全称")
    private String groupFullName;

    /**
     * 部门描述
     */
    @Column(name = "DESCRIPTION")
    @ExportConfig(value = "机构描述")
    private String description;

    /**
     * 有效性，有效：禁用否，无效：禁用是
     */
    @Column(name = "VALID")
    @ExportConfig(value = "机构状态", convert = "s:0=禁用,1=启用")
    private String valid;

    /**
     * 电话号码
     */
    @Column(name = "TEL")
    @ExportConfig(value = "电话号码")
    private String tel;

    /**
     * 电子邮箱
     */
    @Column(name = "EMAIL")
    @ExportConfig(value = "电子邮箱")
    private String email;

    /**
     * 行政区划
     */
    @Column(name = "AREA_CODE")
    @ExportConfig(value = "行政区划")
    private String areaCode;

    /**
     * 获取机构ID
     *
     * @return GROUP_ID - 机构ID
     */
    public Long getGroupId() {
        return groupId;
    }

    /**
     * 设置机构ID
     *
     * @param groupId 机构ID
     */
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    /**
     * 获取上级机构ID
     *
     * @return PARENT_ID - 上级机构ID
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * 设置上级机构ID
     *
     * @param parentId 上级机构ID
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取机构名称
     *
     * @return GROUP_NAME - 机构名称
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * 设置机构名称
     *
     * @param groupName 机构名称
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName == null ? null : groupName.trim();
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

    /**
     * 获取机构类型
     *
     * @return GROUP_TYPE - 机构类型
     */
    public String getGroupType() {
        return groupType;
    }

    /**
     * 设置机构类型
     *
     * @param groupType 机构类型
     */
    public void setGroupType(String groupType) {
        this.groupType = groupType == null ? null : groupType.trim();
    }

    /**
     * 获取机构全称
     *
     * @return GROUP_FULL_NAME - 机构全称
     */
    public String getGroupFullName() {
        return groupFullName;
    }

    /**
     * 设置机构全称
     *
     * @param groupFullName 机构全称
     */
    public void setGroupFullName(String groupFullName) {
        this.groupFullName = groupFullName == null ? null : groupFullName.trim();
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

    /**
     * 实体主键
     */
    @Override
    public Object getPrimaryKey() {
        return groupId;
    }
}
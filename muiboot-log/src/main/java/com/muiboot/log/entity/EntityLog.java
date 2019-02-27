package com.muiboot.log.entity;

import com.muiboot.core.entity.BaseModel;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "M_ENTITY_LOG")
public class EntityLog extends BaseModel implements Serializable {
    /**
     * 主键
     */
    @Id
    @Column(name = "LOG_ID")
    private Integer logId;

    /**
     * 表名
     */
    @Column(name = "TABLE_NAME")
    private String tableName;

    /**
     * 表备注
     */
    @Column(name = "TABLE_COMMENT")
    private String tableComment;

    /**
     * 列名
     */
    @Column(name = "COL_NAME")
    private String colName;

    /**
     * 列备注
     */
    @Column(name = "COL_COMMENT")
    private String colComment;

    /**
     * 原值
     */
    @Column(name = "ORIGIN_VAL")
    private String originVal;

    /**
     * 目标值
     */
    @Column(name = "TAGET_VAL")
    private String tagetVal;

    /**
     * 修改批号
     */
    @Column(name = "LOG_NUMBER")
    private Long logNumber;

    /**
     * 日志来源
     */
    @Column(name = "LOG_ORIGIN_ID")
    private String logOriginId;

    /**
     * 获取主键
     *
     * @return LOG_ID - 主键
     */
    public Integer getLogId() {
        return logId;
    }

    /**
     * 设置主键
     *
     * @param logId 主键
     */
    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    /**
     * 获取表名
     *
     * @return TABLE_NAME - 表名
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * 设置表名
     *
     * @param tableName 表名
     */
    public void setTableName(String tableName) {
        this.tableName = tableName == null ? null : tableName.trim();
    }

    /**
     * 获取表备注
     *
     * @return TABLE_COMMENT - 表备注
     */
    public String getTableComment() {
        return tableComment;
    }

    /**
     * 设置表备注
     *
     * @param tableComment 表备注
     */
    public void setTableComment(String tableComment) {
        this.tableComment = tableComment == null ? null : tableComment.trim();
    }

    /**
     * 获取列名
     *
     * @return COL_NAME - 列名
     */
    public String getColName() {
        return colName;
    }

    /**
     * 设置列名
     *
     * @param colName 列名
     */
    public void setColName(String colName) {
        this.colName = colName == null ? null : colName.trim();
    }

    /**
     * 获取列备注
     *
     * @return COL_COMMENT - 列备注
     */
    public String getColComment() {
        return colComment;
    }

    /**
     * 设置列备注
     *
     * @param colComment 列备注
     */
    public void setColComment(String colComment) {
        this.colComment = colComment == null ? null : colComment.trim();
    }

    /**
     * 获取原值
     *
     * @return ORIGIN_VAL - 原值
     */
    public String getOriginVal() {
        return originVal;
    }

    /**
     * 设置原值
     *
     * @param originVal 原值
     */
    public void setOriginVal(String originVal) {
        this.originVal = originVal == null ? null : originVal.trim();
    }

    /**
     * 获取目标值
     *
     * @return TAGET_VAL - 目标值
     */
    public String getTagetVal() {
        return tagetVal;
    }

    /**
     * 设置目标值
     *
     * @param tagetVal 目标值
     */
    public void setTagetVal(String tagetVal) {
        this.tagetVal = tagetVal == null ? null : tagetVal.trim();
    }

    /**
     * 获取修改批号
     *
     * @return LOG_NUMBER - 修改批号
     */
    public Long getLogNumber() {
        return logNumber;
    }

    /**
     * 设置修改批号
     *
     * @param logNumber 修改批号
     */
    public void setLogNumber(Long logNumber) {
        this.logNumber = logNumber;
    }

    /**
     * 获取日志来源
     *
     * @return LOG_ORIGIN_ID - 日志来源
     */
    public String getLogOriginId() {
        return logOriginId;
    }

    /**
     * 设置日志来源
     *
     * @param logOriginId 日志来源
     */
    public void setLogOriginId(String logOriginId) {
        this.logOriginId = logOriginId == null ? null : logOriginId.trim();
    }
    /**
     * 实体主键
     */
    @Override
    @Transient
    public Object getPrimaryKey() {
        return logId;
    }
}
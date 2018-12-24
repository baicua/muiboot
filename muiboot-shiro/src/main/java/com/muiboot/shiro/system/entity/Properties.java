package com.muiboot.shiro.system.entity;

import com.muiboot.core.domain.BaseModel;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "M_PROPERTIES")
public class Properties extends BaseModel implements Serializable {
    /**
     * 主键
     */
    @Id
    @Column(name = "PROP_ID")
    private Long propId;

    /**
     * 配置键
     */
    @Column(name = "PROP_KEY")
    private String propKey;

    /**
     * 配置值
     */
    @Column(name = "PROP_VALUE")
    private String propValue;

    /**
     * 备注
     */
    @Column(name = "PROP_MEMO")
    private String propMemo;

    /**
     * 获取主键
     *
     * @return PROP_ID - 主键
     */
    public Long getPropId() {
        return propId;
    }

    /**
     * 设置主键
     *
     * @param propId 主键
     */
    public void setPropId(Long propId) {
        this.propId = propId;
    }

    /**
     * 获取配置键
     *
     * @return PROP_KEY - 配置键
     */
    public String getPropKey() {
        return propKey;
    }

    /**
     * 设置配置键
     *
     * @param propKey 配置键
     */
    public void setPropKey(String propKey) {
        this.propKey = propKey == null ? null : propKey.trim();
    }

    /**
     * 获取配置值
     *
     * @return PROP_VALUE - 配置值
     */
    public String getPropValue() {
        return propValue;
    }

    /**
     * 设置配置值
     *
     * @param propValue 配置值
     */
    public void setPropValue(String propValue) {
        this.propValue = propValue == null ? null : propValue.trim();
    }

    /**
     * 获取备注
     *
     * @return PROP_MEMO - 备注
     */
    public String getPropMemo() {
        return propMemo;
    }

    /**
     * 设置备注
     *
     * @param propMemo 备注
     */
    public void setPropMemo(String propMemo) {
        this.propMemo = propMemo == null ? null : propMemo.trim();
    }

    @Override
    public Object getPrimaryKey() {
        return propId;
    }
}
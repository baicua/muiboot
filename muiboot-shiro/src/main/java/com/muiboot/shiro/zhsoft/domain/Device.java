package com.muiboot.shiro.zhsoft.domain;

import com.muiboot.core.common.domain.BaseModel;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
@Table(name = "DEVICE")
public class Device extends BaseModel implements Serializable {
    @Id
    @Column(name = "DEVICE_ID")
    private Integer deviceId;

    @Column(name = "DEVICE_NUM")
    private String deviceNum;

    @Column(name = "DEVICE_NAME")
    private String deviceName;

    @Column(name = "DEVICE_ORGAN")
    private String deviceOrgan;

    @Column(name = "DEVICE_NEXT_DATE")
    private Date deviceNextDate;

    @Column(name = "DEVICE_ZL")
    private String deviceZl;

    /**
     * @return DEVICE_ID
     */
    public Integer getDeviceId() {
        return deviceId;
    }

    /**
     * @param deviceId
     */
    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    /**
     * @return DEVICE_NUM
     */
    public String getDeviceNum() {
        return deviceNum;
    }

    /**
     * @param deviceNum
     */
    public void setDeviceNum(String deviceNum) {
        this.deviceNum = deviceNum == null ? null : deviceNum.trim();
    }

    /**
     * @return DEVICE_NAME
     */
    public String getDeviceName() {
        return deviceName;
    }

    /**
     * @param deviceName
     */
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName == null ? null : deviceName.trim();
    }

    /**
     * @return DEVICE_ORGAN
     */
    public String getDeviceOrgan() {
        return deviceOrgan;
    }

    /**
     * @param deviceOrgan
     */
    public void setDeviceOrgan(String deviceOrgan) {
        this.deviceOrgan = deviceOrgan == null ? null : deviceOrgan.trim();
    }

    /**
     * @return DEVICE_NEXT_DATE
     */
    public Date getDeviceNextDate() {
        return deviceNextDate;
    }

    /**
     * @param deviceNextDate
     */
    public void setDeviceNextDate(Date deviceNextDate) {
        this.deviceNextDate = deviceNextDate;
    }

    /**
     * @return DEVICE_ZL
     */
    public String getDeviceZl() {
        return deviceZl;
    }

    /**
     * @param deviceZl
     */
    public void setDeviceZl(String deviceZl) {
        this.deviceZl = deviceZl == null ? null : deviceZl.trim();
    }

    /**
     * 实体主键
     */
    @Override
    public Object getPrimaryKey() {
        return deviceId;
    }
}
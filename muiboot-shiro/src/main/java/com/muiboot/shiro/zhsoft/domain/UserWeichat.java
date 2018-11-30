package com.muiboot.shiro.zhsoft.domain;

import java.util.Date;
import javax.persistence.*;

@Table(name = "USER_WEICHAT")
public class UserWeichat {
    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "OPEN_ID")
    private String openId;

    @Column(name = "BIND_DATE")
    private Date bindDate;

    @Column(name = "UNBIND_DATE")
    private Date unbindDate;

    @Column(name = "STATUS")
    private String status;

    /**
     * @return ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return USER_ID
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * @return OPEN_ID
     */
    public String getOpenId() {
        return openId;
    }

    /**
     * @param openId
     */
    public void setOpenId(String openId) {
        this.openId = openId == null ? null : openId.trim();
    }

    /**
     * @return BIND_DATE
     */
    public Date getBindDate() {
        return bindDate;
    }

    /**
     * @param bindDate
     */
    public void setBindDate(Date bindDate) {
        this.bindDate = bindDate;
    }

    /**
     * @return UNBIND_DATE
     */
    public Date getUnbindDate() {
        return unbindDate;
    }

    /**
     * @param unbindDate
     */
    public void setUnbindDate(Date unbindDate) {
        this.unbindDate = unbindDate;
    }

    /**
     * @return STATUS
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }
}
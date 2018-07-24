package com.baicua.shiro.system.domain;

import javax.persistence.*;

@Table(name = "ATT_NEXUS")
public class AttNexus {
    /**
     * 附件ID
     */
    @Id
    @Column(name = "ATT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attId;

    /**
     * 附件保存名
     */
    @Column(name = "ATT_REAL_NAME")
    private String attRealName;

    /**
     * 附件保存路径
     */
    @Column(name = "ATT_DIR")
    private String attDir;

    /**
     * 附件类型
     */
    @Column(name = "ATT_TYPE")
    private String attType;

    /**
     * 附件真实名称
     */
    @Column(name = "ATT_ORI_NAME")
    private String attOriName;

    /**
     * 附件访问路径
     */
    @Column(name = "ATT_URL")
    private String attUrl;

    /**
     * 获取附件ID
     *
     * @return ATT_ID - 附件ID
     */
    public Long getAttId() {
        return attId;
    }

    /**
     * 设置附件ID
     *
     * @param attId 附件ID
     */
    public void setAttId(Long attId) {
        this.attId = attId;
    }

    /**
     * 获取附件保存名
     *
     * @return ATT_REAL_NAME - 附件保存名
     */
    public String getAttRealName() {
        return attRealName;
    }

    /**
     * 设置附件保存名
     *
     * @param attRealName 附件保存名
     */
    public void setAttRealName(String attRealName) {
        this.attRealName = attRealName == null ? null : attRealName.trim();
    }

    /**
     * 获取附件保存路径
     *
     * @return ATT_DIT - 附件保存路径
     */
    public String getAttDir() {
        return attDir;
    }

    /**
     * 设置附件保存路径
     *
     * @param attDir 附件保存路径
     */
    public void setAttDir(String attDir) {
        this.attDir = attDir == null ? null : attDir.trim();
    }

    /**
     * 获取附件类型
     *
     * @return ATT_TYPE - 附件类型
     */
    public String getAttType() {
        return attType;
    }

    /**
     * 设置附件类型
     *
     * @param attType 附件类型
     */
    public void setAttType(String attType) {
        this.attType = attType == null ? null : attType.trim();
    }

    /**
     * 获取附件真实名称
     *
     * @return ATT_ORI_NAME - 附件真实名称
     */
    public String getAttOriName() {
        return attOriName;
    }

    /**
     * 设置附件真实名称
     *
     * @param attOriName 附件真实名称
     */
    public void setAttOriName(String attOriName) {
        this.attOriName = attOriName == null ? null : attOriName.trim();
    }

    /**
     * 获取附件访问路径
     *
     * @return ATT_URL - 附件访问路径
     */
    public String getAttUrl() {
        return attUrl;
    }

    /**
     * 设置附件访问路径
     *
     * @param attUrl 附件访问路径
     */
    public void setAttUrl(String attUrl) {
        this.attUrl = attUrl == null ? null : attUrl.trim();
    }
}
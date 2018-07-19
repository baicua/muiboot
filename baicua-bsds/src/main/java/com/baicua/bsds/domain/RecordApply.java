package com.baicua.bsds.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "RECORD_APPLY")
public class RecordApply implements Serializable {
    @Id
    @Column(name = "AP_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long apId;

    @Column(name = "AP_DEPT_ID")
    private Long apDeptId;

    @Column(name = "AP_DEPT_NAME")
    private String apDeptName;

    @Column(name = "AP_NAME")
    private String apName;

    @Column(name = "AP_DATE")
    private Date apDate;

    @Column(name = "R_ID")
    private Long rId;

    @Column(name = "AP_TYPE")
    private Integer apType;

    @Column(name = "R_NAME")
    private String rName;

    @Column(name = "AP_BATCH_NUM")
    private String apBatchNum;

    @Column(name = "AP_QUANTITY")
    private Integer apQuantity;

    /**
     * @return AP_ID
     */
    public Long getApId() {
        return apId;
    }

    /**
     * @param apId
     */
    public void setApId(Long apId) {
        this.apId = apId;
    }

    /**
     * @return AP_DEPT_ID
     */
    public Long getApDeptId() {
        return apDeptId;
    }

    /**
     * @param apDeptId
     */
    public void setApDeptId(Long apDeptId) {
        this.apDeptId = apDeptId;
    }

    /**
     * @return AP_DEPT_NAME
     */
    public String getApDeptName() {
        return apDeptName;
    }

    /**
     * @param apDeptName
     */
    public void setApDeptName(String apDeptName) {
        this.apDeptName = apDeptName == null ? null : apDeptName.trim();
    }

    /**
     * @return AP_NAME
     */
    public String getApName() {
        return apName;
    }

    /**
     * @param apName
     */
    public void setApName(String apName) {
        this.apName = apName == null ? null : apName.trim();
    }

    /**
     * @return AP_DATE
     */
    public Date getApDate() {
        return apDate;
    }

    /**
     * @param apDate
     */
    public void setApDate(Date apDate) {
        this.apDate = apDate;
    }

    /**
     * @return R_ID
     */
    public Long getrId() {
        return rId;
    }

    /**
     * @param rId
     */
    public void setrId(Long rId) {
        this.rId = rId;
    }

    /**
     * @return AP_TYPE
     */
    public Integer getApType() {
        return apType;
    }

    /**
     * @param apType
     */
    public void setApType(Integer apType) {
        this.apType = apType;
    }

    /**
     * @return R_NAME
     */
    public String getrName() {
        return rName;
    }

    /**
     * @param rName
     */
    public void setrName(String rName) {
        this.rName = rName == null ? null : rName.trim();
    }

    /**
     * @return AP_BATCH_NUM
     */
    public String getApBatchNum() {
        return apBatchNum;
    }

    /**
     * @param apBatchNum
     */
    public void setApBatchNum(String apBatchNum) {
        this.apBatchNum = apBatchNum == null ? null : apBatchNum.trim();
    }

    /**
     * @return AP_QUANTITY
     */
    public Integer getApQuantity() {
        return apQuantity;
    }

    /**
     * @param apQuantity
     */
    public void setApQuantity(Integer apQuantity) {
        this.apQuantity = apQuantity;
    }
}
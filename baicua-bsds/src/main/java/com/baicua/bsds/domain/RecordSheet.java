package com.baicua.bsds.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "RECORD_SHEET")
public class RecordSheet implements Serializable {
    @Id
    @Column(name = "R_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rId;

    @Column(name = "R_CODE")
    private String rCode;

    @Column(name = "R_TYPE")
    private Integer rType;

    @Column(name = "R_SOL_NAME")
    private String rSolName;

    @Column(name = "R_POTENCY")
    private String rPotency;

    @Column(name = "R_REF_METHOD")
    private String rRefMethod;

    @Column(name = "R_CRT_DATE")
    private Date rCrtDate;

    @Column(name = "R_UPD_DATE")
    private Date rUpdDate;

    @Column(name = "R_UPD_PER")
    private String rUpdPer;

    @Column(name = "R_YEAR")
    private String rYear;

    @Column(name = "R_SERIAL_NUM")
    private Long rSerialNum;

    @Column(name = "R_VESION")
    private Long rVesion;

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
     * @return R_CODE
     */
    public String getrCode() {
        return rCode;
    }

    /**
     * @param rCode
     */
    public void setrCode(String rCode) {
        this.rCode = rCode == null ? null : rCode.trim();
    }

    /**
     * @return R_TYPE
     */
    public Integer getrType() {
        return rType;
    }

    /**
     * @param rType
     */
    public void setrType(Integer rType) {
        this.rType = rType;
    }

    /**
     * @return R_SOL_NAME
     */
    public String getrSolName() {
        return rSolName;
    }

    /**
     * @param rSolName
     */
    public void setrSolName(String rSolName) {
        this.rSolName = rSolName == null ? null : rSolName.trim();
    }

    /**
     * @return R_POTENCY
     */
    public String getrPotency() {
        return rPotency;
    }

    /**
     * @param rPotency
     */
    public void setrPotency(String rPotency) {
        this.rPotency = rPotency == null ? null : rPotency.trim();
    }

    /**
     * @return R_REF_METHOD
     */
    public String getrRefMethod() {
        return rRefMethod;
    }

    /**
     * @param rRefMethod
     */
    public void setrRefMethod(String rRefMethod) {
        this.rRefMethod = rRefMethod == null ? null : rRefMethod.trim();
    }

    /**
     * @return R_CRT_DATE
     */
    public Date getrCrtDate() {
        return rCrtDate;
    }

    /**
     * @param rCrtDate
     */
    public void setrCrtDate(Date rCrtDate) {
        this.rCrtDate = rCrtDate;
    }

    /**
     * @return R_UPD_DATE
     */
    public Date getrUpdDate() {
        return rUpdDate;
    }

    /**
     * @param rUpdDate
     */
    public void setrUpdDate(Date rUpdDate) {
        this.rUpdDate = rUpdDate;
    }

    /**
     * @return R_UPD_PER
     */
    public String getrUpdPer() {
        return rUpdPer;
    }

    /**
     * @param rUpdPer
     */
    public void setrUpdPer(String rUpdPer) {
        this.rUpdPer = rUpdPer == null ? null : rUpdPer.trim();
    }

    /**
     * @return R_YEAR
     */
    public String getrYear() {
        return rYear;
    }

    /**
     * @param rYear
     */
    public void setrYear(String rYear) {
        this.rYear = rYear == null ? null : rYear.trim();
    }

    /**
     * @return R_SERIAL_NUM
     */
    public Long getrSerialNum() {
        return rSerialNum;
    }

    /**
     * @param rSerialNum
     */
    public void setrSerialNum(Long rSerialNum) {
        this.rSerialNum = rSerialNum;
    }

    /**
     * @return R_VESION
     */
    public Long getrVesion() {
        return rVesion;
    }

    /**
     * @param rVesion
     */
    public void setrVesion(Long rVesion) {
        this.rVesion = rVesion;
    }
}
package com.baicua.bsds.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "RECORD_BOOK")
public class RecordBook implements Serializable {
    @Id
    @Column(name = "R_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rId;

    @Column(name = "R_DEPT")
    private String rDept;

    @Column(name = "R_DEPT_ID")
    private Long rDeptId;

    @Column(name = "R_NAME")
    private String rName;

    @Column(name = "R_PRO")
    private String rPro;

    @Column(name = "R_SERIAL_NUM")
    private Long rSerialNum;

    @Column(name = "R_CRT_DATE")
    private Date rCrtDate;

    @Column(name = "R_UPD_DATE")
    private Date rUpdDate;

    @Column(name = "R_UPD_PER")
    private String rUpdPer;

    @Column(name = "R_VESION")
    private Long rVesion;

    @Column(name = "ATT_ID")
    private Long attId;

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
     * @return R_DEPT
     */
    public String getrDept() {
        return rDept;
    }

    /**
     * @param rDept
     */
    public void setrDept(String rDept) {
        this.rDept = rDept == null ? null : rDept.trim();
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
     * @return R_PRO
     */
    public String getrPro() {
        return rPro;
    }

    /**
     * @param rPro
     */
    public void setrPro(String rPro) {
        this.rPro = rPro == null ? null : rPro.trim();
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

    public Long getrDeptId() {
        return rDeptId;
    }

    public void setrDeptId(Long rDeptId) {
        this.rDeptId = rDeptId;
    }

    public Long getAttId() {
        return attId;
    }

    public void setAttId(Long attId) {
        this.attId = attId;
    }
}
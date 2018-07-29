package com.baicua.bsds.domain;

import com.baicua.shiro.common.annotation.ExportConfig;
import org.apache.commons.lang.StringUtils;
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
    @ExportConfig(value = "申领部门")
    private String apDeptName;

    @Column(name = "AP_NAME")
    @ExportConfig(value = "申领人")
    private String apName;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "AP_DATE")
    @ExportConfig(value = "申领时间",convert = "c:com.baicua.shiro.common.util.poi.convert.TimeConvert")
    private Date apDate;

    @Column(name = "R_ID")
    private Long rId;

    /*申领类别：记录本，记录单*/
    @Column(name = "AP_TYPE")
    @ExportConfig(value = "申领类别", convert = "s:1=记录单,2=记录本")
    private Integer apType;

    @Column(name = "SHEET_TYPE")
    @ExportConfig(value = "记录单类别",convert = "s:1=流动相（记录单）,2=标准溶液（记录单）")
    private Integer sheetType;

    @Column(name = "R_NAME")
    @ExportConfig(value = "溶液名称/记录本分类")
    private String rName;

    @Column(name = "AP_BATCH_NUM")
    @ExportConfig(value = "记录单号/记录本号")
    private String apBatchNum;

    @Column(name = "AP_QUANTITY")
    @ExportConfig(value = "申领份数")
    private Integer apQuantity;

    @Transient
    private Object record;

    @Transient
    private String content;

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

    public Integer getSheetType() {
        return sheetType;
    }

    public void setSheetType(Integer sheetType) {
        this.sheetType = sheetType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setRecord(Object record) {
        this.record = record;
    }

    public String getContent() {
        if (record==null|| StringUtils.isNotBlank(content)){
            return content;
        }
        StringBuffer buffer = new StringBuffer("");
        if (record instanceof RecordBook){
            buffer.append("分类：").append(((RecordBook) record).getrName()).append(",");
            buffer.append("份数：").append(this.apQuantity).append("。");
        }else if (record instanceof RecordSheet)
        {
            buffer.append("模板编号：").append(((RecordSheet) record).getrCode()).append(",");
            buffer.append("参考方法：").append(((RecordSheet) record).getrRefMethod()).append(",");
            buffer.append("浓度：").append(((RecordSheet) record).getrPotency()).append(",");
            buffer.append("份数：").append(this.apQuantity).append("。");
        }
        content=buffer.toString();
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
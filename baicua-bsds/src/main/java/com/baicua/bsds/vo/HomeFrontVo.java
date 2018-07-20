package com.baicua.bsds.vo;

import com.baicua.bsds.domain.RecordApply;

import java.util.List;

/**
 * Created by 75631 on 2018/7/20.
 */
/*
前台页面显示
 */
public class HomeFrontVo {
    private Integer sheetApCount;
    private Integer bookApCount;
    private Integer deptApCount;
    private List<RecordApply> appliesSelf;
    private List<RecordApply> appliesLast;

    public Integer getSheetApCount() {
        return sheetApCount;
    }

    public void setSheetApCount(Integer sheetApCount) {
        this.sheetApCount = sheetApCount;
    }

    public Integer getBookApCount() {
        return bookApCount;
    }

    public void setBookApCount(Integer bookApCount) {
        this.bookApCount = bookApCount;
    }

    public Integer getDeptApCount() {
        return deptApCount;
    }

    public void setDeptApCount(Integer deptApCount) {
        this.deptApCount = deptApCount;
    }

    public List<RecordApply> getAppliesSelf() {
        return appliesSelf;
    }

    public void setAppliesSelf(List<RecordApply> appliesSelf) {
        this.appliesSelf = appliesSelf;
    }

    public List<RecordApply> getAppliesLast() {
        return appliesLast;
    }

    public void setAppliesLast(List<RecordApply> appliesLast) {
        this.appliesLast = appliesLast;
    }
}

package com.baicua.bsds.domain;

import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.IllegalFormatException;
import java.util.IllformedLocaleException;
@Table(name = "SEQUENCE")
public class Sequence implements Serializable {
    /**
     * '序列名'
     */
    @Id
    @Column(name = "SEQ_NAME")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String seqName;

    /**
     * 当前值
     */
    @Column(name = "CURRENT_VAL")
    private Long currentVal;

    /**
     * 步长
     */
    @Column(name = "INCREMENT_VAL")
    private Long incrementVal;

    /**
     * 规则
     *  格式 规则字符：参数1,参数2,参数3...
     * 如 %s%03d:NB,currentVal
     */
    @Column(name = "RULE")
    private String rule;

    /**
     * 时间格式yyyyMMdd任意
     */
    @Column(name = "FORMAT")
    private String format;

    public Sequence(String seqName){
        this.seqName=seqName;
    }
    public Sequence(){
    }

    /**
     * 获取'序列名'
     *
     * @return SEQ_NAME - '序列名'
     */
    public String getSeqName() {
        return seqName;
    }

    /**
     * 设置'序列名'
     *
     * @param seqName '序列名'
     */
    public void setSeqName(String seqName) {
        this.seqName = seqName == null ? null : seqName.trim();
    }

    /**
     * 获取当前值
     *
     * @return CURRENT_VAL - 当前值
     */
    public Long getCurrentVal() {
        return currentVal;
    }

    /**
     * 设置当前值
     *
     * @param currentVal 当前值
     */
    public void setCurrentVal(Long currentVal) {
        this.currentVal = currentVal;
    }

    /**
     * 获取步长
     *
     * @return INCREMENT_VAL - 步长
     */
    public Long getIncrementVal() {
        return incrementVal;
    }

    /**
     * 设置步长
     *
     * @param incrementVal 步长
     */
    public void setIncrementVal(Long incrementVal) {
        this.incrementVal = incrementVal;
    }

    /**
     * 获取规则
     *
     * @return RULE - 规则
     */
    public String getRule() {
        return rule;
    }

    /**
     * @param rule 规则
     */
    public void setRule(String rule) {
        this.rule = rule == null ? null : rule.trim();
    }

    /**
     * 获取时间格式yyyyMMdd任意
     *
     * @return FORMAT - 时间格式yyyyMMdd任意
     */
    public String getFormat() {
        return format;
    }

    /**
     * 设置时间格式yyyyMMdd任意
     *
     * @param format 时间格式yyyyMMdd任意
     */
    public void setFormat(String format) {
        this.format = format == null ? null : format.trim();
    }

    public  String getSerialNum() throws IllegalFormatException {
        if (StringUtils.isBlank(this.rule)){
            return null;
        }
        try {
            String[] str = rule.split(":");
            String[] args=str[1].split(",");
            return String.format(str[0],getValue(args));
        }catch (Exception e){
            throw new IllformedLocaleException("获取格式序列错误");
        }
    }

    //===============
    private Object[] getValue(String[] arg){
        int l=arg.length;
        Object res[]=new Object[l];
        for (int i=0;i<l;i++){
            switch (arg[i]){
                case "seqName":res[i]= this.seqName;break;
                case "currentVal":res[i]= this.currentVal;break;
                case "incrementVal":res[i]= this.incrementVal;break;
                case "rule":res[i]= this.rule;break;
                case "format":res[i]= new Date();break;
                default :res[i]=arg[i];break;
            }
        }
        return res;
    }
}
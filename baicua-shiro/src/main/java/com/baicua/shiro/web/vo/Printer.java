package com.baicua.shiro.web.vo;

import javax.print.PrintService;
import javax.print.attribute.Attribute;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.PrinterState;
import javax.print.attribute.standard.PrinterStateReasons;
import javax.print.attribute.standard.QueuedJobCount;
import java.io.Serializable;

/**
 * <p>Title: 打印机属性</p>
 * <p>Description: </p>
 * <p>Company: www.lvmama.com</p>
 *
 * @author jin
 * @version 1.0 2018/7/30
 */
public class Printer implements Serializable {
    private String name;
    private String state;
    private boolean isDefault=false;
    private String stateReasons;
    private String queuedJobCount;
    public Printer(){};
    public Printer(String name){
        this.name=name;
    };
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public String getStateReasons() {
        return stateReasons;
    }

    public void setStateReasons(String stateReasons) {
        this.stateReasons = stateReasons;
    }

    public String getQueuedJobCount() {
        return queuedJobCount;
    }

    public void setQueuedJobCount(String queuedJobCount) {
        this.queuedJobCount = queuedJobCount;
    }
    public void setAttributes(PrintService printService){
        AttributeSet attributes=printService.getAttributes();
        Attribute queuedJobCount=attributes.get(QueuedJobCount.class);
        Attribute printerState =attributes.get(PrinterState.class);
        Attribute printerStateReasons =attributes.get(PrinterStateReasons.class);
        if (null!=queuedJobCount)
            this.queuedJobCount=queuedJobCount.toString();
        if (null!=printerState)
            this.state=printerState.toString();
        if (null!=printerStateReasons)
            this.stateReasons=printerStateReasons.toString();

    }
}

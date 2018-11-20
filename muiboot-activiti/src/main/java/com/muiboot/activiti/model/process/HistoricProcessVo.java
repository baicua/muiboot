package com.muiboot.activiti.model.process;

import java.util.Date;

/**
 * 受理情况信息
 * 
 * @ClassName: HistoricProcessVo
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author cpy
 * @date 2017年2月16日 上午11:32:50
 *
 */
public class HistoricProcessVo {
  private String id;
  private String processName;
  private String assigneName;
  private Date startTime; 
  private Date endTime;
  private String orgName;
  private String partName;
  private String result;
  private String reason;

  public HistoricProcessVo() {
  }
 

  public HistoricProcessVo(String id, String processName, String assigneName, Date startTime, Date endTime, String orgName, String partName, String result, String reason) {
    super();
    this.id = id;
    this.processName = processName;
    this.assigneName = assigneName;
    this.startTime = startTime;
    this.endTime = endTime;
    this.orgName = orgName;
    this.partName = partName;
    this.result = result;
    this.reason = reason;
  }



  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getProcessName() {
    return processName;
  }

  public void setProcessName(String processName) {
    this.processName = processName;
  }

  public String getAssigneName() {
    return assigneName;
  }

  public void setAssigneName(String assigneName) {
    this.assigneName = assigneName;
  }

  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }

  public String getOrgName() {
    return orgName;
  }

  public void setOrgName(String orgName) {
    this.orgName = orgName;
  }

  public String getPartName() {
    return partName;
  }

  public void setPartName(String partName) {
    this.partName = partName;
  }

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

}

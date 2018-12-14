package com.muiboot.activiti.dao;

import com.muiboot.activiti.active.param.query.BusinessParam;
import com.muiboot.activiti.entity.BusinessTask;
import com.muiboot.activiti.entity.HisTask;
import com.muiboot.activiti.entity.RuTask;

import java.util.List;

/**
 * Created by 75631 on 2018/12/14.
 */
public interface BusinessTaskMapper{
    List<RuTask> getBusinessTasks(BusinessParam param);
    List<HisTask> getHisBusinessTasks(BusinessParam param);
}

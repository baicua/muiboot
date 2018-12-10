package com.muiboot.activiti.service.runtime;

import com.muiboot.activiti.active.declaration.BusinessTask;
import com.muiboot.activiti.active.param.operation.CompleteParam;
import com.muiboot.activiti.active.param.operation.StartParam;
import com.muiboot.activiti.active.param.query.BusinessParam;
import org.activiti.engine.runtime.ProcessInstance;

import java.util.List;

/**
 * <p>Description: </p>
 *
 * @version 1.0 2018/11/29
 */
public interface RuntimeService {

    ProcessInstance start(StartParam param);

    void claim(CompleteParam param);

    void resolveTask(CompleteParam param);

    void delegateTask(CompleteParam param);

    void complete(CompleteParam param);

    List<BusinessTask> getBusinessTasks(BusinessParam param);

}

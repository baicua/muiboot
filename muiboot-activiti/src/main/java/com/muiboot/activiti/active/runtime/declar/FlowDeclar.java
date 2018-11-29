package com.muiboot.activiti.active.runtime.declar;

import com.muiboot.activiti.active.operation.arg.param.CompleteParam;
import com.muiboot.activiti.active.operation.arg.param.StartParam;
import org.activiti.engine.runtime.ProcessInstance;

/**
 * <p>Description: </p>
 *
 * @version 1.0 2018/11/29
 */
public interface FlowDeclar {

    ProcessInstance start(StartParam param);

    void claim(CompleteParam param);

    void resolveTask(CompleteParam param);

    void delegateTask(CompleteParam param);

    void complete(CompleteParam param);

}

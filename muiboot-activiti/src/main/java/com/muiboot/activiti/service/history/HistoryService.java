package com.muiboot.activiti.service.history;

import com.muiboot.activiti.active.declaration.BusinessTask;
import com.muiboot.activiti.active.param.query.BusinessParam;
import org.activiti.engine.task.Comment;

import java.util.List;

/**
 * <p>Description: </p>
 *
 * @version 1.0 2018/12/10
 */
public interface HistoryService {

    List<Comment> getTaskComments(String taskId);

    List<Comment> getProInstanceComments(String proInstanceId);

    List<BusinessTask> getBusinessTasks(BusinessParam param);
}

package com.muiboot.activiti.service.history;

import com.muiboot.activiti.dao.BusinessTaskMapper;
import com.muiboot.activiti.entity.BusinessTask;
import com.muiboot.activiti.active.param.query.BusinessParam;
import com.muiboot.activiti.entity.HisTask;
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

    List<HisTask> getBusinessTasks(BusinessParam param, BusinessTaskMapper mapper);

}

package com.muiboot.activiti.service.history.impl;

import com.muiboot.activiti.dao.BusinessTaskMapper;
import com.muiboot.activiti.active.param.query.BusinessParam;
import com.muiboot.activiti.entity.HisTask;
import com.muiboot.activiti.service.history.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>Description: </p>
 *
 * @version 1.0 2018/12/10
 */
@Service
@Transactional
public class HistoryServiceImpl implements HistoryService {
    @Autowired
    private TaskService taskService;
    @Override
    public List<Comment> getTaskComments(String taskId) {
        return taskService.getTaskComments(taskId,"OPINION");
    }

    @Override
    public List<Comment> getProInstanceComments(String proInstanceId) {
        return taskService.getProcessInstanceComments(proInstanceId,"OPINION");
    }

    @Override
    public List<HisTask> getBusinessTasks(BusinessParam param, BusinessTaskMapper mapper){
       return mapper.getHisBusinessTasks(param);
    }

}

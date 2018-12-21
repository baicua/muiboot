package com.muiboot.activiti.listener;

import com.muiboot.activiti.active.group.User;
import com.muiboot.activiti.dao.RuShipMapper;
import com.muiboot.activiti.entity.RuShip;
import com.muiboot.activiti.util.AuthenticationUtil;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by 75631 on 2018/12/15.
 */
@Service
@Transactional
public class TaskShipListener implements TaskListener {
    protected  final Logger logger = LoggerFactory.getLogger(TaskShipListener.class);
    @Autowired
    private RuShipMapper ruShipMapper;
    @Override
    public void notify(DelegateTask delegateTask) {
        User user=delegateTask.getVariable("VARIABLE_USER_CLASS", User.class);
        User authUser= AuthenticationUtil.getAuthenticatedUser();
        user=user==null?authUser:user;
        delegateTask.removeVariable("VARIABLE_USER_CLASS");
        logger.info("监听任务事件，事件类型:{},任务ID:{}",delegateTask.getEventName(),delegateTask.getId());
        try {
/*            switch (delegateTask.getEventName()){
                case EVENTNAME_CREATE:
                    ruShipMapper.insert(new RuShip(delegateTask,user));break;
                case EVENTNAME_COMPLETE:
                    ruShipMapper.deleteByPrimaryKey(delegateTask.getId());;break;
                case EVENTNAME_DELETE:
                    ruShipMapper.deleteByPrimaryKey(delegateTask.getId());;break;
                case EVENTNAME_ASSIGNMENT:
                    ruShipMapper.insert(new RuShip(delegateTask,user));break;
            }*/
        }catch (Exception e){
            logger.error("监听任务处理失败");
            throw new ActivitiException("监听任务处理失败");
        }
    }
}

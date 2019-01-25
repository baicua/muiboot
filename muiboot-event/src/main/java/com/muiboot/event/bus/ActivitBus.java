package com.muiboot.event.bus;

import com.muiboot.event.listener.EmailListener;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * <p>Description: </p>
 *
 * @version 1.0 2019/1/25
 */
@Component
public class ActivitBus extends BaseBus implements InitializingBean {
    @Resource
    private EmailListener emailListener;
    @Override
    public void afterPropertiesSet() throws Exception {
        this.register(emailListener);
    }
}

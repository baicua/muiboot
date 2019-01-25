package com.muiboot.event.bus;

import com.muiboot.event.listener.EmailListener;
import com.muiboot.event.listener.LogListener;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * <p>Description: </p>
 *
 * @version 1.0 2019/1/25
 */
@Component
public class TwoBus extends BaseBus implements InitializingBean {
    @Resource
    private EmailListener emailListener;
    @Resource
    private LogListener logListener;
    @Override
    public void afterPropertiesSet() throws Exception {
        this.register(emailListener);
        this.register(logListener);
    }
}

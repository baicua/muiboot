package com.muiboot.event.bus;

import com.google.common.eventbus.EventBus;
import com.muiboot.event.factory.EventBusFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

/**
 * <p>Description: </p>
 *
 * @version 1.0 2019/1/25
 */
public abstract class BaseBus extends EventBus {
    @Autowired
    public void factory(){
        EventBusFactory.register(this.getClass(),this);
    }
}

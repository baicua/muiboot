package com.muiboot.event.bus;

import org.springframework.beans.factory.InitializingBean;

/**
 * <p>Description: </p>
 *
 * @version 1.0 2019/1/28
 */
public class NullBus extends BaseBus implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {

    }
}

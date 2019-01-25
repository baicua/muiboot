package com.muiboot.event;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;

/**
 * <p>Description: </p>
 *
 * @version 1.0 2019/1/25
 */
public interface EventListener {
    @Subscribe
    @AllowConcurrentEvents
    public void listen(EventMessage message);
}

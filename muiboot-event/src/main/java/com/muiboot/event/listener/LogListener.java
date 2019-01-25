package com.muiboot.event.listener;

import com.muiboot.event.EventListener;
import com.muiboot.event.EventMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * <p>Description: </p>
 *
 * @version 1.0 2019/1/25
 */
@Component
public class LogListener  implements EventListener {
    public final static Logger logger = LoggerFactory.getLogger(LogListener.class);

    @Override
    public void listen(EventMessage message) {
        logger.info("LogListener");
    }
}

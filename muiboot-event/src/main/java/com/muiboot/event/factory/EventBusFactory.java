package com.muiboot.event.factory;

import com.google.common.eventbus.EventBus;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Description: </p>
 *
 * @version 1.0 2019/1/25
 */
public class EventBusFactory {
    private static Map<Class, EventBus> eventBusMap = new HashMap();

    public EventBusFactory() {
    }

    public static void register(Class clazz,EventBus bus) {
        if (!eventBusMap.containsKey(clazz)){
            synchronized (EventBusFactory.class){
                if (!eventBusMap.containsKey(clazz)){
                    eventBusMap.put(clazz,bus);
                }
            }
        }
    }

    public static EventBus get(Class clazz) {
        EventBus eventBus = eventBusMap.get(clazz);
        if(eventBus == null) {
            throw new NullPointerException("eventBus is null");
        }
        return eventBus;
    }
}

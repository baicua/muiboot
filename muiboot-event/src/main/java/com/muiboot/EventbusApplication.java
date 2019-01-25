package com.muiboot;

import com.google.common.eventbus.EventBus;
import com.muiboot.event.EventMessage;
import com.muiboot.event.bus.TwoBus;
import com.muiboot.event.factory.EventBusFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@EnableCaching
@SpringBootApplication
@EnableTransactionManagement
@RestController
public class EventbusApplication {
	protected static  Logger logger = LoggerFactory.getLogger(EventbusApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(EventbusApplication.class, args);
		logger.info("muiboot-eventbus启动完毕#########################");
	}
	@RequestMapping("/")
	public void eventbus(){
		EventBus bus=EventBusFactory.get(TwoBus.class);
		EventMessage message=new EventMessage();
		bus.post(message);
	}
}

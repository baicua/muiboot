package com.muiboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableCaching
@SpringBootApplication
@MapperScan("com.muiboot.**.dao")
@EnableTransactionManagement
@EnableAutoConfiguration(exclude={
		org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class,
		org.activiti.spring.boot.SecurityAutoConfiguration.class

})
public class ActivitiApplication {
	protected static  Logger logger = LoggerFactory.getLogger(ActivitiApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(ActivitiApplication.class, args);
		logger.info("MUIBOOT-ACTIVITI启动完毕#########################");
	}
}

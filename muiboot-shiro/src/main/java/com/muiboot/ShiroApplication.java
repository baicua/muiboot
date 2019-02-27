package com.muiboot;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableCaching
@EnableAsync
@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.muiboot.**.dao")
//@EnableAutoConfiguration(exclude={
//		JpaRepositoriesAutoConfiguration.class//禁止springboot自动加载持久化bean
//		,org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class
//		,org.activiti.spring.boot.SecurityAutoConfiguration.class
//})
public class ShiroApplication {
	protected static  Logger logger = LoggerFactory.getLogger(ShiroApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(ShiroApplication.class, args);
		logger.info("muiboot-SHIRO启动完毕#########################");
	}

/*	@Bean
	public ResourceUrlEncodingFilter resourceUrlEncodingFilter() {
		return new ResourceUrlEncodingFilter();
	}*/
}

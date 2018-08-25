package com.muiboot;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.resource.ResourceUrlEncodingFilter;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan({"com.muiboot.shiro.*.dao"})
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

package com.muiboot.shiro.common.config;

import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Description: 邮箱配置</p>
 *
 * @version 1.0 2019/1/21
 */
@Configuration
public class MailConfig {

    @Bean
    public MailProperties getMailProperties(){
        MailProperties properties=new MailProperties();
        properties.setHost("smtp.qq.com");
        properties.setUsername("");
        properties.setPassword("");
        properties.getProperties().put("","");
        properties.getProperties().put("","");
        return properties;
    }
}

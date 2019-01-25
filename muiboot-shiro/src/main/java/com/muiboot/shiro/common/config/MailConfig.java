package com.muiboot.shiro.common.config;

import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * <p>Description: 邮箱配置</p>
 *
 * @version 1.0 2019/1/21
 */
@Configuration
public class MailConfig {

    public MailProperties getMailProperties(){
        MailProperties properties=new MailProperties();
        properties.setHost("smtp.exmail.qq.com");
        properties.setPort(465);
        properties.setUsername("manager@baicua.com");
        properties.setPassword("xsVqUP5Vh9vgvYKK");
        properties.getProperties().put("spring.mail.properties.mail.smtp.auth","true");
        properties.getProperties().put("spring.mail.properties.mail.smtp.starttls.enable","true");
        properties.getProperties().put("spring.mail.properties.mail.smtp.starttls.required","true");
        properties.getProperties().put("spring.mail.properties.mail.smtp.ssl.enable","true");

        return properties;
    }
}

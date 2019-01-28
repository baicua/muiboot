package com.muiboot.shiro;

import com.muiboot.shiro.common.controller.ShiroBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>Description: 测试</p>
 *
 * @version 1.0 2019/1/24
 */
@RestController
public class TestController extends ShiroBaseController {
    @Autowired
    private JavaMailSender mailSender;

    @RequestMapping("send/email")
    public void sendEmail(){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("manager@baicua.com");
        message.setTo("511637001@qq.com");
        message.setSubject("主题：徐通傻逼");
        message.setText("徐通傻逼");

        mailSender.send(message);
    }
}

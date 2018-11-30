package com.muiboot.shiro.zhsoft.controller;

import com.muiboot.core.common.domain.ResponseBo;
import com.muiboot.core.common.util.MD5Utils;
import com.muiboot.shiro.common.controller.BaseController;
import com.muiboot.shiro.common.util.ShiroUtil;
import com.muiboot.shiro.system.domain.User;
import com.muiboot.shiro.zhsoft.domain.UserWeichat;
import com.muiboot.shiro.zhsoft.service.UserWeichatService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * <p>Description: </p>
 *
 * @version 1.0 2018/11/30
 */
@Controller
@RequestMapping(value = "api")
public class ApiBindController extends BaseController {

    @RequestMapping(value="token")
    @ResponseBody
    public String token(String echostr){
        logger.info("微信服务器验证--{}",echostr);
        return echostr;
    }

    @Autowired
    private UserWeichatService  userWeichatService;


    @RequestMapping(value="bind",method = RequestMethod.GET)
    @ResponseBody
    public ResponseBo bind(User user, String openId){
        if(StringUtils.isBlank(user.getUsername())||StringUtils.isBlank(user.getPassword())||StringUtils.isBlank(openId)){
            return ResponseBo.error("用户名、密码、openId不能为空");
        }
        Session session = super.getSession();
        // 密码 MD5 加密
        String password = MD5Utils.encrypt(user.getUsername().toLowerCase(), user.getPassword());
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), password, false);
        try {
            super.login(token);
            this.bindWeichat(ShiroUtil.getCurrentUser(),openId);
            return ResponseBo.ok("绑定成功");
        } catch (UnknownAccountException | IncorrectCredentialsException | LockedAccountException e) {
            logger.error("登录失败："+e.getMessage());
            return ResponseBo.error(e.getMessage());
        } catch (AuthenticationException e) {
            logger.error("登录认证失败");
            return ResponseBo.error("认证失败！");
        }catch (Exception e){
            getSubject().logout();
            return ResponseBo.error("绑定失败！");
        }
    }

    private void bindWeichat(User user, String openId) {
        UserWeichat weichat=new UserWeichat();
        weichat.setUserId(user.getUserId());
        weichat.setOpenId(openId);
        weichat.setBindDate(new Date());
        weichat.setStatus("1");
        userWeichatService.save(weichat);
    }
}

package com.muiboot.shiro.common.shiro;

import com.muiboot.core.common.util.HttpUtils;
import com.muiboot.shiro.system.domain.User;
import com.muiboot.shiro.system.service.UserService;
import com.muiboot.shiro.zhsoft.domain.UserWeichat;
import com.muiboot.shiro.zhsoft.service.UserWeichatService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Map;

/**
 * 在线文档过滤器配置
 * @see org.apache.shiro.web.filter.authc.AnonymousFilter
 */
public class WeiChatFilter extends UserFilter {

    @Autowired
    @Lazy//必须懒加载，否则缓存失效
    private UserWeichatService userWeichatService;

    protected  final Logger logger = LoggerFactory.getLogger(WeiChatFilter.class);

    @Autowired
    @Lazy//必须懒加载，否则缓存失效
    private UserService userService;

    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if(this.isLoginRequest(request, response)) {
            return true;
        } else {
            Subject subject = this.getSubject(request, response);
            return subject.getPrincipal() != null;
        }
    }

    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        String code = request.getParameter("code");
        String openId="";
        if (StringUtils.isNotBlank(code)){
            Map map=HttpUtils.https("https://api.weixin.qq.com/sns/oauth2/access_token","appid=wx7b153a3d8fa28196&secret=cac935747491b9976de124e9fbe3c3a3&code="+code+"&grant_type=authorization_code");
            if (null!=map){
                openId= (String) map.get("openid");
                logger.info("微信服务--code={}，openid={}",code,openId);
                UserWeichat userWeichat=this.userWeichatService.getBindUserByOpenId(openId);
                if (null==userWeichat){
                    this.saveRequestAndRedirectToLogin(request, response);//跳转绑定地址
                    return false;
                }
                User user=userService.selectByKey(userWeichat.getUserId());
                if (null!=userWeichat&&user!=null){
                    //String password = MD5Utils.encrypt(user.get.toLowerCase(), password);
                    UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword(), false);
                    SecurityUtils.getSubject().login(token);
                }else {
                    return false;
                }
                //this.saveRequestAndRedirectToLogin(request, response);
                return true;
            }
        }
        this.saveRequestAndRedirectToLogin(request, response);//跳转绑定地址
        return false;
    }
}

package com.muiboot.shiro.system.controller;

import com.muiboot.core.common.annotation.Log;
import com.muiboot.shiro.common.controller.BaseController;
import com.muiboot.core.common.domain.ResponseBo;
import com.muiboot.core.common.util.MD5Utils;
import com.muiboot.core.common.util.exec.ExecutorsUtil;
import com.muiboot.core.common.util.vcode.Captcha;
import com.muiboot.core.common.util.vcode.GifCaptcha;
import com.muiboot.shiro.system.domain.User;
import com.muiboot.shiro.system.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.concurrent.ExecutorService;
/**
 * <p>Title: </p>
 * <p>Description: 登录逻辑</p>
 *
 * @author jin
 * @version 1.0 2018/9/18
 */
@Controller
public class LoginController extends BaseController {

    @Autowired
    private UserService userService;

    ExecutorService exeService= ExecutorsUtil.getInstance().getMultilThreadExecutor();

    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @Log("登录系统")
    @PostMapping("/login")
    @ResponseBody
    public ResponseBo login(String username, String password, String code, Boolean rememberMe) {
        if (null==rememberMe)rememberMe=false;
        if (!StringUtils.isNotBlank(code)) {
//            return ResponseBo.warn("验证码不能为空！");
        }
        if(StringUtils.isBlank(username)||StringUtils.isBlank(password)){
            return ResponseBo.error("用户名、密码、验证码不能为空");
        }
        Session session = super.getSession();
        String sessionCode = (String) session.getAttribute("_code");
        session.removeAttribute("_code");
/*        if (!code.toLowerCase().equals(sessionCode)) {
//            return ResponseBo.warn("验证码错误！");
        }*/
        // 密码 MD5 加密
        password = MD5Utils.encrypt(username.toLowerCase(), password);
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
        try {
            super.login(token);
            exeService.execute(new Runnable() {
                @Override
                public void run() {
                    userService.updateLoginTime(username);
                }
            });
            return ResponseBo.ok();
        } catch (UnknownAccountException | IncorrectCredentialsException | LockedAccountException e) {
            logger.error("登录失败："+e.getMessage());
            return ResponseBo.error(e.getMessage());
        } catch (AuthenticationException e) {
            logger.error("登录认证失败");
            return ResponseBo.error("认证失败！");
        }
    }
    @RequestMapping("home")
    public String home() {
        return "system/home/default";
    }
    @GetMapping(value = "gifCode")
    public void getGifCode(HttpServletResponse response, HttpServletRequest request) {
        try {
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/gif");

            Captcha captcha = new GifCaptcha(146, 33, 4);
            captcha.out(response.getOutputStream());
            HttpSession session = request.getSession(true);
            session.removeAttribute("_code");
            session.setAttribute("_code", captcha.text().toLowerCase());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/")
    public String redirectIndex() {
        return "redirect:/sys";
    }

    @GetMapping("/403")
    public String forbid() {
        return "403";
    }

    @RequestMapping("/sys")
    public String index(Model model) {
        // 登录成后，即可通过 Subject 获取登录的用户信息
        User user = super.getCurrentUser();
        model.addAttribute("user", user);
        model.addAttribute("forward", "home");
        return "index";
    }
}

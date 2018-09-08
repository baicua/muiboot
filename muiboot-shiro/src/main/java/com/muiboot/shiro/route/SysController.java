package com.muiboot.shiro.route;

import com.muiboot.shiro.common.controller.BaseController;
import com.muiboot.shiro.system.domain.User;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 75631 on 2018/8/19.
 */
@Controller
public class SysController extends BaseController {
    @ModelAttribute
    public void initBinder(Model model, HttpServletRequest request, HttpServletResponse response){
        //response.setHeader("Cache-Control", "max-age="+MAX_AGE);
    }
    @RequestMapping("sys/{forward}")
    public String sys(@PathVariable(name = "forward") String forward,Model model) {
        model.addAttribute("forward", forward);
        User user = super.getCurrentUser();
        model.addAttribute("user", user);
        return "index";
    }
    @RequestMapping("menu")
    @RequiresPermissions("menu:list")
    public String menu() {
        return "system/menu/menu";
    }
    @RequestMapping("dict")
    @RequiresPermissions("dict:list")
    public String dict() {
        return "system/dict/dict";
    }
    @RequestMapping("group")
    @RequiresPermissions("group:list")
    public String group() {
        return "system/group/group";
    }
    @RequestMapping("log")
    @RequiresPermissions("log:list")
    public String log() {
        return "system/log/log";
    }
    @RequestMapping("session")
    @RequiresPermissions("session:list")
    public String online() {
        return "system/monitor/online";
    }
    @RequestMapping("user")
    @RequiresPermissions("user:list")
    public String index(Model model) {
        User user = super.getCurrentUser();
        model.addAttribute("user", user);
        return "system/user/user";
    }
}

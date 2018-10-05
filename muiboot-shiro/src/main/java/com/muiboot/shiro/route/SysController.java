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
* <p>Description: 菜单跳转</p>
* @version 1.0 2018/9/18
* @author jin
*/
@Controller
public class SysController extends BaseController {
    //预处理信息，设置页面缓存时间
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
    public String user(Model model) {
        User user = super.getCurrentUser();
        model.addAttribute("user", user);
        return "system/user/user";
    }
    @RequestMapping("role")
    @RequiresPermissions("role:list")
    public String role() {
        return "system/role/role";
    }
}

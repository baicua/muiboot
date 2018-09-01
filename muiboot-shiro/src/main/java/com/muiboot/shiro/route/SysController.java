package com.muiboot.shiro.route;

import com.muiboot.shiro.common.controller.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 75631 on 2018/8/19.
 */
@Controller
@RequestMapping("sys")
public class SysController extends BaseController {
    @ModelAttribute
    public void initBinder(Model model, HttpServletRequest request, HttpServletResponse response){
        //response.setHeader("Cache-Control", "max-age="+MAX_AGE);
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
}

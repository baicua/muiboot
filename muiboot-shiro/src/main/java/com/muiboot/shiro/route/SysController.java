package com.muiboot.shiro.route;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by 75631 on 2018/8/19.
 */
@Controller
@RequestMapping("sys")
public class SysController {
    @RequestMapping("menu")
    @RequiresPermissions("menu:list")
    public String menu() {
        return "system/menu/menu";
    }
}

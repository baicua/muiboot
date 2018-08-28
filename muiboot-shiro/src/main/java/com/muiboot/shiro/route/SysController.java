package com.muiboot.shiro.route;

import com.muiboot.shiro.common.controller.BaseController;
import com.muiboot.shiro.common.domain.ResponseBo;
import com.muiboot.shiro.system.domain.Menu;
import com.muiboot.shiro.system.domain.SysLog;
import com.muiboot.shiro.system.service.LogService;
import com.muiboot.shiro.system.service.MenuService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 75631 on 2018/8/19.
 */
@Controller
@RequestMapping("sys")
public class SysController extends BaseController {
    @Autowired
    private MenuService menuService;
    @Autowired
    private LogService logService;
    private static final long MAX_AGE =60*60*24*7;//缓存7天
    @ModelAttribute
    public void initBinder(Model model, HttpServletRequest request, HttpServletResponse response){
        response.setHeader("Cache-Control", "max-age="+MAX_AGE);
    }
    @RequestMapping("menu")
    @RequiresPermissions("menu:list")
    public String menu() {
        return "system/menu/menu";
    }

    @RequestMapping("test")
    @ResponseBody
    public ResponseBo test() {
        try {
            Menu menu=new Menu();
            menu.setMenuId(10L);
            menu.setOrderNum("222");
            SysLog log = new SysLog();
            log.setIp("2222");
            log.setUsername("2222222222");
            // 保存系统日志
            this.logService.save(log);
            this.menuService.save(menu);
            return ResponseBo.ok(menu);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBo.error("测试失败！");
        }
    }
}

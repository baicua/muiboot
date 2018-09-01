package com.muiboot.shiro.route;

import com.muiboot.shiro.common.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 75631 on 2018/8/31.
 */
@Controller
@RequestMapping("model")
public class ModelController  extends BaseController {
    @ModelAttribute
    public void initBinder(HttpServletResponse response){
        response.setHeader("Cache-Control", "max-age="+MAX_AGE);
    }
    @RequestMapping("dic")
    public String dic() {
        return "system/model/dic/dic";
    }

    @RequestMapping("dic/{file}")
    public String dicRoute(@PathVariable(name = "file") String file) {
        return "system/model/dic/"+file;
    }
}

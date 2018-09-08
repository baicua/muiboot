package com.muiboot.shiro.route;

import com.muiboot.shiro.common.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @RequestMapping("group")
    public String group() {
        return "system/model/group/group";
    }

    @RequestMapping("dic/{file}")
    public String dicRoute(@PathVariable(name = "file") String file) {
        return "system/model/dic/"+file;
    }

    @RequestMapping("group/{file}")
    public String groupRoute(@PathVariable(name = "file") String file) {
        return "system/model/group/"+file;
    }
}

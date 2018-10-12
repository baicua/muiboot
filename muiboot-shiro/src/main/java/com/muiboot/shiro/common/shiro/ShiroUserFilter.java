package com.muiboot.shiro.common.shiro;;
import com.muiboot.shiro.common.util.ServletUtil;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* <p>Description: 请求超时，返回ERROR</p>
* @version 1.0 2018/10/12
* @author jin
*/
public class ShiroUserFilter extends UserFilter {
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if(ServletUtil.isAjax(request)){
            HttpServletResponse res= (HttpServletResponse) response;
            res.sendError(HttpServletResponse.SC_SEE_OTHER,"登陆超时");
        }else{
            this.saveRequestAndRedirectToLogin(request, response);
        }
        return false;
    }
}

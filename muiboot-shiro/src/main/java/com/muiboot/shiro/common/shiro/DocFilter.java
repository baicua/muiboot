package com.muiboot.shiro.common.shiro;

import org.apache.shiro.web.filter.PathMatchingFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * 在线文档过滤器配置
 * @see org.apache.shiro.web.filter.authc.AnonymousFilter
 */
public class DocFilter  extends PathMatchingFilter {
    /**
     * Always returns <code>true</code> allowing unchecked access to the underlying path or resource.
     *
     * @return <code>true</code> always, allowing unchecked access to the underlying path or resource.
     */
    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {
        // Always return true since we allow access to anyone
        return this.saveRequestAndRedirect(request,response);
    }
    protected boolean saveRequestAndRedirect(ServletRequest request, ServletResponse response) throws IOException {
       return redirect(request, response);
    }
    protected boolean redirect(ServletRequest request, ServletResponse response) throws IOException {
        String url = this.getPathWithinApplication(request);
        if (url.endsWith("/")){
            WebUtils.saveRequest(request);
            WebUtils.issueRedirect(request, response, url+="index.html");
            return false;
        }else {
            return true;
        }
    }
}

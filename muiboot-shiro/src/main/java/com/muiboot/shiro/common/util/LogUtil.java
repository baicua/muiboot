package com.muiboot.shiro.common.util;
import com.alibaba.druid.support.json.JSONUtils;
import com.muiboot.shiro.system.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 75631 on 2018/8/29.
 */
public class LogUtil{
    private  Logger log = null;

    public static LogUtil getLoger(Class clazz){
        LogUtil logger = new LogUtil();
        logger.setLog(LoggerFactory.getLogger(clazz));
        return logger;
    }
    public Logger getLog() {
        return log;
    }

    public void setLog(Logger log) {
        this.log = log;
    }

    public void error(String message, Exception ex, HttpServletRequest req){
        String userId="未登录用户";
        String url="";
        Long user=ShiroUtil.getCurrentUserId();
        if (user!=null){
            userId=user.toString();
        }
        if (null!=req&&null!=req.getRequestURL()){
            url=req.getRequestURL().toString();
        }
        log.error("user:{},url:{},message:{},Exception:{},errorMessage:{},IP:{}",userId,url,message,ex.getClass(),ex.getMessage(),IPUtils.getIpAddr(req));
        StackTraceElement[] stackTraceElements=ex.getStackTrace();
        if (null!=stackTraceElements){
            StringBuilder error = new StringBuilder();
            for (StackTraceElement stackTraceElement : stackTraceElements) {
                if (stackTraceElement.getClassName().contains("muiboot")){
                    error.append("{class:").append(stackTraceElement.getClassName()).append(",");
                    error.append("method:").append(stackTraceElement.getMethodName()).append(",");
                    error.append("line:").append(stackTraceElement.getLineNumber()).append("}");
                    log.error(error.toString());
                    error.delete(0,error.length());
                }
            }
        }
    }
}

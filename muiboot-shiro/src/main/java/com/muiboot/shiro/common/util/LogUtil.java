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

    public void error(String message, Exception ex,String url){
        long threadId=Thread.currentThread().getId();
        error(message,ex,url,threadId);
    }
    public void error(String message, Exception ex,String url,long threadId){
        String userId="nouser";
        Long user=ShiroUtil.getCurrentUserId();
        if (user!=null){
            userId=user.toString();
        }
        Throwable throwable=ex.getCause()==null?ex:ex.getCause();
        log.error("threadId:{},user:{},url:{},message:{},Exception:{},,Caused:{},errorMessage:{}",threadId,userId,url,message,ex.getClass(),throwable.getClass(),throwable.getMessage());
        StackTraceElement[] stackTraceElements=throwable.getStackTrace();
        if (null!=stackTraceElements){
            StringBuilder error = new StringBuilder("threadId:").append(threadId).append(",Caused by:");
            error.append(throwable.getClass()).append(":").append(throwable.getMessage()).append("\n");;
            for (StackTraceElement stackTraceElement : stackTraceElements) {
                error.append("at :").append(stackTraceElement.getClassName()).append(",");
                error.append("method:").append(stackTraceElement.getMethodName()).append(",");
                error.append("line:").append(stackTraceElement.getLineNumber()).append("");
                error.append("\n");
            }
            log.error(error.toString());
        }
    }
    public void info(String message,String url){
        long threadId=Thread.currentThread().getId();
        info(message,url,threadId);
    }
    public void info(String message,String url,long threadId){
        String userId="nouser";
        Long user=ShiroUtil.getCurrentUserId();
        if (user!=null){
            userId=user.toString();
        }
        log.info("threadId:{},user:{},url:{},message:{}",threadId,userId,url,message);
    }
}

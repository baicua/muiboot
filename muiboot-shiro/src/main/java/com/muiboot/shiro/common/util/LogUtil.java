package com.muiboot.shiro.common.util;
import org.codehaus.groovy.runtime.StackTraceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        StackTraceUtils.printSanitizedStackTrace(ex);
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

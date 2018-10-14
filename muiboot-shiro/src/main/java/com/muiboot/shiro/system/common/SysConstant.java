package com.muiboot.shiro.system.common;

import java.util.concurrent.TimeUnit;

/**
 * Created by 75631 on 2018/10/4.
 */
public interface SysConstant {
    public static final String BASE_ROLE_KEY="BASE_ROLE_KEY";//用户初始角色
    public static final String INIT_USER_PWD="INIT_USER_PWD";//用户初始密码
    public static final String DATA_MAX_AGE ="DATA_MAX_AGE";//数据缓存时间
    public static final long START_TIME =System.currentTimeMillis();//项目启动时间
    public static final String EXPIRES_DAY ="EXPIRES_DAY";//项目启动后X天过期
}

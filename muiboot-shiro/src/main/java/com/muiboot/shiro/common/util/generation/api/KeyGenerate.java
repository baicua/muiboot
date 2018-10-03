package com.muiboot.shiro.common.util.generation.api;


public interface KeyGenerate {
    /**
     * 生成String类型主键
     * @return
     * */
    public String generateStringKey(Class clazz);
    /**
     * 生成long类型主键
     * @return
     * */
    public long generateLongKey(Class clazz);
}

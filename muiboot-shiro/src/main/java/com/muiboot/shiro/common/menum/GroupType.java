package com.muiboot.shiro.common.menum;

/**
 * Created by 75631 on 2018/8/29.
 */
public enum GroupType {
    ORGAN("1"),DEPT("2");
    private String type;
    GroupType(String type){
        this.type=type;
    }
    public String getType(){
        return type;
    }
}

package com.muiboot.shiro.common.menum;

/**
* <p>Description: 机构类型枚举</p>
* @version 1.0 2018/10/12
* @author jin
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

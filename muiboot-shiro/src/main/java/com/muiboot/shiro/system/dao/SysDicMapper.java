package com.muiboot.shiro.system.dao;

import com.muiboot.core.mapper.MyMapper;
import com.muiboot.shiro.system.entity.SysDic;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysDicMapper extends MyMapper<SysDic> {
    /**
     * 原生SQL查询
     * @param msql
     * @return
     */
    List<Map> nativeSelectBySQL(@Param(value="msql")String msql);
}
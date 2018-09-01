package com.muiboot.shiro.system.dao;

import com.muiboot.shiro.common.config.MyMapper;
import com.muiboot.shiro.system.domain.CoreDic;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CoreDicMapper extends MyMapper<CoreDic> {
    /**
     * 原生SQL查询
     * @param msql
     * @return
     */
    List<Map> nativeSelectBySQL(@Param(value="msql")String msql);
}
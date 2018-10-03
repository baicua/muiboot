package com.muiboot.shiro.common.config;

import com.muiboot.shiro.common.mapper.common.InsertByBatchMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertUseGeneratedKeysMapper;

public interface MyMapper<T> extends Mapper<T>, InsertByBatchMapper<T>, InsertUseGeneratedKeysMapper<T> {

}
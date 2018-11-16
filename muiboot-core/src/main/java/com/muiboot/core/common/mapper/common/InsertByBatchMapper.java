package com.muiboot.core.common.mapper.common;

import com.muiboot.core.common.mapper.provider.BatchProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;

import java.util.List;

/**
 * <p>Description: 批量插入方法，支持ORACLE,MYSQL</p>
 *
 * @author jin
 * @version 1.0 2018/10/10
 */
public interface InsertByBatchMapper<T>  {
    @Options(
            useGeneratedKeys = false,
            keyProperty = "id"
    )
    @InsertProvider(
            type = BatchProvider.class,
            method = "dynamicSQL"
    )
    int insertByBatch(List<T> var1);
}

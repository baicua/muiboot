package com.muiboot.shiro.common.mapper.common;

import com.muiboot.shiro.common.mapper.provider.BatchProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;

import java.util.List;

/**
 * Created by 75631 on 2018/10/3.
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

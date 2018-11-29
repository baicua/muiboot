package com.muiboot.activiti.active.operation.arg;

import com.muiboot.activiti.active.validation.Assert;

import java.util.Map;

/**
 * <p>Description: </p>
 *
 * @version 1.0 2018/11/29
 */
public interface Variable<T> extends Assert {
    Map<String,String> toMap();
}

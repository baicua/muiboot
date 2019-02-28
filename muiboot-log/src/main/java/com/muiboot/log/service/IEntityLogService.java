package com.muiboot.log.service;

import com.muiboot.core.service.IService;
import com.muiboot.log.entity.EntityLog;

/**
 * <p>Description: </p>
 *
 * @version 1.0 2019/2/26
 */
public interface IEntityLogService  extends IService<EntityLog> {
    public void pushLog(Object origin,Object target);
}

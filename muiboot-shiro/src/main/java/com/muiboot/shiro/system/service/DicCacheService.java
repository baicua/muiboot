package com.muiboot.shiro.system.service;

import com.muiboot.core.common.service.IService;
import com.muiboot.shiro.system.domain.SysDic;

import java.util.Map;

/**
 * Created by 75631 on 2018/9/1.
 */
public interface DicCacheService extends IService<SysDic> {
     Map<String,Object> getAllDicMap();
}

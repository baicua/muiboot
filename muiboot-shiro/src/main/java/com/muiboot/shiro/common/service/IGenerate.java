package com.muiboot.shiro.common.service;

import com.muiboot.shiro.common.domain.Generate;
import net.sf.ehcache.Element;

/**
 * Created by 75631 on 2018/10/3.
 */
public interface IGenerate extends IService<Generate> {

    public Element getGenerate(Class clazz);


}

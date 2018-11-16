package com.muiboot.shiro.system.service.impl;

import com.muiboot.core.common.service.impl.BaseService;
import com.muiboot.shiro.system.domain.Properties;
import com.muiboot.shiro.system.service.PropertiesService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
@Service("propertiesService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PropertiesServiceImpl extends BaseService<Properties> implements PropertiesService {

}

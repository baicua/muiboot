package com.muiboot.shiro.zhsoft.service.impl;

import com.muiboot.core.common.service.impl.BaseService;
import com.muiboot.shiro.zhsoft.domain.Device;
import com.muiboot.shiro.zhsoft.service.DeviceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>Description: </p>
 *
 * @version 1.0 2018/11/29
 */
@Service("deviceService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DeviceServiceImpl extends BaseService<Device> implements DeviceService {
}

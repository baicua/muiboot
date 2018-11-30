package com.muiboot.shiro.zhsoft.service;

import com.muiboot.core.common.service.IService;
import com.muiboot.shiro.zhsoft.domain.UserWeichat;

/**
 * <p>Description: </p>
 *
 * @version 1.0 2018/11/29
 */
public interface UserWeichatService extends IService<UserWeichat> {

    UserWeichat getBindUserByOpenId(String openId);
}

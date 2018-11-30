package com.muiboot.shiro.zhsoft.service.impl;

import com.muiboot.core.common.service.impl.BaseService;
import com.muiboot.shiro.zhsoft.domain.UserWeichat;
import com.muiboot.shiro.zhsoft.service.UserWeichatService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>Description: </p>
 *
 * @version 1.0 2018/11/29
 */
@Service("userWeichatService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserWeichatServiceImpl extends BaseService<UserWeichat> implements UserWeichatService {
    @Override
    @Cacheable(value="sessionCache",key="#openId")
    public UserWeichat getBindUserByOpenId(String openId) {
        if (StringUtils.isBlank(openId)){
            return null;
        }
        UserWeichat param = new UserWeichat();
        param.setOpenId(openId);
        param.setStatus(NumberUtils.INTEGER_ONE.toString());
        UserWeichat userWeichat=this.getMapper().selectOne(param);
        return userWeichat;
    }
}

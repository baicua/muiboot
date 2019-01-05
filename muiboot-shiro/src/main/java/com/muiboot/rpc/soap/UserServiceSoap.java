package com.muiboot.rpc.soap;
import com.muiboot.rpc.ResponseBo;
import com.muiboot.rpc.Service;
import com.muiboot.shiro.system.entity.User;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by 75631 on 2019/1/5.
 */
@WebService
public interface UserServiceSoap extends Service {
    @WebMethod
    public ResponseBo<User> getUser(@WebParam(name = "userId",targetNamespace = "http://get.rcp.muiboot.com/") Long userId);
}

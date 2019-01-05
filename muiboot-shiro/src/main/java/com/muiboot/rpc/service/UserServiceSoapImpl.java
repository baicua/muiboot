package com.muiboot.rpc.service;
import com.muiboot.rpc.AbstractService;
import com.muiboot.rpc.ResponseBo;
import com.muiboot.rpc.RpcException;
import com.muiboot.shiro.system.entity.User;
import com.muiboot.rpc.soap.UserServiceSoap;
import com.muiboot.shiro.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.ResponseWrapper;

/**
 * Created by 75631 on 2019/1/4.
 */
@WebService(name = "userServiceType",serviceName = "userService",portName = "userServicePort",targetNamespace = "http://service.rcp.muiboot.com/",endpointInterface = "com.muiboot.rpc.soap.UserServiceSoap")
@RestController
public class UserServiceSoapImpl extends AbstractService implements UserServiceSoap {
    private final String ADDRESS="/users";
    @Override
    @WebMethod(exclude = true)
    public String getAddress() {
        return ADDRESS;
    }

    @Autowired
    private UserService userService;

    @Override
    @RequestMapping(value = "rest/users/{userId}")
    public ResponseBo<User> getUser(@PathVariable Long userId){
        User user =null;
        ResponseBo bo=null;
        try {
            increment();
            user = this.userService.selectByKey(userId);
            bo= ResponseBo.ok(user);
        } catch (RpcException e) {
            bo=ResponseBo.limit(e.getMessage());
        }finally {
            decrement();
        }
        return bo;
    }
}

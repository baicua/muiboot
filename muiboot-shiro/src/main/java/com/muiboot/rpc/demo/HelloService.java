package com.muiboot.rpc.demo;

import com.muiboot.rpc.Service;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * Created by 75631 on 2019/1/4.
 */
@WebService
public interface HelloService extends Service {
    @WebMethod
    public String test();
}

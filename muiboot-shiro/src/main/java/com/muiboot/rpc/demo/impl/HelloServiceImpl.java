package com.muiboot.rpc.demo.impl;

import com.muiboot.rpc.demo.HelloService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * Created by 75631 on 2019/1/4.
 */
@WebService(targetNamespace = "http://rcp.baicua.com/",endpointInterface = "com.muiboot.rpc.demo.HelloService")
@RestController
public class HelloServiceImpl implements HelloService {
    private final String ADDRESS="/hello";
    @Override
    @WebMethod(exclude = true)
    public String getAddress() {
        return ADDRESS;
    }

    @Override
    @RequestMapping(value = "rest/hello/test")
    public String test() {
        return "this is soap";
    }
}

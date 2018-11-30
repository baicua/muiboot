package com.muiboot.shiro.zhsoft.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.muiboot.core.common.domain.QueryRequest;
import com.muiboot.core.common.domain.ResponseBo;
import com.muiboot.core.common.util.MD5Utils;
import com.muiboot.shiro.common.controller.BaseController;
import com.muiboot.shiro.system.domain.User;
import com.muiboot.shiro.zhsoft.domain.Device;
import com.muiboot.shiro.zhsoft.service.DeviceService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import java.util.Map;

/**
 * <p>Description: </p>
 *
 * @version 1.0 2018/11/29
 */
@RestController
@RequestMapping(value = "api/device")
public class DeviceApi extends BaseController{
    @Autowired
    private DeviceService deviceService;

    @RequestMapping(value="all",method = RequestMethod.GET)
    public Map<String, Object> all(QueryRequest request){
        PageHelper.startPage(request.getPage(), request.getLimit());
        PageInfo<Device> pageInfo = new PageInfo<>(deviceService.selectAll());
        return getDataTable(pageInfo);
    }

    @RequestMapping(value="/{deviceZl}",method = RequestMethod.GET)
    public Map<String, Object> category(@PathVariable("deviceZl") String deviceZl,QueryRequest request){
        PageHelper.startPage(request.getPage(), request.getLimit());
        Example example = new Example(Device.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("deviceZl",deviceZl);
        PageInfo<Device> pageInfo = new PageInfo<>(deviceService.selectByExample(example));
        return getDataTable(pageInfo);
    }

    @RequestMapping(value="/expire",method = RequestMethod.GET)
    public Map<String, Object> expire(QueryRequest request){
        PageHelper.startPage(request.getPage(), request.getLimit());
        PageInfo<Device> pageInfo = new PageInfo<>(deviceService.selectAll());
        return getDataTable(pageInfo);
    }
}

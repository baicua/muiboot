package com.muiboot.shiro.system.common;

import com.muiboot.shiro.system.domain.Properties;
import com.muiboot.shiro.system.service.PropertiesService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

/*
    配置获取
 */

@Component
public class PropertiesUtil implements CommandLineRunner {
    private static volatile HashMap<String,String> instance=new HashMap<>();
    protected  final Logger log = LoggerFactory.getLogger(PropertiesUtil.class);
    @Autowired
    private PropertiesService propertiesService;

    public static String get(String key){
        return instance.get(key);
    }

    public static void put(Properties prop){
        instance.put(prop.getPropKey(),prop.getPropValue());
    }

    @Override
    public void run(String... strings) throws Exception {
        init();
    }
    private synchronized void init(){
        log.info("开始加载配置文件","PropertiesUtil");
        List<Properties> properties=propertiesService.selectAll();
        if (CollectionUtils.isNotEmpty(properties)){
            for (Properties property:properties){
                instance.put(property.getPropKey(),property.getPropValue());
            }
        }
        log.info("本次加载了【"+properties.size()+"】条配置","PropertiesUtil");
    }
}

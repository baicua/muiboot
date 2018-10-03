package com.muiboot.shiro.common.service.impl;

import com.muiboot.shiro.common.domain.Generate;
import com.muiboot.shiro.common.exception.BusinessException;
import com.muiboot.shiro.common.service.IGenerate;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by 75631 on 2018/10/3.
 */
@Service("generate")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class GenerateImpl extends BaseService<Generate> implements IGenerate {
    @Override
    public Element getGenerate(Class clazz) {
        String key=clazz.getName();
        List<CacheManager>cacheManagers=CacheManager.ALL_CACHE_MANAGERS;
        Cache cache=CacheManager.getCacheManager("muiboot").getCache(Generate.CACHE_GENERATE_KEY);
        Element element=cache.get(key);
        if (null!=element){
            return element;
        }else {
            int hit=0;
            Generate generate=null;
            while (0==hit) {
                generate = this.getMapper().selectByPrimaryKey(key);
                if (null == generate) {
                    generate = new Generate();
                    generate.setClassName(key);
                    this.save(generate);
                }

                //generate.setAtomicCurrentval(new AtomicLong(generate.getCurrentval()+generate.getCache()));
                //generate.setMaxval(generate.getCurrentval()+generate.getCache());
                try {
                    Example example = new Example(Generate.class);
                    example.createCriteria().andEqualTo("className",generate.getClassName())
                            .andEqualTo("currentval",generate.getCurrentval());
                    Generate gen= (Generate) BeanUtils.cloneBean(generate);
                    gen.setCurrentval(generate.getMaxval());
                    gen.setMaxval(generate.getMaxval()+generate.getCache());
                    hit = this.mapper.updateByExampleSelective(gen,example);
                } catch (Exception e) {
                    hit=0;
                    throw new BusinessException("序列号生成失败！");
                }
            }
            element=new Element(key,generate);
            cache.put(element);
            return element;
        }
    }
}

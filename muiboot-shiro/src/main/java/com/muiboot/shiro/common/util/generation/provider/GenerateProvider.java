package com.muiboot.shiro.common.util.generation.provider;

import com.muiboot.shiro.common.domain.Generate;
import com.muiboot.shiro.common.service.IGenerate;
import com.muiboot.shiro.common.util.generation.api.KeyGenerate;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by 75631 on 2018/10/3.
 */
@Component
public class GenerateProvider  implements KeyGenerate {
    @Autowired
    private IGenerate generate;
    @Override
    public synchronized String generateStringKey(Class clazz) {
        return null;
    }

    @Override
    public synchronized long generateLongKey(Class clazz) {
        String clazzName=clazz.getName();
        boolean op=false;
        Element old=null;
        Generate gennew=null;
        while (!op) {
            old = generate.getGenerate(clazz);
            Generate gen= (Generate) old.getObjectValue();
            gennew=gen.next();
            if (gennew.getEmpty()) {
                op = this.clear(clazz,old,new Element(clazzName,gennew));
            }else {
                op = this.update(old,new Element(clazzName,gennew));
            }
        }
        return gennew.getAtomicCurrentval().get();
    }

    private boolean update( Element old,Element neww) {
        Cache cache=CacheManager.getCacheManager("muiboot").getCache(Generate.CACHE_GENERATE_KEY);
        return cache.replace(old,neww);
    }

    private boolean clear(Class clazz, Element old,Element neww) {
        Cache cache=CacheManager.getCacheManager("muiboot").getCache(Generate.CACHE_GENERATE_KEY);
        return cache.removeElement(old);
    }

}

package com.muiboot.shiro.system.service.impl;

import com.muiboot.shiro.common.service.impl.BaseService;
import com.muiboot.shiro.system.domain.CoreDic;
import com.muiboot.shiro.system.service.DicMapService;
import com.muiboot.shiro.system.service.DictService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 75631 on 2018/9/1.
 */
@Service("dicMapService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DicMapServiceImpl extends BaseService<CoreDic> implements DicMapService {
    @Autowired
    private DictService dictService;
    @Override
    @Cacheable(value="dicCache",key="'ALLDIC'")
    public Map<String,Object> getAllDicMap() {
        List<CoreDic> dics=dictService.getAllDics();
        Map<String,Object> res=null;
        if (CollectionUtils.isNotEmpty(dics)){
            res=new LinkedHashMap();
            for (int i=0,l=dics.size();i<l;i++){
                Object dicMap = dictService.buildDicList(dics.get(i));
                if (null!=dicMap){
                    res.put(dics.get(i).getDicKey(),dicMap);
                }
            }
        }
        return res;
    }
}

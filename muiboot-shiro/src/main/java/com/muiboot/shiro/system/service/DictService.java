package com.muiboot.shiro.system.service;

import com.muiboot.shiro.common.layer.LayerTree;
import com.muiboot.shiro.common.service.IService;
import com.muiboot.shiro.system.domain.CoreDic;

import java.util.List;
import java.util.Map;

public interface DictService extends IService<CoreDic> {
	LayerTree<CoreDic> getDictTree(String dicName);

    /**
     * 新增字典
     * @param dic
     */
    void add(CoreDic dic);

    Map findDicDetail(Long dicId);

    Map loadDics(String[] dicKeys);

    Map nativeSelectBySQL(String msql);

    List<CoreDic> getAllDics();

    void updateDicNotNull(CoreDic dict);

    Object buildDicList(CoreDic coreDic);
}

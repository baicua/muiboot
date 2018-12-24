package com.muiboot.shiro.system.service;

import com.muiboot.core.domain.LayerTree;
import com.muiboot.core.service.IService;
import com.muiboot.shiro.system.entity.SysDic;

import java.util.List;
import java.util.Map;

public interface DictService extends IService<SysDic> {
	LayerTree<SysDic> getDictTree(String dicName);

    /**
     * 新增字典
     * @param dic
     */
    void add(SysDic dic);

    Map findDicDetail(Long dicId);

    Map loadDics(String[] dicKeys);

    Map nativeSelectBySQL(String msql);

    List<SysDic> getAllDics();

    void updateDicNotNull(SysDic dict);

    Object buildDicList(SysDic sysDic);

    void deleteDicts(String dictIds);
}

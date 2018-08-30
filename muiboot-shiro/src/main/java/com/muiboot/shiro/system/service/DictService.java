package com.muiboot.shiro.system.service;

import com.muiboot.shiro.common.layer.LayerTree;
import com.muiboot.shiro.common.service.IService;
import com.muiboot.shiro.system.domain.CoreDic;
public interface DictService extends IService<CoreDic> {
	LayerTree<CoreDic> getDictTree();
}

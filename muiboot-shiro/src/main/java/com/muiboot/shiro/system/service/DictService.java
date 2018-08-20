package com.muiboot.shiro.system.service;

import java.util.List;

import com.muiboot.shiro.common.service.IService;
import com.muiboot.shiro.system.domain.Dict;

public interface DictService extends IService<Dict> {

	List<Dict> findAllDicts(Dict dict);

	Dict findById(Long dictId);

	void addDict(Dict dict);

	void deleteDicts(String dictIds);

	void updateDict(Dict dicdt);
}

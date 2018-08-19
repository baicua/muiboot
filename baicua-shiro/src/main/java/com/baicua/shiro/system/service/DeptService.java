package com.baicua.shiro.system.service;

import java.util.List;

import com.baicua.shiro.common.layer.LayerTree;
import com.baicua.shiro.common.service.IService;
import com.baicua.shiro.system.domain.Dept;

public interface DeptService extends IService<Dept> {

	LayerTree<Dept> getDeptTree();

	List<Dept> findAllDepts(Dept dept);

	Dept findByName(String deptName);

	Dept findById(Long deptId);
	
	void addDept(Dept dept);
	
	void updateDept(Dept dept);

	void deleteDepts(String deptIds);
}

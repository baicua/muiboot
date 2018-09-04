package com.muiboot.shiro.system.service;

import java.util.List;
import java.util.Map;

import com.muiboot.shiro.common.layer.LayerTree;
import com.muiboot.shiro.common.service.IService;
import com.muiboot.shiro.system.domain.Dept;

public interface DeptService extends IService<Dept> {

	LayerTree<Dept> getDeptTree();

	List<Dept> findAllDepts(Dept dept);

	Dept findByName(String deptName);

	Dept findById(Long deptId);
	
	void addDept(Dept dept);
	
	void updateDept(Dept dept);

	void deleteDepts(String deptIds);

    Map getDeptDetail(Long deptId);
}

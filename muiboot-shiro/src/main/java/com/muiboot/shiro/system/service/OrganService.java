package com.muiboot.shiro.system.service;

import java.util.List;
import java.util.Map;

import com.muiboot.shiro.common.layer.LayerTree;
import com.muiboot.shiro.common.service.IService;
import com.muiboot.shiro.system.domain.Organ;

public interface OrganService extends IService<Organ> {

	LayerTree<Organ> getOrganTree();

	List<Organ> findAllOrgans(Organ organ);

	Organ findByName(String organName);

	Organ findById(Long organId);
	
	void addOrgan(Organ organ);
	
	void updateOrgan(Organ organ);

	void deleteOrgans(String organIds);

    Map getOrganDetail(Long organId);
}

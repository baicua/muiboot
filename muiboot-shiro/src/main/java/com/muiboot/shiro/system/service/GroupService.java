package com.muiboot.shiro.system.service;

import java.util.List;
import java.util.Map;

import com.muiboot.shiro.common.layer.LayerTree;
import com.muiboot.shiro.common.service.IService;
import com.muiboot.shiro.system.domain.SysGroup;

public interface GroupService extends IService<SysGroup> {

	LayerTree<SysGroup> getGroupTree();

	List<SysGroup> findAllGroups(SysGroup group);

	SysGroup findByName(String groupName);

	SysGroup findById(Long groupId);
	
	void addGroup(SysGroup group);
	
	void updateGroup(SysGroup group);

	void deleteGroups(String groupIds);

    Map getGroupDetail(Long groupId);
}

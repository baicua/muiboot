package com.muiboot.shiro.system.service;

import java.util.List;
import java.util.Map;

import com.muiboot.core.domain.LayerTree;
import com.muiboot.core.service.IService;
import com.muiboot.shiro.system.domain.SysGroup;

public interface GroupService extends IService<SysGroup> {

	LayerTree<SysGroup> getGroupTree(String groupName);

	List<SysGroup> findAllGroups(SysGroup group);

	SysGroup findByName(String groupName);

	SysGroup findById(Long groupId);
	
	void addGroup(SysGroup group);
	
	void updateGroup(SysGroup group);

	void deleteGroups(String groupIds);

    Map getGroupDetail(Long groupId);


	Map getDeptByParent(Long parentId);
}

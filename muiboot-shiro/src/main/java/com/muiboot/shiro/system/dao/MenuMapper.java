package com.muiboot.shiro.system.dao;

import java.util.List;

import com.muiboot.shiro.common.config.MyMapper;
import com.muiboot.shiro.system.domain.Menu;

public interface MenuMapper extends MyMapper<Menu> {
	
	List<Menu> findUserPermissions(String userName);
	
	List<Menu> findUserMenus(String userName);

	List<Menu> findUserAuths(String userName);

	List<Menu> findByRole(Long roleId);

	// 删除父节点，子节点变成顶级节点（根据实际业务调整）
	void changeToTop(List<String> menuIds);
}
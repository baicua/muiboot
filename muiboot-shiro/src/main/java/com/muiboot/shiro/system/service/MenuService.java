package com.muiboot.shiro.system.service;

import java.util.List;

import com.muiboot.shiro.common.layer.LayerTree;
import com.muiboot.shiro.common.service.IService;
import com.muiboot.shiro.system.domain.Menu;
import com.muiboot.shiro.system.domain.Role;

public interface MenuService extends IService<Menu> {

	List<Menu> findUserPermissions(String userName);

	List<Menu> findUserMenus(String userName);

	List<Menu> findAllMenus(Menu menu);

	List<Menu> findAllPermissions(Menu menu);

	List<Role> findAllRoles(Menu menu);

	LayerTree<Menu> getMenuButtonTree();
	
	LayerTree<Menu> getMenuTree();
	
	LayerTree<Menu> getUserMenu(String userName);
	
	Menu findById(Long menuId);

	Menu findByNameAndType(String menuName, String type);

	void addMenu(Menu menu);

	void updateMenu(Menu menu);
	
	void deleteMeuns(String menuIds);
}

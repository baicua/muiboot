package com.muiboot.shiro.system.service;

import java.util.List;
import java.util.Map;

import com.muiboot.core.entity.LayerTree;
import com.muiboot.core.service.IService;
import com.muiboot.shiro.system.entity.Menu;
import com.muiboot.shiro.system.entity.Role;

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

	Map findMenuDetail(Long menuId);

	LayerTree<Menu> getAuthList(Long roleId);
}

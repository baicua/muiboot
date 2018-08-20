package com.muiboot.shiro.system.service;

import java.util.List;

import com.muiboot.shiro.common.service.IService;
import com.muiboot.shiro.system.domain.Role;
import com.muiboot.shiro.system.domain.RoleWithMenu;

public interface RoleService extends IService<Role> {

	List<Role> findUserRole(String userName);

	List<Role> findAllRole(Role role);
	
	RoleWithMenu findRoleWithMenus(Long roleId);

	List<RoleWithMenu> findByMenuId(Long menuId);

	Role findByName(String roleName);

	void addRole(Role role, Long[] menuIds);
	
	void updateRole(Role role, Long[] menuIds);

	void deleteRoles(String roleIds);
}

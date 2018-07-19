package com.baicua.shiro.system.service;

import java.util.List;

import com.baicua.shiro.common.service.IService;
import com.baicua.shiro.system.domain.Role;
import com.baicua.shiro.system.domain.RoleWithMenu;

public interface RoleService extends IService<Role> {

	List<Role> findUserRole(String userName);

	List<Role> findAllRole(Role role);
	
	RoleWithMenu findRoleWithMenus(Long roleId);

	Role findByName(String roleName);

	void addRole(Role role, Long[] menuIds);
	
	void updateRole(Role role, Long[] menuIds);

	void deleteRoles(String roleIds);
}

package com.muiboot.shiro.system.service;

import com.muiboot.core.service.IService;
import com.muiboot.shiro.system.domain.RoleMenu;

public interface RoleMenuServie extends IService<RoleMenu> {

	void deleteRoleMenusByRoleId(String roleIds);

	void deleteRoleMenusByMenuId(String menuIds);
}

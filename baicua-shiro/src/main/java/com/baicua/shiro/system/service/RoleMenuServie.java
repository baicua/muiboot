package com.baicua.shiro.system.service;

import com.baicua.shiro.common.service.IService;
import com.baicua.shiro.system.domain.RoleMenu;

public interface RoleMenuServie extends IService<RoleMenu> {

	void deleteRoleMenusByRoleId(String roleIds);

	void deleteRoleMenusByMenuId(String menuIds);
}

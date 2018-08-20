package com.muiboot.shiro.system.service;

import com.muiboot.shiro.common.service.IService;
import com.muiboot.shiro.system.domain.UserRole;

public interface UserRoleService extends IService<UserRole> {

	void deleteUserRolesByRoleId(String roleIds);

	void deleteUserRolesByUserId(String userIds);
}

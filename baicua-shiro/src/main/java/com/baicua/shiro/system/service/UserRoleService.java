package com.baicua.shiro.system.service;

import com.baicua.shiro.common.service.IService;
import com.baicua.shiro.system.domain.UserRole;

public interface UserRoleService extends IService<UserRole> {

	void deleteUserRolesByRoleId(String roleIds);

	void deleteUserRolesByUserId(String userIds);
}

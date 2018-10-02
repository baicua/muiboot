package com.muiboot.shiro.system.service;

import com.muiboot.shiro.common.service.IService;
import com.muiboot.shiro.system.domain.UserRole;

import java.util.List;

public interface UserRoleService extends IService<UserRole> {

	void deleteUserRolesByRoleId(String roleIds);

	void deleteUserRolesByUserId(String userIds);

	List<UserRole> findByEntity(UserRole userRole);

	void insertList(List<UserRole> userRoles);
}

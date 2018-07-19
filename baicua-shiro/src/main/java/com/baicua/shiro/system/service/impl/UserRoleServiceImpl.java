package com.baicua.shiro.system.service.impl;

import java.util.Arrays;
import java.util.List;

import com.baicua.shiro.common.service.impl.BaseService;
import com.baicua.shiro.system.domain.UserRole;
import com.baicua.shiro.system.service.UserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("userRoleService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserRoleServiceImpl extends BaseService<UserRole> implements UserRoleService {

	@Override
	@Transactional
	public void deleteUserRolesByRoleId(String roleIds) {
		List<String> list = Arrays.asList(roleIds.split(","));
		this.batchDelete(list, "roleId", UserRole.class);
	}

	@Override
	@Transactional
	public void deleteUserRolesByUserId(String userIds) {
		List<String> list = Arrays.asList(userIds.split(","));
		this.batchDelete(list, "userId", UserRole.class);
	}

}

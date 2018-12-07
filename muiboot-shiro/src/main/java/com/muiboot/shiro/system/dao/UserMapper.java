package com.muiboot.shiro.system.dao;

import java.util.List;

import com.muiboot.core.common.mapper.MyMapper;
import com.muiboot.shiro.system.domain.User;
import com.muiboot.shiro.system.domain.UserWithRole;

public interface UserMapper extends MyMapper<User> {

	List<User> findUserWithDept(User user);
	
	List<UserWithRole> findUserWithRole(Long userId);
	
	User findUserProfile(User user);

	List<UserWithRole> findUserByRole(UserWithRole userWithRole);
}
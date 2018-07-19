package com.baicua.shiro.system.dao;

import java.util.List;

import com.baicua.shiro.common.config.MyMapper;
import com.baicua.shiro.system.domain.User;
import com.baicua.shiro.system.domain.UserWithRole;

public interface UserMapper extends MyMapper<User> {

	List<User> findUserWithDept(User user);
	
	List<UserWithRole> findUserWithRole(Long userId);
	
	User findUserProfile(User user);
}
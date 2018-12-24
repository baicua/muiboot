package com.muiboot.shiro.system.service;

import java.util.List;

import com.muiboot.core.service.IService;
import com.muiboot.shiro.system.entity.User;
import com.muiboot.shiro.system.entity.UserWithRole;

public interface UserService extends IService<User> {

	UserWithRole findById(Long userId);
	
	User findByName(String userName);

	List<User> findUserWithDept(User user);

	void registUser(User user);

	void updateTheme(String theme, String userName);

	void addUser(User user, Long[] roles);

	void updateUser(User user, Long[] roles);
	
	void deleteUsers(String userIds);

	void updateLoginTime(String userName);
	
	void updatePassword(String password);
	
	User findUserProfile(User user);
	
	void updateUserProfile(User user);

    List<UserWithRole> findUserByRole(UserWithRole userRole);
}

package com.baicua.shiro.system.dao;

import java.util.List;

import com.baicua.shiro.common.config.MyMapper;
import com.baicua.shiro.system.domain.Role;
import com.baicua.shiro.system.domain.RoleWithMenu;

public interface RoleMapper extends MyMapper<Role> {
	
	List<Role> findUserRole(String userName);
	
	List<RoleWithMenu> findById(Long roleId);
}
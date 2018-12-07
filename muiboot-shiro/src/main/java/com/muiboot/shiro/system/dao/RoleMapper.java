package com.muiboot.shiro.system.dao;

import java.util.List;

import com.muiboot.core.common.mapper.MyMapper;
import com.muiboot.shiro.system.domain.Role;
import com.muiboot.shiro.system.domain.RoleWithMenu;

public interface RoleMapper extends MyMapper<Role> {
	
	List<Role> findUserRole(String userName);
	
	List<RoleWithMenu> findById(Long roleId);

    List<RoleWithMenu> findByMenuId(Long menuId);
}
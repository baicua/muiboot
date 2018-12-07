package com.muiboot.shiro.system.dao;

import java.util.List;

import com.muiboot.core.common.mapper.MyMapper;
import com.muiboot.shiro.system.domain.Menu;
import org.apache.ibatis.annotations.Param;

public interface MenuMapper extends MyMapper<Menu> {
	
	List<Menu> findUserPermissions(@Param(value = "userName")String userName);
	
	List<Menu> findUserMenus(@Param(value = "userName") String userName);

	List<Menu> findUserAuths(@Param(value = "userName")String userName);

	List<Menu> findByRole(Long roleId);

	// 删除父节点，子节点变成顶级节点（根据实际业务调整）
	void changeToTop(List<String> menuIds);
}
package com.muiboot.shiro.system.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.muiboot.shiro.common.exception.BusinessException;
import com.muiboot.shiro.common.service.impl.BaseService;
import com.muiboot.shiro.common.util.ShiroUtil;
import com.muiboot.shiro.system.domain.*;
import com.muiboot.shiro.system.service.RoleService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.muiboot.shiro.system.dao.RoleMapper;
import com.muiboot.shiro.system.dao.RoleMenuMapper;
import com.muiboot.shiro.system.service.RoleMenuServie;
import com.muiboot.shiro.system.service.UserRoleService;
import tk.mybatis.mapper.entity.Example;

@Service("roleService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RoleServiceImpl extends BaseService<Role> implements RoleService {

	@Autowired
	private RoleMapper roleMapper;

	@Autowired
	private RoleMenuMapper roleMenuMapper;

	@Autowired
	private UserRoleService userRoleService;

	@Autowired
	private RoleMenuServie roleMenuService;

	@Override
	public List<Role> findUserRole(String userName) {
		return this.roleMapper.findUserRole(userName);
	}

	@Override
	public List<Role> findAllRole(Role role) {
		try {
			User user = ShiroUtil.getCurrentUser();
			Example example = new Example(Role.class);
			Example.Criteria criteria=example.createCriteria();
			if (StringUtils.isNotBlank(role.getRoleName())) {
				criteria.andCondition("role_name=", role.getRoleName());
			}
			criteria.andCondition("role_level*group_id=role_level*", user.getOrganId());
			example.setOrderByClause("create_time");
			return this.selectByExample(example);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	@Override
	public Role findByName(String roleName) {
		Example example = new Example(Role.class);
		example.createCriteria().andCondition("lower(role_name)=", roleName.toLowerCase());
		List<Role> list = this.selectByExample(example);
		if (list.size() == 0) {
			return null;
		} else {
			return list.get(0);
		}
	}

	@Override
	@Transactional
	public void addRole(Role role, Long[] menuIds) {
		role.setGroupId(ShiroUtil.getCurrentUser().getOrganId());
		role.setCreateTime(new Date());
		this.save(role);
		setRoleMenus(role, menuIds);
	}

	private void setRoleMenus(Role role, Long[] menuIds) {
		if (null==menuIds)return;
		List<RoleMenu> menus=new ArrayList<>();
		for (Long menuId : menuIds) {
			RoleMenu rm = new RoleMenu();
			rm.setMenuId(menuId);
			rm.setRoleId(role.getRoleId());
			menus.add(rm);
			//this.roleMenuMapper.insert(rm);
		}
		roleMenuMapper.insertList(menus);
	}

	@Override
	@Transactional
	public void deleteRoles(String roleIds) {
		List<String> list = Arrays.asList(roleIds.split(","));
		this.batchDelete(list, "roleId", Role.class);

		this.roleMenuService.deleteRoleMenusByRoleId(roleIds);
		this.userRoleService.deleteUserRolesByRoleId(roleIds);

	}

	@Override
	public RoleWithMenu findRoleWithMenus(Long roleId) {
		List<RoleWithMenu> list = this.roleMapper.findById(roleId);
		List<Long> menuList = new ArrayList<>();
		for (RoleWithMenu rwm : list) {
			menuList.add(rwm.getMenuId());
		}
		if (list.size() == 0) {
			return null;
		}
		RoleWithMenu roleWithMenu = list.get(0);
		roleWithMenu.setMenuIds(menuList);
		return roleWithMenu;
	}

	@Override
	public List<RoleWithMenu> findByMenuId(Long menuId) {
		return this.roleMapper.findByMenuId(menuId);
	}

	@Override
	@Transactional
	public void updateRole(Role role, Long[] menuIds) {
		role.setModifyTime(new Date());
		//role.setGroupId(ShiroUtil.getCurrentUser().getOrganId());
		Role roleold= roleMapper.selectByPrimaryKey(role.getRoleId());
		if (roleold==null){
			throw new BusinessException("角色不存在");
		}
		if (!ShiroUtil.getCurrentUser().getOrganId().equals(roleold.getGroupId())){
			throw new BusinessException("只能修改本局创建角色，该角色无法修改");
		}
		role.setGroupId(ShiroUtil.getCurrentUser().getOrganId());
		this.updateNotNull(role);
		Example example = new Example(RoleMenu.class);
		example.createCriteria().andCondition("role_id=", role.getRoleId());
		this.roleMenuMapper.deleteByExample(example);
		setRoleMenus(role, menuIds);
	}

}

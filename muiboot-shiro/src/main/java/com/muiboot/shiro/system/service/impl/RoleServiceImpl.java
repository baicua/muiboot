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
import org.apache.commons.collections.CollectionUtils;
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
		roleMenuMapper.insertByBatch(menus);
	}

	@Override
	@Transactional
	public void deleteRoles(String roleIds) {
		List<String> list = Arrays.asList(roleIds.split(","));
		Example example = new Example(Role.class);
		Example.Criteria criteria=example.createCriteria();
		criteria.andIn("roleId", list);
		Long organId = ShiroUtil.getCurrentUser().getOrganId();
		if (null!=organId){
			criteria.andEqualTo("groupId",organId);
		}
		List<Role> roles=this.selectByExample(example);
		if (CollectionUtils.isEmpty(roles)){
			throw new BusinessException("您没有这些角色的删除权限！");
		}
		list.clear();
		for (Role role:roles){
			list.add(role.getRoleId().toString());
		}
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

	@Override
	public void grant(Long[] userIds, Long[] roleIds) {
		if (null==userIds||null==roleIds){
			throw new BusinessException("授权用户或者角色不能为空！");
		}
		List<UserRole> userRoleList=new ArrayList<>();
		for (Long userId :userIds){
			for (Long roleId :roleIds){
				UserRole userRole=new UserRole(userId,roleId);
				List<UserRole> userRoles=userRoleService.findByEntity(userRole);
				if (CollectionUtils.isEmpty(userRoles)){
					userRoleList.add(userRole);
				}
			}
		}
		if (CollectionUtils.isNotEmpty(userRoleList)){
			userRoleService.insertList(userRoleList);
		}
	}

	@Override
	public void grantUser(Long roleId, Long userId) {
		if (null==roleId||null==userId){
			throw new BusinessException("授权失败，角色或者用户不能为空！");
		}
		UserRole userRole = new UserRole();
		userRole.setRoleId(roleId);
		userRole.setUserId(userId);
		List roleUsers=userRoleService.findByEntity(userRole);
		if (CollectionUtils.isNotEmpty(roleUsers)){
			throw new BusinessException("授权失败，该用户已授权选择的角色！");
		}
		userRoleService.save(userRole);
		//UserRole userRole = new UserRole();
	}

	@Override
	public void revokeUser(Long roleId, Long userId) {
		if (null==roleId||null==userId){
			throw new BusinessException("权限回收失败，角色或者用户不能为空！");
		}
		UserRole userRole = new UserRole();
		userRole.setRoleId(roleId);
		userRole.setUserId(userId);
		int hit=userRoleService.delete(userRole);
		if (hit==0){
			throw new BusinessException("权限回收失败，没有找到需要删除的权限！");
		}
	}


}

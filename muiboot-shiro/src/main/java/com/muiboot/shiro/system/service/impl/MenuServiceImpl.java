package com.muiboot.shiro.system.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.muiboot.shiro.common.layer.LayerTree;
import com.muiboot.shiro.common.service.impl.BaseService;
import com.muiboot.shiro.system.dao.MenuMapper;
import com.muiboot.shiro.system.domain.Menu;
import com.muiboot.shiro.system.domain.Role;
import com.muiboot.shiro.system.service.RoleMenuServie;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.muiboot.shiro.common.util.TreeUtils;
import com.muiboot.shiro.system.service.MenuService;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service("menuService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class MenuServiceImpl extends BaseService<Menu> implements MenuService {

	@Autowired
	private MenuMapper menuMapper;

	@Autowired
	private RoleMenuServie roleMenuService;

	@Override
	public List<Menu> findUserPermissions(String userName) {
		return this.menuMapper.findUserPermissions(userName);
	}

	@Override
	public List<Menu> findUserMenus(String userName) {
		return this.menuMapper.findUserMenus(userName);
	}

	@Override
	public List<Menu> findAllMenus(Menu menu) {
		try {
			Example example = new Example(Menu.class);
			Criteria criteria = example.createCriteria();
			if (StringUtils.isNotBlank(menu.getMenuName())) {
				criteria.andCondition("menu_name=", menu.getMenuName());
			}
			if (StringUtils.isNotBlank(menu.getType())) {
				criteria.andCondition("type=", Long.valueOf(menu.getType()));
			}
			example.setOrderByClause("menu_id");
			return this.selectByExample(example);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	@Override
	public List<Menu> findAllPermissions(Menu menu) {
		Example example = new Example(Menu.class);
		example.createCriteria().andCondition("type =", 1).andEqualTo("parentId", menu.getMenuId());
		example.setOrderByClause("create_time");
		List<Menu> menus = this.selectByExample(example);
		return menus;
	}

	@Override
	public List<Role> findAllRoles(Menu menu) {
		return null;
	}

	@Override
	public LayerTree<Menu> getMenuButtonTree() {
		List<LayerTree<Menu>> trees = new ArrayList<>();
		List<Menu> menus = this.findAllMenus(new Menu());
		buildTrees(trees, menus);
		return TreeUtils.build(trees);
	}

	@Override
	public LayerTree<Menu> getMenuTree() {
		List<LayerTree<Menu>> trees = new ArrayList<>();
		Example example = new Example(Menu.class);
		example.createCriteria().andCondition("type =", 0);
		example.setOrderByClause("create_time");
		List<Menu> menus = this.selectByExample(example);
		buildTrees(trees, menus);
		return TreeUtils.build(trees);
	}

	private void buildTrees(List<LayerTree<Menu>> trees, List<Menu> menus) {
		for (Menu menu : menus) {
			LayerTree<Menu> tree = new LayerTree<>();
			tree.setId(menu.getMenuId().toString());
			tree.setParentId(menu.getParentId().toString());
			tree.setName(menu.getMenuName());
			tree.setIcon(menu.getIcon());
			//tree.setHref(menu.getUrl());
			trees.add(tree);
		}
	}

	@Override
	public LayerTree<Menu> getUserMenu(String userName) {
		List<LayerTree<Menu>> trees = new ArrayList<>();
		List<Menu> menus = this.findUserMenus(userName);
		for (Menu menu : menus) {
			LayerTree<Menu> tree = new LayerTree<>();
			tree.setId(menu.getMenuId().toString());
			tree.setParentId(menu.getParentId().toString());
			tree.setName(menu.getMenuName());
			tree.setIcon(menu.getIcon());
			tree.setHref(menu.getUrl());
			trees.add(tree);
		}
		return TreeUtils.build(trees);
	}

	@Override
	public Menu findByNameAndType(String menuName, String type) {
		Example example = new Example(Menu.class);
		example.createCriteria().andCondition("lower(menu_name)=", menuName.toLowerCase()).andEqualTo("type",
				Long.valueOf(type));
		List<Menu> list = this.selectByExample(example);
		if (list.size() == 0) {
			return null;
		} else {
			return list.get(0);
		}
	}

	@Override
	@Transactional
	public void addMenu(Menu menu) {
		menu.setCreateTime(new Date());
		if (menu.getParentId() == null)
			menu.setParentId(0L);
		this.save(menu);
	}

	@Override
	@Transactional
	public void deleteMeuns(String menuIds) {
		List<String> list = Arrays.asList(menuIds.split(","));
		this.batchDelete(list, "menuId", Menu.class);
		this.roleMenuService.deleteRoleMenusByMenuId(menuIds);
		this.menuMapper.changeToTop(list);
	}

	@Override
	public Menu findById(Long menuId) {
		return this.selectByKey(menuId);
	}

	@Override
	@Transactional
	public void updateMenu(Menu menu) {
		menu.setModifyTime(new Date());
		if (menu.getParentId() == null)
			menu.setParentId(0L);
		this.updateNotNull(menu);
	}

}

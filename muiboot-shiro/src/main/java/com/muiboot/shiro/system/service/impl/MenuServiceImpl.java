package com.muiboot.shiro.system.service.impl;

import java.util.*;
import java.util.concurrent.*;

import com.muiboot.core.common.exception.BusinessException;
import com.muiboot.core.common.layer.LayerTree;
import com.muiboot.core.common.service.impl.BaseService;
import com.muiboot.shiro.common.util.ShiroUtil;
import com.muiboot.core.common.util.exec.ExecutorsUtil;
import com.muiboot.shiro.system.dao.MenuMapper;
import com.muiboot.shiro.system.domain.Menu;
import com.muiboot.shiro.system.domain.Role;
import com.muiboot.shiro.system.domain.RoleWithMenu;
import com.muiboot.shiro.system.domain.User;
import com.muiboot.shiro.system.service.RoleMenuServie;
import com.muiboot.shiro.system.service.RoleService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections.SetUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.muiboot.core.common.util.TreeUtils;
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

	@Autowired
	private RoleService roleService;

	ExecutorService exeService= ExecutorsUtil.getInstance().getMultilThreadExecutor();

	@Override
	public List<Menu> findUserPermissions(String userName) {
		if (StringUtils.isBlank(userName)){
			throw new BusinessException("用户名不能为空！");
		}
		if (User.SUPPER_USER.equals(userName)){
			Example example = new Example(Menu.class);
			example.createCriteria().andIsNotNull("perms").andCondition("length(perms)>0");
			example.orderBy("orderNum");
			return menuMapper.selectByExample(example);
		}
		return this.menuMapper.findUserPermissions(userName);
	}

	@Override
	public List<Menu> findUserMenus(String userName) {
		if(StringUtils.isBlank(userName)){
			throw new BusinessException("用户名不能为空！");
		}
		List<Menu> menus=null;
		if(User.SUPPER_USER.equals(userName)){
			Example example = new Example(Menu.class);
			example.createCriteria().andEqualTo("type",Menu.TYPE_MENU);
			example.orderBy("orderNum");
			menus=menuMapper.selectByExample(example);
		}else{
			menus=this.menuMapper.findUserMenus(userName);
		}
		return menus;
	}

	@Override
	public List<Menu> findAllMenus(Menu menu) {
		Example example = new Example(Menu.class);
		Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(menu.getMenuName())) {
			criteria.andEqualTo("menuName",menu.getMenuName());
		}
		if (StringUtils.isNotBlank(menu.getType())) {
			criteria.andEqualTo("type",menu.getType());
		}
		example.orderBy("orderNum");
		return this.selectByExample(example);
	}

	@Override
	public List<Menu> findAllPermissions(Menu menu) {
		Example example = new Example(Menu.class);
		example.createCriteria().andCondition("type =", 1).andEqualTo("parentId", menu.getMenuId());
		example.setOrderByClause("create_time");
		List<Menu> menus = this.mapper.selectByExample(example);
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
		example.orderBy("orderNum");
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
	@Cacheable(value="sessionCache",key="#userName")
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
		example.createCriteria().andCondition("menu_name=", menuName).andEqualTo("type",
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
	public Map findMenuDetail(Long menuId) {
		Future<Menu> menuFuture=exeService.submit(new Callable<Menu>() {
			@Override
			public Menu call() throws Exception {
				return findById(menuId);
			}
		});
		Future<List<Menu>> permissionsuFuture=exeService.submit(new Callable<List<Menu>>() {
			@Override
			public List<Menu> call() throws Exception {
				Menu menuParam = new Menu();
				menuParam.setMenuId(menuId);
				return findAllPermissions(menuParam);
			}
		});
		Future<List<RoleWithMenu>> rolesuFuture=exeService.submit(new Callable<List<RoleWithMenu>>() {
			@Override
			public List<RoleWithMenu> call() throws Exception {
				return roleService.findByMenuId(menuId);
			}
		});
		Menu menu= null;
		List<Menu> permissions=null;
		List<RoleWithMenu> roles=null;
		try {
			menu = menuFuture.get(3, TimeUnit.SECONDS);
			permissions=permissionsuFuture.get(3, TimeUnit.SECONDS);
			roles=rolesuFuture.get(3, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			throw new BusinessException("查询异常中断！");
		} catch (ExecutionException e) {
			throw new BusinessException("查询处理异常！");
		} catch (TimeoutException e) {
			throw new BusinessException("查询数据超时！");
		}
		Map res =new HashMap();
		res.put("menu",menu);
		res.put("roles",roles);
		res.put("permissions",permissions);
		return res;
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

	@Override
	public LayerTree<Menu> getAuthList(Long roleId) {
		String userName = ShiroUtil.getCurrentUser().getUsername();
		List<Menu> auths =null;
		if(User.SUPPER_USER.equals(userName)){
			auths=menuMapper.selectAll();
		}else{
			auths = this.menuMapper.findUserAuths(userName);
		}
		List<Menu> selects =null;
		Set<Long> selectsMenuIds=new HashSet<>();
		if (null!=roleId){
			selects = this.menuMapper.findByRole(roleId);
			if (CollectionUtils.isNotEmpty(selects)){
				for (Menu m:selects){
					selectsMenuIds.add(m.getMenuId());
				}
			}
		}
		List<LayerTree<Menu>> trees = new ArrayList<>();
		for (Menu menu : auths) {
			LayerTree<Menu> tree = new LayerTree<>();
			tree.setId(menu.getMenuId().toString());
			tree.setParentId(menu.getParentId().toString());
			tree.setName(menu.getMenuName());
			tree.setIcon(menu.getIcon());
			tree.setHref(menu.getUrl());
			if (selectsMenuIds.contains(menu.getMenuId())){
				tree.setChecked(Boolean.TRUE);
			}
			if (Menu.TYPE_BUTTON.equals(menu.getType())){
				tree.setLevel("attribute");
			}
			trees.add(tree);
		}
		return TreeUtils.build(trees);
	}
}

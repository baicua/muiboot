package com.muiboot.shiro.common.shiro;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.muiboot.shiro.system.domain.Menu;
import com.muiboot.shiro.system.domain.Role;
import com.muiboot.shiro.system.domain.User;
import com.muiboot.shiro.system.service.RoleService;
import com.muiboot.shiro.system.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.muiboot.shiro.system.service.MenuService;
import org.springframework.context.annotation.Lazy;

/**
 * 自定义实现 ShiroRealm，包含认证和授权两大模块
 * @author MrBird
 */
public class ShiroRealm extends AuthorizingRealm {

	@Autowired
	@Lazy
	private UserService userService;
	@Autowired
	@Lazy
	private RoleService roleService;
	@Autowired
	@Lazy
	private MenuService menuService;

	/**
	 * 授权模块，获取用户角色和权限
	 * @param principal principal
	 * @return AuthorizationInfo 权限信息
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		String userName = user.getUsername();

		SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

		// 获取用户角色集
		List<Role> roleList = this.roleService.findUserRole(userName);
		Set<String> roleSet = new HashSet<>();
		for (Role r : roleList) {
			roleSet.add(r.getRoleName());
		}
		simpleAuthorizationInfo.setRoles(roleSet);

		// 获取用户权限集
		List<Menu> permissionList = this.menuService.findUserPermissions(userName);
		Set<String> permissionSet = new HashSet<>();
		for (Menu m : permissionList) {
			permissionSet.add(m.getPerms());
		}
		simpleAuthorizationInfo.setStringPermissions(permissionSet);
		return simpleAuthorizationInfo;
	}

	/**
	 * 用户认证
	 * @param token AuthenticationToken 身份认证 token
	 * @return AuthenticationInfo 身份认证信息
	 * @throws AuthenticationException 认证相关异常
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

		// 获取用户输入的用户名和密码
		String userName = (String) token.getPrincipal();
		String password = new String((char[]) token.getCredentials());

		// 通过用户名到数据库查询用户信息
		User user = this.userService.findByName(userName);

		if (user == null) {
			throw new UnknownAccountException("用户名或密码错误！");
		}
		if (!password.equals(user.getPassword())) {
			throw new IncorrectCredentialsException("用户名或密码错误！");
		}
		if (User.STATUS_LOCK.equals(user.getStatus())) {
			throw new LockedAccountException("账号已被锁定,请联系管理员！");
		}
		return new SimpleAuthenticationInfo(user, password, getName());
	}

	/**
	 * 清除所有用户的缓存
	 */
	public void clearAuthorizationInfoCache() {
		Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
		if(cache!=null) {
			cache.clear();
		}
	}

	/**
	 * 清除指定用户的缓存
	 * @param user
	 */
	private void clearAuthorizationInfoCache(User user) {
		Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
		cache.remove(user.getUserId());
	}

}

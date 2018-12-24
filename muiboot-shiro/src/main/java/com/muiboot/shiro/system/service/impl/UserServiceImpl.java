package com.muiboot.shiro.system.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.muiboot.core.exception.BusinessException;
import com.muiboot.core.service.impl.BaseService;
import com.muiboot.core.util.MD5Utils;
import com.muiboot.shiro.system.common.PropertiesUtil;
import com.muiboot.shiro.system.common.SysConstant;
import com.muiboot.shiro.system.dao.UserMapper;
import com.muiboot.shiro.system.entity.SysGroup;
import com.muiboot.shiro.system.entity.User;
import com.muiboot.shiro.system.entity.UserRole;
import com.muiboot.shiro.system.entity.UserWithRole;
import com.muiboot.shiro.system.service.GroupService;
import com.muiboot.shiro.system.service.UserRoleService;
import com.muiboot.shiro.system.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.muiboot.shiro.system.dao.UserRoleMapper;
import tk.mybatis.mapper.entity.Example;

@Service("userService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserServiceImpl extends BaseService<User> implements UserService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private UserRoleMapper userRoleMapper;

	@Autowired
	private UserRoleService userRoleService;

	@Autowired
	private GroupService groupService;

	@Override
	public User findByName(String userName) {
		Example example = new Example(User.class);
		example.createCriteria().andCondition("username=", userName.toLowerCase());
		List<User> list = this.selectByExample(example);
		if (list.size() == 0) {
			return null;
		} else {
			User user =list.get(0);
			SysGroup group = groupService.findById(user.getGroupId());
			user.setGroupName(group.getGroupName());
			user.setOrganId(group.getParentId());
			return user;
		}
	}

	@Override
	public List<User> findUserWithDept(User user) {
		try {
			return this.userMapper.findUserWithDept(user);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	@Override
	@Transactional
	public void registUser(User user) {
		user.setCrateTime(new Date());
		user.setTheme(User.DEFAULT_THEME);
		user.setAvatar(User.DEFAULT_AVATAR);
		user.setSsex(User.SEX_UNKNOW);
		user.setPassword(MD5Utils.encrypt(user.getUsername(), user.getPassword()));
		this.save(user);
		UserRole ur = new UserRole();
		ur.setUserId(user.getUserId());
		ur.setRoleId(3L);
		this.userRoleMapper.insert(ur);
	}

	@Override
	@Transactional
	public void updateTheme(String theme, String userName) {
		Example example = new Example(User.class);
		example.createCriteria().andCondition("username=", userName);
		User user = new User();
		user.setTheme(theme);
		this.userMapper.updateByExampleSelective(user, example);
	}

	@Override
	@Transactional
	public void addUser(User user, Long[] roles) {
		user.setCrateTime(new Date());
		user.setTheme(User.DEFAULT_THEME);
		user.setAvatar(User.DEFAULT_AVATAR);
		if (StringUtils.isBlank(user.getPassword())){
			if (StringUtils.isNotBlank(user.getMobile())){
				user.setPassword(user.getMobile());
			}else {
				user.setPassword(StringUtils.defaultIfBlank(PropertiesUtil.get(SysConstant.INIT_USER_PWD),"111111"));
			}
		}
		user.setPassword(MD5Utils.encrypt(user.getUsername(),user.getPassword()));
		User u=this.findByName(user.getUsername());
		if (null!=u){
			throw new BusinessException(String.format("用户名【%s】已经存在！",u.getUsername()));
		}
		this.save(user);
		if (roles==null){
			Long baseRole = NumberUtils.toLong(PropertiesUtil.get(SysConstant.BASE_ROLE_KEY),0);
			if (0==baseRole.longValue()){
				throw new BusinessException("基础角色不存在，无法添加用户，请下配置基础角色！");
			}
			roles=new Long[]{baseRole};
		}
		setUserRoles(user, roles);
	}

	private void setUserRoles(User user, Long[] roles) {
		if (null==roles)return;
		for (Long roleId : roles) {
			UserRole ur = new UserRole();
			ur.setUserId(user.getUserId());
			ur.setRoleId(roleId);
			this.userRoleMapper.insert(ur);
		}
	}

	@Override
	@Transactional
	public void updateUser(User user, Long[] roles) {
		user.setPassword(null);
		user.setUsername(null);
		user.setModifyTime(new Date());
		this.updateNotNull(user);
		//Example example = new Example(UserRole.class);
		//example.createCriteria().andCondition("user_id=", user.getUserId());
		//this.userRoleMapper.deleteByExample(example);
		setUserRoles(user, roles);
	}

	@Override
	@Transactional
	public void deleteUsers(String userIds) {
		List<String> list = Arrays.asList(userIds.split(","));
		this.batchDelete(list, "userId", User.class);

		this.userRoleService.deleteUserRolesByUserId(userIds);
	}

	@Override
	@Transactional
	public void updateLoginTime(String userName) {
		Example example = new Example(User.class);
		example.createCriteria().andCondition("lower(username)=", userName.toLowerCase());
		User user = new User();
		user.setLastLoginTime(new Date());
		this.userMapper.updateByExampleSelective(user, example);
	}

	@Override
	@Transactional
	public void updatePassword(String password) {
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		Example example = new Example(User.class);
		example.createCriteria().andCondition("username=", user.getUsername());
		String newPassword = MD5Utils.encrypt(user.getUsername().toLowerCase(), password);
		user.setPassword(newPassword);
		this.userMapper.updateByExampleSelective(user, example);
	}

	@Override
	public UserWithRole findById(Long userId) {
		List<UserWithRole> list = this.userMapper.findUserWithRole(userId);
		List<Long> roleList = new ArrayList<>();
		for (UserWithRole uwr : list) {
			roleList.add(uwr.getRoleId());
		}
		if (list.size() == 0) {
			return null;
		}
		UserWithRole userWithRole = list.get(0);
		userWithRole.setRoleIds(roleList);
		return userWithRole;
	}

	@Override
	public User findUserProfile(User user) {
		return this.userMapper.findUserProfile(user);
	}

	@Override
	@Transactional
	public void updateUserProfile(User user) {
		user.setUsername(null);
		user.setPassword(null);
		user.setStatus(null);
		this.updateNotNull(user);
	}

	@Override
	public List<UserWithRole> findUserByRole(UserWithRole userRole) {
		return this.userMapper.findUserByRole(userRole);
	}

}

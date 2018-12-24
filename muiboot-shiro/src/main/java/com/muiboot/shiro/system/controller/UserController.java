package com.muiboot.shiro.system.controller;

import java.util.List;
import java.util.Map;

import com.muiboot.shiro.common.util.ShiroUtil;
import com.muiboot.shiro.system.entity.UserWithRole;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import com.muiboot.core.annotation.Log;
import com.muiboot.shiro.common.controller.BaseController;
import com.muiboot.core.domain.QueryRequest;
import com.muiboot.core.domain.ResponseBo;
import com.muiboot.core.util.FileUtils;
import com.muiboot.core.util.MD5Utils;
import com.muiboot.shiro.system.entity.User;
import com.muiboot.shiro.system.service.UserService;

@Controller
public class UserController extends BaseController {

	@Autowired
	private UserService userService;

	private static final String ON = "on";

	@RequestMapping("user/checkUserName")
	@ResponseBody
	public boolean checkUserName(String username, String oldusername) {
		if (StringUtils.isNotBlank(oldusername) && username.equalsIgnoreCase(oldusername)) {
			return true;
		}
		User result = this.userService.findByName(username);
		return result == null;
	}

	@RequestMapping("user/getUser")
	@ResponseBody
	public ResponseBo getUser(Long userId) {
		try {
			User user = this.userService.selectByKey(userId);
			return ResponseBo.ok(user);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseBo.error("获取用户信息失败，请联系网站管理员！");
		}
	}
	@RequestMapping("user/list")
	@ResponseBody
	public Map<String, Object> userList(QueryRequest request, User user) {
		PageHelper.startPage(request.getPage(), request.getLimit());
		if (null==user.getOrganId()){
			boolean allPermit=ShiroUtil.getSubject().isPermitted("user:all");//有全局权限
			if(!allPermit){
				user.setOrganId(ShiroUtil.getCurrentUser().getOrganId());
			}
		}
		List<User> list = this.userService.findUserWithDept(user);
		PageInfo<User> pageInfo = new PageInfo<>(list);
		return getDataTable(pageInfo);
	}

	@RequestMapping("user/user-role")
	@ResponseBody
	public Map<String, Object> userList(QueryRequest request, UserWithRole userRole) {
		if (userRole.getRoleId()==null){
			return null;
		}
		if (null==userRole.getOrganId()){
			boolean allPermit=ShiroUtil.getSubject().isPermitted("user:all");//有全局权限
			if(!allPermit){
				userRole.setOrganId(ShiroUtil.getCurrentUser().getOrganId());
			}
		}
		PageHelper.startPage(request.getPage(), request.getLimit());
		List<UserWithRole> list = this.userService.findUserByRole(userRole);
		PageInfo<UserWithRole> pageInfo = new PageInfo<>(list);
		return getDataTable(pageInfo);
	}

	@RequestMapping("user/excel")
	@ResponseBody
	public ResponseBo userExcel(User user) {
		try {
			List<User> list = this.userService.findUserWithDept(user);
			return FileUtils.createExcelByPOIKit("用户表", list, User.class);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseBo.error("导出Excel失败，请联系网站管理员！");
		}
	}

	@RequestMapping("user/csv")
	@ResponseBody
	public ResponseBo userCsv(User user) {
		try {
			List<User> list = this.userService.findUserWithDept(user);
			return FileUtils.createCsv("用户表", list, User.class);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseBo.error("导出Csv失败，请联系网站管理员！");
		}
	}

	@RequestMapping("user/regist")
	@ResponseBody
	public ResponseBo regist(User user) {
		try {
			User result = this.userService.findByName(user.getUsername());
			if (result != null) {
				return ResponseBo.warn("该用户名已被使用！");
			}
			this.userService.registUser(user);
			return ResponseBo.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseBo.error("注册失败，请联系网站管理员！");
		}
	}

	@RequestMapping("user/theme")
	@ResponseBody
	public ResponseBo updateTheme(User user) {
		try {
			this.userService.updateTheme(user.getTheme(), user.getUsername());
			return ResponseBo.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseBo.error();
		}
	}

	@Log("新增用户")
	@RequiresPermissions("user:add")
	@RequestMapping("user/add")
	@ResponseBody
	public ResponseBo addUser(User user, Long[] roles) {
		if (StringUtils.isBlank(user.getSsex())){
			user.setSsex(User.SEX_FEMALE);
		}
		if (StringUtils.isBlank(user.getStatus())){
			user.setStatus(User.STATUS_LOCK);
		}
		this.userService.addUser(user, roles);
		return ResponseBo.ok("新增用户成功！");
	}

	@Log("修改用户")
	@RequiresPermissions("user:update")
	@RequestMapping("user/update")
	@ResponseBody
	public ResponseBo updateUser(User user, Long[] rolesSelect) {
		try {
			if (StringUtils.isBlank(user.getSsex())){
				user.setSsex(User.SEX_FEMALE);
			}
			if (StringUtils.isBlank(user.getStatus())){
				user.setStatus(User.STATUS_LOCK);
			}
			this.userService.updateUser(user, rolesSelect);
			return ResponseBo.ok("修改用户成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseBo.error("修改用户失败，请联系网站管理员！");
		}
	}

	@Log("删除用户")
	@RequiresPermissions("user:delete")
	@RequestMapping("user/delete")
	@ResponseBody
	public ResponseBo deleteUsers(String ids) {
		try {
			this.userService.deleteUsers(ids);
			return ResponseBo.ok("删除用户成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseBo.error("删除用户失败，请联系网站管理员！");
		}
	}

	@RequestMapping("user/checkPassword")
	@ResponseBody
	public boolean checkPassword(String password) {
		User user = getCurrentUser();
		String encrypt = MD5Utils.encrypt(user.getUsername().toLowerCase(), password);
		return user.getPassword().equals(encrypt);
	}
	@Log("修改密码")
	@RequestMapping("user/updatePwd")
	@ResponseBody
	public ResponseBo updatePwd(String password,String newPassword,String confirmPassword) {
		if (!newPassword.equals(confirmPassword)){
			return ResponseBo.error("两次密码输入不一致，请重新输入！");
		}
		User user = ShiroUtil.getCurrentUser();
		String oldPassword = MD5Utils.encrypt(user.getUsername().toLowerCase(), password);
		if (!oldPassword.equals(user.getPassword())){
			return ResponseBo.error("密码验证失败，请重新输入！");
		}
		this.userService.updatePassword(newPassword);
		return ResponseBo.ok("更改密码成功！");
	}

	@Log("修改个人信息")
	@RequestMapping("user/updateProfile")
	@ResponseBody
	public ResponseBo updateProfile(User user) {
		User sessionUser = ShiroUtil.getCurrentUser();
		if (!sessionUser.getUserId().equals(user.getUserId())){
			return ResponseBo.error("只能修改登录用户的个人信息！");
		}
		if (StringUtils.isBlank(user.getSsex())){
			user.setSsex(User.SEX_FEMALE);
		}
		userService.updateUserProfile(user);
		return ResponseBo.ok("修改个人信息成功！");
	}

	@RequestMapping("user/profile")
	public String profileIndex(Model model) {
		User user = super.getCurrentUser();
		user = this.userService.findUserProfile(user);
		String ssex = user.getSsex();
		if (User.SEX_MALE.equals(ssex)) {
			user.setSsex("性别：男");
		} else if (User.SEX_FEMALE.equals(ssex)) {
			user.setSsex("性别：女");
		} else {
			user.setSsex("性别：保密");
		}
		model.addAttribute("user", user);
		return "system/user/profile";
	}

}

package com.muiboot.shiro.common.controller;

import java.util.HashMap;
import java.util.Map;

import com.muiboot.core.web.BaseController;
import com.muiboot.shiro.system.entity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.github.pagehelper.PageInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* <p>Description: 控制层基类</p>
* @version 1.0 2018/10/12
* @author jin
*/
public  abstract class ShiroBaseController extends BaseController {
	public static Subject getSubject() {
		return SecurityUtils.getSubject();
	}

	protected User getCurrentUser() {
		return (User) getSubject().getPrincipal();
	}

	protected Session getSession() {
		return getSubject().getSession();
	}

	protected Session getSession(Boolean flag) {
		return getSubject().getSession(flag);
	}

	protected void login(AuthenticationToken token) {
		getSubject().login(token);
	}
}

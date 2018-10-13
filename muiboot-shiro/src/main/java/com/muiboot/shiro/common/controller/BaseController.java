package com.muiboot.shiro.common.controller;

import java.util.HashMap;
import java.util.Map;

import com.muiboot.shiro.system.controller.SysConstant;
import com.muiboot.shiro.system.domain.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.github.pagehelper.PageInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
* <p>Description: 控制层基类</p>
* @version 1.0 2018/10/12
* @author jin
*/
@Controller
public  abstract class BaseController {
	protected  final Logger logger = LoggerFactory.getLogger(this.getClass());
	@ModelAttribute
	public void initBinder(Model model, HttpServletRequest request, HttpServletResponse response){
		response.setHeader("Cache-Control", "max-age="+ SysConstant.DATA_MAX_AGE);
	}

	protected Map<String, Object> getDataTable(PageInfo<?> pageInfo) {
		Map<String, Object> rspData = new HashMap<>();
		rspData.put("code", 0);
		rspData.put("data", pageInfo.getList());
		rspData.put("count", pageInfo.getTotal());
		return rspData;
	}

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

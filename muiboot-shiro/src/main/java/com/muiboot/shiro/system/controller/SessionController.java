package com.muiboot.shiro.system.controller;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.muiboot.shiro.common.controller.BaseController;
import com.muiboot.core.common.domain.QueryRequest;
import com.muiboot.core.common.domain.ResponseBo;
import com.muiboot.shiro.system.service.MenuService;
import com.muiboot.shiro.system.service.SessionService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.muiboot.shiro.system.domain.UserOnline;


@Controller
public class SessionController extends BaseController {
	
	@Autowired
    SessionService sessionService;

	@Autowired
	private MenuService menuService;

	@ResponseBody
	@RequestMapping("session/list")
	public Map<String, Object> list(QueryRequest request) {
		PageHelper.startPage(request.getPage(), request.getLimit());
		List<UserOnline> list = sessionService.list();
		PageInfo<UserOnline> pageInfo = new PageInfo<>(list);
		return getDataTable(pageInfo);
	}

	@RequestMapping("session/getUserMenu")
	@ResponseBody
	public ResponseBo getUserMenu(String userName) {
		return ResponseBo.ok(this.menuService.getUserMenu(userName));
	}

	@RequestMapping("session/getAuthList")
	@ResponseBody
	public ResponseBo getAuthList(Long roleId) {
		return ResponseBo.ok(this.menuService.getAuthList(roleId));
	}

	@ResponseBody
	@RequiresPermissions("user:kickout")
	@RequestMapping("session/forceLogout")
	public ResponseBo forceLogout(String id) {
		try {
			sessionService.forceLogout(id);
			return ResponseBo.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseBo.error("踢出用户失败");
		}

	}
}

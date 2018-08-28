package com.muiboot.shiro.system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.muiboot.shiro.common.annotation.Log;
import com.muiboot.shiro.common.domain.ResponseBo;
import com.muiboot.shiro.system.service.MenuService;
import com.muiboot.shiro.system.service.SessionService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.muiboot.shiro.system.domain.UserOnline;


@Controller
public class SessionController {
	
	@Autowired
    SessionService sessionService;

	@Autowired
	private MenuService menuService;
	
	@Log("获取在线用户信息")
	@RequestMapping("session")
	@RequiresPermissions("session:list")
	public String online() {
		return "system/monitor/online";
	}

	@ResponseBody
	@RequestMapping("session/list")
	public Map<String, Object> list() {
		List<UserOnline> list = sessionService.list();
		Map<String, Object> rspData = new HashMap<>();
		rspData.put("rows", list);
		rspData.put("total", list.size());
		return rspData;
	}

	@RequestMapping("menu/getUserMenu")
	@ResponseBody
	public ResponseBo getUserMenu(String userName) {
		return ResponseBo.ok(this.menuService.getUserMenu(userName));
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

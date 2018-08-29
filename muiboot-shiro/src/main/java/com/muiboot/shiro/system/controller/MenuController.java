package com.muiboot.shiro.system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.muiboot.shiro.common.domain.ResponseBo;
import com.muiboot.shiro.common.layer.LayerTree;
import com.muiboot.shiro.common.util.FileUtils;
import com.muiboot.shiro.system.domain.Role;
import com.muiboot.shiro.system.domain.RoleWithMenu;
import com.muiboot.shiro.system.service.RoleService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.muiboot.shiro.common.annotation.Log;
import com.muiboot.shiro.common.controller.BaseController;
import com.muiboot.shiro.system.domain.Menu;
import com.muiboot.shiro.system.service.MenuService;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequiresPermissions("menu:list")
public class MenuController extends BaseController {
	@Autowired
	private MenuService menuService;

	@RequestMapping("menu/getMenu")
	@ResponseBody
	public ResponseBo getMenu(HttpServletResponse response, Long menuId) throws Exception {
		response.setHeader("Cache-Control", "max-age=5");//缓存5秒
		return ResponseBo.ok(this.menuService.findById(menuId));
	}

	@RequestMapping("menu/getMenuDetail")
	@ResponseBody
	public ResponseBo getMenuDetail(HttpServletResponse response, Long menuId) throws Exception {
		response.setHeader("Cache-Control", "max-age=5");//缓存5秒
		return ResponseBo.ok(this.menuService.findMenuDetail(menuId));
	}

	@RequestMapping("menu/tree")
	@ResponseBody
	public ResponseBo getMenuTree(HttpServletResponse response)  throws Exception{
		response.setHeader("Cache-Control", "max-age=5");//缓存5秒
		return ResponseBo.ok(this.menuService.getMenuTree());
	}

	@RequestMapping("menu/excel")
	@ResponseBody
	public ResponseBo menuExcel(Menu menu)  throws Exception{
		return FileUtils.createExcelByPOIKit("菜单表", this.menuService.findAllMenus(menu), Menu.class);
	}

	@RequestMapping("menu/csv")
	@ResponseBody
	public ResponseBo menuCsv(Menu menu) throws Exception{
		try {
			List<Menu> list = this.menuService.findAllMenus(menu);
			return FileUtils.createCsv("菜单表", list, Menu.class);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseBo.error("导出Csv失败，请联系网站管理员！");
		}
	}
	@Log("新增菜单/按钮")
	@RequiresPermissions("menu:add")
	@RequestMapping("menu/add")
	@ResponseBody
	public ResponseBo addMenu(Menu menu)  throws Exception{
		String name;
		if (Menu.TYPE_MENU.equals(menu.getType()))
			name = "菜单";
		else
			name = "按钮";
		this.menuService.addMenu(menu);
		return ResponseBo.ok("新增" + name + "成功！");
	}

	@Log("删除菜单")
	@RequiresPermissions("menu:delete")
	@RequestMapping("menu/delete")
	@ResponseBody
	public ResponseBo deleteMenus(String ids)  throws Exception{
		this.menuService.deleteMeuns(ids);
		return ResponseBo.ok("删除成功！");
	}
	
	/*@Log("修改菜单/按钮")*/
	@RequiresPermissions("menu:update")
	@RequestMapping("menu/update")
	@ResponseBody
	public ResponseBo updateMenu(Menu menu) throws Exception{
		String name;
		if (Menu.TYPE_MENU.equals(menu.getType()))
			name = "菜单";
		else
			name = "按钮";
		this.menuService.updateMenu(menu);
		return ResponseBo.ok("修改" + name + "成功！");
	}
}

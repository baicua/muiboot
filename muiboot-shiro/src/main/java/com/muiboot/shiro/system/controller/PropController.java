package com.muiboot.shiro.system.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.muiboot.shiro.common.annotation.Log;
import com.muiboot.shiro.common.controller.BaseController;
import com.muiboot.shiro.common.domain.QueryRequest;
import com.muiboot.shiro.common.domain.ResponseBo;
import com.muiboot.shiro.system.common.PropertiesUtil;
import com.muiboot.shiro.system.domain.Properties;
import com.muiboot.shiro.system.service.PropertiesService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class PropController extends BaseController {
	@Autowired
	private PropertiesService propertiesService;
	@RequestMapping("prop/getProp")
	@ResponseBody
	public ResponseBo getProp(Long propId) {
		return ResponseBo.ok(propertiesService.selectByKey(propId));
	}
	@RequestMapping("prop/list")
	@ResponseBody
	public Map<String, Object> propList(QueryRequest request) {
		PageHelper.startPage(request.getPage(), request.getLimit());
		List<Properties> list=propertiesService.selectAll();
		PageInfo<Properties> pageInfo = new PageInfo<>(list);
		return getDataTable(pageInfo);
	}

	@Log("新增配置")
	@RequiresPermissions("prop:add")
	@RequestMapping("prop/add")
	@ResponseBody
	public ResponseBo addUser(Properties prop) {
		propertiesService.save(prop);
		PropertiesUtil.put(prop);
		return ResponseBo.ok("新增配置成功！");
	}

	@Log("修改配置")
	@RequiresPermissions("prop:update")
	@RequestMapping("prop/update")
	@ResponseBody
	public ResponseBo updateUser(Properties prop) {
		propertiesService.updateNotNull(prop);
		PropertiesUtil.put(prop);
		return ResponseBo.ok("修改配置成功！");
	}
	
}

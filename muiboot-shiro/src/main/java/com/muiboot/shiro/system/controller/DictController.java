package com.muiboot.shiro.system.controller;

import java.util.List;
import java.util.Map;

import com.muiboot.shiro.common.domain.QueryRequest;
import com.muiboot.shiro.common.domain.ResponseBo;
import com.muiboot.shiro.common.util.FileUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import com.muiboot.shiro.common.annotation.Log;
import com.muiboot.shiro.common.controller.BaseController;
import com.muiboot.shiro.system.domain.Dict;
import com.muiboot.shiro.system.service.DictService;

import javax.servlet.http.HttpServletResponse;

@Controller
public class DictController extends BaseController {

	@Autowired
	private DictService dictService;

	@RequestMapping("dict/tree")
	@ResponseBody
	public ResponseBo getDictTree(HttpServletResponse response)  throws Exception{
		response.setHeader("Cache-Control", "max-age=5");//缓存5秒
		return ResponseBo.ok(this.dictService.getDictTree());
	}
}

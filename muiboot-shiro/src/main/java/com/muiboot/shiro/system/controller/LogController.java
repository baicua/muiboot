package com.muiboot.shiro.system.controller;

import java.util.List;
import java.util.Map;

import com.muiboot.core.entity.QueryRequest;
import com.muiboot.core.entity.ResponseBo;
import com.muiboot.core.util.FileUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import com.muiboot.shiro.common.controller.BaseController;
import com.muiboot.shiro.system.entity.SysLog;
import com.muiboot.shiro.system.service.LogService;
/**
 * <p>Title: </p>
 * <p>Description: 日志</p>
 *
 * @author jin
 * @version 1.0 2018/9/18
 */
@Controller
public class LogController extends BaseController {

	@Autowired
	private LogService logService;

	@RequestMapping("log/list")
	@ResponseBody
	public Map<String, Object> logList(QueryRequest request, SysLog log) {
		PageHelper.startPage(request.getPage(), request.getLimit());
		List<SysLog> list = this.logService.findAllLogs(log);
		PageInfo<SysLog> pageInfo = new PageInfo<>(list);
		return getDataTable(pageInfo);
	}

	@RequestMapping("log/excel")
	@ResponseBody
	public ResponseBo logExcel(SysLog log) {
		try {
			List<SysLog> list = this.logService.findAllLogs(log);
			return FileUtils.createExcelByPOIKit("系统日志表", list, SysLog.class);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseBo.error("导出Excel失败，请联系网站管理员！");
		}
	}

	@RequestMapping("log/csv")
	@ResponseBody
	public ResponseBo logCsv(SysLog log){
		try {
			List<SysLog> list = this.logService.findAllLogs(log);
			return FileUtils.createCsv("系统日志表", list, SysLog.class);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseBo.error("导出Csv失败，请联系网站管理员！");
		}
	}
	
	@RequiresPermissions("log:delete")
	@RequestMapping("log/delete")
	@ResponseBody
	public ResponseBo deleteLogss(String ids) {
		try {
			this.logService.deleteLogs(ids);
			return ResponseBo.ok("删除日志成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseBo.error("删除日志失败，请联系网站管理员！");
		}
	}
}

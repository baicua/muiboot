package com.muiboot.activiti.contoller.instance;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.muiboot.activiti.model.process.ProcessInstanceVo;
import com.muiboot.activiti.service.instance.InstanceService;
import com.muiboot.core.common.domain.QueryRequest;
import com.muiboot.core.common.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 运行中流程管理
 */
@Controller
@RequestMapping(value = "/workflow/instance/pro/")
public class ProInstanceController extends BaseController {
 
	 @Autowired
	 private InstanceService instanceService;

	@RequestMapping("list")
	@ResponseBody
	public Map<String, Object> instanceList(QueryRequest request) {
		PageHelper.startPage(request.getPage(), request.getLimit());
		List<ProcessInstanceVo> list = instanceService.findByPage(request);
		PageInfo<ProcessInstanceVo> pageInfo = new PageInfo<>(list);
		return getDataTable(pageInfo);
	}
 
}

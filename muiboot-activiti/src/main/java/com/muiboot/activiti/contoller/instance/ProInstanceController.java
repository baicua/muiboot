package com.muiboot.activiti.contoller.instance;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.muiboot.activiti.active.group.UserEntity;
import com.muiboot.activiti.active.param.operation.StartParam;
import com.muiboot.activiti.model.process.ProcessInstanceVo;
import com.muiboot.activiti.service.definition.InstanceService;
import com.muiboot.activiti.service.runtime.RuntimeService;
import com.muiboot.core.domain.QueryRequest;
import com.muiboot.core.domain.ResponseBo;
import com.muiboot.core.web.BaseController;
import org.activiti.engine.runtime.ProcessInstance;
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

	@Autowired
	private RuntimeService runtimeService;

	@RequestMapping("list")
	@ResponseBody
	public Map<String, Object> instanceList(QueryRequest request) {
		PageHelper.startPage(request.getPage(), request.getLimit());
		List<ProcessInstanceVo> list = instanceService.findByPage(request);
		PageInfo<ProcessInstanceVo> pageInfo = new PageInfo<>(list);
		return getDataTable(pageInfo);
	}

	@RequestMapping("start")
	@ResponseBody
	public ResponseBo start() {
		StartParam param = new StartParam();
		UserEntity user=new UserEntity();
		user.setUserId("yangjin");
		param.setBusinessKey("11111111");
		param.setFlowKey("flowKey");
		param.setUser(user);
		//param.setVariable();
		ProcessInstance p=runtimeService.start(param);
		return ResponseBo.ok(p.toString());
	}


 
}

package com.muiboot.shiro.system.controller;

import java.util.List;
import java.util.Map;

import com.muiboot.core.annotation.Log;
import com.muiboot.shiro.common.controller.BaseController;
import com.muiboot.core.entity.ResponseBo;
import com.muiboot.core.entity.LayerTree;
import com.muiboot.core.util.FileUtils;
import com.muiboot.shiro.system.entity.SysGroup;
import com.muiboot.shiro.system.service.GroupService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GroupController extends BaseController{

	@Autowired
	private GroupService groupService;

	@RequestMapping("group/tree")
	@ResponseBody
	public ResponseBo getGroupTree(String groupName) {
		try {
			LayerTree<SysGroup> tree = this.groupService.getGroupTree(groupName);
			return ResponseBo.ok(tree);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseBo.error("获取部门列表失败！");
		}
	}

	@RequestMapping("group/getDeptByParent")
	@ResponseBody
	public ResponseBo getGroupDicByParent(Long parentId) {
		try {
			Map groups = this.groupService.getDeptByParent(parentId);
			return ResponseBo.ok(groups);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseBo.error("获取部门信息失败，请联系网站管理员！");
		}
	}

	@RequestMapping("group/getGroup")
	@ResponseBody
	public ResponseBo getGroup(Long groupId) {
		try {
			SysGroup group = this.groupService.findById(groupId);
			return ResponseBo.ok(group);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseBo.error("获取部门信息失败，请联系网站管理员！");
		}
	}

	@RequestMapping("group/getGroupDetail")
	@ResponseBody
	public ResponseBo getGroupDetail(Long groupId) {
		Map groupDetail = this.groupService.getGroupDetail(groupId);
		return ResponseBo.ok(groupDetail);
	}
	
	@RequestMapping("group/list")
	@ResponseBody
	public List<SysGroup> GroupList(SysGroup group) {
		return this.groupService.findAllGroups(group);
	}
	
	@RequestMapping("group/excel")
	@ResponseBody
	public ResponseBo GroupExcel(SysGroup group) {
		try {
			List<SysGroup> list = this.groupService.findAllGroups(group);
			return FileUtils.createExcelByPOIKit("机构表", list, SysGroup.class);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseBo.error("导出Excel失败，请联系网站管理员！");
		}
	}

	@RequestMapping("group/csv")
	@ResponseBody
	public ResponseBo GroupCsv(SysGroup group){
		try {
			List<SysGroup> list = this.groupService.findAllGroups(group);
			return FileUtils.createCsv("机构表", list, SysGroup.class);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseBo.error("导出Csv失败，请联系网站管理员！");
		}
	}

	@Log("新增组织机构")
	@RequiresPermissions("group:add")
	@RequestMapping("group/add")
	@ResponseBody
	public ResponseBo addGroup(SysGroup group) {
		try {
			if (StringUtils.isBlank(group.getValid())){
				group.setValid("0");
			}
			if (group.getParentId()==null){
				group.setParentId(0L);
			}
			this.groupService.addGroup(group);
			return ResponseBo.ok("新增组织机构成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseBo.error("新增组织机构失败，请联系网站管理员！");
		}
	}

	@Log("删除组织机构")
	@RequiresPermissions("group:delete")
	@RequestMapping("group/delete")
	@ResponseBody
	public ResponseBo deleteGroups(String ids) {
		try {
			this.groupService.deleteGroups(ids);
			return ResponseBo.ok("删除组织机构成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseBo.error("删除组织机构失败，请联系网站管理员！");
		}
	}
	
	@Log("修改组织机构")
	@RequiresPermissions("group:update")
	@RequestMapping("group/update")
	@ResponseBody
	public ResponseBo updateGroup(SysGroup group) {
			if (StringUtils.isBlank(group.getValid())){
				group.setValid("0");
			}
			if (group.getParentId()==null){
				group.setParentId(0L);
			}
			this.groupService.updateGroup(group);
			return ResponseBo.ok("修改组织机构成功！");

	}
}

package com.muiboot.shiro.system.controller;

import java.util.List;
import java.util.Map;

import com.muiboot.shiro.common.annotation.Log;
import com.muiboot.shiro.common.controller.BaseController;
import com.muiboot.shiro.common.domain.ResponseBo;
import com.muiboot.shiro.common.layer.LayerTree;
import com.muiboot.shiro.common.util.FileUtils;
import com.muiboot.shiro.system.domain.Organ;
import com.muiboot.shiro.system.service.OrganService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OrganController extends BaseController{

	@Autowired
	private OrganService organService;

	@RequestMapping("organ/tree")
	@ResponseBody
	public ResponseBo getOrganTree() {
		try {
			LayerTree<Organ> tree = this.organService.getOrganTree();
			return ResponseBo.ok(tree);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseBo.error("获取部门列表失败！");
		}
	}

	@RequestMapping("organ/getOrgan")
	@ResponseBody
	public ResponseBo getOrgan(Long organId) {
		try {
			Organ organ = this.organService.findById(organId);
			return ResponseBo.ok(organ);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseBo.error("获取部门信息失败，请联系网站管理员！");
		}
	}

	@RequestMapping("organ/getOrganDetail")
	@ResponseBody
	public ResponseBo getOrganDetail(Long organId) {
		Map organDetail = this.organService.getOrganDetail(organId);
		return ResponseBo.ok(organDetail);
	}
	
	@RequestMapping("organ/list")
	@ResponseBody
	public List<Organ> organList(Organ organ) {
		return this.organService.findAllOrgans(organ);
	}
	
	@RequestMapping("organ/excel")
	@ResponseBody
	public ResponseBo organExcel(Organ organ) {
		try {
			List<Organ> list = this.organService.findAllOrgans(organ);
			return FileUtils.createExcelByPOIKit("机构表", list, Organ.class);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseBo.error("导出Excel失败，请联系网站管理员！");
		}
	}

	@RequestMapping("organ/csv")
	@ResponseBody
	public ResponseBo organCsv(Organ organ){
		try {
			List<Organ> list = this.organService.findAllOrgans(organ);
			return FileUtils.createCsv("机构表", list, Organ.class);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseBo.error("导出Csv失败，请联系网站管理员！");
		}
	}

	@Log("新增组织机构")
	@RequiresPermissions("organ:add")
	@RequestMapping("organ/add")
	@ResponseBody
	public ResponseBo addOrgan(Organ organ) {
		try {
			if (StringUtils.isBlank(organ.getValid())){
				organ.setValid("0");
			}
			if (organ.getParentId()==null){
				organ.setParentId(0L);
			}
			this.organService.addOrgan(organ);
			return ResponseBo.ok("新增组织机构成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseBo.error("新增组织机构失败，请联系网站管理员！");
		}
	}

	@Log("删除组织机构")
	@RequiresPermissions("organ:delete")
	@RequestMapping("organ/delete")
	@ResponseBody
	public ResponseBo deleteOrgans(String ids) {
		try {
			this.organService.deleteOrgans(ids);
			return ResponseBo.ok("删除组织机构成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseBo.error("删除组织机构失败，请联系网站管理员！");
		}
	}
	
	@Log("修改组织机构")
	@RequiresPermissions("organ:update")
	@RequestMapping("organ/update")
	@ResponseBody
	public ResponseBo updateOrgan(Organ organ) {
		try {
			this.organService.updateOrgan(organ);
			return ResponseBo.ok("修改组织机构成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseBo.error("修改组织机构失败，请联系网站管理员！");
		}
	}
}

package com.muiboot.shiro.system.controller;



import com.muiboot.core.entity.ResponseBo;
import com.muiboot.core.util.FileUtils;
import com.muiboot.shiro.system.entity.SysDic;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;



import com.muiboot.core.annotation.Log;
import com.muiboot.shiro.common.controller.ShiroBaseController;
import com.muiboot.shiro.system.service.DictService;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
/**
 * <p>Title: </p>
 * <p>Description: 字典处理</p>
 *
 * @author jin
 * @version 1.0 2018/9/18
 */
@Controller
public class DictControllerShiro extends ShiroBaseController {

	@Autowired
	private DictService dictService;

	@RequestMapping("dict/tree")
	@ResponseBody
	public ResponseBo getDictTree(HttpServletResponse response,String dicName)  throws Exception{
		return ResponseBo.ok(this.dictService.getDictTree(dicName));
	}

	@Log("新增字典")
	@RequiresPermissions("dict:add")
	@RequestMapping("dict/add")
	@ResponseBody
	public ResponseBo addDic(SysDic dic)  throws Exception{
		if (null==dic.getShowIcon()){
			dic.setShowIcon("0");
		}
		if (null==dic.getValid()){
			dic.setValid("0");
		}
		this.dictService.add(dic);
		return ResponseBo.ok("新增字典" + dic.getDicName() + "成功！");
	}

	@RequestMapping("dict/getDicDetail")
	@ResponseBody
	public ResponseBo getDicDetail(HttpServletResponse response, Long dicId) throws Exception {
		return ResponseBo.ok(this.dictService.findDicDetail(dicId));
	}

	@RequestMapping("dict/getDic")
	@ResponseBody
	public ResponseBo getDic(HttpServletResponse response, Long dicId) throws Exception {
		return ResponseBo.ok(this.dictService.selectByKey(dicId));
	}

	@RequestMapping("dict/loadDics")
	@ResponseBody
	public ResponseBo loadDics(HttpServletResponse response, String[] dicKeys) throws Exception {
		return ResponseBo.ok(this.dictService.loadDics(dicKeys));
	}
	@Log("删除字典")
	@RequiresPermissions("dict:delete")
	@RequestMapping("dict/delete")
	@ResponseBody
	public ResponseBo deleteMenus(String ids)  throws Exception{
		this.dictService.deleteDicts(ids);
		return ResponseBo.ok("删除成功！");
	}
	@Log("修改字典 ")
	@RequiresPermissions("dict:update")
	@RequestMapping("dict/update")
	@ResponseBody
	public ResponseBo updateDict(SysDic dict) {
		try {
			dict.setUpdateDate(new Date());
			if (null==dict.getShowIcon()){
				dict.setShowIcon("0");
			}
			if (null==dict.getValid()){
				dict.setValid("0");
			}
			this.dictService.updateDicNotNull(dict);
			return ResponseBo.ok("修改字典成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseBo.error("修改字典失败，请联系网站管理员！");
		}
	}

	@RequestMapping("dict/excel")
	@ResponseBody
	public ResponseBo dictExcel(SysDic dic)  throws Exception{
		return FileUtils.createExcelByPOIKit("字典表", this.dictService.selectAll(), SysDic.class);
	}
}

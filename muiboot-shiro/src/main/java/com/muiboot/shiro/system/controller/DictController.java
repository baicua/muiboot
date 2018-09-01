package com.muiboot.shiro.system.controller;



import com.muiboot.shiro.common.domain.ResponseBo;
import com.muiboot.shiro.system.domain.CoreDic;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;



import com.muiboot.shiro.common.annotation.Log;
import com.muiboot.shiro.common.controller.BaseController;
import com.muiboot.shiro.system.service.DictService;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Controller
public class DictController extends BaseController {

	@Autowired
	private DictService dictService;

	@RequestMapping("dict/tree")
	@ResponseBody
	public ResponseBo getDictTree(HttpServletResponse response,String dicName)  throws Exception{
		response.setHeader("Cache-Control", "max-age=5");//缓存5秒
		return ResponseBo.ok(this.dictService.getDictTree(dicName));
	}

	@Log("新增字典")
	@RequiresPermissions("dict:add")
	@RequestMapping("dict/add")
	@ResponseBody
	public ResponseBo addDic(CoreDic dic)  throws Exception{
		this.dictService.add(dic);
		return ResponseBo.ok("新增字典" + dic.getDicName() + "成功！");
	}

	@RequestMapping("dict/getDicDetail")
	@ResponseBody
	public ResponseBo getDicDetail(HttpServletResponse response, Long dicId) throws Exception {
		response.setHeader("Cache-Control", "max-age=5");//缓存5秒
		return ResponseBo.ok(this.dictService.findDicDetail(dicId));
	}

	@RequestMapping("dict/getDic")
	@ResponseBody
	public ResponseBo getDic(HttpServletResponse response, Long dicId) throws Exception {
		response.setHeader("Cache-Control", "max-age=5");//缓存5秒
		return ResponseBo.ok(this.dictService.selectByKey(dicId));
	}

	@RequestMapping("dict/loadDics")
	@ResponseBody
	public ResponseBo loadDics(HttpServletResponse response, String[] dicKeys) throws Exception {
		response.setHeader("Cache-Control", "max-age=5");//缓存5秒
		return ResponseBo.ok(this.dictService.loadDics(dicKeys));
	}

	@Log("修改字典 ")
	@RequiresPermissions("dict:update")
	@RequestMapping("dict/update")
	@ResponseBody
	public ResponseBo updateDict(CoreDic dict) {
		try {
			dict.setUpdateDate(new Date());
			this.dictService.updateDicNotNull(dict);
			return ResponseBo.ok("修改字典成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseBo.error("修改字典失败，请联系网站管理员！");
		}
	}
}

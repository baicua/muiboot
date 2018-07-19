package com.baicua.bsds.controller;
import com.baicua.bsds.domain.RecordSheet;
import com.baicua.bsds.service.IRecordSheetService;
import com.baicua.shiro.common.annotation.Log;
import com.baicua.shiro.common.controller.BaseController;
import com.baicua.shiro.common.domain.QueryRequest;
import com.baicua.shiro.common.domain.ResponseBo;
import com.baicua.shiro.system.domain.User;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;


@Controller
public class RecordSheetController  extends BaseController {
    @Autowired
    private IRecordSheetService sheetService;
    @RequestMapping("recordSheet")
    @RequiresPermissions("recordSheet:list")
    public String recordSheet(Model model) {
        User user = super.getCurrentUser();
        model.addAttribute("user", user);
        return "bsds/record/recordSheet";
    }
    @RequestMapping("recordSheet/list")
    @ResponseBody
    public Map<String, Object> recordSheetList(QueryRequest request, RecordSheet recordSheet) {
        PageHelper.startPage(request.getPageNum(), request.getPageSize());
        List<RecordSheet> list = this.sheetService.findRecordSheet(recordSheet);
        PageInfo<RecordSheet> pageInfo = new PageInfo<RecordSheet>(list);
        return getDataTable(pageInfo);
    }
    /*
    获取记录单信息
     */
    @RequestMapping("record/getSheet")
    @ResponseBody
    @RequiresPermissions("recordSheet:list")
    public ResponseBo getSheet(Long rId) {
        try {
            RecordSheet recordSheet = this.sheetService.selectByKey(rId);
            return ResponseBo.ok(recordSheet);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取记录单信息失败："+e.getMessage());
            return ResponseBo.error("获取记录单信息失败，请联系网站管理员！");
        }
    }

    @Log("新增/修改记录单模板")
    @RequiresPermissions("recordSheet:update")
    @RequestMapping("record/sheet/update")
    @ResponseBody
    public ResponseBo update(RecordSheet sheet) {
        try {
            int r=sheetService.saveOrUpdate(sheet);
            if (r==0){
                return ResponseBo.error("新增/修改记录单模板失败！");
            }else if (r>1){
                return ResponseBo.error("新增/修改记录单模板异常！");
            }
            return ResponseBo.ok("新增/修改记录单模板成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBo.error("新增/修改记录，请联系网站管理员！");
        }
    }

    @Log("删除记录单模板")
    @RequiresPermissions("recordSheet:delete")
    @RequestMapping("record/sheet/delete")
    @ResponseBody
    public ResponseBo delete(String ids) {
        try {
            this.sheetService.deleteByIds(ids);
            return ResponseBo.ok("删除记录单模板成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBo.error("删除记录单模板，请联系网站管理员！");
        }
    }
}

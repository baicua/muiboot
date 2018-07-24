package com.baicua.bsds.controller;
import com.baicua.bsds.domain.RecordApply;
import com.baicua.bsds.domain.RecordBook;
import com.baicua.bsds.domain.RecordSheet;
import com.baicua.bsds.service.IRecordApplyService;
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
import com.baicua.shiro.common.util.FileUtils;


import java.util.List;
import java.util.Map;


@Controller
public class RecordApplyController extends BaseController {
    @Autowired
    private IRecordApplyService applyService;
    /**
     * <p>Description: 申请单记录列表-页面</p>
     * @version 1.0 2018/7/17
     * @author jin
     */
    @RequestMapping("recordApply")
    @RequiresPermissions("recordApply:list")
    public String recordApply(Model model) {
        User user = super.getCurrentUser();
        model.addAttribute("user", user);
        return "bsds/record/recordApply";
    }
    /**
     * <p>Description: 获取全部申请记录数据</p>
     * @version 1.0 2018/7/17
     * @author jin
     */
    @RequestMapping("recordApply/list")
    @ResponseBody
    public Map<String, Object> recordApplyList(QueryRequest request, RecordApply recordApply) {
        PageHelper.startPage(request.getPageNum(), request.getPageSize());
        List<RecordApply> list = this.applyService.findRecordApply(recordApply);
        PageInfo<RecordApply> pageInfo = new PageInfo<RecordApply>(list);
        return getDataTable(pageInfo);
    }
    @RequestMapping("recordApply/excel")
    @ResponseBody
    public ResponseBo recordApplyExcel(RecordApply recordApply) {
        try {
            List<RecordApply> list = this.applyService.findRecordApply(recordApply);
            return FileUtils.createExcelByPOIKit("申领列表", list, RecordApply.class);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBo.error("导出Excel失败，请联系网站管理员！");
        }
    }

    @RequestMapping("recordApply/csv")
    @ResponseBody
    public ResponseBo recordApplyCsv(RecordApply recordApply) {
        try {
            List<RecordApply> list = this.applyService.findRecordApply(recordApply);
            return FileUtils.createCsv("申领列表", list, RecordApply.class);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBo.error("导出Csv失败，请联系网站管理员！");
        }
    }
    /**
     * <p>Description: 获取当前登录用户申领记录数据</p>
     * @version 1.0 2018/7/17
     * @author jin
     */
    @RequestMapping("recordApply/owner/list")
    @ResponseBody
    public Map<String, Object> ownerList(QueryRequest request, RecordApply recordApply) {
        PageHelper.startPage(request.getPageNum(), request.getPageSize());
        List<RecordApply> list = this.applyService.findRecordApplyByUser(recordApply,this.getCurrentUser());
        PageInfo<RecordApply> pageInfo = new PageInfo<RecordApply>(list);
        return getDataTable(pageInfo);
    }
    /**
     * <p>Description: 申领记录本</p>
     * @version 1.0 2018/7/17
     * @author jin
     */
    @RequestMapping("record/applyBook")
    @ResponseBody
    public ResponseBo applyBook(RecordBook book, int quantity) {
        try {
            this.applyService.applyRecordBook(book,quantity,this.getCurrentUser());
            return ResponseBo.ok();
        } catch (Exception e) {
            logger.error("记录本申领失败:"+e.getMessage());
            e.printStackTrace();
            return ResponseBo.error("记录本申请失败，请联系管理员！");
        }
    }

    /**
     * <p>Description: 申领记录单</p>
     * @version 1.0 2018/7/17
     * @author jin
     */
    @RequestMapping("record/applySheet")
    @ResponseBody
    public ResponseBo applySheet(RecordApply apply) {
        try {
            this.applyService.applyRecordApply(apply,this.getCurrentUser());
            return ResponseBo.ok();
        } catch (Exception e) {
            logger.error("记录单申领失败:"+e.getMessage());
            e.printStackTrace();
            return ResponseBo.error("记录单申领失败，请联系管理员！");
        }
    }
    /**
     * <p>Description: 记录单待申领列表</p>
     * @version 1.0 2018/7/17
     * @author jin
     */
    @RequestMapping("sheetApply")
    @RequiresPermissions("sheetApply:list")
    public String sheetApply(Model model) {
        User user = super.getCurrentUser();
        model.addAttribute("user", user);
        return "bsds/record/sheetApply";
    }
    /**
     * <p>Description:记录本待申领列表 </p>
     * @version 1.0 2018/7/17
     * @author jin
     */
    @RequestMapping("bookApply")
    @RequiresPermissions("bookApply:list")
    public String bookApply(Model model) {
        User user = super.getCurrentUser();
        model.addAttribute("user", user);
        return "bsds/record/bookApply";
    }
}
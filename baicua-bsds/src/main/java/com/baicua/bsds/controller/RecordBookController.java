package com.baicua.bsds.controller;

import com.baicua.bsds.domain.RecordBook;
import com.baicua.bsds.service.IRecordBookService;
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
public class RecordBookController extends BaseController {
    @Autowired
    private IRecordBookService bookService;

    @RequestMapping("recordBook")
    @RequiresPermissions("recordBook:list")
    public String recordBook(Model model) {
        User user = super.getCurrentUser();
        model.addAttribute("user", user);
        return "bsds/record/recordBook";
    }

    @RequestMapping("recordBook/list")
    @ResponseBody
    public Map<String, Object> recordBookList(QueryRequest request, RecordBook recordBook) {
        PageHelper.startPage(request.getPageNum(), request.getPageSize());
        List<RecordBook> list = this.bookService.findRecordBook(recordBook);
        PageInfo<RecordBook> pageInfo = new PageInfo<RecordBook>(list);
        return getDataTable(pageInfo);
    }

    /*
    获取记录本信息
     */
    @RequestMapping("record/getBook")
    @ResponseBody
    @RequiresPermissions("recordBook:list")
    public ResponseBo getBook(Long rId) {
        try {
            RecordBook recordBook = this.bookService.selectByKey(rId);
            return ResponseBo.ok(recordBook);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取记录本信息失败："+e.getMessage());
            return ResponseBo.error("获取记录本信息失败，请联系网站管理员！");
        }
    }

    @Log("新增/修改记录本模板")
    @RequiresPermissions("recordBook:update")
    @RequestMapping("record/book/update")
    @ResponseBody
    public ResponseBo update(RecordBook book) {
        try {
            int r=bookService.saveOrUpdate(book);
            if (r==0){
                return ResponseBo.error("新增/修改记录本模板失败！");
            }else if (r>1){
                return ResponseBo.error("新增/修改记录本模板异常！");
            }
            return ResponseBo.ok("新增/修改记录本模板成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBo.error("新增/修改记录，请联系网站管理员！");
        }
    }

    @Log("删除记录本模板")
    @RequiresPermissions("recordBook:delete")
    @RequestMapping("record/book/delete")
    @ResponseBody
    public ResponseBo delete(String ids) {
        try {
            this.bookService.deleteByIds(ids);
            return ResponseBo.ok("删除记录本模板成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBo.error("删除记录本模板，请联系网站管理员！");
        }
    }
}

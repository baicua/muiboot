package com.baicua.bsds.controller;
import com.baicua.bsds.domain.RecordApply;
import com.baicua.bsds.domain.RecordBook;
import com.baicua.bsds.service.IRecordApplyService;
import com.baicua.shiro.common.controller.BaseController;
import com.baicua.shiro.common.domain.QueryRequest;
import com.baicua.shiro.common.domain.ResponseBo;
import com.baicua.shiro.system.domain.User;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("business")
public class BusinessController extends BaseController {
    @Autowired
    private IRecordApplyService applyService;
    /**
    * <p>Description: 申请单记录列表-页面</p>
    * @version 1.0 2018/7/17
    * @author jin
    */
    @RequestMapping("done")
    @RequiresPermissions("business:done")
    public String done(Model model) {
        User user = super.getCurrentUser();
        model.addAttribute("user", user);
        return "bsds/business/done";
    }
    /**
    * <p>Description: 获取当前登录用户申领记录数据</p>
    * @version 1.0 2018/7/17
    * @author jin
    */
    @RequestMapping("owner/list")
    @ResponseBody
    public Map<String, Object> ownerList(QueryRequest request, RecordApply recordApply) {
        PageHelper.startPage(request.getPageNum(), request.getPageSize());
        List<RecordApply> list = this.applyService.findRecordApplyByUser(recordApply,this.getCurrentUser());
        PageInfo<RecordApply> pageInfo = new PageInfo<RecordApply>(list);
        return getDataTable(pageInfo);
    }
    @RequestMapping("mainPage")
    public String mainPage(Model model) {
        User user = super.getCurrentUser();
        Subject subject = getSubject();
        if (subject.hasRole(BUSINESS_ROLE)){
            return "bsds/business/done";
        }else {
            return "bsds/business/done2";
        }
    }
}

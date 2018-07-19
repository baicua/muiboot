package com.baicua.bsds.service.impl;

import com.baicua.bsds.comm.TypeUnit;
import com.baicua.bsds.domain.RecordApply;
import com.baicua.bsds.domain.RecordBook;
import com.baicua.bsds.domain.RecordSheet;
import com.baicua.bsds.service.IRecordApplyService;
import com.baicua.bsds.service.IRecordBookService;
import com.baicua.bsds.service.IRecordSheetService;
import com.baicua.shiro.common.service.impl.BaseService;
import com.baicua.shiro.system.dao.DeptMapper;
import com.baicua.shiro.system.domain.Dept;
import com.baicua.shiro.system.domain.User;
import com.baicua.shiro.system.service.DeptService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 75631 on 2018/7/14.
 */
@Service("recordApplyService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RecordApplyServiceImp extends BaseService<RecordApply> implements IRecordApplyService {
    @Autowired
    private IRecordSheetService sheetService;
    @Autowired
    private IRecordBookService bookService;
    @Autowired
    private DeptService deptService;

    @Override
    public List<RecordApply> findRecordApply(RecordApply recordApply) {
        try {
            Example example = new Example(RecordApply.class);
            Example.Criteria criteria = example.createCriteria();
            if (null!=recordApply.getApType()){
                criteria.andEqualTo("apType",recordApply.getApType());
            }
            if (StringUtils.isNotBlank(recordApply.getrName())){
                criteria.andCondition("concat(AP_NAME,R_NAME,AP_BATCH_NUM) like ","%"+recordApply.getrName()+"%");
            }
            example.setOrderByClause("AP_DATE desc");
            return this.selectByExample(example);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<RecordApply>();
        }
    }

    /**
     * <p>Description:按用户查询申领记录 </p>
     *
     * @param recordApply
     * @param user
     * @version 1.0 2018/7/17
     * @author jin
     */
    @Override
    public List<RecordApply> findRecordApplyByUser(RecordApply recordApply, User user) {
        if (null==user){
            logger.warn("查询条件 user# 为空");
            return new ArrayList<RecordApply>();
        }
        try {
            Example example = new Example(RecordApply.class);
            Example.Criteria criteria = example.createCriteria();
            if (null!=recordApply.getApType()){
                criteria.andEqualTo("apType",recordApply.getApType());
            }
            criteria.andEqualTo("userId",user.getUserId());
            if (StringUtils.isNotBlank(recordApply.getrName())){
                criteria.andCondition("concat(AP_NAME,R_NAME,AP_BATCH_NUM) like ","%"+recordApply.getrName()+"%");
            }
            example.setOrderByClause("AP_DATE desc");
            return this.selectByExample(example);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<RecordApply>();
        }
    }

    @Override
    @Transactional
    public void applyRecordSheet(RecordSheet sheet, int quantity, User user) {
        Dept dept = deptService.findById(user.getDeptId());
        RecordSheet sheetU=sheetService.compareSerialAndSet(sheet);
        RecordApply recordApply = new RecordApply();
        recordApply.setApDeptId(user.getDeptId());
        recordApply.setApDeptName(dept.getDeptName());
        recordApply.setApName(user.getUsername());
        recordApply.setrName(sheetU.getrSolName());
        recordApply.setApDate(new Date());
        recordApply.setApQuantity(quantity);
        recordApply.setApType(sheetU.getrType());
        recordApply.setApBatchNum(String.format("%2s%03d",sheetU.getrYear().substring(2), sheetU.getrSerialNum()));
        this.save(recordApply);
    }

    @Override
    @Transactional
    public void applyRecordBook(RecordBook book, int quantity, User user) {
        Dept dept = deptService.findById(user.getDeptId());
        RecordBook booktU=bookService.compareSerialAndSet(book);
        RecordApply recordApply = new RecordApply();
        recordApply.setApDeptId(user.getDeptId());
        recordApply.setApDeptName(dept.getDeptName());
        recordApply.setApName(user.getUsername());
        recordApply.setrName(booktU.getrName());
        recordApply.setApDate(new Date());
        recordApply.setApQuantity(quantity);
        recordApply.setApType(TypeUnit.BOOK.applyType());
        recordApply.setApBatchNum(String.format("%s%03d",booktU.getrPro(), booktU.getrSerialNum()));
        this.save(recordApply);
    }
}

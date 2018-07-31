package com.baicua.bsds.service.impl;

import com.baicua.bsds.comm.TypeUnit;
import com.baicua.bsds.domain.RecordApply;
import com.baicua.bsds.domain.RecordBook;
import com.baicua.bsds.domain.RecordSheet;
import com.baicua.bsds.domain.Sequence;
import com.baicua.bsds.service.IRecordApplyService;
import com.baicua.bsds.service.IRecordBookService;
import com.baicua.bsds.service.IRecordSheetService;
import com.baicua.bsds.service.ISeqService;
import com.baicua.bsds.vo.HomeFrontVo;
import com.baicua.shiro.common.annotation.Log;
import com.baicua.shiro.common.service.impl.BaseService;
import com.baicua.shiro.common.util.FileUtils;
import com.baicua.shiro.common.util.PrintUtils;
import com.baicua.shiro.system.domain.AttNexus;
import com.baicua.shiro.system.domain.Dept;
import com.baicua.shiro.system.domain.User;
import com.baicua.shiro.system.service.DeptService;
import com.baicua.shiro.system.service.IAttNexusService;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.awt.print.PrinterException;
import java.io.IOException;
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
    @Autowired
    private ISeqService seqService;
    @Autowired
    private IAttNexusService attNexusService;

    @Override
    public List<RecordApply> findRecordApply(RecordApply recordApply) {
        try {
            Example example = new Example(RecordApply.class);
            Example.Criteria criteria = example.createCriteria();
            if (null!=recordApply.getApType()){
                criteria.andEqualTo("apType",recordApply.getApType());
            }
            if (null!=recordApply.getSheetType()){
                criteria.andEqualTo("sheetType",recordApply.getSheetType());
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
    @Log("申请记录单")
    public void applyRecordSheet(RecordSheet sheet, int quantity, String printerName, User user) {
        sheet =sheetService.selectByKey(sheet.getrId());
        Dept dept = deptService.findById(user.getDeptId());
        RecordSheet sheetU=sheetService.selectByKey(sheet.getrId());
        Sequence sequence = new Sequence("SHEET"+sheetU.getrType());
        String[] serialNum = seqService.compareAndSet(sequence,quantity);
        //sequence = seqService.compareAndSet(sequence);
        RecordApply recordApply = new RecordApply();
        recordApply.setrId(sheetU.getrId());
        recordApply.setApDeptId(user.getDeptId());
        recordApply.setApDeptName(dept.getDeptName());
        recordApply.setUserId(user.getUserId());
        recordApply.setApName(user.getUsername());
        recordApply.setrName(sheetU.getrSolName());
        recordApply.setApDate(new Date());
        recordApply.setApQuantity(quantity);
        recordApply.setApType(TypeUnit.SHEET.applyType());
        recordApply.setSheetType(sheetU.getrType());
        recordApply.setApBatchNum(StringUtils.join(serialNum,","));
        this.save(recordApply);
    }

    @Override
    @Transactional
    @Log("申请记录本")
    public void applyRecordBook(RecordBook book, int quantity, String printerName, User user) throws IOException, PrinterException {
        if (StringUtils.isBlank(printerName)){
            throw new NullPointerException("请先选择打印机");
        }
        Dept dept = deptService.findById(user.getDeptId());
        RecordBook booktU=bookService.selectByKey(book.getrId());
        Sequence sequence = new Sequence("BOOK");
        String[] serialNum = seqService.compareAndSet(sequence,quantity);
        AttNexus attNexus =attNexusService.selectByKey(booktU.getAttId());
        String files[]=new String[quantity];
        for (int i=0;i<quantity;i++){
            files[i]=attNexus.getAttDir()+"/"+attNexus.getAttOriName();
        }
        PDDocument document =null;
        try {
            document=FileUtils.mergePdf(files,serialNum);
            PrintUtils.printFile(document,printerName);
        } catch (IOException e) {
            logger.error("附件合并失败："+e.getMessage());
            throw new IOException("附件合并失败，"+e.getMessage());
        } catch (PrinterException e) {
            logger.error("打印失败："+e.getMessage());
            throw new PrinterException("打印失败，"+e.getMessage());
        }finally {
            if (null!=document)
             document.close();
        }
        //sequence = seqService.compareAndSet(sequence);
        RecordApply recordApply = new RecordApply();
        recordApply.setrId(booktU.getrId());
        recordApply.setApDeptId(user.getDeptId());
        recordApply.setApDeptName(dept.getDeptName());
        recordApply.setApName(user.getUsername());
        recordApply.setUserId(user.getUserId());
        recordApply.setrName(booktU.getrName());
        recordApply.setApDate(new Date());
        recordApply.setApQuantity(quantity);
        recordApply.setApType(TypeUnit.BOOK.applyType());
        recordApply.setApBatchNum(StringUtils.join(serialNum,","));
        this.save(recordApply);
    }

    @Override
    @Transactional
    public void applyRecordApply(RecordApply apply, User currentUser) throws IOException, PrinterException {
        if (null==apply||null==currentUser||null==apply.getApType())
            throw new NullPointerException("当前登录用户不能识别，或者申请信息为空");
        if (StringUtils.isBlank(apply.getPrinterName())){
            throw new NullPointerException("请先选择打印机");
        }
        if (1==apply.getApType().intValue()){
            RecordSheet sheet = new RecordSheet();
            sheet.setrId(apply.getrId());
            applyRecordSheet(sheet,apply.getApQuantity(),apply.getPrinterName(),currentUser);
        }else {
            RecordBook book = new RecordBook();
            book.setrId(apply.getrId());
            applyRecordBook(book,apply.getApQuantity(),apply.getPrinterName(),currentUser);
        }
    }

    @Override
    public HomeFrontVo queryHomeFrontVo(User user) {
        HomeFrontVo frontVo=new HomeFrontVo();
        //查询记录单申请记录
        //查询记录本申请记录
        //查询部门申请记录
        //查询6条当前用户的申请记录
        //查询6条所有用户的申请记录
        RecordApply apply = new RecordApply();
        apply.setUserId(user.getUserId());
        apply.setApType(TypeUnit.SHEET.applyType());
        int sheetCount=mapper.selectCount(apply);

        apply.setApType(TypeUnit.BOOK.applyType());
        int bookCount=mapper.selectCount(apply);

        apply.setUserId(null);
        apply.setApType(null);
        apply.setApDeptId(user.getDeptId());
        int deptCount = mapper.selectCount(apply);

        Example example = new Example(RecordApply.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",user.getUserId());
        example.setOrderByClause("AP_ID desc");
        PageHelper.startPage(1, 6);
        List<RecordApply> appliesSelf=mapper.selectByExample(example);
        this.preProcess(appliesSelf);

        example.clear();
        PageHelper.startPage(1, 6);
        example.setOrderByClause("AP_ID desc");
        List<RecordApply> appliesLast=mapper.selectByExample(example);
        frontVo.setSheetApCount(sheetCount);
        frontVo.setBookApCount(bookCount);
        frontVo.setDeptApCount(deptCount);
        frontVo.setAppliesSelf(appliesSelf);
        frontVo.setAppliesLast(appliesLast);
        return frontVo;
    }

    private void preProcess(List<RecordApply> appliesSelf) {
        if (CollectionUtils.isNotEmpty(appliesSelf)){
            for (int i=0,l=appliesSelf.size();i<l;i++){
                RecordApply r = appliesSelf.get(i);
                Object o = null;
                if (TypeUnit.BOOK.applyType()==r.getApType().intValue()){
                    o=bookService.selectByKey(r.getrId());
                }else if (TypeUnit.SHEET.applyType()==r.getApType().intValue()){
                    o=sheetService.selectByKey(r.getrId());
                }else {
                    throw new IllegalArgumentException("申领记录表数据错误。");
                }
                r.setRecord(o);
            }
        }
    }
}

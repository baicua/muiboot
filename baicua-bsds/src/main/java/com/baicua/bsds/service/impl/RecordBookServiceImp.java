package com.baicua.bsds.service.impl;

import com.baicua.bsds.dao.RecordBookMapper;
import com.baicua.bsds.domain.RecordBook;
import com.baicua.bsds.service.IRecordBookService;
import com.baicua.shiro.common.service.impl.BaseService;
import com.baicua.shiro.system.dao.DeptMapper;
import com.baicua.shiro.system.domain.Dept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by 75631 on 2018/7/14.
 */
@Service("recordBookService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RecordBookServiceImp extends BaseService<RecordBook> implements IRecordBookService {
    @Autowired
    private RecordBookMapper recordBookMapper;

    @Autowired
    private DeptMapper deptMapper;
    @Override
    public List<RecordBook> findRecordBook(RecordBook recordBook) {
        try {
            return this.recordBookMapper.findRecordBook(recordBook);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<RecordBook>();
        }
    }

    /**
     * <p>Description: CAS算法更新</p>
     *
     * @param book
     * @version 1.0 2018/7/16
     * @author jin
     */
    @Override
    @Transactional
    public RecordBook compareSerialAndSet(RecordBook book) {
        logger.info("CAS算法更新RecordBook系列号");
        if(null==book||null==book.getrId()){
            throw new NullPointerException("RecordBook记录为空--R_ID：null");
        }
        Example example = new Example(RecordBook.class);
        Example.Criteria criteria = example.createCriteria();
        RecordBook bookNow=null;
        int hitCount=0;
        do {
            if (hitCount>10){
                throw new ArithmeticException("数据异常：更新BOOK序列号失败--R_ID："+book.getrId());
            }
            bookNow = this.selectByKey(book.getrId());
            if (null==bookNow){
                throw new NullPointerException("RecordBook记录为空--R_ID："+book.getrId());
            }
            criteria.andEqualTo("rId",bookNow.getrId());
            criteria.andEqualTo("rSerialNum",bookNow.getrSerialNum());
            bookNow.setrSerialNum(bookNow.getrSerialNum()+1);
            hitCount++;
        }while (recordBookMapper.updateByExampleSelective(bookNow,example)==0);
        return bookNow;
    }

    @Override
    @Transactional
    public int saveOrUpdate(RecordBook book) {
        Dept dept=new Dept();
        if (null!=book.getrDeptId()){
            dept=deptMapper.selectByPrimaryKey(book.getrDeptId());
        }
        Date now =new Date();
        if (null==book.getrId()){
            book.setrCrtDate(now);
            book.setrUpdDate(now);
            book.setrPro("NB");
            book.setrSerialNum(0L);
            book.setrVesion(1L);
            book.setrDept(dept.getDeptName());
            return mapper.insert(book);
        }else {
            book.setrUpdDate(now);
            book.setrVesion(book.getrVesion()+1);
            book.setrDept(dept.getDeptName());
            return mapper.updateByPrimaryKeySelective(book);
        }
    }

    @Override
    @Transactional
    public int deleteByIds(String ids) {
        List<String> list = Arrays.asList(ids.split(","));
        return this.batchDelete(list, "rId", RecordBook.class);
    }
}

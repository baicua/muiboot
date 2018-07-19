package com.baicua.bsds.service.impl;

import com.baicua.bsds.dao.RecordSheetMapper;
import com.baicua.bsds.domain.RecordSheet;
import com.baicua.bsds.service.IRecordSheetService;
import com.baicua.shiro.common.service.impl.BaseService;
import com.baicua.shiro.common.util.DateUtil;
import org.apache.commons.lang.StringUtils;
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
@Service("recordSheetService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RecordSheetServiceImp extends BaseService<RecordSheet> implements IRecordSheetService {
    @Autowired
    private RecordSheetMapper recordSheetMapper;
    @Override
    public List<RecordSheet> findRecordSheet(RecordSheet recordSheet) {
        try {
            Example example = new Example(RecordSheet.class);
            Example.Criteria criteria = example.createCriteria();
            if (null!=recordSheet.getrType()){
                criteria.andEqualTo("rType",recordSheet.getrType());
            }
            if (StringUtils.isNotBlank(recordSheet.getrSolName())){
                criteria.andCondition("concat(R_CODE,R_SOL_NAME,R_POTENCY,R_REF_METHOD) like ","%"+recordSheet.getrSolName()+"%");
            }
            example.setOrderByClause("R_UPD_DATE desc");
            return this.selectByExample(example);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<RecordSheet>();
        }
    }

    @Override
    public RecordSheet compareSerialAndSet(RecordSheet sheet) {
        logger.info("CAS算法更新SHEET系列号");
        if(null==sheet||null==sheet.getrId()){
            throw new NullPointerException("RecordSheet记录为空--R_ID：null");
        }
        Example example = new Example(RecordSheet.class);
        Example.Criteria criteria = example.createCriteria();
        RecordSheet sheetNow=null;
        int hitCount=0;
        do {
            if (hitCount>10){
                throw new ArithmeticException("更新SHEET系列号异常--R_ID："+sheet.getrId());
            }
            sheetNow = this.selectByKey(sheet.getrId());
            if (null==sheetNow){
                throw new NullPointerException("RecordSheet记录为空--R_ID："+sheet.getrId());
            }
            String year=DateUtil.getNowDateFormat("yyyy");
            if (!year.equals(sheetNow.getrYear())){
                sheetNow.setrSerialNum(0L);
                sheetNow.setrYear(year);
            }
            criteria.andEqualTo("rId",sheetNow.getrId());
            criteria.andEqualTo("rSerialNum",sheetNow.getrSerialNum());
            sheetNow.setrSerialNum(sheetNow.getrSerialNum()+1);
            hitCount++;
        }while (recordSheetMapper.updateByExampleSelective(sheetNow,example)==0);
        return sheetNow;
    }

    @Override
    @Transactional
    public int saveOrUpdate(RecordSheet sheet) {
        Date now =new Date();
        if (null==sheet.getrId()){
            sheet.setrCrtDate(now);
            sheet.setrUpdDate(now);
            sheet.setrYear(DateUtil.getNowDateFormat("yyyy"));
            sheet.setrSerialNum(0L);
            sheet.setrVesion(1L);
            return mapper.insert(sheet);
        }else {
            sheet.setrUpdDate(now);
            sheet.setrYear(DateUtil.getNowDateFormat("yyyy"));
            sheet.setrVesion(sheet.getrVesion()+1);
            return mapper.updateByPrimaryKeySelective(sheet);
        }
    }

    @Override
    @Transactional
    public int deleteByIds(String ids) {
        List<String> list = Arrays.asList(ids.split(","));
        return this.batchDelete(list, "rId", RecordSheet.class);
    }
}

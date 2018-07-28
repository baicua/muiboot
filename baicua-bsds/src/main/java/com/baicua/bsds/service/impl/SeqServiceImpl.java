package com.baicua.bsds.service.impl;

import com.baicua.bsds.domain.Sequence;
import com.baicua.bsds.service.ISeqService;
import com.baicua.shiro.common.service.impl.BaseService;
import com.baicua.shiro.common.util.DateUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

/**
 * Created by 75631 on 2018/7/28.
 */
@Service("sequenceService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SeqServiceImpl extends BaseService<Sequence> implements ISeqService {

    @Override
    public Sequence compareAndSet(Sequence sequence) {
        logger.info("CAS算法更新系列号"+sequence.getSeqName());
        if(null==sequence||null==sequence.getSeqName()){
            throw new NullPointerException("Sequence为空--seqName：null");
        }
        Example example = new Example(Sequence.class);
        Example.Criteria criteria = example.createCriteria();
        Sequence sequenceNew=null;
        int hitCount=0;
        do {
            if (hitCount>10){
                throw new ArithmeticException("更新Sequence系列号异常--R_ID："+sequence.getSeqName());
            }
            sequenceNew = this.selectByKey(sequence.getSeqName());
            if (null==sequenceNew){
                throw new NullPointerException("Sequence为空--seqName："+sequence.getSeqName());
            }
            String year= DateUtil.getNowDateFormat("yyyy");
            if (!year.equals(sequenceNew.getFormat())){
                sequenceNew.setCurrentVal(0L);
                sequenceNew.setFormat(year);
            }
            criteria.andEqualTo("seqName",sequenceNew.getSeqName());
            criteria.andEqualTo("currentVal",sequenceNew.getCurrentVal());
            sequenceNew.setCurrentVal(sequenceNew.getCurrentVal()+sequenceNew.getIncrementVal());
            hitCount++;
        }while (mapper.updateByExampleSelective(sequenceNew,example)==0);
        return sequenceNew;
    }
}

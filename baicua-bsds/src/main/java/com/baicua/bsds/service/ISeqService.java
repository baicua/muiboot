package com.baicua.bsds.service;

import com.baicua.bsds.domain.Sequence;
import com.baicua.shiro.common.service.IService;

/**
 * Created by 75631 on 2018/7/28.
 */
public interface ISeqService  extends IService<Sequence> {
    /*
    CAS更新序列
     */
    Sequence compareAndSet(Sequence sequence);

    /*
    获取多个申领批号
     */
    String[] getSerialNum(Sequence sequence, int quantity);

    /*
    获取多个申领批号
     */
    String[] compareAndSet(Sequence sequence, int quantity);
}

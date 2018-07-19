package com.baicua.bsds.service;

import com.baicua.bsds.domain.RecordBook;
import com.baicua.shiro.common.service.IService;

import java.util.List;

/**
 * Created by 75631 on 2018/7/14.
 */
public interface IRecordBookService extends IService<RecordBook> {
    List<RecordBook> findRecordBook(RecordBook recordBook);
    /**
     * <p>Description: CAS算法更新</p>
     * @version 1.0 2018/7/16
     * @author jin
     */
    RecordBook compareSerialAndSet(RecordBook book);

    int saveOrUpdate(RecordBook book);

    int deleteByIds(String ids);
}

package com.baicua.bsds.service;

import com.baicua.bsds.domain.RecordSheet;
import com.baicua.shiro.common.service.IService;

import java.util.List;

/**
 * Created by 75631 on 2018/7/14.
 */
public interface IRecordSheetService   extends IService<RecordSheet> {
    List<RecordSheet> findRecordSheet(RecordSheet recordSheet);

    /**
    * <p>Description: CAS算法更新</p>
    * @version 1.0 2018/7/16
    * @author jin
    */
    RecordSheet compareSerialAndSet(RecordSheet sheet);
    /*
    新增或者修改记录单模板
     */
    int saveOrUpdate(RecordSheet sheet);

    int deleteByIds(String ids);
}

package com.baicua.bsds.service;

import com.baicua.bsds.domain.RecordApply;
import com.baicua.bsds.domain.RecordBook;
import com.baicua.bsds.domain.RecordSheet;
import com.baicua.bsds.vo.HomeFrontVo;
import com.baicua.shiro.common.service.IService;
import com.baicua.shiro.system.domain.User;

import java.awt.print.PrinterException;
import java.io.IOException;
import java.util.List;

/**
 * Created by 75631 on 2018/7/14.
 */
public interface IRecordApplyService  extends IService<RecordApply> {
    /**
    * <p>Description: 查询申请记录</p>
    * @version 1.0 2018/7/17
    * @author jin
    */
    List<RecordApply> findRecordApply(RecordApply recordApply);
    /**
    * <p>Description:按用户查询申领记录 </p>
    * @version 1.0 2018/7/17
    * @author jin
    */
    List<RecordApply> findRecordApplyByUser(RecordApply recordApply, User user);
    /**
    * <p>Description: 申请记录单</p>
    * @version 1.0 2018/7/16
    * @author jin
    */
    void applyRecordSheet(RecordSheet sheet, int quantity, String printerName, User user);
    /**
    * <p>Description: 申请记录本</p>
    * @version 1.0 2018/7/16
    * @author jin
    */
    void applyRecordBook(RecordBook book, int quantity, String printerName, User user) throws IOException, PrinterException;

    /**
     * <p>Description: 申请记录本/记录单</p>
     * @version 1.0 2018/7/16
     * @author jin
     */
    void applyRecordApply(RecordApply apply, User currentUser) throws IOException, PrinterException;

    /*
    查询首页数据
     */
    HomeFrontVo queryHomeFrontVo(User user);
}

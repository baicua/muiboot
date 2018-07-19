package com.baicua.bsds.dao;

import com.baicua.bsds.domain.RecordBook;
import com.baicua.shiro.common.config.MyMapper;

import java.util.List;

public interface RecordBookMapper extends MyMapper<RecordBook> {
    List<RecordBook> findRecordBook(RecordBook recordBook);
}
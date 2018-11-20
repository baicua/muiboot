package com.muiboot.activiti.service.instance;

import com.muiboot.activiti.model.process.ProcessInstanceVo;
import com.muiboot.core.common.domain.QueryRequest;

import java.util.List;

public interface InstanceService {
  public List<ProcessInstanceVo> findByPage(QueryRequest request);
}

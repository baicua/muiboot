package com.muiboot.activiti.service.definition;

import com.muiboot.activiti.model.process.ProcessInstanceVo;
import com.muiboot.core.domain.QueryRequest;

import java.util.List;

public interface InstanceService {
  public List<ProcessInstanceVo> findByPage(QueryRequest request);
}

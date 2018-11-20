package com.muiboot.activiti.service.process;
import com.muiboot.activiti.model.process.ProcessDefinitionVo;
import com.muiboot.core.common.domain.QueryRequest;

import javax.xml.stream.XMLStreamException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface ProcessService { 
  public List<ProcessDefinitionVo> findByPage(QueryRequest request);
  public void convertToModel(String processDefinitionId)throws UnsupportedEncodingException, XMLStreamException;
}


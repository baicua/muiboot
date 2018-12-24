package com.muiboot.activiti.service.definition;
import com.muiboot.activiti.entity.process.ProcessDefinitionDeploy;
import com.muiboot.core.entity.QueryRequest;

import javax.xml.stream.XMLStreamException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface ProcessService { 
  public List<ProcessDefinitionDeploy> findByPage(QueryRequest request);
  public void convertToModel(String processDefinitionId)throws UnsupportedEncodingException, XMLStreamException;
}


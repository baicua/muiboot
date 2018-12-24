package com.muiboot.activiti.service.definition;

import com.muiboot.core.entity.QueryRequest;
import org.activiti.engine.repository.Model;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface ModelService {
  public List<Model> findByPage(QueryRequest request);
  public void deployModel(String modelId);
  public String createModel(String name, String key, String description)throws UnsupportedEncodingException;
  
}

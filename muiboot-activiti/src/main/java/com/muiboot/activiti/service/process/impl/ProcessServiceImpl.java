package com.muiboot.activiti.service.process.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.muiboot.activiti.model.process.ProcessDefinitionVo;
import com.muiboot.activiti.service.process.ProcessService;
import com.muiboot.core.common.domain.QueryRequest;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProcessServiceImpl implements ProcessService {

  @Autowired
  private RepositoryService repositoryService;

  @Override
  public List<ProcessDefinitionVo> findByPage(QueryRequest request){
    ProcessDefinitionQuery query=repositoryService.createProcessDefinitionQuery();
    List<ProcessDefinition> processDefinitionList =new ArrayList<ProcessDefinition>();
    List<ProcessDefinitionVo> pdvoList = new ArrayList<ProcessDefinitionVo>();
    processDefinitionList = query.orderByDeploymentId().desc()
            .listPage((request.getPage() -1)* request.getLimit(), request.getLimit());
    for (ProcessDefinition pd : processDefinitionList) {
      String deploymentId = pd.getDeploymentId();
      Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
      ProcessDefinitionVo pdvo=new ProcessDefinitionVo(pd.getId(),deploymentId
          ,pd.getName(),pd.getKey(),pd.getVersion(),deployment.getDeploymentTime()
          ,pd.getResourceName(),pd.getDiagramResourceName());   
      pdvoList.add(pdvo);
    }
    long count=query.count();
    return pdvoList;
  }
  /**
   * 上传文件转换成模型
   */
  @Override
  public void convertToModel(String processDefinitionId) throws UnsupportedEncodingException, XMLStreamException {
    ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
    InputStream bpmnStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), processDefinition.getResourceName());
    XMLInputFactory xif = XMLInputFactory.newInstance();
    InputStreamReader in = new InputStreamReader(bpmnStream, "UTF-8");
    XMLStreamReader xtr = xif.createXMLStreamReader(in);
    BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);

    BpmnJsonConverter converter = new BpmnJsonConverter();
    com.fasterxml.jackson.databind.node.ObjectNode modelNode = converter.convertToJson(bpmnModel);
    Model modelData = repositoryService.newModel();
    modelData.setKey(processDefinition.getKey());
    modelData.setName(processDefinition.getName());
    modelData.setCategory(processDefinition.getDeploymentId());

    ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
    modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, processDefinition.getName());
    modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
    modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, processDefinition.getDescription());
    modelData.setMetaInfo(modelObjectNode.toString());

    repositoryService.saveModel(modelData);
    repositoryService.addModelEditorSource(modelData.getId(), modelNode.toString().getBytes("utf-8"));
  }

}

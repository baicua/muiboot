package com.muiboot.activiti.service.definition.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.muiboot.activiti.entity.process.ProcessDefinitionDeploy;
import com.muiboot.activiti.service.definition.ProcessService;
import com.muiboot.activiti.util.ActivitiUtil;
import com.muiboot.core.entity.QueryRequest;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ProcessServiceImpl implements ProcessService {

  @Autowired
  private RepositoryService repositoryService;

  @Override
  public List<ProcessDefinitionDeploy> findByPage(QueryRequest request){

    Page<ProcessDefinitionDeploy> page = PageHelper.getLocalPage();
    DeploymentQuery deploymentQuery=repositoryService.createDeploymentQuery();
    List<Deployment> deployments=deploymentQuery.orderByDeploymenTime().desc()
            .listPage((request.getPage() -1)* request.getLimit(), request.getLimit());
    List<ProcessDefinitionDeploy> processDefinitionDeploys=null;
    if (CollectionUtils.isNotEmpty(deployments)){
      Set<String> deploymentIds=deployments.stream().map(Deployment::getId).collect(Collectors.toSet());
      List<ProcessDefinition> processDefinitions=repositoryService.createProcessDefinitionQuery().deploymentIds(deploymentIds).list();
      processDefinitionDeploys=ActivitiUtil.createProcessDefinitionDeploy(deployments,processDefinitions);
    }
    if (null!=page){
      PageHelper.clearPage();
      page.setTotal(deploymentQuery.count());
      page.addAll(processDefinitionDeploys);
      return page;
    }
    return processDefinitionDeploys;
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

package com.muiboot.activiti.service.definition.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.muiboot.activiti.listener.TaskShipListener;
import com.muiboot.activiti.service.definition.ModelService;
import com.muiboot.core.entity.QueryRequest;
import com.muiboot.core.exception.BusinessException;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.persistence.entity.DeploymentEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ModelQuery;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ModelServiceImpl implements ModelService {
  @Autowired
  private RepositoryService repositoryService;

  @Autowired
  private TaskShipListener taskShipListener;

  @Override
  public List<Model> findByPage(QueryRequest request){
    ModelQuery modelQuery = repositoryService.createModelQuery();
    Page<Model> page = PageHelper.getLocalPage();
    if (null!=page){
      PageHelper.clearPage();
      page.setTotal(modelQuery.count());
      page.addAll(modelQuery.orderByLastUpdateTime().desc().listPage((request.getPage() -1)* request.getLimit(), request.getLimit()));
      return page;
    }else {
      return modelQuery.orderByLastUpdateTime().desc().listPage((request.getPage() -1)* request.getLimit(), request.getLimit());
    }
   
  }

  @Override
  public void deployModel(String modelId) {
    try {
      Model modelData = repositoryService.getModel(modelId);
      ObjectNode modelNode = (ObjectNode) new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
      byte[] bpmnBytes = null;
      BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
      bpmnBytes = new BpmnXMLConverter().convertToXML(model);
      String processName = modelData.getName() + ".bpmn20.xml";
      DeploymentEntity deployment = (DeploymentEntity) repositoryService.createDeployment().name(modelData.getName()).addString(processName, new String(bpmnBytes, "UTF-8")).deploy();
      //ProcessDefinitionEntity processDefinition= (ProcessDefinitionEntity) repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
      ProcessDefinitionEntity processDefinition= deployment.getDeployedArtifacts(ProcessDefinitionEntity.class).get(0);
      Map<String, TaskDefinition> taskDefinitions=processDefinition.getTaskDefinitions();
      if (MapUtils.isNotEmpty(taskDefinitions)){
        for (Map.Entry<String, TaskDefinition> entry : taskDefinitions.entrySet()){
          TaskDefinition taskDefinition=entry.getValue();
          taskDefinition.addTaskListener(TaskListener.EVENTNAME_ALL_EVENTS,taskShipListener);
        }
      }
      modelData.setDeploymentId(deployment.getId());
      modelData.setVersion(processDefinition.getVersion());
      repositoryService.saveModel(modelData);
    } catch (Exception e) {
      throw new BusinessException("流程模型部署失败",e.getCause());
    }  
  }

  @Override
  public String createModel(String name, String key, String description) throws UnsupportedEncodingException {
    ObjectMapper objectMapper = new ObjectMapper();
    ObjectNode editorNode = objectMapper.createObjectNode();
    editorNode.put("id", "canvas");
    editorNode.put("resourceId", "canvas");
    ObjectNode stencilSetNode = objectMapper.createObjectNode();
    stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
    editorNode.set("stencilset", stencilSetNode);
    
    Model modelData = repositoryService.newModel();

    ObjectNode modelObjectNode = objectMapper.createObjectNode();
    modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, name);
    modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
    description = StringUtils.defaultString(description);
    modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
    modelData.setMetaInfo(modelObjectNode.toString());
    modelData.setName(name);
    modelData.setKey(StringUtils.defaultString(key));

    repositoryService.saveModel(modelData);
    repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));

    return modelData.getId();
  }
 

}

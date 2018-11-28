package com.muiboot.activiti.contoller.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.muiboot.activiti.model.process.ProcessInstanceVo;
import com.muiboot.activiti.service.model.ModelService;
import com.muiboot.core.common.domain.QueryRequest;
import com.muiboot.core.common.domain.ResponseBo;
import com.muiboot.core.common.web.BaseController;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.WebAsyncTask;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

/**
 * 流程模型控制器
 */
@Controller
@RequestMapping(value = "/workflow/")
public class ModelController extends BaseController {

  @Autowired
  private RepositoryService repositoryService;
  @Autowired
  private ModelService modelService;

  @RequestMapping(value = "models", method = RequestMethod.GET)
  public String getModels() {
    return "act/models";
  }

  @RequestMapping("model/list")
  @ResponseBody
  public Map<String, Object> modelList(QueryRequest request) {
    PageHelper.startPage(request.getPage(), request.getLimit());
    List<Model> list = modelService.findByPage(request);
    PageInfo<Model> pageInfo = new PageInfo<>(list);
    return getDataTable(pageInfo);
  }
  
 
  /**
   * 创建模型
   */
  @RequestMapping(value = "model/create", method = RequestMethod.POST)
  @ResponseBody
  public ResponseBo create(String name, String key, String description, HttpServletRequest request, HttpServletResponse response) {
    try {
      String modelId = modelService.createModel(name, key, description);
      //response.sendRedirect(request.getContextPath() + "/modeler.html?modelId=" + modelId);
      return ResponseBo.ok(modelId);
    } catch (Exception e) {
      logger.error("创建模型失败", e);
    }
    return ResponseBo.error("创建模型失败");
  }

  /* *//**
   * 编辑模型
   */
  @RequestMapping(value = "model/edit")
  public void edit(String modelId, HttpServletRequest request, HttpServletResponse response) {
    try {
      response.sendRedirect(request.getContextPath() + "/modeler.html?modelId=" + modelId);
    } catch (Exception e) {
      logger.error(e.toString(), e);
    }
  }

  /**
   * 根据Model部署流程
   */
  @RequestMapping(value = "model/deploy", method = RequestMethod.POST)
  public ResponseBo deploy(@RequestParam("modelId") String modelId) {
      modelService.deployModel(modelId);
      return ResponseBo.ok("部署成功");
  }
 

  /**
   * 导出model对象为指定类型
   * 
   * @param modelId
   *          模型ID
   * @param type
   *          导出文件类型(bpmn\json)
   */
  @RequestMapping(value = "model/export/{modelId}/{type}")
  public void export(@PathVariable("modelId") String modelId, @PathVariable("type") String type, HttpServletResponse response) {
    try {
      Model modelData = repositoryService.getModel(modelId);
      BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
      byte[] modelEditorSource = repositoryService.getModelEditorSource(modelData.getId());

      JsonNode editorNode = new ObjectMapper().readTree(modelEditorSource);
      BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);

      // 处理异常
      if (bpmnModel.getMainProcess() == null) {
        response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
        response.getOutputStream().println("no main process, can't export for type: " + type);
        response.flushBuffer();
        return;
      }
      String filename = "";
      byte[] exportBytes = null;
      String mainProcessId = bpmnModel.getMainProcess().getId();
      switch (type) {
      case "bpmn": {
        BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
        exportBytes = xmlConverter.convertToXML(bpmnModel);
        filename = mainProcessId + ".bpmn20.xml";
        break;
      }
      case "json": {
        exportBytes = modelEditorSource;
        filename = mainProcessId + ".json";
        break;
      }
      }
      ByteArrayInputStream in = new ByteArrayInputStream(exportBytes);
      IOUtils.copy(in, response.getOutputStream());

      response.setHeader("Content-Disposition", "attachment; filename=" + filename);
      response.flushBuffer();
    } catch (Exception e) {
      logger.error("导出model的xml文件失败：modelId={}, type={}", modelId, type, e);
    }

  }
  @RequestMapping(value = "model/del", method = RequestMethod.GET)
  public ResponseBo del(@RequestParam("id") String[] modelIds) {
    try {
      for (int i = 0; i < modelIds.length; i++) {
        repositoryService.deleteModel(modelIds[i]);
      }
      ResponseBo.ok("删除成功");
    }catch (Exception e) {
      logger.error(e.toString(), e);
      return ResponseBo.error("删除失败");
    }
    return ResponseBo.ok("部署成功");
  }

}

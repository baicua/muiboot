package com.muiboot.activiti.contoller.process;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.muiboot.activiti.model.process.ProcessDefinitionDeploy;
import com.muiboot.activiti.service.process.ProcessService;
import com.muiboot.core.common.domain.QueryRequest;
import com.muiboot.core.common.domain.ResponseBo;
import com.muiboot.core.common.web.BaseController;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 * 流程定义管理
 */
@Controller
@RequestMapping(value = "/workflow/process")
public class ProcessController extends BaseController {

  @Autowired
  private RepositoryService repositoryService;
  @Autowired
  private ProcessService processService;

  @RequestMapping(value = "", method = RequestMethod.GET)
  public String process() {
    return "act/process";
  }

  @RequestMapping("list")
  @ResponseBody
  public Map<String, Object> processList(QueryRequest request) {
    PageHelper.startPage(request.getPage(), request.getLimit());
    List<ProcessDefinitionDeploy> list = processService.findByPage(request);
    PageInfo<ProcessDefinitionDeploy> pageInfo = new PageInfo<>(list);
    return getDataTable(pageInfo);
  }

  /**
   * 删除部署的流程，级联删除流程实例
   *
   *          流程部署ID
   */
  @RequestMapping(value = "del", method = RequestMethod.POST)
  @ResponseBody
  public ResponseBo del(@RequestParam("ids") String[] deploymentIds) {
    try {
      for (int i = 0; i < deploymentIds.length; i++) {
        repositoryService.deleteDeployment(deploymentIds[i]);
      }
      ResponseBo.ok("删除成功");
    }catch (Exception e) {
      logger.error(e.toString(), e);
      return ResponseBo.error("删除失败:"+e.getMessage());
    }
    return ResponseBo.ok("删除成功");
  }
  @RequestMapping(value = "uploadModel")
  @ResponseBody
  public void uploadModel(@RequestParam(value = "modelFile", required = false) MultipartFile file) {

    try {
      String fileName = file.getOriginalFilename();
      InputStream fileInputStream = file.getInputStream();
      Deployment deployment = null;
      String extension = FilenameUtils.getExtension(fileName);
      if (extension.equals("zip") || extension.equals("bar")) {
        ZipInputStream zip = new ZipInputStream(fileInputStream);
        deployment = repositoryService.createDeployment().addZipInputStream(zip).deploy();
      } else {
        deployment = repositoryService.createDeployment().addInputStream(fileName, fileInputStream).deploy();
      }
      if (deployment != null) {
        logger.info("上传成功");
      }
    } catch (Exception e) {
      logger.error("上传失败");
    }

  }

  @RequestMapping(value = "convertToModel")
  @ResponseBody
  public void convertToModel(String processDefinitionId) {

    try {
      processService.convertToModel(processDefinitionId);
      logger.info("转换成功");
    } catch (Exception e) {
      logger.error("转换模型失败", e);

    }

  }

  /**
   * 读取资源，通过部署ID
   *
   * @param processDefinitionId
   *          流程定义
   * @param resourceType
   *          资源类型(xml|image)
   * @throws Exception
   */
  @RequestMapping(value = "resource/read")
  public void loadByDeployment(@RequestParam("processDefinitionId") String processDefinitionId, @RequestParam("resourceType") String resourceType, HttpServletResponse response) throws Exception {
    response.setCharacterEncoding("UTF-8");
    ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
    String resourceName = "";
    if (resourceType.equals("image")) {
      resourceName = processDefinition.getDiagramResourceName();
    } else if (resourceType.equals("xml")) {
      resourceName = processDefinition.getResourceName();
    }
    InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), resourceName);
    byte[] b = new byte[1024];
    int len = -1;
    while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
      response.getOutputStream().write(b, 0, len);
    }
  }
}

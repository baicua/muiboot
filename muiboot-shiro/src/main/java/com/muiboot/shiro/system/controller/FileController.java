package com.muiboot.shiro.system.controller;

import com.muiboot.shiro.common.controller.BaseController;
import com.muiboot.shiro.common.domain.ResponseBo;
import com.muiboot.shiro.common.util.FileUtils;
import com.muiboot.shiro.system.domain.AttNexus;
import com.muiboot.shiro.system.service.IAttNexusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;


/**
 * Created by 75631 on 2018/7/21.
 */
@Controller
public class FileController  extends BaseController {

    @Value("${file.directory}")
    private String dir;
    @Autowired
    private IAttNexusService attNexusService;

    @RequestMapping("upload/{nextDir}")
    @ResponseBody
    public ResponseBo uploadRecordTemplate(@PathVariable String nextDir,@RequestParam MultipartFile[] files) {
        logger.info("文件上传目录："+nextDir);
        try {
            if (null!=files){
                for (int i=0;i<files.length;i++){
                    MultipartFile file=files[i];
                    if (!file.isEmpty()) {
                        String contentType = file.getContentType();
                        String fileName = file.getOriginalFilename();
                        String filePath = dir;
                        String prefix=fileName.substring(fileName.lastIndexOf("."));
                        try {
                            String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
                            String path=FileUtils.uploadFile(file.getBytes(), filePath+"/"+nextDir+"/", uuid+prefix);
                            Long attId=attNexusService.saveEntity(path,uuid+prefix,fileName);
                            return ResponseBo.ok(attId);
                        } catch (Exception e) {
                            return ResponseBo.error("文件上传失败，请联系网站管理员！");
                        }
                    }
                }
            }
            return ResponseBo.ok("文件上传成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBo.error("文件上传失败，请联系网站管理员！");
        }
    }

    @GetMapping("/getAtt/{id}")
    @ResponseBody
    public ResponseBo getAtt(@PathVariable Long id) {
        try{
            AttNexus attNexus = attNexusService.selectByKey(id);
            if (attNexus==null)
                return ResponseBo.error("文件下载失败，文件不存在！");
            File file = new File(attNexus.getAttDir()+"/"+attNexus.getAttOriName());
            String pdf = FileUtils.PDFToBase64(file);
            return ResponseBo.ok(pdf);
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseBo.error("文件下载失败，请联系网站管理员！");
        }
    }
}

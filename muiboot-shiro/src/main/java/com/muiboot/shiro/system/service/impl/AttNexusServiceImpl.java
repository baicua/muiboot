package com.muiboot.shiro.system.service.impl;

import com.muiboot.shiro.common.service.impl.BaseService;
import com.muiboot.shiro.common.util.FileUtils;
import com.muiboot.shiro.system.domain.AttNexus;
import com.muiboot.shiro.system.service.IAttNexusService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by 75631 on 2018/7/21.
 */
@Service("attNexusService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AttNexusServiceImpl extends BaseService<AttNexus> implements IAttNexusService {
    @Override
    public Long saveEntity(String filePath,String copyName, String fileName) {
        AttNexus attNexus = new AttNexus();
        attNexus.setAttDir(filePath);
        attNexus.setAttOriName(copyName);
        attNexus.setAttRealName(fileName);
        //attNexus.getAttUrl();
        this.save(attNexus);
        return attNexus.getAttId();
    }

    @Override
    public Long saveFiles(MultipartFile[] files,String dir) throws IOException {
        if (files==null)
            throw new NullPointerException("文件上传失败:没有待上传文件");
        for (int i=0;i<files.length;i++){
            MultipartFile file=files[i];
            if (!file.isEmpty()) {
                String contentType = file.getContentType();
                String fileName = file.getOriginalFilename();
                String prefix=fileName.substring(fileName.lastIndexOf("."));
                String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
                String path= FileUtils.uploadFile(file.getBytes(), dir+"/", uuid+prefix);
                Long attId=this.saveEntity(path,uuid+prefix,fileName);
                return attId;
            }
        }
        throw new IllegalAccessError("文件上传失败");
    }
}

package com.baicua.shiro.system.service;

import com.baicua.shiro.common.service.IService;
import com.baicua.shiro.system.domain.AttNexus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by 75631 on 2018/7/21.
 */
public interface IAttNexusService extends IService<AttNexus> {
    Long saveEntity(String filePath,String copyName, String fileName);

    Long saveFiles(MultipartFile[] files,String dir) throws IOException;
}

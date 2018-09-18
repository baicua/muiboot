package com.muiboot.shiro.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

@Controller
public class CommonController {

	@RequestMapping("common/download")
	public void fileDownload(String fileName, Boolean delete, HttpServletResponse response) {
		String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
		try {
			response.setCharacterEncoding("utf-8");
			response.setContentType("multipart/form-data");
			response.setHeader("Content-Disposition",
					"attachment;fileName=" + java.net.URLEncoder.encode(realFileName, "utf-8"));
			String filePath = "file/" + fileName;
			File file = new File(filePath);
			InputStream inputStream = new FileInputStream(file);
			OutputStream os = response.getOutputStream();
			byte[] b = new byte[2048];
			int length;
			while ((length = inputStream.read(b)) > 0) {
				os.write(b, 0, length);
			}
			os.close();
			inputStream.close();
			if (delete && file.exists()) file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

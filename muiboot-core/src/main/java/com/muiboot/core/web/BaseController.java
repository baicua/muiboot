package com.muiboot.core.web;

import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import java.util.HashMap;
import java.util.Map;


/**
* <p>Description: 控制层基类</p>
* @version 1.0 2018/10/12
* @author jin
*/
public  abstract class BaseController {
	protected  final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected Map<String, Object> getDataTable(PageInfo<?> pageInfo) {
		Map<String, Object> rspData = new HashMap<>();
		rspData.put("code", 0);
		rspData.put("data", pageInfo.getList());
		rspData.put("count", pageInfo.getTotal());
		return rspData;
	}
}

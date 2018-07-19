package com.baicua.shiro.system.service;

import java.util.List;

import com.baicua.shiro.common.service.IService;
import com.baicua.shiro.system.domain.SysLog;

public interface LogService extends IService<SysLog> {
	
	List<SysLog> findAllLogs(SysLog log);
	
	void deleteLogs(String logIds);
}

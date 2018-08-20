package com.muiboot.shiro.system.service;

import java.util.List;

import com.muiboot.shiro.common.service.IService;
import com.muiboot.shiro.system.domain.SysLog;

public interface LogService extends IService<SysLog> {
	
	List<SysLog> findAllLogs(SysLog log);
	
	void deleteLogs(String logIds);
}

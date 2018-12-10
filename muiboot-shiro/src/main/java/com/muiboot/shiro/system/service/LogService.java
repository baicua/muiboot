package com.muiboot.shiro.system.service;

import java.util.List;

import com.muiboot.core.service.IService;
import com.muiboot.shiro.system.domain.SysLog;

public interface LogService extends IService<SysLog> {
	
	List<SysLog> findAllLogs(SysLog log);
	
	void deleteLogs(String logIds);

	/**
	 * <p>Title: </p>
	 * <p>Description: 交给调度器执行</p>*
	 * @author jin
	 * @version 1.0 2018/9/3
	 */
	void sendScheduled(SysLog log);
}

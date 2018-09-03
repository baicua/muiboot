package com.muiboot.shiro.system.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.muiboot.shiro.common.service.impl.BaseService;
import com.muiboot.shiro.common.util.exec.ExecutorsUtil;
import com.muiboot.shiro.system.dao.LogMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.muiboot.shiro.system.domain.SysLog;
import com.muiboot.shiro.system.service.LogService;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service("logService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class LogServiceImpl extends BaseService<SysLog> implements LogService {
	@Autowired
	private LogMapper logMapper;
	private List<SysLog>  scheduledList= Collections.synchronizedList(new ArrayList<>());
	private ScheduledExecutorService scheduledThreadPool = ExecutorsUtil.getInstance().getScheduledThreadPool();
	private ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
	@Override
	public List<SysLog> findAllLogs(SysLog log) {
		try {
			Example example = new Example(SysLog.class);
			Criteria criteria = example.createCriteria();
			if (StringUtils.isNotBlank(log.getUsername())) {
				criteria.andCondition("username=", log.getUsername().toLowerCase());
			}
			if (StringUtils.isNotBlank(log.getOperation())) {
				criteria.andCondition("operation like", "%" + log.getOperation() + "%");
			}
			if (StringUtils.isNotBlank(log.getTimeField())) {
				String[] timeArr = log.getTimeField().split("~");
				criteria.andCondition("date_format(CREATE_TIME,'%Y-%m-%d') >=", timeArr[0]);
				criteria.andCondition("date_format(CREATE_TIME,'%Y-%m-%d') <=", timeArr[1]);
			}
			example.setOrderByClause("create_time desc");
			return this.selectByExample(example);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	@Override
	@Transactional
	public void deleteLogs(String logIds) {
		List<String> list = Arrays.asList(logIds.split(","));
		this.batchDelete(list, "id", SysLog.class);
	}

	/**
	 * <p>Title: </p>
	 * <p>Description: 交给调度器执行</p>*
	 *
	 * @param log
	 * @author jin
	 * @version 1.0 2018/9/3
	 */
	@Override
	public void sendScheduled(SysLog log) {
		//三秒后执行
		scheduledList.add(log);
		if (scheduledList.size()==1){
			scheduledThreadPool.schedule(new Runnable() {
				@Override
				public void run() {
					List<SysLog> copy = new ArrayList<>();
					CollectionUtils.addAll(copy,scheduledList.iterator());
					scheduledList.clear();
					logMapper.insertList(copy);
				}
			},3000, TimeUnit.MILLISECONDS);
		}
	}

}

package com.muiboot.core.common.service.impl;

import java.util.List;

import com.muiboot.core.common.service.IService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;
/**
* <p>Description: service基类</p>
* @version 1.0 2018/10/12
* @author jin
*/
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public abstract class BaseService<T> implements IService<T> {
	protected  final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	protected Mapper<T> mapper;

	public Mapper<T> getMapper() {
		return mapper;
	}

	@Override
	public List<T> selectAll() {
		return mapper.selectAll();
	}

	@Override
	public T selectByKey(Object key) {
		return mapper.selectByPrimaryKey(key);
	}

	@Override
	public int save(T entity) {
		return mapper.insert(entity);
	}

	@Override
	@Transactional
	public int delete(Object key) {
		return mapper.deleteByPrimaryKey(key);
	}

	@Override
	@Transactional
	public int batchDelete(List<String> list, String property, Class<T> clazz) {
		Example example = new Example(clazz);
		example.createCriteria().andIn(property, list);
		return this.mapper.deleteByExample(example);
	}

	@Override
	@Transactional
	public int updateAll(T entity) {
		return mapper.updateByPrimaryKey(entity);
	}

	@Override
	@Transactional
	public int updateNotNull(T entity) {
		return mapper.updateByPrimaryKeySelective(entity);
	}

	@Override
	public List<T> selectByExample(Object example) {
		return mapper.selectByExample(example);
	}
}

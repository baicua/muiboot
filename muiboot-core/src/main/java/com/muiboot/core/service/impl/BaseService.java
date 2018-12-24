package com.muiboot.core.service.impl;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import com.muiboot.core.annotation.Format;
import com.muiboot.core.exception.BusinessException;
import com.muiboot.core.service.IService;
import com.muiboot.core.util.FormatUtil;
import org.apache.commons.lang.ArrayUtils;
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
	private static Field[] fileds=null;
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
	public int save(T entity, Map<String,Object> param) {
		if (ArrayUtils.isEmpty(fileds)){
			fileds=entity.getClass().getDeclaredFields();
		}
		if (ArrayUtils.isNotEmpty(fileds)){
			for (int i=0,l=fileds.length;i<l;i++){
				Field field=fileds[i];
				if (field.isAnnotationPresent(Format.class)){
					Format format=field.getAnnotation(Format.class);
					String name=format.name();
					String key=format.name();
					String expression=format.expression();
					param.put("seq","2");//todo
					String formatStr= FormatUtil.toString(expression,param);
					try {
						field.setAccessible(true);
						field.set(entity,formatStr);
					} catch (IllegalAccessException e) {
						logger.error("赋值失败",e.getCause());
						throw new BusinessException("赋值失败",e.getCause());
					}
				}
			}
		}
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

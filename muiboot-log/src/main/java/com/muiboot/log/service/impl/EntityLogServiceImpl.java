package com.muiboot.log.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muiboot.core.entity.BaseModel;
import com.muiboot.core.service.impl.BaseService;
import com.muiboot.log.dao.EntityLogMapper;
import com.muiboot.log.entity.EntityLog;
import com.muiboot.log.service.IEntityLogService;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.Reflector;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.reflection.invoker.Invoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.mapperhelper.EntityHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * <p>Description: </p>
 *
 * @version 1.0 2019/2/26
 */
@Service("entityLogService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class EntityLogServiceImpl extends BaseService<EntityLog> implements IEntityLogService {
    @Autowired
    ObjectMapper jsonMapper;
    @Autowired
    private EntityLogMapper entityLogMapper;
    @Override
    @Async
    public void pushLog(Object origin, Object target) {
        Class clazzo=origin.getClass();
        Class clazzt=target.getClass();
        if (!clazzo.getName().equals(clazzt.getName())){
            logger.error("原类型[{}]和修改类型[{}]不同",clazzo.getName(),clazzt.getName());
            return;
        }
        List<EntityLog> entityLogs=new ArrayList<>();
        MetaObject metaObject= SystemMetaObject.forObject(origin);
        ReflectorFactory reflectorFactory=metaObject.getReflectorFactory();
        final Reflector reflectoro=reflectorFactory.findForClass(clazzo);
        final Reflector reflectort=reflectorFactory.findForClass(clazzt);
        Set<EntityColumn> entityColumns= EntityHelper.getColumns(clazzo);
        EntityTable table=EntityHelper.getEntityTable(clazzo);
        final Long logNumber=new Date().getTime();
        String keyTemp=null;
        String[] keys=table.getKeyProperties();
        if (null!=keys&&keys.length==1){
            Object keyo= null;
            try {
                keyo = reflectoro.getGetInvoker(keys[0]).invoke(origin,null);
            } catch (IllegalAccessException e) {
                logger.error("参数错误",e);
            } catch (InvocationTargetException e) {
                logger.error("反射方法异常", e);
            }
            keyTemp=ObjectUtils.toString(keyo);
        }
        final String key=keyTemp;
        entityColumns.forEach(a->{
            Invoker invokero=reflectoro.getGetInvoker(a.getProperty());
            Invoker invokert=reflectort.getGetInvoker(a.getProperty());
            Object originInvo= null;
            Object targetInvo=null;
            String originInStr= null;
            String targetInStr=null;
            try {
                originInvo = invokero.invoke(origin,null);
                targetInvo=invokert.invoke(target,null);
                if (originInvo instanceof List||originInvo instanceof BaseModel){
                    originInStr=jsonMapper.writeValueAsString(originInvo);
                    targetInStr=jsonMapper.writeValueAsString(targetInvo);
                }else {
                    originInStr=ObjectUtils.toString(originInvo);
                    targetInStr=ObjectUtils.toString(targetInvo);
                }
                if (originInStr!=null&&originInStr.length()>1000){
                    logger.warn("修改批次:{},原数据:{}",logNumber,originInStr);
                    originInStr.substring(0,1000);
                }
                if (targetInStr!=null&&targetInStr.length()>1000){
                    logger.warn("修改批次:{},修改数据:{}",logNumber,targetInStr);
                    targetInStr.substring(0,1000);
                }
            } catch (IllegalAccessException e) {
                logger.error("参数错误",e);
            } catch (InvocationTargetException e) {
                logger.error("反射方法异常",e);
            } catch (JsonProcessingException e) {
                logger.error("JSON转换异常",e);
            }
            if (StringUtils.equals(originInStr,targetInStr)){
                return;
            }
            EntityLog entityLog=new EntityLog();
            entityLog.setTableName(table.getName());
            entityLog.setTableComment(null);
            entityLog.setColName(a.getColumn());
            entityLog.setColComment(a.getProperty());
            entityLog.setLogNumber(logNumber);
            entityLog.setLogOriginId(key);
            entityLog.setOriginVal(originInStr);
            entityLog.setTagetVal(targetInStr);
            entityLogs.add(entityLog);
        });
        entityLogMapper.insertByBatch(entityLogs);
        //EntityHelper.getColumns(clazzt);
        //List<EntityField> originFields=FieldHelper.getFields(clazzo);
        //List<EntityField> targetFields=FieldHelper.getFields(clazzt);

    }
}

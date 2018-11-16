package com.muiboot.core.common.mapper.provider;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.SqlHelper;
import tk.mybatis.mapper.provider.SpecialProvider;

import java.util.Iterator;
import java.util.Set;

/**
 * 支持ORACLE和mysql批量插入
 */
public class BatchProvider extends SpecialProvider {
    public BatchProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public String insertByBatch(MappedStatement ms) {
        if (this.getIDENTITY().equalsIgnoreCase("mysql")){
            return this.insertList(ms);
        }
        Class entityClass = this.getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.insertIntoTable(entityClass, this.tableName(entityClass)));
        sql.append(SqlHelper.insertColumns(entityClass, true, false, false));
        sql.append("<foreach collection=\"list\" item=\"record\" separator=\" UNION ALL\" >");
        sql.append("<trim prefix=\"SELECT\" suffix=\" FROM DUAL\" suffixOverrides=\",\">");
        Set columnList = EntityHelper.getColumns(entityClass);
        Iterator var5 = columnList.iterator();

        while(var5.hasNext()) {
            EntityColumn column = (EntityColumn)var5.next();
            if(!column.isId() && column.isInsertable()) {
                sql.append(column.getColumnHolder("record") + ",");
            }
        }
        sql.append("</trim>");
        sql.append("</foreach>");
        return sql.toString();
    }

    public String insertUseGeneratedKeys(MappedStatement ms) {
        Class entityClass = this.getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.insertIntoTable(entityClass, this.tableName(entityClass)));
        sql.append(SqlHelper.insertColumns(entityClass, true, false, false));
        sql.append(SqlHelper.insertValuesColumns(entityClass, true, false, false));
        return sql.toString();
    }
}

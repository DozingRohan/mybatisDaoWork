package com.dozingrohan.ds.api;

import com.dozingrohan.ds.api.model.BasePo;
import com.dozingrohan.ds.api.model.PageResult;

import java.util.List;
import java.util.Map;

/**
 * @Description: 持久层基础对象,
 * 可以基于此接口做 mybatis/hibernate/jpa/jdbctemplate/自定义sql 等多种实现，
 * 可以基于此接口做 各种异构数据库的操作，例如非关系型数据库实现，文件数据库实现
 * @author:		lipj27423
 * @version: 	0.0.1
 * @createDate: 2020年6月24日 上午11:23:40
 */
public interface DaoSupport {
    
	/*
     * 统计查询
     */
    <T extends BasePo> Integer count(T parameter);
    
    <T extends BasePo> Integer count(String statementPostfix, T parameter);
    
    Integer count(String statementPostfix, Map<String, Object> parameter);
    
    /*
     * 单一对象查询
     * statementPostfix：自定义sql语句id
     * map-parameter：自定义操作时参数
     */
    <T extends BasePo> T selectOne(T parameter);
    
    <T extends BasePo> T selectOne(String statementPostfix, T parameter);
    
    <T extends BasePo> T selectOne(String statementPostfix, Map<String, Object> parameter);
    
    /*
     * 记录行锁查询
     * statementPostfix：自定义sql语句id
     * map-parameter：自定义操作时参数
     */
    <T extends BasePo> T selectForUpdate(T parameter);

    <T extends BasePo> T selectForUpdate(String statementPostfix, T parameter);
    
    /*
     * 新增记录
     * statementPostfix：自定义sql语句id
     * map-parameter：自定义操作时参数
     */
    <T extends BasePo> int insert(T parameter);
    
    <T extends BasePo> int insert(List<T> entity);
    
    /*
     * 更新记录
     * statementPostfix：自定义sql语句id
     * map-parameter：自定义操作时参数
     */
    <T extends BasePo> int update(T parameter);
    
    <T extends BasePo> int update(T setParameter, T whereParameter);
    
    <T extends BasePo> int update(String statementPostfix, T setParameter, T whereParameter);
    
    <T extends BasePo> int update(String statementPostfix, T parameter);
    
    int update(String statementPostfix, Map<String, Object> parameter);
    
    /*
     * 删除
     * statementPostfix：自定义sql语句id
     * map-parameter：自定义操作时参数
     */
    <T extends BasePo> int delete(T parameter);
    
    <T extends BasePo> int delete(String statementPostfix, T parameter);
    
    int delete(String statementPostfix, Map<String, Object> parameter);
    
    /*
     * 查列表，不分页
     * statementPostfix：自定义sql语句id
     * map-parameter：自定义操作时参数
     */
    <T extends BasePo> List<T> selectList(T entity);
    
    <T extends BasePo> List<T> selectList(String statementPostfix, T entity);
    
    <T extends BasePo> List<T> selectList(String statementPostfix, Map<String, Object> parameter);
    
    <T extends BasePo> List<T> selectList(T entity, int pageIndex, int pageSize);
    
    /*
     * 查列表，分页
     * statementPostfix：自定义sql语句id
     * map-parameter：自定义操作时参数
     */
    <T extends BasePo> List<T> selectList(String statementPostfix, T entity, int pageIndex,int pageSize);
    
    <T extends BasePo> List<T> selectList(String statementPostfix, Map<String, Object> parameter, int pageIndex, int pageSize);
    
    /*
     * 查列表，带总数，根据rowboundsql做拦截，适配多种数据库方言
     * statementPostfix：自定义sql语句id
     * map-parameter：自定义操作时参数
     */
    <T extends BasePo> PageResult<T> selectPage(T entity, int pageIndex, int pageSize);
    
    <T extends BasePo> PageResult<T> selectPage(String statementPostfix, T entity, int pageIndex, int pageSize);
    
    <T extends BasePo> PageResult<T> selectPage(String statementPostfix, Map<String, Object> parameter, int pageIndex, int pageSize);
    
}

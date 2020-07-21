package com.dozingrohan.ds.api.model;

import com.dozingrohan.ds.util.ReflectUtil;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 持久化基础对象
 * @author:		lipj27423
 * @version: 	0.0.1
 * @createDate: 2020年6月24日 上午11:19:11
 */
public abstract class BasePo implements Serializable{

	private static final long serialVersionUID = 1L;

    private List<Order> orderBy;

    private List<String> cacheKeyList;

    public final List<Order> getOrderBy() {
        return orderBy;
    }

    final void setOrderBy(List<Order> orderBy) {
        this.orderBy = orderBy;
    }

    public void addOrder(Order order) {
        if (orderBy == null) {
            orderBy = new ArrayList<Order>();
        }
        orderBy.add(order);
    }

    /**
     *
     * @param propertyName 属性名称
     * @param start 低区间值
     * @param end 高区间值
     * @param <T> 属性的类型
     */
    public <T> void addBetween(String propertyName,T start, T end){
        Assert.notNull(propertyName,"propertyName not null");
        Assert.notNull(start,"between start not null");
        Assert.notNull(end,"between end not null");
        if(!start.getClass().equals(end.getClass())){
            throw new IllegalArgumentException("between 语句的参数必须为相同类型");
        }
        if(ReflectUtil.isUnSupportedComparableType(start.getClass())){
            throw new IllegalArgumentException("类型不支持 between 操作");
        }
        if(checkPropertyNameType(propertyName,start.getClass())){
            throw new IllegalArgumentException("between 语句的参数类型必须和属性类型一致");
        }
        this.setComparableValue(ReflectUtil.getSetLessMethodName(propertyName),end.getClass(),end);
        this.setComparableValue(ReflectUtil.getSetGreaterMethodName(propertyName),end.getClass(),start);
    }

    public List<String> getCacheKeyList() {
        return cacheKeyList;
    }

    public void setCacheKeyList(List<String> cacheKeyList) {
        this.cacheKeyList = cacheKeyList;
    }

    /**
     * 对比属性类型和区间类型，必须一致
     * @param propertyName
     * @param sType
     * @return
     */
    protected boolean checkPropertyNameType(String propertyName,Class sType){
        try {
            return sType.equals(this.getClass().getDeclaredField(propertyName).getType());
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("property:" + propertyName + " not found on class:" + this.getClass().getName());
        }
    }

    /**
     * 反射调用 赋值
     * @param methodName
     * @param paramType
     * @param param
     */
    protected void setComparableValue(String methodName, Class paramType, Object param){
        try {
            Method m = this.getClass().getMethod(methodName,paramType);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Method:" + methodName + " not found on class:" + this.getClass().getName());
        }
    }

}

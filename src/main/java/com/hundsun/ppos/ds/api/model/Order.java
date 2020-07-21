package com.hundsun.ppos.ds.api.model;

import java.io.Serializable;

/**
 * 
 * @Description: 排序辅助对象
 * @author:		lipj27423
 * @version: 	0.0.1
 * @createDate: 2020年6月24日 上午11:11:14
 */
public class Order implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String ASC = "ASC";
	
	private static final String DESC = "DESC";
	
	/**
	 * 属性名称
	 */
    private String propertyName;

    /**
     * 表字段名
     */
    private String columnName;

    /**
     * 升降标识
     */
    private String sort;
    
    
	public Order(String propertyName, String sort) {
		super();
		this.propertyName = propertyName;
		this.columnName = propertyName;
		this.sort = sort;
	}
	
	public static Order asc(String propertyName) {
        return new Order(propertyName, ASC);
    }
	
	public static Order desc(String propertyName) {
        return new Order(propertyName, DESC);
    }

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
	
}

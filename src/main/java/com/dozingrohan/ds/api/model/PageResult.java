package com.dozingrohan.ds.api.model;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果集对象
 * @param <T>
 */
public class PageResult<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PageResult(List<T> resultlist, long totalrecord) {
		super();
		this.resultlist = resultlist;
		this.totalrecord = totalrecord;
	}
	
	/**
	 * 分页结果集
	 */
	private List<T> resultlist;
	
	/**
	 * 数据总量
	 */
    private long totalrecord;

	public List<T> getResultlist() {
		return resultlist;
	}

	public void setResultlist(List<T> resultlist) {
		this.resultlist = resultlist;
	}

	public long getTotalrecord() {
		return totalrecord;
	}

	public void setTotalrecord(long totalrecord) {
		this.totalrecord = totalrecord;
	}
    
    
}

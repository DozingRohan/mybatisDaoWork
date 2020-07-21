package com.hundsun.ppos.ds.jpa;

import java.util.List;
import java.util.Map;
import com.hundsun.ppos.ds.api.model.BasePo;
import com.hundsun.ppos.ds.api.DaoSupport;
import com.hundsun.ppos.ds.api.model.PageResult;

/**
 * @Description: 
 * @author:		lipj27423
 * @version: 	0.0.1
 * @createDate: 2020年6月25日 下午2:57:00
 */
public class JpaDaoSupportSupport implements DaoSupport {
	
	private static final String ERRORMSG = "jpa not support yet";

	@Override
	public <T extends BasePo> Integer count(T parameter) {
		throw new IllegalStateException(ERRORMSG);
	}

	@Override
	public <T extends BasePo> Integer count(String statementPostfix, T parameter) {
		throw new IllegalStateException(ERRORMSG);
	}

	@Override
	public Integer count(String statementPostfix, Map<String, Object> parameter) {
		throw new IllegalStateException(ERRORMSG);
	}

	@Override
	public <T extends BasePo> T selectOne(T parameter) {
		throw new IllegalStateException(ERRORMSG);
	}

	@Override
	public <T extends BasePo> T selectOne(String statementPostfix, T parameter) {
		throw new IllegalStateException(ERRORMSG);
	}

	@Override
	public <T extends BasePo> T selectOne(String statementPostfix, Map<String, Object> parameter) {
		throw new IllegalStateException(ERRORMSG);
	}

	@Override
	public <T extends BasePo> T selectForUpdate(T parameter) {
		throw new IllegalStateException(ERRORMSG);
	}

	@Override
	public <T extends BasePo> T selectForUpdate(String statementPostfix, T parameter) {
		throw new IllegalStateException(ERRORMSG);
	}

	@Override
	public <T extends BasePo> int insert(T parameter) {
		throw new IllegalStateException(ERRORMSG);
	}

	@Override
	public <T extends BasePo> int insert(List<T> entity) {
		throw new IllegalStateException(ERRORMSG);
	}

	@Override
	public <T extends BasePo> int update(T parameter) {
		throw new IllegalStateException(ERRORMSG);
	}

	@Override
	public <T extends BasePo> int update(T setParameter, T whereParameter) {
		throw new IllegalStateException(ERRORMSG);
	}

	@Override
	public <T extends BasePo> int update(String statementPostfix, T setParameter, T whereParameter) {
		throw new IllegalStateException(ERRORMSG);
	}

	@Override
	public <T extends BasePo> int update(String statementPostfix, T parameter) {
		throw new IllegalStateException(ERRORMSG);
	}

	@Override
	public int update(String statementPostfix, Map<String, Object> parameter) {
		throw new IllegalStateException(ERRORMSG);
	}

	@Override
	public <T extends BasePo> int delete(T parameter) {
		throw new IllegalStateException(ERRORMSG);
	}

	@Override
	public <T extends BasePo> int delete(String statementPostfix, T parameter) {
		throw new IllegalStateException(ERRORMSG);
	}

	@Override
	public int delete(String statementPostfix, Map<String, Object> parameter) {
		throw new IllegalStateException(ERRORMSG);
	}

	@Override
	public <T extends BasePo> List<T> selectList(T entity) {
		throw new IllegalStateException(ERRORMSG);
	}

	@Override
	public <T extends BasePo> List<T> selectList(String statementPostfix, T entity) {
		throw new IllegalStateException(ERRORMSG);
	}

	@Override
	public <T extends BasePo> List<T> selectList(String statementPostfix, Map<String, Object> parameter) {
		throw new IllegalStateException(ERRORMSG);
	}

	@Override
	public <T extends BasePo> List<T> selectList(T entity, int pageIndex, int pageSize) {
		throw new IllegalStateException(ERRORMSG);
	}

	@Override
	public <T extends BasePo> List<T> selectList(String statementPostfix, T entity, int pageIndex, int pageSize) {
		throw new IllegalStateException(ERRORMSG);
	}

	@Override
	public <T extends BasePo> List<T> selectList(String statementPostfix, Map<String, Object> parameter, int pageIndex,
			int pageSize) {
		throw new IllegalStateException(ERRORMSG);
	}

	@Override
	public <T extends BasePo> PageResult<T> selectPage(T entity, int pageIndex, int pageSize) {
		throw new IllegalStateException(ERRORMSG);
	}

	@Override
	public <T extends BasePo> PageResult<T> selectPage(String statementPostfix, T entity, int pageIndex,
                                                              int pageSize) {
		throw new IllegalStateException(ERRORMSG);
	}

	@Override
	public <T extends BasePo> PageResult<T> selectPage(String statementPostfix, Map<String, Object> parameter,
                                                              int pageIndex, int pageSize) {
		throw new IllegalStateException(ERRORMSG);
	}
	
	

}

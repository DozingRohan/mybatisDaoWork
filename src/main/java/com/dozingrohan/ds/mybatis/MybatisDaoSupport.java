package com.dozingrohan.ds.mybatis;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.dozingrohan.ds.mybatis.assist.TotalrecordHelper;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dozingrohan.ds.api.model.BasePo;
import com.dozingrohan.ds.api.DaoSupport;
import com.dozingrohan.ds.api.model.PageResult;
import com.dozingrohan.ds.constant.DsConstant;
import com.dozingrohan.ds.mybatis.assist.SelectForUpdateHelper;

/**
 * @Description: mybatis持久化实现
 * @author:		lipj27423
 * @version: 	0.0.1
 * @createDate: 2020年6月25日 下午3:03:40
 */
public class MybatisDaoSupport extends SqlSessionDaoSupport implements DaoSupport {

	final static Logger logger = LoggerFactory.getLogger(MybatisDaoSupport.class);

	private static Map<String, Map<String, String>> propertyColumnMapper = new HashMap<>();

	public void init(){
		Collection<ResultMap> resultMaps = getSqlSession().getConfiguration().getResultMaps();
		Iterator<ResultMap> iterator = resultMaps.iterator();
		while(iterator.hasNext()){
			Object object = iterator.next();
			if(object instanceof ResultMap){
				ResultMap resultMap = (ResultMap)object;
				List<ResultMapping> resultMappingList = resultMap.getResultMappings();
				Map<String,String> _aTableMap = new HashMap<>();
				for(ResultMapping resultMapping : resultMappingList){
					_aTableMap.put(resultMapping.getProperty(),resultMapping.getColumn());
				}
				propertyColumnMapper.put(resultMap.getId(),_aTableMap);
			}
		}
	}

	@Override
	public <T extends BasePo> Integer count(T parameter) {
		return getSqlSession().selectOne(new StringBuilder(parameter.getClass().getName()).append(DsConstant.POSTFIX_COUNT).toString(), parameter);
	}

	@Override
	public <T extends BasePo> Integer count(String statementPostfix,T parameter) {
		return getSqlSession().selectOne(new StringBuilder(parameter.getClass().getName()).append(".").append(statementPostfix).toString(), parameter);
	}

	@Override
	public Integer count(String statementPostfix,Map<String, Object> parameter) {
		return getSqlSession().selectOne(new StringBuilder(parameter.getClass().getName()).append(".").append(statementPostfix).toString(), parameter);
	}

	@Override
	public <T extends BasePo> T selectOne(T parameter) {
		return getSqlSession().selectOne(new StringBuilder(parameter.getClass().getName()).append(DsConstant.POSTFIX_SELECTONE).toString(), parameter);
	}

	@Override
	public <T extends BasePo> T selectOne(String statementPostfix,T parameter) {
		return getSqlSession().selectOne(new StringBuilder(parameter.getClass().getName()).append(".").append(statementPostfix).toString(), parameter);
	}

	@Override
	public <T extends BasePo> T selectOne(String statementPostfix, Map<String, Object> parameter) {
		return getSqlSession().selectOne(new StringBuilder(parameter.getClass().getName()).append(".").append(statementPostfix).toString(), parameter);
	}

	@Override
	public <T extends BasePo> T selectForUpdate(T parameter) {
		try {
			SelectForUpdateHelper.setSelectForUpdate();
			return selectOne(parameter);
		} finally {
			SelectForUpdateHelper.cancelSelectForUpdate();
		}
	}

	@Override
	public <T extends BasePo> T selectForUpdate(String statementPostfix, T parameter) {
		try {
			SelectForUpdateHelper.setSelectForUpdate();
			return selectOne(statementPostfix,parameter);
		} finally {
			SelectForUpdateHelper.cancelSelectForUpdate();
		}
	}

	@Override
	public <T extends BasePo> int insert(T parameter) {
		return getSqlSession().insert(new StringBuilder(parameter.getClass().getName()).append(DsConstant.POSTFIX_INSERT).toString(),parameter);
	}

	@Override
	public <T extends BasePo> int insert(List<T> entitys) {
		int k = 0;
		for(T parameter : entitys){
			k += this.insert(parameter);
		}
		return k;
	}

	@Override
	public <T extends BasePo> int update(T parameter) {
		return getSqlSession().update(new StringBuilder(parameter.getClass().getName()).append(DsConstant.POSTFIX_UPDATE).toString(),parameter);
	}

	@Override
	public <T extends BasePo> int update(String statementPostfix, T parameter) {
		return getSqlSession().update(new StringBuilder(parameter.getClass().getName()).append(".").append(statementPostfix).toString(),parameter);
	}

	@Override
	public <T extends BasePo> int update(T setParameter, T whereParameter) {
		return this.update(new StringBuilder(setParameter.getClass().getName()).append(DsConstant.POSTFIX_UPDATE_BY_ENTITY).toString(),setParameter,whereParameter);
	}

	@Override
	public <T extends BasePo> int update(String statementPostfix, T setParameter, T whereParameter) {
		Map<String, Object> parameter = new HashMap<>();
		parameter.put("s", setParameter);
		parameter.put("w", whereParameter);
		return update(statementPostfix, parameter);
	}

	@Override
	public int update(String statementPostfix, Map<String, Object> parameter) {
		return getSqlSession().update(statementPostfix,parameter);
	}

	@Override
	public <T extends BasePo> int delete(T parameter) {
		return getSqlSession().delete(new StringBuilder(parameter.getClass().getName()).append(DsConstant.POSTFIX_DELETE).toString(),parameter);
	}

	@Override
	public <T extends BasePo> int delete(String statementPostfix, T parameter) {
		return getSqlSession().delete(new StringBuilder(parameter.getClass().getName()).append(".").append(statementPostfix).toString(),parameter);
	}

	@Override
	public int delete(String statementPostfix, Map<String, Object> parameter) {
		return getSqlSession().delete(statementPostfix,parameter);
	}

	@Override
	public <T extends BasePo> List<T> selectList(T entity) {
		return getSqlSession().selectList(new StringBuilder(entity.getClass().getName()).append(DsConstant.POSTFIX_SELECTLIST).toString(),entity);
	}

	@Override
	public <T extends BasePo> List<T> selectList(String statementPostfix, T entity) {
		return this.getSqlSession().selectList(new StringBuilder(entity.getClass().getName()).append(".").append(statementPostfix).toString(),entity);
	}

	@Override
	public <T extends BasePo> List<T> selectList(String statementPostfix, Map<String, Object> parameter) {
		return selectList(statementPostfix, parameter);
	}

	@Override
	public <T extends BasePo> List<T> selectList(T entity, int pageIndex, int pageSize) {
		return selectList(new StringBuilder(entity.getClass().getName()).append(DsConstant.POSTFIX_SELECTLIST).toString(),entity,pageIndex,pageSize);
	}

	@Override
	public <T extends BasePo> List<T> selectList(String statementPostfix,T entity,int pageIndex,int pageSize) {
		Boolean needTotalFlag = TotalrecordHelper.isNeedTotalRowCount();
		try {
			TotalrecordHelper.setNeedTotalRowCount(false);
			String preStr = null;
			if(statementPostfix.contains(entity.getClass().getName())){
				preStr = statementPostfix;
			}else{
				preStr = new StringBuilder(entity.getClass().getName()).append(".").append(statementPostfix).toString();
			}
			return getSqlSession().selectList(preStr, entity, new RowBounds(pageIndex, pageSize));
		} finally {
			TotalrecordHelper.setNeedTotalRowCount(needTotalFlag);
		}
	}

	@Override
	public <T extends BasePo> List<T> selectList(String statementPostfix,
												 Map<String, Object> parameter,
												 int pageIndex,
												 int pageSize) {
		Boolean needTotalFlag = TotalrecordHelper.isNeedTotalRowCount();
		try {
			TotalrecordHelper.setNeedTotalRowCount(false);
			return getSqlSession().selectList(statementPostfix, parameter, new RowBounds(pageIndex, pageSize));
		} finally {
			TotalrecordHelper.setNeedTotalRowCount(needTotalFlag);
		}
	}

	@Override
	public <T extends BasePo> PageResult<T> selectPage(T entity, int pageIndex, int pageSize) {
		return this.selectPage(new StringBuilder(entity.getClass().getName()).append(DsConstant.POSTFIX_SELECTLIST).toString()
				,entity,pageIndex,pageSize);
	}

	@Override
	public <T extends BasePo> PageResult<T> selectPage(String statementPostfix, T entity, int pageIndex,int pageSize) {
		Boolean needTotalFlag = TotalrecordHelper.isNeedTotalRowCount();
		try {
			TotalrecordHelper.setNeedTotalRowCount(true);
			String preStr = null;
			if(statementPostfix.contains(entity.getClass().getName())){
				preStr = statementPostfix;
			}else{
				preStr = new StringBuilder(entity.getClass().getName()).append(".").append(statementPostfix).toString();
			}
			List<T> resultList = getSqlSession().selectList(preStr, entity, new RowBounds(pageIndex, pageSize));
			Long totalrecord = TotalrecordHelper.getTotalrecord();
			return new PageResult<T>(resultList, totalrecord);
		} finally {
			TotalrecordHelper.setNeedTotalRowCount(needTotalFlag);
		}
	}

	@Override
	public <T extends BasePo> PageResult<T> selectPage(String statementPostfix,
															  Map<String, Object> parameter,
															  int pageIndex,
															  int pageSize) {
		Boolean needTotalFlag = TotalrecordHelper.isNeedTotalRowCount();
		try {
			TotalrecordHelper.setNeedTotalRowCount(true);
			List<T> resultList = getSqlSession().selectList(statementPostfix, parameter,new RowBounds(pageIndex, pageSize));
			Long totalrecord = TotalrecordHelper.getTotalrecord();
			return new PageResult<T>(resultList, totalrecord);
		} finally {
			TotalrecordHelper.setNeedTotalRowCount(needTotalFlag);
		}
	}

	public static Map<String, Map<String, String>> getPropertyColumnMapper() {
		return propertyColumnMapper;
	}

}

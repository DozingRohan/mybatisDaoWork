package com.hundsun.ppos.ds.mybatis.assist;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hundsun.ppos.ds.mybatis.MybatisDaoSupport;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.executor.statement.PreparedStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.hundsun.ppos.ds.api.model.BasePo;
import com.hundsun.ppos.ds.api.model.Order;
import com.hundsun.ppos.ds.constant.DsConstant;

/**
 * @Description: 
 * @author:		DozingRohan
 * @version: 	0.0.1
 * @createDate: 2020年6月25日 下午6:06:34
 */
@Component
public abstract class AbstractInterceptor implements Interceptor {
	
	private final static Logger logger = LoggerFactory.getLogger(AbstractInterceptor.class);
	
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		RoutingStatementHandler statementHandler = (RoutingStatementHandler) invocation.getTarget();
		PreparedStatementHandler preparedStatHandler = (PreparedStatementHandler) FieldUtils.readField(statementHandler, DELEGATE, true);
        RowBounds rowBounds = (RowBounds) FieldUtils.readField(preparedStatHandler, ROW_BOUNDS, true);
        Connection connection = (Connection) invocation.getArgs()[CONNECTION_INDEX];
        BoundSql boundSql = preparedStatHandler.getBoundSql();
        String originalSql = compress(boundSql.getSql());
        Object parameter = preparedStatHandler.getParameterHandler().getParameterObject();
        
        //排序预处理
        if (parameter instanceof BasePo) {
        	List<Order> orders = ((BasePo)parameter).getOrderBy();
        	if(!CollectionUtils.isEmpty(orders)) {
        		StringBuilder orderSql = new StringBuilder(originalSql).append(ORDERBY);
        		Map<String,String> columMap = MybatisDaoSupport.getPropertyColumnMapper()
        				.get(new StringBuilder(parameter.getClass().getName())
        						.append(DsConstant.BASE_RESULT_MAP).toString());
        		for(Order order : orders) {
        			String columName = order.getColumnName();
        			if(!CollectionUtils.isEmpty(columMap)) {
        				String mappedcolumName = columMap.get(order.getPropertyName());
            			if(StringUtils.isNotBlank(mappedcolumName)) {
            				columName = mappedcolumName;
            			}
        			}
        			orderSql.append(columName).append(order.getSort()).append(DOT);
        		}
        		originalSql = orderSql.deleteCharAt(orderSql.length()-1).toString();
        	}
        }
        //分页预处理
        if(rowBounds == null || rowBounds == RowBounds.DEFAULT) {
        	if (SelectForUpdateHelper.isSelectForUpdate()) {
                originalSql += SelectForUpdateHelper.getUpdateSql();
            }
            FieldUtils.writeField(boundSql, SQL, originalSql, true);
            return invocation.proceed();
        }
        // 1.获取总行数，将行数绑定到当前线程中
        if (TotalrecordHelper.isNeedTotalRowCount()) {
            String countSql = getCountSql(originalSql);
            TotalrecordHelper.setTotalrecord(countSql, preparedStatHandler, connection);
        }
        String pagingSql = getPagingSql(originalSql, rowBounds.getOffset(), rowBounds.getLimit());
        FieldUtils.writeField(boundSql, SQL, pagingSql, true);
        FieldUtils.writeField(rowBounds, "offset", RowBounds.NO_ROW_OFFSET, true);
        FieldUtils.writeField(rowBounds, "limit", RowBounds.NO_ROW_LIMIT, true);
        return invocation.proceed();
	}

	private String getCountSql(String originalSql) {
		originalSql = compress(originalSql);
        String upperQuerySelect = originalSql.toUpperCase();
        int orderIndex = getLastOrderInsertPoint(upperQuerySelect);
        int formIndex = getAfterFormInsertPoint(upperQuerySelect);
        String select = upperQuerySelect.substring(0, formIndex);
        if (select.indexOf("SELECT DISTINCT") != -1 || upperQuerySelect.indexOf("GROUP BY") != -1) {
            return new StringBuffer(originalSql.length())
            		.append("select count(1) count from (")
            		.append(originalSql.substring(0, orderIndex)).append(" ) t")
            		.toString();
        } else {
            return new StringBuffer(originalSql.length())
            		.append("select count(1) count ")
            		.append(originalSql.substring(formIndex, orderIndex))
            		.toString();
        }
	}
	
	/**
	 * SQL第一个正确的FROM的的插入点
	 * @author:	DozingRohan
	 */
	private int getAfterFormInsertPoint(String upperCasedSql) {
		String regex = "\\s+FROM\\s+";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(upperCasedSql);
        while (matcher.find()) {
            int fromStartIndex = matcher.start(0);
            String text = upperCasedSql.substring(0, fromStartIndex);
            if (isBracketCanPartnership(text)) {
                return fromStartIndex;
            }
        }
        return 0;
	}

	private int getLastOrderInsertPoint(String upperCasedSql) {
		int orderIndex = upperCasedSql.lastIndexOf("ORDER BY");
        if (orderIndex == -1) {
            orderIndex = upperCasedSql.length();
        } else {
            if (!isBracketCanPartnership(upperCasedSql.substring(orderIndex, upperCasedSql.length()))) {
                throw new RuntimeException("SQL 分页必须要有Order by 语句!");
            }
        }
        return orderIndex;
	}
	
	/**
	 * 判断括号"()"是否匹配,并不会判断排列顺序是否正确
	 * @author:	DozingRohan
	 */
    private static boolean isBracketCanPartnership(String text) {
        if (text == null || (getIndexOfCount(text, '(') != getIndexOfCount(text, ')'))) {
            return false;
        }
        return true;
    }
    
    /**
     * 一个字符在另一个字符串中出现的次数
     * @author:	DozingRohan
     */
    private static int getIndexOfCount(String text, char ch) {
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            count = (text.charAt(i) == ch) ? count + 1 : count;
        }
        return count;
    }

	/**
	 * 剔除sql杂质
	 * @author:	DozingRohan
	 */
	private static String compress(String sql) {
        return sql.replaceAll("[\r\n]", " ").replaceAll("\\s{2,}", " ");
    }
	
	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
		
	}
	
	/**
	 * 分页方言扩展点
	 * @author:	DozingRohan
	 */
    protected abstract String getPagingSql(String querySelect, int pageIndex, int pageSize);


	private static final String ROW_BOUNDS = "rowBounds";

    private static final String SQL = "sql";

    private static final String DELEGATE = "delegate";

    private static final String ORDERBY = " ORDER BY ";

    private static final String DOT = ",";

    private static final int CONNECTION_INDEX = 0;

}

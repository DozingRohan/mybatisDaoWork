package com.hundsun.ppos.ds.mybatis.assist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.PreparedStatementHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: 
 * @author:		DozingRohan
 * @version: 	0.0.1
 * @createDate: 2020年6月25日 下午6:32:36
 */
public class TotalrecordHelper {
	
	private static final Logger logger = LoggerFactory.getLogger(TotalrecordHelper.class);
	
    private static ThreadLocal<Long> totalRowCountHolder = new ThreadLocal<>();
    
    private static ThreadLocal<Boolean> isNeedTotalRowCountHolder = new ThreadLocal<Boolean>() {
    	@Override
        protected Boolean initialValue() {
            return Boolean.FALSE;
        }
    };
    
    public static boolean isNeedTotalRowCount() {
        return isNeedTotalRowCountHolder.get();
    }
    
    public static void setNeedTotalRowCount(Boolean isNeedTotalRowCount) {
        isNeedTotalRowCountHolder.set(isNeedTotalRowCount);
    }
    
    /**
     * 获取查询对象的总行数
     */
    static void setTotalrecord(String sql, 
    		PreparedStatementHandler statementHandler, 
    		Connection connection) throws Throwable {

        PreparedStatement countStmt = null;
        ResultSet rs = null;
        try {
            countStmt = connection.prepareStatement(sql);
            ParameterHandler parameterHandler = statementHandler.getParameterHandler();
            parameterHandler.setParameters(countStmt);
            rs = countStmt.executeQuery();
            long count = 0l;
            if (rs.next()) {
                count = rs.getLong(1);
            }
            logger.debug("count result : [{}]", count);
            totalRowCountHolder.set(count);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } finally {
                if (countStmt != null) {
                    countStmt.close();
                }
            }
        }
    }


    /**
     * @Description: 获取当前线程对应的分页查询的总行数
     * @author:		 lipj27423
     * @return
     */
    public static Long getTotalrecord() {
        return totalRowCountHolder.get();
    }

}

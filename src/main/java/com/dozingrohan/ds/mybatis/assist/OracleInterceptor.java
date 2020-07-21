package com.dozingrohan.ds.mybatis.assist;

import java.sql.Connection;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Signature;

@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class,Integer.class }) })
public class OracleInterceptor extends AbstractInterceptor {

	@Override
	protected String getPagingSql(String querySelect, int pageIndex, int pageSize) {
		StringBuffer pageSql = new StringBuffer();
        pageSql.append("select * from (select tmp_tb.*,ROWNUM row_id from (");
        pageSql.append(querySelect);
        pageSql.append(") tmp_tb where ROWNUM<=");
        pageSql.append(pageIndex * pageSize);
        pageSql.append(") where row_id>");
        pageSql.append((pageIndex - 1) * pageSize);
        return pageSql.toString();
	}

}

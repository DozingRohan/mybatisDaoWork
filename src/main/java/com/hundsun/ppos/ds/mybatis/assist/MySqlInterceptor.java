package com.hundsun.ppos.ds.mybatis.assist;

import java.sql.Connection;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Signature;

@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class,Integer.class }) })
public class MySqlInterceptor extends AbstractInterceptor{

	@Override
	protected String getPagingSql(String querySelect, int pageIndex, int pageSize) {
		StringBuilder pageSql = new StringBuilder();
        pageSql.append(querySelect);
        pageSql.append(" limit ");
        pageSql.append((pageIndex - 1) * pageSize);
        pageSql.append(",").append(pageSize);
        return pageSql.toString();
	}

}

package com.github.yangwk.jdbclient.page.parser.impl;

import com.github.yangwk.jdbclient.page.Page;
import com.github.yangwk.jdbclient.page.parser.SqlServer;

public class SqlServerParser extends AbstractParser {
    private static final SqlServer sqlServer = new SqlServer();

    //with(nolock)
    protected static final String WITHNOLOCK = ", PAGEWITHNOLOCK";

    @Override
    public String getCountSql(String sql) {
        sql = sql.replaceAll("((?i)with\\s*\\(nolock\\))", WITHNOLOCK);
        sql = super.getCountSql(sql);
        sql = sql.replaceAll(WITHNOLOCK, " with(nolock)");
        return sql;
    }

    @Override
    public String getPageSql(String sql,Page page) {
        sql = sql.replaceAll("((?i)with\\s*\\(nolock\\))", WITHNOLOCK);
        sql = sqlServer.convertToPageSql(sql, page.getStartRow(), page.getPageSize());
        sql = sql.replaceAll(WITHNOLOCK, " with(nolock)");
        return sql;
    }

    @Override
    public Object[] getPageParameter(Object[] paras, Page page) {
    	Object[] newParas = new Object[paras.length];
    	System.arraycopy(paras, 0, newParas, 0, paras.length);
    	return newParas;
    }
}
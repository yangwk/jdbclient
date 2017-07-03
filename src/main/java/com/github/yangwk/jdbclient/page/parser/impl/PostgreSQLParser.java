package com.github.yangwk.jdbclient.page.parser.impl;

import com.github.yangwk.jdbclient.page.Page;

public class PostgreSQLParser extends AbstractParser {
    @Override
    public String getPageSql(String sql,Page page) {
        StringBuilder sqlBuilder = new StringBuilder(sql.length() + 14);
        sqlBuilder.append(sql);
        sqlBuilder.append(" limit ? offset ?");
        return sqlBuilder.toString();
    }

    @Override
    public Object[] getPageParameter(Object[] paras, Page page) {
    	Object[] newParas = new Object[paras.length + 2];
    	System.arraycopy(paras, 0, newParas, 0, paras.length);
    	newParas[paras.length] = page.getPageSize();
    	newParas[paras.length+1] = page.getStartRow();
    	return newParas;
    }
}
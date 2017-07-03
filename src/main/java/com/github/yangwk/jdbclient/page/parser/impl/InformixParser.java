package com.github.yangwk.jdbclient.page.parser.impl;

import com.github.yangwk.jdbclient.page.Page;

public class InformixParser extends AbstractParser {
    @Override
    public String getPageSql(String sql,Page page) {
        StringBuilder sqlBuilder = new StringBuilder(sql.length() + 40);
        sqlBuilder.append("select skip ? first ? * from ( ");
        sqlBuilder.append(sql);
        sqlBuilder.append(" ) temp_t");
        return sqlBuilder.toString();
    }

    @Override
    public Object[] getPageParameter(Object[] paras, Page page) {
    	Object[] newParas = new Object[paras.length + 2];
    	System.arraycopy(paras, 0, newParas, 2, paras.length);
    	newParas[0] = page.getStartRow();
    	newParas[1] = page.getPageSize();
    	return newParas;
    }
}
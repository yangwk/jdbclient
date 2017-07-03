package com.github.yangwk.jdbclient.page.parser.impl;

import com.github.yangwk.jdbclient.page.Page;

public class OracleParser extends AbstractParser {
    @Override
    public String getPageSql(String sql,Page page) {
        StringBuilder sqlBuilder = new StringBuilder(sql.length() + 120);
        sqlBuilder.append("select * from ( select tmp_page.*, rownum row_id from ( ");
        sqlBuilder.append(sql);
        sqlBuilder.append(" ) tmp_page where rownum <= ? ) where row_id > ?");
        return sqlBuilder.toString();
    }

    @Override
    public Object[] getPageParameter(Object[] paras, Page page) {
    	Object[] newParas = new Object[paras.length + 2];
    	System.arraycopy(paras, 0, newParas, 0, paras.length);
    	newParas[paras.length] = page.getEndRow();
    	newParas[paras.length+1] = page.getStartRow();
    	return newParas;
    }
}
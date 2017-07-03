package com.github.yangwk.jdbclient.page.parser.impl;

import com.github.yangwk.jdbclient.page.Page;

/**
 * Take note that at least one column
 * needs to be defined for ORDER BY
 * in oder for OFFSET .. ROWS to work
 */
public class SqlServer2012Parser extends AbstractParser {
    //with(nolock)
    protected static final String WITHNOLOCK = ", PAGEWITHNOLOCK";

    @Override
    public String getCountSql(String sql) {
        sql = sql.replaceAll("((?i)with\\s*\\(nolock\\))", WITHNOLOCK);
        sql = super.getCountSql(sql);
        sql = sql.replace(WITHNOLOCK, " with(nolock)");
        return sql;
    }

    @Override
    public String getPageSql(String sql,Page page) {
        StringBuilder sqlBuilder = new StringBuilder(sql.length() + 14);
        sqlBuilder.append(sql);
        sqlBuilder.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        return sqlBuilder.toString();
    }

    @Override
    public Object[] getPageParameter(Object[] paras, Page page) {
        // OFFSET (@PageNumber-1)*@RowsPerPage ROWS
    	Object[] newParas = new Object[paras.length + 2];
    	System.arraycopy(paras, 0, newParas, 0, paras.length);
    	newParas[paras.length] = page.getStartRow();
    	newParas[paras.length+1] = page.getPageSize();
    	return newParas;
    }

}

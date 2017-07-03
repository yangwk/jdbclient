package com.github.yangwk.jdbclient.page.parser.impl;

import com.github.yangwk.jdbclient.page.Dialect;
import com.github.yangwk.jdbclient.page.Page;
import com.github.yangwk.jdbclient.page.parser.Parser;
import com.github.yangwk.jdbclient.page.parser.SqlParser;

public abstract class AbstractParser implements Parser {
    //处理SQL
    public static final SqlParser sqlParser = new SqlParser();

    public static Parser newParser(Dialect dialect) {
        Parser parser = null;
        switch (dialect) {
            case mysql:
            case mariadb:
            case sqlite:
                parser = new MysqlParser();
                break;
            case oracle:
                parser = new OracleParser();
                break;
            case hsqldb:
                parser = new HsqldbParser();
                break;
            case sqlserver:
                parser = new SqlServerParser();
                break;
            case sqlserver2012:
                parser = new SqlServer2012Parser();
                break;
            case db2:
                parser = new Db2Parser();
                break;
            case postgresql:
                parser = new PostgreSQLParser();
                break;
            case informix:
                parser = new InformixParser();
                break;
            case h2:
                parser = new H2Parser();
                break;
            default:
                throw new RuntimeException("分页插件" + dialect + "方言错误!");
        }
        return parser;
    }

    @Override
    public String getCountSql(final String sql) {
        return sqlParser.getSmartCountSql(sql);
    }

    public abstract String getPageSql(String sql,Page page);

    public abstract Object[] getPageParameter(Object[] paras, Page page);
    
}

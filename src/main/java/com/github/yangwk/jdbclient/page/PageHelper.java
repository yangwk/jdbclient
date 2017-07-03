package com.github.yangwk.jdbclient.page;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

import com.github.yangwk.jdbclient.page.parser.Parser;
import com.github.yangwk.jdbclient.page.parser.impl.AbstractParser;

public class PageHelper {
    private static ConcurrentHashMap<String, Parser> parserCache = new ConcurrentHashMap<String, Parser>();

    public static Page getPage(int pageNum, int pageSize) {
        Page page = new Page(pageNum, pageSize);
        page.setReasonable(true);
        page.setPageSizeZero(true);
        return page;
    }
    
	public static boolean isQueryOnly(Page page) {
        return ((page.getPageSizeZero() != null && page.getPageSizeZero()) && page.getPageSize() == 0);
    }

    public static Parser getParser(Connection conn) throws SQLException {
    	String jdbcUrl = conn.getMetaData().getURL();
    	return getParser(jdbcUrl);
    }
    
    public static Parser getParser(String jdbcUrl){
    	Parser parser = parserCache.get(jdbcUrl);
		if (parser != null) {
			return parser;
        }
		Dialect dialect = Dialect.fromJdbcUrl(jdbcUrl);
		parser = AbstractParser.newParser(dialect);
		parserCache.putIfAbsent(jdbcUrl, parser);
		return parser;
    }
    

}

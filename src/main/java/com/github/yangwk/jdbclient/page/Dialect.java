package com.github.yangwk.jdbclient.page;

public enum Dialect {
    mysql, mariadb, sqlite, oracle, hsqldb, postgresql, sqlserver, db2, informix, h2, sqlserver2012;

    public static Dialect of(String dialect) {
    	return Dialect.valueOf(dialect.toLowerCase());
    }

    public static Dialect fromJdbcUrl(String jdbcUrl) {
        for (Dialect dialect : Dialect.values()) {
        	String strDialect = dialect.toString();
            if (jdbcUrl.indexOf(":" + strDialect + ":") != -1) {
                return dialect;
            }
        }
        return null;
    }
}

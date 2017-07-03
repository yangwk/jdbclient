package com.github.yangwk.jdbclient;

import javax.sql.DataSource;

public class BaseTest {
	
	enum DbType{
		mysql("JDBC:mysql://localhost:8080/testDB"),
		mariadb("jdbc:mariadb://localhost:3306/db"),
		sqlite("jdbc:sqlite://COMPUTERNAME/shareA/dirB/dbfile"),
		oracle("JDBC:oracle:thin:@localhost:1521:orcl"),
		hsqldb("jdbc:hsqldb:hsql://localhost:9001/testDbName"),
		postgresql("JDBC:postgresql://localhost/testDb"),
		sqlserver("JDBC:microsoft:sqlserver://localhost:1433;DatabaseName=testDb"),
		db2("JDBC:db2://localhost:5000/testDb"),
		informix("JDBC:informix:localhost:1533/testDb:INFORMIXSERVER=myserver"),
		h2("jdbc:h2:tcp://localhost/mini-web"),
		sqlserver2012("jdbc:sqlserver2012://localhost:1433");
		
		String jdbcUrl;
		DbType( String jdbcUrl){
			this.jdbcUrl = jdbcUrl;
		}
	}
	
	void init(){
		DataSource dataSource = DataSourceFactory.getDataSource();
		Db.init( new ConnectionHolder(dataSource) );
	}
	
	void print(String s){
		System.out.println(s);
	}
}

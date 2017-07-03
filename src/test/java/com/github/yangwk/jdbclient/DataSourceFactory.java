package com.github.yangwk.jdbclient;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;

public class DataSourceFactory {
	private final static DruidDataSource dataSource = new DruidDataSource();
	private volatile static boolean inited = false;
	
	private synchronized static void init(){
		if(inited)
			return ;
		Properties prop = load();
		
		dataSource.setUrl( prop.getProperty("url") );
		dataSource.setDriverClassName( prop.getProperty("driverClass") );
		dataSource.setUsername( prop.getProperty("user") );
		dataSource.setPassword( prop.getProperty("password") );
		
		dataSource.setMaxActive(20);
		dataSource.setInitialSize(1);
		dataSource.setMaxWait(60000);
		dataSource.setMinIdle(1);
		
		try {
			dataSource.init();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		inited = true;
	}
	
	private static Properties load(){
		Properties prop = new Properties();
		try {
			prop.load( ClassLoader.getSystemResourceAsStream("db.properties") );
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}
	
	public static DataSource getDataSource(){
		init();
		return dataSource;
	}
	
	public synchronized static void close(){
		dataSource.close();
	}
}

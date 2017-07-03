package com.github.yangwk.jdbclient;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

public class ConnectionHolder {
	private final ThreadLocal<Connection> threadLocal;
	
	private DataSource dataSource;
	
	public ConnectionHolder(DataSource dataSource) {
		if(dataSource == null){
			throw new IllegalArgumentException("dataSource is null");
		}
		this.dataSource = dataSource;
		this.threadLocal = new ThreadLocal<Connection>();
	}
	
	public final DataSource getDataSource() {
		return this.dataSource;
	}
	
	
	/**
	 * Get Connection. Support transaction if Connection in ThreadLocal
	 * @throws SQLException 
	 */
	public final Connection getConnection() throws SQLException{
		Connection threadLocalConn = threadLocal.get();
		if(threadLocalConn == null){
			threadLocalConn = dataSource.getConnection();
			threadLocal.set(threadLocalConn);
		}
		return threadLocalConn;
	}
	
	public final void close(Connection conn) {
		Connection threadLocalConn = threadLocal.get();
		if (threadLocalConn == null){
			throw new RuntimeException("Connection closed error , because the operating Connection is null .");
		}
		else if(threadLocalConn != conn){
			throw new RuntimeException("Connection closed error , because the closing Connection != the operating Connection .");
		}
		threadLocal.remove();
		try { threadLocalConn.close(); } catch (SQLException e) { throw new RuntimeException(e); }
	}
	
}




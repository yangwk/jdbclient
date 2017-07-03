package com.github.yangwk.jdbclient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.yangwk.jdbclient.page.Page;
import com.github.yangwk.jdbclient.page.PageHelper;
import com.github.yangwk.jdbclient.page.PageInfo;
import com.github.yangwk.jdbclient.page.parser.Parser;

/**
 * 
 * <p><b>Title: </b>Db.java
 * <p><b>Description: </b>数据库操作类，支持当前线程的事务操作<br>
 * <pre>
	//使用示例
	//in Dao
	public Student findById(Integer id){
		String sql = "select s.STU_NAME name, s.SCH_ID age from student s where s.id = ? ";
		Student student = Db.get().queryOne(Student.class, sql, id);
		return student;
	}
		
	//in Service
	try{
		Db.get().beginTransaction();	//开启事务
		Student student = studentDao.findById(id);
		...
		Db.get().commit();	//提交
	}catch(Exception e){
		Db.get().rollback();	//回滚
	}
 * </pre>
 * @author yangwk
 * @version V1.0
 * <p>
 */
public class Db {
	private final String lineSeparator = System.getProperty("line.separator", "\n");
	private final org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(getClass());
	
	private static ConnectionHolder holder = null;
	private static Db me = null;
	
	private Db() {}
	
	public static void init(ConnectionHolder holder){
		if(holder == null){
			throw new IllegalArgumentException("ConnectionHolder is null");
		}
		if(Db.holder == null){
			Db.holder = holder;
		}
	}
	
	/**
	 * 
	 * <p><b>Description: </b>获得实例的唯一方法
	 * @author yangwk
	 * @return
	 */
	public static Db get(){
		if(me == null){
			me = new Db();
		}
		return me;
	}
	
	/**
	 * 
	 * <p><b>Description: </b>任何以事务为单元处理的业务，必须先调用该方法
	 * @author yangwk
	 * @throws SQLException
	 */
	public void beginTransaction() throws SQLException{
		Connection conn = holder.getConnection();	//
		conn.setAutoCommit(false);
		log("beginTransaction");
	}
	
	
	/**
	 * 
	 * <p><b>Description: </b>事务提交。自动回收数据库连接
	 * @author yangwk
	 */
	public void commit(){
		Connection conn = null;
		try{
			conn = holder.getConnection();	//
			conn.commit();
			log("commit");
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			recoverAutoCommit(conn);
			holder.close(conn);
			log("Connection closed");
		}
	}
	
	
	/**
	 * 
	 * <p><b>Description: </b>事务回滚。自动回收数据库连接
	 * @author yangwk
	 */
	public void rollback(){
		Connection conn = null;
		try{
			conn = holder.getConnection();	//
			conn.rollback();
			log("rollback");
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			recoverAutoCommit(conn);
			holder.close(conn);
			log("Connection closed");
		}
	}
	
	private void recoverAutoCommit(Connection conn) {
		try {
			conn.setAutoCommit(true);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 
	 * <p><b>Description: </b>通用查询，查询结果使用封装的get系列方法获取列的值<br>
	 * <pre>
	 * 		Record record = records.get(0);
	 * 		String name = record.getStr("name");
	 * </pre>
	 * @author yangwk
	 * @param sql
	 * @param paras 若sql没有参数，传入null
	 * @return 永远不会返回null
	 * @see #query(Class, String, Object...)
	 */
	public List<Record> query(String sql, Object... paras) {
		List<Record> result = new ArrayList<Record>();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			conn = holder.getConnection();	//
			StringBuilder sb = new StringBuilder();
			pst = preparedStatement(sb,conn,sql,paras);
			rs = pst.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			String[] columnLabels = new String[columnCount + 1];
			int[] columnTypes = new int[columnCount + 1];
			buildColumnLabelsAndTypes(rsmd, columnLabels, columnTypes);
			long rowCount = 0;
			while (rs.next()) {
				rowCount ++;
				Map<String,Object> dataMap = new HashMap<String, Object>();
				for (int col=1; col<=columnCount; col++) {
					dataMap.put(columnLabels[col], getObject(rs,columnTypes[col],col) );
				}
				result.add( new Record(dataMap) );
			}
			log(sb, rowCount);
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			DbKit.close(rs, pst);
		}
		return result;
	}
	
	/**
	 * 
	 * <p><b>Description: </b>只查询一条记录，如果记录数大于1将抛出异常
	 * @author yangwk
	 * @param sql
	 * @param paras 若sql没有参数，传入null
	 * @return 查询无结果将返回null
	 * @see #query(String, Object...)
	 */
	public Record queryOne(String sql, Object... paras) {
		List<Record> result = query(sql, paras);
		if(result.size() > 1){
			throw new RuntimeException("expect one , but more than one");
		}
		return (result.size() > 0 ? result.get(0) : null);
	}
	
	
	/**
	 * 
	 * <p><b>Description: </b>通用增删改
	 * @author yangwk
	 * @param sql
	 * @param paras 若sql没有参数，传入null
	 * @return 影响的行数
	 */
	public int update(String sql, Object... paras){
		int result = -1;
		Connection conn = null;
		PreparedStatement pst = null;
		try {
			conn = holder.getConnection();	//
			StringBuilder sb = new StringBuilder();
			pst = preparedStatement(sb,conn,sql,paras);
			result = pst.executeUpdate();
			log(sb, result);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}finally{
			DbKit.close(pst);
		}
		return result;
	}
	
	
	/**
	 * 
	 * <p><b>Description: </b>通用的实体查询
	 * @author yangwk
	 * @param clazz 查询结果对应的实体
	 * @param sql 查询的字段必须与实体属性名一致，否则无法转换实体
	 * @param paras 若sql没有参数，传入null
	 * @return 永远不会返回null
	 * @see #query(String, Object...)
	 */
	public <T> List<T> query(Class<T> clazz, String sql, Object... paras) {
		List<T> result = new ArrayList<T>();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			conn = holder.getConnection();	//
			StringBuilder sb = new StringBuilder();
			pst = preparedStatement(sb,conn,sql,paras);
			rs = pst.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			String[] columnLabels = new String[columnCount + 1];
			int[] columnTypes = new int[columnCount + 1];
			buildColumnLabelsAndTypes(rsmd, columnLabels, columnTypes);
			long rowCount = 0;
			while (rs.next()) {
				rowCount ++;
				Map<String,Object> dataMap = new HashMap<String, Object>();
				for (int col=1; col<=columnCount; col++) {
					dataMap.put(columnLabels[col], getObject(rs,columnTypes[col],col) );
				}
				result.add( DbKit.populate(clazz, dataMap) );
			}
			log(sb, rowCount);
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			DbKit.close(rs, pst);
		}
		return result;
	}
	
	/**
	 * 
	 * <p><b>Description: </b>只查询一条记录，如果记录数大于1将抛出异常
	 * @author yangwk
	 * @param clazz 查询结果对应的实体
	 * @param sql 查询的字段必须与实体属性名一致，否则无法转换实体
	 * @param paras 若sql没有参数，传入null
	 * @return 查询无结果将返回null
	 * @see #query(Class,String, Object...)
	 */
	public <T> T queryOne(Class<T> clazz, String sql, Object... paras) {
		List<T> result = query(clazz, sql, paras);
		if(result.size() > 1){
			throw new RuntimeException("expect one , but more than one");
		}
		return (result.size() > 0 ? result.get(0) : null);
	}
	
	
	//include log
	private PreparedStatement preparedStatement(StringBuilder sb,Connection conn,String sql,Object... paras) throws SQLException {
		PreparedStatement pst = conn.prepareStatement(sql);
		
		sb.append(lineSeparator)
			.append("Preparing: ").append(lineSeparator)
			.append(sql).append(lineSeparator)
			.append("Parameter: ").append(lineSeparator);	//
		
		for (int i=0; paras !=null && i<paras.length; i++) {
			pst.setObject(i + 1, paras[i]);
			
			sb.append(paras[i]).append( (i >= paras.length-1 ? "" : ",") );	//
		}
		
		return pst;
	}
	
	private void fillStatement(PreparedStatement pst, Object... paras) throws SQLException {
		for (int i=0; paras !=null && i<paras.length; i++) {
			pst.setObject(i + 1, paras[i]);
		}
	}
	
	private void buildColumnLabelsAndTypes(ResultSetMetaData rsmd, String[] columnLabels, int[] columnTypes) throws SQLException {
		for (int i=1; i<columnLabels.length; i++) {
			columnLabels[i] = rsmd.getColumnLabel(i);
			columnTypes[i] = rsmd.getColumnType(i);
		}
	}
	
	private void log(String message){
		if(log.isDebugEnabled()){
			log.debug(message);
		}
	}
	
	private void log(StringBuilder sb,long total){
		sb.append(lineSeparator).append("Total: ").append(total);
		log(sb.toString());
	}
	
	
	private Object getObject(ResultSet rs, int columnType, int column) throws SQLException{
		Object value = null;
		if (columnType < Types.BLOB)
			value = rs.getObject(column);
		else if (columnType == Types.CLOB)
			value = DbKit.handleClob(rs.getClob(column));
		else if (columnType == Types.NCLOB)
			value = DbKit.handleClob(rs.getNClob(column));
		else if (columnType == Types.BLOB)
			value = DbKit.handleBlob(rs.getBlob(column));
		else
			value = rs.getObject(column);
		return value;
	}
	
	/////////分页版本如下
	
    /**
     * @param pageNum      页码
     * @param pageSize     每页显示数量,为0时返回全部结果
     */
	public <T> PageInfo<T> page(int pageNum, int pageSize, Class<T> clazz, String sql, Object... paras ){
		Connection conn = null;
		try{
			conn = holder.getConnection();	//
			Page page = PageHelper.getPage(pageNum, pageSize);
	    	if (PageHelper.isQueryOnly(page)) {
	            return pageQueryOnly(page,clazz,sql,paras);
	        }
	    	Parser parser = PageHelper.getParser(conn);
	    	String countSql = parser.getCountSql(sql);	//总数sql
	        long count = queryOne(countSql, paras).getOne();	//查询总数
	        page.setTotal(count);	//设置总数
	        if (page.getTotal() == 0) {	//无结果
	            return new PageInfo<T>( new ArrayList<T>(), page);
	        }
	        //pageSize>0的时候执行分页查询，pageSize<=0的时候不执行相当于可能只返回了一个count
	        if (page.getPageSize() > 0 && page.getPageNum() > 0 ) {
	        	String pageSql = parser.getPageSql(sql,page);	//分页sql
	            Object[] newParas = parser.getPageParameter( (paras==null ? new Object[0] : paras), page);
	            //执行分页查询
	            List<T> result = query(clazz, pageSql, newParas);
	            return new PageInfo<T>(result, page);
	        }
	       //返回结果
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		return new PageInfo<T>();
	}
	
	
	private <T> PageInfo<T> pageQueryOnly(Page page,Class<T> clazz,String sql,Object[] paras) {
    	List<T> result = query(clazz, sql, paras);	//查询结果
        page.setPageNum(1);	//相当于查询第一页
        page.setPageSize(result.size());	//这种情况相当于pageSize=total
        page.setTotal(result.size());	//仍然要设置total
        
        PageInfo<T> pageInfo = new PageInfo<T>(result, page);
        return pageInfo;
    }
	
	
	public int[] batch(String sql, List<Object[]> parasList){
		Connection conn = null;
		PreparedStatement pst = null;
		int[] rows = new int[0];
		try {
			conn = holder.getConnection();	//
			pst = conn.prepareStatement(sql);
			if(parasList != null){
				for(Object[] paras : parasList){
					fillStatement(pst,paras);
					pst.addBatch();
				}
			}
			else{
				pst.addBatch();
			}
			rows = pst.executeBatch();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}finally{
			DbKit.close(pst);
		}
		
		return rows;
	}
	
	
}

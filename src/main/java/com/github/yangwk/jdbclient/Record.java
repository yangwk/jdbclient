package com.github.yangwk.jdbclient;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Record implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Map<String, Object> columns;
	
	public Record(Map<String, Object> columns){
		if(columns == null){
			throw new IllegalArgumentException("constructor's columns is null");
		}
		this.columns = columns;
	}
	
	/**
	 * Return columns map.
	 */
	public Map<String, Object> getColumns() {
		return columns;
	}
	
	/**
	 * Get column of any mysql type
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(String column) {
		return (T)getColumns().get(column);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getOne() {
		Map<String,Object> map = getColumns();
		if(map.size() > 1){
			throw new RuntimeException("expect one , but more than one");
		}
		Set<Entry<String, Object>> entrys = map.entrySet();
		Iterator<Entry<String, Object>> iter = entrys.iterator();
		if(iter.hasNext()){
			return (T)iter.next().getValue();
		}
		return null;
	}
	
	/**
	 * Get column of any mysql type. Returns defaultValue if null.
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(String column, Object defaultValue) {
		Object result = getColumns().get(column);
		return (T)(result != null ? result : defaultValue);
	}
	
	/**
	 * Get column of mysql type: varchar, char, enum, set, text, tinytext, mediumtext, longtext
	 */
	public String getStr(String column) {
		return (String)getColumns().get(column);
	}
	
	/**
	 * Get column of mysql type: int, integer, tinyint(n) n > 1, smallint, mediumint
	 */
	public Integer getInt(String column) {
		return (Integer)getColumns().get(column);
	}
	
	/**
	 * Get column of mysql type: bigint
	 */
	public Long getLong(String column) {
		return (Long)getColumns().get(column);
	}
	
	/**
	 * Get column of mysql type: float
	 */
	public Float getFloat(String column) {
		return (Float)getColumns().get(column);
	}

	/**
	 * Get column of mysql type: real, double
	 */
	public Double getDouble(String column) {
		return (Double)getColumns().get(column);
	}

	/**
	 * Get column of mysql type: unsigned bigint
	 */
	public java.math.BigInteger getBigInteger(String column) {
		return (java.math.BigInteger)getColumns().get(column);
	}
	
	/**
	 * Get column of mysql type: decimal, numeric
	 */
	public java.math.BigDecimal getBigDecimal(String column) {
		return (java.math.BigDecimal)getColumns().get(column);
	}

	/**
	 * Get column of any type that extends from Number
	 */
	public Number getNumber(String column) {
		return (Number)getColumns().get(column);
	}

	/**
	 * Get column of mysql type: date, year
	 */
	public java.util.Date getDate(String column) {
		return (java.util.Date)getColumns().get(column);
	}
	
	/**
	 * Get column of mysql type: time
	 */
	public java.sql.Time getTime(String column) {
		return (java.sql.Time)getColumns().get(column);
	}
	
	/**
	 * Get column of mysql type: timestamp, datetime
	 */
	public java.sql.Timestamp getTimestamp(String column) {
		return (java.sql.Timestamp)getColumns().get(column);
	}
	
	/**
	 * Get column of mysql type: bit, tinyint(1)
	 */
	public Boolean getBoolean(String column) {
		return (Boolean)getColumns().get(column);
	}
	
	/**
	 * Get column of mysql type: binary, varbinary, tinyblob, blob, mediumblob, longblob
	 * I have not finished the test.
	 */
	public byte[] getBytes(String column) {
		return (byte[])getColumns().get(column);
	}
	
	@Override
	public String toString() {
		return columns.toString();
	}
	
	
}

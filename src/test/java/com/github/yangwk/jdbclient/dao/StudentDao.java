package com.github.yangwk.jdbclient.dao;

import com.github.yangwk.jdbclient.Db;
import com.github.yangwk.jdbclient.entity.Student;

public class StudentDao {
	
	public Student findById(Object id){
		String sql = "select s.* from student s where s.id = ? ";
		Student student = Db.get().queryOne(Student.class, sql, id);
		return student;
	}
	
	public int updateNameById(Object id,String name){
		String sql = "update student set name = ? where id = ?";
		int count = Db.get().update(sql, name,1);
		return count;
	}
}

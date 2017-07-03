package com.github.yangwk.jdbclient.service.impl;

import com.github.yangwk.jdbclient.Db;
import com.github.yangwk.jdbclient.dao.StudentDao;
import com.github.yangwk.jdbclient.entity.Student;
import com.github.yangwk.jdbclient.service.StudentService;

public class StudentServiceImpl implements StudentService{
	
	private StudentDao studentDao = new StudentDao();

	@Override
	public int updateByIdAndName(Integer id, String name) {
		int allCount = -1;
		try{
			Db.get().beginTransaction();
			Student student = studentDao.findById(id);
			if(student != null){
				System.out.println("查询到学生信息");
				allCount = studentDao.updateNameById(id,name);
				if(allCount > 0){
					System.out.println("更新学生信息成功了");
					throw new RuntimeException("事务回滚测试");
				}
			}
			Db.get().commit();	//提交
		}catch(Exception e){
			allCount = -1;
			Db.get().rollback();	//回滚
			throw new RuntimeException(e);
		}
		return allCount;
	}

}

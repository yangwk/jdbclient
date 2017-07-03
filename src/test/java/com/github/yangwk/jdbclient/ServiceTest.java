package com.github.yangwk.jdbclient;

import com.github.yangwk.jdbclient.service.StudentService;
import com.github.yangwk.jdbclient.service.impl.StudentServiceImpl;

public class ServiceTest extends BaseTest{
	
	void testService(){
		init();
		
		StudentService studentService = new StudentServiceImpl();
		studentService.updateByIdAndName(1, "haha tx");
	}
	
	public static void main(String[] args) {
		new ServiceTest().testService();
	}

}

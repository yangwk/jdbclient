package com.github.yangwk.jdbclient;

import java.io.IOException;

import com.github.yangwk.jdbclient.entity.Student;
import com.github.yangwk.jdbclient.page.PageInfo;

public class PageTest extends BaseTest{
	
	void testPage(String sql, Object[] paras, int pageNum, int pageSize) throws IOException{
		PageInfo<Student> pageInfo = Db.get().page(pageNum, pageSize, Student.class, sql, paras);
		
		print(pageInfo.toString());
	}
	
	void doTestPage(){
		init();
		
		try {
			testPage("select * from student where id = ?", 
					new Object[]{1}, 1, 20);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new PageTest().doTestPage();
	}

}

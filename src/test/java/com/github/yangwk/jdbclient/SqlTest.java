package com.github.yangwk.jdbclient;

import java.io.IOException;

import com.github.yangwk.jdbclient.page.Page;
import com.github.yangwk.jdbclient.page.PageHelper;
import com.github.yangwk.jdbclient.page.parser.Parser;


public class SqlTest extends BaseTest{
	
	
	
	void testSql(String sql,Object[] paras, int pageNum, int pageSize, String jdbcUrl) throws IOException{
		Page page = PageHelper.getPage(pageNum, pageSize);	//分页对象
		Parser parser = PageHelper.getParser(jdbcUrl);	//sql解析器
		String countSql = parser.getCountSql(sql);	//总数sql
		String pageSql = parser.getPageSql(sql,page);	//分页sql
		Object[] newParas = parser.getPageParameter(paras, page);	//分页sql的所有参数
		
		//打印
		{
			String parasStr = "";
			for(int i=0;i<paras.length;i++){
				parasStr += paras[i].toString();
				parasStr += (i == paras.length-1 ? "" : ",");
			}
			
			String newParasStr = "";
			for(int i=0;i<newParas.length;i++){
				newParasStr += newParas[i].toString();
				newParasStr += (i == newParas.length-1 ? "" : ",");
			}
			
			print(countSql);
			print(parasStr);
			print(pageSql);
			print(newParasStr);
			print("--------分界线-------");
		}
	}
	
	void doTestSql(){
		init();
		
		try {
			testSql("select * from mysql where id = ? order by date desc , name asc", 
					new Object[]{2}, 3, 2, DbType.mysql.jdbcUrl);
			testSql("select * from mariadb where id = ? and name like '%?%'", 
					new Object[]{2,"bo"}, 4, 21, DbType.mariadb.jdbcUrl);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		new SqlTest().doTestSql();
	}
	
}

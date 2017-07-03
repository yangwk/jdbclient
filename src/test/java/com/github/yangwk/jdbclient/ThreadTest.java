package com.github.yangwk.jdbclient;

public class ThreadTest extends BaseTest{
	
	void testThread(){
		init();
		
		Thread[] threads = new Thread[3];
		for(Thread thread : threads){
			thread = new Thread(new Runnable() {
				
				void doTest(){
					try {
						Db.get().beginTransaction();
						String sql = "select name,birthDate from student where id = ? and age > ?";
						Record record = Db.get().queryOne(sql, 1, 0);
						Db.get().commit();
						String str = record.get("name");
						java.util.Date date = record.getDate("birthDate");
						
						print(str);
						print(date.toString());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				@Override
				public void run() {
					doTest();
					doTest();
					doTest();
				}
			});
			thread.start();
		}
	}
	
	
	public static void main(String[] args) {
		new ThreadTest().testThread();
	}
	
	
}

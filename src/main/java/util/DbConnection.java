package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
	
	public static void main(String[] args) {
		System.out.println(DbConnection.getDb());
	}
	// static直接呼叫 method方法 
	public static Connection getDb()
	{
		String url="jdbc:mysql://localhost:3306/javademo06";
		String user="root";
		String password="1234";
		Connection conn=null;	
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			DriverManager.getConnection(url, user, password);
			conn=DriverManager.getConnection(url, user, password);  //這邊要加，不然會連不上
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return conn;
	}
	
}

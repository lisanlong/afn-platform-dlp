package org.cloud.framework.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class DbConnection {
	private final static String MysqlDriver="com.mysql.jdbc.Driver";
	private final static String OracleDriver="oracle.jdbc.driver.OracleDriver";
	private final static String SQLServerDriver="com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private final static String AccessDriver="sun.jdbc.odbc.JdbcOdbcDriver";
	
	public static class MySQL {
		
		static{
			
			try {
				Class.forName(MysqlDriver);
			} catch (Exception e) {
			}
		}
		/**
		 * 获取myql链接
		 * @param url
		 * @param username
		 * @param password
		 * @return
		 * @throws SQLException 
		 */
		public static Connection getConnection(String url,String username,String password) throws SQLException {
			Connection conn = null;
			conn = DriverManager.getConnection(url, username, password);
			return conn;
		}
	}
	public static class Oracle {
		static{
			try {
				Class.forName(OracleDriver);
			} catch (Exception e) {
			}
		}
		/**
		 * 获取oracle链接
		 * @param url
		 * @param username
		 * @param password
		 * @return
		 * @throws SQLException 
		 */
		public static Connection getConnection(String url,String username,String password) throws SQLException {
			Connection conn = null;
			conn = DriverManager.getConnection(url, username, password);
			return conn;
		}
	}
	public static class SQLServer {
		static{
			try {
				Class.forName(SQLServerDriver);
				
			} catch (Exception e) {
			}
		}
		/**
		 * 获取sqlserver链接
		 * @param url
		 * @param username
		 * @param password
		 * @return
		 * @throws SQLException 
		 */
		public static Connection getConnection(String url,String username,String password) throws SQLException {
			Connection conn = null;
			conn = DriverManager.getConnection(url, username, password);
			return conn;
		}
	}
	/**
	 * 桥连接方式
	 */
	public static class Access{
		static{
			try {
				Class.forName(AccessDriver);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		/**
		 * 获取access链接
		 * @param database
		 * @param encode
		 * @param username
		 * @param password
		 * @return
		 * @throws SQLException 
		 */
		public static Connection getConnection(String database,String encode,String username,String password) throws SQLException {
			 Connection conn = null;
			 Properties prop = new Properties();
			 if(encode == null || encode.equals("")){
				 prop.setProperty("charSet", "gbk");
			 }else{
				 prop.setProperty("charSet", encode);
			 }
			 prop.setProperty("user", username);
			 prop.setProperty("password", password);
			 String url="jdbc:odbc:"+database;
			//String url="jdbc:odbc:driver={Microsoft Access Driver (*.mdb,*.accdb)};DBQ="+dbpath
			conn = DriverManager.getConnection(url,prop);
			return conn;
		}
	}
	/**
	 * 是空是null 返回true
	 * @param str
	 * @return
	 */
	@SuppressWarnings("unused")
	private static boolean emptyOrNull(String str){
		if ((str == null) || (str.isEmpty())) {
			return true;
		}else{
			return false;
		}
	}
	public static void main(String[] args) {
	}
}

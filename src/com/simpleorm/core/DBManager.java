package com.simpleorm.core;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.simpleorm.bean.Configuration;

/**
 * 根据配置信息,维持连接对象的管理 
 * @author MiKimouse
 *
 */
public class DBManager {
	private static Configuration conf;
	
	/**
	 * 获得配置文件
	 */
	static {	//静态代码块
		Properties pros = new Properties();
		try {
			pros.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		conf = new Configuration();
		conf.setDriver(pros.getProperty("driver"));
		conf.setPoPackage(pros.getProperty("poPackage"));
		conf.setPwd(pros.getProperty("pwd"));
		conf.setSrcPath(pros.getProperty("srcPath"));
		conf.setUrl(pros.getProperty("url"));
		conf.setUser(pros.getProperty("user"));
		conf.setUsingDB(pros.getProperty("usingDB"));
	}
	
	/**
	 * 获取数据库连接的方法
	 * @return
	 */
	public static Connection getConn() {
		try {
			Class.forName(conf.getDriver());	
			return DriverManager.getConnection(conf.getUrl(),conf.getUser(),conf.getPwd());	//后期增加连接池提高效率
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 创建数据库连接的方法
	 * @return
	 */
	public static Connection createConn() {
		try {
			Class.forName(conf.getDriver());	
			return DriverManager.getConnection(conf.getUrl(),conf.getUser(),conf.getPwd());	//后期增加连接池提高效率
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 获取Configuration配置信息对象
	 * @return	Configuration对象
	 */
	public static Configuration getConf() {
		return conf;
	}
	
	/**
	 * 关闭传入的ResultSet,Statement,Connection对象
	 * @param rs	
	 * @param ps
	 * @param conn
	 */
	public static void close(ResultSet rs,Statement ps,Connection conn) {
		try {
			if(rs!=null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if(ps!=null) {
				ps.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if(conn!=null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 关闭传入的Statement,Connection对象
	 * @param ps
	 * @param conn
	 */
	public static void close(Statement ps,Connection conn) {
		
		try {
			if(ps!=null) {
				ps.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if(conn!=null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}

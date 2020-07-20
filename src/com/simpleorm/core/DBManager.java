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
 * ����������Ϣ,ά�����Ӷ���Ĺ��� 
 * @author MiKimouse
 *
 */
public class DBManager {
	private static Configuration conf;
	
	/**
	 * ��������ļ�
	 */
	static {	//��̬�����
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
	 * ��ȡ���ݿ����ӵķ���
	 * @return
	 */
	public static Connection getConn() {
		try {
			Class.forName(conf.getDriver());	
			return DriverManager.getConnection(conf.getUrl(),conf.getUser(),conf.getPwd());	//�����������ӳ����Ч��
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * �������ݿ����ӵķ���
	 * @return
	 */
	public static Connection createConn() {
		try {
			Class.forName(conf.getDriver());	
			return DriverManager.getConnection(conf.getUrl(),conf.getUser(),conf.getPwd());	//�����������ӳ����Ч��
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * ��ȡConfiguration������Ϣ����
	 * @return	Configuration����
	 */
	public static Configuration getConf() {
		return conf;
	}
	
	/**
	 * �رմ����ResultSet,Statement,Connection����
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
	 * �رմ����Statement,Connection����
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

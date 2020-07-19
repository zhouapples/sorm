package com.simpleorm.core;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
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
	 * ��ȡ���ݿ�����
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
	 * ��ȡConfiguration����,������Ϣ����
	 * @return	Configuration����
	 */
	public static Configuration getConf() {
		return conf;
	}
}

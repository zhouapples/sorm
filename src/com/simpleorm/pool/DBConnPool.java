package com.simpleorm.pool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.simpleorm.core.DBManager;

/**
 * ���ӳص���,ʹ��Listʵ��
 * @author MiKimouse
 *
 */
public class DBConnPool {
	/**
	 * ���ӳض���
	 */
	public static List pool;	//���ӳ�
	
	/**
	 * ���ӳش������������
	 */
	private static final int POOL_MAX_SIZE = 100;
	
	/**
	 * ��ʼ�����ӳ�,ʹ�����������ﵽ��Сֵ
	 */
	private static final int POOL_MIN_SIZE = 10;
	
	public void initPool() {
		if(pool==null) {
			pool = new ArrayList<Connection>();
		}
		
		while(pool.size()<DBConnPool.POOL_MIN_SIZE) {	//���������������С�������򴴽�����
			pool.add(DBManager.createConn());
		}
	}
	
	public DBConnPool() {
		initPool();	//ֱ���ڶ��󴴽���ʱ���ʼ��
	}
	
	/**
	 * �����ӳ�ȡ������,��Ҫ�߳�ͬ��synchronized
	 * @return
	 */
	public synchronized Connection getConnection() {
		int last_index = pool.size()-1;	//��ȡ���һ��
		Connection conn = (Connection) pool.get(last_index);
		pool.remove(last_index);	//�Ƴ���ȡ����,�����»ر���ȡ��ʱ����ȡ����
		return conn;
	}
	
	/**
	 * �����ӷŻ����ӳ�
	 * @param conn
	 */
	public synchronized void close(Connection conn) {
		try {
			if(pool.size()>POOL_MAX_SIZE) {
				if(conn!=null) {
					conn.close();	//������ӳ��Ѿ��ﵽ�������,��ֱ�ӹص�					
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		pool.add(conn);	//������ӳ�û�ﵽ��������Ͱ�������ӽ�����
	}
	
}

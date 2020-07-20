package com.simpleorm.pool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.simpleorm.core.DBManager;

/**
 * 连接池的类,使用List实现
 * @author MiKimouse
 *
 */
public class DBConnPool {
	/**
	 * 连接池对象
	 */
	public static List pool;	//连接池
	
	/**
	 * 连接池储存最大连接数
	 */
	private static final int POOL_MAX_SIZE = 100;
	
	/**
	 * 初始化连接池,使池中连接数达到最小值
	 */
	private static final int POOL_MIN_SIZE = 10;
	
	public void initPool() {
		if(pool==null) {
			pool = new ArrayList<Connection>();
		}
		
		while(pool.size()<DBConnPool.POOL_MIN_SIZE) {	//如果连接数少于最小连接数则创建连接
			pool.add(DBManager.createConn());
		}
	}
	
	public DBConnPool() {
		initPool();	//直接在对象创建的时候初始化
	}
	
	/**
	 * 从连接池取出连接,需要线程同步synchronized
	 * @return
	 */
	public synchronized Connection getConnection() {
		int last_index = pool.size()-1;	//获取最后一个
		Connection conn = (Connection) pool.get(last_index);
		pool.remove(last_index);	//移除已取出的,避免下回别人取的时候还是取到它
		return conn;
	}
	
	/**
	 * 将连接放回连接池
	 * @param conn
	 */
	public synchronized void close(Connection conn) {
		try {
			if(pool.size()>POOL_MAX_SIZE) {
				if(conn!=null) {
					conn.close();	//如果连接池已经达到最大容量,就直接关掉					
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		pool.add(conn);	//如果连接池没达到最大容量就把连接添加进池中
	}
	
}

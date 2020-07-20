package com.simpleorm.utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 封装了jdbc常用查询操作
 * @author MiKimouse
 *
 */
public class JDBCUtils {
	
	/**
	 * 给sql设置参数
	 * @param ps	预编译sql语句对象
	 * @param params	sql参数集
	 */
	public static void handleParams(PreparedStatement ps,Object[] params) {
		//给sql设置参数
		if(params!=null) {
			for(int i=0;i<params.length;i++) {
				try {
					ps.setObject(1+i, params[i]);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

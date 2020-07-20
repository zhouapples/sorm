package com.simpleorm.utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * ��װ��jdbc���ò�ѯ����
 * @author MiKimouse
 *
 */
public class JDBCUtils {
	
	/**
	 * ��sql���ò���
	 * @param ps	Ԥ����sql������
	 * @param params	sql������
	 */
	public static void handleParams(PreparedStatement ps,Object[] params) {
		//��sql���ò���
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

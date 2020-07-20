package com.simpleorm.core;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.simpleorm.bean.ColumnInfo;
import com.simpleorm.bean.TableInfo;
import com.simpleorm.po.Emp;
import com.simpleorm.utils.JDBCUtils;
import com.simpleorm.utils.ReflectUtils;
import com.simpleorm.utils.StringUtils;
/**
 * �������mysql���ݿ�Ĳ�ѯ
 * @author MiKimouse
 *
 */
public class MySqlQuery implements Query{
	public static void main(String[] args) {
		Emp e = new Emp();
		e.setId(1);
		new MySqlQuery().delete(e);
		
	}
	
	
	
	@Override
	public int executeDML(String sql, Object[] params) {
		Connection conn = DBManager.getConn();
		int count = 0;
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			//��sql���ò���
			JDBCUtils.handleParams(ps, params);
			count = ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBManager.close(ps, conn);
		}
		return count;
	}

	@Override
	public void insert(Object obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Class cla, Object id) {
		// ��������� Emp.class,2 -->��ô������"delete from emp where id=2"
		
		//ͨ��Class������TableInfo(����)
		TableInfo tableInfo = TableContext.poClassMap.get(cla);
		//�������
		ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();
		String sql = "delete from " +tableInfo.getTname() +" where " +onlyPriKey.getName()+"=?";
		System.out.println(sql+"****************");
		executeDML(sql,new Object[] {id}); 
	}
 
	@Override
	public void delete(Object obj) {
		Class c = obj.getClass();
		TableInfo tableInfo = TableContext.poClassMap.get(c);
		ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();	//����
		
		//ͨ���������,�������Զ�Ӧ��set,get����
		Object priKeyValue = ReflectUtils.invokeGet(onlyPriKey.getName(),obj);
		delete(c,priKeyValue);
		
	}

	@Override
	public int update(Object obj, String[] fileNames) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List queryRows(String sql, Class cla, Object[] params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object queryUniqueRow(String sql, Class cla, Object[] params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object queryValue(String sql, Object[] params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Number queryNumber(String sql, Object[] params) {
		// TODO Auto-generated method stub
		return null;
	}
	
}

package com.simpleorm.core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
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
		e.setEmpname("xiaoou");
		e.setAge(20);
		e.setBirthday(new java.sql.Date(System.currentTimeMillis()));
		
		//new MySqlQuery().delete(e);
		//new MySqlQuery().insert(e);
		//new MySqlQuery().update(e,new String[] {"empname","age","birthday"});
		List<Emp> list = new MySqlQuery().queryRows("select empname,age from emp where id>? and age<?",
				Emp.class, new Object[] {1,100});
		for (Emp emp : list) {
			System.out.println(emp.getEmpname());
		}
	}
	
	
	
	@Override
	public int executeDML(String sql, Object[] params) {
		Connection conn = DBManager.getConn();
		int count = 0;
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			System.out.println(sql.toString()+"*******");
			JDBCUtils.handleParams(ps, params);	//��sql���ò���
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
		
		//insert into ����(id,name,age,address) values(?,?,?,?);
		List<Object> params = new ArrayList<Object>();
		Class c = obj.getClass();
		TableInfo tableInfo = TableContext.poClassMap.get(c);	//��ȡpoClassMap���TableInfo����
		Field[] fs = c.getDeclaredFields();
		StringBuilder sql = new StringBuilder("insert into "+tableInfo.getTname()+" (");
		int countNotNullField = 0; //��¼���Բ�Ϊnull�ĸ���(����ȷ��insert��values)
		for (Field f : fs) {
			String fieldName = f.getName();
			Object fieldValue = ReflectUtils.invokeGet(fieldName, obj);
			
			if(fieldValue!=null) {
				countNotNullField++;
				sql.append(fieldName+",");
				params.add(fieldValue);	//�������,Ϊvalues��׼��
			}
		}
		sql.setCharAt(sql.length()-1, ')');
		sql.append(" values (");
		for (int i = 0; i < countNotNullField; i++) {
			sql.append("?,");	//�м��������ͷż����ʺ���Ϊռλ��
		}
		sql.setCharAt(sql.length()-1, ')');
		System.out.println(sql.toString()+"*******************");
		executeDML(sql.toString(),params.toArray());	//Listתobject�������ֱ��ת
	}

	@Override
	public void delete(Class cla, Object id) {
		// ��������� Emp.class,2 -->��ô������"delete from emp where id=2"
		
		//ͨ��Class������TableInfo(����)
		TableInfo tableInfo = TableContext.poClassMap.get(cla);
		//�������
		ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();
		String sql = "delete from " +tableInfo.getTname() +" where " +onlyPriKey.getName()+"=?";
		//System.out.println(sql+"****************");
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
	public int update(Object obj, String[] fieldNames) {
		//�ο� update ���� set username=?,password=? where id=?;
		List<Object> params = new ArrayList<Object>();
		Class c = obj.getClass();
		TableInfo tableInfo = TableContext.poClassMap.get(c);	//��ȡpoClassMap���TableInfo����
		ColumnInfo priKey = tableInfo.getOnlyPriKey();	//�������,���������޸�
		Field[] fs = c.getDeclaredFields();
		StringBuilder sql = new StringBuilder("update "+tableInfo.getTname()+" set ");
		
		for (String fname : fieldNames) {
			Object fvalue = ReflectUtils.invokeGet(fname, obj);
			params.add(fvalue);	//���������
			sql.append(fname+"=?,");
			
		}
		sql.setCharAt(sql.length()-1, ' ');
		sql.append(" where "+priKey.getName()+" =? ");
		params.add(ReflectUtils.invokeGet(priKey.getName(), obj));	//������ֵ
		
		return executeDML(sql.toString(), params.toArray());
	}

	@Override
	public List queryRows(String sql, Class cla, Object[] params) {
		Connection conn = DBManager.getConn();
		List list = null;	//���ڴ�Ų�ѯ���������
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			JDBCUtils.handleParams(ps, params);	//��sql���ò���
			System.out.println(ps);
			rs = ps.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			//����
			while(rs.next()) {
				if(list==null) {
					list = new ArrayList();
				}
				Object rowObj = cla.newInstance();	//����Javabean���޲ι�����
				//���� ,�ο� select username,age,gender from ���� where id>? and age>?
				for (int i=0;i<metaData.getColumnCount();i++) {
					String  columnName = metaData.getColumnLabel(i+1);
					Object columnValue = rs.getObject(i+1);
					
					//ͨ���������rowObj�Ķ�Ӧset����,��columnValue��ֵ���ý�ȥ
					ReflectUtils.invokeSet(rowObj, columnName, columnValue);//��������rowObj�����columnName����ΪcolumnValue
				}
				list.add(rowObj);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBManager.close(ps, conn);
		}
		return list;
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

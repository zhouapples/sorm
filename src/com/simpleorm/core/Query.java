package com.simpleorm.core;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.simpleorm.bean.ColumnInfo;
import com.simpleorm.bean.TableInfo;
import com.simpleorm.utils.JDBCUtils;
import com.simpleorm.utils.ReflectUtils;

/**
 * �����ѯ:�����ṩ����ĺ�����
 * @author MiKimouse
 *
 */
public abstract class Query {
	
	public Object executeQueryTemplate(String sql,Object[] params,Class cla,CallBack back) {
		Connection conn = DBManager.getConn();
		List list = null;	//���ڴ�Ų�ѯ���������
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			JDBCUtils.handleParams(ps, params);	//��sql���ò���
			System.out.println(ps);
			rs = ps.executeQuery();
			
			return back.doExecute(conn, ps, rs);
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally {
			DBManager.close(ps, conn);
		}
	}
	
	/**
	 * ֱ��ִ��һ��DML���
	 * @param sql	sql���
	 * @param params	��������
	 * @return	������Ӱ������
	 */
	public int executeDML(String sql,Object[] params) {

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
	
	/**
	 * ��һ���������Ϣ���浽���ݿ���
	 * �Ѷ����в�Ϊnull�����Դ��浽���ݿ���,����ȱʧ��Ĭ��Ϊ0
	 * @param obj	Ҫ����Ķ���
	 */
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
	
	/**
	 * ɾ��cla��Ӧ���еļ�¼
	 * @param cla	Ҫɾ���Ķ���
	 * @param id	Ҫɾ��������id
	 * @return	
	 */
	public void delete(Class cla,Object id) {
		// ��������� Emp.class,2 -->��ô������"delete from emp where id=2"
		
		//ͨ��Class������TableInfo(����)
		TableInfo tableInfo = TableContext.poClassMap.get(cla);
		//�������
		ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();
		String sql = "delete from " +tableInfo.getTname() +" where " +onlyPriKey.getName()+"=?";
		executeDML(sql,new Object[] {id}); 
	}
	
	/**
	 * ɾ������obj��Ӧ�����ݿ��еļ�¼
	 * @param obj	Ҫɾ���Ķ���
	 */
	public void delete(Object obj) {
		Class c = obj.getClass();
		TableInfo tableInfo = TableContext.poClassMap.get(c);
		ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();	//����
		
		//ͨ���������,�������Զ�Ӧ��set,get����
		Object priKeyValue = ReflectUtils.invokeGet(onlyPriKey.getName(),obj);
		delete(c,priKeyValue);
	}
	
	/**
	 * ���¶����Ӧ�ļ�¼,ֻ����ָ���ֶ�
	 * @param obj	Ҫ���µĶ���
	 * @param fileNames	Ҫ���µ����Ա�
	 * @return	��Ӱ������
	 */
	public int update(Object obj,String[] fieldNames) {
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
	
	/**
	 * ��ѯ���ض��м�¼,ÿ�м�¼��ŵ�cla������
	 * @param sql	��ѯ���
	 * @param cla	��װ��ѯ�����javabean����
	 * @param params	sql����
	 * @return	����һ��������cla���б�
	 */
	public List queryRows(String sql,Class cla,Object[] params) {
		
		return (List)executeQueryTemplate(sql, params, cla, new CallBack() {
			
			@Override
			public Object doExecute(Connection conn, PreparedStatement ps, ResultSet rs) {
				List list = null;	//���ڴ�Ų�ѯ���������
				try {
					if(list==null) {
						list = new ArrayList();
					}
					ResultSetMetaData metaData = rs.getMetaData();
					//����
					while(rs.next()) {
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
				}
				return list;
			}
		});
	}
	
	/**
	 * ����ֻ��һ�м�¼�Ķ���
	 * @param sql	��ѯ���
	 * @param cla 	��װ����õ�Javabean
	 * @param params	��ѯ����
	 * @return	��װ����õ�Javabean
	 */
	public Object queryUniqueRow(String sql,Class cla,Object[] params) {
		List list = queryRows(sql,cla,params);
		return (list==null&&list.size()>0)?null:list.get(0);	//���list��Ϊ��,ֻ�õ�һ��

	}
	
	/**
	 * ����һ��ֵ(һ��һ��)
	 * @param sql	��ѯ���
	 * @param params	sql����
	 * @return	���ص�ֵ�Ķ���
	 */
	public Object queryValue(String sql,Object[] params) {
		Connection conn = DBManager.getConn();
		Object value = null;	//���ڴ�Ų�ѯ���������
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			JDBCUtils.handleParams(ps, params);	//��sql���ò���
			System.out.println(ps);
			rs = ps.executeQuery();
			//����
			while(rs.next()) {
				value = rs.getObject(1);
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBManager.close(ps, conn);
		}
		return value;
	}
	
	/**
	 * ����һ������
	 * @param sql	��ѯ���
	 * @param params	sql����
	 * @return	�鵽�Ľ��
	 */
	public Number queryNumber(String sql,Object[] params) {
		return (Number)queryValue(sql,params);
	}
	
	/**
	 * ��ҳ��ѯ
	 * @param pageNum	��ǰ�ڼ�ҳ
	 * @param size	ÿҳ��ʾ������
	 * @return
	 */
	public abstract Object queryPagenate(int pageNum,int size);
	
}

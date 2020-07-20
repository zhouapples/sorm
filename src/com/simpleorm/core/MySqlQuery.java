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
 * 负责针对mysql数据库的查询
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
			JDBCUtils.handleParams(ps, params);	//给sql设置参数
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
		
		//insert into 表名(id,name,age,address) values(?,?,?,?);
		List<Object> params = new ArrayList<Object>();
		Class c = obj.getClass();
		TableInfo tableInfo = TableContext.poClassMap.get(c);	//获取poClassMap里的TableInfo对象
		Field[] fs = c.getDeclaredFields();
		StringBuilder sql = new StringBuilder("insert into "+tableInfo.getTname()+" (");
		int countNotNullField = 0; //记录属性不为null的个数(用于确定insert的values)
		for (Field f : fs) {
			String fieldName = f.getName();
			Object fieldValue = ReflectUtils.invokeGet(fieldName, obj);
			
			if(fieldValue!=null) {
				countNotNullField++;
				sql.append(fieldName+",");
				params.add(fieldValue);	//储存参数,为values做准备
			}
		}
		sql.setCharAt(sql.length()-1, ')');
		sql.append(" values (");
		for (int i = 0; i < countNotNullField; i++) {
			sql.append("?,");	//有几个参数就放几个问号作为占位符
		}
		sql.setCharAt(sql.length()-1, ')');
		System.out.println(sql.toString()+"*******************");
		executeDML(sql.toString(),params.toArray());	//List转object数组可以直接转
	}

	@Override
	public void delete(Class cla, Object id) {
		// 如果传进来 Emp.class,2 -->那么就生成"delete from emp where id=2"
		
		//通过Class对象找TableInfo(反射)
		TableInfo tableInfo = TableContext.poClassMap.get(cla);
		//获得主键
		ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();
		String sql = "delete from " +tableInfo.getTname() +" where " +onlyPriKey.getName()+"=?";
		//System.out.println(sql+"****************");
		executeDML(sql,new Object[] {id}); 
	}
 
	@Override
	public void delete(Object obj) {
		Class c = obj.getClass();
		TableInfo tableInfo = TableContext.poClassMap.get(c);
		ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();	//主键
		
		//通过反射机制,调用属性对应的set,get方法
		Object priKeyValue = ReflectUtils.invokeGet(onlyPriKey.getName(),obj);
		delete(c,priKeyValue);
		
	}

	@Override
	public int update(Object obj, String[] fieldNames) {
		//参考 update 表名 set username=?,password=? where id=?;
		List<Object> params = new ArrayList<Object>();
		Class c = obj.getClass();
		TableInfo tableInfo = TableContext.poClassMap.get(c);	//获取poClassMap里的TableInfo对象
		ColumnInfo priKey = tableInfo.getOnlyPriKey();	//获得主键,根据主键修改
		Field[] fs = c.getDeclaredFields();
		StringBuilder sql = new StringBuilder("update "+tableInfo.getTname()+" set ");
		
		for (String fname : fieldNames) {
			Object fvalue = ReflectUtils.invokeGet(fname, obj);
			params.add(fvalue);	//存入参数集
			sql.append(fname+"=?,");
			
		}
		sql.setCharAt(sql.length()-1, ' ');
		sql.append(" where "+priKey.getName()+" =? ");
		params.add(ReflectUtils.invokeGet(priKey.getName(), obj));	//主键的值
		
		return executeDML(sql.toString(), params.toArray());
	}

	@Override
	public List queryRows(String sql, Class cla, Object[] params) {
		Connection conn = DBManager.getConn();
		List list = null;	//用于存放查询结果的容器
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			JDBCUtils.handleParams(ps, params);	//给sql设置参数
			System.out.println(ps);
			rs = ps.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			//多行
			while(rs.next()) {
				if(list==null) {
					list = new ArrayList();
				}
				Object rowObj = cla.newInstance();	//调用Javabean的无参构造器
				//多列 ,参考 select username,age,gender from 表名 where id>? and age>?
				for (int i=0;i<metaData.getColumnCount();i++) {
					String  columnName = metaData.getColumnLabel(i+1);
					Object columnValue = rs.getObject(i+1);
					
					//通过反射调用rowObj的对应set方法,将columnValue的值设置进去
					ReflectUtils.invokeSet(rowObj, columnName, columnValue);//就是设置rowObj对象的columnName属性为columnValue
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

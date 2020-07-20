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
 * 负责查询:对外提供服务的核心类
 * @author MiKimouse
 *
 */
public abstract class Query {
	
	public Object executeQueryTemplate(String sql,Object[] params,Class cla,CallBack back) {
		Connection conn = DBManager.getConn();
		List list = null;	//用于存放查询结果的容器
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			JDBCUtils.handleParams(ps, params);	//给sql设置参数
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
	 * 直接执行一个DML语句
	 * @param sql	sql语句
	 * @param params	其他参数
	 * @return	返回受影响行数
	 */
	public int executeDML(String sql,Object[] params) {

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
	
	/**
	 * 将一个对象的信息储存到数据库中
	 * 把对象中不为null的属性储存到数据库中,数字缺失的默认为0
	 * @param obj	要储存的对象
	 */
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
	
	/**
	 * 删除cla对应表中的记录
	 * @param cla	要删除的对象
	 * @param id	要删除的主键id
	 * @return	
	 */
	public void delete(Class cla,Object id) {
		// 如果传进来 Emp.class,2 -->那么就生成"delete from emp where id=2"
		
		//通过Class对象找TableInfo(反射)
		TableInfo tableInfo = TableContext.poClassMap.get(cla);
		//获得主键
		ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();
		String sql = "delete from " +tableInfo.getTname() +" where " +onlyPriKey.getName()+"=?";
		executeDML(sql,new Object[] {id}); 
	}
	
	/**
	 * 删除对象obj对应在数据库中的记录
	 * @param obj	要删除的对象
	 */
	public void delete(Object obj) {
		Class c = obj.getClass();
		TableInfo tableInfo = TableContext.poClassMap.get(c);
		ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();	//主键
		
		//通过反射机制,调用属性对应的set,get方法
		Object priKeyValue = ReflectUtils.invokeGet(onlyPriKey.getName(),obj);
		delete(c,priKeyValue);
	}
	
	/**
	 * 更新对象对应的记录,只更新指定字段
	 * @param obj	要更新的对象
	 * @param fileNames	要更新的属性表
	 * @return	受影响行数
	 */
	public int update(Object obj,String[] fieldNames) {
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
	
	/**
	 * 查询返回多行记录,每行记录存放到cla对象中
	 * @param sql	查询语句
	 * @param cla	封装查询结果的javabean对象
	 * @param params	sql参数
	 * @return	返回一个含所有cla的列表
	 */
	public List queryRows(String sql,Class cla,Object[] params) {
		
		return (List)executeQueryTemplate(sql, params, cla, new CallBack() {
			
			@Override
			public Object doExecute(Connection conn, PreparedStatement ps, ResultSet rs) {
				List list = null;	//用于存放查询结果的容器
				try {
					if(list==null) {
						list = new ArrayList();
					}
					ResultSetMetaData metaData = rs.getMetaData();
					//多行
					while(rs.next()) {
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
				}
				return list;
			}
		});
	}
	
	/**
	 * 返回只有一行记录的对象
	 * @param sql	查询语句
	 * @param cla 	封装结果用的Javabean
	 * @param params	查询参数
	 * @return	封装结果用的Javabean
	 */
	public Object queryUniqueRow(String sql,Class cla,Object[] params) {
		List list = queryRows(sql,cla,params);
		return (list==null&&list.size()>0)?null:list.get(0);	//如果list不为空,只拿第一条

	}
	
	/**
	 * 返回一个值(一行一列)
	 * @param sql	查询语句
	 * @param params	sql参数
	 * @return	返回的值的对象
	 */
	public Object queryValue(String sql,Object[] params) {
		Connection conn = DBManager.getConn();
		Object value = null;	//用于存放查询结果的容器
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			JDBCUtils.handleParams(ps, params);	//给sql设置参数
			System.out.println(ps);
			rs = ps.executeQuery();
			//多行
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
	 * 返回一个数字
	 * @param sql	查询语句
	 * @param params	sql参数
	 * @return	查到的结果
	 */
	public Number queryNumber(String sql,Object[] params) {
		return (Number)queryValue(sql,params);
	}
	
	/**
	 * 分页查询
	 * @param pageNum	当前第几页
	 * @param size	每页显示数据量
	 * @return
	 */
	public abstract Object queryPagenate(int pageNum,int size);
	
}

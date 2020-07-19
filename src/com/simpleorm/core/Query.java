package com.simpleorm.core;

import java.util.List;

/**
 * 负责查询:对外提供服务的核心类
 * @author MiKimouse
 *
 */
public interface Query {
	/**
	 * 直接执行一个DML语句
	 * @param sql	sql语句
	 * @param params	其他参数
	 * @return	返回受影响行数
	 */
	public int executeDML(String sql,Object[] params);
	
	/**
	 * 将一个对象的信息储存到数据库中
	 * @param obj	要储存的对象
	 */
	public void insert(Object obj);
	
	/**
	 * 删除cla对应表中的记录
	 * @param cla	要删除的对象
	 * @param id	要删除的主键id
	 * @return	
	 */
	public void delete(Class cla,int id);
	
	/**
	 * 删除对象obj对应在数据库中的记录
	 * @param obj	要删除的对象
	 */
	public void delete(Object obj);
	
	/**
	 * 更新对象对应的记录,只更新指定字段
	 * @param obj	要更新的对象
	 * @param fileNames	要更新的属性表
	 * @return	受影响行数
	 */
	public int update(Object obj,String[] fileNames);
	
	/**
	 * 查询返回多行记录,每行记录存放到cla对象中
	 * @param sql	查询语句
	 * @param cla	封装查询结果的javabean对象
	 * @param params	sql参数
	 * @return	返回一个含所有cla的列表
	 */
	public List queryRows(String sql,Class cla,Object[] params);
	
	/**
	 * 返回只有一行记录的对象
	 * @param sql	查询语句
	 * @param cla 	封装结果用的Javabean
	 * @param params	查询参数
	 * @return	封装结果用的Javabean
	 */
	public Object queryUniqueRow(String sql,Class cla,Object[] params);
	
	/**
	 * 返回一个值(一行一列)
	 * @param sql	查询语句
	 * @param params	sql参数
	 * @return	返回的值的对象
	 */
	public Object queryValue(String sql,Object[] params);
	
	/**
	 * 返回一个数字
	 * @param sql	查询语句
	 * @param params	sql参数
	 * @return	查到的结果
	 */
	public Number queryNumber(String sql,Object[] params);
	
	
}

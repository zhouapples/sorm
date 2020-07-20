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
public class MySqlQuery extends Query{
	public static void main(String[] args) {
		Emp e = new Emp();
		e.setId(1);
		e.setEmpname("xiaoou");
		e.setAge(20);
		e.setBirthday(new java.sql.Date(System.currentTimeMillis()));
		
		//new MySqlQuery().delete(e);
		//new MySqlQuery().insert(e);
		//new MySqlQuery().update(e,new String[] {"empname","age","birthday"});
		/*List<Emp> list = new MySqlQuery().queryRows("select empname,age from emp where id>? and age<?",
				Emp.class, new Object[] {1,100});
		for (Emp emp : list) {
			System.out.println(emp.getEmpname());
		}*/
		
		Object obj = new MySqlQuery().queryValue("select count(1) from emp where id<?", new Object[] {5});
		System.out.println(obj);
	}

	
	
	
}

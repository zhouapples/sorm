package com.simpleorm.unittest;

import java.util.List;

import com.simpleorm.core.MySqlQuery;
import com.simpleorm.core.Query;
import com.simpleorm.core.QueryFactory;
import com.simpleorm.po.Emp;
/**
 * 测试QueryFactory查询工厂单例获取
 * @author MiKimouse
 *
 */
public class Test0 {
	public static void main(String[] args) {
		Query q = QueryFactory.createQuery();
		
		List<Emp> list = q.queryRows("select empname,age from emp where id>? and age<?",
		Emp.class, new Object[] {1,100});
		for (Emp emp : list) {
			System.out.println(emp.getEmpname());
		}
	}
}

package com.simpleorm.unittest;

import java.util.List;

import com.simpleorm.core.MySqlQuery;
import com.simpleorm.core.Query;
import com.simpleorm.core.QueryFactory;
import com.simpleorm.po.Emp;

/**
 * 测试连接池效率
 * @author MiKimouse
 *
 */
public class Test1 {
	
	public static Object test() {
		Query q = QueryFactory.createQuery();
		Object obj = new MySqlQuery().queryValue("select e.empname name,d.dname part from emp e left join dept d on e.deptId=d.id limit ?", new Object[] {1});
		return obj;
		//System.out.println(obj);
	}
	
	
	
	
	public static void main(String[] args) {
		
		long s = System.currentTimeMillis();
		for (int i = 0; i < 1000; i++) {
			test();			
		}
		long e = System.currentTimeMillis();
		System.out.println("查询3000次共耗时:"+(e-s)+"ms");
		
		//未使用连接池查询3000次共耗时:27748ms
		// 使用连接池查询3000次共耗时:2926ms	//效率提升明显
		
		
		
		
		
	}
}

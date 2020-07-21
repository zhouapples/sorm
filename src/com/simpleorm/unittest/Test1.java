package com.simpleorm.unittest;

import java.util.List;

import com.simpleorm.core.MySqlQuery;
import com.simpleorm.core.Query;
import com.simpleorm.core.QueryFactory;
import com.simpleorm.po.Emp;

/**
 * �������ӳ�Ч��
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
		System.out.println("��ѯ3000�ι���ʱ:"+(e-s)+"ms");
		
		//δʹ�����ӳز�ѯ3000�ι���ʱ:27748ms
		// ʹ�����ӳز�ѯ3000�ι���ʱ:2926ms	//Ч����������
		
		
		
		
		
	}
}

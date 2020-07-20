package com.simpleorm.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 封装了反射常用操作
 * @author MiKimouse
 *
 */
public class ReflectUtils {
	
	/**
	 * 调用obj对象对应属性filename的get方法
	 * @param fieldName	属性名
	 * @param obj	
	 * @return
	 */
	public static Object invokeGet(String fieldName,Object obj) {
		try {
			Class c = obj.getClass();
			Method m = c.getMethod("get"+StringUtils.firstChar2Upper(fieldName),null);
			return m.invoke(obj, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static void invokeSet(Object obj,String columnName,Object columnValue) {
		//通过反射调用rowObj的对应set方法,将columnValue的值设置进去
		try {
			Method m = obj.getClass().getDeclaredMethod("set"+StringUtils.firstChar2Upper(columnName),
					columnValue.getClass() );
			
			m.invoke(obj, columnValue);	//获取到方法就要去调用,设置参数
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

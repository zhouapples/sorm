package com.simpleorm.utils;

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
}

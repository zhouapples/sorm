package com.simpleorm.utils;

import java.lang.reflect.Method;

/**
 * ��װ�˷��䳣�ò���
 * @author MiKimouse
 *
 */
public class ReflectUtils {
	
	/**
	 * ����obj�����Ӧ����filename��get����
	 * @param fieldName	������
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

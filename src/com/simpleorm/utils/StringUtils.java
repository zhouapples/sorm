package com.simpleorm.utils;
/**
 * ��װ���ַ������ò���
 * @author MiKimouse
 *
 */
public class StringUtils {
	
	/**
	 * �������ַ�������ĸ��Ϊ��д
	 * @param str	�����ַ���
	 * @return	����ĸ��Ϊ��д�Ľ��
	 */
	public static String firstChar2Upper(String str) {
		//str.toUpperCase().substring(0,1) �ĳɴ�д��ȡ����һλ
		//str.substring(1);	���ַ����ĵڶ�λȡ�����
		return str.toUpperCase().substring(0,1)+str.substring(1);
	}
}

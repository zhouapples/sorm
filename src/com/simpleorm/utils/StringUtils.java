package com.simpleorm.utils;
/**
 * 封装了字符串常用操作
 * @author MiKimouse
 *
 */
public class StringUtils {
	
	/**
	 * 将传入字符串首字母变为大写
	 * @param str	传入字符串
	 * @return	首字母变为大写的结果
	 */
	public static String firstChar2Upper(String str) {
		//str.toUpperCase().substring(0,1) 改成大写后取出第一位
		//str.substring(1);	从字符串的第二位取到最后
		return str.toUpperCase().substring(0,1)+str.substring(1);
	}
}

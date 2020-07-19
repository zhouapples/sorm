package com.simpleorm.core;
/**
 * 负责java数据类型和数据库类型的转换
 * @author MiKimouse
 *
 */
public interface TypeConvertor {
	
	/**
	 * 将数据库类型转为数据库类型
	 * @param columnType	数据库字段的数字类型
	 * @return	java的数据类型
	 */
	public String databaseType2JavaType(String columnType);
	
	/**
	 * 将java数据类型转化为数据库的类型
	 * @param columnType
	 * @return	数据库类型
	 */
	public String JavaType2DatabaseType(String JavaType);
	
	
	
}

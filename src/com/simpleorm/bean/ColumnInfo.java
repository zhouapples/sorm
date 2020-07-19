package com.simpleorm.bean;
/**
 * 封装数据库表中一个字段的信息
 * @author MiKimouse
 * @version 0.8
 */
public class ColumnInfo {
	/**
	 * 字段名称
	 */
	private String name;
	
	/**
	 * 字段数据类型
	 */
	private String dataType;
	
	/**
	 * 字段键类型(0:普通,1:主键,2:外键)
	 */
	private String keyType;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getKeyType() {
		return keyType;
	}

	public void setKeyType(String keyType) {
		this.keyType = keyType;
	}

	public ColumnInfo(String name, String dataType, String keyType) {
		super();
		this.name = name;
		this.dataType = dataType;
		this.keyType = keyType;
	}

	public ColumnInfo() {
		super();
	}
	
	
}

package com.simpleorm.bean;
/**
 * ��װ���ݿ����һ���ֶε���Ϣ
 * @author MiKimouse
 * @version 0.8
 */
public class ColumnInfo {
	/**
	 * �ֶ�����
	 */
	private String name;
	
	/**
	 * �ֶ���������
	 */
	private String dataType;
	
	/**
	 * �ֶμ�����(0:��ͨ,1:����,2:���)
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

package com.simpleorm.core;
/**
 * ����java�������ͺ����ݿ����͵�ת��
 * @author MiKimouse
 *
 */
public interface TypeConvertor {
	
	/**
	 * �����ݿ�����תΪ���ݿ�����
	 * @param columnType	���ݿ��ֶε���������
	 * @return	java����������
	 */
	public String databaseType2JavaType(String columnType);
	
	/**
	 * ��java��������ת��Ϊ���ݿ������
	 * @param columnType
	 * @return	���ݿ�����
	 */
	public String JavaType2DatabaseType(String JavaType);
	
	
	
}

package com.simpleorm.core;

import java.util.List;

/**
 * �����ѯ:�����ṩ����ĺ�����
 * @author MiKimouse
 *
 */
public interface Query {
	/**
	 * ֱ��ִ��һ��DML���
	 * @param sql	sql���
	 * @param params	��������
	 * @return	������Ӱ������
	 */
	public int executeDML(String sql,Object[] params);
	
	/**
	 * ��һ���������Ϣ���浽���ݿ���
	 * @param obj	Ҫ����Ķ���
	 */
	public void insert(Object obj);
	
	/**
	 * ɾ��cla��Ӧ���еļ�¼
	 * @param cla	Ҫɾ���Ķ���
	 * @param id	Ҫɾ��������id
	 * @return	
	 */
	public void delete(Class cla,int id);
	
	/**
	 * ɾ������obj��Ӧ�����ݿ��еļ�¼
	 * @param obj	Ҫɾ���Ķ���
	 */
	public void delete(Object obj);
	
	/**
	 * ���¶����Ӧ�ļ�¼,ֻ����ָ���ֶ�
	 * @param obj	Ҫ���µĶ���
	 * @param fileNames	Ҫ���µ����Ա�
	 * @return	��Ӱ������
	 */
	public int update(Object obj,String[] fileNames);
	
	/**
	 * ��ѯ���ض��м�¼,ÿ�м�¼��ŵ�cla������
	 * @param sql	��ѯ���
	 * @param cla	��װ��ѯ�����javabean����
	 * @param params	sql����
	 * @return	����һ��������cla���б�
	 */
	public List queryRows(String sql,Class cla,Object[] params);
	
	/**
	 * ����ֻ��һ�м�¼�Ķ���
	 * @param sql	��ѯ���
	 * @param cla 	��װ����õ�Javabean
	 * @param params	��ѯ����
	 * @return	��װ����õ�Javabean
	 */
	public Object queryUniqueRow(String sql,Class cla,Object[] params);
	
	/**
	 * ����һ��ֵ(һ��һ��)
	 * @param sql	��ѯ���
	 * @param params	sql����
	 * @return	���ص�ֵ�Ķ���
	 */
	public Object queryValue(String sql,Object[] params);
	
	/**
	 * ����һ������
	 * @param sql	��ѯ���
	 * @param params	sql����
	 * @return	�鵽�Ľ��
	 */
	public Number queryNumber(String sql,Object[] params);
	
	
}

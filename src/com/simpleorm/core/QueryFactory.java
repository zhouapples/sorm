package com.simpleorm.core;
/**
 * Query������,������ֻ����һ��
 * ���õ���ģʽ
 * @author MiKimouse
 *
 */
public class QueryFactory{
	
	private static QueryFactory factory = new QueryFactory();
	private static Query prototypeObj;
	static {
		try {
			Class c = Class.forName(DBManager.getConf().getQueryClass());	//������ز�ѯ��
			prototypeObj = (Query) c.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private QueryFactory() {	//˽�л�������
		
	}
	
	/**
	 * ��¡ģʽ
	 * @return
	 */
	public static Query createQuery() {	
		try {
			return (Query) prototypeObj.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
}

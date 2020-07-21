package com.simpleorm.core;
/**
 * Query工厂类,工厂类只能有一个
 * 设置单例模式
 * @author MiKimouse
 *
 */
public class QueryFactory{
	
	private static QueryFactory factory = new QueryFactory();
	private static Query prototypeObj;
	static {
		try {
			Class c = Class.forName(DBManager.getConf().getQueryClass());	//反射加载查询类
			prototypeObj = (Query) c.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private QueryFactory() {	//私有化构造器
		
	}
	
	/**
	 * 克隆模式
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

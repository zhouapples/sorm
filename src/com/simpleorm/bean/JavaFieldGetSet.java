package com.simpleorm.bean;
/**
 * ��װjava�������Լ�get,set����Դ����
 * javabean����������װ���ݵ�
 * @author MiKimouse
 *
 */
public class JavaFieldGetSet {
	/**
	 * ���Ե�Դ����Ϣ
	 */
	private String fieldInfo;
	
	/**
	 * get����Դ����Ϣ
	 */
	private String getInfo;
	
	/**
	 * set����Դ����Ϣ,��public void setUserId(int id){this.id=id}
	 */
	private String setInfo;

	public JavaFieldGetSet(String fieldInfo, String getInfo, String setInfo) {
		super();
		this.fieldInfo = fieldInfo;
		this.getInfo = getInfo;
		this.setInfo = setInfo;
	}

	public JavaFieldGetSet() {
		super();
	}

	public String getFieldInfo() {
		return fieldInfo;
	}

	public void setFieldInfo(String fieldInfo) {
		this.fieldInfo = fieldInfo;
	}

	public String getGetInfo() {
		return getInfo;
	}

	public void setGetInfo(String getInfo) {
		this.getInfo = getInfo;
	}

	public String getSetInfo() {
		return setInfo;
	}

	public void setSetInfo(String setInfo) {
		this.setInfo = setInfo;
	}
	
	//�������,��дtoString����
	public String toString() {
		System.out.println(fieldInfo);
		System.out.println(getInfo);
		System.out.println(setInfo);
		return super.toString();
	}
	
	
	
	
}

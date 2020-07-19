package com.simpleorm.bean;
/**
 * ����������Ϣ
 * @author MiKimouse
 *
 */
public class Configuration {
	/**
	 * ���ݿ�����
	 */
	private String driver;	//���ݿ�����
	
	/**
	 * jdbc��url-->���ݿ��ַ
	 */
	private String url;	//���ݿ��ַ
	
	/**
	 * ���ݿ��û���
	 */
	private String user;	//�û���
	
	/**
	 * ���ݿ�����
	 */
	private String pwd;	//����
	
	/**
	 * ����ʹ�õ����ݿ�����
	 */
	private String usingDB;	//����ʹ�õ����ݿ�����
	
	/**
	 * ��orm��Ŀ��·��
	 */
	private String srcPath;	//��Ŀ·��
	
	/**
	 * persist to package,ɨ������java��İ�
	 */
	private String poPackage;	//persist2package·��,���־û�·��

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getUsingDB() {
		return usingDB;
	}

	public void setUsingDB(String usingDB) {
		this.usingDB = usingDB;
	}

	public String getSrcPath() {
		return srcPath;
	}

	public void setSrcPath(String srcPath) {
		this.srcPath = srcPath;
	}

	public String getPoPackage() {
		return poPackage;
	}

	public void setPoPackage(String poPackage) {
		this.poPackage = poPackage;
	}

	public Configuration(String driver, String url, String user, String pwd, String usingDB, String srcPath,
			String poPackage) {
		super();
		this.driver = driver;
		this.url = url;
		this.user = user;
		this.pwd = pwd;
		this.usingDB = usingDB;
		this.srcPath = srcPath;
		this.poPackage = poPackage;
	}

	public Configuration() {
		super();
	}
	
	
}

package com.simpleorm.bean;
/**
 * 管理配置信息
 * @author MiKimouse
 *
 */
public class Configuration {
	/**
	 * 数据库驱动
	 */
	private String driver;	//数据库驱动
	
	/**
	 * jdbc的url-->数据库地址
	 */
	private String url;	//数据库地址
	
	/**
	 * 数据库用户名
	 */
	private String user;	//用户名
	
	/**
	 * 数据库密码
	 */
	private String pwd;	//密码
	
	/**
	 * 正在使用的数据库类型
	 */
	private String usingDB;	//正在使用的数据库类型
	
	/**
	 * 本orm项目的路径
	 */
	private String srcPath;	//项目路径
	
	/**
	 * persist to package,扫描生成java类的包
	 */
	private String poPackage;	//persist2package路径,即持久化路径

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

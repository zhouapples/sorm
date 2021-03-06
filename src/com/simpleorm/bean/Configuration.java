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
	 * 连接池中最小连接数
	 */
	private int poolMinSize;
	
	/**
	 * 连接池中最大连接数
	 */
	private int poolMaxSize;
	
	/**
	 * persist to package,扫描生成java类的包
	 */
	private String poPackage;	//persist2package路径,即持久化路径
	
	/**
	 * 项目使用查询类的路径(使用哪个查询类来执行查询)
	 */
	private String queryClass;
	

	public String getQueryClass() {
		return queryClass;
	}

	public void setQueryClass(String queryClass) {
		this.queryClass = queryClass;
	}

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
	
	public int getPoolMinSize() {
		return poolMinSize;
	}

	public void setPoolMinSize(int poolMinSize) {
		this.poolMinSize = poolMinSize;
	}

	public int getPoolMaxSize() {
		return poolMaxSize;
	}

	public void setPoolMaxSize(int poolMaxSize) {
		this.poolMaxSize = poolMaxSize;
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

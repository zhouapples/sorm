package com.simpleorm.core;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.simpleorm.bean.ColumnInfo;
import com.simpleorm.bean.TableInfo;
import com.simpleorm.utils.JavaFileUtils;
import com.simpleorm.utils.StringUtils;

/**
 * 负责管理数据库
 * 管理数据库表结构和类的关系
 * 可以根据表结构生成类结构
 * @author MiKimouse
 *
 */
public class TableContext {
	/**
	 * 表名为key,表信息对象为value
	 */
	public static Map<String,TableInfo> tables = new HashMap<String,TableInfo>();
	
	/**
	 * 将po的class对象和表信息对象关联起来,便于重用
	 */
	public static Map<Class,TableInfo> poClassMap = new HashMap<Class,TableInfo>();
	
	private TableContext() {}
	
	static {
		try {	//初始化获得表信息
			Connection con = DBManager.getConn();
			DatabaseMetaData dbmd = con.getMetaData();
			
			ResultSet tableRet = dbmd.getTables(null, "%", "%", new String[] {"TABLE"});
			while(tableRet.next()) {
				String tableName = (String) tableRet.getObject("TABLE_NAME");
				TableInfo ti = new TableInfo(tableName,new ArrayList<ColumnInfo>(),new HashMap<String,ColumnInfo>());
				tables.put(tableName,ti);
				
				ResultSet set = dbmd.getColumns(null, "%", tableName, "%");	//查询表中所有字段
				while(set.next()) {
					ColumnInfo ci = new ColumnInfo(set.getString("COLUMN_NAME"),
							set.getString("TYPE_NAME"),"0");
					ti.getColumns().put(set.getString("COLUMN_NAME"), ci);
					
					ResultSet set2 = dbmd.getPrimaryKeys(null, "%", tableName);	//查询表中主键
					while(set2.next()) {
						ColumnInfo ci2 = (ColumnInfo)ti.getColumns().get(set2.getObject("COLUMN_NAME"));
						ci2.setKeyType("1");
						ti.getPriKeys().add(ci2);
					}
					if(ti.getPriKeys().size()>0) {	//取唯一主键,方便使用,如果是联合主键则为空
						ti.setOnlyPriKey(ti.getPriKeys().get(0));
					}
					
				}
				
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//更新java类结构(类初始化时就执行)
		updateJavaPOFile();
		//加载po包下的表类
		loadPOTable();
	}
	
	/**
	 * 根据表结构生成相应的java类到po包下
	 */
	public static void updateJavaPOFile() {
		Map<String,TableInfo> map = TableContext.tables;
		for (TableInfo t : map.values()) {
			JavaFileUtils.createJavaPOFile(t, new MySqlTypeConvertor());
		}
	}
	
	/**
	 * 加载po包下的类
	 */
	public static void loadPOTable() {	//
		
		for (TableInfo tableInfo : tables.values()) {
			Class c;
			try {
				c = Class.forName(DBManager.getConf().getPoPackage()+"."
							+StringUtils.firstChar2Upper(tableInfo.getTname()));
				poClassMap.put(c, tableInfo);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void main(String[] args) {
		Map<String,TableInfo> tables = TableContext.tables;
		System.out.println(tables);
	}
	
	
	
}

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
 * ����������ݿ�
 * �������ݿ��ṹ����Ĺ�ϵ
 * ���Ը��ݱ�ṹ������ṹ
 * @author MiKimouse
 *
 */
public class TableContext {
	/**
	 * ����Ϊkey,����Ϣ����Ϊvalue
	 */
	public static Map<String,TableInfo> tables = new HashMap<String,TableInfo>();
	
	/**
	 * ��po��class����ͱ���Ϣ�����������,��������
	 */
	public static Map<Class,TableInfo> poClassMap = new HashMap<Class,TableInfo>();
	
	private TableContext() {}
	
	static {
		try {	//��ʼ����ñ���Ϣ
			Connection con = DBManager.getConn();
			DatabaseMetaData dbmd = con.getMetaData();
			
			ResultSet tableRet = dbmd.getTables(null, "%", "%", new String[] {"TABLE"});
			while(tableRet.next()) {
				String tableName = (String) tableRet.getObject("TABLE_NAME");
				TableInfo ti = new TableInfo(tableName,new ArrayList<ColumnInfo>(),new HashMap<String,ColumnInfo>());
				tables.put(tableName,ti);
				
				ResultSet set = dbmd.getColumns(null, "%", tableName, "%");	//��ѯ���������ֶ�
				while(set.next()) {
					ColumnInfo ci = new ColumnInfo(set.getString("COLUMN_NAME"),
							set.getString("TYPE_NAME"),"0");
					ti.getColumns().put(set.getString("COLUMN_NAME"), ci);
					
					ResultSet set2 = dbmd.getPrimaryKeys(null, "%", tableName);	//��ѯ��������
					while(set2.next()) {
						ColumnInfo ci2 = (ColumnInfo)ti.getColumns().get(set2.getObject("COLUMN_NAME"));
						ci2.setKeyType("1");
						ti.getPriKeys().add(ci2);
					}
					if(ti.getPriKeys().size()>0) {	//ȡΨһ����,����ʹ��,���������������Ϊ��
						ti.setOnlyPriKey(ti.getPriKeys().get(0));
					}
					
				}
				
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//����java��ṹ(���ʼ��ʱ��ִ��)
		updateJavaPOFile();
		//����po���µı���
		loadPOTable();
	}
	
	/**
	 * ���ݱ�ṹ������Ӧ��java�ൽpo����
	 */
	public static void updateJavaPOFile() {
		Map<String,TableInfo> map = TableContext.tables;
		for (TableInfo t : map.values()) {
			JavaFileUtils.createJavaPOFile(t, new MySqlTypeConvertor());
		}
	}
	
	/**
	 * ����po���µ���
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

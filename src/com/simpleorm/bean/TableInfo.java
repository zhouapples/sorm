package com.simpleorm.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 储存表结构信息
 * @author MiKimouse
 *
 */
public class TableInfo {
	/**
	 * 表名
	 */
	private String tname;
	
	/**
	 * 所有字段的信息
	 */
	private Map<String,ColumnInfo> columns;
	
	/**
	 * 唯一主键(目前只能处理表中只有一个主键这种情况)
	 */
	private ColumnInfo onlyPriKey;
	
	/**
	 * 联合主键,后期拓展可能用到
	 *
	 */
	private List<ColumnInfo> priKeys;
	
	

	public List<ColumnInfo> getPriKeys() {
		return priKeys;
	}

	public void setPriKeys(List<ColumnInfo> priKeys) {
		this.priKeys = priKeys;
	}

	public String getTname() {
		return tname;
	}

	public void setTname(String tname) {
		this.tname = tname;
	}

	public Map<String, ColumnInfo> getColumns() {
		return columns;
	}

	public void setColumns(Map<String, ColumnInfo> columns) {
		this.columns = columns;
	}

	public ColumnInfo getOnlyPriKey() {
		return onlyPriKey;
	}

	public void setOnlyPriKey(ColumnInfo onlyPriKey) {
		this.onlyPriKey = onlyPriKey;
	}

	public TableInfo(String tname, Map<String, ColumnInfo> columns, ColumnInfo onlyPriKey) {
		super();
		this.tname = tname;
		this.columns = columns;
		this.onlyPriKey = onlyPriKey;
	}

	public TableInfo() {
		super();
	}

	public TableInfo(String tname,  List<ColumnInfo> priKeys,Map<String, ColumnInfo> columns) {
		super();
		this.tname = tname;
		this.columns = columns;
		this.priKeys = priKeys;
	}
	
	
}

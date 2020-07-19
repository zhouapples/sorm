package com.simpleorm.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.simpleorm.bean.ColumnInfo;
import com.simpleorm.bean.JavaFieldGetSet;
import com.simpleorm.bean.TableInfo;
import com.simpleorm.core.DBManager;
import com.simpleorm.core.MySqlTypeConvertor;
import com.simpleorm.core.TableContext;
import com.simpleorm.core.TypeConvertor;


/**
 * 封装了file文件常用操作
 * @author MiKimouse
 *
 */
public class JavaFileUtils {
	
	/**
	 * java代码生成器
	 * 根据字段信息生成java属性信息,如 var username-->private String 颜色那么;以及相应的get,set方法
	 * @param column	字段信息
	 * @param convertor	类型转化器
	 * @return	java属性和set,get,方法
	 */
	public static JavaFieldGetSet createFieldGetSetSRC(ColumnInfo column,TypeConvertor convertor) {
		JavaFieldGetSet jfgs = new JavaFieldGetSet();
		String javaFieldType = convertor.databaseType2JavaType(column.getDataType());
		jfgs.setFieldInfo("\tprivate "+javaFieldType+" "+column.getName()+";\n");	//根据字段名生成java属性代码 private........;
		
		//生成get方法,参考此段public String getUsername(){return username;}
		StringBuilder getSrc = new StringBuilder(); 
		getSrc.append("\tpublic "+javaFieldType+" get"+StringUtils.firstChar2Upper(column.getName())+"(){\n");
		getSrc.append("\t\treturn "+column.getName()+";\n");
		getSrc.append("\t}\n");
		jfgs.setGetInfo(getSrc.toString());
		
		//生成set方法,参考此段public void setUsername(String){this.username=username;}
		StringBuilder setSrc = new StringBuilder(); 
		setSrc.append("\tpublic void set"+StringUtils.firstChar2Upper(column.getName())+"(");
		setSrc.append(javaFieldType+" "+column.getName()+"){\n");
		setSrc.append("\t\tthis."+column.getName()+"="+column.getName()+";\n");
		setSrc.append("\t}\n");
		jfgs.setSetInfo(setSrc.toString());
		return jfgs;
	}
	
	/**
	 * 根据表信息生成java类的源码
	 * @param tableInfo	数据库表信息
	 * @param convertor	数据类型转化器
	 * @return	java源代码
	 */
	public static String createJavaSrc(TableInfo tableInfo,TypeConvertor convertor) {
		Map<String,ColumnInfo> columns = tableInfo.getColumns();
		List<JavaFieldGetSet> javaFields = new ArrayList<JavaFieldGetSet>();
		
		for (ColumnInfo c : columns.values()) {
			javaFields.add(createFieldGetSetSRC(c, convertor));
		}
		
		StringBuilder src = new StringBuilder();	//StringBuilder可变长字符串
		
		//生成package语句,参考 package com.simpleorm.utils;
		src.append("package "+DBManager.getConf().getPoPackage()+";\n\n");
		//生成import语句
		src.append("import java.util.*; \n");
		src.append("import java.sql.*; \n\n\n");
		//生成类声明语句 public class xxx{}
		src.append("public class "+StringUtils.firstChar2Upper(tableInfo.getTname())+" {\n\n");
		//生成属性列表
		for(JavaFieldGetSet f:javaFields) {
			src.append(f.getFieldInfo());
		}
		src.append("\n\n");	
		//生成get方法
		for(JavaFieldGetSet f:javaFields) {
			src.append(f.getGetInfo());
		}
		src.append("\n\n");
		//生成set方法
		for(JavaFieldGetSet f:javaFields) {
			src.append(f.getSetInfo());
		}
		src.append("\n\n");
		//生成类结束
		src.append("}\n");
		System.out.println(src);
		
		return src.toString();
	}
	
	
	
	public static void main(String[] args) {
		
//		ColumnInfo ci = new ColumnInfo("id","int","0");
//		JavaFieldGetSet f = createFieldGetSetSRC(ci, new MySqlTypeConvertor());
//		System.out.println(f);
		
		Map<String,TableInfo> map = TableContext.tables;
		TableInfo t = map.get("emp");
		
		createJavaSrc(t, new MySqlTypeConvertor());
	}
}

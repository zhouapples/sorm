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
 * ��װ��file�ļ����ò���
 * @author MiKimouse
 *
 */
public class JavaFileUtils {
	
	/**
	 * java����������
	 * �����ֶ���Ϣ����java������Ϣ,�� var username-->private String ��ɫ��ô;�Լ���Ӧ��get,set����
	 * @param column	�ֶ���Ϣ
	 * @param convertor	����ת����
	 * @return	java���Ժ�set,get,����
	 */
	public static JavaFieldGetSet createFieldGetSetSRC(ColumnInfo column,TypeConvertor convertor) {
		JavaFieldGetSet jfgs = new JavaFieldGetSet();
		String javaFieldType = convertor.databaseType2JavaType(column.getDataType());
		jfgs.setFieldInfo("\tprivate "+javaFieldType+" "+column.getName()+";\n");	//�����ֶ�������java���Դ��� private........;
		
		//����get����,�ο��˶�public String getUsername(){return username;}
		StringBuilder getSrc = new StringBuilder(); 
		getSrc.append("\tpublic "+javaFieldType+" get"+StringUtils.firstChar2Upper(column.getName())+"(){\n");
		getSrc.append("\t\treturn "+column.getName()+";\n");
		getSrc.append("\t}\n");
		jfgs.setGetInfo(getSrc.toString());
		
		//����set����,�ο��˶�public void setUsername(String){this.username=username;}
		StringBuilder setSrc = new StringBuilder(); 
		setSrc.append("\tpublic void set"+StringUtils.firstChar2Upper(column.getName())+"(");
		setSrc.append(javaFieldType+" "+column.getName()+"){\n");
		setSrc.append("\t\tthis."+column.getName()+"="+column.getName()+";\n");
		setSrc.append("\t}\n");
		jfgs.setSetInfo(setSrc.toString());
		return jfgs;
	}
	
	/**
	 * ���ݱ���Ϣ����java���Դ��
	 * @param tableInfo	���ݿ����Ϣ
	 * @param convertor	��������ת����
	 * @return	javaԴ����
	 */
	public static String createJavaSrc(TableInfo tableInfo,TypeConvertor convertor) {
		Map<String,ColumnInfo> columns = tableInfo.getColumns();
		List<JavaFieldGetSet> javaFields = new ArrayList<JavaFieldGetSet>();
		
		for (ColumnInfo c : columns.values()) {
			javaFields.add(createFieldGetSetSRC(c, convertor));
		}
		
		StringBuilder src = new StringBuilder();	//StringBuilder�ɱ䳤�ַ���
		
		//����package���,�ο� package com.simpleorm.utils;
		src.append("package "+DBManager.getConf().getPoPackage()+";\n\n");
		//����import���
		src.append("import java.util.*; \n");
		src.append("import java.sql.*; \n\n\n");
		//������������� public class xxx{}
		src.append("public class "+StringUtils.firstChar2Upper(tableInfo.getTname())+" {\n\n");
		//���������б�
		for(JavaFieldGetSet f:javaFields) {
			src.append(f.getFieldInfo());
		}
		src.append("\n\n");	
		//����get����
		for(JavaFieldGetSet f:javaFields) {
			src.append(f.getGetInfo());
		}
		src.append("\n\n");
		//����set����
		for(JavaFieldGetSet f:javaFields) {
			src.append(f.getSetInfo());
		}
		src.append("\n\n");
		//���������
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

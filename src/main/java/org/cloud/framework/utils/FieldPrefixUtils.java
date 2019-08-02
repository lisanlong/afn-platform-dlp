package org.cloud.framework.utils;

/**
 * solr动态字段前缀
 * 
 * @author vimes
 * 
 */
public class FieldPrefixUtils {

	/**
	 ** 类型 auto_string/string/long/int/float/double/date
	 * 是否多值 multiValued 是=1 否=0
	 *
	 * @param dataType
	 * @param multiValued
	 * @return
	 */
	public static String getDynamicField(String dataType, String multiValued) {
		String fieldStr = dataType ;

		//默认字段都存储
		if ("auto_string".equals(dataType)) {
			fieldStr += "IT";
		} else {
			fieldStr += "I";
		}

		//是否多值
		if ("1".equals(multiValued)) {
			fieldStr += "M";
		}

		//默认所有字段都构建索引
		fieldStr += "S_";

		return fieldStr;

	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String fieldStr = FieldPrefixUtils.getDynamicField("double","1");
		System.out.println(fieldStr);
	}



}

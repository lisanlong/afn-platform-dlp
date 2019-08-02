package org.cloud.framework.utils;

import java.util.regex.Pattern;

/**
 * 2016-9-21 16:17:50
 * 
 * @author vimes
 * 
 */
public class CodeUtils {

	public static boolean isNumeric(String str) {
		try {
			if (null == str || "".equals(str) || "null".equals(str)
					|| " ".equals(str)) {
				return false;
			}
			Pattern pattern = Pattern.compile("[0-9]*");
			return pattern.matcher(str).matches();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}
	 
}

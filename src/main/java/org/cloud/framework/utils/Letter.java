package org.cloud.framework.utils;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class Letter {
	public static String getLetter(Integer s) {
		HashMap<Integer, String> dictItemParams = new LinkedHashMap<Integer, String>();
		dictItemParams.put(1, "A");
		dictItemParams.put(2, "B");
		dictItemParams.put(3, "C");
		dictItemParams.put(4, "D");
		dictItemParams.put(5, "E");
		dictItemParams.put(6, "F");
		dictItemParams.put(7, "G");
		dictItemParams.put(8, "H");
		dictItemParams.put(9, "I");
		dictItemParams.put(10, "J");
		dictItemParams.put(11, "K");
		dictItemParams.put(12, "L");
		dictItemParams.put(13, "M");
		dictItemParams.put(14, "N");
		dictItemParams.put(15, "O");
		dictItemParams.put(16, "P");
		dictItemParams.put(17, "Q");
		dictItemParams.put(18, "R");
		dictItemParams.put(19, "S");
		dictItemParams.put(20, "T");
		dictItemParams.put(21, "U");
		dictItemParams.put(22, "V");
		dictItemParams.put(23, "W");
		dictItemParams.put(24, "X");
		dictItemParams.put(25, "Y");
		dictItemParams.put(26, "Z");
		return dictItemParams.get(s);
	}
}

package org.cloud.framework.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @Author wgsh
 * @Date wgshb on 2019/7/10 14:40
 */
public class NumberUtils {

	/**
	 * 返回两个数字的百分比
	 * @param num1
	 * @param num2
	 * @param position 保留几位小数
	 * @return
	 */
	public static String getRatePercent(int num1, int num2, int position) {
		if(0 == num2) {
			return "0";
		}
		NumberFormat numberFormat = NumberFormat.getInstance();
		numberFormat.setMaximumFractionDigits(position);
		String result = numberFormat.format((float) num1 / (float) num2 * 100);
		return result;
	}

	/**
	 * 格式化文件的大小
	 * @param fileS
	 * @return
	 */
	public static String formatFileSize(long fileS) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		String wrongSize = "0B";
		if (fileS == 0) {
			return wrongSize;
		}
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "KB";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "MB";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "GB";
		}
		return fileSizeString;
	}

	public static void main(String[] args) {
		System.out.println(formatFileSize(1323323232));
	}

}

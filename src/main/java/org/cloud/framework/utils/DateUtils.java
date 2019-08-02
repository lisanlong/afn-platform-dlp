package org.cloud.framework.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 日期格式转换
 *
 * @author Administrator
 *
 */
public class DateUtils {

	/**
	 * 转换为时间（天,时:分:秒.毫秒）
	 * @param timeMillis
	 * @return
	 */
	public static String formatDateTime(long timeMillis){
		long day = timeMillis/(24*60*60*1000);
		long hour = (timeMillis/(60*60*1000)-day*24);
		long min = ((timeMillis/(60*1000))-day*24*60-hour*60);
		long s = (timeMillis/1000-day*24*60*60-hour*60*60-min*60);
		long sss = (timeMillis-day*24*60*60*1000-hour*60*60*1000-min*60*1000-s*1000);
		return (day>0?day+",":"")+hour+":"+min+":"+s+"."+sss;
	}

	public static Date getDate() {
		Date dateU = new Date();
		java.sql.Date date = new java.sql.Date(dateU.getTime());
		return date;
	}

	// 把字符串转为日期
	/**
	 * 支持以下格式
	 * 2018
	 * 201801
	 * 20180101
	 * 2018-01-01
	 * 2018-01
	 * 2018年
	 * 2018年1月
	 * 2018年01月
	 * 2018年1月2日
	 * 2018年01月02日
	 * 2018-01-01 00:00:00
	 * @param strDate
	 * @return
	 */
	public static Date ConverToDate(String strDate) {
		if(isNullOrNone(strDate)){
			return null;
		}
		String t = isFormatTime(strDate);
		String time = "";
		if(t != null){
			time = t;
		}else{
			String d = isFormatDay(strDate);
			if(d != null){
				time = d + " 00:00:00";
			}else{
				String m = isFormatMonth(strDate);
				if(m != null){
					time = m + "-01 00:00:00";
				}else{
					String y = isFormatYear(strDate);
					if(y != null){
						time = y + "-01-01 00:00:00";
					}
				}
			}
		}
		if(!"".equals(time)){
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				return df.parse(time);
			} catch (ParseException e) {
				return null;
			}
		}
		return null;
	}
	public static Date ConverToDate2(String strDate) {
		if (strDate == null || "".equals(strDate.trim()))
			return null;
		if (strDate.length() == 10)
			strDate = strDate + " 00:00:00";
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return df.parse(strDate);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getDate(String date, String format) {
		try {
			if (format == null)
				format = "yyyy-MM-dd HH:mm:ss";
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.format((Date) sdf.parse(date));
		} catch (Exception e) {
			return date;
		}

	}

	public static String getCutStr(String date) {
		return null;
	}

	public static String getDateTime() {
		SimpleDateFormat sdf;
		Date date = null;
		Calendar myDate = Calendar.getInstance();
		myDate.setTime(new Date());
		date = myDate.getTime();
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	public static String getDateNoStyle() {
		SimpleDateFormat sdf;
		Date date = null;
		Calendar myDate = Calendar.getInstance();
		myDate.setTime(new Date());
		date = myDate.getTime();
		sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(date);
	}

	public static String formatDate(Date date, String format) {
		if (format == null)
			format = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	/**********************start*********************************/
	/**
	 * 判断是否为空或null
	 *
	 * @param src
	 * @return boolean
	 */
	public static boolean isNullOrNone(String src) {
		if (null == src || "".equals(src)) {
			return true;
		}
		return false;
	}
	/**
	 * 2018年
	 * 2018
	 */
	public static String isFormatYear(String day) {
		if(isNullOrNone(day)){
			return null;
		}
		day = day.replace("年", "");
		boolean f = day.matches("([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})");
		if(f){
			String y = getMatcher(day,"([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})");
			return y;
		}
		return null;
	}
	/**
	 * 201801
	 * 2018年01月
	 * 2018-01
	 * 2018-1
	 */
	public static String isFormatMonth(String day) {
		if(isNullOrNone(day)){
			return null;
		}
		day = day.replace("年", "-").replace("月", "");
		boolean f = day.matches("([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(0{0,1}[123456789]|1[02])");
		String mon = "";
		String d = "";
		if(f){//中间有-
			mon = getMatcher(day,"([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(0{0,1}[123456789]|1[02])");
			d = mon.substring(5);
			if(d.length() == 1){
				d = "0" + d;
				mon = mon.substring(0,5) + d;
			}
			return mon;
		}else{//中间没-
			f = day.matches("([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})(0[123456789]|1[02])");
			if(f){
				mon = getMatcher(day,"([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})(0[123456789]|1[02])");
				d = mon.substring(0, 4) + "-" +mon.substring(4);
				return d;
			}
		}
		return null;
	}

	/**
	 * 20180101
	 * 2018-01-01
	 * 2018-1-1
	 * 2018年01月01日
	 * 2018年1月1日
	 */
	public static String isFormatDay(String day){
		if(isNullOrNone(day)){
			return null;
		}
		day = day.replace("年", "-").replace("月", "-").replace("日", "");
		String da = "";
		if(day.indexOf("-") != -1){//有-
			String[] split = day.split("-");
			if(split.length != 3){
				return null;
			}
			String y = split[0];
			String m = split[1];
			String d = split[2];
			if(y.length() != 4){
				return null;
			}
			if(m.length() == 1){
				m = "0" + m;
			}
			if(d.length() == 1){
				d = "0" + d;
			}
			day = y + "-" + m + "-" + d;
			boolean f = day.matches("((?!0000)[0-9]{4}-((0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-8])|(0[13-9]|1[0-2])-(29|30)|(0[13578]|1[02])-31)|([0-9]{2}(0[48]|[2468][048]|[13579][26])|(0[48]|[2468][048]|[13579][26])00)-02-29)");
			if(f){
				da = getMatcher(day,"((?!0000)[0-9]{4}-((0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-8])|(0[13-9]|1[0-2])-(29|30)|(0[13578]|1[02])-31)|([0-9]{2}(0[48]|[2468][048]|[13579][26])|(0[48]|[2468][048]|[13579][26])00)-02-29)");
				return da;
			}
		}else{//没-
			if(day.length() == 8){
				day = day.substring(0,4) + "-" + day.substring(4, 6) + "-" + day.substring(6);
				boolean f = day.matches("((?!0000)[0-9]{4}-((0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-8])|(0[13-9]|1[0-2])-(29|30)|(0[13578]|1[02])-31)|([0-9]{2}(0[48]|[2468][048]|[13579][26])|(0[48]|[2468][048]|[13579][26])00)-02-29)");
				if(f){
					da = getMatcher(day,"((?!0000)[0-9]{4}-((0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-8])|(0[13-9]|1[0-2])-(29|30)|(0[13578]|1[02])-31)|([0-9]{2}(0[48]|[2468][048]|[13579][26])|(0[48]|[2468][048]|[13579][26])00)-02-29)");
					return da;
				}
			}
		}
		return null;
	}
	/**
	 * 20180101 00:00:00
	 * 2018-01-01 00:00:00
	 * 2018-1-1 00:00:00
	 * 2018年01月01日 00:00:00.0
	 * 2018年1月1日 00:00:00.000
	 */
	public static String isFormatTime(String day){
		if(isNullOrNone(day)){
			return null;
		}
		day = day.replace("年", "-").replace("月", "-").replace("日", "");

		String[] dt = day.split("\\s+");
		if(dt.length==2){
			String d = dt[0];
			String t = dt[1];
			String ymd = isFormatDay(d);
			if(ymd == null){
				return null;
			}
			if(t.indexOf(".") != -1){
				t = t.substring(0,t.indexOf("."));
				boolean f = t.matches("(([0-1][0-9])|2[0-3]):[0-5][0-9]:[0-5][0-9]");
				if(f){
					t = getMatcher(t,"(([0-1][0-9])|2[0-3]):[0-5][0-9]:[0-5][0-9]");
					return ymd + " " + t;
				}
			}else{
				boolean f = t.matches("(([0-1][0-9])|2[0-3]):[0-5][0-9]:[0-5][0-9]");
				if(f){
					t = getMatcher(t,"(([0-1][0-9])|2[0-3]):[0-5][0-9]:[0-5][0-9]");
					return ymd + " " + t;
				}
			}
		}
		return null;
	}
	/*正则表达式 匹配字符串*/
	public static String getMatcher(String source,String regex) {
		String result = "";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(source);
		while (matcher.find()) {
			result = matcher.group();
		}
		return result;
	}

	/**
	 * 将常见日期 yyyy-MM-dd 转化为 MM.dd
	 */
	public static String getFormatDate(String day) {
		String[] dayArray = day.split("-");
		StringBuilder sb = new StringBuilder();
		sb.append(dayArray[1].startsWith("0")?dayArray[1].substring(1,2):dayArray[1]);
		sb.append(".");
		sb.append(dayArray[2].startsWith("0")?dayArray[2].substring(1,2):dayArray[2]);
		return sb.toString();
	}

	/**********************end*********************************/

	public static void main(String[] args) {

		// Calendar myDate = Calendar.getInstance();
		// myDate.setTime(new Date());
		// for (int i = myDate.get(Calendar.YEAR) + 1; i > 1990; i--) {
		// System.out.println(i);
		//
		// }

		Date date = ConverToDate("2017-01-01 22:22:22");
		String r = formatDate(date, null);
		System.out.println(r);

	}

}

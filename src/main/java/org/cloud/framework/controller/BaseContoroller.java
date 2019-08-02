package org.cloud.framework.controller;

import net.sf.json.JSONArray;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 基础控制层
 * 
 * @author vimes
 * 
 */
public class BaseContoroller {

	protected void pringWriterToPage(String content,
			HttpServletResponse response) throws Exception {
		this.pringWriterToPage(content, null, response);
	}

	protected void pringWriterToPage(String content, String contentType,
			HttpServletResponse response) throws Exception {
		// "application/json"
		// ".txt"="text/plain"
		// "text/xml"
		if (contentType != null && !"".equals(contentType)) {
			response.setContentType(contentType);
		} else {
			// response.setContentType("text/plain");
		}
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		pw.print(content);
		pw.flush();
		pw.close();
	}

	protected String formatToJson(Object obj) {
		JSONArray jsonObject = JSONArray.fromObject(obj);
		try {
			// System.out.println(jsonObject.toString());
			return jsonObject.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 获取访问者IP
	 * 
	 * 在一般情况下使用Request.getRemoteAddr()即可，但是经过nginx等反向代理软件后，这个方法会失效。
	 * 
	 * 本方法先从Header中获取X-Real-IP，如果不存在再从X-Forwarded-For获得第一个IP(用,分割)，
	 * 如果还不存在则调用Request .getRemoteAddr()。
	 * 
	 * @param request
	 * @return
	 */
	public String getIpAddr(HttpServletRequest request) throws Exception {
		String ip = request.getHeader("X-Real-IP");
		if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			return ip;
		}
		ip = request.getHeader("X-Forwarded-For");
		if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			// 多次反向代理后会有多个IP值，第一个为真实IP。
			int index = ip.indexOf(',');
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		} else {
			return request.getRemoteAddr();
		}
	}
	
	/**
	 * 根据名字获取cookie
	 * @param request
	 * @param name cookie名字
	 * @return
	 */
	public static Cookie getCookieByName(HttpServletRequest request,String name){
	    Map<String,Cookie> cookieMap = ReadCookieMap(request);
	    if(cookieMap.containsKey(name)){
	        Cookie cookie = (Cookie)cookieMap.get(name);
	        return cookie;
	    }else{
	        return null;
	    }   
	}
	
	/**
	 * 将cookie封装到Map里面
	 * @param request
	 * @return
	 */
	private static Map<String,Cookie> ReadCookieMap(HttpServletRequest request){  
	    Map<String,Cookie> cookieMap = new HashMap<String,Cookie>();
	    Cookie[] cookies = request.getCookies();
	    if(null!=cookies){
	        for(Cookie cookie : cookies){
	            cookieMap.put(cookie.getName(), cookie);
	        }
	    }
	    return cookieMap;
	}

	/**
	 * 获取近7天的日期的集合
	 */
	protected List<String> getLastWeekList() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		List<String> weekList =  new ArrayList<>();
		int start = -6;
		while(start < 1) {
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.DATE, start);
			Date d = c.getTime();
			String day = format.format(d);
			/*String[] dayArray = day.split("-");
			StringBuilder sb = new StringBuilder();
			sb.append(dayArray[1].startsWith("0")?dayArray[1].substring(1,2):dayArray[1]);
			sb.append(".");
			sb.append(dayArray[2].startsWith("0")?dayArray[2].substring(1,2):dayArray[2]);
			weekList.add(sb.toString());*/
			weekList.add(day);
			start ++;
		}
		return weekList;
	}

	/**
	 * 获取今天的日期
	 */
	protected List<String> getTodayList() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		List<String> weekList =  new ArrayList<>();
		weekList.add(format.format(new Date()));
		return weekList;
	}
 
}

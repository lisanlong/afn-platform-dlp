package org.cloud.framework.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * String and Unicode convert each other.
 * @author    yzh.lin
 * @since    2012-08-09
 * 
 */
public class UnicodeConverter {

    private static final Pattern REG_UNICODE = Pattern.compile("[0-9A-Fa-f]{4}");  
    private static final Pattern EN_CODE = Pattern.compile("[A-Za-z]{4}");  

    public static String unicode2String(String str) { 
    	if(str == null){
    		return null;
    	}
        StringBuilder sb = new StringBuilder();  
        int len = str.length();  
        for (int i = 0; i < len; i++) {  
            char c1 = str.charAt(i);  
            if (c1 == '\\' && i < len - 1) {  
                char c2 = str.charAt(++i);  
                if (c2 == 'u' && i <= len - 5) {  
                    String tmp = str.substring(i + 1, i + 5);  
                    Matcher matcher = REG_UNICODE.matcher(tmp);  
                    if (matcher.find()) {  
                        sb.append((char) Integer.parseInt(tmp, 16));  
                        i = i + 4;  
                    } else {  
                        sb.append(c1).append(c2);  
                    }  
                } else {  
                    sb.append(c1).append(c2);  
                }  
            } else {  
                sb.append(c1);  
            }  
        }  
        return sb.toString();  
    } 

    /**
     * Convert the whole String object.
     * @param str
     * @return
     */
    public static String string2Unicode(String str)
    {
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<str.length();i++)
        {
            String tmpStr = Integer.toHexString(str.charAt(i));
            if(tmpStr.length() < 4){
                sb.append("\\u00");
            }else{
                sb.append("\\u");
            }
            sb.append(tmpStr);
        }
        return sb.toString();
    }
    /**
     * Just convert Chinese String
     * @param str
     * @return
     */
    public static String chinese2Unicode(String str){
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<str.length();i++)
        {
            String tmpStr = Integer.toHexString(str.charAt(i));

            if(tmpStr.length() < 4){
                sb.append(str.charAt(i));
            }else{
                sb.append("\\u");
                sb.append(tmpStr);
            }
        }
        return sb.toString();
    }
    public static String unicodeToString(String str) {  
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");  
        Matcher matcher = pattern.matcher(str);  
        char ch;  
        while (matcher.find()) {  
            //group 6728  
            String group = matcher.group(2);  
            //ch:'木' 26408  
            ch = (char) Integer.parseInt(group, 16);  
            //group1 \u6728  
            String group1 = matcher.group(1);  
            str = str.replace(group1, ch + "");  
        }  
        return str;  
    } 
    
    //High-half zone of UTF-16
    //Low-half zone of UTF-16  
    public static String filterUnicode(String str) {
    	if(str == null){
    		return null;
    	}
    	 StringBuilder sb=new StringBuilder();
    	 int  tempa =  Integer.parseInt("D800", 16);
    	 int  tempb =  Integer.parseInt("DFFF", 16);
    	 for (int i = 0; i < str.length(); i++) {
    		String tmpStr = Integer.toHexString(str.charAt(i));
    		int ia = Integer.parseInt(tmpStr, 16);
    		if(ia >= tempa && ia <= tempb){
    			  sb.append("\\u");
                  sb.append(tmpStr);
    		}else{
    			sb.append(str.charAt(i));
    		}
    		
		}
    	return sb.toString();  
    }
	public static void main(String[] args) {
		long s = System.currentTimeMillis();
		String unicode2String = unicode2String("fjdsalf1231接口了放大镜看了大夫山的结束啦看风景\\usdaf\ud835\udc5b \ud835\udc5f \ud835\udc5f\u2264\ud835\udc5b \ud835\udefe(\ud835\udc5b,\ud835\udc5f)[\ud835\udf02(\ud835\udc5b,\ud835\udc5f"
				+ "\ud835\udc5b \ud835\udc5f \ud835\udc5f\u2264\ud835\udc5b \ud835\udefe(\ud835\udc5b,\ud835\udc5f)[\ud835\udf02(\ud835\udc5b,\ud835\udc5f");
		long d = System.currentTimeMillis();
		System.out.println(unicode2String);
		System.out.println(d-s);
	}
}
package org.cloud.framework.utils;

import java.util.HashMap;

public class UserLiteratureUtils {

    private static String KEY = "NINEMAX_LITERATURES_OPERATION";
    private static HashMap<String, String> userLiteratures = new HashMap<String,String>();

    public static String setUserLiteratures(String username ,String value){
        String realKey = MD5.convert(username+"|"+KEY);
        userLiteratures.put(realKey,value);
        return realKey;
    }
    public static String getUrl(String key) {
        String resultKey = userLiteratures.get(key);
        return resultKey;
    }

    public static String getUserLiteratures(String username) {
        String realKey = MD5.convert(username+"|"+KEY);
        String resultKey = userLiteratures.get(realKey);
        return resultKey;
    }

}


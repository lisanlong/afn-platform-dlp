package org.cloud.framework.utils;


import java.util.List;

public class CollectionUtils {

    public static String list2Str(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for(String str : list) {
            if(list.indexOf(str) != list.size() - 1) {
                sb.append(str.trim() + ",");
            } else {
                sb.append(str);
            }
        }
        return sb.toString();
    }

}

package org.cloud.framework.utils;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 参数封装工具类
 * 
 * @author anhuifeng
 * 
 */
public class MybatisPlusTools<T> {

	@SuppressWarnings("rawtypes")
	public QueryWrapper<T> getEwByCount(HashMap<String, Object> queryParams) {
		QueryWrapper<T> ew = new QueryWrapper<T>();
		if (queryParams != null) {

			Iterator iter = queryParams.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String key = entry.getKey().toString();
				String val = entry.getValue().toString();
				String sign = key.split(",")[0];
				String column = key.split(",")[1];
				if (sign.equals("=")) {
					ew.eq(column, val);
				} else if (sign.equals("%")) {
					ew.like(column, val);
				} else if (sign.equals(">")) {
					ew.gt(column, val);
				} else if (sign.equals("<")) {
					ew.lt(column, val);
				} else if (sign.equals(">=")) {
					ew.ge(column, val);
				} else if (sign.equals("<=")) {
					ew.le(column, val);
				} else if (sign.equals("!")) {
					ew.ne(column, val);
				} else if (sign.equals("!%")) {
					ew.notLike(column, val);
				}
			}
		}
		return ew;
	}

	@SuppressWarnings("rawtypes")
	public QueryWrapper<T> getEwByList(HashMap<String, Object> queryParams,int pageNum) {
		QueryWrapper<T> ew = new QueryWrapper<T>();
		ew.eq(pageNum+"",pageNum+"");
		if (queryParams != null) {
			Iterator iter = queryParams.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String key = entry.getKey().toString();
				String val = entry.getValue().toString();
				String sign = key.split(",")[0];
				String column = key.split(",")[1];
				if (sign.equals("=")) {
					ew.eq(column, val);
				} else if (sign.equals("%")) {
					ew.like(column, val);
				} else if (sign.equals(">")) {
					ew.gt(column, val);
				} else if (sign.equals("<")) {
					ew.lt(column, val);
				} else if (sign.equals(">=")) {
					ew.ge(column, val);
				} else if (sign.equals("<=")) {
					ew.le(column, val);
				} else if (sign.equals("!")) {
					ew.ne(column, val);
				} else if (sign.equals("!%")) {
					ew.notLike(column, val);
				} else if (sign.equals("^")) {
					if ("asc".equals(val)) {
						ew.orderByAsc(column);
					}
					ew.orderByDesc(column);
				}
			}
		}
		return ew;
	}

}

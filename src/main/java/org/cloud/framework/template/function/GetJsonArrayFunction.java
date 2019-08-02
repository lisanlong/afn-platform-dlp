package org.cloud.framework.template.function;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.beetl.core.Context;
import org.beetl.core.Function;

/**
 * 获得字段信息（参数：项目英文名称+资源库英文名称+字段英文名称）
 * 调用方法： 
 */
public class GetJsonArrayFunction implements Function {
    @Override
    public Object call(Object[] paras, Context ctx) {
        JSONArray obj = null;
        if(paras[0]!=null &&!"".equals(paras[0].toString())){
            obj = JSONArray.fromObject(paras[0]);
        }
        return obj;
    }
}

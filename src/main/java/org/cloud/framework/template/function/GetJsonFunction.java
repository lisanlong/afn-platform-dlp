package org.cloud.framework.template.function;

import net.sf.json.JSONObject;
import org.beetl.core.Context;
import org.beetl.core.Function;

/**
 * 获得字段信息（参数：项目英文名称+资源库英文名称+字段英文名称）
 * 调用方法：
 */
public class GetJsonFunction implements Function {
    @Override
    public Object call(Object[] paras, Context ctx) {
        JSONObject obj = null;
        if(paras[0]!=null &&!"".equals(paras[0].toString())){
            obj = JSONObject.fromObject(paras[0]);
        }
        return obj;
    }
}

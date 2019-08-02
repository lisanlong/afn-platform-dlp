package org.cloud.framework.template.tag;

import org.beetl.core.GeneralVarTagBinding;
import org.cloud.framework.SpringUtil;
import org.cloud.framework.model.SystemUser;
import org.cloud.framework.protocol.ISearchDataReadService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 获得公共字段列表
 * * 示例： 参数：projectEn=""
 * 	 <#_login_list  var="obj">
        ${obj.username}
     </#_login_list>
 */

@Service
@Scope("prototype")
public class LoginListTag extends GeneralVarTagBinding {


    ISearchDataReadService searchDataReadService = (ISearchDataReadService) SpringUtil.getBean("searchDataReadService");

    public static HttpServletRequest getHttpServletRequest(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes())
                .getRequest();
        return request;
    }
    @Override
    public void render() {
        HttpServletRequest request = getHttpServletRequest();
        Object userid=request.getSession().getAttribute("userid");
        SystemUser aSystemUser = new SystemUser();
        if(userid!=null && !"".equals(userid)) {
            aSystemUser = searchDataReadService.findSystemUser(Integer.parseInt(userid.toString()));
        }
        this.binds(aSystemUser);
        this.doBodyRender();
    }

}

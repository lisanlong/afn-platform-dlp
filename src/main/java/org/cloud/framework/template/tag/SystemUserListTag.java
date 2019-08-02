package org.cloud.framework.template.tag;

import org.beetl.core.GeneralVarTagBinding;
import org.cloud.framework.SpringUtil;
import org.cloud.framework.model.SystemUser;
import org.cloud.framework.protocol.ISearchDataReadService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 获得用户列表
 * * 示例： 参数：projectEn=""
 * 	 <#_system_user_list  var="obj">
        ${obj.nameZh}/${obj.nameEn}
     </#_system_user_list>
 */

@Service
@Scope("prototype")
public class SystemUserListTag extends GeneralVarTagBinding {


    ISearchDataReadService searchDataReadService = (ISearchDataReadService) SpringUtil.getBean("searchDataReadService");

    @SuppressWarnings("rawtypes")
	@Override
    public void render() {

        // args[1] 则是标签的属性，参数是个map，key是html tag的属性，value是其属性值
        Map attrs = (Map) args[1];
        System.out.println("attrs=" + attrs);
        List<SystemUser> list = searchDataReadService.getSystemUser();
        for (SystemUser systemUser : list) {
            this.binds(systemUser);
            this.doBodyRender();
        }

    }

}

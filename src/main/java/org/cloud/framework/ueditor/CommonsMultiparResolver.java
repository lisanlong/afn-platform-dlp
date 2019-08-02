package org.cloud.framework.ueditor;


import org.springframework.web.multipart.commons.CommonsMultipartResolver;


public class CommonsMultiparResolver  extends CommonsMultipartResolver{
	  @Override    
      public boolean isMultipart(javax.servlet.http.HttpServletRequest request) {    
       String uri = request.getRequestURI();    
       //过滤使用百度UEditor的URI    
       if (uri.indexOf("ueditorSearch") > 0) {     //此处拦截路径即为上面编写的controller路径
    	   return false;    
       }    
       return super.isMultipart(request);    
      }    
}

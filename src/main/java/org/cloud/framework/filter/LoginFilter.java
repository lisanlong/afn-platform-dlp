package org.cloud.framework.filter;

import org.cloud.framework.model.SystemUser;
import org.cloud.framework.protocol.ISearchDataReadService;
import org.cloud.framework.utils.CookieUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;


public class LoginFilter implements HandlerInterceptor {
    @Autowired
    private ISearchDataReadService searchDataReadService;

    Logger logger = LoggerFactory.getLogger(LoginFilter.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("------preHandle------");
        String url = request.getServletPath();
        if (url.indexOf("/login") != -1 || url.indexOf("/demo") != -1 || url.indexOf("/swagger-ui.html") != -1) {
            return true;
        } else {
            Cookie cookie = CookieUtil.findCookie(request.getCookies(), "token");
            if (cookie != null) {
                String token = cookie.getValue();
                //System.out.println(token);
                if (token != null && !token.equals("")) {
                    String[] tokens = token.split("@#%#@");
                    if (tokens.length > 1) {
                        String username = tokens[0];
                        String password = tokens[1];
                        HashMap<String, Object> queryParams = new HashMap<String, Object>();
                        queryParams.put("=,username", username);
                        queryParams.put("=,password", password);
                        List<SystemUser> users = searchDataReadService.getSystemUser(queryParams, 1, 1);
                        if (users.size() > 0) {
                            request.getSession().setAttribute("userid", users.get(0).getId());
                            request.getSession().setAttribute("username", users.get(0).getUsername());
                            return true;
                        } else {
                            Cookie tokenCookie = new Cookie("token","");
                            tokenCookie.setMaxAge(0);// 立即销毁cookie
                            tokenCookie.setPath("/");
                            response.addCookie(tokenCookie);
                            request.getSession().setAttribute("userid", null);
                            request.getSession().setAttribute("username", null);
                            response.sendRedirect(request.getContextPath() + "/login/index.html");
                            return false;
                        }
                    } else {
                        response.sendRedirect(request.getContextPath() + "/login/index.html");
                        return false;
                    }
                } else {
                    response.sendRedirect(request.getContextPath() + "/login/index.html");
                    return false;
                }
            } else {
                if (request.getSession().getAttribute("userid") == null){
                    response.sendRedirect(request.getContextPath() + "/login/index.html");
                    return false;
                }else{
                    return true;
                }
            }
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

}

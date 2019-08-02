package org.cloud.framework.controller;

import org.cloud.framework.annotation.SystemControllerLog;
import org.cloud.framework.model.SystemUser;
import org.cloud.framework.protocol.ISearchDataReadService;
import org.cloud.framework.utils.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

@Controller
public class LoginController extends BaseContoroller{

    @Autowired
    private ISearchDataReadService searchDataReadService;

    @SystemControllerLog(description="登录")
    @RequestMapping("login")
    @ResponseBody
    public void login(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String username = request.getParameter("username");
        String password = MD5.convert(request.getParameter("password"));
        String rpassword = request.getParameter("rpassword");
        HashMap<String, Object> map = new HashMap<String, Object>();
        HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("=,username", username);
        queryParams.put("=,password", password);
        List<SystemUser> users = searchDataReadService.getSystemUser(queryParams,1,1);
        if (users.size() > 0) {
            if(users.get(0).getIsStoped()!=null && users.get(0).getIsStoped()==0){
                if(rpassword!=null && rpassword.equals("true")){
                    String token = users.get(0).getUsername()+"@#%#@"+users.get(0).getPassword();
                    Cookie tokenCookie = new Cookie("token",token);
                    tokenCookie.setMaxAge(60*60*24*15);//15天
                    response.addCookie(tokenCookie);
                }
                request.getSession().setAttribute("userid", users.get(0).getId());
                request.getSession().setAttribute("username", users.get(0).getUsername());
                map.put("tishi", "ok");
            }else{
                map.put("tishi", "stop");
            }
            map.put("userid", users.get(0).getId());
            map.put("username", users.get(0).getUsername());
        } else {
            map.put("tishi", "no");
        }
        String resultString = formatToJson(map);
        super.pringWriterToPage(resultString, "application/json", response);
    }

    @SystemControllerLog(description="注销")
    @RequestMapping("logout")
    @ResponseBody
    public void loginOut(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Cookie tokenCookie = new Cookie("token","");
        tokenCookie.setMaxAge(0);// 立即销毁cookie
        tokenCookie.setPath("/");
        response.addCookie(tokenCookie);
        request.getSession().setAttribute("userid", null);
        request.getSession().setAttribute("username", null);
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("tishi", "ok");
        String resultString = formatToJson(map);
        super.pringWriterToPage(resultString, "application/json", response);
    }
}
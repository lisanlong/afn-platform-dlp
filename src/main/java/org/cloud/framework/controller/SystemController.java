package org.cloud.framework.controller;

import org.cloud.framework.annotation.SystemControllerLog;
import org.cloud.framework.model.*;
import org.cloud.framework.protocol.ISearchDataReadService;
import org.cloud.framework.protocol.ISearchDataWriteService;
import org.cloud.framework.utils.DateUtils;
import org.cloud.framework.utils.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
public class SystemController extends BaseContoroller{

    @Autowired
    private ISearchDataReadService searchDataReadService;
    @Autowired
    private ISearchDataWriteService searchDataWriteService;

    @Value("${data.upload.file}")
    private String uploadFilePath;

    //获取用户列表
    @SystemControllerLog(description="获取用户列表")
    @RequestMapping("getSystemUserList")
    @ResponseBody
    public void getSystemUserList(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Integer pageNum = Integer.parseInt(request.getParameter("pageNum"));
        Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
        String queryParam = request.getParameter("queryParam");
        String searchText = request.getParameter("searchText");
        HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("^,create_time","asc");
        if(queryParam!=null && !queryParam.equals("")){
            if(searchText!=null && !searchText.equals("")){
                queryParams.put("%,"+queryParam,searchText);
            }
        }
        List<SystemUser> systemUserList = searchDataReadService.getSystemUser(queryParams,pageNum,pageSize);
        Integer count = searchDataReadService.getSystemUserCount(queryParams);
        List<HashMap<String, Object>> dataList = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> dataMap = null;
        for(SystemUser user:systemUserList){
            dataMap = new HashMap<String, Object>();
            dataMap.put("id", user.getId());
            dataMap.put("username", user.getUsername());
            dataMap.put("nickname", user.getNickname());
            dataMap.put("isStoped", user.getIsStoped());
            dataMap.put("telephone", user.getTelephone());
            dataMap.put("email", user.getEmail());
            String department = "";
            HashMap<String, Object> queryParamd = new HashMap<String, Object>();
            queryParamd.put("=,id",user.getDptId());
            List<DlpDepartment> dlpDepartmentList = searchDataReadService.getDlpDepartment(queryParamd,1,1);
            if(dlpDepartmentList.size()>0){
                department = dlpDepartmentList.get(0).getName();
            }
            dataMap.put("department", department);
            dataMap.put("createTime", user.getCreateTime());
            dataList.add(dataMap);
        }
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("data",dataList);
        data.put("count",(count + pageSize - 1) / pageSize);
        String resultString = formatToJson(data);
        super.pringWriterToPage(resultString, "application/json", response);
    }
    //获取用户
    @SystemControllerLog(description="获取用户")
    @RequestMapping("getSystemUser")
    @ResponseBody
    public void getClassify(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String id = request.getParameter("id");
        SystemUser systemUser = new SystemUser();
        if(id!=null && !id.equals("")){
            systemUser = searchDataReadService.findSystemUser(Integer.parseInt(id));
        }
        String resultString = formatToJson(systemUser);
        super.pringWriterToPage(resultString, "application/json", response);
    }
    //新增或修改用户
    @SystemControllerLog(description="新增或修改用户")
    @RequestMapping("addOrEditSystemUser")
    @ResponseBody
    public void addOrEditSystemUser(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String id = request.getParameter("id");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String nickname = request.getParameter("nickname");
        String telephone = request.getParameter("telephone");
        String email = request.getParameter("email");
        String depId = request.getParameter("depId");
        String isStoped = request.getParameter("isStoped");
        HashMap<String, Object> map = new HashMap<String, Object>();
        if(id!=null && id.equals("0")){
            SystemUser systemUser = new SystemUser();
            if(username!=null && !username.equals("")){
                HashMap<String, Object> queryParams = new HashMap<String, Object>();
                queryParams.put("=,username",username);
                List<SystemUser> systemUserList = searchDataReadService.getSystemUser(queryParams,1,1);
                if(systemUserList.size()<1){
                    systemUser.setUsername(username);
                    if(password!=null && !password.equals("")){
                        systemUser.setPassword(MD5.convert(password));
                    }
                    if(nickname!=null && !nickname.equals("")){
                        systemUser.setNickname(nickname);
                    }
                    if(telephone!=null && !telephone.equals("")){
                        systemUser.setTelephone(telephone);
                    }
                    if(email!=null && !email.equals("")){
                        systemUser.setEmail(email);
                    }
                    if(isStoped!=null && !isStoped.equals("")){
                        systemUser.setIsStoped(Integer.parseInt(isStoped));
                    }
                    if(depId!=null && !depId.equals("")){
                        systemUser.setDptId(Integer.parseInt(depId));
                    }
                    systemUser.setCreateTime(DateUtils.getDateTime());
                    try {
                        searchDataWriteService.saveSystemUser(systemUser);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    map.put("type", "false");
                }
            }

        }else{
            SystemUser systemUser = searchDataReadService.findSystemUser(Integer.parseInt(id));
            if(username!=null && !username.equals("")){
                HashMap<String, Object> queryParams = new HashMap<String, Object>();
                queryParams.put("=,username",username);
                List<SystemUser> systemUserList = searchDataReadService.getSystemUser(queryParams,1,1);
                if(username.equals(systemUser.getUsername()) || systemUserList.size()<1){
                    systemUser.setUsername(username);
                    if(password!=null && !password.equals("")){
                        systemUser.setPassword(MD5.convert(password));
                    }
                    if(nickname!=null && !nickname.equals("")){
                        systemUser.setNickname(nickname);
                    }
                    if(telephone!=null && !telephone.equals("")){
                        systemUser.setTelephone(telephone);
                    }
                    if(email!=null && !email.equals("")){
                        systemUser.setEmail(email);
                    }
                    if(isStoped!=null && !isStoped.equals("")){
                        systemUser.setIsStoped(Integer.parseInt(isStoped));
                    }
                    if(depId!=null && !depId.equals("")){
                        systemUser.setDptId(Integer.parseInt(depId));
                    }
                    try {
                        searchDataWriteService.editSystemUser(systemUser);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    map.put("type", "false");
                }
            }

        }
        String resultString = formatToJson(map);
        super.pringWriterToPage(resultString, "application/json", response);
    }
    //删除用户
    @SystemControllerLog(description="删除用户")
    @RequestMapping("delSystemUser")
    @ResponseBody
    public void delSystemUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String ids = request.getParameter("id");
        String[] idstr = ids.split(",");
        for(String id:idstr){
            SystemUser systemUser = searchDataReadService.findSystemUser(Integer.parseInt(id));
            searchDataWriteService.deleteSystemUser(systemUser);
        }
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("type", "true");
        String resultString = formatToJson(map);
        super.pringWriterToPage(resultString, "application/json", response);
    }
    //获取全部部门
    @SystemControllerLog(description="获取全部部门")
    @RequestMapping("getDepartmentAll")
    @ResponseBody
    public void getDepartmentAll(HttpServletRequest request, HttpServletResponse response) throws Exception{
        List<DlpDepartment> dlpDepartmentList = searchDataReadService.getDlpDepartment();
        String resultString = formatToJson(dlpDepartmentList);
        super.pringWriterToPage(resultString, "application/json", response);
    }
    //获取部门列表
    @SystemControllerLog(description="获取部门列表")
    @RequestMapping("getDlpDepartmentList")
    @ResponseBody
    public void getDlpDepartmentList(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Integer pageNum = Integer.parseInt(request.getParameter("pageNum"));
        Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
        String queryParam = request.getParameter("queryParam");
        String searchText = request.getParameter("searchText");
        HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("^,create_time","desc");
        if(queryParam!=null && !queryParam.equals("")){
            if(searchText!=null && !searchText.equals("")){
                queryParams.put("%,"+queryParam,searchText);
            }
        }
        List<DlpDepartment> dlpDepartmentList = searchDataReadService.getDlpDepartment(queryParams,pageNum,pageSize);
        Integer count = searchDataReadService.getDlpDepartmentCount(queryParams);
        List<HashMap<String, Object>> dataList = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> dataMap = null;
        for(DlpDepartment dep:dlpDepartmentList){
            dataMap = new HashMap<String, Object>();
            dataMap.put("id", dep.getId());
            dataMap.put("name", dep.getName());
            dataMap.put("detail", dep.getDetail()==null?"":dep.getDetail());
            dataMap.put("createTime", dep.getCreateTime());
            dataList.add(dataMap);
        }
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("data",dataList);
        data.put("count",(count + pageSize - 1) / pageSize);
        String resultString = formatToJson(data);
        super.pringWriterToPage(resultString, "application/json", response);
    }
    //获取部门
    @SystemControllerLog(description="获取部门")
    @RequestMapping("getDlpDepartment")
    @ResponseBody
    public void getDlpDepartment(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String id = request.getParameter("id");
        DlpDepartment dlpDepartment = searchDataReadService.findDlpDepartment(Integer.parseInt(id));
        String resultString = formatToJson(dlpDepartment);
        super.pringWriterToPage(resultString, "application/json", response);
    }
    //新增或修改部门
    @SystemControllerLog(description="新增或修改部门")
    @RequestMapping("addOrEditDlpDepartment")
    @ResponseBody
    public void addOrEditDlpDepartment(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String detail = request.getParameter("detail");
        HashMap<String, Object> map = new HashMap<String, Object>();
        if(id!=null && id.equals("0")){
            DlpDepartment dlpDepartment = new DlpDepartment();
            if(name!=null && !name.equals("")){
                HashMap<String, Object> queryParams = new HashMap<String, Object>();
                queryParams.put("=,name",name);
                List<DlpDepartment> dlpDepartmentList = searchDataReadService.getDlpDepartment(queryParams,1,1);
                if(dlpDepartmentList.size()<1){
                    dlpDepartment.setName(name);
                    if(detail!=null && !detail.equals("")){
                        dlpDepartment.setDetail(detail);
                    }
                    dlpDepartment.setCreateTime(DateUtils.getDateTime());
                    try {
                        searchDataWriteService.saveDlpDepartment(dlpDepartment);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    map.put("type", "false");
                }
            }
        }else{
            DlpDepartment dlpDepartment = searchDataReadService.findDlpDepartment(Integer.parseInt(id));
            if(name!=null && !name.equals("")){
                HashMap<String, Object> queryParams = new HashMap<String, Object>();
                queryParams.put("=,name",name);
                List<DlpDepartment> dlpDepartmentList = searchDataReadService.getDlpDepartment(queryParams,1,1);
                if(name.equals(dlpDepartment.getName()) || dlpDepartmentList.size()<1){
                    dlpDepartment.setName(name);
                    if(detail!=null && !detail.equals("")){
                        dlpDepartment.setDetail(detail);
                    }
                    try {
                        searchDataWriteService.editDlpDepartment(dlpDepartment);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    map.put("type", "false");
                }
            }
        }
        String resultString = formatToJson(map);
        super.pringWriterToPage(resultString, "application/json", response);
    }
    //删除部门
    @SystemControllerLog(description="删除部门")
    @RequestMapping("delDlpDepartment")
    @ResponseBody
    public void delDlpDepartment(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String ids = request.getParameter("id");
        String[] idstr = ids.split(",");
        HashMap<String, Object> map = new HashMap<String, Object>();
        for(String id:idstr){
            HashMap<String, Object> queryParams = new HashMap<String, Object>();
            queryParams.put("=,dpt_id",id);
            List<SystemUser> userList = searchDataReadService.getSystemUser(queryParams,1,1);
            if(userList.size()<1){
                DlpDepartment dlpDepartment = searchDataReadService.findDlpDepartment(Integer.parseInt(id));
                searchDataWriteService.deleteDlpDepartment(dlpDepartment);
            }else{
                map.put("description", "部门下关联用户项无法删除");
            }
        }
        String resultString = formatToJson(map);
        super.pringWriterToPage(resultString, "application/json", response);
    }
    //获取日志列表
    @SystemControllerLog(description="获取日志列表")
    @RequestMapping("getSystemLogList")
    @ResponseBody
    public void getSystemLogList(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Integer pageNum = Integer.parseInt(request.getParameter("pageNum"));
        Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
        String queryType = request.getParameter("queryType");
        String queryParam = request.getParameter("queryParam");
        String searchText = request.getParameter("searchText");
        HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("^,create_time","desc");
        if(queryType!=null && !queryType.equals("")){
            queryParams.put("=,type",queryType);
        }
        if(queryParam!=null && !queryParam.equals("")){
            if(searchText!=null && !searchText.equals("")){
                queryParams.put("%,"+queryParam,searchText);
            }
        }
        List<SystemLog> systemLogList = searchDataReadService.getSystemLog(queryParams,pageNum,pageSize);
        Integer count = searchDataReadService.getSystemLogCount(queryParams);
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("data",systemLogList);
        data.put("count",(count + pageSize - 1) / pageSize);
        String resultString = formatToJson(data);
        super.pringWriterToPage(resultString, "application/json", response);
    }
    //获取日志详情
    @SystemControllerLog(description="获取日志详情")
    @RequestMapping("getSystemLogDetail")
    @ResponseBody
    public void getSystemLogDetail(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String id = request.getParameter("id");
        SystemLog systemLog = searchDataReadService.findSystemLog(Integer.parseInt(id));
        String resultString = formatToJson(systemLog);
        super.pringWriterToPage(resultString, "application/json", response);
    }
}
package org.cloud.framework.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
public class UserController extends BaseContoroller {


	/*
	 * @Autowired private ISearchDataReadService searchDataReadService;
	 * 
	 * @Autowired private ISearchDataWriteService searchDataWriteService;
	 */
    /*
     *
     * 用户管理开始
     * */
    //角色选择

    @RequestMapping("/roleManage")
    @ResponseBody
    public void roleManage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        LinkedHashMap<String, Object> data = null;
        data = new LinkedHashMap<String, Object>();
        data.put("systemRole", "tesr");
        String result = formatToJson(data);
        super.pringWriterToPage(result, "application/json", response);

    }

//    //用户管理列表
//    @SystemControllerLog(description="查看用户页面")
//    @RequestMapping("/userManage")
//    @ResponseBody
//    public void userManage(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        String username = request.getParameter("username");
//        String count = request.getParameter("count");
//        String status = request.getParameter("status");
//        LinkedHashMap<String, Object> data = null;
//        HashMap<String, Object> queryParams = new LinkedHashMap<String, Object>();
//        //控制每页显示条数
//        int pageCount = 15;
//        if (username.equals("") != true && username.equals(null) != true) {
//            queryParams.put("%,username", username);
//        }
//        if (status != null && status.equals("") != true) {
//            queryParams.put("=,type", status);
//        }
//        List<SystemUser> systemUser = searchDataReadService.getSystemUser(queryParams, Integer.parseInt(count), pageCount);
//        List<String> rolename = new ArrayList<>();
//        for(SystemUser s : systemUser){
//            SystemRole systemRole = searchDataReadService.findSystemRole(s.getType());
//            rolename.add(systemRole.getName());
//        }
//        Integer systemUserCount = searchDataReadService.getSystemUserCount(queryParams);
//        systemUserCount = (systemUserCount + pageCount - 1) / pageCount;
//        data = new LinkedHashMap<String, Object>();
//        data.put("lists", systemUser);
//        data.put("count", systemUserCount);
//        data.put("rolename", rolename);
//        String result = formatToJson(data);
//        super.pringWriterToPage(result, "application/json", response);
//
//    }
//
//    //编辑页面回显
//
//    @RequestMapping("/findUserAddEdit")
//    @ResponseBody
//    public void findUserAddEdit(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        String id = request.getParameter("id");
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("=,id", id);
//        List<SystemUser> list = searchDataReadService.getSystemUser(map, 0, 10);
//        String result = formatToJson(list);
//        super.pringWriterToPage(result, "application/json", response);
//    }
//
//    //删除user
//    @SystemControllerLog(description="删除用户操作")
//    @RequestMapping("/deleteuser")
//    @ResponseBody
//    public void deleteuser(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        //删除用户
//        String id = request.getParameter("id");
//
//        SystemUser systemUser = searchDataReadService.findSystemUser(Integer.parseInt(id));
//        //不能删除admin
//        if(systemUser.getUsername().equals("admin")){
//            Map<String, String> m = new HashMap<String, String>();
//            m.put("type", "3");
//            String result = super.formatToJson(m);
//            super.pringWriterToPage(result, "application/json", response);
//        }else{
//            searchDataWriteService.deleteSystemUser(systemUser);
//            //删除关联表
//            HashMap<String, Object> queryParams = new LinkedHashMap<String, Object>();
//            queryParams.put("=,user_id", id);
//            List<SystemUserRole> systemUserRole1 = searchDataReadService.getSystemUserRole(queryParams, 0, 99);
//            for(SystemUserRole s :systemUserRole1){
//                searchDataWriteService.deleteSystemUserRole(s);
//            }
//            Map<String, String> m = new HashMap<String, String>();
//            m.put("type", "1");
//            String result = super.formatToJson(m);
//            super.pringWriterToPage(result, "application/json", response);
//        }
//
//    }
//
//    //编辑/新增
//    @SystemControllerLog(description="修改/新增用户操作")
//    @RequestMapping("/projectAddEdit")
//    @ResponseBody
//    public void projectAddEdit(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        String id = request.getParameter("id");
//        String password = MD5.convert(request.getParameter("password"));
//        String status = request.getParameter("status");
//        String phone = request.getParameter("phone");
//        String email = request.getParameter("email");
//        String signature = request.getParameter("signature");
//        String type = request.getParameter("type");
//        String project = "0";
//        String username = request.getParameter("username");
//        String createTime = request.getParameter("createTime");
//        String createUser = request.getParameter("createUser");
//        String types = request.getParameter("types");
//        //获取登录用户
//        Cookie[] cookies = request.getCookies();
//        String cookiesusername = "";
//        for (Cookie cookie : cookies) {
//            if (cookie.getName().equals("username")) {
//                cookiesusername = cookie.getValue();
//            }
//        }
//        SystemUser systemuser = new SystemUser();
//        systemuser.setUsername(username);
//        systemuser.setPassword(password);
//        systemuser.setStatus(Integer.parseInt(status));
//        systemuser.setTelephone(phone);
//        systemuser.setEmail(email);
//        systemuser.setSignature(signature);
//        systemuser.setType(Integer.parseInt(type));
//        systemuser.setProjectId(Integer.parseInt(project));
//        systemuser.setCreateTime(createTime);
//        systemuser.setCreateUser(createUser);
//        if (types.equals("0")) {
//            Date day = new Date();
//            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            systemuser.setCreateTime(df.format(day));
//            systemuser.setCreateUser(cookiesusername);
//            Cookie[] cookie = request.getCookies();
//            for(Cookie c : cookie){
//                if(c.getName().equals("username")){
//                    systemuser.setCreateUser(c.getValue());
//                }
//            }
//            SystemUser systemUser = searchDataWriteService.saveSystemUser(systemuser);
//
//            SystemUserRole systemUserRole = new SystemUserRole();
//            systemUserRole.setRoleId(Integer.parseInt(type));
//            systemUserRole.setUserId(systemUser.getId());
//            systemUserRole.setProjectId(1);
//            searchDataWriteService.saveSystemUserRole(systemUserRole);
//
//            Map<String, String> m = new HashMap<String, String>();
//            m.put("type", "0");
//            String result = super.formatToJson(m);
//            super.pringWriterToPage(result, "application/json", response);
//        } else if (types.equals("1")) {
//            SystemUser systemUser = searchDataReadService.findSystemUser(Integer.parseInt(id));
//            //关联表修改
//            HashMap<String, Object> queryParams = new LinkedHashMap<String, Object>();
//            queryParams.put("=,user_id", id);
//            queryParams.put("=,role_id", systemUser.getType());
//            List<SystemUserRole> systemUserRole1 = searchDataReadService.getSystemUserRole(queryParams, 0, 99);
//            for(SystemUserRole s :systemUserRole1){
//                s.setRoleId(Integer.parseInt(type));
//                searchDataWriteService.editSystemUserRole(s);
//            }
//            //修改用户
//            systemuser.setId(Integer.parseInt(id));
//            if(request.getParameter("password").equals(systemUser.getPassword())){
//                systemuser.setPassword(request.getParameter("password"));
//            }
//            searchDataWriteService.editSystemUser(systemuser);
//            Map<String, String> m = new HashMap<String, String>();
//            m.put("type", "1");
//            String result = super.formatToJson(m);
//            super.pringWriterToPage(result, "application/json", response);
//        }
//    }
//
//    /*
//     *用户管理结束
//     *角色管理开始
//     */
//    @SystemControllerLog(description="查看角色页面")
//    @RequestMapping("/rolelist")
//    @ResponseBody
//    public void rolelist(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        String rolename = request.getParameter("rolename");
//        String count = request.getParameter("count");
//        LinkedHashMap<String, Object> data = null;
//        HashMap<String, Object> queryParams = new LinkedHashMap<String, Object>();
//        //控制每页显示条数
//        int pageCount = 15;
//        if ( rolename != ""&&rolename != null) {
//            queryParams.put("%,name", rolename);
//        }
//
//        List<SystemRole> systemRole = searchDataReadService.getSystemRole(queryParams, Integer.parseInt(count), pageCount);
//        Integer systemRoleCount = searchDataReadService.getSystemRoleCount(queryParams);
//        systemRoleCount = (systemRoleCount + pageCount - 1) / pageCount;
//        data = new LinkedHashMap<String, Object>();
//        data.put("lists", systemRole);
//        data.put("count", systemRoleCount);
//        String result = formatToJson(data);
//        super.pringWriterToPage(result, "application/json", response);
//
//    }
//
//    //角色编辑页面回显
//    @RequestMapping("/findRoleAddEdit")
//    @ResponseBody
//    public void findRoleAddEdit(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        String id = request.getParameter("id");
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("=,id", id);
//        List<SystemRole> list = searchDataReadService.getSystemRole(map, 0, 10);
//        String result = formatToJson(list);
//        super.pringWriterToPage(result, "application/json", response);
//    }
//
//    //角色编辑/新增
//    @SystemControllerLog(description="修改/新增角色操作")
//    @RequestMapping("/roleAddEdit")
//    @ResponseBody
//    public void roleAddEdit(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        String id = request.getParameter("id");
//        String name = request.getParameter("name");
//        String description = request.getParameter("description");
//        String managerRoleIds = request.getParameter("managerRoleIds");
//        String projectId = "0";
//        String types = request.getParameter("types");
//
//        SystemRole systemRole = new SystemRole();
//
//        systemRole.setDescription(description);
//        systemRole.setManagerRoleIds(managerRoleIds);
//        systemRole.setName(name);
//        systemRole.setProjectId(Integer.parseInt(projectId));
//        //0为新增   1为编辑
//        if (types.equals("0")) {
//            searchDataWriteService.saveSystemRole(systemRole);
//            Map<String, String> m = new HashMap<String, String>();
//            m.put("type", "0");
//            String result = super.formatToJson(m);
//            super.pringWriterToPage(result, "application/json", response);
//        } else if (types.equals("1")) {
//            systemRole.setId(Integer.parseInt(id));
//            searchDataWriteService.editSystemRole(systemRole);
//            Map<String, String> m = new HashMap<String, String>();
//            m.put("type", "1");
//            String result = super.formatToJson(m);
//            super.pringWriterToPage(result, "application/json", response);
//        }
//    }
//
//    //删除角色
//    @SystemControllerLog(description="删除用户操作")
//    @RequestMapping("/deleterole")
//    @ResponseBody
//    public void deleterole(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        String id = request.getParameter("id");
//        HashMap<String, Object> queryParams = new LinkedHashMap<String, Object>();
//        queryParams.put("=,type", id);
//        List<SystemUser> systemUser = searchDataReadService.getSystemUser(queryParams, 0, 10);
//        if(systemUser.size()>0){
//            Map<String, String> m = new HashMap<String, String>();
//            m.put("type", "2");
//            String result = super.formatToJson(m);
//            super.pringWriterToPage(result, "application/json", response);
//        }else{
//            SystemRole systemRole = searchDataReadService.findSystemRole(Integer.parseInt(id));
//            searchDataWriteService.deleteSystemRole(systemRole);
//            //删除关联表
//            HashMap<String, Object> queryParam = new LinkedHashMap<String, Object>();
//            queryParam.put("=,role_id", id);
//            List<SystemUserRole> systemUserRole1 = searchDataReadService.getSystemUserRole(queryParam, 0, 99);
//            for(SystemUserRole s :systemUserRole1){
//                searchDataWriteService.deleteSystemUserRole(s);
//            }
//            Map<String, String> m = new HashMap<String, String>();
//            m.put("type", "1");
//            String result = super.formatToJson(m);
//            super.pringWriterToPage(result, "application/json", response);
//        }
//    }
//
//    /*角色管理结束*/
//    /*菜单管理开始*/
//    //添加菜单
//    @SystemControllerLog(description="添加菜单操作")
//    @RequestMapping("/addmenu")
//    @ResponseBody
//    public void addmenu(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        String id = request.getParameter("id");
//        String name = request.getParameter("name");
//        String describe = request.getParameter("describe");
//        String url = request.getParameter("url");
//        String sort = request.getParameter("sort");
//        String types = request.getParameter("types");
//        String status = request.getParameter("status");
//        String parentId = request.getParameter("nodeid");
//        String icon = request.getParameter("icon");
//
//        SystemMenu syatemMenu = new SystemMenu();
//        syatemMenu.setDescription(describe);
//        syatemMenu.setName(name);
//        syatemMenu.setParentId(Integer.parseInt(parentId));
//        syatemMenu.setSort(Integer.parseInt(sort));
//        syatemMenu.setStatus(Integer.parseInt(status));
//        syatemMenu.setUrl(url);
//        syatemMenu.setLevel(1);
//        syatemMenu.setParentId(Integer.parseInt(parentId));
//        syatemMenu.setIcon(icon);
//        if (types.equals("0")) {
//            searchDataWriteService.saveSystemMenu(syatemMenu);
//            Map<String, String> m = new HashMap<String, String>();
//            m.put("type", "1");
//            String result = super.formatToJson(m);
//            super.pringWriterToPage(result, "application/json", response);
//        }else if(types.equals("1")){
//            syatemMenu.setId(Integer.parseInt(id));
//            searchDataWriteService.editSystemMenu(syatemMenu);
//            Map<String, String> m = new HashMap<String, String>();
//            m.put("type", "1");
//            String result = super.formatToJson(m);
//            super.pringWriterToPage(result, "application/json", response);
//        }
//
//    }
//
//    //菜单列表展示
//    @SystemControllerLog(description="查看菜单页面")
//    @RequestMapping("/menuList")
//    @ResponseBody
//    public void menuList(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        Map<String, Object> m=null;
//        LinkedList<Map<String,Object>> data = new LinkedList();
//        LinkedHashMap<String, Object> datas = new LinkedHashMap<String, Object>();
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("=,parent_id", 0);
//        map.put("^,sort","asc");
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("!,parent_id", 0);
//        maps.put("^,sort","asc");
//        List<SystemMenu> systemMenulist = searchDataReadService.getSystemMenu(map,0,999);
//        List<SystemMenu> systemMenulists = searchDataReadService.getSystemMenu(maps,0,999);
//        for(SystemMenu menu:systemMenulist){
//            m = new HashMap<String, Object>();
//            m.put("name", menu.getName());
//            m.put("id", menu.getId());
//            m.put("pid", 0);
//            m.put("open", "true");
//            data.add(m);
//            for(SystemMenu menus:systemMenulists){
//                if(menu.getId()==menus.getParentId()){
//                    m = new HashMap<String, Object>();
//                    m.put("name", menus.getName());
//                    m.put("pid", menu.getId());
//                    m.put("id", menus.getId());
//                    data.add(m);
//                }
//            }
//        }
//        String s = data.toString();
//        datas.put("res",data);
//        String result = super.formatToJson(datas);
//        super.pringWriterToPage(result, "application/json", response);
//
//    }
//    //编辑页面回显
//    @RequestMapping("/findMenu")
//    @ResponseBody
//    public void findMenu(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        String id = request.getParameter("id");
//        SystemMenu systemMenu = searchDataReadService.findSystemMenu(Integer.parseInt(id));
//        int fid = systemMenu.getParentId();
//        SystemMenu systemMenu2 = null;
//        if(fid==0){
//            systemMenu2 = new SystemMenu();
//            systemMenu2.setName("根目录");
//        }else{
//            systemMenu2 = searchDataReadService.findSystemMenu(fid);
//        }
//
//
//        LinkedHashMap<String, Object> datas = new LinkedHashMap<String, Object>();
//        datas.put("systemMenu",systemMenu);
//        datas.put("systemMenu2",systemMenu2);
//
//        String result = formatToJson(datas);
//        super.pringWriterToPage(result, "application/json", response);
//    }
//    //删除菜单
//    @SystemControllerLog(description="删除菜单操作")
//    @RequestMapping("/deletemenu")
//    @ResponseBody
//    public void deletemenu(HttpServletRequest request, HttpServletResponse response) throws Exception   {
//        String id = request.getParameter("id");
//        SystemMenu systemMenu  = searchDataReadService.findSystemMenu(Integer.parseInt(id));
//        searchDataWriteService.deleteSystemMenu(systemMenu);
//        Map<String, String> m = new HashMap<String, String>();
//        m.put("type", "1");
//        String result = super.formatToJson(m);
//        super.pringWriterToPage(result, "application/json", response);
//    }
//    //汉字转拼音
//    @RequestMapping("/pinyin")
//    @ResponseBody
//    public void pinyin(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        LinkedHashMap<String, Object> datas = new LinkedHashMap<String, Object>();
//        String id = request.getParameter("nodeid");
//        String name = request.getParameter("name");
//        String pinyin = Pinyin4jUtils.getFirstSpell(name);
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("=,parent_id", id);
//        List<SystemMenu> systemMenulist = searchDataReadService.getSystemMenu(maps,0,999);
//        if(systemMenulist!=null){
//            datas.put("count",systemMenulist.size()+1);
//        }else{
//            datas.put("count",1);
//        }
//        datas.put("pinyin",pinyin);
//        String result = formatToJson(datas);
//        super.pringWriterToPage(result, "application/json", response);
//    }
//    /*菜单结束*/
//    /*权限开始*/
//    //显示角色
//    @RequestMapping("/roleshow")
//    @ResponseBody
//    public void roleshow(HttpServletRequest request, HttpServletResponse response) throws Exception {
//
//        LinkedHashMap<String, Object> data = null;
//        List<SystemRole> systemRole = searchDataReadService.getSystemRole();
//
//        data = new LinkedHashMap<String, Object>();
//        data.put("lists", systemRole);
//
//        String result = formatToJson(data);
//        super.pringWriterToPage(result, "application/json", response);
//
//    }
//    //列表显示
//    @SystemControllerLog(description="查看权限页面")
//    @RequestMapping("/authorityshow")
//    @ResponseBody
//    public void authorityshow(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        String id = request.getParameter("id");
//        LinkedHashMap<String, Object> data = new LinkedHashMap<String, Object>();
//        HashMap<String, Object> queryParams = new HashMap<String, Object>();
//        queryParams.put("=,role_id",id);
//        List<SystemPermission> systemPermissionList = searchDataReadService.getSystemPermission(queryParams,1,1);
//        SystemPermission systemPermission = null;
//        if(null != systemPermissionList && systemPermissionList.size()>0){
//            systemPermission = systemPermissionList.get(0);
//            //过滤会勾选父节点引发默认全部勾选的问题
//            String mids = systemPermission.getMenuIds();
//            String[] split = mids.split(";");
//            List<SystemMenu> systemMenu = searchDataReadService.getSystemMenu();
//            String ids = "";
//            boolean tp = true;
//            for(String a : split){
//                tp = true;
//                for(SystemMenu sm :systemMenu){
//                    if(Integer.parseInt(a)==sm.getParentId()){
//                        tp=false;
//                    }
//                }
//                if(tp){
//                    ids+=a+";";
//                }
//            }
//            data.put("mids", ids);
//        }
//        data.put("systemPermission", systemPermission);
//
//        String result = formatToJson(data);
//        super.pringWriterToPage(result, "application/json", response);
//
//    }
//    //保存\修改权限信息
//    @SystemControllerLog(description="修改权限操作")
//    @RequestMapping("/saveData")
//    @ResponseBody
//    public void saveData(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        String id = request.getParameter("id");
//        String pid = request.getParameter("pid");
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("=,role_id", id);
//        List<SystemPermission> systemPermission = searchDataReadService.getSystemPermission(map,0,1);
//        if(systemPermission.size()>0){
//            SystemPermission sp = systemPermission.get(0);
//            sp.setMenuIds(pid);
//            searchDataWriteService.editSystemPermission(sp);
//            LinkedHashMap<String, Object> data = new LinkedHashMap<String, Object>();
//            data.put("type", 0);
//            String result = formatToJson(data);
//            super.pringWriterToPage(result, "application/json", response);
//        }else{
//            SystemPermission sp = new SystemPermission();
//            sp.setMenuIds(pid);
//            sp.setProjectId(1);
//            sp.setRoleId(Integer.parseInt(id));
//            searchDataWriteService.saveSystemPermission(sp);
//            LinkedHashMap<String, Object> data = new LinkedHashMap<String, Object>();
//            data.put("type", 1);
//            String result = formatToJson(data);
//            super.pringWriterToPage(result, "application/json", response);
//        }
//    }
//    /*权限结束*/
//    /*日志开始*/
//    @SystemControllerLog(description="查看日志页面")
//    @RequestMapping("/loglist")
//    @ResponseBody
//    public void loglist(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        String count = request.getParameter("count");
//        String status = request.getParameter("status");
//        String selectdata = request.getParameter("selectdata");
//
//        LinkedHashMap<String, Object> data = null;
//        HashMap<String, Object> queryParams = new LinkedHashMap<String, Object>();
//        //控制每页显示条数
//        int pageCount = 15;
//        if (status != null && status.equals("") != true) {
//            if(status.equals("1")){
//                if (selectdata.equals("") != true && selectdata.equals(null) != true) {
//                    queryParams.put("%,title", selectdata);
//                }
//            }else if(status.equals("2")){
//                if (selectdata.equals("") != true && selectdata.equals(null) != true) {
//                    queryParams.put("%,type", selectdata);
//                }
//            }
//        }
//        queryParams.put("^,create_time","desc");
//        List<SystemLog> systemLog = searchDataReadService.getSystemLog(queryParams, Integer.parseInt(count), pageCount);
//        Integer systemULogCount = searchDataReadService.getSystemLogCount(queryParams);
//        systemULogCount = (systemULogCount + pageCount - 1) / pageCount;
//        data = new LinkedHashMap<String, Object>();
//        data.put("lists", systemLog);
//        data.put("count", systemULogCount);
//        String result = formatToJson(data);
//        super.pringWriterToPage(result, "application/json", response);
//
//    }
//    @RequestMapping("/getlogdata")
//    @ResponseBody
//    public void getlogdata(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        String id = request.getParameter("id");
//        LinkedHashMap<String, Object> data = null;
//        SystemLog systemLog = searchDataReadService.findSystemLog(Integer.parseInt(id));
//        data = new LinkedHashMap<String, Object>();
//        data.put("systemLog", systemLog);
//        String result = formatToJson(data);
//        super.pringWriterToPage(result, "application/json", response);
//
//    }

}

package org.cloud.framework.controller;

import org.cloud.framework.annotation.SystemControllerLog;
import org.cloud.framework.model.*;
import org.cloud.framework.protocol.INlpService;
import org.cloud.framework.protocol.ISearchDataReadService;
import org.cloud.framework.protocol.ISearchDataWriteService;
import org.cloud.framework.utils.DateUtils;
import org.cloud.framework.utils.MuliFileUtils;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;

@Controller
public class SecretController extends BaseContoroller{

    @Autowired
    private ISearchDataReadService searchDataReadService;
    @Autowired
    private ISearchDataWriteService searchDataWriteService;
    @Autowired
    private INlpService nlpService;
    @Autowired
    @Qualifier("Scheduler")
    private Scheduler scheduler;

    @Value("${data.upload.file}")
    private String uploadFilePath;

    //获取密级列表
    @SystemControllerLog(description="获取密级列表")
    @RequestMapping("getClassifyList")
    @ResponseBody
    public void getClassifyList(HttpServletRequest request, HttpServletResponse response) throws Exception{
        HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("^,use_level","asc");
        List<DlpClassify> dlpClassifyList = searchDataReadService.getDlpClassify(queryParams,1,Integer.MAX_VALUE);
        String resultString = formatToJson(dlpClassifyList);
        super.pringWriterToPage(resultString, "application/json", response);
    }
    //获取密级
    @SystemControllerLog(description="获取密级")
    @RequestMapping("getClassify")
    @ResponseBody
    public void getClassify(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String id = request.getParameter("id");
        DlpClassify dlpClassify = searchDataReadService.findDlpClassify(Integer.parseInt(id));
        String resultString = formatToJson(dlpClassify);
        super.pringWriterToPage(resultString, "application/json", response);
    }
    //新增或修改密级
    @SystemControllerLog(description="新增或修改密级")
    @RequestMapping("addOrEditClassify")
    @ResponseBody
    public void addOrEditClassify(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String cnum = request.getParameter("cnum");
        String level = request.getParameter("level");
        String detail = request.getParameter("detail");
        String isStoped = request.getParameter("isStoped");
        HashMap<String, Object> map = new HashMap<String, Object>();

        if(id!=null && id.equals("0")){
            DlpClassify dlpClassify = new DlpClassify();
            HashMap<String, Object> queryParams = new HashMap<String, Object>();
            queryParams.put("=,name",name);
            List<DlpClassify> dlpClassifyNameList = searchDataReadService.getDlpClassify(queryParams,1,1);
            HashMap<String, Object> queryParamc = new HashMap<String, Object>();
            queryParamc.put("=,cnum",cnum);
            List<DlpClassify> dlpClassifyCnumList = searchDataReadService.getDlpClassify(queryParamc,1,1);
            HashMap<String, Object> queryParaml = new HashMap<String, Object>();
            queryParaml.put("=,use_level",level);
            List<DlpClassify> dlpClassifyLevelList = searchDataReadService.getDlpClassify(queryParaml,1,1);
            if(dlpClassifyNameList.size()>0){
                map.put("description", "密级名称重复");
            }else  if(dlpClassifyCnumList.size()>0){
                map.put("description", "密级编号重复");
            }else  if(dlpClassifyLevelList.size()>0){
                map.put("description", "密级等级重复");
            }else{
                dlpClassify.setName(name);
                if(cnum!=null && !cnum.equals("")){
                    dlpClassify.setCnum(cnum);
                }
                if(level!=null && !level.equals("")){
                    dlpClassify.setUseLevel(Integer.parseInt(level));
                }
                if(detail!=null && !detail.equals("")){
                    dlpClassify.setDetail(detail);
                }
                if(isStoped!=null && !isStoped.equals("")){
                    dlpClassify.setIsStoped(Integer.parseInt(isStoped));
                }
                dlpClassify.setCreateTime(DateUtils.getDateTime());
                try {
                    searchDataWriteService.saveDlpClassify(dlpClassify);
                    nlpService.classifyCreate(dlpClassify);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else{
            DlpClassify dlpClassify = searchDataReadService.findDlpClassify(Integer.parseInt(id));
            HashMap<String, Object> queryParams = new HashMap<String, Object>();
            queryParams.put("=,name",name);
            List<DlpClassify> dlpClassifyNameList = searchDataReadService.getDlpClassify(queryParams,1,1);
            HashMap<String, Object> queryParamc = new HashMap<String, Object>();
            queryParamc.put("=,cnum",cnum);
            List<DlpClassify> dlpClassifyCnumList = searchDataReadService.getDlpClassify(queryParamc,1,1);
            HashMap<String, Object> queryParaml = new HashMap<String, Object>();
            queryParaml.put("=,use_level",level);
            List<DlpClassify> dlpClassifyLevelList = searchDataReadService.getDlpClassify(queryParaml,1,1);
            if(!name.equals(dlpClassify.getName()) && dlpClassifyNameList.size()>0){
                map.put("description", "密级名称重复");
            }else if(!cnum.equals(dlpClassify.getCnum()) && dlpClassifyCnumList.size()>0){
                map.put("description", "密级编号重复");
            }else if(!level.equals(dlpClassify.getUseLevel().toString()) && dlpClassifyLevelList.size()>0){
                map.put("description", "密级等级重复");
            }else{
                dlpClassify.setName(name);
                if(cnum!=null && !cnum.equals("")){
                    dlpClassify.setCnum(cnum);
                }
                if(level!=null && !level.equals("")){
                    dlpClassify.setUseLevel(Integer.parseInt(level));
                }
                if(detail!=null && !detail.equals("")){
                    dlpClassify.setDetail(detail);
                }
                if(isStoped!=null && !isStoped.equals("")){
                    dlpClassify.setIsStoped(Integer.parseInt(isStoped));
                }
                try {
                    searchDataWriteService.editDlpClassify(dlpClassify);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        String resultString = formatToJson(map);
        super.pringWriterToPage(resultString, "application/json", response);
    }
    //删除密级
    @SystemControllerLog(description="删除密级")
    @RequestMapping("delClassify")
    @ResponseBody
    public void delClassify(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String id = request.getParameter("id");
        HashMap<String, Object> map = new HashMap<String, Object>();
        DlpClassify dlpClassify = searchDataReadService.findDlpClassify(Integer.parseInt(id));
        HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("=,cnum",dlpClassify.getCnum());
        List<DlpCword> dlpCwordList = searchDataReadService.getDlpCword(queryParams,1,1);
        boolean dmFlag = false;
        List<DlpModel> dlpModelList = searchDataReadService.getDlpModel();
        for(DlpModel dm:dlpModelList){
            if(dm.getLabels()!=null && !dm.getLabels().equals("")){
                String[] labels = dm.getLabels().split(";");
                List<String> labelsList = Arrays.asList(labels);
                if(labelsList.contains(dlpClassify.getCnum())){
                    dmFlag = true;
                    break;
                }
            }
        }
        if(dlpCwordList.size()>0 ){
            map.put("description", "密级下关联触发词项无法删除");
        }else if(dmFlag){
            map.put("description", "密级下关联模型项无法删除");
        }else{
            searchDataWriteService.deleteDlpClassify(dlpClassify);
        }
        String resultString = formatToJson(map);
        super.pringWriterToPage(resultString, "application/json", response);
    }

    //获取术语列表
    @SystemControllerLog(description="获取术语列表")
    @RequestMapping("getTermList")
    @ResponseBody
    public void getTermList(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Integer pageNum = Integer.parseInt(request.getParameter("pageNum"));
        Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
        String termclassId = request.getParameter("termclassId");
        String isPublished = request.getParameter("isPublished");
        HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("^,create_time","desc");
        if(termclassId!=null && !termclassId.equals("")){
            queryParams.put("=,termclass_id",termclassId);
        }
        if(isPublished!=null && !isPublished.equals("")){
            queryParams.put("=,is_published",isPublished);
        }
        List<DlpTerm> dlpTermList = searchDataReadService.getDlpTerm(queryParams,pageNum, pageSize);
        Integer count = searchDataReadService.getDlpTermCount(queryParams);
        List<HashMap<String, Object>> dataList = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> dataMap = null;
        for(DlpTerm dt:dlpTermList){
            dataMap = new HashMap<String, Object>();
            dataMap.put("id", dt.getId());
            dataMap.put("name", dt.getName());
            dataMap.put("isStoped", dt.getIsStoped());
            dataMap.put("isPublished", dt.getIsPublished());
            String termClass ="";
            if(dt.getTermclassId()!=null && !dt.getTermclassId().equals("")){
                DlpTermClass dlpTermClass = searchDataReadService.findDlpTermClass(dt.getTermclassId());
                if(dlpTermClass!=null){
                    termClass = dlpTermClass.getName();
                }
            }
            dataMap.put("termClass", termClass);
            String createUser ="";
            if(dt.getCreateUid()!=null && !dt.getCreateUid().equals("")){
                createUser = searchDataReadService.findSystemUser(dt.getCreateUid()).getUsername();
            }
            dataMap.put("createUser", createUser);
            dataMap.put("createTime", dt.getCreateTime());
            dataList.add(dataMap);
        }
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("data",dataList);
        data.put("count",(count + pageSize - 1) / pageSize);
        String resultString = formatToJson(data);
        super.pringWriterToPage(resultString, "application/json", response);
    }
    //获取术语
    @SystemControllerLog(description="获取术语")
    @RequestMapping("getTerm")
    @ResponseBody
    public void getTerm(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String id = request.getParameter("id");
        DlpTerm dlpTerm = searchDataReadService.findDlpTerm(Integer.parseInt(id));
        String resultString = formatToJson(dlpTerm);
        super.pringWriterToPage(resultString, "application/json", response);
    }
    //新增或修改术语
    @SystemControllerLog(description="新增或修改术语")
    @RequestMapping("addOrEditTerm")
    @ResponseBody
    public void addOrEditTerm(HttpServletRequest request, HttpServletResponse response) throws Exception{
        SystemUser systemuser = new SystemUser();
        if(request.getSession().getAttribute("userid")!=null && !request.getSession().getAttribute("userid").equals("")){
            systemuser = searchDataReadService.findSystemUser(Integer.parseInt(request.getSession().getAttribute("userid").toString()));
        }
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String termclassId = request.getParameter("termclassId");
        String isStoped = request.getParameter("isStoped");
        HashMap<String, Object> map = new HashMap<String, Object>();

        if(id!=null && id.equals("0")){
            DlpTerm dlpTerm = new DlpTerm();
            if(name!=null && !name.equals("")){
                HashMap<String, Object> queryParams = new HashMap<String, Object>();
                queryParams.put("=,name",name);
                List<DlpTerm> dlpTermList = searchDataReadService.getDlpTerm(queryParams,1,1);
                if(dlpTermList.size()<1){
                    dlpTerm.setName(name);
                    if(termclassId!=null && !termclassId.equals("")){
                        dlpTerm.setTermclassId(Integer.parseInt(termclassId));
                    }
                    if(isStoped!=null && !isStoped.equals("")){
                        dlpTerm.setIsStoped(Integer.parseInt(isStoped));
                    }
                    dlpTerm.setIsPublished(0);
                    dlpTerm.setCreateUid(systemuser.getId());
                    dlpTerm.setCreateTime(DateUtils.getDateTime());
                    try {
                        searchDataWriteService.saveDlpTerm(dlpTerm);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    map.put("type", "false");
                }
            }

        }else{
            DlpTerm dlpTerm = searchDataReadService.findDlpTerm(Integer.parseInt(id));
            if(name!=null && !name.equals("")){
                HashMap<String, Object> queryParams = new HashMap<String, Object>();
                queryParams.put("=,name",name);
                List<DlpTerm> dlpTermList = searchDataReadService.getDlpTerm(queryParams,1,1);
                if(name.equals(dlpTerm.getName()) || dlpTermList.size()<1){
                    dlpTerm.setName(name);
                    if(termclassId!=null && !termclassId.equals("")){
                        dlpTerm.setTermclassId(Integer.parseInt(termclassId));
                    }
                    if(isStoped!=null && !isStoped.equals("")){
                        dlpTerm.setIsStoped(Integer.parseInt(isStoped));
                    }
                    dlpTerm.setIsPublished(0);
                    try {
                        searchDataWriteService.editDlpTerm(dlpTerm);
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
    //删除术语
    @SystemControllerLog(description="删除术语")
    @RequestMapping("delTerm")
    @ResponseBody
    public void delTerm(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String ids = request.getParameter("id");
        String[] idstr = ids.split(",");
        for(String id:idstr){
            DlpTerm dlpTerm = searchDataReadService.findDlpTerm(Integer.parseInt(id));
            searchDataWriteService.deleteDlpTerm(dlpTerm);
        }
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("type", "true");
        String resultString = formatToJson(map);
        super.pringWriterToPage(resultString, "application/json", response);
    }
    //发布术语
    @SystemControllerLog(description="发布术语")
    @RequestMapping("publishTerm")
    @ResponseBody
    public void publishTerm(HttpServletRequest request, HttpServletResponse response) throws Exception{
        HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("=,is_stoped",0);
        List<DlpTerm> dlpTermLists = searchDataReadService.getDlpTerm(queryParams,1,Integer.MAX_VALUE);
        nlpService.termReload(dlpTermLists);
        for(DlpTerm d:dlpTermLists){
            d.setIsPublished(1);
            try {
                searchDataWriteService.editDlpTerm(d);
            }catch (Exception e){

            }
        }
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("type", "true");
        String resultString = formatToJson(map);
        super.pringWriterToPage(resultString, "application/json", response);
    }
    //更改术语状态
    @SystemControllerLog(description="更改术语状态")
    @RequestMapping("updateTermStatus")
    @ResponseBody
    public void updateTermStatus(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String ids = request.getParameter("id");
        String isStoped = request.getParameter("isStoped");
        String[] idstr = ids.split(",");
        for(String id:idstr){
            DlpTerm dlpTerm = searchDataReadService.findDlpTerm(Integer.parseInt(id));
            dlpTerm.setIsStoped(Integer.parseInt(isStoped));
            searchDataWriteService.editDlpTerm(dlpTerm);
        }
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("type", "true");
        String resultString = formatToJson(map);
        super.pringWriterToPage(resultString, "application/json", response);
    }
    //获取术语分类全部列表
    @SystemControllerLog(description="获取术语分类全部列表")
    @RequestMapping("getTermClassListAll")
    @ResponseBody
    public void getTermClassListAll(HttpServletRequest request, HttpServletResponse response) throws Exception{
        List<DlpTermClass> dlpTermClassList = searchDataReadService.getDlpTermClass();
        String resultString = formatToJson(dlpTermClassList);
        super.pringWriterToPage(resultString, "application/json", response);
    }
    //获取术语分类列表
    @SystemControllerLog(description="获取术语分类列表")
    @RequestMapping("getTermClassList")
    @ResponseBody
    public void getTermClassList(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Integer pageNum = Integer.parseInt(request.getParameter("pageNum"));
        Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
        HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("^,id","desc");
        List<DlpTermClass> dlpTermClassList = searchDataReadService.getDlpTermClass(queryParams, pageNum, pageSize);
        Integer count = searchDataReadService.getDlpTermClassCount(queryParams);
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("data",dlpTermClassList);
        data.put("count",(count + pageSize - 1) / pageSize);
        String resultString = formatToJson(data);
        super.pringWriterToPage(resultString, "application/json", response);
    }
    //获取术语分类
    @SystemControllerLog(description="获取术语分类")
    @RequestMapping("getTermClass")
    @ResponseBody
    public void getTermClass(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String id = request.getParameter("id");
        DlpTermClass dlpTermClass = searchDataReadService.findDlpTermClass(Integer.parseInt(id));
        String resultString = formatToJson(dlpTermClass);
        super.pringWriterToPage(resultString, "application/json", response);
    }
    //新增或修改术语分类
    @SystemControllerLog(description="新增或修改术语分类")
    @RequestMapping("addOrEditTermClass")
    @ResponseBody
    public void addOrEditTermClass(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String detail = request.getParameter("detail");
        HashMap<String, Object> map = new HashMap<String, Object>();

        if(id!=null && id.equals("0")){
            DlpTermClass dlpTermClass = new DlpTermClass();
            if(name!=null && !name.equals("")){
                HashMap<String, Object> queryParams = new HashMap<String, Object>();
                queryParams.put("=,name",name);
                List<DlpTermClass> dlpTermClassList = searchDataReadService.getDlpTermClass(queryParams,1,1);
                if(dlpTermClassList.size()<1){
                    dlpTermClass.setName(name);
                    if(detail!=null && !detail.equals("")) {
                        dlpTermClass.setDetail(detail);
                    }
                    try {
                        searchDataWriteService.saveDlpTermClass(dlpTermClass);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    map.put("type", "false");
                }
            }

        }else{
            DlpTermClass dlpTermClass = searchDataReadService.findDlpTermClass(Integer.parseInt(id));
            if(name!=null && !name.equals("")){
                HashMap<String, Object> queryParams = new HashMap<String, Object>();
                queryParams.put("=,name",name);
                List<DlpTermClass> dlpTermClassList = searchDataReadService.getDlpTermClass(queryParams,1,1);
                if(name.equals(dlpTermClass.getName()) || dlpTermClassList.size()<1){
                    dlpTermClass.setName(name);
                    if(detail!=null && !detail.equals("")){
                        dlpTermClass.setDetail(detail);
                    }
                    try {
                        searchDataWriteService.editDlpTermClass(dlpTermClass);
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
    //删除术语分类
    @SystemControllerLog(description="删除术语分类")
    @RequestMapping("delTermClass")
    @ResponseBody
    public void delTermClass(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String ids = request.getParameter("id");
        String[] idstr = ids.split(",");
        HashMap<String, Object> map = new HashMap<String, Object>();
        for(String id:idstr){
            HashMap<String, Object> queryParams = new HashMap<String, Object>();
            queryParams.put("=,termclass_id",id);
            List<DlpTerm> dlpTermList = searchDataReadService.getDlpTerm(queryParams,1,1);
            if(dlpTermList.size()<1){
                DlpTermClass dlpTermClass = searchDataReadService.findDlpTermClass(Integer.parseInt(id));
                searchDataWriteService.deleteDlpTermClass(dlpTermClass);
            }else{
                map.put("description", "术语分类下关联术语无法删除");
            }
        }
        String resultString = formatToJson(map);
        super.pringWriterToPage(resultString, "application/json", response);
    }

    //获取触发词列表
    @SystemControllerLog(description="获取触发词列表")
    @RequestMapping("getCwordList")
    @ResponseBody
    public void getCwordList(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Integer pageNum = Integer.parseInt(request.getParameter("pageNum"));
        Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
        String classifyCnum = request.getParameter("classifyCnum");
        String isPublished = request.getParameter("isPublished");
        HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("^,create_time","desc");
        if(classifyCnum!=null && !classifyCnum.equals("")){
            queryParams.put("=,cnum",classifyCnum);
        }
        if(isPublished!=null && !isPublished.equals("")){
            queryParams.put("=,is_published",isPublished);
        }
        List<DlpCword> dlpCwordList = searchDataReadService.getDlpCword(queryParams,pageNum, pageSize);
        Integer count = searchDataReadService.getDlpCwordCount(queryParams);
        List<HashMap<String, Object>> dataList = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> dataMap = null;
        for(DlpCword dc:dlpCwordList){
            dataMap = new HashMap<String, Object>();
            dataMap.put("id", dc.getId());
            dataMap.put("name", dc.getWord());
            String classifyName = "";
            HashMap<String, Object> queryParamc = new HashMap<String, Object>();
            queryParamc.put("=,cnum",dc.getCnum());
            List<DlpClassify> dlpClassifyList = searchDataReadService.getDlpClassify(queryParamc,1,1);
            if(dlpClassifyList.size()>0){
                classifyName = dlpClassifyList.get(0).getName();
            }
            dataMap.put("classifyName", classifyName);
            dataMap.put("isStoped", dc.getIsStoped());
            dataMap.put("isPublished", dc.getIsPublished());
            String createUser ="";
            if(dc.getCreateUid()!=null && !dc.getCreateUid().equals("")){
                createUser = searchDataReadService.findSystemUser(dc.getCreateUid()).getUsername();
            }
            dataMap.put("createUser", createUser);
            dataMap.put("createTime", dc.getCreateTime());
            dataList.add(dataMap);
        }
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("data",dataList);
        data.put("count",(count + pageSize - 1) / pageSize);
        String resultString = formatToJson(data);
        super.pringWriterToPage(resultString, "application/json", response);
    }
    //获取触发词
    @SystemControllerLog(description="获取触发词")
    @RequestMapping("getCword")
    @ResponseBody
    public void getCword(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String id = request.getParameter("id");
        DlpCword dlpCword = searchDataReadService.findDlpCword(Integer.parseInt(id));
        String resultString = formatToJson(dlpCword);
        super.pringWriterToPage(resultString, "application/json", response);
    }
    //新增或修改触发词
    @SystemControllerLog(description="新增或修改触发词")
    @RequestMapping("addOrEditCword")
    @ResponseBody
    public void addOrEditCword(HttpServletRequest request, HttpServletResponse response) throws Exception{
        SystemUser systemuser = new SystemUser();
        if(request.getSession().getAttribute("userid")!=null && !request.getSession().getAttribute("userid").equals("")){
            systemuser = searchDataReadService.findSystemUser(Integer.parseInt(request.getSession().getAttribute("userid").toString()));
        }
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String cnum = request.getParameter("classifyCnum");
        String isStoped = request.getParameter("isStoped");
        HashMap<String, Object> map = new HashMap<String, Object>();

        if(id!=null && id.equals("0")){
            DlpCword dlpCword = new DlpCword();
            if(name!=null && !name.equals("")){
                HashMap<String, Object> queryParams = new HashMap<String, Object>();
                queryParams.put("=,word",name);
                List<DlpCword> dlpCwordList = searchDataReadService.getDlpCword(queryParams,1,1);
                if(dlpCwordList.size()<1){
                    dlpCword.setWord(name);
                    if(cnum!=null && !cnum.equals("")){
                        dlpCword.setCnum(cnum);
                    }
                    if(isStoped!=null && !isStoped.equals("")){
                        dlpCword.setIsStoped(Integer.parseInt(isStoped));
                    }
                    dlpCword.setIsPublished(0);
                    dlpCword.setCreateUid(systemuser.getId());
                    dlpCword.setCreateTime(DateUtils.getDateTime());
                    try {
                        searchDataWriteService.saveDlpCword(dlpCword);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    map.put("type", "false");
                }
            }

        }else{
            DlpCword dlpCword = searchDataReadService.findDlpCword(Integer.parseInt(id));
            if(name!=null && !name.equals("")){
                HashMap<String, Object> queryParams = new HashMap<String, Object>();
                queryParams.put("=,word",name);
                List<DlpCword> dlpCwordList = searchDataReadService.getDlpCword(queryParams,1,1);
                if(name.equals(dlpCword.getWord()) || dlpCwordList.size()<1){
                    dlpCword.setWord(name);
                    if(cnum!=null && !cnum.equals("")){
                        dlpCword.setCnum(cnum);
                    }
                    if(isStoped!=null && !isStoped.equals("")){
                        dlpCword.setIsStoped(Integer.parseInt(isStoped));
                    }
                    dlpCword.setIsPublished(0);
                    try {
                        searchDataWriteService.editDlpCword(dlpCword);
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
    //删除触发词
    @SystemControllerLog(description="删除触发词")
    @RequestMapping("delCword")
    @ResponseBody
    public void delCword(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String ids = request.getParameter("id");
        String[] idstr = ids.split(",");
        for(String id:idstr){
            DlpCword dlpCword = searchDataReadService.findDlpCword(Integer.parseInt(id));
            searchDataWriteService.deleteDlpCword(dlpCword);
        }
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("type", "true");
        String resultString = formatToJson(map);
        super.pringWriterToPage(resultString, "application/json", response);
    }
    //发布触发词
    @SystemControllerLog(description="发布触发词")
    @RequestMapping("publishCword")
    @ResponseBody
    public void publishCword(HttpServletRequest request, HttpServletResponse response) throws Exception{
        HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("=,is_stoped",0);
        List<DlpCword> dlpCwordLists = searchDataReadService.getDlpCword(queryParams,1,Integer.MAX_VALUE);
        nlpService.cwordReload(dlpCwordLists);
        for(DlpCword d:dlpCwordLists){
            d.setIsPublished(1);
            try {
                searchDataWriteService.editDlpCword(d);
            }catch (Exception e){

            }
        }
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("type", "true");
        String resultString = formatToJson(map);
        super.pringWriterToPage(resultString, "application/json", response);
    }
    //更改触发词状态
    @SystemControllerLog(description="更改触发词状态")
    @RequestMapping("updateCwordStatus")
    @ResponseBody
    public void updateCwordStatus(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String ids = request.getParameter("id");
        String isStoped = request.getParameter("isStoped");
        String[] idstr = ids.split(",");
        for(String id:idstr){
            DlpCword dlpCword = searchDataReadService.findDlpCword(Integer.parseInt(id));
            dlpCword.setIsStoped(Integer.parseInt(isStoped));
            searchDataWriteService.editDlpCword(dlpCword);
        }
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("type", "true");
        String resultString = formatToJson(map);
        super.pringWriterToPage(resultString, "application/json", response);
    }
    //获取语料统计列表
    @SystemControllerLog(description="获取语料统计列表")
    @RequestMapping("getCorpusNumList")
    @ResponseBody
    public void getCorpusNumList(HttpServletRequest request, HttpServletResponse response) throws Exception{
        List<DlpClassify> dlpClassifyList = searchDataReadService.getDlpClassify();
        List<HashMap<String, Object>> dataList = new ArrayList<HashMap<String, Object>>();
        for(DlpClassify dc:dlpClassifyList){
            HashMap<String, Object> queryParams = new HashMap<String, Object>();
            queryParams.put("=,cnum", dc.getCnum());
            Integer count = searchDataReadService.getDlpCorpusCount(queryParams);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("id", dc.getId());
            map.put("name", dc.getName());
            map.put("cnum", dc.getCnum());
            map.put("isStoped", dc.getIsStoped());
            map.put("detail", dc.getDetail());
            map.put("createTime", dc.getCreateTime());
            map.put("count", count);
            dataList.add(map);
        }

        String resultString = formatToJson(dataList);
        super.pringWriterToPage(resultString, "application/json", response);
    }
    //获取语料列表
    @SystemControllerLog(description="获取语料列表")
    @RequestMapping("getCorpusList")
    @ResponseBody
    public void getCorpusList(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Integer pageNum = Integer.parseInt(request.getParameter("pageNum"));
        Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
        String cnum = request.getParameter("cnum");
        String isPublished = request.getParameter("isPublished");
        HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("^,create_time","desc");
        queryParams.put("=,cnum",cnum);
        if(isPublished!=null && !isPublished.equals("")){
            queryParams.put("=,is_published",isPublished);
        }
        List<DlpCorpus> dlpCorpusList = searchDataReadService.getDlpCorpus(queryParams,pageNum, pageSize);
        Integer count = searchDataReadService.getDlpCorpusCount(queryParams);
        List<HashMap<String, Object>> dataList = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> dataMap = null;
        for(DlpCorpus dc:dlpCorpusList){
            dataMap = new HashMap<String, Object>();
            dataMap.put("id", dc.getId());
            dataMap.put("name", dc.getFileName());
            dataMap.put("size", dc.getFileSize());
            dataMap.put("type", dc.getFileType());
            dataMap.put("path", dc.getFilePath());
            dataMap.put("content", dc.getContent());
            dataMap.put("feature", dc.getFeature());
            dataMap.put("isPublished", dc.getIsPublished());
            String classifyName = "";
            HashMap<String, Object> queryParamc = new HashMap<String, Object>();
            queryParamc.put("=,cnum",dc.getCnum());
            List<DlpClassify> dlpClassifyList = searchDataReadService.getDlpClassify(queryParamc,1,1);
            if(dlpClassifyList.size()>0){
                classifyName = dlpClassifyList.get(0).getName();
            }
            dataMap.put("classifyName", classifyName);
            dataMap.put("isStoped", dc.getIsStoped());
            String createUser ="";
            if(dc.getCreateUid()!=null && !dc.getCreateUid().equals("")){
                createUser = searchDataReadService.findSystemUser(dc.getCreateUid()).getUsername();
            }
            dataMap.put("createUser", createUser);
            dataMap.put("createTime", dc.getCreateTime());
            dataList.add(dataMap);
        }
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("data",dataList);
        data.put("count",(count + pageSize - 1) / pageSize);
        String resultString = formatToJson(data);
        super.pringWriterToPage(resultString, "application/json", response);
    }
    //获取语料详情
    @SystemControllerLog(description="获取语料详情")
    @RequestMapping("getCorpusDetail")
    @ResponseBody
    public void getCorpusDetail(HttpServletRequest request, HttpServletResponse response) throws Exception{
        HashMap<String, Object> data = new HashMap<String, Object>();
        String cnum = request.getParameter("cnum");
        if(cnum!=null && !cnum.equals("")){
            HashMap<String, Object> queryParams = new HashMap<String, Object>();
            queryParams.put("=,cnum",cnum);
            Integer count = searchDataReadService.getDlpCorpusCount(queryParams);
            List<DlpClassify> dlpClassify = searchDataReadService.getDlpClassify(queryParams,1,1);
            String classifyName = "";
            if(dlpClassify.size()>0){
                classifyName = dlpClassify.get(0).getName();
            }
            data.put("classifyName",classifyName);
            data.put("count",count);
        }
        String resultString = formatToJson(data);
        super.pringWriterToPage(resultString, "application/json", response);
    }
    //删除语料
    @SystemControllerLog(description="删除语料")
    @RequestMapping("delCorpus")
    @ResponseBody
    public void delCorpus(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String ids = request.getParameter("id");
        String[] idstr = ids.split(",");
        for(String id:idstr){
            DlpCorpus dlpCorpus = searchDataReadService.findDlpCorpus(Integer.parseInt(id));
            searchDataWriteService.deleteDlpCorpus(dlpCorpus);
        }
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("type", "true");
        String resultString = formatToJson(map);
        super.pringWriterToPage(resultString, "application/json", response);
    }
    //发布语料
    @SystemControllerLog(description="发布语料")
    @RequestMapping("publishCorpus")
    @ResponseBody
    public void publishCorpus(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String cnum = request.getParameter("cnum");

        HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("=,is_stoped",0);
        if(cnum!=null && !cnum.equals("")){
            queryParams.put("=,cnum",cnum);
        }
        List<DlpCorpus> dlpCorpusLists = searchDataReadService.getDlpCorpus(queryParams,1,Integer.MAX_VALUE);
        for(DlpCorpus dc:dlpCorpusLists){
            nlpService.corpusExtract(dc);
            dc.setIsPublished(1);
            try {
                searchDataWriteService.editDlpCorpus(dc);
            }catch (Exception e){

            }
        }
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("type", "true");
        String resultString = formatToJson(map);
        super.pringWriterToPage(resultString, "application/json", response);
    }
    //浏览语料
    @SystemControllerLog(description="浏览语料")
    @RequestMapping("seeCorpus")
    @ResponseBody
    public void seeCorpus(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String id = request.getParameter("id");
        DlpCorpus dlpCorpus = searchDataReadService.findDlpCorpus(Integer.parseInt(id));
        String content = "";
        if(dlpCorpus.getFilePath()!=null && !dlpCorpus.equals("")){
            File f = new File(uploadFilePath+dlpCorpus.getFilePath());
            if(f.exists()){
                content = nlpService.getFileContent(f.getAbsolutePath());
            }
        }
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("type", "true");
        map.put("content", content);
        String resultString = formatToJson(map);
        super.pringWriterToPage(resultString, "application/json", response);
    }
    //获取发布数量
    @SystemControllerLog(description="获取发布数量")
    @RequestMapping("getPublishNum")
    @ResponseBody
    public void getPublishNum(HttpServletRequest request, HttpServletResponse response) throws Exception{
        HashMap<String, Object> data = new HashMap<String, Object>();
        String type = request.getParameter("type");
        String isPublished = request.getParameter("isPublished");
        String cnum = request.getParameter("cnum");
        Integer count = 0;
        if(type!=null && !type.equals("") && isPublished!=null && !isPublished.equals("")){
            HashMap<String, Object> queryParams = new HashMap<String, Object>();
            queryParams.put("=,is_published",isPublished);
            if(type.equals("corpus")){
                queryParams.put("=,cnum",cnum);
                count = searchDataReadService.getDlpCorpusCount(queryParams);
            }else if(type.equals("triggers")){
                count = searchDataReadService.getDlpCwordCount(queryParams);
            }else if(type.equals("term")){
                count = searchDataReadService.getDlpTermCount(queryParams);
            }
        }
        data.put("count",count);
        String resultString = formatToJson(data);
        super.pringWriterToPage(resultString, "application/json", response);
    }
    //查询文件列表
    public static void getFileLists(String path, HashMap<String, Object> data) throws Exception {
        File unFile = new File(path);
        if (unFile.exists()) {
            for (File f : unFile.listFiles()) {
                if (f.isDirectory()) {
                    getFileLists(f.getAbsolutePath()+"/", data);
                } else if(f.getAbsolutePath().matches(".*\\.(txt|pdf|doc|docx)+")){
                    data.put("fileNames",data.get("fileNames")+f.getName() + ";");
                    String size = "";
                    if (f.length() >= 1024 * 1024 * 1024) {
                        size = String.format("%.2f", f.length() / (double) (1024 * 1024 * 1024)) + "GB";
                    } else if (f.length() >= 1024 * 1024) {
                        size = String.format("%.2f", f.length() / (double) (1024 * 1024)) + "MB";
                    } else {
                        size = String.format("%.2f", f.length() / (double) (1024)) + "KB";
                    }
                    data.put("fileSizes",data.get("fileSizes")+size + ";");
                    Thread.sleep(100);
                    String newName = DateUtils.formatDate(new Date(), "yyyyMMddHHmmssSSS") + f.getAbsolutePath().substring(f.getAbsolutePath().lastIndexOf("."), f.getAbsolutePath().length());
                    File newFile = new File(path + newName);
                    f.renameTo(newFile);
                    data.put("filePaths",data.get("filePaths")+newFile.getAbsolutePath() + ";");
                }
            }
        }
    }

    //新增语料
    @SystemControllerLog(description="新增语料")
    @RequestMapping("addCorpus")
    @ResponseBody
    public void addCorpus(HttpServletRequest request, HttpServletResponse response) throws Exception{
        SystemUser systemuser = new SystemUser();
        if(request.getSession().getAttribute("userid")!=null && !request.getSession().getAttribute("userid").equals("")){
            systemuser = searchDataReadService.findSystemUser(Integer.parseInt(request.getSession().getAttribute("userid").toString()));
        }
        String cnum = request.getParameter("cnum");
        String filePath = request.getParameter("filePath");
        String fileName = request.getParameter("fileName");
        String fileSize = request.getParameter("fileSize");
        String filePaths = "";
        String fileNames = "";
        String fileSizes = "";
        LinkedHashMap<String, Object> data = new LinkedHashMap<String, Object>();
        if(filePath.matches(".*\\.(zip)+")){
            String unPath = DateUtils.formatDate(new Date(),"yyyyMMddHHmmss");
            MuliFileUtils.unZip(filePath, uploadFilePath+"/"+unPath);
            HashMap<String, Object> fileMap = new HashMap<String, Object>();
            fileMap.put("fileNames","");
            fileMap.put("fileSizes","");
            fileMap.put("filePaths","");
            getFileLists(uploadFilePath+"/"+unPath+"/", fileMap);
            filePaths = fileMap.get("filePaths").toString();
            fileNames = fileMap.get("fileNames").toString();
            fileSizes = fileMap.get("fileSizes").toString();
        }else{
            filePaths = filePath;
            fileNames = fileName;
            fileSizes = fileSize;
        }
        String[] filePathz = filePaths.split(";");
        String[] fileNamez = fileNames.split(";");
        String[] fileSizez = fileSizes.split(";");
        String descrition = "";
        for(int i=0;i<filePathz.length;i++){
            DlpCorpus dlpCorpus = new DlpCorpus();
            dlpCorpus.setFilePath(filePathz[i].replaceAll("\\\\","/").replaceAll(uploadFilePath,""));
            dlpCorpus.setFileName(fileNamez[i]);
            dlpCorpus.setFileSize(fileSizez[i]);
            dlpCorpus.setFileType(fileNamez[i].substring(fileNamez[i].lastIndexOf(".")+1,fileNamez[i].length()));
            dlpCorpus.setCnum(cnum);
            dlpCorpus.setIsStoped(0);
            dlpCorpus.setIsPublished(0);
            dlpCorpus.setCreateUid(systemuser.getId());
            dlpCorpus.setCreateTime(DateUtils.getDateTime());
            searchDataWriteService.saveDlpCorpus(dlpCorpus);
        }
        data.put("descrition",descrition);
        if(filePath.matches(".*\\.(zip)+")){
            deleteFileMethod(filePath);
        }
        String resultString = formatToJson(data);
        super.pringWriterToPage(resultString, "application/json", response);
    }
    //文件上传
    @SystemControllerLog(description="文件上传")
    @RequestMapping("filesUpload")
    @ResponseBody
    public Map<String, Object> filesUpload(HttpServletRequest request, HttpServletResponse response, @RequestParam("file") MultipartFile file) throws Exception {
        String desc = request.getParameter("desc");
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        HashMap<String, Object> dMap = new HashMap<String, Object>();
        String uuid="";
        String suffix="";
        //PrintWriter out;
        String path ="";
        boolean flag = false;
        if (file.getSize() > 0) {
            if(uploadFilePath.endsWith("/") || uploadFilePath.endsWith("\\")){
                path = uploadFilePath + DateUtils.formatDate(new Date(),"yyyyMMddHHmm");
            }else{
                path = uploadFilePath+"/"+ DateUtils.formatDate(new Date(),"yyyyMMddHHmm");
            }
            File f = new File(path);
            if(!f.exists()){
                f.mkdirs();
            }
            double random = Math.round(Math.random()*100);
            long l = new Double(random).longValue();
            Thread.sleep(l);
            uuid = DateUtils.formatDate(new Date(),"yyyyMMddHHmmssSSS");
            String fileName = file.getOriginalFilename();
            suffix = fileName.substring(fileName.lastIndexOf("."),fileName.length());
            file.transferTo(new File(path + "/" + uuid + suffix));
            flag = true;
        }
        if (flag == true) {
            map.put("code", 0);
            dMap.put("src", path + "/" + uuid + suffix);
            dMap.put("name", file.getOriginalFilename());
            String size = "";
            if(file.getSize()>=1024*1024*1024){
                size = String.format("%.2f", file.getSize()/(double)(1024*1024*1024))+"GB";
            }else if(file.getSize()>=1024*1024){
                size = String.format("%.2f", file.getSize()/(double)(1024*1024))+"MB";
            }else{
                size = String.format("%.2f", file.getSize()/(double)(1024))+"KB";
            }
            dMap.put("size", size);
        } else {
            map.put("code", 1);
            dMap.put("src", "");
            dMap.put("name", "");
            dMap.put("size", "");
        }
        map.put("desc", desc);
        map.put("msg", "success");
        map.put("data", dMap);
        return map;
    }
    /**
     * 删除文件及文件夹
     * @param filePath
     */
    public void deleteFileMethod(String filePath){
        File file = new File(filePath);
        if(file.exists() && file.isFile()){
            boolean deleteResult = file.delete();
            int tryCount = 0;
            while(!deleteResult && tryCount++ <10){
                System.gc();
                deleteResult = file.delete();
            }
        }
        if(file.getParentFile().exists() && file.getParentFile().listFiles().length==0){
            file.getParentFile().delete();
        }
    }
    //获取模型列表
    @SystemControllerLog(description="获取模型列表")
    @RequestMapping("getModelList")
    @ResponseBody
    public void getModelList(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Integer pageNum = Integer.parseInt(request.getParameter("pageNum"));
        Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
        String status = request.getParameter("status");
        HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("^,create_time","desc");
        if(status!=null && !status.equals("")){
            queryParams.put("=,model_status",status);
        }
        List<DlpModel> dlpModelList = searchDataReadService.getDlpModel(queryParams,pageNum, pageSize);
        Integer count = searchDataReadService.getDlpModelCount(queryParams);
        List<HashMap<String, Object>> dataList = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> dataMap = null;
        for(DlpModel dm:dlpModelList){
            dataMap = new HashMap<String, Object>();
            dataMap.put("id", dm.getId());
            dataMap.put("name", dm.getName());
            dataMap.put("version", dm.getVersion());
            dataMap.put("status", dm.getModelStatus());
            dataMap.put("startTime", dm.getStartTime());
            dataMap.put("endTime", dm.getEndTime());
            dataMap.put("detail", dm.getDetail());
            HashMap<String, Object> queryParamc = new HashMap<String, Object>();
            String lables = "";
            if(dm.getLabels()!=null && !dm.getLabels().equals("")){
                String[] lablestr = dm.getLabels().split(";");
                for(String lable:lablestr){
                    queryParamc.put("=,cnum",lable);
                    List<DlpClassify> dlpClassifyList = searchDataReadService.getDlpClassify(queryParamc,1,1);
                    if(dlpClassifyList.size()>0){
                        lables += dlpClassifyList.get(0).getName()+";";
                    }
                }
                if(lables.length()>0){
                    lables = lables.substring(0,lables.length()-1);
                }
            }
            dataMap.put("classifyNames", lables);
            dataMap.put("isStoped", dm.getIsStoped());
            String createUser ="";
            if(dm.getCreateUid()!=null && !dm.getCreateUid().equals("")){
                createUser = searchDataReadService.findSystemUser(dm.getCreateUid()).getUsername();
            }
            dataMap.put("createUser", createUser);
            dataMap.put("createTime", dm.getCreateTime());
            dataList.add(dataMap);
        }
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("data",dataList);
        data.put("count",(count + pageSize - 1) / pageSize);
        String resultString = formatToJson(data);
        super.pringWriterToPage(resultString, "application/json", response);
    }
    //清空模型
    @SystemControllerLog(description="清空模型")
    @RequestMapping("delModel")
    @ResponseBody
    public void delModel(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String ids = request.getParameter("id");
        if(ids!=null && !ids.equals("0")){
            String[] idstr = ids.split(",");
            for(String id:idstr){
                DlpModel dlpModel = searchDataReadService.findDlpModel(Integer.parseInt(id));
                nlpService.modelClear(dlpModel);
                searchDataWriteService.deleteDlpModel(dlpModel);
            }
        }else{
            HashMap<String, Object> queryParams = new HashMap<String, Object>();
            queryParams.put("=,model_status",5);
            List<DlpModel> dlpModelList = searchDataReadService.getDlpModel(queryParams,1,Integer.MAX_VALUE);
            for(DlpModel dm:dlpModelList){
                nlpService.modelClear(dm);
                searchDataWriteService.deleteDlpModel(dm);
            }
        }
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("type", "true");
        String resultString = formatToJson(map);
        super.pringWriterToPage(resultString, "application/json", response);
    }
    //查询模型当前版本号
    @SystemControllerLog(description="查询模型当前版本号")
    @RequestMapping("getModelVersion")
    @ResponseBody
    public void getModelVersion(HttpServletRequest request, HttpServletResponse response) throws Exception{
        HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("^,version","desc");
        List<DlpModel> dlpModelList = searchDataReadService.getDlpModel(queryParams,1,1);
        String version = "1.0";
        if(dlpModelList.size()>0){
            version = String.format("%.1f", Double.parseDouble(dlpModelList.get(0).getVersion())+0.1);
        }
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("version", version);
        String resultString = formatToJson(map);
        super.pringWriterToPage(resultString, "application/json", response);
    }
    //查询密级ztree
    @SystemControllerLog(description="查询密级")
    @RequestMapping("getClassifyZTree")
    @ResponseBody
    public void getClassifyZTree(HttpServletRequest request, HttpServletResponse response) throws Exception{
        HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("^,use_level", "asc");
        List<DlpClassify> dlpClassifyList = searchDataReadService.getDlpClassify(queryParams,1,Integer.MAX_VALUE);
        List<HashMap<String, Object>> r = new ArrayList<HashMap<String, Object>>();
        LinkedHashMap<String, Object> mp = null;
        for (int i = 0; i < dlpClassifyList.size(); i++) {
            mp = new LinkedHashMap<String, Object>();
            mp.put("id", dlpClassifyList.get(i).getId());
            mp.put("name", dlpClassifyList.get(i).getName());
            mp.put("cnum",  dlpClassifyList.get(i).getCnum());
            mp.put("pId", 0);
            r.add(mp);
        }
        String result = formatToJson(r);
        super.pringWriterToPage(result.toString(), "application/json", response);
    }
    //通过id查询密级
    @SystemControllerLog(description="查询密级")
    @RequestMapping("getClassifyByIds")
    @ResponseBody
    public void getClassifyByIds(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String classifyIds = request.getParameter("classifyIds");
        List<String> ids = Arrays.asList(classifyIds.split(";"));
        HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("^,use_level", "asc");
        List<DlpClassify> dlpClassifyList = searchDataReadService.getDlpClassify(queryParams,1,Integer.MAX_VALUE);
//        List<HashMap<String, Object>> r = new ArrayList<HashMap<String, Object>>();
        List<DlpClassify> dlpClassifies = new ArrayList<DlpClassify>();
        for (int i = 0; i < dlpClassifyList.size(); i++) {
            if(ids.contains(dlpClassifyList.get(i).getId().toString())){
                dlpClassifies.add(dlpClassifyList.get(i));
            }
        }
        String result = formatToJson(dlpClassifies);
        super.pringWriterToPage(result.toString(), "application/json", response);
    }

    //查询模型详情
    @SystemControllerLog(description="查询模型详情")
    @RequestMapping("getModelDetail")
    @ResponseBody
    public void getModelDetail(HttpServletRequest request, HttpServletResponse response) throws Exception{
        SystemUser systemuser = new SystemUser();
        if(request.getSession().getAttribute("userid")!=null && !request.getSession().getAttribute("userid").equals("")){
            systemuser = searchDataReadService.findSystemUser(Integer.parseInt(request.getSession().getAttribute("userid").toString()));
        }
//        String id = request.getParameter("id");
        HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("=,create_uid", systemuser.getId());
        queryParams.put("^,id", "desc");
        List<DlpModel> dlpModelList = searchDataReadService.getDlpModel(queryParams,1,1);
        DlpModel dlpModel = new DlpModel();
        if(dlpModelList.size()>0){
            dlpModel = dlpModelList.get(0);
        }
        String lables = "";
        if(dlpModel.getLabels()!=null && !dlpModel.getLabels().equals("")){
            String[] lablestr = dlpModel.getLabels().split(";");
            HashMap<String, Object> queryParamc = new HashMap<String, Object>();
            for(String lable:lablestr){
                queryParamc.put("=,cnum",lable);
                List<DlpClassify> dlpClassifyList = searchDataReadService.getDlpClassify(queryParamc,1,1);
                if(dlpClassifyList.size()>0){
                    lables += dlpClassifyList.get(0).getName()+";";
                }
            }
            if(lables.length()>0){
                lables = lables.substring(0,lables.length()-1);
            }
        }
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("labels",lables);
        map.put("model",dlpModel);
        String result = formatToJson(map);
        super.pringWriterToPage(result.toString(), "application/json", response);
    }
    //取消构建模型
    @SystemControllerLog(description="取消构建模型")
    @RequestMapping("cancelModel")
    @ResponseBody
    public void cancelModel(HttpServletRequest request, HttpServletResponse response) throws Exception{
        SystemUser systemuser = new SystemUser();
        if(request.getSession().getAttribute("userid")!=null && !request.getSession().getAttribute("userid").equals("")){
            systemuser = searchDataReadService.findSystemUser(Integer.parseInt(request.getSession().getAttribute("userid").toString()));
        }
        HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("=,create_uid", systemuser.getId());
        queryParams.put("^,id", "desc");
        queryParams.put("<,model_status", 5);
        List<DlpModel> dlpModelList = searchDataReadService.getDlpModel(queryParams,1,1);
        if(dlpModelList.size()>0){
            DlpModel dlpModel = dlpModelList.get(0);
            JobAndTrigger quartz = new JobAndTrigger();
            quartz.setJobName("model_"+dlpModel.getId());
            quartz.setJobGroup("model");
            delModelJob(quartz);
            nlpService.modelClear(dlpModel);
            searchDataWriteService.deleteDlpModel(dlpModel);
        }
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("type","true");
        String result = formatToJson(map);
        super.pringWriterToPage(result.toString(), "application/json", response);
    }
    //确认构建模型
    @SystemControllerLog(description="确认构建模型")
    @RequestMapping("confirmModel")
    @ResponseBody
    public void confirmModel(HttpServletRequest request, HttpServletResponse response) throws Exception{
        SystemUser systemuser = new SystemUser();
        if(request.getSession().getAttribute("userid")!=null && !request.getSession().getAttribute("userid").equals("")){
            systemuser = searchDataReadService.findSystemUser(Integer.parseInt(request.getSession().getAttribute("userid").toString()));
        }
        HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("=,create_uid", systemuser.getId());
        queryParams.put("^,id", "desc");
        queryParams.put("=,model_status", 3);
        List<DlpModel> dlpModelList = searchDataReadService.getDlpModel(queryParams,1,1);
        if(dlpModelList.size()>0){
            DlpModel dlpModel = dlpModelList.get(0);
            dlpModel.setModelStatus(5);
            searchDataWriteService.editDlpModel(dlpModel);
        }
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("type","true");
        String result = formatToJson(map);
        super.pringWriterToPage(result.toString(), "application/json", response);
    }
    //构建模型
    @SystemControllerLog(description="构建模型")
    @RequestMapping("addModel")
    @ResponseBody
    public void addModel(HttpServletRequest request, HttpServletResponse response) throws Exception{
        SystemUser systemuser = new SystemUser();
        if(request.getSession().getAttribute("userid")!=null && !request.getSession().getAttribute("userid").equals("")){
            systemuser = searchDataReadService.findSystemUser(Integer.parseInt(request.getSession().getAttribute("userid").toString()));
        }
        String classifyCnums = request.getParameter("classifyCnums");
        String version = request.getParameter("version");
        DlpModel dlpModel = new DlpModel();
        dlpModel.setVersion(version);
        dlpModel.setLabels(classifyCnums);
        dlpModel.setModelStatus(0);
        dlpModel.setIsStoped(0);
        dlpModel.setCreateUid(systemuser.getId());
        dlpModel.setCreateTime(DateUtils.getDateTime());
        searchDataWriteService.saveDlpModel(dlpModel);
        JobAndTrigger quartz = new JobAndTrigger();
        quartz.setJobName("model_"+dlpModel.getId());
        quartz.setJobGroup("model");
        quartz.setDescription("模型构建");
        quartz.setJobClassName("org.cloud.framework.job.task.ModelTrainTaskJob");
        JobDataMap jobDataMap = new JobDataMap();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("modelId", dlpModel.getId());
        jobDataMap.put("data", map);
        saveModelJob(quartz,jobDataMap);
        String resultString = formatToJson(map);
        super.pringWriterToPage(resultString, "application/json", response);
    }
    //创建任务
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public void saveModelJob(JobAndTrigger quartz, JobDataMap jobDataMap){
        try {
            if (quartz.getOldJobGroup() != null) {
                JobKey key = new JobKey(quartz.getOldJobName(), quartz.getOldJobGroup());
                scheduler.deleteJob(key);
            }
            Class cls = Class.forName(quartz.getJobClassName());
            cls.newInstance();
            /*** 构建job信息 */
            JobDetail job = JobBuilder.newJob(cls).setJobData(jobDataMap).withIdentity(quartz.getJobName(), quartz.getJobGroup())
                    .withDescription(quartz.getDescription()).build();
            /*** 触发时间点 */
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("trigger" + quartz.getJobName(), quartz.getJobGroup()).startNow()
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(3).withRepeatCount(0)).build();
            /**
             * *交由Scheduler安排触发
             **/
            scheduler.scheduleJob(job, trigger);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //移除任务
    public void delModelJob(JobAndTrigger quartz){
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(quartz.getJobName(), quartz.getJobGroup());
            /*** 停止触发器 */
            scheduler.pauseTrigger(triggerKey);
            /** 移除触发器 */
            scheduler.unscheduleJob(triggerKey);
            /*** 删除任务 */
            scheduler.deleteJob(JobKey.jobKey(quartz.getJobName(), quartz.getJobGroup()));
            System.out.println("removeJob:" + JobKey.jobKey(quartz.getJobName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
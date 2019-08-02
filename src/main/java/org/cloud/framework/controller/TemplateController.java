package org.cloud.framework.controller;

import net.sf.json.JSONObject;
import org.cloud.framework.ueditor.ActionEnter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Controller
@RequestMapping("/")
public class TemplateController {

    /*@SystemControllerLog(description="通用页面跳转日志")*/
    @RequestMapping("/{siteName}/{pageName}.html")
    public String webTemplate(@PathVariable("siteName") String siteName,
                              @PathVariable("pageName") String pageName, ModelMap model)
            throws IOException {

//        //新增方法
//        Map<String, Object> dynamicData = new HashMap<>();
//        dynamicData.put("bt","计算机技术不错new");
//        dynamicData.put("gjc","科技;计算机;信息new");
//        dynamicData.put("zy","我是一个大学生，学习计算机不错new。");
//        dynamicData.put("op_jg","json字符串new");
//        dynamicService.addDynamicData("rw", dynamicData, "2018-07-21 22:22:22","11","0","1.0","init");
//
//        //编辑方法
//        Map<String, Object> dynamicData1 = new HashMap<>();
//        dynamicData1.put("bt","计算机技术不错1");
//        dynamicData1.put("gjc","科技;计算机;信息1");
//        dynamicData1.put("zy","我是一个大学生，学习计算机不错。1");
//        dynamicData1.put("op_jg","json字符串1");
//
//        dynamicService.editDynamicData("rw", dynamicData1, "2018-07-21 22:33:33", "11", "1", "1.1", "3986622372628480");

//        //查询单个
//        Map<String,Object> map = dynamicService.findDynamicData("rw","3986622372628480");
//        System.out.println(map);
//
//        //删除方法
//        Map<String, Object> dynamicData2 = new HashMap<>();
//        dynamicData2.put("id","3986622372628480");
//        dynamicService.deleteDynamicData("rw",dynamicData2);
//
//        //查询总数
//        long r1 = dynamicService.getDynamicCount("rw",null);
//        System.out.println(r1);
//
//        //查询列表
//        List<Map<String, Object>> r2 = dynamicService.getDynamicDataList("rw",null,1,10);
//        System.out.println(r2);

        // System.out.println("siteName=" + siteName + "/pageName=" + pageName);

//        //单条发布
//        Map<String,Object> map = dynamicService.findDynamicData("rw","4000883495094272");
//        System.out.println(map);
//        solrService.submitDynamicData("rw", map, "2018-07-31 17:17:17", "admin", "1");

        //批量发布
//        List<Map<String, Object>> r2 = dynamicService.getDynamicDataList("rw",null,1,10);
//        solrService.submitDynamicData("rw", r2, "2018-07-31 17:17:17", "admin", "1");

        //单库发布
//        solrService.submitDynamicData("rw","2018-08-16 22:17:17", "anhuifeng");
//
//        //分面
//        HashMap<String, Object> r7 =solrService.searchFacet("$title:计算机",null,new String[]{"keywords"},10);
//        System.out.println("分面=" + r7);
//
//        //查询详情
//        HashMap<String, Object> r4 = solrService.searchEntity("4000883495094272",new String[]{"id","title"});
//        System.out.println(r4);

        //通过名称匹配所有节点，返回节点展示信息
//        List<Record> nodeList = neo4jService.matchAllNodes("战斗机",
//                new String[]{"n.name as aName","n.id as aId","n.imagePath as aImagePath","n.label as aLabel"}, 1000);
//        for (int i = 0; i < nodeList.size(); i++) {
//            Record aRecord = nodeList.get(i);
//            System.out.println(aRecord);
//        }

        //#获取网站文件变量
        model.addAttribute("ocFilePath", "/" + siteName);

        return  siteName + "/" + pageName;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/ueditorStore")
    public void ueditorStore(HttpServletRequest request, HttpServletResponse response) throws Exception{
        try {
            response.setContentType("application/json");
            request.setCharacterEncoding( "utf-8" );
            response.setHeader("Content-Type" , "text/html");
            String rootPath = request.getSession().getServletContext().getRealPath("/");
//            String a = request.getRequestURI();
            String exec = new ActionEnter(request, rootPath).exec();
            JSONObject jb = JSONObject.fromObject(exec);
            Map map = (Map)jb;
            for (Object key : map.keySet()) {
                String url ="/upload"+map.get(key).toString();
                if("url".equals(key)){
                    map.put(key, url);
                }
            }
            exec = JSONObject.fromObject(map).toString();
            PrintWriter writer = response.getWriter();
            writer.write(exec);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 测试beetl模板
     *
     * @return
     */
    @RequestMapping("/add")
    public ModelAndView home() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("email", "1111@1631111.com||||");
        modelAndView.setViewName("add");

        return modelAndView;
    }

}

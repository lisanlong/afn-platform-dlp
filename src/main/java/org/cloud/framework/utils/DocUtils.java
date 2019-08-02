package org.cloud.framework.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocUtils {

    private Configuration configuration = null;

    public DocUtils() {
        configuration = new Configuration();
        configuration.setDefaultEncoding("utf-8");
    }

    public void createDocByFos(Map<String,Object> dataMap, OutputStream fos) throws UnsupportedEncodingException {
        //dataMap 要填入模本的数据文件
        //设置模本装置方法和路径,FreeMarker支持多种模板装载方法。可以重servlet，classpath，数据库装载，
        //这里我们的模板是放在template包下面
        configuration.setClassForTemplateLoading(this.getClass(), "/static/template");
        Template t = null;
        try {
            //test.ftl为要装载的模板
            t = configuration.getTemplate("5.ftl");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Writer out = null;
        OutputStreamWriter oWriter = new OutputStreamWriter(fos, "UTF-8");
        //这个地方对流的编码不可或缺，使用main（）单独调用时，应该可以，但是如果是web请求导出时导出后word文档就会打不开，并且包XML文件错误。主要是编码格式不正确，无法解析。
        //out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)));
        out = new BufferedWriter(oWriter);

        try {
            t.process(dataMap, out);
            out.close();
            fos.close();
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //System.out.println("---------------------------");
    }

    public void createDoc(Map<String,Object> dataMap, String fileName) throws UnsupportedEncodingException {
        //dataMap 要填入模本的数据文件
        //设置模本装置方法和路径,FreeMarker支持多种模板装载方法。可以重servlet，classpath，数据库装载，
        //这里我们的模板是放在template包下面
        configuration.setClassForTemplateLoading(this.getClass(), "/static/template");
        Template t = null;
        try {
            //test.ftl为要装载的模板
            t = configuration.getTemplate("5.ftl");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //输出文档路径及名称
        File outFile = new File(fileName);
        Writer out = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(outFile);
            OutputStreamWriter oWriter = new OutputStreamWriter(fos, "UTF-8");
            //这个地方对流的编码不可或缺，使用main（）单独调用时，应该可以，但是如果是web请求导出时导出后word文档就会打不开，并且包XML文件错误。主要是编码格式不正确，无法解析。
            //out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)));
            out = new BufferedWriter(oWriter);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        try {
            t.process(dataMap, out);
            out.close();
            fos.close();
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //System.out.println("---------------------------");
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("taskName", "文件夹监测任务");
//        dataMap.put("reportTime", "201907262006");
//        dataMap.put("deptName", "保密部");
        dataMap.put("username", "保密部");
        dataMap.put("checkTime", "201907112006");
        dataMap.put("totalCheckNum", 100);
        dataMap.put("totalWarnNum", 2);
        dataMap.put("realWarnNum", 1);
        dataMap.put("qualifiedNum", 3);
        dataMap.put("rangeCheck", "秘密 机密 绝密 公开");

        List<HashMap<String, Object>> recordList = new ArrayList<>();

        HashMap<String, Object> map = new HashMap<>();

        map.put("index", 1);
        map.put("filename", "北京");
        map.put("orginLabel", "秘密");
        map.put("checkLabel", "绝密");
        map.put("reviewLabel", "公开");
        map.put("fileOwner", "六二狗");
        map.put("fileDept", "六二狗");
        recordList.add(map);
        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("index", 2);
        map2.put("filename", "北京2");
        map2.put("orginLabel", "秘密2");
        map2.put("checkLabel", "绝密2");
        map2.put("reviewLabel", "公开2");
        map2.put("fileOwner", "六二狗2");
        map2.put("fileDept", "六二狗2");
        recordList.add(map2);

        dataMap.put("recordList", recordList);

        DocUtils docUtils = new DocUtils();
        docUtils.createDoc(dataMap, "D:/2.doc");
    }

}

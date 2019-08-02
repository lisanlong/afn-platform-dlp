package org.cloud.framework.handling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Scope("prototype")
public class SQLManager_QKLW {

//    ISearchService solrService = (ISearchService) SpringUtil.getBean("solrService");
    private static transient Logger logger = LoggerFactory.getLogger(SQLManager_QKLW.class);

    public void build() {

        int number = 10000; //每组记录数
        long startTimeClassesTotal = System.currentTimeMillis();
        //从文件读取id值
        String id= "cadcamyzzyxxh200807001";
        if(id==null || "".equals(id)){
            id="0";
        }
        logger.info("起始id：" + id);
        long startTime = System.currentTimeMillis();
        List<Map<String, Object>> dynamicData = getList(id, number);
        long endTime = System.currentTimeMillis();
        logger.info("本组取数据时间：" + (endTime - startTime) + "ms");
        while (dynamicData != null && dynamicData.size()>0) {

            //数据组装发布时间
            startTime = System.currentTimeMillis();

            //solrService.indexDynamicData("qklw",dynamicData,DateUtils.getDateTime(),"12","0");

            endTime = System.currentTimeMillis();
            logger.info("本组数据组装发布时间：" + (endTime - startTime) + "ms\t结束id："+id);


            id = dynamicData.get(dynamicData.size()-1).get("id").toString();
            //保存id值🈯️到文件
            dynamicData = null;

            startTime = System.currentTimeMillis();
            dynamicData = getList(id, number);
            endTime = System.currentTimeMillis();
            logger.info("查询下一组时间：" + (endTime - startTime) + "ms\t结束id："+id);
        }
        //最后一组手动提交
//        solrService.commitIndex();
        long endTimeClassesTotal = System.currentTimeMillis();
        logger.info("本组创建完成：" + (endTimeClassesTotal - startTimeClassesTotal) + "ms\t结束id："+id);

    }


    public List<Map<String, Object>> getList(String id, int number) {

        List<Map<String, Object>> r = new ArrayList<>();

        String sql = " select article_id,article_title,en_title,doi,authors_name,authors_unit,publish_year,orig_abstract,orig_keys,orig_classcode,perio_title "
                + " from perio_artical_002 where article_id>? order by article_id asc limit ? ";
//        StringBuilder builder = new StringBuilder();

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=false", "root",
                    "ninemax");
            ps = con.prepareStatement(sql);
            ps.setString(1, id);
            ps.setInt(2, number);
            rs = ps.executeQuery();

            Map<String, Object> map = null;
            while (rs.next()) {

                map = new HashMap<String, Object>();
                String article_id = rs.getString("article_id");
                String article_title = rs.getString("article_title");
                String en_title = rs.getString("en_title");
                String doi = rs.getString("doi");
                String authors_name = rs.getString("authors_name");
                String authors_unit = rs.getString("authors_unit");
                String publish_year = rs.getString("publish_year");
                String orig_abstract = rs.getString("orig_abstract");
                String orig_keys = rs.getString("orig_keys");
                String orig_classcode = rs.getString("orig_classcode");
                String perio_title = rs.getString("perio_title");

                map.put("id",article_id);

                if(null!=rs.getString("article_title")) {
                    map.put("title",article_title);
                }
                if(null!=rs.getString("en_title")) {
                    map.put("ywmc",en_title);
                }
                if(null!=rs.getString("doi")) {
                    map.put("doi",doi);
                }
                if(null!=rs.getString("authors_name")) {
                    map.put("author",authors_name);
                }
                if(null!=rs.getString("authors_unit")) {
                    map.put("author_unit",authors_unit);
                }
                if(null!=rs.getString("publish_year")) {
                    map.put("year",publish_year);
                }
                if(null!=rs.getString("orig_keys")) {
                    map.put("keywords",orig_keys);
                }
                if(null!=rs.getString("orig_classcode")) {
                    map.put("ztffl",orig_classcode);
                }
                if(null!=rs.getString("perio_title")) {
                    map.put("kmc",perio_title);
                }
                if(null!=rs.getString("orig_abstract")) {
                    map.put("summary",orig_abstract);
                }

//                for (int i = 1; i <= data.getColumnCount(); i++) {
//                    String columnName = data.getColumnLabel(i);
//                    if(rs.getObject(i) != null){
//                        map.put(columnName, UnicodeConverter.unicode2String(String.valueOf(rs.getObject(i))));
//                    }
//                }

                r.add(map);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (rs != null)
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            if (ps != null)
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            if (con != null)
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            rs = null;
            ps = null;
            con = null;

        }
        return r;
    }

    public static void main(String[] args) {

        SQLManager_QKLW aSQLManager = new SQLManager_QKLW();
        long startTimeTotal = System.currentTimeMillis();
        aSQLManager.build();
        long endTimeTotal = System.currentTimeMillis(); // 获取结束时间
        System.out.println("数据处理总时间：" + (endTimeTotal - startTimeTotal) + "ms");

    }

}

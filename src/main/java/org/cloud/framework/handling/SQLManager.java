package org.cloud.framework.handling;

import org.apache.commons.lang.StringUtils;
import org.cloud.framework.utils.DateUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.*;

public class SQLManager {

    public void build() {

        int totalCount = getCount(); // 总记录数
        int groupLenth = 10000; // 每组记录数
        int groupCount = totalCount / groupLenth; // 组数
        System.out.println("总数totalCount=" + totalCount);
        System.out.println("每组记录数groupLenth=" + groupLenth);
        System.out.println("组数groupCount=" + groupCount);
        for (int i = 0; i < groupCount; i++) {
            long startTime = System.currentTimeMillis(); // 获取开始时间
            getList((i + 1), groupLenth);
            System.out.println(i + "/" + groupCount);
            long endTime = System.currentTimeMillis(); // 获取结束时间
            System.out.println("第" + (i + 1) + "组数据处理时间："
                    + (endTime - startTime) + "ms");
        }
        // 不足一整组
        if (totalCount % groupLenth > 0) {
            long startTime = System.currentTimeMillis(); // 获取开始时间
            getList((groupCount + 1), totalCount % groupLenth);
            long endTime = System.currentTimeMillis(); // 获取结束时间
            System.out.println("最后一组数据处理时间：" + (endTime - startTime) + "ms");
        }
        System.out.println(">>>>处理完毕~");

    }

    public int getCount() {
        int r = 0;
        String sql = " select count(1) as count from hymax_news ";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=false", "root",
                    "ninemax");
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                r = rs.getInt("count");
            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
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

    //转义字符
    public String escapeSpecialChar(String keyword) {
        if (StringUtils.isNotBlank(keyword)) {
            String[] fbsArr = {"\\", "$", "|", "%", "_", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}"};
            for (String key : fbsArr) {
                if (keyword.contains(key)) {
                    keyword = keyword.replace(key, "\\" + key);
                }
            }
        }

        return keyword;

    }

    public void getList(int pageNumber, int pageSize) {

        String sql = " select id,title,abstracts,source,content,keywords,source_url from hymax_news order by id limit ?,? ";
        StringBuilder builder = new StringBuilder();

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        InputStream is = null;

        try {
            con = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=false", "root",
                    "ninemax");
            ps = con.prepareStatement(sql);
            ps.setInt(1, (pageNumber - 1) * pageSize);
            ps.setInt(2, pageSize);
            rs = ps.executeQuery();

            while (rs.next()) {


                builder.append(rs.getString("id"));
                builder.append("@@@@@");

                String title = rs.getString("title");
                if(title==null) {
                    builder.append("");
                    builder.append("@@@@@");
                }else {
                    builder.append(escapeSpecialChar(title));
                    builder.append("@@@@@");
                }

                String abstracts = rs.getString("abstracts");
                if(abstracts==null) {
                    builder.append("");
                    builder.append("@@@@@");
                }else {
                    builder.append(escapeSpecialChar(abstracts));
                    builder.append("@@@@@");
                }

                String source = rs.getString("source");
                if(source==null) {
                    builder.append("");
                    builder.append("@@@@@");
                }else {
                    builder.append(escapeSpecialChar(source));
                    builder.append("@@@@@");
                }

                String content = rs.getString("content");
                if(content==null) {
                    builder.append("");
                    builder.append("@@@@@");
                }else {
                    builder.append(escapeSpecialChar(content));
                    builder.append("@@@@@");
                }

                String keywords = rs.getString("keywords");
                if(keywords==null) {
                    builder.append("");
                    builder.append("@@@@@");
                }else {
                    builder.append(escapeSpecialChar(keywords));
                    builder.append("@@@@@");
                }

                String source_url = rs.getString("source_url");
                if(source_url==null) {
                    builder.append("");
                    builder.append("@@@@@");
                }else {
                    builder.append(escapeSpecialChar(source_url));
                    builder.append("@@@@@");
                }

                builder.append("11");
                builder.append("@@@@@");
                builder.append(DateUtils.getDateTime());
                builder.append("@@@@@");
                builder.append("3");
                builder.append("@@@@@");
                builder.append("1.0");
                builder.append("@@@@@");
                builder.append("1");
                builder.append("@@@@@");
                builder.append("0");
                builder.append("#####");

            }

            byte[] bytes = builder.toString().getBytes("utf-8");
            is = new ByteArrayInputStream(bytes);
            // 提交
            System.out.println("is=" + is);

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

        // select id,title,abstracts,source,content,keywords,source_url
        String loadDataSql = "LOAD DATA LOCAL INFILE 'sql.csv' IGNORE INTO TABLE "
                + "bs_xwzx fields terminated by '@@@@@' lines terminated by '#####' (id,title,summary,ly,content,keywords,url,create_user_id,create_time,database_id,version,data_source,status)";

        Connection con_is = null;

        try {
            con_is = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/cp_store?useUnicode=true&characterEncoding=utf-8&useSSL=false", "root",
                    "ninemax");

            PreparedStatement statement = con_is.prepareStatement(loadDataSql);

            int result = 0;

            if (statement.isWrapperFor(com.mysql.jdbc.Statement.class)) {

                com.mysql.jdbc.PreparedStatement mysqlStatement = statement
                        .unwrap(com.mysql.jdbc.PreparedStatement.class);

                mysqlStatement.setLocalInfileInputStream(is);
                result = mysqlStatement.executeUpdate();
            }

            System.out.println("importing " + result + " rows data into mysql ");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con_is != null)
                try {
                    con_is.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            con_is = null;
        }

    }

    public static void main(String[] args) {

        SQLManager aSQLManager = new SQLManager();
        long startTimeTotal = System.currentTimeMillis();
        aSQLManager.build();
        long endTimeTotal = System.currentTimeMillis(); // 获取结束时间
        System.out
                .println("数据处理总时间：" + (endTimeTotal - startTimeTotal) + "ms");

    }

}

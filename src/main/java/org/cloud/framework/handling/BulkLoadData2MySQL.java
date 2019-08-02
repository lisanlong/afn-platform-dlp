package org.cloud.framework.handling;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import org.apache.log4j.Logger;


public class BulkLoadData2MySQL {

    private static final Logger logger = Logger.getLogger(BulkLoadData2MySQL.class);
    private JdbcTemplate jdbcTemplate;
    private Connection conn = null;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public static InputStream getTestDataInputStream() {
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i <= 10; i++) {
            for (int j = 0; j <= 10000; j++) {

                builder.append(4);
                builder.append("\t");
                builder.append(4 + 1);
                builder.append("\t");
                builder.append(4 + 2);
                builder.append("\t");
                builder.append(4 + 3);
                builder.append("\t");
                builder.append(4 + 4);
                builder.append("\t");
                builder.append(4 + 5);
                builder.append("\n");
            }
        }
        byte[] bytes = builder.toString().getBytes();
        InputStream is = new ByteArrayInputStream(bytes);
        return is;
    }

    /**
     * load bulk data from InputStream to MySQL
     */
    public int bulkLoadFromInputStream(String loadDataSql,
                                       InputStream dataStream) throws SQLException {
        if (dataStream == null) {
            logger.info("InputStream is null ,No data is imported");
            return 0;
        }
        conn = jdbcTemplate.getDataSource().getConnection();
        PreparedStatement statement = conn.prepareStatement(loadDataSql);

        int result = 0;

        if (statement.isWrapperFor(com.mysql.jdbc.Statement.class)) {

            com.mysql.jdbc.PreparedStatement mysqlStatement = statement
                    .unwrap(com.mysql.jdbc.PreparedStatement.class);

            mysqlStatement.setLocalInfileInputStream(dataStream);
            result = mysqlStatement.executeUpdate();
        }
        return result;
    }

    public static void main(String[] args) {
        String testSql = "LOAD DATA LOCAL INFILE 'sql.csv' IGNORE INTO TABLE "
                + "test.hymax_news (id,status,title,date,abstracts,author,language,mesh_headings,source,year,content,category,keywords,source_url)";
        InputStream dataStream = getTestDataInputStream();
        BulkLoadData2MySQL dao = new BulkLoadData2MySQL();
        try {
            long beginTime = System.currentTimeMillis();
            int rows = dao.bulkLoadFromInputStream(testSql, dataStream);
            long endTime = System.currentTimeMillis();
            logger.info("importing " + rows + " rows data into mysql and cost " + (endTime - beginTime) + " ms!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.exit(1);
    }

}

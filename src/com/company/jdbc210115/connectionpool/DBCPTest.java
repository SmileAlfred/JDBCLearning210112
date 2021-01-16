package com.company.jdbc210115.connectionpool;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class DBCPTest {

    @Test
    public void testGetConnection() {
        Connection conn = null;
        try {
            //创建了DBCP的数据库连接池
            BasicDataSource source = new BasicDataSource();

            //设置基本信息
            source.setDriverClassName("com.mysql.jdbc.Driver");
            source.setUrl("jdbc:mysql:///test");
            source.setUsername("root");
            source.setPassword("1234");

            //还可以设置其他涉及数据库连接池管理的相关属性：
            source.setInitialSize(10);
            source.setMaxActive(10);
            //。。。

            conn = source.getConnection();
        } catch (Exception e) {
            System.out.println("失败1 ：" + e.getMessage());
        }
        System.out.println(conn);
    }

    //方式二：推荐：使用配置文件
    @Test
    public void testGetConnection1() throws Exception {
        Properties pros = new Properties();

        //方式1：
        //InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
        //方式2：
        FileInputStream is = new FileInputStream(new File("src/dbcp.properties"));


        pros.load(is);
        DataSource source = BasicDataSourceFactory.createDataSource(pros);

        Connection conn = source.getConnection();
        System.out.println(conn);
    }


}

package com.company.jdbc210112.test2_preparedstatement.utils;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * 操作数据库的操作类
 */
public class JDBCUtiils {

    public static Connection getConnection() {

        PreparedStatement ps = null;
        Connection connection = null;
        try {
            //1.读取配置文件中的 4 各级本心
            InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");

            Properties properties = new Properties();
            properties.load(inputStream);
            String url = properties.getProperty("url");
            String user = properties.getProperty("user");
            String password = properties.getProperty("password");
            String driverClass = properties.getProperty("driverClass");

            //2. 加载驱动
            Class.forName(driverClass);

            //3. 获取连接
            connection = DriverManager.getConnection(url, user, password);
            return connection;
        } catch (Exception e) {
            System.out.println("getConnection 出错：" + e.getMessage());
            return null;
        }
    }

    /**
     * 关闭资源的操作
     * @param ps
     * @param connection
     */
    public static void closeConnection(PreparedStatement ps, Connection connection) {
        try {
            if (ps != null) ps.close();
            if (connection != null) connection.close();
        } catch (SQLException throwables) {
            System.out.println("连接关闭失败：" + throwables.getMessage());
        }
    }

    public static void closeConnection(PreparedStatement ps, Connection connection, ResultSet resultSet) {
        try {
            if (ps != null) ps.close();
            if (connection != null) connection.close();
            if(resultSet!=null)resultSet.close();
        } catch (SQLException throwables) {
            System.out.println("连接关闭失败：" + throwables.getMessage());
        }
    }
}
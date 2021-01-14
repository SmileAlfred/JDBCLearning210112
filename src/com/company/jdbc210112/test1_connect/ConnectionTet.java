package com.company.jdbc210112.test1_connect;

import org.junit.Test;

import javax.sound.midi.Soundbank;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionTet {

    @Test
    public void testConnection1() {
        Driver driver = null;
        /**
         * jdbc:mysql   主协议及子协议
         * localhost    地址IP
         * 3306         端口号
         * test         数据库
         */
        String url = "jdbc:mysql://localhost:3306/test";
        //将用户名和密码封装到 Properties 中
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "1234");

        try {
            //获取 Driver 的实现类对象
            driver = new com.mysql.jdbc.Driver();

            Connection connection = driver.connect(url, info);
            System.out.println("testConnection1 :" + connection);
        } catch (SQLException throwables) {
            System.out.println("testConnection1 报错：" + throwables.getMessage());
        }
    }

    /**
     * 连接方式二，不是另一种连接方式，而是对 方式一 的迭代
     * 通过反射动态的实现 对 Driver 的实现；
     * 目的：在程序中不出现 第三方的 API,解决更换数据库之后的可移植性
     */
    @Test
    public void testConnection2() {
        try {
            //1. 获取 Driver 的实现类对象；使用反射
            Class clazz = Class.forName("com.mysql.jdbc.Driver");
            Driver driver = (Driver) clazz.newInstance();

            //2. 提供要连接的数据库：
            String url = "jdbc:mysql://localhost:3306/test";

            //3. 提供连接用的 用户名和密码
            Properties info = new Properties();
            info.setProperty("user", "root");
            info.setProperty("password", "1234");

            //4. 获取连接
            Connection connection = driver.connect(url, info);
            System.out.println("testConnection2 : " + connection);
        } catch (Exception e) {
            System.out.println("testConnection2 报错：" + e.getMessage());
        }
    }

    /**
     * 连接方式三，不是另一种连接方式，而是对 方式二 的迭代
     * 通过 DriverManager 替换 Driver；
     */
    @Test
    public void testConnection3() {
        try {
            //1. 提供其他三个 连接的基本信息
            String url = "jdbc:mysql://localhost:3306/test",
                    user = "root",
                    password = "1234";

            //2. 获取 Driver 的实现类对象；使用反射
            Class clazz = Class.forName("com.mysql.jdbc.Driver");
            Driver driver = (Driver) clazz.newInstance();

            //3. 注册驱动
            DriverManager.registerDriver(driver);

            //4. 获取连接
            Connection connection = DriverManager.getConnection(url, user, password);
            System.out.println("testConnection3 : " + connection);
        } catch (Exception e) {
            System.out.println("testConnection3 报错：" + e.getMessage());
        }
    }

    /**
     * 连接方式四，不是另一种连接方式，而是对 方式三 的迭代
     * 取消注册驱动，因为 MySQL 中 Driver 的实现类中，静态代码块（随累的加载而加载）已经实现了注册
     */
    @Test
    public void testConnection4() {
        try {
            //1. 提供其他三个 连接的基本信息
            String url = "jdbc:mysql://localhost:3306/test",
                    user = "root",
                    password = "1234";

            //2. 加载 Driver
            Class.forName("com.mysql.jdbc.Driver");

            //3. 获取连接
            Connection connection = DriverManager.getConnection(url, user, password);
            System.out.println("testConnection4 : " + connection);
        } catch (Exception e) {
            System.out.println("testConnection4 报错：" + e.getMessage());
        }
    }

    /**
     * 连接方式五（final），不是另一种连接方式，而是对 方式四 的迭代
     * 将 4 个 连接参数写道配置文件，通过读取配置文件的方式获取连接
     * 实现了 代码 和 数据 的分离，提高了 代码移植性！
     * 如果需要修该 配置文件信息，避免程序打包
     */
    @Test
    public void testConnection5() {
        try {
            //1.读取配置文件中的 4 各级本心
            InputStream inputStream = ConnectionTet.class.getClassLoader().getResourceAsStream("jdbc.properties");

            Properties properties = new Properties();
            properties.load(inputStream);
            String url = properties.getProperty("url");
            String user = properties.getProperty("user");
            String password = properties.getProperty("password");
            String driverClass = properties.getProperty("driverClass");

            //2. 加载驱动
            Class.forName(driverClass);

            //3. 获取连接
            Connection connection = DriverManager.getConnection(url, user, password);
            System.out.println("testConnection5 : " + connection);
        } catch (Exception e) {
            System.out.println("testConnection5 报错：" + e.getMessage());
        }
    }


}













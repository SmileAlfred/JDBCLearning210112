package com.company.jdbc210115.dbutils;

import com.company.jdbc210112.test2_preparedstatement.bean.Customer;
import com.company.jdbc210115.connectionpool.JDBCConnectUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.*;
import org.junit.Test;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.ResponseHandler;

import javax.xml.transform.Source;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map;

/**
 * commons-dbutils 是 Apache 组织提供的一个开源 JDBC工具类库,封装了针对于数据库的增删改查操作
 */
public class QueyRunnerTest {

    //测试插入
    @Test
    public void testInsert() {
        Connection connection = null;
        try {
            QueryRunner queryRunner = new QueryRunner();
            connection = JDBCConnectUtils.getConnection3();
            String sql = "insert into customers(id,name,email,birth)values(?,?,?,?)";

            int result = queryRunner.update(connection, sql, 77, "CXK", "cxk@126.com", "1998-05-03");
            String hint = result > 0 ? "插入成功" : "插入失败";
            System.out.println(hint);
        } catch (Exception e) {
            System.out.println("插入报错：" + e.getMessage());
        } finally {
            JDBCConnectUtils.closeResource(connection, null);
        }
    }

    //测试查询:BeanHander:是ResultSetHandler接口的实现类，用于封装表中的一条记录。
    @Test
    public void testQuery1() throws Exception {
        Connection connection = null;
        try {
            connection = JDBCConnectUtils.getConnection3();
            QueryRunner queryRunner = new QueryRunner();
            String sql = "select id,name,email,birth date from customers where id = ?";
            BeanHandler<Customer> customerBeanHandler = new BeanHandler<Customer>(Customer.class);

            Customer customer = queryRunner.query(connection, sql, customerBeanHandler, 77);
            System.out.println(customer);
        } catch (Exception e) {
        } finally {
            JDBCConnectUtils.closeResource(connection, null);
        }
    }

    //测试查询:BeanListHandler:是 ResultSetHandler接口的实现类，用于封装表中的多条记录构成的集合。
    @Test
    public void testQuery2() {
        Connection connection = null;
        try {
            connection = JDBCConnectUtils.getConnection3();
            QueryRunner queryRunner = new QueryRunner();
            String sql = "select id,name,email,birth date from customers where id < ?";
            BeanListHandler<Customer> customerBeanListHandler = new BeanListHandler<Customer>(Customer.class);

            List<Customer> customers = queryRunner.query(connection, sql, customerBeanListHandler, 77);
            customers.forEach(System.out::println);
        } catch (Exception e) {
        } finally {
            JDBCConnectUtils.closeResource(connection, null);
        }
    }


    //MapHander:是ResultSetHandler接口的实现类，对应表中的一条记录。
    //将字段及相应字段的值作为map中的key和value
    @Test
    public void testQuery3() throws Exception {
        Connection connection = null;
        try {
            connection = JDBCConnectUtils.getConnection3();
            QueryRunner queryRunner = new QueryRunner();
            String sql = "select id,name,email,birth date from customers where id = ?";
            MapHandler handler = new MapHandler();

            Map<String, Object> customer = queryRunner.query(connection, sql, handler, 77);
            System.out.println("test3 " + customer);
        } catch (Exception e) {
        } finally {
            JDBCConnectUtils.closeResource(connection, null);
        }
    }

    //MapListHander:是ResultSetHandler接口的实现类，对应表中的多条记录。
    //将字段及相应字段的值作为map中的key和value。将这些map添加到List中
    @Test
    public void testQuery4() {
        Connection connection = null;
        try {
            connection = JDBCConnectUtils.getConnection3();
            QueryRunner queryRunner = new QueryRunner();
            String sql = "select id,name,email,birth date from customers where id < ?";
            MapListHandler mapListHandler = new MapListHandler();

            List<Map<String, Object>> customers = queryRunner.query(connection, sql, mapListHandler, 77);
            customers.forEach(System.out::println);
        } catch (Exception e) {
        } finally {
            JDBCConnectUtils.closeResource(connection, null);
        }
    }


    //ScalarHandler:用于查询特殊值
    @Test
    public void testQuery5() throws Exception {
        Connection connection = null;
        try {
            connection = JDBCConnectUtils.getConnection3();
            QueryRunner queryRunner = new QueryRunner();

            String sql = "select count(*) from customers";

            ScalarHandler scalarHandler = new ScalarHandler();
            Object result = queryRunner.query(connection, sql, scalarHandler);
            System.out.println("test5 " + result);
        } catch (Exception e) {
        } finally {
            JDBCConnectUtils.closeResource(connection, null);
        }
    }


    @Test
    public void testQuery6() throws Exception {
        Connection connection = null;
        try {
            connection = JDBCConnectUtils.getConnection3();
            QueryRunner queryRunner = new QueryRunner();

            String sql = "select max(birth) from customers";

            ScalarHandler scalarHandler = new ScalarHandler();
            Object result = queryRunner.query(connection, sql, scalarHandler);
            System.out.println("test6 " + result);
        } catch (Exception e) {
        } finally {
            JDBCConnectUtils.closeResource(connection, null);
        }
    }

    // 自定义ResultSetHandler的实现类
    @Test
    public void testQuery7() throws Exception {
        Connection connection = null;
        try {
            connection = JDBCConnectUtils.getConnection3();
            QueryRunner queryRunner = new QueryRunner();

            String sql = "select id,name,email,birth date from customers where id = 77";

            ResultSetHandler handler = new ResultSetHandler<Customer>() {

                @Override
                public Customer handle(ResultSet resultSet) throws SQLException {
                    return null;
                }
            };

            Object result = queryRunner.query(connection, sql, handler);
            System.out.println("test7 " + result);
        } catch (Exception e) {
        } finally {
            JDBCConnectUtils.closeResource(connection, null);
        }
    }




}
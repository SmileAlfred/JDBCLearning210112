package com.company.jdbc210112.test2_preparedstatement.crud;

import com.company.jdbc210112.test2_preparedstatement.bean.Customer;
import com.company.jdbc210112.test2_preparedstatement.bean.Order;
import com.company.jdbc210112.test2_preparedstatement.utils.JDBCUtiils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * 使用 PreparedStatement 实现针对不同表的通用的查询操作
 */
public class PreparedStatementQueryTest {

    @Test
    public void query() {
        /*String sql1 = "select id,name,email,birth date from customers where id = ?";
        Customer customer = getInstance(Customer.class, sql1, 8);
        System.out.println(customer);

        String sql2 = "select order_id orderId,order_name orderName,order_date orderDate from `order` where order_id = ?";
        Order order = getInstance(Order.class, sql2, 4);
        System.out.println(order);*/

        String sql3 = "select id,name,email,birth date from customers where id < ?";
        List customers = getInstanceList(Customer.class, sql3, 8);

        customers.forEach(System.out::println);
        /*for (int i = 0; i < customers.size(); i++) {
            System.out.println(customers.get(i));
        }*/

        String sql4 = "select order_id orderId,order_name orderName,order_date orderDate from `order` where order_id < ?";
        List orders = getInstanceList(Order.class, sql4, 5);
        orders.forEach(System.out::println);
    }

    /**
     * 针对 不同表，返回一串查询结果的方法；
     * @return
     */
    public <T> List<T> getInstanceList(Class<T> clazz, String sql, Object... args) {
        Connection connection = JDBCUtiils.getConnection();
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        try {
            ps = connection.prepareStatement(sql);
            //填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            resultSet = ps.executeQuery();
            List instans = new ArrayList<T>();
            while (resultSet.next()) {
                T t =clazz.newInstance();
                ResultSetMetaData rSMD = resultSet.getMetaData();
                int columnCount = rSMD.getColumnCount();
                for (int i = 0; i < columnCount; i++) {
                    //通过 getColumnLabel 获取列的别名
                    String columnLabel = rSMD.getColumnLabel(i + 1);

                    Object columnValue = resultSet.getObject(i + 1);
                    Field declaredField = clazz.getDeclaredField(columnLabel);
                    //获取 private 属性的权限
                    declaredField.setAccessible(true);
                    declaredField.set(t, columnValue);
                }
                instans.add(t);
            }
            return instans;
        } catch (Exception e) {
            System.out.println("testQueryCommon 报错: " + e.getMessage());
        } finally {
            JDBCUtiils.closeConnection(ps, connection, resultSet);
        }
        return null;
    }


    /**
     * 针对 不同表，返回一个查询结果的方法；
     * @param clazz 这个结果对应的 bean
     * @param sql   查询语句
     * @param args  参数
     * @param <T>   bean 对应的 类型
     * @return  一个查询结果
     */
    public <T> T getInstance(Class<T> clazz, String sql, Object... args) {
        Connection connection = JDBCUtiils.getConnection();
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        try {
            ps = connection.prepareStatement(sql);
            //填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            resultSet = ps.executeQuery();
            if (resultSet.next()) {
                T t =clazz.newInstance();
                ResultSetMetaData rSMD = resultSet.getMetaData();
                int columnCount = rSMD.getColumnCount();
                for (int i = 0; i < columnCount; i++) {
                    //通过 getColumnLabel 获取列的别名
                    String columnLabel = rSMD.getColumnLabel(i + 1);

                    Object columnValue = resultSet.getObject(i + 1);
                    Field declaredField = clazz.getDeclaredField(columnLabel);
                    //获取 private 属性的权限
                    declaredField.setAccessible(true);
                    declaredField.set(t, columnValue);
                }
                return t;
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println("testQueryCommon 报错: " + e.getMessage());
        } finally {
            JDBCUtiils.closeConnection(ps, connection, resultSet);
        }
        return null;
    }
}

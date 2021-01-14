package com.company.jdbc210112.test2_preparedstatement.crud;

import com.company.jdbc210112.test2_preparedstatement.bean.Order;
import com.company.jdbc210112.test2_preparedstatement.utils.JDBCUtiils;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.junit.Test;

import javax.naming.ldap.Rdn;
import javax.sound.midi.Soundbank;
import javax.xml.crypto.Data;
import java.lang.reflect.Field;
import java.sql.*;

/**
 * 针对于 Order 表的查找操作
 * 注意：表中的属性名为：order_id、order_name、order_date 这就和 bean 中属性不一致了
 */
public class Order4Query {

    @Test
    public void testOrder() {
        //注意：通过查询语句给 属性 起别名
        String sql = "select order_id orderId,order_name orderName,order_date orderDate from `order` where order_id = ?";
        Order order = testQueryCommon(sql, 2);
        System.out.println("检索结果：" + order);
    }

    public Order testQueryCommon(String sql, Object... args) {
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
                Order od = new Order();
                ResultSetMetaData rSMD = resultSet.getMetaData();
                int columnCount = rSMD.getColumnCount();
                for (int i = 0; i < columnCount; i++) {
                    //通过 getColumnName 获取的是列名，而不是自己起的重命名
                    //String columnName = rSMD.getColumnName(i+1);
                    //通过 getColumnLabel 获取列的别名
                    String columnLabel = rSMD.getColumnLabel(i+1);

                    /*int index = columnName.indexOf('_');
                    char c = columnName.charAt(index + 1);
                    char result = Character.toUpperCase(c);
                    columnName = columnName.replaceAll("_" + c, "" +result);*/


                    Object columnValue = resultSet.getObject(i + 1);
                    Field declaredField = Order.class.getDeclaredField(columnLabel);
                    //获取 private 属性的权限
                    declaredField.setAccessible(true);
                    declaredField.set(od, columnValue);
                }
                return od;
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


    //简单的查询操作
    @Test
    public void testQuery1() {
        Connection connection = JDBCUtiils.getConnection();
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        try {
            String sql = "select order_id,order_name,order_date from `order` where order_id = ?";
            ps = connection.prepareStatement(sql);

            ps.setObject(1, 2);
            resultSet = ps.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                Date date = resultSet.getDate(3);

                Order order = new Order(id, name, date);
                System.out.println(order);
            }
        } catch (Exception e) {
            System.out.println("testQuery1 报错：" + e.getMessage());
        } finally {
            JDBCUtiils.closeConnection(ps, connection, resultSet);
        }
    }


}

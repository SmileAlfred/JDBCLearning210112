package com.company.jdbc210112.test2_preparedstatement.crud;

import com.company.jdbc210112.test2_preparedstatement.bean.Customer;
import com.company.jdbc210112.test2_preparedstatement.utils.JDBCUtiils;
import com.mysql.jdbc.ResultSetRow;
import com.sun.xml.internal.bind.v2.model.core.ID;
import javafx.scene.chart.BubbleChart;
import javafx.scene.chart.CategoryAxis;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * 针对 Customers 表的查询操作
 */
public class Customers4Query {

    @Test
    public void testQueryCommon() {
        String sql = "select id,name,email from customers where id = ?";
        Customer customer = query4Customer(sql, "9");//这里 id 类型为 int,但是输入 ”10“ 也可以
        System.out.println(customer);
    }

    /**
     * 这是 重 难 点
     * 注意：用Test 跑单元测试是要求方法不能有参数和返回类型的；否则会报错：“Method query4Customer() should be void”
     * @param sql
     * @param args
     * @return
     */
    public Customer query4Customer(String sql, Object... args) {
        Connection connection = JDBCUtiils.getConnection();
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        try {
            ps = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            resultSet = ps.executeQuery();
            //获取 resultSet 的元数据
            ResultSetMetaData rSMD = resultSet.getMetaData();
            //获取查询结果有多少列
            int columnCount = rSMD.getColumnCount();

            //resultSet.next() 表示查询结果是否有 下一条数据
            if (resultSet.next()) {
                Customer customer = new Customer();
                for (int i = 0; i < columnCount; i++) {
                    //获取查询结果每一列的 值
                    Object columnValue = resultSet.getObject(i + 1);//需要注意，这里的数据 从 1 开始 ;
                    //获取查询结果 每一列对应的 列名
                    String columnName = rSMD.getColumnName(i + 1);
                    //通过列名获取对象的属性名 （通过属性描述，将对象的属性和 表中属性建立联系）
                    Field declaredField = Customer.class.getDeclaredField(columnName);
                    //如果该属性不是公开的，将其设置为公开的
                    declaredField.setAccessible(true);
                    declaredField.set(customer, columnValue);//注意第一个参数 是对象
                }
                return customer;
            } else {
                System.out.print("没有找到查询结果：\t");
                return null;
            }
        } catch (Exception e) {
            System.out.println("query4Customer 报错：" + e.getMessage());
        } finally {
            JDBCUtiils.closeConnection(ps, connection, resultSet);
        }
        return null;
    }


    @Test
    public void testQuery1() {
        Connection connection = JDBCUtiils.getConnection();
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        String sql = "select id,name,email,birth from customers where id = ?";
        try {
            ps = connection.prepareStatement(sql);

            //填充占位符
            ps.setString(1, "2");

            resultSet = ps.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String email = resultSet.getString(3);
                Date date = resultSet.getDate(4);

                Customer customer = new Customer(id, name, email, date);
                System.out.println(customer.toString());
            }
        } catch (Exception e) {
            System.out.println(" testQuery1 报错：" + e.getMessage());
        } finally {
            JDBCUtiils.closeConnection(ps, connection, resultSet);
        }
    }

}

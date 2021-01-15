package com.company.jdbc210115.dao_plus;

import com.company.jdbc210112.test2_preparedstatement.bean.Customer;
import com.company.jdbc210112.test2_preparedstatement.utils.JDBCUtiils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class CustomDAOImplTest {

    CustomDAOImpl customDAO = new CustomDAOImpl();
    Connection connection = JDBCUtiils.getConnection();

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void insert() {
        //日期转换
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = null;
        try {
            date = sdf.parse("1991-03-03");
        } catch (ParseException e) {
        }
        Date sqlDate = new Date(date.getTime());

        Customer customer = new Customer(66, "WangZhongYuan", "ZYWang@emial.com", sqlDate);
        int result = customDAO.insert(connection, customer);
        if (result > 0) System.out.println("插入成功");
        else System.out.println("插入失败");
    }

    @Test
    public void delete() {
        int result = customDAO.delete(connection, 66);
        if (result > 0) System.out.println("删除成功");
        else System.out.println("删除失败");
    }

    @Test
    public void update() {
        //日期转换
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = null;
        try {
            date = sdf.parse("1996-06-06");
        } catch (ParseException e) {
        }
        Date sqlDate = new Date(date.getTime());
        Customer customer = new Customer(21, "LiuXueXi", "LiuXueXi@qq.com", sqlDate);
        customDAO.update(connection, 21, customer);
    }

    @Test
    public void getCustomByID() {
        Customer customer = customDAO.getCustomByID(connection, 66);
        System.out.println("Customer = " + customer);
    }

    @Test
    public void getCustomAll() {
        List<Customer> customAll = customDAO.getCustomAll(connection);
        if (null != customAll) customAll.forEach(System.out::println);
        else System.out.println("获取全部数据失败");
    }

    @Test
    public void getCount() {
        String sql = "select count(*) from customers";
        Object value = customDAO.getValue(connection, sql);
        System.out.println("count = " + value);
    }

    @Test
    public void getMaxBirth() {
        String sql = "select max(birth) from customers";
        Object value = customDAO.getValue(connection, sql);
        System.out.println("max(bitrh) = " + value);
    }
}
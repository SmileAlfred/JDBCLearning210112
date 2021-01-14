package com.company.jdbc210112.test2_preparedstatement.crud;

import com.company.jdbc210112.test2_preparedstatement.utils.JDBCUtiils;
import org.junit.Test;

import java.sql.*;
import java.text.SimpleDateFormat;

/**
 * 使用 PreparedStatement 实现对数据表的 增删改，查 操作
 */
public class PreparedStatementUpdateTest {

    @Test
    public void testCommonMethod(){
        //增
        /*String sql = "insert into `order`(order_name,order_date)values(?,?)";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = null;
        try {
            date = sdf.parse("1101-03-03");
        } catch (ParseException e) {
        }
        commonModifyInfo(sql,"HongKangSong",new Date(date.getTime()));*/

        //改
        /*String sql = "update `order` set order_name = ? where order_id = ?";
        commonModifyInfo(sql,"hkSong","5");*/

        //删除
        String sql = "delete from `order` where order_id = ?";
        commonModifyInfo(sql,"5");

        System.out.println("执行完成");
    }

    /**
     * 实现通用的增删改操作
     */
    public void commonModifyInfo(String sql, Object ...args) {
        Connection connection = JDBCUtiils.getConnection();
        PreparedStatement ps = null;
        try {

         ps = connection.prepareStatement(sql);
        for (int i = 0; i < args.length; i++) {
            //数据库 以 1 开始，故，用 i+1；数组用 0 开始，故用 i
            ps.setObject(i+1,args[i]);
        }
        ps.execute();
        }catch (Exception e){
            System.out.println("修改报错：" + e.getMessage());
        }finally {
            JDBCUtiils.closeConnection(ps,connection);
        }
    }


    //像 customers 表中添加一条记录；
    @Test
    public void insert() {
        PreparedStatement ps = null;
        Connection connection = null;
        try {
            connection = JDBCUtiils.getConnection();

            //4. 预编译 SQL 语句；返回 PreparedStatement 实例
            String sql = "insert into customers(name,email,birth)values(?,?,?)";

            ps = connection.prepareStatement(sql);

            //5. 填充占位符
            ps.setString(1, "MaiLinPaoShou");
            ps.setString(2, "paoXiao@wei.com");
            //日期转换
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = sdf.parse("1101-03-03");
            ps.setDate(3, new Date(date.getTime()));

            //6. 执行操作
            ps.execute();
            System.out.println("插入完成！");
        } catch (Exception e) {
            System.out.println("连接报错：" + e.getMessage());
        } finally {
            JDBCUtiils.closeConnection(ps, connection);
        }
    }
}



















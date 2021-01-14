package com.company.jdbc210112.test2_preparedstatement.exer;

import com.company.jdbc210112.test2_preparedstatement.utils.JDBCUtiils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

/**
 * 课后练习 1 ：从控制台向数据库的表customers中插入一条数据
 */
public class Exer1Test {
    public static void main(String[] args) {
        insertInfo();
    }

    //注意，单元测试 时无法完成 scanner 的输入！
    @Test
    public static void insertInfo() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入姓名");

        String name = scanner.next();
        System.out.print("请输入邮箱：");
        String email = scanner.next();
        System.out.print("请输入生日：");//1995-08-19
        String birth = scanner.next();

        String sql = "insert into customers(name,email,birth)values(?,?,?)";
        int result = commonModifyInfo(sql, name, email, birth);
        if (result > 0) System.out.println("植入成功");
    }


    public static int commonModifyInfo(String sql, Object... args) {
        Connection connection = JDBCUtiils.getConnection();
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                //数据库 以 1 开始，故，用 i+1；数组用 0 开始，故用 i
                ps.setObject(i + 1, args[i]);
            }
            //ps.execute();
            return ps.executeUpdate();//返回的是 修改表格 影响到的行数
        } catch (Exception e) {
            System.out.println("修改报错：" + e.getMessage());
        } finally {
            JDBCUtiils.closeConnection(ps, connection);
        }
        return 0;
    }

}

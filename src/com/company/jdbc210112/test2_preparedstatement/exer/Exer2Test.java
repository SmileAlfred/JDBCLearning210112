package com.company.jdbc210112.test2_preparedstatement.exer;

import com.company.jdbc210112.test2_preparedstatement.bean.ExamStudent;
import com.company.jdbc210112.test2_preparedstatement.utils.JDBCUtiils;
import org.junit.Test;

import javax.security.sasl.SaslServer;
import javax.sound.midi.Soundbank;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 测试 2 ：填、删、查
 */
public class Exer2Test {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("你想干啥？");
            System.out.println("1.修改");
            System.out.println("2.查询");
            System.out.println("3.删除");
            System.out.println("4.退出");
            String next = scanner.next();
            switch (next) {
                case "1":
                    insertInfo();
                    break;
                case "2":
                    query();
                    break;
                case "3":
                    deleteInfo();
                    break;
                case "4":
                    return;
                default:
                    break;
            }
        }
    }

    //问题 3 ：删除操作（通过准考证号）
    public static void deleteInfo() {
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("请输入学生的考号：");
            String ExamCard = scanner.next();

            String sql = "delete from examstudent where ExamCard = ?";
            int result = commonModifyInfo(sql, ExamCard);
            if (result == 0) System.out.println("输入有误");
            else {
                System.out.println("删除成功");
                break;
            }
        }
    }

    //问题 2 ：查询（通过 准考证号 或 身份证号）
    public static void query() {
        Scanner scanner = new Scanner(System.in);
        String inputType, inputStr;
        while (true) {
            System.out.print("请输入查询的类型（a / b）");
            inputType = scanner.next();
            if (!inputType.equalsIgnoreCase("a") && !inputType.equals("b")) continue;

            String sql = inputType.equals("a") ?
                    "select FlowID,Type,IDCard,ExamCard,StudentName,Location,Grade from examstudent where ExamCard = ?"
                    : "select FlowID,Type,IDCard,ExamCard,StudentName,Location,Grade from examstudent where IDCard = ?";
            String hintStr = inputType.equals("a") ? "请输入准考证号：" : "请输入身份证号：";
            System.out.print(hintStr);

            inputStr = scanner.next();

            List<ExamStudent> instanceList = getInstanceList(ExamStudent.class, sql, inputStr);

            if (instanceList.size() > 0) instanceList.forEach(System.out::println);
            else System.out.println("查无此人");
        }
    }

    public static <T> List<T> getInstanceList(Class<T> clazz, String sql, Object... args) {
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
                T t = clazz.newInstance();
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


    //问题 1 ：向 examstudent 表中添加 一条 数据
    public static void insertInfo() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("输入考试类型：");
        String Type = scanner.next();

        System.out.print("输入身份证号：");
        String IDCard = scanner.next();

        System.out.print("输入准考证号：");
        String ExamCard = scanner.next();

        System.out.print("输入考生姓名：");
        String StudentName = scanner.next();

        System.out.print("输入考试地点：");
        String Location = scanner.next();

        System.out.print("输入考试成绩：");
        String Grade = scanner.next();


        String sql = "insert into examstudent(Type,IDCard,ExamCard,StudentName,Location,Grade)values(?,?,?,?,?,?)";
        int insertResult = commonModifyInfo(sql, Type,IDCard,ExamCard,StudentName,Location,Grade);
        if (insertResult > 0) System.out.println("插入成功");
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
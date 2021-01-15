package com.company.jdbc210115.transaction;

import com.company.jdbc210115.utils.JDBCUtiils;
import org.junit.Test;

import javax.sound.midi.Soundbank;
import java.lang.reflect.Field;
import java.sql.*;

/*
 * 1.什么叫数据库事务？
 * 事务：一组逻辑操作单元,使数据从一种状态变换到另一种状态。
 * 		> 一组逻辑操作单元：一个或多个DML操作。
 *
 * 2.事务处理的原则：保证所有事务都作为一个工作单元来执行，即使出现了故障，都不能改变这种执行方式。
 * 当在一个事务中执行多个操作时，要么所有的事务都被提交(commit)，那么这些修改就永久地保存
 * 下来；要么数据库管理系统将放弃所作的所有修改，整个事务回滚(rollback)到最初状态。
 *
 * 3.数据一旦提交，就不可回滚
 *
 * 4.哪些操作会导致数据的自动提交？
 * 		>DDL操作一旦执行，都会自动提交。
 * 			>set autocommit = false 对DDL操作失效
 * 		>DML默认情况下，一旦执行，就会自动提交。
 * 			>我们可以通过set autocommit = false的方式取消DML操作的自动提交。
 * 		>默认在关闭连接时，会自动的提交数据
 */
public class TransactionTest {
    String sql1 = "UPDATE user_table SET balance=balance-100 WHERE USER=?";
    String sql2 = "UPDATE user_table SET balance=balance+100 WHERE USER=?";

    //******************未考虑数据库事务情况下的转账操作**************************

    /**
     * 针对于 user_table 来说；
     * AA 给 BB 转账 100 元
     * UPDATE user_table SET balance=balance-100 WHERE USER='AA';
     * UPDATE user_table SET balance=balance+100 WHERE USER='BB';
     */
    @Test
    public void testTransaction() {
        commonModifyInfo(sql1, "AA");
        System.out.println(100 / 0);//模拟擦做失败，AA 扣了钱， BB 没收到
        commonModifyInfo(sql2, "BB");
    }

    /**
     * 通用的 增删改 操作
     *
     * @param sql
     * @param args
     */
    public int commonModifyInfo(String sql, Object... args) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = JDBCUtiils.getConnection();
            ps = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                //数据库 以 1 开始，故，用 i+1；数组用 0 开始，故用 i
                ps.setObject(i + 1, args[i]);
            }
            return ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("修改报错：" + e.getMessage());
        } finally {
            JDBCUtiils.closeConnection(ps, connection);
        }
        return 0;
    }

    //********************考虑数据库事务后的转账操作*********************
    @Test
    public void testTransactionByTX() {
        Connection connection = null;
        try {
            connection = JDBCUtiils.getConnection();

            //1. *****取消数据的自动提交！
            connection.setAutoCommit(false);

            commonModifyInfo(connection, sql1, "AA");
            System.out.println(10 / 0);
            commonModifyInfo(connection, sql2, "BB");

            //2. 提交数据
            connection.commit();
            System.out.println("转账成功");
        } catch (Exception e) {
            // 3. 数据回滚！
            try {
                connection.rollback();
            } catch (SQLException throwables) {
                System.out.println("回滚数据失败：" + throwables.getMessage());
            }
            System.out.println("转账失败：" + e.getMessage());
        } finally {
            try {
                //将连接 恢复成自动提交状态
                connection.setAutoCommit(true);
            } catch (SQLException throwables) {
            }
            JDBCUtiils.closeConnection(null, connection);
        }
    }

    public int commonModifyInfo(Connection connection, String sql, Object... args) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                //数据库 以 1 开始，故，用 i+1；数组用 0 开始，故用 i
                ps.setObject(i + 1, args[i]);
            }

            return ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("修改报错：" + e.getMessage());
        } finally {
            JDBCUtiils.closeConnection(ps, null);
        }
        return 0;
    }

    //************************隔离级别的讨论*************************
    @Test
    public void testTransactionSelect() throws Exception {
        Connection connection = JDBCUtiils.getConnection();
        //获取当前连接的的 隔离性
        System.out.println(connection.getTransactionIsolation());
        //设置当前连接的 隔离性
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        String sql = "select user,password,balance from user_table where user = ?";
        User cc = getInstance(connection, User.class, sql, "CC");
        System.out.println(cc);
    }


    /**
     * 针对 不同表，返回一个查询结果的方法V2.0  考虑上事务
     *
     * @param clazz 这个结果对应的 bean
     * @param sql   查询语句
     * @param args  参数
     * @param <T>   bean 对应的 类型
     * @return 一个查询结果
     */
    public <T> T getInstance(Connection connection, Class<T> clazz, String sql, Object... args) {
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
                return t;
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println("testQueryCommon 报错: " + e.getMessage());
        }
        return null;
    }


}

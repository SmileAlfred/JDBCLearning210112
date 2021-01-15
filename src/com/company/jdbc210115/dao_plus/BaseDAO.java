package com.company.jdbc210115.dao_plus;

import com.company.jdbc210115.utils.JDBCUtiils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * 封装了对数据表的 通用操作
 */
public abstract class BaseDAO<T> {

    private Class<T> clazz = null;

    {
        //获取当前BaseDAO的子类继承的父类中的泛型；此时的 this 表示的是子类自己
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        ParameterizedType paramType = (ParameterizedType) genericSuperclass;

        Type[] typeArguments = paramType.getActualTypeArguments();//获取了父类的泛型参数
        clazz = (Class<T>) typeArguments[0];//泛型的第一个参数
    }


    /**
     * 通用的增删改 V2.0；考虑到事务
     *
     * @param connection
     * @param sql
     * @param args
     * @return
     */
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

    /**
     * 通用的 查询操作V2.0 考虑到 事务 操作
     *
     * @param connection
     * @param sql
     * @param args
     * @return
     */
    public T getInstance(Connection connection, String sql, Object... args) {
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
        } finally {
            JDBCUtiils.closeConnection(ps, null, resultSet);
        }
        return null;
    }


    /**
     * 查询一串结果，V2.0 考虑到 事务
     *
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public <T> List<T> getInstanceList(Connection connection,String sql, Object... args) {
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
                T t = (T) clazz.newInstance();
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
            JDBCUtiils.closeConnection(ps, null, resultSet);
        }
        return null;
    }


    /**
     * 用于查询特殊值的 通用方法：如 查询 count
     */
    public <E> E getValue(Connection connection, String sql, Object... args) {
        ResultSet resultSet = null;
        PreparedStatement ps = null;

        try {
            ps = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return (E) resultSet.getObject(1);
            }
        } catch (Exception e) {
            System.out.println("获取特殊值报错：" + e.getMessage());
        } finally {
            JDBCUtiils.closeConnection(ps, null, resultSet);
        }
        return null;
    }
}

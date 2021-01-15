package com.company.jdbc210115.dao_plus;


import com.company.jdbc210112.test2_preparedstatement.bean.Customer;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

/**
 * DAO：数据库访问对象
 * 改进：针对于制定表的操作，取消了 查询操作时 传入 类名的步骤，利用反射获取其子类指定的 表对应的 Bean 名
 */
public class CustomDAOImpl extends BaseDAO<Customer> implements CustomDAO {
    @Override
    public int insert(Connection connection, Customer customer) {
        String sql = "insert into customers (id,name,email,birth)values(?,?,?,?)";
        return commonModifyInfo(connection, sql, customer.getId(), customer.getName(), customer.getEmail(), customer.getDate());
    }

    @Override
    public int delete(Connection connection, int id) {
        String sql = "delete from customers where id = ?";
        return commonModifyInfo(connection, sql, id);
    }

    @Override
    public int update(Connection connection, int id, Customer customer) {
        String sql = "update customers set name = ?,email = ?,birth = ? where id = ?";
        return commonModifyInfo(connection, sql, customer.getName(), customer.getEmail(), customer.getDate(), id);
    }

    @Override
    public Customer getCustomByID(Connection connection, int id) {
        String sql = "select id,name,email,birth date from customers where id = ?";
        return  getInstance(connection, sql, id);
    }

    @Override
    public List<Customer> getCustomAll(Connection connection) {
        String sql = "select id,name,email,birth date from customers";
        return getInstanceList(connection, sql);
    }

    @Override
    public Long getCount(Connection connection) {
        String sql = "select count(*) from customer";
        return  getValue(connection, sql);
    }

    @Override
    public Date getMaxBirth(Connection connection) {
        String sql = "select max(birth) from customer";
        return  getValue(connection,sql);
    }
}

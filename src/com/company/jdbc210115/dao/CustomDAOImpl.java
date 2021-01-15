package com.company.jdbc210115.dao;

import com.company.jdbc210112.test2_preparedstatement.bean.Customer;
import com.sun.corba.se.spi.legacy.connection.GetEndPointInfoAgainException;
import com.sun.java.swing.plaf.motif.MotifFileChooserUI;

import javax.lang.model.element.NestingKind;
import java.sql.Connection;
import java.sql.SQLOutput;
import java.util.Date;
import java.util.List;

/**
 * DAO：数据库访问对象
 */
public class CustomDAOImpl extends BaseDAO implements CustomDAO {
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
        return getInstance(connection, Customer.class, sql, id);
    }

    @Override
    public List<Customer> getCustomAll(Connection connection) {
        String sql = "select id,name,email,birth date from customers";
        return getInstanceList(connection, Customer.class, sql);
    }

    @Override
    public Long getCount(Connection connection) {
        String sql = "select count(*) from customer";
        return getValue(connection, sql);
    }

    @Override
    public Date getMaxBirth(Connection connection) {
        String sql = "select max(birth) from customer";
        return getValue(connection,sql);
    }
}

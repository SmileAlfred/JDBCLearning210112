package com.company.jdbc210115.dao_plus;

import com.company.jdbc210112.test2_preparedstatement.bean.Customer;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

/**
 * 用于规范 针对于 Custom 表的常用操作
 */
public interface CustomDAO {

    /**
     * 将数据插入到表中
     */
    int insert(Connection connection, Customer customer);

    int delete(Connection connection, int id);

    int update(Connection connection, int id, Customer customer);

    Customer getCustomByID(Connection connection, int id);

    /**
     * 查询 表中 所有的记录
     *
     * @param connection
     * @return
     */
    List<Customer> getCustomAll(Connection connection);

    /**
     * 返回数据表中 数据的条目数
     *
     * @param connection
     * @return
     */
    Long getCount(Connection connection);

    /**
     * 返回 最大的生日
     *
     * @param connection
     * @return
     */
    Date getMaxBirth(Connection connection);


}

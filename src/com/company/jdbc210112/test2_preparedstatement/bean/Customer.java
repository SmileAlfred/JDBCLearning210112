package com.company.jdbc210112.test2_preparedstatement.bean;

import java.sql.Date;

/**
 * ORM 编程思想，一个数据表对应一个 Java 类；
 * 表中的一条记录对应 Java 类的一个对象
 * 表中的一个字段对应 Java 类的一个s属性；
 */
public class Customer {
    private int id;
    private String name;
    private String email;
    private Date date;

    public Customer(int id, String name, String email, Date date) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.date = date;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", date=" + date +
                '}';
    }

    public Customer() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

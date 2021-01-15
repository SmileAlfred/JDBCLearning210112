package com.company.jdbc210112.test3_blob;

import com.company.jdbc210112.test2_preparedstatement.bean.Customer;
import com.company.jdbc210112.test2_preparedstatement.utils.JDBCUtiils;
import org.junit.Test;

import javax.sound.midi.Soundbank;
import java.io.*;
import java.sql.*;
import java.time.format.FormatStyle;

/**
 * 测试使用 PreparedStatement 操作 Blob 类型的数据
 */
public class BlobTest {


    @Test
    public void testQuery() {
        Connection connection = JDBCUtiils.getConnection();
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        InputStream is = null;
        FileOutputStream fos = null;

        try {
            String sql = "select id,name,email,birth,photo from customers where id = ?";
            ps = connection.prepareStatement(sql);
            ps.setObject(1, 16);
            resultSet = ps.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                Date birth = resultSet.getDate("birth");
                Customer cus = new Customer(id, name, email, birth);
                System.out.println(cus);

                //从数据库 获取 二进制文件，并保存到本地
                Blob photo = resultSet.getBlob("photo");
                is = photo.getBinaryStream();
                fos = new FileOutputStream("zhuyin.jpg");
                byte[] bytes = new byte[1024];
                int len;
                while ((len = is.read(bytes)) != -1) {
                    fos.write(bytes, 0, len);
                    fos.flush();
                }
            }
            System.out.println("查看结束！下载完成！");
        } catch (Exception e) {
            System.out.println("查看二进制文件报错：" + e.getMessage());
        } finally {
            JDBCUtiils.closeConnection(ps, connection, resultSet);
            try {
                if (null != is) is.close();
                if (null != fos) fos.close();
            } catch (IOException e) {
                System.out.println("关流报错："+e.getMessage());
            }
        }
    }


    //向 customers 数据库中插入 blob 类型字段
    @Test
    public void testInsert() {
        Connection connection = JDBCUtiils.getConnection();
        PreparedStatement ps = null;
        try {
            String sql = "insert into customers(name,email,birth,photo)values(?,?,?,?)";
            ps = connection.prepareStatement(sql);
            ps.setObject(1, "xuwei");
            ps.setObject(2, "xuwei@gmail.com");
            ps.setObject(3, "1979-06-06");
            FileInputStream is = new FileInputStream(new File("xuwei.jpeg"));
            ps.setBlob(4, is);
            ps.execute();
            System.out.println("插入完成");
        } catch (Exception e) {
            System.out.println("插入 Blob 报错:" + e.getMessage());
        } finally {
            JDBCUtiils.closeConnection(ps, connection);
        }
    }
}

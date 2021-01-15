package com.company.jdbc210115.transaction;

import com.company.jdbc210115.utils.JDBCUtiils;
import com.sun.istack.internal.FinalArrayList;
import org.junit.Test;

import javax.sound.midi.Soundbank;
import java.sql.Connection;

public class ConnectionTest {

    @Test
    public void testGetCinnection() {
        Connection connection = null;
        try {
             connection = JDBCUtiils.getConnection();
        } catch (Exception e) {
            System.out.println("连接报错：" + e.getMessage());
        }
    }
}

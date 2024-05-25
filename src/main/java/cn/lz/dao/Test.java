package cn.lz.dao;

import java.sql.*;

public class Test {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        // 打开链接
        System.out.println("连接数据库...");
        Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.13.134:3306/seq?useSsl=false","root","123456");

        // 执行查询
        System.out.println(" 实例化Statement对象...");
        Statement stmt = conn.createStatement();
        String sql;
        sql = "SELECT stub from video_seq";
        ResultSet resultSet = stmt.executeQuery(sql);
        while (resultSet.next()) {
            System.out.println(resultSet.getString("stub"));
        }
        System.out.println();

    }
}

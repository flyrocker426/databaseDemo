package com.lmh;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 003
 */
public class jdbc {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        // 加载驱动
//        Class<?> aClass = Class.forName("com.mysql.cj.jdbc.Driver");

        // 连接信息
        String url = "jdbc:mysql://localhost:3306/fruitdb?serverTimezone=GMT%2B8&useUnicode=true&characterEncoding=utf-8";
        String user = "root";
        String pwd = "root";

        // 通过驱动管理器获取连接对象
        Connection connection = DriverManager.getConnection(url, user, pwd);
        System.out.println(connection);

        // 插入
        // 编写sql语句
        String sql = "insert into t_fruit values(1,?,?,?,?)";

        // 创建预处理命令对象
        PreparedStatement psmt = connection.prepareStatement(sql);

        // 填充参数
        psmt.setString(1, "桃子");
        psmt.setInt(2, 20);
        psmt.setInt(3, 100);
        psmt.setString(4, "无");

        // 执行更新(增删改)，返回影响行数
        int count = psmt.executeUpdate();
        System.out.println(count > 0 ? "添加成功" : "添加失败");

        // 查询
        // 编写sql语句
        sql = "select * from t_fruit";

        // 创建预处理命令对象
        psmt = connection.prepareStatement(sql);

        // 执行查询，返回结果集
        ResultSet rs = psmt.executeQuery();

        List<Fruit> fruitList = new ArrayList<>();
        while(rs.next()){
            // 结果集列名、列数
            int fid = rs.getInt(1);
            String fname = rs.getString("fname");
            int price = rs.getInt(3);
            int fcount = rs.getInt(4);
            String remark = rs.getString(5);

            // 封装到数组
            fruitList.add(new Fruit(fid, fname, price, fcount, remark));
        }

        // 释放资源
        rs.close();
        psmt.close();
        connection.close();

        fruitList.forEach(System.out::println);


    }
}

package com.fruitmanager.dao.impl;

import com.fruitmanager.dao.FruitDAO;
import com.fruitmanager.dao.base.BaseDAO;
import com.lmh.Fruit;

import java.sql.*;
import java.util.List;

public class FruitDAOImpl extends BaseDAO<Fruit> implements FruitDAO {

    @Override
    public List<Fruit> getFruitList() throws SQLException {
        String sql = "select * from t_fruit";
        return super.executeQuery(sql);
    }

    @Override
    public boolean addFruit(Fruit fruit) throws SQLException {

        String sql = "insert into t_fruit values(?,?,?,?,?)";

        return super.executeUpdate(sql, fruit.getFid(),
                fruit.getFname(), fruit.getPrice(),
                fruit.getFcount(), fruit.getRemark()) > 0;
    }

    @Override
    public boolean updateFruit(Fruit fruit) throws SQLException {

        String sql = "update t_fruit set fcount = ? where fid = ?";
        return super.executeUpdate(sql, fruit.getFcount(), fruit.getFid()) > 0;

    }

    @Override
    public Fruit getFruitByFname(String fname) throws SQLException {

        String sql = "select * from t_fruit where fname = ?";
        return super.load(sql, fname);

    }

    @Override
    public boolean delFruit(String fname) throws SQLException {
        String sql = "delete t_fruit where fname = ?";
        return super.executeUpdate(sql, fname) > 0;
    }

    /**
     * 批处理执行，若任务较多，可以分批次执行，每次执行完情况批处理执行集
     */
    public void addFruitBatch(Fruit fruit) throws SQLException, ClassNotFoundException {

        Class.forName(DRIVER);

        connection = DriverManager.getConnection(URL + "rewriteBatchedStatements=true", USER, PWD);

        String sql = "insert into t_fruit values(?,?,?,?,?)";

        pst = connection.prepareStatement(sql);

        for (int i = 0; i < 10; i++) {
            pst.setInt(1, 250 + i);
            pst.setString(2, "榴莲吧" + i);
            pst.setInt(3, 15);
            pst.setInt(4, 100);
            pst.setString(5, "榴莲神奇呢");

            pst.addBatch();

            if (i % 1000 == 0) {
                pst.executeBatch();
                pst.clearBatch();
            }
        }
        pst.executeBatch();
    }
}


package com.fruitmanager.dao;

import com.lmh.Fruit;

import java.sql.SQLException;
import java.util.List;

/**
 * @author lmh
 * @description
 * @create 2022-12-03-11:24
 */
public interface FruitDAO {
    List<Fruit> getFruitList() throws SQLException;

    boolean addFruit(Fruit fruit) throws SQLException;

    boolean updateFruit(Fruit fruit) throws SQLException;

    Fruit getFruitByFname(String fname) throws SQLException;

    boolean delFruit(String fname) throws SQLException;
}

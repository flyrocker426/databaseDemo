package com.fruitmanager.controller;

import com.fruitmanager.dao.FruitDAO;
import com.fruitmanager.dao.impl.FruitDAOImpl;
import com.lmh.Fruit;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * 对List的一些操作，数据库相关的增删改查，以及相关规则限制
 */
public class menu {

    Scanner scanner = new Scanner(System.in);
    FruitDAO fruitDAO = new FruitDAOImpl();

    public int showMenu() {
        System.out.println("welcome to fruit manager!");
        System.out.println("1. display current fruit list");
        System.out.println("2. add fruit");
        System.out.println("3. display the specified fruit");
        System.out.println("4. delete fruit");
        System.out.println("5. exit");
        System.out.println("====================================");
        System.out.print("please choose:");


        return scanner.nextInt();
    }

    public void showFruit(String fruitName) {
        try {
            Fruit fruit = fruitDAO.getFruitByFname(fruitName);
            if (fruit == null) {
                System.out.println("sorry it doesn't exist");
            } else {
                System.out.println("------------------------------------");
                System.out.println("编号\t\t名称\t\t单价\t\t库存\t\t备注");
                System.out.println(fruit.toStringList());
                System.out.println("------------------------------------");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void showFruitList() {
        try {
            List<Fruit> fruitList = fruitDAO.getFruitList();
            System.out.println("------------------------------------");
            System.out.println("编号\t\t名称\t\t单价\t\t库存\t\t备注");
            if (fruitList == null || fruitList.size() <= 0) {
                System.out.println("sorry list empty");
            } else {
                for (Fruit fruit : fruitList) {
                    System.out.println(fruit.toStringList());
                }
            }
            System.out.println("------------------------------------");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void addFruit() {
        System.out.println("please input fid:");
        int fid = scanner.nextInt();
        System.out.println("please input fname:");
        String fname = scanner.next();
        System.out.println("please input price:");
        int price = scanner.nextInt();
        System.out.println("please input fcount:");
        int fcount = scanner.nextInt();
        System.out.println("please input remark:");
        String remark = scanner.next();

        try {
            fruitDAO.addFruit(new Fruit(fid, fname, price, fcount, remark));
            System.out.println("add success!");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void deleteFruit(String fruitName) {
        try {
            fruitDAO.delFruit(fruitName);
            System.out.println("delete success");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean exit() {
        System.out.println("confirm exit?");
        String next = scanner.next();
        return !"Y".equalsIgnoreCase(next);
    }

}

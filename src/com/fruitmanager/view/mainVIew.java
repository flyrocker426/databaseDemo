package com.fruitmanager.view;

import com.fruitmanager.controller.menu;

import java.util.Scanner;

/**
 * 实现类，实现整体界面显示，
 * 显示并关联具体操作方法
 * 同时启动
 */
public class mainVIew {


    public static void main(String[] args) {
        menu menu = new menu();
        Scanner scanner = new Scanner(System.in);

        boolean flag = true;

        while(flag) {
            int input = menu.showMenu();
            switch (input) {
                case 1:  // 查看水果库存列表
                    menu.showFruitList();
                    System.out.println("please input random key for continue");
                    scanner.next();
                    break;
                case 2:  // 添加水果库存信息
                    menu.addFruit();
                    break;
                case 3:  // 查看特定水果库存信息
                    System.out.println("please input fruit name you wanna seek:");
                    menu.showFruit(scanner.next());
                    System.out.println("please input random key for continue");
                    scanner.next();
                    break;
                case 4:  // 水果下架
                    System.out.println("please input fruit name you wanna seek:");
                    menu.deleteFruit(scanner.next());
                    break;
                case 5:  // 退出
                    flag = menu.exit();
                    break;
                default:
                    System.out.println("input invalid, please check u input.");
            }
        }
        System.out.println("Thanks for using, goodbye.");
    }

}

package com.lmh;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.pool.DruidPooledConnection;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

/**
 * dataSource
 */
public class Druid {
    public static void main(String[] args) throws Exception {

        Properties properties = new Properties();
        InputStream is = Druid.class.getClassLoader().getResourceAsStream("com/jdbc.properties");
        properties.load(is);

        DruidDataSource druidDataSource = new DruidDataSource();

        DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
//        druidDataSource.setDriverClassName(properties.getProperty("driverCLassName"));
//        druidDataSource.setUrl(properties.getProperty("url"));
//        druidDataSource.setUsername(properties.getProperty("username"));
//        druidDataSource.setPassword(properties.getProperty("password"));
//
//        druidDataSource.setInitialSize(Integer.parseInt(properties.getProperty("initialSize")));
//        druidDataSource.setMaxActive(Integer.parseInt(properties.getProperty("maxActive")));
//        druidDataSource.setMaxWait(Integer.parseInt(properties.getProperty("maxWait")));

        for (int i = 0; i < 5; i++) {
            System.out.println(dataSource.getConnection());
        }
    }
}

package com.fruitmanager.dao.base;


import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * bug修复，目前查不到东西
 * 全文注释，尤其反射部分
 * @param <T>
 */
public class BaseDAO<T> {
    protected ResultSet rs;
    protected PreparedStatement pst;
    protected Connection connection;

    private Class<T> entityClass;

    public final static String DRIVER = "com.mysql.cj.jdbc.Driver";
    public final static String URL = "jdbc:mysql://localhost:3306/fruitdb?serverTimezone=GMT%2B8&useUnicode=true&characterEncoding=utf-8" ;
    public final static String USER = "root";
    public final static String PWD = "root";

    public BaseDAO(){
        // getClass()为当前执行对象的类，即一般是子类实现类，所以后跟Superclass方法
        Type genericType = getClass().getGenericSuperclass();
        // ParameterizedType参数化类型
        Type[] actualTypeArguments = ((ParameterizedType) genericType).getActualTypeArguments();

        Type actualType = actualTypeArguments[0];     // 真实T类型

        String className = actualType.getTypeName();  // 当前的调用的全类名

        try {
            entityClass = (Class<T>) Class.forName(className);    // 反射获取当前调用类
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected Connection getConn(){

        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(URL, USER, PWD);
            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void close(ResultSet rs, PreparedStatement pst, Connection connection){
        try {
            if (rs != null) {
                rs.close();
            }
            if(pst != null){
                pst.close();
            }
            if (connection != null){
                connection.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * 返回了自增主键，可以用于下次赋值
     */
    protected int executeUpdate(String sql, Object... params) throws SQLException {
        boolean insertFlag;

        insertFlag = sql.trim().toUpperCase().startsWith("INSERT");
        
        try{
            connection = getConn();
            if (insertFlag){
                pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            }else{
                pst = connection.prepareStatement(sql);
            }
            setParams(pst, params);
            int count = pst.executeUpdate();

            rs = pst.getGeneratedKeys();
            if (rs.next()){
                return ((Long)rs.getLong(1)).intValue();
            }

            return count;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs, pst, connection);
        }
        return 0;
    }

    protected List<T> executeQuery(String sql, Object...params){
        List<T> list = new ArrayList<>();
        try{
            connection = getConn();
            PreparedStatement pst = connection.prepareStatement(sql);

            rs = setParams(pst, params).executeQuery();

            // 元数据：描述结果集的数据，结果集有哪些类，什么长度等
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            while (rs.next()) {
                T entity = entityClass.newInstance();

                for (int i = 0; i < columnCount; i++) {
                    String columnName = rsmd.getColumnName(i+1);
                    Object columnValue = rs.getObject(i + 1);
                    setValue(entity, columnName, columnValue);
                }

                list.add(entity);
            }
            return list;

        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            close(rs, pst, connection);
        }
        return null;
    }

    protected T load(String sql, Object... params){
        try{
            connection = getConn();
            PreparedStatement pst = connection.prepareStatement(sql);

            rs = setParams(pst, params).executeQuery();

            // 元数据：描述结果集的数据，结果集有哪些类，什么长度等
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            if (rs.next()) {
                T entity = entityClass.newInstance();

                for (int i = 0; i < columnCount; i++) {
                    String columnName = rsmd.getColumnName(i+1);
                    Object columnValue = rs.getObject(i + 1);
                    setValue(entity, columnName, columnValue);
                }
                return entity;
            }
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            close(rs, pst, connection);
        }
        return null;
    }

    // 执行复杂查询，例如统计结果
    protected Object[] executeComplexQuery(String sql, Object...params){
        try{
            connection = getConn();
            PreparedStatement pst = connection.prepareStatement(sql);

            rs = setParams(pst, params).executeQuery();

            // 元数据：描述结果集的数据，结果集有哪些类，什么长度等
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            Object[] columnValueArr = new Object[columnCount];

            if (rs.next()) {
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = rs.getObject(i+1);
                    columnValueArr[i] = columnValue;
                }
                return columnValueArr;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs, pst, connection);
        }
        return null;
    }

    private PreparedStatement setParams(PreparedStatement pst, Object... params) throws SQLException {
        if (params != null && params.length > 0){
            for (int i = 0; i < params.length; i++) {
                pst.setObject(i+1, params[i]);
            }
        }
        return pst;
    }


    private void setValue(Object obj, String property, Object propertyValue){
        Class<?> clazz = obj.getClass();

        try {
            // 获取property字符串中对应的属性名，比如“fid”， 则就去对象中找fid
            Field field = clazz.getDeclaredField(property);
            field.setAccessible(true);
            field.set(obj, propertyValue);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}

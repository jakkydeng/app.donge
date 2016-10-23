package com.example.hujiahong.hujiahong;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.sql.*;
import java.util.Vector;


// 表appversion（name,version,url）
//'donge','1.0','http://182.254.130.173/app/donge.apk'
//'donge2','1.0','http://182.254.130.173/app/donge2.apk'


public class DataInterface{

    public static  String user="root";
    public static  String password="wlzx2015";
    public static Connection con;
    public static Statement stmt;
    public static ResultSet rs;
    public static Boolean flag=true;
    public static File f=new File("server");
    public static Vector columnHeads = new Vector();
    public static  Vector rows = new Vector();


    public static void main(String[] args) throws SQLException {


    }
    public  static void connect() throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.jdbc.Driver");
        String serverName = "56e4d81e2280b.sh.cdb.myqcloud.com";
        String portNumber = "4904";
        String sid = "appinfo";
        String url = "jdbc:mysql://" + serverName + ":" + portNumber + "/" + sid;
        String username = "root";
        password = "wlzx2015";

        con = DriverManager.getConnection(url, username, password);
        stmt = con.createStatement();
        System.out.println("数据库连接成功!");

    }
    public static void close() throws SQLException{
        stmt.close();
        con.close();


    }

    public static String select(String sql) throws SQLException, JSONException {


        try {

            rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd =rs.getMetaData();

        } catch (SQLException e) {
            System.out.println(e);
        }


        String returnJason=resultSetToJson(rs);

        return returnJason;





    }




    public static String resultSetToJson(ResultSet rs) throws SQLException,JSONException
    {
        // json数组
        JSONArray array = new JSONArray();

        // 获取列数
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        // 遍历ResultSet中的每条数据
        while (rs.next()) {
            JSONObject jsonObj = new JSONObject();

            // 遍历每一列
            for (int i = 1; i <= columnCount; i++) {
                String columnName =metaData.getColumnLabel(i);
                String value = rs.getString(columnName);
                jsonObj.put(columnName, value);
            }
            array.put(jsonObj);
        }

        return array.toString();
    }



}
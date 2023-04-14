package datalayer;

import java.sql.*;

/**
 * This file implements data layer functionality
 */
public class DataLayer {

    public static final String URL = "jdbc:mysql://localhost/";
    public static final String DEFAULT_DRIVER = "com.mysql.cj.jdbc.Driver";
    public  String userName = new String();
    public  String password = new String();


    private Connection conn;
    private Statement stmt;
    private ResultSet rs;
    private String sql;

    public boolean loadDriver() {
        try {
            Class.forName(DEFAULT_DRIVER);
            System.out.println("Driver Loaded " + DEFAULT_DRIVER + "\n");
            return true;
        }
        catch (ClassNotFoundException e) {
            System.out.println("Unable to load driver class: " + DEFAULT_DRIVER);
            return false;
        }
    }

    public boolean getConnection(String databaseName) {

        try {
            String url = URL + databaseName + "?serverTimezone=UTC";
            conn = DriverManager.getConnection(url, userName, password);
            return true;
        }
        catch (SQLException sqle) {
            System.out.println("Error: Could not connect to the DB.");
            sqle.printStackTrace();
            return false;
        }
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

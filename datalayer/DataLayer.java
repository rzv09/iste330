package datalayer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.*;

import static datalayer.RebuildTablesString.DEFINE_TABLES;

/**
 * File: DataLayer.java
 * Author: Raman Zatsarenko
 * This file implements data layer functionality
 */
public class DataLayer {

    public static final String URL = "jdbc:mysql://localhost/";
    public static final String DEFAULT_DRIVER = "com.mysql.cj.jdbc.Driver";
    public  static String userName = new String();
    public static String password = new String();


    private static Connection conn;
    private Statement stmt;
    private ResultSet rs;
    private String sql;

    /**
     * Loads the driver for the database
     * @return true if successful
     */
    public static boolean loadDriver() {
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


    /**
     * Create a connection with mysql database server
     * @param databaseName name of the database to be connected to
     * @return true if successful
     */
    public static boolean getConnection(String databaseName) {

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

    public boolean addFacultyMember(String firstName, String lastName, String email, String password,
                                    String phone, String address) {
        String passwordHash = encrypt(password);
        try {
            sql = "INSERT INTO Faculty(firstName, lastName, email, password, phone, address) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, email);
            ps.setString(4, passwordHash);
            ps.setString(5, phone);
            ps.setString(6, address);
            ps.executeUpdate();
            return true;
        }
        catch (SQLException sqle) {
            System.out.println("Error: Could not add a faculty member record to the Faculty table");
            return false;
        }

    }

    public boolean rebuildTables() {
        try {
            ScriptRunner scriptRunner = new ScriptRunner(conn, false, false);
            String file = "/mysql/Group5_Database.sql";
            scriptRunner.runScript(file);
            return true;
        }
        catch (IOException ioe) {
            System.out.println("Error: could not rebuild database tables.");
            ioe.printStackTrace();
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * set username for db connection
     * @param userName
     */
    public static void setUserName(String userName) {
        DataLayer.userName = userName;
    }

    /**
     * set password for db connection
     * @param password
     */
    public static void setPassword(String password) {
        DataLayer.password = password;
    }

    /**
     * Helper method to encrypt password using SHA1 algorithm
     * @param secret
     * @return
     */
    public String encrypt(String secret)
    {

        String sha1 = "";
        String value = new String(secret);
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update(value.getBytes("utf8"));
            sha1 = String.format("%040x", new BigInteger(1, digest.digest()));
        } catch (Exception e){
            e.printStackTrace();
        }// end of catch

        System.out.println( "The sha1 of \""+ value + "\" is:");
        System.out.println("--->" + sha1 );
        System.out.println();
        return sha1;
    }
}

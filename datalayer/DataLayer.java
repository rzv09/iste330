package datalayer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.*;

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

    /**
     * A driver method that adds a faculty member, their abstract, and a Faculty-Abstract Record
     * @return true if successful
     */
    public boolean addFacultyDriver(String firstName, String lastName, String email, String password,
                                    String phone, String address,
                                    String title, String description) {
        int facultyId = addFacultyMember(firstName, lastName, email, password, phone, address);
        int abstractId = addAbstract(title, description);
        try {
            sql = "INSERT INTO Faculty_Abstract(facultyID, abstractID) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, String.valueOf(facultyId));
            ps.setString(2, String.valueOf(abstractId));
            ps.executeUpdate();
            return true;
        }
        catch (SQLException sqle) {
            System.out.println("Error: could not add a faculty member's data to the db");
            sqle.printStackTrace();
            return false;
        }

    }

    public int addFacultyMember(String firstName, String lastName, String email, String password,
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
            sql = "SELECT LAST_INSERT_ID()";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery(sql);
            rs.next();
            String inserted = rs.getString(1);
            System.out.println("ID of inserted record is: " + inserted + "\n");
            return Integer.parseInt(inserted);
        }
        catch (SQLException sqle) {
            System.out.println("Error: Could not add a faculty member record to the Faculty table");
            return -1;
        }

    }

    public int addAbstract(String title, String description) {
        try {
            sql = "INSERT INTO Abstract(title, description) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, title);
            ps.setString(2, description);
            ps.executeUpdate();
            sql = "SELECT LAST_INSERT_ID()";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery(sql);
            rs.next();
            String inserted = rs.getString(1);
            System.out.println("ID of inserted record is: " + inserted + "\n");
            return Integer.parseInt(inserted);
        }
        catch (SQLException sqle) {
            System.out.println("Error: Could not add an abstract record to the Abstract table");
            sqle.printStackTrace();
            return -1;
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

    /**
     * add a student record to Student table
     * @param firstName
     * @param lastName
     * @param email
     * @param password
     * @return
     */
    public int addStudent(String firstName, String lastName, String email, String password) {
        String passwordHash = encrypt(password);
        try {
            sql = "INSERT INTO Student(firstName, lastName, email, password) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, email);
            ps.setString(4, passwordHash);
            ps.executeUpdate();
            sql = "SELECT LAST_INSERT_ID()";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery(sql);
            rs.next();
            String inserted = rs.getString(1);
            System.out.println("ID of inserted record is: " + inserted + "\n");
            return Integer.parseInt(inserted);
        }
        catch (SQLException sqle) {
            System.out.println("Error: Could not add a student record to the Student table");
            return -1;
        }
    }

    /**
     * Add a keyword record to Student_Topic
     * @param keyword
     * @return
     */
    public int addStudentTopic(String keyword) {
        try {
            sql = "INSERT INTO Student_Topic(keyword) VALUES (?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, keyword);
            ps.executeUpdate();
            sql = "SELECT LAST_INSERT_ID()";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery(sql);
            rs.next();
            String inserted = rs.getString(1);
            System.out.println("ID of inserted record is: " + inserted + "\n");
            return Integer.parseInt(inserted);
        }
        catch (SQLException sqle) {
            System.out.println("Error: Could not add a keyword record to the Student_Topic record");
            return -1;
        }
    }

    /**
     * Driver method that adds a student to the Student table, then adds a topic to the Student_Topic table,
     * then adds to Student_Keywords the IDs
     * @param firstName
     * @param lastName
     * @param email
     * @param password
     * @param keyword
     * @return
     */
    public boolean addStudentDriver(String firstName, String lastName, String email, String password,
                                String keyword) {
        int studentId = addStudent(firstName, lastName, email, password);
        int student_topicId = addStudentTopic(keyword);
        try {
            sql = "INSERT INTO Student_Keyword(studentID, keywordID) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, String.valueOf(studentId));
            ps.setString(2, String.valueOf(student_topicId));
            ps.executeUpdate();
            return true;
        }
        catch (SQLException sqle) {
            System.out.println("Error: could not add a student's data to the db");
            sqle.printStackTrace();
            return false;
        }

    }

    /**
     * add a student record to Student table
     * @param name
     * @param email
     * @param password
     * @return
     */
    public int addGuest(String name, String email, String password) {
        String passwordHash = encrypt(password);
        try {
            sql = "INSERT INTO Guest(name, email, password) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, passwordHash);
            ps.executeUpdate();
            sql = "SELECT LAST_INSERT_ID()";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery(sql);
            rs.next();
            String inserted = rs.getString(1);
            System.out.println("ID of inserted record is: " + inserted + "\n");
            return Integer.parseInt(inserted);
        }
        catch (SQLException sqle) {
            System.out.println("Error: Could not add a guest record to the Guest table");
            sqle.printStackTrace();
            return -1;
        }
    }

    /**
     * Add a keyword record to Student_Topic
     * @param keyword
     * @return
     */
    public int addGuestTopic(String keyword) {
        try {
            sql = "INSERT INTO Guest_Topic(keyword) VALUES (?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, keyword);
            ps.executeUpdate();
            sql = "SELECT LAST_INSERT_ID()";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery(sql);
            rs.next();
            String inserted = rs.getString(1);
            System.out.println("ID of inserted record is: " + inserted + "\n");
            return Integer.parseInt(inserted);
        }
        catch (SQLException sqle) {
            System.out.println("Error: Could not add a keyword record to the Guest_Topic record");
            return -1;
        }
    }

    public boolean addGuestDriver(String name, String email, String password, String keyword) {
        int guestId = addGuest(name, email, password);
        int guestTopicId = addGuestTopic(keyword);
        try {
            sql = "INSERT INTO Guest_Keyword(guestID, keywordID) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, String.valueOf(guestId));
            ps.setString(2, String.valueOf(guestTopicId));
            ps.executeUpdate();
            return true;
        }
        catch (SQLException sqle) {
            System.out.println("Error: could not add a guest's data to the db");
            sqle.printStackTrace();
            return false;
        }
    }




}

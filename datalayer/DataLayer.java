package datalayer;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.*;
import java.util.*;

/**
 * File: DataLayer.java
 * Author: Raman Zatsarenko
 * This file implements data layer functionality
 */
public class DataLayer {

    public static final String URL = "jdbc:mysql://localhost/";
    public static final String DEFAULT_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static String userName = new String();
    public static String password = new String();


    private static Connection conn;
    private ResultSet rs;
    private Statement stmt;
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
     * Add a keyword record to Faculty_Topic
     * @param keyword
     * @return
     */
    public int addFacultyTopic(String keyword) {
        try {
            sql = "INSERT INTO Faculty_Topic(keyword) VALUES (?)";
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
            System.out.println("Error: Could not add a keyword record to the Faculty_Topic record");
            return -1;
        }
    }

    /**
     * A driver method that adds a faculty member, their abstract, and a Faculty-Abstract Record
     * @return true if successful
     */
    public boolean addFacultyKeyword(int facultyId, int faculty_topicId) {
        try {
            sql = "INSERT INTO Faculty_Keyword(facultyID, keywordID) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, facultyId);
            ps.setInt(2, faculty_topicId);
            ps.executeUpdate();
            return true;
        }
        catch (SQLException sqle) {
            System.out.println("Error: Could Not Implement Faculty Keyword");
            sqle.printStackTrace();
            return false;
        }
    }

    /**
     * Author: Declan Naughton
     *
     * A getter method that prints a faculty member's first and last name, their
     * email address, and phone number.
     * @param facultyID
     * @return
     */
    public String printFacultyMember(int facultyID){
        try{
            sql = "SELECT firstName, lastName, email, phone FROM Faculty ";
            sql += "WHERE facultyID = ?"; //Search for the faculty member by their id.
            //Prepare the statement
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, facultyID);

            rs = ps.executeQuery(); //Resultset
            rs.next();

            String result = rs.getString(2); //lname
            result += ", "+rs.getString(1); //fname
            result += "\nEmail: "+rs.getString(3); //email
            result += "\nPhone Number: "+rs.getString(4); //phone number



            return result;
        } catch (SQLException sqle) {
            System.out.println("Error: Could not find a faculty member record to the Faculty table");
            return null;
        }
    }



    /**
     *
     * @param firstName
     * @param lastName
     * @param email
     * @param password
     * @param phone
     * @param address
     * @return
     */
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

    /**
     *
     * @param title
     * @param description
     * @return
     */
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

    /**
     * Author: Declan Naughton
     *
     * Assigns an abstract to the faculty member that created it.
     * @param facultyID
     * @param abstractID
     * @return
     */
    public boolean assignAbstract(int facultyID, int abstractID) {
        try {
            sql = "INSERT INTO Faculty_Abstract(facultyID, abstractID) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, facultyID);
            ps.setInt(2, abstractID);
            ps.executeUpdate();
            return true;
        }
        catch (SQLException sqle) {
            System.out.println("Error: Could Not Implement Faculty Abstract");
            sqle.printStackTrace();
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
     *
     * @param studentId
     * @param student_topicId
     * @return
     */
    public boolean addStudentKeyword(int studentId, int student_topicId) {
        try {
            sql = "INSERT INTO Student_Keyword(studentID, keywordID) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, studentId);
            ps.setInt(2, student_topicId);
            ps.executeUpdate();
            return true;
        }
        catch (SQLException sqle) {
            System.out.println("Error: Could Not Implement Student Keyword");
            sqle.printStackTrace();
            return false;
        }
    }



    /**
     * Author: Declan Naughton
     *
     * A getter method that stringifies a student's first and last name,
     * and their email address
     * @param studentID
     * @return
     */
    public String printStudent(int studentID){
        try{
            sql = "SELECT firstName, lastName, email FROM Student ";
            sql += "WHERE studentID = ?"; //Search for the faculty member by their id.
            //Prepare the statement
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, studentID);

            rs = ps.executeQuery(); //Resultset
            rs.next();

            String result = rs.getString(2); //lname
            result += ", "+rs.getString(1); //fname
            result += "\nEmail: "+rs.getString(3); //email



            return result;
        } catch (SQLException sqle) {
            System.out.println("Error: Could not find a faculty member record to the Faculty table");
            return null;
        }
    }



    /**
     * Add a student record to Student table
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

    /**
     *
     * @param guestId
     * @param guest_topicId
     * @return
     */
    public boolean addGuestKeyword(int guestId, int guest_topicId) {
        try {
            sql = "INSERT INTO Guest_Keyword(guestID, keywordID) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, guestId);
            ps.setInt(2, guest_topicId);
            ps.executeUpdate();
            return true;
        }
        catch (SQLException sqle) {
            System.out.println("Error: Could Not Implement Guest Keyword");
            sqle.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param studentID
     * @return
     */
    public Set<Integer> getStudentMatches(int studentID) {
        Set<Integer> ids = new HashSet<>();
        try {
            sql = "SELECT DISTINCT fk.facultyID FROM Faculty_Keyword fk " +
                    "JOIN Faculty_Topic ft on ft.keyword_ID = fk.keywordID " +
                    "WHERE keyword IN " +
                    "(SELECT keyword FROM Student_Keyword sk JOIN Student_Topic st ON st.keyword_ID = sk.keywordID WHERE sk.studentID = ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, studentID);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                ids.add(rs.getInt(1));
            }
            sql = "SELECT DISTINCT facultyID FROM Faculty_Abstract fa " +
                    "JOIN abstract a on fa.abstractID = a.abstractID " +
                    "WHERE description LIKE '%'||" +
                        "(SELECT GROUP_CONCAT(keyword SEPARATOR '|') FROM Student_Keyword sk " +
                        "JOIN Student_Topic st ON st.keyword_ID = sk.keywordID WHERE sk.studentID = ?)" +
                    "||'%'";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, studentID);
            rs = ps.executeQuery();
            while(rs.next()){
                ids.add(rs.getInt(1));
            }
        }
        catch (SQLException sqle) {
            System.out.println("Error: could not add a student's data to the db");
            sqle.printStackTrace();
        }
        return ids;
    }




}

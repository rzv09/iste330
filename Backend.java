import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Objects;

/**
 * ISTE 330.01 - Habermas
 * Group 5 - Group Project
 * Authors: Maple Riemer, Declan Naughton, Raman Zatsarenko, Raghav Sharma, Sai Charkadar Meruva
 */

public class Backend {
    private Connection conn;
    private Statement stmt;
    private PreparedStatement pstmt;
    private CallableStatement cstmt;
    private ResultSet rs;
    private String sql;

    // JDBC Type 4 Driver
    final String DEFAULT_DRIVER = "com.mysql.cj.jdbc.Driver";

    // Define Data Source
    final String url = "jdbc:mysql://localhost/CollegeConnection";

    /**
     * Connects to the database
     *
     * @param username Username for database connection
     * @param password Password for database connection
     * @return If the connection is successful
     */
    public boolean connect(String username, String password) {
        // load the driver
        try {
            Class.forName(DEFAULT_DRIVER);
            System.out.println("Succefully loaded driver class: \n" + DEFAULT_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("Unable to load driver class: \n" + DEFAULT_DRIVER);
            System.out.println(e);
            return false;
        } // end of try/catch

        // create a connection
        boolean connected = true;

        String user = username.strip();
        String pass = password.strip();

        try {
            if ((Objects.equals(user, "") || user == null) && (Objects.equals(pass, "") || pass == null)) {
                conn = DriverManager.getConnection(url + "?serverTimezone=UTC", "root", "Student12345");
            } else if (Objects.equals(pass, "") || pass == null) {
                conn = DriverManager.getConnection(url, user, "student");
            } else {
                conn = DriverManager.getConnection(url + "?serverTimezone=UTC", user, password);
            } // end of if/else
        } catch (SQLException se) {
            System.out.println("Error occured while attempting to connect");
            se.printStackTrace();
            connected = false;
        } // end of try/catch

        if (connected) {
            System.out.println("Connected to data source:\n" + url);
            return true;
        } else {
            System.out.println("Unable to connect to data source:\n" + url);
            return false;
        } //end of if/else
    } // end of connect

    /**
     * Closes the database connection
     */
    public void close() {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (pstmt != null) pstmt.close();
            if (cstmt != null) cstmt.close();
            if (conn != null) conn.close();
        } catch (SQLException se) {
            System.out.println("Error occured at closing");
            se.printStackTrace();
            System.exit(1);
        } //end of try/catch
        java.util.Date now = new java.util.Date();
        System.out.println("\nEnd of program\nTerminated at " + now);
        System.exit(0);
    } // end of close

    /**
     * Adds a faculty member.
     *
     * @param fName          Faculty First Name
     * @param lName          Faculty Last Name
     * @param email          Faculty Email
     * @param buildingNumber Faculty Building Number
     * @param officeNumber   Faculty Office Number
     * @return int - the id of the faculty member added. Returns 0 if the method failed.
     */
    public int addFaculty(String fName, String lName, String email, int buildingNumber, int officeNumber) {
        try {

            if (buildingNumber == 0 && officeNumber == 0) {
                pstmt = conn.prepareStatement("INSERT INTO faculty (lastName, firstName, email) VALUES (?, ?, ?)");
                pstmt.setString(1, lName.strip());
                pstmt.setString(2, fName.strip());
                pstmt.setString(3, email.strip());
            } else if (buildingNumber == 0) {
                pstmt = conn.prepareStatement("INSERT INTO faculty (lastName, firstName, email, officeNumber) VALUES (?, ?, ?, ?)");
                pstmt.setString(1, lName.strip());
                pstmt.setString(2, fName.strip());
                pstmt.setString(3, email.strip());
                pstmt.setInt(4, officeNumber);
            } else if (officeNumber == 0) {
                pstmt = conn.prepareStatement("INSERT INTO faculty (lastName, firstName, email, buildingNumber) VALUES (?, ?, ?, ?)");
                pstmt.setString(1, lName.strip());
                pstmt.setString(2, fName.strip());
                pstmt.setString(3, email.strip());
                pstmt.setInt(4, buildingNumber);
            } else {
                pstmt = conn.prepareStatement("INSERT INTO faculty (lastName, firstName, email, buildingNumber, officeNumber) VALUES (?, ?, ?, ?, ?)");
                pstmt.setString(1, lName.strip());
                pstmt.setString(2, fName.strip());
                pstmt.setString(3, email.strip());
                pstmt.setInt(4, buildingNumber);
                pstmt.setInt(5, officeNumber);
            }

            int rows = pstmt.executeUpdate();
            System.out.println(rows + " rows updated.");

            if (rows > 0) {
                stmt = conn.createStatement();
                rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");
                rs.next();
                return rs.getInt("LAST_INSERT_ID()");
            } else {
                return 0;
            }
        } catch (SQLException se) {
            System.out.println("Error occured while attempting to insert record");
            se.printStackTrace();
            return 0;
        }
    }

    /**
     * Queries information on a faculty member with the given id to check if
     * they exist.
     *
     * @param id Faculty ID
     * @return If the faculty member is valid or not
     */
    public boolean validateFaculty(int id) {
        try {
            pstmt = conn.prepareStatement("SELECT facultyID FROM faculty WHERE facultyID = ?");
            pstmt.setInt(1, id);

            rs = pstmt.executeQuery();

            rs.next();
            return rs.getInt("facultyID") >= 100;
        } catch (SQLException se) {
            System.out.println("Error occured while attempting to select record");
            se.printStackTrace();
            return false;
        }
    }

    /**
     * Adds an abstract to a faculty member
     *
     * @param facultyID    Faculty Member's ID
     * @param abstractText Abstract's Text
     */
    public void addRecord(int facultyID, String abstractText) {
        try {
            cstmt = conn.prepareCall("{CALL buildFacultyAbs(?, ?)}");
            cstmt.setInt(1, facultyID);
            cstmt.setString(2, abstractText.strip());
            cstmt.executeQuery();
        } catch (SQLException se) {
            System.out.println("Error occured while attempting to insert record");
            se.printStackTrace();
        }
    }

    /**
     * Adds a student to the database
     *
     * @param fName Student's First Name
     * @param lName Student's Last Name
     * @param email Student's Email
     * @return the ID of the student added. Will return 0 if the method failed.
     */
    public int addStudent(String fName, String lName, String email) {
        try {
            pstmt = conn.prepareStatement("INSERT INTO student (lastName, firstName, email) VALUES (?, ?, ?)");
            pstmt.setString(1, lName.strip());
            pstmt.setString(2, fName.strip());
            pstmt.setString(3, email.strip());

            int rows = pstmt.executeUpdate();
            System.out.println(rows + " rows updated.");

            if (rows > 0) {
                stmt = conn.createStatement();
                rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");
                rs.next();
                return rs.getInt("LAST_INSERT_ID()");
            } else {
                return 0;
            }
        } catch (SQLException se) {
            System.out.println("Error occured while attempting to insert record");
            se.printStackTrace();
            return 0;
        }
    }

    /**
     * Validates that a student is in the system
     *
     * @param id Student ID
     * @return If the Student ID provided is valid
     */
    public boolean validateStudent(int id) {
        try {
            pstmt = conn.prepareStatement("SELECT studentID FROM student WHERE studentID = ?");
            pstmt.setInt(1, id);

            rs = pstmt.executeQuery();

            rs.next();
            return rs.getInt("studentID") >= 100;
        } catch (SQLException se) {
            System.out.println("Error occured while attempting to select record");
            se.printStackTrace();
            return false;
        }
    }

    /**
     * Searches for a given set of keyword(s), either from Faculty Abstracts/Keywords or Student Keywords
     *
     * @param type         Search Type, either "faculty" or "student"
     * @param keywordOne   First Keyword
     * @param keywordTwo   Second Keyword
     * @param keywordThree Third Keyword
     */
    public void searchKeywords(String type, String keywordOne, String keywordTwo, String keywordThree) {
        ResultSet rs;
        try {
            switch (type) {
                case "student" -> cstmt = conn.prepareCall("{CALL Student_Keyword_Lookup(?, ?, ?)}");
                case "faculty" -> cstmt = conn.prepareCall("{CALL Faculty_Keyword_Lookup(?, ?, ?)}");
            }
            cstmt.setString(1, keywordOne);
            cstmt.setString(2, keywordTwo);
            cstmt.setString(3, keywordThree);

            rs = cstmt.executeQuery();
            boolean header = true;
            switch (type) {
                case "student":
                    while (rs.next()) {
                        if (header) {
                            header = false;
                            System.out.println("Matching Student(s):");
                        }
                        System.out.println("Last Name: " +
                                rs.getString("lastName") + " | " + "First Name: " +
                                rs.getString("firstName") + " | " + "Email Address: " +
                                rs.getString("email"));
                    }
                    break;
                case "faculty":
                    while (rs.next()) {
                        if (header) {
                            header = false;
                            System.out.println("Matching Faculty Member(s):");
                        }
                        System.out.println("Last Name: " +
                                rs.getString("lastName") + " | " + "First Name: " +
                                rs.getString("firstName") + " | " + "Email Address: " +
                                rs.getString("email") + " | " + "Building Number: " +
                                rs.getString("buildingNumber") + " | " + "Office Number: " +
                                rs.getString("officeNumber"));
                    }
                    break;
            }
        } catch (SQLException se) {
            System.out.println("Error occured while attempting to search keywords");
            se.printStackTrace();
        }


    }


    /**
     * Adds a Keyword to a given student or faculty
     *
     * @param type Type of user being given a keyword, either "student" or "faculty"
     * @param id   Faculty/Student ID
     * @param word Keyword Text
     */
    public void addKeyword(String type, int id, String word) {
        try {
            switch (type) {
                case "student" -> {
                    //Add student keyword
                    System.out.println("Preparing to add student keyword...");
                    cstmt = conn.prepareCall("{CALL addStudentKeyword(?, ?)}");
                }
                case "faculty" -> {
                    //Add faculty keyword
                    System.out.println("Preparing to add faculty keyword...");
                    cstmt = conn.prepareCall("{CALL addFacultyKeyword(?, ?)}");
                }
            } //End of switch
            cstmt.setInt(1, id);
            cstmt.setString(2, word);
            cstmt.executeUpdate();
            System.out.println("Keyword Added!");
        } catch (SQLException se) {
            System.out.println("Error occured while attempting to add keyword.");
            se.printStackTrace();
        }
    } //End of addKeyword


    /**
     * Removed a Keyword from a given student or faculty
     *
     * @param type Type of user whose Keyword is being deleted, either "student" or "faculty"
     * @param id   Faculty/Student ID
     * @param word Keyword Text
     */
    public void removeKeyword(String type, int id, String word) {
        try {
            switch (type) {
                case "student" -> {
                    //remove student keyword if the type is 's'
                    System.out.println("Preparing to remove student keyword...");
                    cstmt = conn.prepareCall("{CALL removeStudentKeyword(?, ?)}");
                }
                case "faculty" -> {
                    //remove faculty keyword if the type is 'f'
                    System.out.println("Preparing to remove faculty keyword...");
                    cstmt = conn.prepareCall("{CALL removeFacultyKeyword(?, ?)}");
                }
            } //End of switch

            cstmt.setInt(1, id);
            cstmt.setString(2, word);
            cstmt.executeUpdate();
            System.out.println("Keyword Removed!");
        } catch (SQLException se) {
            System.out.println("Error occured while attempting to remove keyword.");
            se.printStackTrace();
        }
    } //End of addKeyword
}
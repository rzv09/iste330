import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * ISTE 330.01 - Habermas
 * Group 5 - Project Part 1
 *	Authors:Raghav, Charki,Maple, Ramanz,
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

   /*
    * Creates the database connection
    * Returns true if the connection was successful
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
         if((user == "" || user == null) && (pass == "" || pass == null)) {
            conn = DriverManager.getConnection(url + "?serverTimezone=UTC", "root", "Student12345");
         } else if (pass == "" || pass == null) {
            conn = DriverManager.getConnection(url, user, "student");
         } else {
			   conn = DriverManager.getConnection(url + "?serverTimezone=UTC", user, password);
         } // end of if/else
		} catch (SQLException se) {
         System.out.println("Error occured while attempting to connect");
         se.printStackTrace();
			connected = false;
		} // end of try/catch

		if(connected) {
			System.out.println("Connected to data source:\n" + url);
         return true;
		} else {
			System.out.println("Unable to connect to data source:\n" + url);
         return false;
		} //end of if/else
	} // end of connect

   /*
    * Closes the database connection
    */
   public void close() {
      try {
			if(rs != null) rs.close();
			if(stmt != null) stmt.close();
         if(pstmt != null) pstmt.close();
         if(cstmt != null) cstmt.close();
			if(conn != null) conn.close();
		} catch(SQLException se) {
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
    * @param fName
    * @param lName
    * @param email
    * @param buildingNumber
    * @param officeNumber
    * @return int - the id of the faculty member added. Returns 0 if the method failed.
    */
   public int addFaculty(String fName, String lName, String email, int buildingNumber, int officeNumber) {
      try {

         if(buildingNumber == 0 && officeNumber == 0) {
            pstmt = conn.prepareStatement("INSERT INTO faculty (lastName, firstName, email) VALUES (?, ?, ?)");
            pstmt.setString(1, lName.strip());
            pstmt.setString(2, fName.strip());
            pstmt.setString(3, email.strip());
         } else if(buildingNumber == 0) {
            pstmt = conn.prepareStatement("INSERT INTO faculty (lastName, firstName, email, officeNumber) VALUES (?, ?, ?, ?)");
            pstmt.setString(1, lName.strip());
            pstmt.setString(2, fName.strip());
            pstmt.setString(3, email.strip());
            pstmt.setInt(4, officeNumber);
         } else if(officeNumber == 0) {
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

         if(rows > 0){
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");
            rs.next();
            return rs.getInt("LAST_INSERT_ID()");
         } else {
            return 0;
         }
      } catch(SQLException se) {
         System.out.println("Error occured while attempting to insert record");
         se.printStackTrace();
         return 0;
      }
   }

   /**
    * Queries information on a faculty member with the given id to check if
    * they exist.
    * @param id
    * @return true if the faculty member is valid
    */
   public boolean validateFaculty(int id) {
      try {
         pstmt = conn.prepareStatement("SELECT facultyID FROM faculty WHERE facultyID = ?");
         pstmt.setInt(1, id);

         rs = pstmt.executeQuery();

         rs.next();
         if(rs.getInt("facultyID") >= 100) {
            return true;
         } else {
            return false;
         }
      } catch(SQLException se) {
         System.out.println("Error occured while attempting to select record");
         se.printStackTrace();
         return false;
      }
   }

   /**
    * Adds an abstract to a faculty member
    * @param facultyID
    * @param abstractText
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
    * @param fName
    * @param lName
    * @param email
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

         if(rows > 0){
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");
            rs.next();
            return rs.getInt("LAST_INSERT_ID()");
         } else {
            return 0;
         }
      } catch(SQLException se) {
         System.out.println("Error occured while attempting to insert record");
         se.printStackTrace();
         return 0;
      }
   }

   public boolean validateStudent(int id) {
      try {
         pstmt = conn.prepareStatement("SELECT studentID FROM student WHERE studentID = ?");
         pstmt.setInt(1, id);

         rs = pstmt.executeQuery();

         rs.next();
         if(rs.getInt("studentID") >= 100) {
            return true;
         } else {
            return false;
         }
      } catch(SQLException se) {
         System.out.println("Error occured while attempting to select record");
         se.printStackTrace();
         return false;
      }
   }

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

         switch(type){
            case "student":
               while(rs.next()) {
                  System.out.println("\nMatching Student:\nLast Name: " +
                          rs.getString("lastName") + " | " + "First Name: " +
                          rs.getString("firstName") + " | " + "Email Address: " +
                          rs.getString("email"));
               }
               break;
            case "faculty":
               while(rs.next()) {
                  System.out.println("\nMatching Faculty Member:\nLast Name: " +
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
    * A function which allows one to add a keyword to a person.
    * @param type - a character indicating the type of person the keyword is being added to. 'f' for faculty, 's' for student.
    * @param id
    * @param word
    */
   public void addKeyword(char type, int id, String word){
      try{
         switch(type){
            case 's':
               //Add student keyword if the type is 's'
               System.out.println("Preparing to add student keyword...");
               cstmt = conn.prepareCall("{CALL addStudentKeyword(?, ?)}");
               break;
            case 'f':
               //Add faculty keyword if the type is 'f'
               System.out.println("Preparing to add faculty keyword...");
               cstmt = conn.prepareCall("{CALL addFacultyKeyword(?, ?)}");
               break;
         } //End of switch

         cstmt.setInt(1, id);
         cstmt.setString(2, word);
         cstmt.executeUpdate();
      } catch (SQLException se) {
         System.out.println("Error occured while attempting to add keyword.");
         se.printStackTrace();
      }
   } //End of addKeyword


   /**
    * A function which removes a keyword from a person.
    * @param type - a character indicating the type of person the keyword is being added to. 'f' for faculty, 's' for student.
    * @param id
    * @param word
    */
    public void removeKeyword(char type, int id, String word){
      try{
         switch(type){
            case 's':
               //remove student keyword if the type is 's'
               System.out.println("Preparing to remove student keyword...");
               cstmt = conn.prepareCall("{CALL removeStudentKeyword(?, ?)}");
               break;
            case 'f':
               //remove faculty keyword if the type is 'f'
               System.out.println("Preparing to remove faculty keyword...");
               cstmt = conn.prepareCall("{CALL removeFacultyKeyword(?, ?)}");
               break;
         } //End of switch

         cstmt.setInt(1, id);
         cstmt.setString(2, word);
         cstmt.executeUpdate();
      } catch (SQLException se) {
         System.out.println("Error occured while attempting to remove keyword.");
         se.printStackTrace();
      }
   } //End of addKeyword
}
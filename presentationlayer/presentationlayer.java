/**
 * File: presentationlayer.java
 * Author: Declan Naughton
 * A file which will handle all of the front end interaction.
 */
package presentationlayer;
import datalayer.DataLayer;
import java.io.*;
import java.sql.*;
import java.util.Scanner;
import java.util.*;

//Import gui stuff
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class presentationlayer {
    DataLayer dl = new DataLayer(); //The datalayer
    private Scanner scanner = new Scanner(System.in);

    public static Font defaultFont = new Font("Courier", Font.PLAIN, 32); //The default font

    presentationlayer(){
        try{
            dl.loadDriver(); //Load the driver
        } catch (Exception e){
            System.out.println("Error: Could not load driver.");
            e.printStackTrace();
        }
        
    }

    /**
     * A method which receives user input from the command line, and provides the
     * user with options accordingly.
     */
    private void interpretOption(){
        int user_selection = 0; //The variable that stores what the user input.

        System.out.println("\nSelect an Option:");
        System.out.println("\t 1: \t Faculty");
        System.out.println("\t 2: \t Student");
        System.out.println("\t 3: \t Guest");
        System.out.println("\t 4: \t Rebuild Tables");
        System.out.println("\t 5: \t Exit");
        //Query the user for an option.
        System.out.print("\n Selection: ");
        String str_input = scanner.nextLine(); //The variable that stores the string input

        try{
            user_selection = Integer.parseInt(str_input); //Convert string to integer.

            switch(user_selection){
                case 1:
                    facultyCommands();
                    break;
                case 2:
                    studentCommands();
                    break;
                case 3:
                    guestCommands();
                    break;
                case 4: //Rebuild Tables
                    dl.rebuildTables();
                    break;
                case 5: //Exit
                    break;
                default:
                    System.out.println("Unrecognized selection...");
            }//End of switch

            if(user_selection != 5){
                interpretOption(); //Loop
            } else {
                return;
            }
        }//End of try
        catch(Exception e) {
            System.out.println("ERROR: could not parse selection to integer.");
            e.printStackTrace();
        }
    } //End of interpretOption




    /**
     * A method which presents the user with commands regarding the faculty.
     */
    private void facultyCommands(){
        int user_command = 0; //The variable that stores what the user input.

        System.out.println("\nSelect a Command:");
        System.out.println("\t 1: \t Find Faculty Member");
        System.out.println("\t 2: \t Add Faculty Member");
        System.out.println("\t 3: \t Add Faculty Abstract");
        System.out.println("\t 4: \t Add Faculty Topic");
        System.out.println("\t 5: \t Back");
        //Query the user for an option.
        System.out.print("\n Selection: ");
        String str_input = scanner.nextLine(); //The variable that stores the string input

        try{
            user_command = Integer.parseInt(str_input); //Convert string to integer.

            switch(user_command){
                case 1:
                    System.out.print("\nFacultyID: ");
                    str_input = scanner.nextLine(); //The variable that stores the string input
                    user_command = Integer.parseInt(str_input); //Convert string to integer.

                    String output = dl.printFacultyMember(user_command);
                    System.out.println(output);
                    break;
                case 2: //Add faculty member
                    System.out.println("\n Please enter faculty ");

                    System.out.print("\n\tFirst Name: ");
                    String fname = scanner.nextLine();

                    System.out.print("\n\tLast Name: ");
                    String lname = scanner.nextLine();

                    System.out.print("\n\tEmail: ");
                    String email = scanner.nextLine();

                    //Will need to modify this line so that it requires a user 
                    String password = PassMask.mask();

                    System.out.print("\n\tPhone: ");
                    String phone = scanner.nextLine();

                    System.out.print("\n\tAddress: ");
                    str_input = scanner.nextLine(); //Faculty address


                    dl.addFacultyMember(fname, lname, email, password, phone, str_input);
                    break;
                case 3: //Add faculty abstract
                    System.out.print("\n Faculty ID: ");
                    str_input = scanner.nextLine(); //get the faculty ID
                    user_command = Integer.parseInt(str_input); //convert ID to integer.

                    System.out.println("Please enter the abstract ");
                    System.out.print("\n\tTitle: ");
                    
                    str_input = scanner.nextLine(); //Get the abstract title.
                    String title = str_input; //Get the title

                    System.out.println("\n\tDescription: ");
                    str_input = scanner.nextLine(); //Get the description

                    int abstractID = dl.addAbstract(title, str_input); //Add the faculty abstract to the table
                    if(abstractID == -1){
                        //Do nothing if the function failed
                    } else{
                        dl.assignAbstract(user_command, abstractID);
                    }
                    break;

                case 4: //Add faculty topic
                    System.out.print("\n Faculty ID: ");
                    str_input = scanner.nextLine(); //get the faculty ID
                    user_command = Integer.parseInt(str_input); //convert ID to integer.

                    System.out.print("\n\t Topic: ");
                    str_input = scanner.nextLine(); //Get the description

                    int topicID = dl.addFacultyTopic(str_input); //Add the faculty abstract to the table
                    if(topicID == -1){
                        //Do nothing if the function failed
                    } else{
                        dl.addFacultyKeyword(user_command, topicID);
                    }
                    break;

                case 5: //Back
                    break;
                default:
                    System.out.println("Unrecognized selection...");
                    facultyCommands();
                    break;
            }//End of switch
        }//End of try
        catch(Exception e) {
            System.out.println("ERROR: could not parse selection to integer.");
            e.printStackTrace();
        }
    } //End of facultyCommands



    /**
     * A method which presents the user with commands regarding students.
     */
    private void studentCommands(){
        int user_command = 0; //The variable that stores what the user input.

        System.out.println("\nSelect a Command:");
        System.out.println("\t 1: \t Find Student");
        System.out.println("\t 2: \t Add Student");
        System.out.println("\t 3: \t Add Student Topic");
        System.out.println("\t 4: \t Find Student Match");
        System.out.println("\t 5: \t Back");
        //Query the user for an option.
        System.out.print("\n Selection: ");
        String str_input = scanner.nextLine(); //The variable that stores the string input

        try{
            user_command = Integer.parseInt(str_input); //Convert string to integer.

            switch(user_command){
                case 1:
                    System.out.print("\nStudent ID: ");
                    str_input = scanner.nextLine(); //The variable that stores the string input
                    user_command = Integer.parseInt(str_input); //Convert string to integer.

                    String o_str = dl.printStudent(user_command);
                    System.out.println(o_str);
                    break;
                case 2: //Add student
                    System.out.println("\n Please enter student ");

                    System.out.print("\n\tFirst Name: ");
                    String fname = scanner.nextLine();

                    System.out.print("\n\tLast Name: ");
                    String lname = scanner.nextLine();

                    System.out.print("\n\tEmail: ");
                    String email = scanner.nextLine();

                    //Will need to modify this line so that it requires a user 
                    String password = PassMask.mask();


                    dl.addStudent(fname, lname, email, password);
                    break;
                case 3: //Add student topic
                    System.out.print("\n Student ID: ");
                    str_input = scanner.nextLine(); //get the student ID
                    user_command = Integer.parseInt(str_input); //convert ID to integer.

                    System.out.println("Please enter the topic ");
                    System.out.print("\n\tTopic: ");
                    
                    str_input = scanner.nextLine(); //Get the keyword.


                    int topicID = dl.addStudentTopic(str_input); //Add the student topic to the table
                    if(topicID == -1){
                        //Do nothing if the method failed
                    } else{
                        //Insert into interim table
                        dl.addStudentKeyword(user_command, topicID);
                        System.out.println("Topic added to student.");
                    }
                    break;

                case 4: //Match a student to professors with similar interests
                    System.out.print("\n Student ID: ");
                    str_input = scanner.nextLine(); //get the student ID
                    user_command = Integer.parseInt(str_input); //convert ID to integer.
                    Set<Integer> IDs = dl.getStudentMatches(user_command);

                    Iterator<Integer> iter = IDs.iterator();
                    System.out.println("Possible Faculty Matches: ");
                    while(iter.hasNext()){
                        int i = iter.next();
                        String output = dl.printFacultyMember(i);
                        output += "\n Faculty ID: "+i;
                        System.out.println(output);
                    }
                    break;

                case 5: //Back
                    break;
                default:
                    System.out.println("Unrecognized selection...");
                    studentCommands();
                    break;
            }//End of switch
        }//End of try
        catch(Exception e) {
            System.out.println("ERROR: could not parse selection to integer.");
            e.printStackTrace();
        }
    } //End of studentCommands




    /**
     * A method which presents the user with commands regarding guests.
     */
    private void guestCommands(){
        int user_command = 0; //The variable that stores what the user input.

        System.out.println("\nSelect a Command:");
        System.out.println("\t 1: \t Find Guest");
        System.out.println("\t 2: \t Add Guest");
        System.out.println("\t 3: \t Add Guest Topic");
        System.out.println("\t 4: \t Back");
        //Query the user for an option.
        System.out.print("\n Selection: ");
        String str_input = scanner.nextLine(); //The variable that stores the string input

        try{
            user_command = Integer.parseInt(str_input); //Convert string to integer.

            switch(user_command){
                case 1:
                    System.out.print("GuestID: ");
                    str_input = scanner.nextLine();
                    user_command = Integer.parseInt(str_input);
                    System.out.println(dl.printGuest(user_command));
                    break;
                case 2: //Add guest
                    System.out.println("\n Please enter guest ");

                    System.out.print("\n\tName: ");
                    String name = scanner.nextLine();

                    System.out.print("\n\tEmail: ");
                    String email = scanner.nextLine();

                    //Will need to modify this line so that it requires a user to confirm
                    //the password
                    String password = PassMask.mask();


                    dl.addGuest(name, email, password);
                    break;
                case 3: //Add student topic
                    System.out.print("\n Guest ID: ");
                    str_input = scanner.nextLine(); //get the guest ID
                    user_command = Integer.parseInt(str_input); //convert ID to integer.

                    System.out.println("Please enter the topic ");
                    System.out.print("\n\tTopic: ");
                    
                    str_input = scanner.nextLine(); //Get the keyword.


                    int topicID = dl.addGuestTopic(str_input); //Add the guest topic to the table
                    if(topicID == -1){
                        //Do nothing if the method failed
                    } else{
                        //Insert into interim table
                        dl.addStudentKeyword(user_command, topicID);
                        System.out.println("Topic added to guest.");
                    }
                    break;
                
                case 4: //Find match
                    System.out.print("\n Guest ID: ");
                    str_input = scanner.nextLine(); //get the guest ID
                    user_command = Integer.parseInt(str_input); //convert ID to integer.
                    Set<Integer> IDs = dl.getGuestMatches(user_command);
                    Iterator<Integer> iter = IDs.iterator();
                    System.out.println("Possible Faculty Matches: ");
                    while(iter.hasNext()){
                        int i = iter.next();
                        String output = dl.printFacultyMember(i);
                        output += "\n Faculty ID: "+i;
                        System.out.println(output);
                    }
                    break;
                
                case 5: //Back
                    break;
                default:
                    System.out.println("Unrecognized selection...");
                    guestCommands();
                    break;
            }//End of switch
        }//End of try
        catch(Exception e) {
            System.out.println("ERROR: could not parse selection to integer.");
            e.printStackTrace();
        }
    } //End of guestCommands



    /**
     * A method which creates and runs the gui for logging into the
     * SQL server.
     * 
     * Author: Declan Naughton
     */
    public void loginSQL(){
        //Create login panel
        JPanel Inputbox = new JPanel(new GridLayout(3,2));

        //Create database input
        JLabel lblDB = new JLabel("SQL Database Name -> ");
            JTextField tfDB = new JTextField("CollegeConnection");
            //Add field to box.
            Inputbox.add(lblDB);
            Inputbox.add(tfDB);
            //Set Font
            lblDB.setFont(defaultFont);
            tfDB.setFont(defaultFont);
            //Set Font Colour
            tfDB.setForeground(Color.BLUE);
        //Create username input
        JLabel lblUser     = new JLabel("SQL Username  -> ");
            JTextField tfUser     = new JTextField("root");
            //Add field to box.
            Inputbox.add(lblUser);
		    Inputbox.add(tfUser);
            //Set Font
            lblUser.setFont(defaultFont);
            tfUser.setFont(defaultFont);
            //Set font colour
            tfUser.setForeground(Color.BLUE);
        //Create password input
		JLabel lblPassword = new JLabel("SQL Password  -> ");
            JTextField tfPassword = new JPasswordField("");
            //Set Font
            lblPassword.setFont(defaultFont);
            tfPassword.setFont(defaultFont);
            //Set Font colour
            tfPassword.setForeground(Color.BLUE);
            //Add to inputbox
            Inputbox.add(lblPassword);
            Inputbox.add(tfPassword);
        
            //Set the title of the input box
        JOptionPane.showMessageDialog(null, Inputbox,
            "SQL Login", JOptionPane.INFORMATION_MESSAGE);
        
        String uName = tfUser.getText(); //Get the username entered
        String password = tfPassword.getText(); //Get the password entered
        String DBName = tfDB.getText(); //Get the database name

        dl.setUserName(uName);
        dl.setPassword(password);
        dl.getConnection(DBName);
    }








    public static void main(String[] args) {
        
    }
}

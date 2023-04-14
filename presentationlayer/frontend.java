/**
 * File: frontend.java
 * Author: Declan Naughton
 * A file which will handle all of the front end interaction.
 */
package presentationlayer;
import datalayer.DataLayer;
import presentationlayer.PassMask;
import java.io.*;
import java.sql.*;
import java.util.Scanner;
import java.util.*;


public class frontend {
    DataLayer dl = new DataLayer(); //The datalayer
    private Scanner scanner = new Scanner(System.in);

    frontend(){
        try{
            dl.loadDriver(); //Load the driver
            
            System.out.print("\nUsername: ");
            dl.setUserName(scanner.nextLine());
            
            
            String pass;
            try{
                pass = PassMask.mask();
            } catch (Exception e){
                System.out.println("Warning: Unable to hide your password. Please proceed with caution.");
                System.out.print("\nPassword: ");
                pass = scanner.nextLine();
            }
            //System.out.println(pass);
            dl.setPassword(pass);

            dl.getConnection("CollegeConnection");

            interpretOption();


        } catch (Exception e){
            
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
        System.out.println("\t 5: \t Add Faculty Topic");
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

                    String o_str = dl.printFacultyMember(user_command);
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
                    System.out.println("ERROR: this command is not operational at this time. Please select a different one.");
                    guestCommands();
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
                case 4: //Back
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








    public static void main(String[] args) {
        frontend fr = new frontend();
    }
}

/**
 * File: frontend.java
 * Author: Declan Naughton
 * A file which will handle all of the front end interaction.
 */
package presentationlayer;
import datalayer.DataLayer;
import java.io.*;
import java.sql.*;
import java.util.Scanner;


public class frontend {
    DataLayer dl = new DataLayer(); //The datalayer
    private Scanner scanner = new Scanner(System.in);

    frontend(){
        try{
            dl.loadDriver(); //Load the driver
            
            System.out.print("\nUsername: ");
            dl.setUserName(scanner.nextLine());
            
            
            System.out.print("\nPassword: ");
            dl.setPassword(scanner.nextLine());

            
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
                    break;
                case 3:
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
        System.out.println("\t 4: \t Back");
        //Query the user for an option.
        System.out.print("\n Selection: ");
        String str_input = scanner.nextLine(); //The variable that stores the string input

        try{
            user_command = Integer.parseInt(str_input); //Convert string to integer.

            switch(user_command){
                case 1:
                    System.out.println("ERROR: this command is not operational at this time. Please select a different one.");
                    facultyCommands();
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
                    System.out.print("\n\tPassword: ");
                    String password = scanner.nextLine();

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
                        //
                    }
                    break;
                case 4: //Back
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




    private void studentCommands(){
        int user_command = 0; //The variable that stores what the user input.

        System.out.println("\nSelect a Command:");
        System.out.println("\t 1: \t Find Student");
        System.out.println("\t 2: \t Add Student");
        System.out.println("\t 3: \t Add Student Topic");
        System.out.println("\t 4: \t Back");
        //Query the user for an option.
        System.out.print("\n Selection: ");
        String str_input = scanner.nextLine(); //The variable that stores the string input

        try{
            user_command = Integer.parseInt(str_input); //Convert string to integer.

            switch(user_command){
                case 1:
                    System.out.println("ERROR: this command is not operational at this time. Please select a different one.");
                    studentCommands();
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
                    System.out.print("\n\tPassword: ");
                    String password = scanner.nextLine();


                    dl.addStudent(fname, lname, email, password);
                    break;
                case 3: //Add student topic
                    System.out.print("\n Student ID: ");
                    str_input = scanner.nextLine(); //get the student ID
                    user_command = Integer.parseInt(str_input); //convert ID to integer.

                    System.out.println("Please enter the topic ");
                    System.out.print("\n\tTopic: ");
                    
                    str_input = scanner.nextLine(); //Get the keyword.


                    int topicID = dl.addStudentTopic(str_input); //Add the faculty abstract to the table
                    if(topicID == -1){
                        //Do nothing if the function failed
                    } else{
                        //Insert into interim table
                    }
                    break;
                case 4: //Back
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
    } //End of facultyCommands








    public static void main(String[] args) {
        frontend fr = new frontend();
    }
}

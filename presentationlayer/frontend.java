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
        dl.loadDriver(); //Load the driver
        if(dl.getConnection("CollegeConnections")){
            System.out.println("Connection to CollegeConnections successful!");
        } else {

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
        System.out.println("\t 4: \t Add Faculty Keyword");
        System.out.println("\t 5: \t Back");
        //Query the user for an option.
        System.out.print("\n Selection: ");
        String str_input = scanner.nextLine(); //The variable that stores the string input

        try{
            user_command = Integer.parseInt(str_input); //Convert string to integer.

            switch(user_command){
                case 1:
                    break;
                case 2:
                    break;
                case 3: //Add faculty abstract
                    break;
                case 4: //Add faculty keyword
                    System.out.println("\nEnter Faculty Topic");
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








    public static void main(String[] args) {
        frontend fr = new frontend();
    }
}

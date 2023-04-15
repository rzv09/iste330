package presentationlayer;
import datalayer.DataLayer;
import presentationlayer.PassMask;
import java.io.*;
import java.sql.*;
import java.util.Scanner;
import java.util.*;


public class showoff {
    DataLayer dl = new DataLayer(); //The datalayer
    private Scanner scanner = new Scanner(System.in);

    showoff(){
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
        } catch (Exception e){
            
        }
    }
}

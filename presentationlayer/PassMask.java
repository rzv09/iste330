package presentationlayer;
import java.io.*;

/**
 * File: MaskedThread.java
 * Author: Declan Naughton
 * A class which 
 */
public class PassMask extends Thread{
    private static char echochar = '•';
    
    public static String mask(){
        Console console = System.console();
        try{
            char[] passArr = console.readPassword("Password: ");
            int len = passArr.length;
            for(int i = 0; i<len; i++){
                System.out.print(echochar);
            }
            System.out.println();

            return passArr.toString();
        } catch (Exception e){
            System.out.println("ERROR: could not find console.");
            e.printStackTrace();
            return null;
        }
    }
    

}

package presentationlayer;
import java.io.*;

/**
 * File: MaskedThread.java
 * Author: Declan Naughton
 * A class which 
 */
public class PassMask extends Thread{
    private static char echochar = '•';
    
    /**
     * A method which, when called, will receive 
     * @return String - the actual password entered.
     */
    public static String mask(){
        Console console = System.console();
        try{
            char[] passArr = console.readPassword("Password: ");
            String password = "";
            int len = passArr.length;
            for(int i = 0; i<len; i++){
                System.out.print(echochar);
                password += passArr[i];
            }
            System.out.println();

            return password;
        } catch (Exception e){
            System.out.println("ERROR: could not find console.");
            e.printStackTrace();
            return null;
        }
    }
    

}

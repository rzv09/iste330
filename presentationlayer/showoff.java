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

            run();
        } catch (Exception e){
            
        }
    }

    public void run(){
        dl.rebuildTables();
        int f1 = dl.addFacultyMember("Brom", "Holcombson", "Ironscale@varden.org", "Saphira1", "908-124-7871", "5 Carvahall Rd");
        dl.printFacultyMember(f1);
        int a1 = dl.addAbstract("The Fall of Morzan", "In this abstract we discuss "+
        "of Galbatorix's empire, his warcrimes, and how a people's rebellion fueled by "+
        "merchants brought about his end.");
        dl.assignAbstract(f1, a1);
        int t1 = dl.addFacultyTopic("magic");
        dl.addFacultyKeyword(a1, t1);
        int t2 = dl.addFacultyTopic("history");
        dl.addFacultyKeyword(a1, t2);

        int f2 = dl.addFacultyMember("Harley", "Warren", "barrow_sleeper04@unspeakable.yog", "Ry'Leh", "106-559-1922", "200 Miskatonic University, Arkham");
        dl.printFacultyMember(f2);

        int a2 = dl.addAbstract("The Dreamers of the Deep", "A dissertation on the horrors of "+
        "the great old ones and evils archived in the infamous necronomicon.");
        dl.assignAbstract(f2, a2);

        int t3 = dl.addFacultyTopic("quantum physics");
        int t4 = dl.addFacultyTopic("mythology");
        int t5 = dl.addFacultyTopic("xenobiology");

        dl.addFacultyKeyword(f2, t3);
        dl.addFacultyKeyword(f2, t4);
        dl.addFacultyKeyword(f2, t5);

        int s1 = dl.addStudent("Luke", "Skywalker", "NewHope@force.echo", "0604BWU");
        dl.printStudent(s1);

        int t6 = dl.addStudentTopic("mythology");
        int t7 = dl.addStudentTopic("war");
        int t8 = dl.addStudentTopic("empire");

        dl.addStudentKeyword(s1, t6);
        dl.addStudentKeyword(s1, t7);
        dl.addStudentKeyword(s1, t8);

        int s2 = dl.addStudent("Victor", "Laurence", "holyvicar@old.blood", "FearTheOldBlood");
        int t9 = dl.addStudentTopic("evil");
        int t10 = dl.addStudentTopic("blood");
        dl.addStudentKeyword(s2, t10);
        dl.addStudentKeyword(s2, t9);

        Set<Integer> IDs = dl.getStudentMatches(s1);

        Iterator<Integer> iter = IDs.iterator();
                    System.out.println("Possible Faculty Matches: ");
                    while(iter.hasNext()){
                        int i = iter.next();
                        String output = dl.printFacultyMember(i);
                        output += "\n Faculty ID: "+i;
                        System.out.println(output);
                    }
    }
    public static void main(String[] args) {
        showoff s = new showoff();
    }
}

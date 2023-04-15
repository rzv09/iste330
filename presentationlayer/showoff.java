package presentationlayer;
import datalayer.DataLayer;
import presentationlayer.PassMask;
import java.io.*;
import java.sql.*;
import java.util.Scanner;
import java.util.*;

/**
 * Showoff.java is a class and program designed to test the basic capabilities
 * of the data layer, primarily the matching methods. This program creates
 * numerous students, faculty members, and a guest and assigns them each a series
 * of topics with similar names. Furthermore, the faculty have abstracts 
 * whose descriptions contain words which match the topics of the guests and
 * students. This is to test if the match methods can detect keywords within
 * strings instead of just matching strings character for character.
 */
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
        //dl.rebuildTables();
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

        System.out.println("Adding Faculty: Harley Warren");
        int f2 = dl.addFacultyMember("Harley", "Warren", "barrow_sleeper04@unspeakable.yog", "Ry'Leh", "106-559-1922", "200 Miskatonic University, Arkham");
        dl.printFacultyMember(f2);

        System.out.println("Giving Warren an abstract...");
        int a2 = dl.addAbstract("The Dreamers of the Deep", "A dissertation on the horrors of "+
        "the great old ones and evils archived in the infamous necronomicon.");
        dl.assignAbstract(f2, a2);

        int t3 = dl.addFacultyTopic("quantum physics");
        int t4 = dl.addFacultyTopic("mythology");
        int t5 = dl.addFacultyTopic("xenobiology");

        dl.addFacultyKeyword(f2, t3);
        dl.addFacultyKeyword(f2, t4);
        dl.addFacultyKeyword(f2, t5);

        System.out.println("Adding Student: Victor Laurence");
        int s2 = dl.addStudent("Victor", "Laurence", "holyvicar@old.blood", "FearTheOldBlood");
        System.out.println(dl.printStudent(s2));
        int t9 = dl.addStudentTopic("evil");
        int t10 = dl.addStudentTopic("blood");
        System.out.println("Giving Laurence an interest in blood.");
        dl.addStudentKeyword(s2, t10);
        System.out.println("Giving Laurence an interest in evil.");
        dl.addStudentKeyword(s2, t9);

        Set<Integer> IDs = dl.getStudentMatches(s2);
        System.out.println("Finding Faculty Matches for Victor Laurence...");
        Iterator<Integer> iter = IDs.iterator();
                    System.out.println("Possible Faculty Matches: ");
                    while(iter.hasNext()){
                        int i = iter.next();
                        String output = dl.printFacultyMember(i);
                        output += "\n Faculty ID: "+i;
                        System.out.println(output);
                    }
        


        System.out.println("Adding Guest: Dark Somnium");
        int g1 = dl.addGuest("Dark Somnium", "SpookySpaghetti@gmail.com", "Tenebris");
        System.out.println(dl.printGuest(g1));
        int gt1 = dl.addGuestTopic("evil");
        int gt2 = dl.addGuestTopic("terror");
        int gt3 = dl.addGuestTopic("blood");
        System.out.println("Giving Dark Somnium the topic of evil...");
        dl.addGuestKeyword(g1, gt1);
        System.out.println("Giving Dark Somnium the topic of terror...");
        dl.addGuestKeyword(g1, gt2);
        System.out.println("Giving Dark Somnium the topic of blood...");
        dl.addGuestKeyword(g1, gt3);


        IDs = dl.getStudentMatches(g1);
        System.out.println("Finding Faculty Matches for Dark Somnium...");
        iter = IDs.iterator();
                    System.out.println("Possible Faculty Matches: ");
                    while(iter.hasNext()){
                        int i = iter.next();
                        String output = dl.printFacultyMember(i);
                        output += "\n Faculty ID: "+i;
                        System.out.println(output);
                    }


        dl.rebuildTables();
    }
    public static void main(String[] args) {
        showoff s = new showoff();
    }
}

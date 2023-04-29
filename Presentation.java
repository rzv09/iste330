import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
import java.io.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * ISTE 330.01 - Habermas
 * Group 5 - Project Part 1
 *	Authors: Raghav, Charki,Maple, Ramanz,
 */

public class Presentation extends JFrame {
   private Backend DBConnect;
   public Font myButtonFont = new Font("Courier", Font.PLAIN, 38);
   private int userID;

   public Presentation() {
		super("Presentation");
		setSize(630,400);
		setLocation(200,380);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.DBConnect = new Backend();

		JPanel jpCenter = new JPanel();
		jpCenter.setLayout(new GridLayout(0,1));

        connect();


	   JButton jbFaculty = new JButton("I'm a Faculty Member");
		jbFaculty.setFont(myButtonFont);
		jbFaculty.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
            boolean loggedIn;

            int loginStatus = JOptionPane.showConfirmDialog(null, "Are you a returning user?", "Faculty Login", JOptionPane.YES_NO_OPTION);
            if(loginStatus == JOptionPane.YES_OPTION){
               loggedIn = validateFaculty();
            } else {
               loggedIn = addFaculty();
            }

            if(loggedIn){
               boolean working = true;
               boolean subtask;
               while(working){
                  String[] opt = new String[] {"Add a Abstract", "Add a Keyword", "Delete a Keyword", "Search Students", "Exit"};
                  int response = JOptionPane.showOptionDialog(null, "Select an Action:", "Faculty Options", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opt, opt[0]);
                  switch (response) {
                     case 0 -> { // Add Abstract
                        subtask = true;
                        while (subtask) {
                           addRecord();
                           UIManager.put("OptionPane.minimumSize", new Dimension(320, 120));
                           int status = JOptionPane.showConfirmDialog(null, "Would you like to add another abstract?", "Input Abstract Entry", JOptionPane.YES_NO_OPTION);
                           UIManager.put("OptionPane.minimumSize", null);
                           subtask = status == JOptionPane.YES_OPTION;
                        }
                     }
                     case 1 -> { // Add a Faculty Keyword
                        subtask = true;
                        while (subtask) {
                           addKeyword("faculty");
                           int status = JOptionPane.showConfirmDialog(null, "Would you like to add another keyword?", "Add Student Keyword", JOptionPane.YES_NO_OPTION);
                           subtask = status == JOptionPane.YES_OPTION;
                        }
                     }
                     case 2 -> { // Delete a Faculty Keyword
                        subtask = true;
                        while (subtask) {
                           deleteKeyword("faculty");
                           int status = JOptionPane.showConfirmDialog(null, "Would you like to remove another keyword?", "Add Student Keyword", JOptionPane.YES_NO_OPTION);
                           subtask = status == JOptionPane.YES_OPTION;
                        }
                     }
                     case 3 -> { // Search Student Keywords
                        subtask = true;
                        while (subtask) {
                           searchKeywords("student");
                           int status = JOptionPane.showConfirmDialog(null, "Would you like to search students again?", "Search Students", JOptionPane.YES_NO_OPTION);
                           subtask = status == JOptionPane.YES_OPTION;
                        }
                     }
                     default -> working = false; // Exit
                  }
               }
            }
			}
		});


      JButton jbStudent = new JButton("I'm a Student");
		jbStudent.setFont(myButtonFont);
		jbStudent.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
            boolean loggedIn = false;

            int loginStatus = JOptionPane.showConfirmDialog(null, "Are you a returning user?", "Student Login", JOptionPane.YES_NO_OPTION);
            if(loginStatus == JOptionPane.YES_OPTION){
               loggedIn = validateStudent();
            } else {
               loggedIn = addStudent();
            }

               if(loggedIn){
                  boolean working = true;
                  boolean subtask;
                  while(working){
                     String[] opt = new String[] {"Add a Keyword", "Delete a Keyword", "Search Faculty", "Exit"};
                     int response = JOptionPane.showOptionDialog(null, "Select an Action:", "Student Options", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opt, opt[0]);
                     switch (response) {
                        case 0 -> { // Add a Student Keyword
                           subtask = true;
                           while (subtask) {
                              addKeyword("student");
                              int status = JOptionPane.showConfirmDialog(null, "Would you like to add another keyword?", "Add Student Keyword", JOptionPane.YES_NO_OPTION);
                              subtask = status == JOptionPane.YES_OPTION;
                           }
                        }
                        case 1 -> { // Delete a Student Keyword
                           subtask = true;
                           while (subtask) {
                              deleteKeyword("student");
                              int status = JOptionPane.showConfirmDialog(null, "Would you like to remove another keyword?", "Add Student Keyword", JOptionPane.YES_NO_OPTION);
                              subtask = status == JOptionPane.YES_OPTION;
                           }
                        }
                        case 2 -> { // Search Faculty Keywords
                           subtask = true;
                           while (subtask) {
                              searchKeywords("faculty");
                              int status = JOptionPane.showConfirmDialog(null, "Would you like to search faculty again?", "Search Faculty", JOptionPane.YES_NO_OPTION);
                              subtask = status == JOptionPane.YES_OPTION;
                           }
                        }
                        default -> working = false; // Exit
                     }
                  }
               }
			}
		});

      JButton jbGuest = new JButton("I'm a Guest");
      jbGuest.setFont(myButtonFont);
      jbGuest.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent ae) {
            boolean working = true;
            boolean subtask;
            while(working){
               String[] opt = new String[] {"Search Students", "Search Faculty", "Exit"};
               int response = JOptionPane.showOptionDialog(null, "Select an Action:", "Guest Options", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opt, opt[0]);
               switch (response) {
                  case 0 -> { // Search Students
                     subtask = true;
                     while (subtask) {
                        searchKeywords("student");
                        int status = JOptionPane.showConfirmDialog(null, "Would you like to search students again?", "Search Students", JOptionPane.YES_NO_OPTION);
                        subtask = status == JOptionPane.YES_OPTION;
                     }
                  }
                  case 1 -> { // Search Faculty Keywords
                     subtask = true;
                     while (subtask) {
                        searchKeywords("faculty");
                        int status = JOptionPane.showConfirmDialog(null, "Would you like to search faculty again?", "Search Faculty", JOptionPane.YES_NO_OPTION);
                        subtask = status == JOptionPane.YES_OPTION;
                     }
                  }
                  default -> working = false; // Exit
               }
            }
         }
      });

      // Button to handle closing the DB connection
		JButton jbExit = new JButton("Exit");
      jbExit.setFont(myButtonFont);
		jbExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
            DBConnect.close();
			}
		});

		jpCenter.add(jbFaculty);
        jpCenter.add(jbStudent);
        jpCenter.add(jbGuest);
		jpCenter.add(jbExit);
		add(jpCenter);

		setVisible(true);
	} // end of constructor

   public void connect() {
      JPanel Inputbox = new JPanel(new GridLayout(2,2));

		JLabel lblUser = new JLabel("Username: ");
		JLabel lblPass = new JLabel("Password: ");

		JTextField tfUser = new JTextField("");
		JTextField tfPass = new JPasswordField("");

		Inputbox.add(lblUser);
		Inputbox.add(tfUser);
		Inputbox.add(lblPass);
		Inputbox.add(tfPass);

      JOptionPane.showMessageDialog(null, Inputbox, "Connect to Data Source", JOptionPane.INFORMATION_MESSAGE);

      boolean connected = DBConnect.connect(tfUser.getText().strip(), tfPass.getText().strip());

      if(connected) {
			JOptionPane.showMessageDialog(null,"Connected to the data source.");
		} else {
			JOptionPane.showMessageDialog(null,"Unable to connect to data source.");
         System.exit(0);
		} //end of if/else
   } // end of connect


   // Methods for buttons and such
   public boolean addFaculty() {
      JPanel Searchbox = new JPanel(new GridLayout(5,2));

      JLabel lblLName = new JLabel("Last Name: ");
      JLabel lblFName = new JLabel("First Name: ");
      JLabel lblEmail = new JLabel("Email: ");
      JLabel lblBuildingNum = new JLabel("Building Number: ");
      JLabel lblOfficeNum = new JLabel("Office Number: ");

      JTextField tfLName = new JTextField("");
      JTextField tfFName = new JTextField("");
      JTextField tfEmail = new JTextField("");
      JTextField tfBuildingNum = new JTextField("");
      JTextField tfOfficeNum = new JTextField("");

      Searchbox.add(lblLName);
      Searchbox.add(tfLName);
      Searchbox.add(lblFName);
      Searchbox.add(tfFName);
      Searchbox.add(lblEmail);
      Searchbox.add(tfEmail);
      Searchbox.add(lblBuildingNum);
      Searchbox.add(tfBuildingNum);
      Searchbox.add(lblOfficeNum);
      Searchbox.add(tfOfficeNum);

      JOptionPane.showMessageDialog(null, Searchbox, "Input Faculty Information", JOptionPane.INFORMATION_MESSAGE);

      String lname = tfLName.getText().strip();
      String fname = tfFName.getText().strip();
      String email = tfEmail.getText().strip();

      int buildingNum = 0;
      if(!tfBuildingNum.getText().strip().equals("")) {
         buildingNum = Integer.parseInt(tfBuildingNum.getText());
      } // end of if

      int officeNum = 0;
      if(!tfOfficeNum.getText().strip().equals("")) {
         officeNum = Integer.parseInt(tfOfficeNum.getText());
      } // end of if

      if((!lname.equals("")) || (!fname.equals("")) || (!email.equals(""))) {
         try {
            userID = DBConnect.addFaculty(fname, lname, email, buildingNum, officeNum);
            if(userID != 0) {
               JOptionPane.showMessageDialog(null, "Your ID is " + userID, "Input Faculty Information", JOptionPane.INFORMATION_MESSAGE);
               return true;
            } else {
               JOptionPane.showMessageDialog(null, "Error occured while attempting to create faculty account. Please try again", "Input Faculty Information", JOptionPane.ERROR_MESSAGE);
               return false;
            } // end of if/else
         } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error occured while attempting to create faculty account", "Input Faculty Information", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
   		} // end of try/catch
      } else {
         JOptionPane.showMessageDialog(null, "Last name, first name, and email are required. Please try again", "Input Faculty Information", JOptionPane.ERROR_MESSAGE);
         return false;
      } // end of if/else
   } // end of addFaculty()

   public boolean validateFaculty() {
      try {
         JPanel Searchbox = new JPanel(new GridLayout(1,2));

         JLabel lblID = new JLabel("Faculty ID: ");

         JTextField tfID = new JTextField("");

         Searchbox.add(lblID);
         Searchbox.add(tfID);

         JOptionPane.showMessageDialog(null, Searchbox, "Faculty Login", JOptionPane.INFORMATION_MESSAGE);

         int id = 0;
         if(!tfID.getText().strip().equals("")) {
            id = Integer.parseInt(tfID.getText());
         } // end of if

         if((id != 0) && DBConnect.validateFaculty(id)) {
            JOptionPane.showMessageDialog(null, "Logged in", "Faculty Login", JOptionPane.INFORMATION_MESSAGE);
            userID = id;
            return true;
         } else {
            JOptionPane.showMessageDialog(null, "That ID is invalid. Please try again", "Faculty Login", JOptionPane.ERROR_MESSAGE);
            return false;
         } // end of if/else
      } catch (Exception e) {
         JOptionPane.showMessageDialog(null, "Error occured while attempting to validate faculty account", "Faculty Login", JOptionPane.ERROR_MESSAGE);
         e.printStackTrace();
         return false;
      } // end of try/catch
   } // end of validateFaculty

   public boolean addStudent() {
      JPanel Searchbox = new JPanel(new GridLayout(3,2));

      JLabel lblLName = new JLabel("Last Name: ");
      JLabel lblFName = new JLabel("First Name: ");
      JLabel lblEmail = new JLabel("Email: ");

      JTextField tfLName = new JTextField("");
      JTextField tfFName = new JTextField("");
      JTextField tfEmail = new JTextField("");

      Searchbox.add(lblLName);
      Searchbox.add(tfLName);
      Searchbox.add(lblFName);
      Searchbox.add(tfFName);
      Searchbox.add(lblEmail);
      Searchbox.add(tfEmail);

      JOptionPane.showMessageDialog(null, Searchbox, "Input Student Information", JOptionPane.INFORMATION_MESSAGE);

      String lname = tfLName.getText().strip();
      String fname = tfFName.getText().strip();
      String email = tfEmail.getText().strip();

      if((!lname.equals("")) || (!fname.equals("")) || (!email.equals(""))) {
         try {
            userID = DBConnect.addStudent(lname, fname, email);
            if(userID != 0) {
               JOptionPane.showMessageDialog(null, "Your ID is " + userID, "Input Student Information", JOptionPane.INFORMATION_MESSAGE);
               return true;
            } else {
               JOptionPane.showMessageDialog(null, "Error occured while attempting to create student account. Please try again", "Input Student Information", JOptionPane.ERROR_MESSAGE);
               return false;
            } // end of if/else
         } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error occured while attempting to create student account", "Input Student Information", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
   		} // end of try/catch
      } else {
         JOptionPane.showMessageDialog(null, "All fields are required. Please try again", "Input Student Information", JOptionPane.ERROR_MESSAGE);
         return false;
      } // end of if/else
   } // end of addStudent()

   public boolean validateStudent() {
      try {
         JPanel Searchbox = new JPanel(new GridLayout(1,2));

         JLabel lblID = new JLabel("Student ID: ");

         JTextField tfID = new JTextField("");

         Searchbox.add(lblID);
         Searchbox.add(tfID);

         JOptionPane.showMessageDialog(null, Searchbox, "Student Login", JOptionPane.INFORMATION_MESSAGE);

         int id = 0;
         if(!tfID.getText().strip().equals("")) {
            id = Integer.parseInt(tfID.getText());
         } // end of if

         if((id != 0) && DBConnect.validateStudent(id)) {
            JOptionPane.showMessageDialog(null, "Logged in", "Student Login", JOptionPane.INFORMATION_MESSAGE);
            userID = id;
            return true;
         } else {
            JOptionPane.showMessageDialog(null, "That ID is invalid. Please try again", "Student Login", JOptionPane.ERROR_MESSAGE);
            return false;
         } // end of if/else
      } catch (Exception e) {
         JOptionPane.showMessageDialog(null, "Error occured while attempting to validate student account", "Student Login", JOptionPane.ERROR_MESSAGE);
         e.printStackTrace();
         return false;
      } // end of try/catch
   } // end of validateStudent

   String fileChosen = "";

   public void addRecord() {
      JPanel Searchbox = new JPanel(new BorderLayout(1,2));
      JPanel TF = new JPanel(new BorderLayout(2,2));
      JPanel TA = new JPanel(new BorderLayout(2,2));

      JLabel lblId = new JLabel("Faculty ID: ");
      JLabel lblAbst = new JLabel("Type or paste Abstract Text: ");

      JTextField tfId = new JTextField("" + userID);
      JTextArea taAbst = new JTextArea("");

      JButton button = new JButton("Select a File");
      button.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            fileChosen = fileChooser();
            taAbst.append(fileChosen);
         }
      } );

      taAbst.setLineWrap(true);

      JScrollPane scroll = new JScrollPane(taAbst);
      scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

      TF.add(lblId, BorderLayout.WEST);
      TF.add(tfId, BorderLayout.CENTER);
      TF.add(button, BorderLayout.EAST);
      TA.add(lblAbst, BorderLayout.WEST);
      TA.add(scroll, BorderLayout.CENTER);

      Searchbox.add(TF, BorderLayout.NORTH);
      Searchbox.add(TA, BorderLayout.CENTER);

      UIManager.put("OptionPane.minimumSize",new Dimension(950,500));

      JOptionPane.showMessageDialog(null, Searchbox, "Abstract Entry Form", JOptionPane.INFORMATION_MESSAGE);

      String abst = taAbst.getText().strip();

      try {
         DBConnect.addRecord(userID, abst);
      } catch (Exception e) {
         System.out.println("Error occured while attempting to insert abstract record");
         e.printStackTrace();
		} // end of try/catch
   } // end of addRecord()

   public String fileChooser() {
      String data = "";
      JPanel temp = new JPanel(new BorderLayout(1,1));
      JFileChooser chooser = new JFileChooser();
      FileNameExtensionFilter filter = new FileNameExtensionFilter("*.txt", "txt");
      chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
      chooser.setFileFilter(filter);
      int result = chooser.showOpenDialog(this);
      if (result == JFileChooser.APPROVE_OPTION) {
         try {
            File file = chooser.getSelectedFile();
            try(BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
               data = readAllLines(reader);
            } //end try
            catch(Exception e) {
               System.out.println("An error has occured while attempting to read selected file.");
               e.printStackTrace();
            } //end catch

         }catch(Exception e) {
            System.out.println("An error has occured while attempting to open selected file.");
            e.printStackTrace();
         } //end catch
      } //end if
      return data;
   } // end of fileChooser

   public String readAllLines(BufferedReader reader) {
      StringBuilder content = new StringBuilder();
      String line;

      try {
         while ((line = reader.readLine()) != null) {
            content.append(line);
            content.append(System.lineSeparator());
         }
      }catch(Exception e){
         System.out.println("An error has occured while attempting to read selected file text.");
         e.printStackTrace();
      }
      return content.toString();
   } //end readAllLines

   public void searchKeywords(String type) {
      JPanel Searchbox = new JPanel(new GridLayout(2,3));

      JLabel lblWordOne = new JLabel("Keyword One: ");
      JLabel lblWordTwo = new JLabel("Keyword Two: ");
      JLabel lblWordThree = new JLabel("Keyword Three: ");

      JTextField tfWordOne = new JTextField("");
      JTextField tfWordTwo = new JTextField("");
      JTextField tfWordThree = new JTextField("");

      Searchbox.add(lblWordOne);
      Searchbox.add(lblWordTwo);
      Searchbox.add(lblWordThree);
      Searchbox.add(tfWordOne);
      Searchbox.add(tfWordTwo);
      Searchbox.add(tfWordThree);

      JOptionPane.showMessageDialog(null, Searchbox, "Input Keywords to Search", JOptionPane.INFORMATION_MESSAGE);

      String one = tfWordOne.getText().strip();
      String two = tfWordTwo.getText().strip();
      String three = tfWordThree.getText().strip();

      try {
         if((two == "" || two == null) && (three == "" || three == null)) {
            DBConnect.searchKeywords(type, one, "", "");
         } else if (three == "" || three == null) {
            DBConnect.searchKeywords(type, one, two, "");
         } else { DBConnect.searchKeywords(type, one, two, three); }
      } catch (Exception e) {
         System.out.println("Error occured while attempting to search");
         e.printStackTrace();
		} // end of try/catch
   } //end of searchKeywords()

   public void addKeyword(String type) {
      JPanel Searchbox = new JPanel(new GridLayout(1,2));

      JLabel lblWordOne = new JLabel("Keyword: ");

      JTextField tfWordOne = new JTextField("");

      Searchbox.add(lblWordOne);
      Searchbox.add(tfWordOne);

      JOptionPane.showMessageDialog(null, Searchbox, "Input "+type.substring(0, 1).toUpperCase() + type.substring(1)+" Keyword to be Added", JOptionPane.INFORMATION_MESSAGE);

      String keyword = tfWordOne.getText().strip();

      try {
         DBConnect.addKeyword(type, userID, keyword);
      } catch (Exception e) {
         System.out.println("Error occured while attempting to add a keyword");
         e.printStackTrace();
      } // end of try/catch
   }

   public void deleteKeyword(String type) {
      JPanel Searchbox = new JPanel(new GridLayout(1,2));

      JLabel lblWordOne = new JLabel("Keyword: ");

      JTextField tfWordOne = new JTextField("");

      Searchbox.add(lblWordOne);
      Searchbox.add(tfWordOne);

      JOptionPane.showMessageDialog(null, Searchbox, "Input "+type.substring(0, 1).toUpperCase() + type.substring(1)+" Keyword to be Deleted", JOptionPane.INFORMATION_MESSAGE);

      String keyword = tfWordOne.getText().strip();

      try {
         DBConnect.removeKeyword(type, userID, keyword);
      } catch (Exception e) {
         System.out.println("Error occured while attempting to add a keyword");
         e.printStackTrace();
      } // end of try/catch
   }

   public static void main(String [] args){
      System.out.println("ISTE330-01  Presentation   Raghav, Charki,Maple, Ramanz,    04-25-2023\n");

      new Presentation();
	} // end of main method
}
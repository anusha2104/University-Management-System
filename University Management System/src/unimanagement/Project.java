package unimanagement;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Project extends JFrame implements ActionListener {
    
    String accountType; // To store "Teacher" or "Student"
    String username;    // To store empId or rollno
    
    JMenu newInformation, details, exam, updateInfo, fee, utility, exit;
    JMenuItem examinationdetails, entermarks;
    
    // Updated Constructor to accept role and username
    Project(String accountType, String username) {
        this.accountType = accountType;
        this.username = username;
        
        setSize(1500, 684);
        
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/third.jpg"));
        Image i2 = i1.getImage().getScaledInstance(1500, 684, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel image = new JLabel(i3);
        add(image);
        
        JMenuBar mb = new JMenuBar();
        
        // 1. New Information (Teacher only: Add new teacher/student)
        newInformation = new JMenu("New Information");
        newInformation.setForeground(Color.BLUE);
        mb.add(newInformation);
        
        JMenuItem facultyInfo = new JMenuItem("New Teacher Information");
        facultyInfo.setBackground(Color.WHITE);
        facultyInfo.addActionListener(this);
        newInformation.add(facultyInfo);
        
        JMenuItem studentInfo = new JMenuItem("New Student Information");
        studentInfo.setBackground(Color.WHITE);
        studentInfo.addActionListener(this);
        newInformation.add(studentInfo);
        
        // 2. Details (Teacher/Student: View their respective details)
        details = new JMenu("View Details");
        details.setForeground(Color.RED);
        mb.add(details);
        
        JMenuItem facultydetails = new JMenuItem("View Teacher Details");
        facultydetails.setBackground(Color.WHITE);
        facultydetails.addActionListener(this);
        details.add(facultydetails);
        
        JMenuItem studentdetails = new JMenuItem("View Student Details");
        studentdetails.setBackground(Color.WHITE);
        studentdetails.addActionListener(this);
        details.add(studentdetails);
        
        // 5. Exams (Teacher: Enter Marks / Student: View Marks)
        exam = new JMenu("Examination");
        exam.setForeground(Color.BLUE);
        mb.add(exam);
        
        examinationdetails = new JMenuItem("Examination Results"); // Student: View Marks
        examinationdetails.setBackground(Color.WHITE);
        examinationdetails.addActionListener(this);
        exam.add(examinationdetails);
        
        entermarks = new JMenuItem("Enter Marks"); // Teacher: Enter Marks
        entermarks.setBackground(Color.WHITE);
        entermarks.addActionListener(this);
        exam.add(entermarks);
       
  
        // 8. Utility (Both)
        utility = new JMenu("Utility");
        utility.setForeground(Color.RED);
        mb.add(utility);
        
        JMenuItem notepad = new JMenuItem("Notepad");
        notepad.setBackground(Color.WHITE);
        notepad.addActionListener(this);
        utility.add(notepad);
        
        JMenuItem calc = new JMenuItem("Calculator");
        calc.setBackground(Color.WHITE);
        calc.addActionListener(this);
        utility.add(calc);
        
        // 9. Exit (Both)
        exit = new JMenu("Exit");
        exit.setForeground(Color.BLUE);
        mb.add(exit);
        
        JMenuItem ex = new JMenuItem("Exit");
        ex.setBackground(Color.WHITE);
        ex.addActionListener(this);
        exit.add(ex);
        
        // --- Role-Based Menu Customization ---
        if (accountType.equals("Student")) {
            // Student only needs to view their own details/marks, apply leave
            newInformation.setVisible(false);     // No adding new info
            details.setVisible(false);    // Cannot view all teacher details
                // Cannot update teacher info
                // Cannot apply faculty leave
                // Cannot view all leave applications
            entermarks.setVisible(false);        // Cannot enter marks
            // Student can view 'Examination Results'
            
        } else if (accountType.equals("Teacher")) {
            // Teacher needs to manage records, enter marks, view details
           // Cannot apply student leave (only view/manage)
                 // Does not need to fill fee form
            examinationdetails.setVisible(false); // Teacher doesn't use this, they use 'Enter Marks'
        }
        // ------------------------------------
        
        setJMenuBar(mb);
        
        setVisible(true);
    }
    
    // Old default constructor removed/disabled to force role/username passing
    public Project() {
        // Only for testing, production code should use Project(String role, String username)
        this("Teacher", "1011234"); 
    }
    
    public void actionPerformed(ActionEvent ae){
        String msg = ae.getActionCommand();
        
        if (msg.equals("Exit")){
            setVisible(false);
            System.exit(0);
        } else if(msg.equals("Calculator")){
            try{
                Runtime.getRuntime().exec("calc.exe");
            } catch (Exception e){
                e.printStackTrace();
            }
        } else if(msg.equals("Notepad")){
            try{
                Runtime.getRuntime().exec("notepad.exe");
            } catch (Exception e){
                e.printStackTrace();
            }
        } else if(msg.equals("New Teacher Information")){
            new AddTeacher();   
        } else if(msg.equals("New Student Information")){
            new AddStudent();
        } else if(msg.equals("View Teacher Details")){
            new TeacherDetails();
        } else if(msg.equals("View Student Details")){
            new StudentDetails();
        } else if(msg.equals("Enter Marks")){
            // Teacher-specific: Open mark entry screen
            new EnterMarks();
        } else if(msg.equals("Examination Results")){
            // Student-specific: Open view marks screen, pass the rollno (which is the username)
            new ViewMarks(this.username); 
        } else if(msg.equals("Update Teacher Details")){
            // new UpdateTeacher();
        } else if(msg.equals("Update Student Details")){
            // new UpdateStudent();
        }
    }
    
    public static void main(String[] args){
        new Project("Teacher", "1011234"); // You can run Login.java instead
    }
}
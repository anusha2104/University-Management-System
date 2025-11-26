package unimanagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.io.*;
import java.util.Scanner;

public class EnterMarks extends JFrame implements ActionListener {
    
    Choice crollno;
    JTextField tfsubject1, tfmarks1, tfsubject2, tfmarks2, tfnote;
    JTextArea taStudentComment, taTeacherReply; // Text areas for comments
    JButton submitManual, submitCSV, cancel;
    
    EnterMarks() {
        setSize(700, 650);
        setLocation(400, 50);
        setLayout(null);
        
        JLabel heading = new JLabel("Enter Student Marks & View Feedback");
        heading.setBounds(150, 20, 450, 30);
        heading.setFont(new Font("Tahoma", Font.BOLD, 22));
        add(heading);
        
        // --- Manual Entry Section ---
        
        JLabel lblrollno = new JLabel("Select Roll Number");
        lblrollno.setBounds(50, 70, 150, 20);
        add(lblrollno);
        
        crollno = new Choice();
        crollno.setBounds(250, 70, 150, 20);
        crollno.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ie) {
                fetchStudentFeedback(crollno.getSelectedItem());
            }
        });
        add(crollno);
        
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("select * from student");
            while(rs.next()) {
                crollno.add(rs.getString("rollno"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Mark Fields (Same as before)
        addLabelAndField(50, 110, "Subject 1", tfsubject1 = new JTextField());
        addLabelAndField(50, 150, "Marks 1", tfmarks1 = new JTextField());
        addLabelAndField(50, 190, "Subject 2", tfsubject2 = new JTextField());
        addLabelAndField(50, 230, "Marks 2", tfmarks2 = new JTextField());
        addLabelAndField(50, 270, "Teacher Note", tfnote = new JTextField());

        // Button for manual submission
        submitManual = new JButton("Submit Manual Entry");
        submitManual.setBounds(50, 320, 200, 30);
        submitManual.setBackground(Color.BLACK);
        submitManual.setForeground(Color.WHITE);
        submitManual.addActionListener(this);
        add(submitManual);
        
        // --- Feedback Loop Section ---
        
        JLabel lblstudentComment = new JLabel("Student Feedback:");
        lblstudentComment.setBounds(50, 380, 200, 20);
        lblstudentComment.setFont(new Font("Tahoma", Font.BOLD, 14));
        add(lblstudentComment);
        
        taStudentComment = new JTextArea();
        taStudentComment.setBounds(50, 410, 600, 50);
        taStudentComment.setEditable(false);
        taStudentComment.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        taStudentComment.setLineWrap(true);
        add(taStudentComment);
        
        JLabel lblTeacherReply = new JLabel("Teacher Reply:");
        lblTeacherReply.setBounds(50, 470, 200, 20);
        lblTeacherReply.setFont(new Font("Tahoma", Font.BOLD, 14));
        add(lblTeacherReply);
        
        taTeacherReply = new JTextArea();
        taTeacherReply.setBounds(50, 500, 600, 50);
        taTeacherReply.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        taTeacherReply.setLineWrap(true);
        add(taTeacherReply);

        // Button for submitting reply
        JButton submitReply = new JButton("Reply to Student");
        submitReply.setBounds(50, 560, 200, 30);
        submitReply.setBackground(Color.RED);
        submitReply.setForeground(Color.WHITE);
        submitReply.addActionListener(this);
        add(submitReply);

        // --- CSV Upload Section ---
        
        JLabel lblCSV = new JLabel("Bulk Update from CSV (marks_update.csv):");
        lblCSV.setBounds(400, 70, 300, 20);
        lblCSV.setFont(new Font("Tahoma", Font.BOLD, 14));
        add(lblCSV);

        submitCSV = new JButton("Upload CSV File");
        submitCSV.setBounds(400, 110, 200, 30);
        submitCSV.setBackground(new Color(0, 128, 0)); // Green
        submitCSV.setForeground(Color.WHITE);
        submitCSV.addActionListener(this);
        add(submitCSV);
        
        // --- General Buttons ---
        
        cancel = new JButton("Cancel");
        cancel.setBounds(550, 560, 100, 30);
        cancel.setBackground(Color.BLACK);
        cancel.setForeground(Color.WHITE);
        cancel.addActionListener(this);
        add(cancel);
        
        // Initialize feedback view with the first student
        if (crollno.getItemCount() > 0) {
            fetchStudentFeedback(crollno.getItem(0));
        }

        setVisible(true);
    }
    
    // Helper method to add labels and fields
    private void addLabelAndField(int x, int y, String labelText, JTextField textField) {
        JLabel label = new JLabel(labelText);
        label.setBounds(x, y, 100, 20);
        add(label);
        textField.setBounds(x + 100, y, 150, 20);
        add(textField);
    }

    // Method to fetch and display student's comment
    private void fetchStudentFeedback(String rollno) {
        taStudentComment.setText("");
        taTeacherReply.setText("");
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("SELECT * FROM marks WHERE rollno = '" + rollno + "'");
            if (rs.next()) {
                String studentComment = rs.getString("student_comment");
                String teacherReply = rs.getString("teacher_reply");
                
                taStudentComment.setText(studentComment != null ? studentComment : "No comment submitted by student yet.");
                taTeacherReply.setText(teacherReply != null ? teacherReply : "");
                
                // Also load the marks for manual editing
                tfsubject1.setText(rs.getString("subject1"));
                tfmarks1.setText(rs.getString("marks1"));
                tfsubject2.setText(rs.getString("subject2"));
                tfmarks2.setText(rs.getString("marks2"));
                tfnote.setText(rs.getString("comment")); // Teacher's Note
            } else {
                // Clear fields if no marks exist
                taStudentComment.setText("Marks not yet entered for this student.");
                taTeacherReply.setText("");
                tfsubject1.setText("");
                tfmarks1.setText("");
                tfsubject2.setText("");
                tfmarks2.setText("");
                tfnote.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == submitManual) {
            submitMarks(crollno.getSelectedItem(), tfsubject1.getText(), tfmarks1.getText(), 
                        tfsubject2.getText(), tfmarks2.getText(), tfnote.getText());
        } else if (ae.getActionCommand().equals("Reply to Student")) {
            submitTeacherReply(crollno.getSelectedItem(), taTeacherReply.getText());
        } else if (ae.getSource() == submitCSV) {
            uploadMarksFromCSV("marks_update.csv"); // Hardcoded file name for simplicity
        } else {
            setVisible(false);
        }
    }
    
    // Common method for mark submission (manual or CSV)
    private void submitMarks(String rollno, String sub1, String m1, String sub2, String m2, String note) {
        try {
            String query = "INSERT INTO marks (rollno, subject1, marks1, subject2, marks2, comment) VALUES ('" + rollno + "', '" + sub1 + "', '" + m1 + "', '" + sub2 + "', '" + m2 + "', '" + note + "') " +
                           "ON DUPLICATE KEY UPDATE subject1='" + sub1 + "', marks1='" + m1 + "', subject2='" + sub2 + "', marks2='" + m2 + "', comment='" + note + "'";

            Conn con = new Conn();
            con.s.executeUpdate(query);
            
            JOptionPane.showMessageDialog(null, "Marks Updated Successfully for Roll No: " + rollno);
            fetchStudentFeedback(rollno); // Refresh feedback view
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error saving marks for Roll No: " + rollno);
            e.printStackTrace();
        }
    }

    // New method to save teacher's reply
    private void submitTeacherReply(String rollno, String reply) {
         try {
            String query = "UPDATE marks SET teacher_reply = '" + reply + "' WHERE rollno = '" + rollno + "'";
            Conn con = new Conn();
            int rowsAffected = con.s.executeUpdate(query);
            
            if (rowsAffected > 0) {
                 JOptionPane.showMessageDialog(null, "Reply submitted successfully to Roll No: " + rollno);
            } else {
                 JOptionPane.showMessageDialog(null, "Marks record not found. Please submit marks first.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error submitting reply for Roll No: " + rollno);
            e.printStackTrace();
        }
    }
    
    // New method for CSV upload logic
    private void uploadMarksFromCSV(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            int successCount = 0;
            int errorCount = 0;
            
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                // Simple CSV parsing: split by comma
                String[] data = line.split(","); 
                
                if (data.length >= 5) {
                    // Trim whitespace from all data fields
                    String rollno = data[0].trim();
                    String sub1 = data[1].trim();
                    String m1 = data[2].trim();
                    String sub2 = data[3].trim();
                    String m2 = data[4].trim();
                    
                    try {
                        // Use a simplified note for bulk upload
                        submitMarks(rollno, sub1, m1, sub2, m2, "Marks updated via bulk CSV upload."); 
                        successCount++;
                    } catch (Exception e) {
                        errorCount++;
                    }
                } else {
                    errorCount++;
                }
            }
            JOptionPane.showMessageDialog(null, "CSV Upload Complete.\nSuccessful Updates: " + successCount + "\nErrors/Skipped Lines: " + errorCount);

        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error: CSV file '" + filename + "' not found in the project directory.");
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An unexpected error occurred during CSV processing.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new EnterMarks();
    }
}
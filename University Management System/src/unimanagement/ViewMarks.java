package unimanagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ViewMarks extends JFrame implements ActionListener {
    
    String rollno;
    JLabel lblsub1, lblmark1, lblsub2, lblmark2, lblcomment, lblTeacherReply;
    JTextArea taComment; 
    JButton update, cancel;
    
    ViewMarks(String rollno) {
        this.rollno = rollno;
        
        setSize(500, 550);
        setLocation(500, 150);
        setLayout(null);
        
        JLabel heading = new JLabel("Your Examination Results");
        heading.setBounds(100, 30, 300, 30);
        heading.setFont(new Font("Tahoma", Font.BOLD, 22));
        add(heading);

        JLabel lblRoll = new JLabel("Roll Number: " + rollno);
        lblRoll.setBounds(50, 80, 200, 20);
        lblRoll.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(lblRoll);

        // Subject 1 & 2 Display... (unchanged)
        JLabel sub1 = new JLabel("Subject 1: ");
        sub1.setBounds(50, 120, 100, 20);
        add(sub1);
        lblsub1 = new JLabel("-");
        lblsub1.setBounds(150, 120, 150, 20);
        add(lblsub1);
        JLabel m1 = new JLabel("Marks: ");
        m1.setBounds(300, 120, 80, 20);
        add(m1);
        lblmark1 = new JLabel("-");
        lblmark1.setBounds(380, 120, 80, 20);
        add(lblmark1);

        JLabel sub2 = new JLabel("Subject 2: ");
        sub2.setBounds(50, 160, 100, 20);
        add(sub2);
        lblsub2 = new JLabel("-");
        lblsub2.setBounds(150, 160, 150, 20);
        add(lblsub2);
        JLabel m2 = new JLabel("Marks: ");
        m2.setBounds(300, 160, 80, 20);
        add(m2);
        lblmark2 = new JLabel("-");
        lblmark2.setBounds(380, 160, 80, 20);
        add(lblmark2);

        // Teacher Comment/Note (Original)
        JLabel tcom = new JLabel("Teacher Note:");
        tcom.setBounds(50, 210, 100, 20);
        add(tcom);
        lblcomment = new JLabel("No comment provided.");
        lblcomment.setBounds(150, 210, 300, 20);
        add(lblcomment);
        
        // --- Teacher Reply Display (New) ---
        JLabel tr = new JLabel("Teacher Reply:");
        tr.setBounds(50, 250, 100, 20);
        tr.setForeground(Color.RED);
        add(tr);
        lblTeacherReply = new JLabel("Awaiting teacher response.");
        lblTeacherReply.setBounds(150, 250, 300, 20);
        add(lblTeacherReply);
        // ----------------------------------
        
        // Student Comment/Feedback Field
        JLabel scom = new JLabel("Your Feedback:");
        scom.setBounds(50, 290, 150, 20);
        add(scom);
        taComment = new JTextArea();
        taComment.setBounds(50, 320, 400, 80);
        taComment.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        taComment.setLineWrap(true);
        taComment.setWrapStyleWord(true);
        add(taComment);

        // Buttons
        update = new JButton("Submit Feedback");
        update.setBounds(100, 430, 150, 30);
        update.setBackground(Color.BLUE);
        update.setForeground(Color.WHITE);
        update.addActionListener(this);
        add(update);
        
        cancel = new JButton("Close");
        cancel.setBounds(280, 430, 100, 30);
        cancel.setBackground(Color.RED);
        cancel.setForeground(Color.WHITE);
        cancel.addActionListener(this);
        add(cancel);
        
        fetchMarks();
        setVisible(true);
    }
    
    public void fetchMarks() {
        try {
            Conn c = new Conn();
            String query = "select * from marks where rollno = '"+rollno+"'";
            ResultSet rs = c.s.executeQuery(query);
            
            if (rs.next()) {
                lblsub1.setText(rs.getString("subject1"));
                lblmark1.setText(rs.getString("marks1"));
                lblsub2.setText(rs.getString("subject2"));
                lblmark2.setText(rs.getString("marks2"));
                lblcomment.setText(rs.getString("comment"));
                
                // Fetch and display teacher reply
                String teacherReply = rs.getString("teacher_reply");
                lblTeacherReply.setText(teacherReply != null && !teacherReply.isEmpty() ? teacherReply : "Awaiting teacher response.");

                // Fetch existing student comment
                String existingComment = rs.getString("student_comment"); 
                if (existingComment != null) {
                    taComment.setText(existingComment);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Marks not yet published.");
                update.setEnabled(false); 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == update) {
            // Update the student_comment column in the marks table
            String comment = taComment.getText();
            try {
                String query = "UPDATE marks SET student_comment = '"+comment+"' WHERE rollno = '"+rollno+"'";
                Conn c = new Conn();
                c.s.executeUpdate(query);
                JOptionPane.showMessageDialog(null, "Feedback submitted successfully!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        setVisible(false);
    }
}
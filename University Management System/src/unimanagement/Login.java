package unimanagement;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class Login extends JFrame implements ActionListener{
    
    JButton login, cancel;
    JTextField tfusername;
    JPasswordField tfpassword;
    JComboBox<String> cbrole; // Added role selection

    Login (){
        
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);
        
        JLabel lblusername = new JLabel("Username");
        lblusername.setBounds(40, 20, 100, 20);
        add(lblusername);
        
        tfusername = new JTextField();
        tfusername.setBounds(150, 20, 150, 20);
        add(tfusername);
        
        JLabel lblpassword = new JLabel("Password");
        lblpassword.setBounds(40, 70, 100, 20);
        add(lblpassword);
        
        tfpassword = new JPasswordField(); // Changed from JTextField to JPasswordField
        tfpassword.setBounds(150, 70, 150, 20);
        add(tfpassword);
        
        // --- Login Role Selection ---
        JLabel lblrole = new JLabel("Login As");
        lblrole.setBounds(40, 120, 100, 20);
        add(lblrole);

        String roles[] = {"Teacher", "Student"}; // Removed Admin
        cbrole = new JComboBox<>(roles);
        cbrole.setBounds(150, 120, 150, 20);
        cbrole.setBackground(Color.WHITE);
        add(cbrole);
        // -----------------------------
        
        login = new JButton("Login");
        login.setBounds(40,170,120,30);
        login.setBackground(Color.BLACK);
        login.setForeground(Color.WHITE);
        login.addActionListener(this);
        login.setFont(new Font("Tahoma", Font.BOLD, 15));
        add(login);
        
        cancel = new JButton("Cancel");
        cancel.setBounds(180,170,120,30);
        cancel.setBackground(Color.BLACK);
        cancel.setForeground(Color.WHITE);
        cancel.addActionListener(this);
        cancel.setFont(new Font("Tahoma", Font.BOLD, 15));
        add(cancel);
        
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/second.jpg"));
        Image i2 = i1.getImage().getScaledInstance(200,200, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel image = new JLabel(i3);
        image.setBounds(350, 0, 200, 200);
        add(image);
        
        setSize(600,300);
        setLocation(400,250);
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent ae){
        if(ae.getSource() == login){
            String username = tfusername.getText();
            String password = new String(tfpassword.getPassword()); // Correctly get password from JPasswordField
            String role = (String) cbrole.getSelectedItem(); // Get selected role
            
            // Query checks username, password AND role
            String query = "select * from login where username='"+username+"' and password='"+password+"' AND account_type='"+role+"'";
            
            try{
                Conn c = new Conn();
                ResultSet rs = c.s.executeQuery(query);
                
                if (rs.next()){
                    setVisible(false);
                    // Pass the role and username to the main project frame
                    new Project(role, username); 
                }else{
                    JOptionPane.showMessageDialog(null, "Invalid username, password, or role.");
                    // Keep the login window visible on failure
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        
        }else if (ae.getSource() == cancel){
            setVisible(false);
            System.exit(0); // Exit the application
        }
    }
    
    public static void main(String[] args){
        new Login();
    }
    
}
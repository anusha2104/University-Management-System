package unimanagement;

import java.sql.*;

public class Conn {
    
    Connection c;
    Statement s;

    Conn () {
        try {
            // Update this line with your MySQL connector path if needed
            Class.forName("com.mysql.cj.jdbc.Driver"); 
            // !!! CHANGE YOUR DATABASE NAME AND CREDENTIALS HERE !!!
            c = DriverManager.getConnection("jdbc:mysql:///universitymanagementsystem", "root", "Nahipata");
            s = c.createStatement();
        
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
}
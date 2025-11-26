package university.management.system;

import javax.swing.*;
import java.awt.*;

public class Splash extends JFrame implements Runnable {
    
    Thread t;
    Splash(){
        
        ImageIcon i1;
        i1 = new ImageIcon(ClassLoader.getSystemResource("icons/first.jpg"));
        Image i2 = i1.getImage().getScaledInstance(750,330, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel image = new JLabel(i3);
        add(image);
        
        t = new Thread(this);
        t.start();
        
        
        setVisible(true);
        setLocation(350,250);
        setSize(750,330);
        
    }
    
    public void run(){
        try{
            Thread.sleep(4000);
            setVisible(false);
            
            new Login();
        } catch (Exception e){
        
        } 
    }
    
    public static void main(String[] args){
        new Splash();
         
    }
}

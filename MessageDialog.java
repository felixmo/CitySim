import javax.swing.JOptionPane;
import java.lang.Thread;

/**
 * Write a description of class MessageDialog here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MessageDialog  
{
    public MessageDialog(final String message) {
        
        // Dialog is shown on a seperate thread so it does not block Greenfoot
        new Thread() {
            public void run() {
                JOptionPane.showMessageDialog(null, message);
            }
        }.start();
    }
}

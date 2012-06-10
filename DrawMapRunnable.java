/**
 * Write a description of class DrawMapRunnable here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DrawMapRunnable extends CSRunnable
{

    public DrawMapRunnable() {
        super();
    }

    public void run() {
        Map.getInstance().draw();
    }
}

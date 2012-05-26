/**
 * Write a description of class ZoneAgeDBUpdateThread here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ZoneAgeDBUpdateThread extends CSThread
{
    private int age = 0;

    public ZoneAgeDBUpdateThread(int age) {
        super("");
        this.age = age;
    }
    
    public void run() {
        
        DataSource.getInstance().increaseZoneAge(age);
    }
}

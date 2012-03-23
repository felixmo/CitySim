import java.util.TimerTask;

/**
 * TimeIncrementor
 * CitySim
 * v0.1
 * 
 * Created by Felix Mo on 02-16-2012
 * 
 * 
 * 
 */

public class TimeIncrementor extends TimerTask
{
    private Time time;
    
    public TimeIncrementor(Time time) {
        this.time = time;
    }
   
    public void run() {
        time.increment();
    }
}

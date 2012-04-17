import java.util.TimerTask;

/**
 * DateIncrementor
 * CitySim
 * v0.1
 * 
 * Created by Felix Mo on 02-16-2012
 * 
 * 
 * 
 */

public class DateIncrementor extends TimerTask
{

    // ---------------------------------------------------------------------------------------------------------------------
    
    /*
     * INSTANCE VARIABLES *
     */
    
    private Date date;
    
    // ---------------------------------------------------------------------------------------------------------------------

    public DateIncrementor(Date date) {
        this.date = date;
    }

    public void run() {
        date.increment();
    }
}

import java.util.Timer;
import java.util.logging.*;

/**
 * Date
 * CitySim
 * v0.1
 * 
 * Created by Felix Mo on 02-17-2012
 * 
 * Data structure for dates
 * 
 */

public class Date  
{
    // ---------------------------------------------------------------------------------------------------------------------

    /*
     * REFERENCES *
     */
    private City city;
    private Timer timer;

    /*
     * INSTANCE VARIABLES *
     */

    // Date values
    private int days;
    private int months;
    private int years;

    private Logger logger = LogManager.getLogManager().getLogger("com.felixmo.CitySim.logger");     // Shared logger

    // ---------------------------------------------------------------------------------------------------------------------

    public Date(City city, int days, int months, int years) {
        this.city = city;

        this.days = days;
        this.months = months;
        this.years = years;
    }

    /*
     * PUBLIC METHODS *
     */
    
    // Called to increment date by 1 day
    public void increment() {
        days++;

        if (days == 31) {
            months++;
            days = 0;
        }

        if (months == 12) {
            years++;
            months = 0;
        }

        city.didIncrementDate();
    }

    /*
     * TIME *
     */
    
    // Starts tracking time
    public void start() {
        logger.info("Timer has begun...");

        timer = new Timer();
        timer.schedule(new DateIncrementor(this), 0, 1000);
    }

    // Stops tracking time
    public void stop() {
        logger.info("Timer has stopped.");

        timer.cancel();
        timer = null;
    }

    /*
     * ACCESSORS *
     */

    public int days() {
        return days;
    }

    public int months() {
        return months;
    }

    public int years() {
        return years;
    }

    // ---------------------------------------------------------------------------------------------------------------------

    // Returns a string representation of the date with the specified format
    public String toString() {

        String daysString;
        String monthsString;
        String yearsString;

        if (days < 10) {
            daysString = "0" + days;
        }
        else {
            daysString = Integer.toString(days);
        }

        if (months < 10) {
            monthsString = "0" + months;
        }
        else {
            monthsString = Integer.toString(months);
        }

        if (years < 10) {
            yearsString = "0" + years;
        }
        else {
            yearsString = Integer.toString(years);
        }

        return monthsString + "/" + daysString + "/" + yearsString;
    }
}
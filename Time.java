import java.util.Timer;
import java.util.TimerTask;
import java.lang.Integer;

/**
 * Time
 * CitySim
 * v0.1
 * 
 * Created by Felix Mo on 02-16-2012
 * 
 * Data structure for time and provides time related services
 * 
 */

public class Time
{
    private City city;
    
    private Timer timer;
    private Date date;
    
    private int minutes;
    private int hours;
    private boolean AM = true;
    
    public Time(City city, int minutes, int hours, int days, int months, int years) {
        this.city = city;
        this.minutes = minutes;
        this.hours = hours;
        if (hours >= 12) {
            AM = false;
        }
        this.date = new Date(days, months, years);
    }
    
    public void increment() {
        minutes++;
        
        if (minutes == 60) {
            hours++;
            minutes = 0;
        }
        
        if (hours == 12) {
            AM = false;
        }
        else if (hours == 24) {
            date.increment();
            hours = 0;
        }
        
        city.didIncrementTime();
    }
    
    public void start() {
        System.out.println("Timer has begun...");
        
        timer = new Timer();
        timer.schedule(new TimeIncrementor(this), 0, 1000);
    }
    
    public void stop() {
        System.out.println("Timer has stopped.");
        
        timer.cancel();
        timer = null;
    }
    
    public String toString() {
        String hoursString;
        String minutesString;
        
        if (hours < 10) {
            hoursString = "0" + hours;
        }
        else {
            hoursString = Integer.toString(hours);
        }
        
        if (minutes < 10) {
            minutesString = "0" + minutes;
        }
        else {
            minutesString = Integer.toString(minutes);
        }
        
        return hoursString + ":" + minutesString + " " + (AM ? "AM" : "PM");
    }
    
    public int minutes() {
        return minutes;
    }
    
    public int hours() {
        return hours;
    }
    
    public Date date() {
        return date;
    }
}

/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import java.util.Timer;

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
    private static Timer timer;

    /*
     * INSTANCE VARIABLES *
     */

    // Date values
    private static int days;
    private static int months;
    private static int years;

    // ---------------------------------------------------------------------------------------------------------------------

    public static void set(int d, int m, int y) {

        days = d;
        months = m;
        years = y;
    }

    /*
     * PUBLIC METHODS *
     */
    
    // Called to increment date by 1 day
    public static void increment() {
        days++;

        if (days == 31) {
            months++;
            days = 0;
        }

        if (months == 12) {
            years++;
            months = 0;
            new FinancesThread().start();
        }

        City.getInstance().didIncrementDate();
    }

    /*
     * TIME *
     */
    
    // Starts tracking time
    public static void start() {
        CSLogger.sharedLogger().info("Timer has begun...");

        timer = new Timer();
        timer.schedule(new DateIncrementor(), 0, 1000);
    }

    // Stops tracking time
    public static void stop() {
        CSLogger.sharedLogger().info("Timer has stopped.");

        timer.cancel();
        timer = null;
    }

    /*
     * ACCESSORS *
     */

    public static int days() {
        return days;
    }

    public static int months() {
        return months;
    }

    public static int years() {
        return years;
    }

    /**
     * Returns the days (seconds, in reality) since the CitySim epoch (day 0)
     */
    public static int timeSinceEpoch() {
        
        return ((years * 12) * 31) + (months * 31) + days;        
    }
       
    // ---------------------------------------------------------------------------------------------------------------------

    // Returns a string representation of the date with the specified format
    public static String asString() {

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
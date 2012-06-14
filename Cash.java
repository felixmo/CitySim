/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import java.text.DecimalFormat;

/**
 * Cash
 * CitySim
 * v0.1
 * 
 * Created by Felix Mo on 04-10-2012
 * 
 * Data model representing cash in the game; holds the current value of the player's avaliable cash
 * 
 */
public class Cash  
{

    // ---------------------------------------------------------------------------------------------------------------------

    private static Integer value = 0;
    private static int change = 0;
    
    /*
     * CONSTANTS *
     */
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,##0");
    
    // ---------------------------------------------------------------------------------------------------------------------
    
    public static void set(int x) {
        value = x;
    }

    public static void add(int change) {
        value += change;
    }

    public static void subtract(int change) {
        if ((value - change) < 0) {
            new MessageDialog("Insufficient funds.");
        }
        value -= change;
    }

    public static String asString() {
        return DECIMAL_FORMAT.format(value).toString();
    }
    
    /*
     * ACCESSORS *
     */

    public static int value() {
        return value;
    }
}

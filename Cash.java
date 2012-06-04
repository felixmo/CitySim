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
    
    /*
     * CONSTANTS *
     */
    private static final DecimalFormat decimalFormat = new DecimalFormat("#,##0");
    
    // ---------------------------------------------------------------------------------------------------------------------

    public static void set(int x) {
        value = x;
    }

    public static void add(int change) {
        value += change;
    }

    public static void subtract(int change) {
        value -= change;
    }

    public static String asString() {
        return decimalFormat.format(value).toString();
    }
    
    /*
     * ACCESSORS *
     */

    public static int value() {
        return value;
    }
}

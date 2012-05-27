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
    
    /*
     * INSTANCE VARIABLES *
     */
    private Integer value = 0;
    
    /*
     * CONSTANTS *
     */
    private final DecimalFormat decimalFormat = new DecimalFormat("#,##0");
    
    // ---------------------------------------------------------------------------------------------------------------------

    public Cash(int value) {
        this.value = value;
    }

    public void addValue(int change) {
        this.value += change;
    }

    public void subtractValue(int change) {
        this.value -= change;
    }

    public String toString() {
        return decimalFormat.format(value).toString();
    }
    
    /*
     * ACCESSORS *
     */

    public int value() {
        return value;
    }
}

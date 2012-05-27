/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import java.lang.Integer;

/**
 * Write a description of class TileSelectorEvent here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TileSelectorEvent extends CSEvent
{
 
    private int type = 0;

    public TileSelectorEvent(String message) {
        super(message);
        
        this.type = Integer.parseInt(message);
    }   
    
    /*
     * ACCESSORS *
     */
    
    public int type() {
        return this.type;
    }
}

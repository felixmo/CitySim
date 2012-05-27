/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

/**
 * Write a description of class CSEvent here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class CSEvent  
{
    
    private final String message;
    
    public CSEvent(String message) {
        this.message = message;
    }
    
    public String message() {
        return this.message;
    }
}

/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Write a description of class CSLogger here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CSLogger
{
    private final static Logger sharedLogger = LoggerFactory.getLogger("com.felixmo.CitySim");          

    // Logging is done via a global instance of 'Logger' rather than through CSLogger so that the caller can be logged as well
    public static Logger sharedLogger() {
        return sharedLogger;
    }
}
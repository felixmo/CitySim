/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import java.util.logging.*;

/**
 * Write a description of class CSLogger here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CSLogger
{
    private static Logger sharedLogger;              

    static {

        if (sharedLogger == null) {
            // Configure logging

            LogManager manager = LogManager.getLogManager();
            manager.reset();

            ConsoleHandler handler = new ConsoleHandler();
            handler.setLevel(Level.FINE);
            handler.setFormatter(new CSLogFormatter());

            sharedLogger = Logger.getLogger("com.felixmo.CitySim.logger");
            sharedLogger.addHandler(handler);
            sharedLogger.setLevel(Level.FINE);
        }
    }

    // Logging is done via a global instance of 'Logger' rather than through CSLogger so that the caller can be logged as well
    public static Logger sharedLogger() {
        return sharedLogger;
    }
}
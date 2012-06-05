/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

/**
 * Write a description of class KillPowerDBUpdateThread here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class KillPowerDBUpdateThread extends CSThread
{
    public KillPowerDBUpdateThread() {
        super("KillPowerDBUpdateThread");
    }

    public void run() {
        DataSource.getInstance().killPower();
    }
}

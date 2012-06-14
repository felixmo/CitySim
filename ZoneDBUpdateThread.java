/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

/**
 * Write a description of class ZonePrimaryAllocationDBUpdateThread here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ZoneDBUpdateThread extends CSThread 
{
    private Zone zone;

    public ZoneDBUpdateThread(Zone zone) {
        super("ZoneDBUpdateThread");
        this.zone = zone;
    }
    
    public void run() {
        DataSource.getInstance().updateZone(this.zone);
    }
}

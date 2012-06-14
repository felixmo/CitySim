/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

/**
 * Write a description of class PowerZoneDBUpdateThread here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class PowerZoneDBUpdateThread extends CSThread
{
    private Zone zone;

    public PowerZoneDBUpdateThread(Zone zone) {
        super("PowerZoneDBUpdateThread");
        this.zone = zone;
    }

    public void run() {
        DataSource.getInstance().powerZone(this.zone);
    }
}

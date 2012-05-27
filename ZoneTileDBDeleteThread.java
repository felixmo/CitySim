/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

/**
 * Write a description of class ZoneDBDeleteThread here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ZoneTileDBDeleteThread extends CSThread
{

    private int zoneID;

    public ZoneTileDBDeleteThread(int zoneID) {
        super("");
        this.zoneID = zoneID;
    }

    public void run() {
        DataSource.getInstance().deleteZoneTileWithID(this.zoneID);
    }
}

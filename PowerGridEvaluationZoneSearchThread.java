/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

/**
 * Write a description of class PowerGridEvaluationZoneSearchThread here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class PowerGridEvaluationZoneSearchThread extends CSThread
{
    private Zone zone;

    public PowerGridEvaluationZoneSearchThread(Zone zone) {
        super("PowerGridEvaluationZoneSearchThread");
        this.zone = zone;
    }
        
    public void run() {
        PowerGrid.searchZone(this.zone);
    }
}

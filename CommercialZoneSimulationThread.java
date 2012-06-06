/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

/**
 * Write a description of class CommercialZoneSimulationThread here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CommercialZoneSimulationThread extends CSThread  
{
    private CommercialZone zone;
    
    public CommercialZoneSimulationThread(CommercialZone zone) {
        super("CommercialZoneSimulationThread");
        this.zone = zone;
    }
    
    public void run() {
        zone.simulate();
    }
}


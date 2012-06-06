/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

/**
 * Write a description of class IndustrialZoneSimulationThread here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class IndustrialZoneSimulationThread extends CSThread  
{
    private IndustrialZone zone;
    
    public IndustrialZoneSimulationThread(IndustrialZone zone) {
        super("IndustrialZoneSimulationThread");
        this.zone = zone;
    }
    
    public void run() {
        zone.simulate();
    }
}

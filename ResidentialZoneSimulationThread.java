/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

/**
 * Write a description of class ResidentialZoneSimulationThread here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ResidentialZoneSimulationThread extends CSThread  
{
    private ResidentialZone zone;
    
    public ResidentialZoneSimulationThread(ResidentialZone zone) {
        super("ResidentialZoneSimulationThread");
        this.zone = zone;
    }
    
    public void run() {
        zone.simulate();
    }
}

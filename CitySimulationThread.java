/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import java.util.HashMap;
import java.awt.Point;

/**
 * Write a description of class CitySimulationThread here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CitySimulationThread extends CSThread
{
    public CitySimulationThread() {
        super("");
    }

    public void run() {
        CSLogger.sharedLogger().info("Began simulating the city...");
        long startTime = System.currentTimeMillis();

        DataSource.getInstance().resetJobAllocations();

        ResidentialZone[] rZones = Data.residentialZones();
        CSLogger.sharedLogger().fine("Simulating " + rZones.length + " residential zones");
        for (ResidentialZone zone : rZones) {
            new ResidentialZoneSimulationThread(zone).start();
            //             zone.simulate();
        }

        CommercialZone[] cZones = Data.commercialZones();
        CSLogger.sharedLogger().fine("Simulating " + cZones.length + " commercial zones");

        for (CommercialZone zone : cZones) {
            new CommercialZoneSimulationThread(zone).start();
            //             zone.simulate();
        }

        IndustrialZone[] iZones = Data.industrialZones();
        //         CSLogger.sharedLogger().info("Simulating " + iZones.length + " industrial zones");

        for (IndustrialZone zone : iZones) {
            new IndustrialZoneSimulationThread(zone).start();
            //             zone.simulate();
        }

        int population = 0;
        for (ResidentialZone zone : Data.residentialZones()) {
            //             new ResidentialZoneSimulationThread(zone).start();
            population += zone.allocation();
        }
        Population.set(population);

        CSLogger.sharedLogger().info("Finished simulating the city (" + (System.currentTimeMillis() - startTime) + " ms)");
    }
}
